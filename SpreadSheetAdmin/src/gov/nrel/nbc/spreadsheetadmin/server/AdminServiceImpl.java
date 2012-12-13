package gov.nrel.nbc.spreadsheetadmin.server;

import gov.nrel.nbc.spreadsheetadmin.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheetadmin.client.AdminService;
import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.NameValue;
import gov.nrel.nbc.spreadsheetadmin.client.SheetCellHeaders;
import gov.nrel.nbc.spreadsheetadmin.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.DataFormatDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.MetadataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.RowDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.SetupInfoDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.SheetConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.SheetDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.DataType;
import gov.nrel.nbc.spreadsheetadmin.dto.DataFormat;
import gov.nrel.nbc.spreadsheetadmin.dto.Metadata;
import gov.nrel.nbc.spreadsheetadmin.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.RowData;
import gov.nrel.nbc.spreadsheetadmin.dto.SetupInfo;
import gov.nrel.nbc.spreadsheetadmin.dto.SetupUrls;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetData;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookData;
import gov.nrel.nbc.spreadsheetadmin.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheetadmin.parse.DelimitedFileParser;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelParser;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelSXParser;
import gov.nrel.nbc.spreadsheetadmin.utilities.XLogger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdminServiceImpl extends RemoteServiceServlet implements
		AdminService, AppConstants {

	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final Logger log = Logger.getLogger(AdminServiceImpl.class);

	private static final long serialVersionUID = -3015212326197607550L;

	/**
	 * Public method to get a data type id using its description.
	 * 
	 * @param description String
	 * @return Long DataType ID.
	 */
	public Long getTypeId(String description) {
		Session session = null;
		long id = 0;
		try {
			session = HibernateSessionFactory.getSession();
			DataTypeDAOHibernate dth = new DataTypeDAOHibernate();
			dth.setSession(session);

			Transaction tx = session.beginTransaction();
			
			DataType dt = dth.findByDescription(description);
			id = dt.getId();

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return id;
	}
	
	/**
	 * Public method to set the list of MetaDataHeaders for a given WorkbookConfig.
	 * 
	 * @param metaDatas List<NameValue>
	 * @param wbConfigName String
	 * @return Boolean - success?
	 */
	public Boolean setMetaDataHeaders(List<NameValue> metaDatas, String wbConfigName) {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			DataTypeDAOHibernate dth = new DataTypeDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			dfdh.setSession(session);
			dth.setSession(session);
			wdh.setSession(session);
			mhdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			WorkbookConfig workbook = wdh.findBySynonym(wbConfigName);
			if (workbook != null) {
				Iterator<NameValue> sit = metaDatas.iterator();
				while (sit.hasNext()) {
					NameValue nv = sit.next();
					MetadataHeader header = null;
					String name = nv.getName().trim();
					String synonym = name;
					name = removeBadCharacters(name);
					header = mhdh.findByNameAndConfig(workbook, name);
					int ctr = 1;
					while (header != null) {
						name = name+String.valueOf(ctr++);
						header = mhdh.findByNameAndConfig(workbook, name);						
					}
					if (header == null) {
						header = new MetadataHeader();
						header.setName(nv.getName());
						header.setSynonym(synonym);
					}
					header.setInternal(nv.isInternal());
					header.setHdr_order(nv.getOrder());
					long typeId = nv.getDataType();
					DataType dt = dth.findById(typeId);
					header.setTypeId(dt);
					header.setWorkbook_config_id(workbook);
					if (dt != null && (dt.getDescription().equals("DATE")||dt.getDescription().equals("REAL"))) {
						String format = nv.getDataFormat();
						DataFormat dformat = dfdh.findByFormat(format);
						if (dformat != null) {
							header.setData_format(dformat);
						}
					}
					session.saveOrUpdate(header);
				}
			}

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
			String stack = AdminServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return true;
	}
	
	/**
	 * Public method to set a single MetaDataHeader for a WorkbookConfig.
	 * 
	 * @param metaData NameValue
	 * @param wbConfigName String
	 * @return Boolean - success?
	 */
	public Boolean setMetaDataHeader(NameValue metaData, String wbConfigName) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			DataTypeDAOHibernate dth = new DataTypeDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			dfdh.setSession(session);
			dth.setSession(session);
			wdh.setSession(session);
			mhdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			WorkbookConfig workbook = wdh.findByName(wbConfigName);
			if (workbook != null) {
				MetadataHeader header = null;
				String name = metaData.getName().trim();
				//String synonym = name;
				//name = removeBadCharacters(name);
				header = mhdh.findByNameAndConfig(workbook, name);
				/*
				int ctr = 1;
				while (header != null) {
					name = name+String.valueOf(ctr++);
					header = mhdh.findByNameAndConfig(workbook, name);					
				}
				*/
				if (header == null) {
					header = new MetadataHeader();
					header.setName(removeBadCharacters(name));
					header.setSynonym(name);
				}
				header.setHdr_order(metaData.getOrder());
				long typeId = metaData.getDataType();
				DataType dt = dth.findById(typeId);
				header.setTypeId(dt);
				header.setWorkbook_config_id(workbook);
				if (dt.getDescription().equalsIgnoreCase(DATE_TYPE)) {
					String format = metaData.getDataFormat();
					DataFormat dformat = dfdh.findByDesc(format);
					if (dformat != null) {
						header.setData_format(dformat);
					}
				} else if (dt.getDescription().equalsIgnoreCase(REAL_TYPE)) {
					String format = metaData.getDataFormat();
					DataFormat dformat = dfdh.findByDesc(format);
					if (dformat != null) {
						header.setData_format(dformat);
					}
				}
				session.saveOrUpdate(header);
				Set<SheetConfig> shConfigs = workbook.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					if (shConfig != null)
						alterDataTable(metaData, wbConfigName, shConfig.getSheet_name());
				}
			}

			tx.commit();
		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			ret = false;
			log.warn("Exception caught: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return ret;
	}
	
   /** This methods validates a data entry
   *
   * @param code The data to be validated
   *
   * @return Returns true if in valid format, else returns false
   */
  public static String removeBadCharacters(String data) {
	  //log.info("in hasBadCharacters");
	  CharSequence replacement = String.valueOf('_');
		  for (int i=0;i<data.length();i++)
		  {
			  CharSequence cs = String.valueOf(data.charAt(i));
			  if (BADTABLECHARS.contains(cs)) {
				  //log.info(data+" contains invalid characters");
				  data = data.replace(cs, replacement);
			  }
		  }

     return data;
  }
	/**
	 * Public method to set a list of CellDataHeaders for a given WorkbookConfig and SheetConfig.
	 * 
	 * @param cellDatas List<NameValue>
	 * @param wbConfigName String
	 * @param shConfigName String
	 * @return Boolean - success?
	 */
	public Boolean setCellDataHeaders(List<NameValue> cellDatas, String wbConfigName, String shConfigName) {
		if(cellDatas==null || wbConfigName.equals(null) || shConfigName.equals(null) || cellDatas.isEmpty() || wbConfigName.isEmpty() || shConfigName.isEmpty()){
			log.warn("hit null");
			return false;
		}
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			DataTypeDAOHibernate dth = new DataTypeDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			dfdh.setSession(session);
			chdh.setSession(session);
			sdh.setSession(session);
			dth.setSession(session);
			wdh.setSession(session);
			mhdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			WorkbookConfig workbook = wdh.findBySynonym(wbConfigName);
			if (workbook != null) {
				wbConfigName = workbook.getConfig_name();
				List<MetadataHeader> metas = mhdh.findByConfig(workbook);
				Set<SheetConfig> sheets = workbook.getSheetConfigs();
				Iterator<SheetConfig> sit = sheets.iterator();
				while (sit.hasNext()) {
					SheetConfig config = sit.next();
					if (config.getSynonym().equals(shConfigName)) {
						shConfigName = config.getSheet_name();
						Iterator<NameValue> cit = cellDatas.iterator();
						while (cit.hasNext()) {
							NameValue nv = cit.next();
							CellDataHeader cellHeader = null;
							String name = nv.getName().trim();
							String synonym = name;
							name = removeBadCharacters(name);
							int ctr = 1;
							while (inList(metas,name)) {
								name = name+String.valueOf(ctr++);
							}
							cellHeader = chdh.findByNameAndConfig(config, name);
							while (cellHeader != null) {
								name = name+String.valueOf(ctr++);
								cellHeader = chdh.findByNameAndConfig(config, name);
							}
							nv.setName(name);
							nv.setSynonym(synonym);
							saveCellHeader(session, cellHeader, name, synonym, config, nv, dth, dfdh);
						}
						session.saveOrUpdate(config);
					}
				}
			}
			
			createDataTable(session, cellDatas, wbConfigName, shConfigName);

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return true;
	}
		
	private void alterDataTable(NameValue nv,
			String wbConfigName, String shConfigName) {
		Connection conn = null;
		Statement stmt = null;
		try {			
			String sql = "";
			String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
			/*
			alter table employees
		    -> change empid empid varchar(100);
			 */
			sql = "alter table `"+table+"` change ";
			
			String name = nv.getName().trim();
			int mtype = (int)nv.getDataType();
			String sformat = nv.getDataFormat();
			long format = 50;
			try {
				format = Long.parseLong(sformat.substring(0, 1));
			} catch (NumberFormatException nfe) {
				log.warn("unable to parse: "+sformat);
			}
			String type = "";
							
			switch (mtype) {
				case LONG: type = " BIGINT(20) NULL DEFAULT NULL";break;
				case BOOLEAN: type = " BIT NULL DEFAULT NULL";break;
				case DATE: type = " DATETIME NULL DEFAULT NULL";break;
				case REAL: type = " DOUBLE NULL DEFAULT NULL"; break;
				case STRING: type = " VARCHAR("+format+") NULL DEFAULT NULL";break;
			}
			sql += "`"+ name +"` `"+ name +"` " + type;

			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
	        log.info("alter table sql="+sql);
	        int ret = stmt.executeUpdate(sql);
			log.info("return from alter="+ret);
			
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
	}

	/**
	 *  construct data table name
	 *  
	 *   @param wbConfig name of the workbook config
	 *   @param shConfig name of the sheet config
	 *   @return table name of the data table
	 */
	public static String getDataTableName(String wbConfig, String shConfig) {
		return wbConfig.toLowerCase()+"_"+shConfig.toLowerCase()+DATA_TABLE_ENDING;
	}
	
	public Boolean alterDataTableForMetaData(List<NameValue> headers, String wbConfigName, String shConfigName) {
			Connection conn = null;
			Statement stmt = null;
			String sql = "";
			String table = AdminServiceImpl.getDataTableName(wbConfigName,shConfigName);
			// Load the database driver
			try {
				Class.forName( MYSQL_DRIVER ) ;
				// Get a connection to the database
		        String url = FIRST_PART_DB;
		        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
		        	url += DevTestProdConstants.DEV;
		        else
		        	url += DevTestProdConstants.DEV_PROD_TEST;
		        url += LAST_PART_DB;
				String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
				String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
				boolean first = true;
				String last = "sheet_config_id";
				Iterator<NameValue> hit = headers.iterator();
				while (hit.hasNext()) {
					NameValue nv = hit.next();
					String name = nv.getName().trim();
					int mtype = (int)nv.getDataType();
					String sformat = nv.getDataFormat();
					long format = 50;
					try {
						format = Long.parseLong(sformat.substring(0, 1));
					} catch (NumberFormatException nfe) {
						log.warn("unable to parse: "+sformat);
					}
					String type = "";
									
					switch (mtype) {
						case LONG: type = " BIGINT(20) NULL DEFAULT NULL";break;
						case BOOLEAN: type = " BIT NULL DEFAULT NULL";break;
						case DATE: type = " DATETIME NULL DEFAULT NULL";break;
						case REAL: type = " DOUBLE NULL DEFAULT NULL"; break;
						case STRING: type = " VARCHAR("+format+") NULL DEFAULT NULL";break;
					}
					//ALTER TABLE `james32_city wages_data`  ADD COLUMN `Submission Date` 
					// DATE NOT NULL AFTER `sheet_config_id`;
					sql = "ALTER TABLE `"+table+"` ADD COLUMN ";
					sql += "`"+ name +"` " + type;
					if (first) {
						first = false;
					}
					sql += " AFTER "+"`"+ last +"` ";
					last = name;
			        log.info("alter table sql="+sql);
			        conn = DriverManager.getConnection(url,user,pass);
			        stmt = conn.createStatement();
			        log.info("create table sql="+sql);
			        int ret = stmt.executeUpdate(sql);
					log.info("return from alter="+ret);
				}
			} catch (Exception e1) {
				log.warn("Exception caught: "+e1.getMessage());
			}
			finally {
				try {
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					log.error("Exception caught: "+e.getMessage());
				}
			}
			return true;
	}

	private void createDataTable(Session session, List<NameValue> cellDatas, String wbConfigName, String shConfigName) {
		log.info("in createDataTable");
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		try {
			session = HibernateSessionFactory.getSession();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			SheetDataDAOHibernate sddh = new SheetDataDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			sddh.setSession(session);
			wdh.setSession(session);
			mhdh.setSession(session);

			session.beginTransaction();
			
			String sql = "";
			String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
			sql = "create table `"+table+"` ("
				+ " cell_data_id BIGINT(20) NOT NULL AUTO_INCREMENT"
				+ ", row_data_id BIGINT(20) NOT NULL"
				+ ", sheet_config_id BIGINT(20) NOT NULL";
			
			WorkbookConfig workbook = wdh.findByName(wbConfigName);
			if (workbook != null) {
				List<MetadataHeader> metas = mhdh.findByConfig(workbook);
				Iterator<MetadataHeader> mit = metas.iterator();
				while (mit.hasNext()) {
					MetadataHeader meta = mit.next();
					int mtype = (int)meta.getTypeId().getId();
					DataFormat df = meta.getData_format();
					String sformat = null;
					if (df != null)
						sformat = df.getFormat();
					long format = 50;
					if (sformat != null) {
						try {
							if (sformat != null) 
								if (mtype == STRING)
									format = Long.parseLong(sformat);
								//else if (mtype == REAL)
								//	format = Long.parseLong(sformat.substring(0,1));
						} catch (NumberFormatException nfe) {
							log.warn("unable to parse: "+sformat);
						}
					}
					String type = "";
					
					switch (mtype) {
						case LONG: type = " BIGINT(20) NULL DEFAULT NULL";break;
						case BOOLEAN: type = " BIT NULL DEFAULT NULL";break;
						case DATE: type = " DATETIME NULL DEFAULT NULL";break;
						case REAL: type = " DOUBLE NULL DEFAULT NULL"; break;
						case STRING: type = " VARCHAR("+format+") NULL DEFAULT NULL";break;
					}
					sql += ", `"+ meta.getName() +"` " + type;
				}
				Set<SheetConfig> sheets = workbook.getSheetConfigs();
				Iterator<SheetConfig> sit = sheets.iterator();
				while (sit.hasNext()) {
					SheetConfig config = sit.next();
					if (config.getSheet_name().equals(shConfigName)) {
						Iterator<NameValue> cit = cellDatas.iterator();
						while (cit.hasNext()) {
							NameValue nv = cit.next();
							String name = nv.getName().trim();
							int mtype = (int)nv.getDataType();
							String sformat = nv.getDataFormat();
							long format = 50;
							try {
								if (sformat != null) 
									if (mtype == STRING)
										format = Long.parseLong(sformat);
									//else
										//format = Long.parseLong(sformat.substring(0,1));
							} catch (NumberFormatException nfe) {
								log.warn("unable to parse: "+sformat);
							}
							String type = "";
							
							switch (mtype) {
								case LONG: type = " BIGINT(20) NULL DEFAULT NULL";break;
								case BOOLEAN: type = " BIT NULL DEFAULT NULL";break;
								case DATE: type = " DATETIME NULL DEFAULT NULL";break;
								case REAL: type = " DOUBLE NULL DEFAULT NULL"; break;
								case STRING: type = " VARCHAR("+format+") NULL DEFAULT NULL";break;
							}
							sql += ", `"+ name +"` " + type;
						}
					}
				}
			}
			sql +=   ", PRIMARY KEY (`cell_data_id`),"
					+" INDEX `sheet_config_id` (`sheet_config_id`),"
					+" INDEX `row_data_id` (`row_data_id`)"
					+")";			
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
	        stmt1 = conn.createStatement();
	        log.info("create table sql="+sql);
	        int ret = stmt.executeUpdate(sql);
			log.info("return from create table="+ret);
			sql = "insert into data_tables (table_name) values ('"+table+"')";
	        log.info("insert data_tables sql="+sql);
			ret = stmt1.executeUpdate(sql);
			log.info("return from insert into data_tables="+ret);
			
		}  catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
			String stack = AdminServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (stmt1 != null)
					stmt1.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
	}

	private void saveCellHeader(Session session, CellDataHeader cellHeader, String name, String synonym, SheetConfig config, NameValue cellData, DataTypeDAOHibernate dth, DataFormatDAOHibernate dfdh) {
		if (cellHeader == null) {
			cellHeader = new CellDataHeader();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			int id = chdh.getNextIndexValue();
			cellHeader.setHdr_index(id);
			cellHeader.setName(name);
			cellHeader.setSynonym(synonym);
			cellHeader.setSheet_config_id(config);
		}
		long typeId = cellData.getDataType();
		//log.info("trying to find DataType with "+typeId);
		DataType dt = dth.findById(typeId);
		cellHeader.setTypeId(dt);
		cellHeader.setHdr_index(cellData.getOrder());
		if (dt != null && dt.getDescription().equals("DATE")) {
			String format = cellData.getDataFormat();
			DataFormat dformat = dfdh.findByFormat(format);
			if (dformat != null) {
				cellHeader.setData_format(dformat);
			} else
				log.warn("unable to find format for "+format);
		} else if (dt != null && dt.getDescription().equals("REAL")) {
			String format = cellData.getDataFormat();
			DataFormat dformat = dfdh.findByFormat(format);
			if (dformat != null) {
				cellHeader.setData_format(dformat);
			} else
				log.warn("unable to find format for "+format);
		} else if (dt == null) {
			log.warn("DataType was null for "+cellHeader.getName());
		}

		session.saveOrUpdate(cellHeader);
		Set<CellDataHeader> cds = config.getCell_hdrs();
		Iterator<CellDataHeader> cit = cds.iterator();
		boolean found = false;
		while (cit.hasNext()) {
			CellDataHeader cdh = cit.next();
			if (cdh.getName().equals(cellHeader.getName())) {
				config.getCell_hdrs().remove(cdh);
				config.getCell_hdrs().add(cellHeader);
				found = true;
				break;
			}
		}
		if (!found)
			config.getCell_hdrs().add(cellHeader);
		session.saveOrUpdate(config);
	}
	/**
	 * Public method to set a specific CellDataHeader for a given WorkbookConfig and SheetConfig.
	 * 
	 * @param cellData NameValue
	 * @param wbConfigName String
	 * @param shConfigName String
	 * @return Boolean - success?
	 */
	public Boolean setCellDataHeader(NameValue cellData, String wbConfigName, String shConfigName) {
		if(cellData==null || wbConfigName.equals(null) || shConfigName.equals(null) || cellData.getName().isEmpty() || wbConfigName.isEmpty() || shConfigName.isEmpty()){
			return false;
		}
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			DataTypeDAOHibernate dth = new DataTypeDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			dfdh.setSession(session);
			chdh.setSession(session);
			sdh.setSession(session);
			dth.setSession(session);
			wdh.setSession(session);
			mhdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			WorkbookConfig workbook = wdh.findByName(wbConfigName);
			if (workbook != null) {
				Set<SheetConfig> sheets = workbook.getSheetConfigs();
				Iterator<SheetConfig> sit = sheets.iterator();
				while (sit.hasNext()) {
					SheetConfig config = sit.next();
					if (config.getSheet_name().equals(shConfigName)) {
						CellDataHeader cellHeader = null;
						String name = cellData.getName().trim();
						String synonym = name;
						name = removeBadCharacters(name);
						cellHeader = chdh.findByNameAndConfig(config, name);
						saveCellHeader(session, cellHeader, name, synonym, config, cellData, dth, dfdh);
						alterDataTable(cellData, wbConfigName, shConfigName);
						//break;
					}
					session.saveOrUpdate(config);
				}
			}

			tx.commit();
		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			ret = false;
			log.warn("Exception caught: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return ret;
	}
	
	/**
	 *  construct data table name
	 *  
	 *   @param wbConfig name of the workbook config
	 *   @param shConfig name of the sheet config
	 *   @return table name of the data table
	 */
	public static String getTableName(String wbConfig, String shConfig) {
		return wbConfig.toLowerCase()+"_"+shConfig.toLowerCase()+DATA_TABLE_ENDING;
	}
	
	/**
	 * Public method to create a SheetConfig with its corresponding information.
	 * 
	 * @param specs List<NameValue>
	 * @param wbConfigName String
	 * @param sheetName String
	 * @return String Sheet name
	 */
	public String setSheetConfig(List<NameValue> specs, String wbConfigName, String sheetName) {
		Session session = null;
		String synonym = sheetName;
		String wbSyn = wbConfigName;
		long hdr_row = 0;
		long hdr_col = 0;
		long data_row = 0;
		long data_col = 0;
		long meta_start_row = 0;
		long meta_start_col = 0;
		long meta_end_row = 0;
		long meta_end_col = 0;
		try {
			session = HibernateSessionFactory.getSession();
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			SheetConfigDAOHibernate scdh = new SheetConfigDAOHibernate();
			scdh.setSession(session);
			wdh.setSession(session);
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			wbConfigName = removeBadCharacters(wbConfigName);
			WorkbookConfig workbook = wdh.findByName(wbConfigName);
			if (workbook == null) {
				workbook = new WorkbookConfig();
				workbook.setSynonym(wbSyn);
				workbook.setConfig_name(wbConfigName);
			}
			sheetName = removeBadCharacters(sheetName);
			SheetConfig sheetConfig = null;//wdh.findSheetByConfigAndName(workbook, sheetName);
			Set<SheetConfig> sheetConfigs = workbook.getSheetConfigs();
			Iterator<SheetConfig> scit = sheetConfigs.iterator();
			while (scit.hasNext()) {
				SheetConfig sc = scit.next();
				if (sc.getSheet_name().equals(sheetName)) {
					sheetConfig = sc;
					break;
				}
			}
			if (sheetConfig == null) 
				sheetConfig = wdh.findSheetByConfigAndName(workbook, sheetName);
			Iterator<NameValue> sit = specs.iterator();
			while (sit.hasNext()) {
				NameValue nv = sit.next();
				if (nv.getName().equals(HDR_ROW)) {
					try {
						hdr_row = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing hdr_row = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(HDR_COL)) {
					try {
						hdr_col = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing hdr_col = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(DATA_ROW)) {
					try {
						data_row = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing data_row = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(DATA_COL)) {
					try {
						data_col = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing data_col = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(META_START_ROW)) {
					try {
						meta_start_row = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing meta_start_row = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(META_START_COL)) {
					try {
						meta_start_col = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing meta_start_col = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(META_END_ROW)) {
					try {
						meta_end_row = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing meta_end_row = "+nv.getValue());
					}
				}
				else if (nv.getName().equals(META_END_COL)) {
					try {
						meta_end_col = Long.parseLong(nv.getValue());
					} catch (NumberFormatException nfe) {
						log.warn("Exception parsing meta_end_col = "+nv.getValue());
					}
				}
			}
			if (sheetConfig == null) {
				sheetConfig = new SheetConfig();
				//long id = scdh.getNextId();
				//sheetConfig.setSheet_config_id(id);
				sheetConfig.setSheet_name(sheetName);
				sheetConfig.setSynonym(synonym);
				sheetConfig.setHdr_row(hdr_row);
				sheetConfig.setHdr_col(hdr_col);
				sheetConfig.setData_row(data_row);
				sheetConfig.setData_col(data_col);
				sheetConfig.setMeta_start_col(meta_start_col);
				sheetConfig.setMeta_start_row(meta_start_row);
				sheetConfig.setMeta_end_col(meta_end_col);
				sheetConfig.setMeta_end_row(meta_end_row);
				session.saveOrUpdate(sheetConfig);
			}
			Set<SheetConfig> configs = workbook.getSheetConfigs();
			boolean found = false;
			Iterator<SheetConfig> cit = configs.iterator();
			while (cit.hasNext() && !found) {
				SheetConfig config = cit.next();
				if (config.getSheet_config_id()==sheetConfig.getSheet_config_id()) {
					found = true;
					break;
				}
			}
			if (!found)
				workbook.getSheetConfigs().add(sheetConfig);
			if (sheetConfig.getMeta_end_row() > 0)
				workbook.getMetadata_sheet().add(sheetConfig);
			session.saveOrUpdate(workbook);
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on adding new sheetConfig to workbook. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
			String stack = AdminServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return synonym;
	}

	/**
	 * Method to get internal metadata headers
	 * 
	 * @param filename String
	 * @param worksheet String
	 * 
	 * @return List<String> A list of header strings
	 */
	public List<String> getInternalMetadataHeaders(String wbConfigName, String filename, String worksheet) {
		List<String> headers = new ArrayList<String>();
		File f = null;
		Session session = null;
		try {
			f = new File(filename);
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wch = new WorkbookConfigDAOHibernate();
			wch.setSession(session);

			Transaction tx = session.beginTransaction();

			//String path = filename.replace(DOUBLE_BACK_SLASH, FORWARD_SLASH);
			WorkbookConfig wbConfig = wch.findBySynonym(wbConfigName);//wch.findByFile(path);
			if (wbConfig == null) {
				log.warn("Workbook config was null for name="+wbConfigName);
				return null;
			}
			List<NameValue> nvHeaders = new ArrayList<NameValue>();
			if (filename.endsWith(EXCEL97)) {
				ExcelParser parser = new ExcelParser(f, wbConfig, session);
				nvHeaders = parser.getMetadataNames(worksheet);
			} else if (filename.endsWith(EXCEL07)) {
				ExcelSXParser parser = new ExcelSXParser(f, wbConfig, session);
				nvHeaders = parser.getMetadataNames(worksheet);
			} else  if (filename.endsWith(EXCELMACRO)) {
				ExcelSXParser parser = new ExcelSXParser(f, wbConfig, session);
				nvHeaders = parser.getMetadataNames(worksheet);
			} else  if (filename.endsWith(CSV)) {
				DelimitedFileParser parser = new DelimitedFileParser(f);
				parser.setDELIMITER(COMMA_DELIMITER);
				parser.setWbConfig(wbConfig);
				parser.setSession(session);
				nvHeaders = parser.getMetadataNames(worksheet);
			} else  if (filename.endsWith(TAB)) {
				DelimitedFileParser parser = new DelimitedFileParser(f);
				parser.setDELIMITER(TAB_DELIMITER);
				parser.setWbConfig(wbConfig);
				parser.setSession(session);
				nvHeaders = parser.getMetadataNames(worksheet);
			} 
			Iterator<NameValue> nit = nvHeaders.iterator();
			while (nit.hasNext()) {
				NameValue nv = nit.next();
				headers.add(nv.getName());
			}
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Error accessing spreadsheet file to disk! File: "
					+ f.getPath() + "; Error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return headers;
  	}
	
	
	/**
	 * Method to get data formats
	 * 
	 * @param type String - Type of data (REAL_TYPE, DATE_TYPE)
	 * @return <List<NameValue>> A list of data formats
	 */
	public List<NameValue> getDataFormats(String type) {
		List<NameValue> formats = new ArrayList<NameValue>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			dfdh.setSession(session);

			Transaction tx = session.beginTransaction();
			List<DataFormat> dfs = dfdh.findAll();
			Iterator<DataFormat> dit = dfs.iterator();
			while(dit.hasNext()) {
				DataFormat df = dit.next();
				if (df.getType().equals(type)) {
					NameValue nv = new NameValue();
					nv.setName(df.getFormat());
					nv.setValue(df.getDescription());
					formats.add(nv);
				}
			}

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting formats. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Error accessing date formats "
					+ "; Error: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return formats;
  	}
	
	/**
	 * Method to get cell headers
	 * 
	 * @param filename String
	 * @param sheet String
	 * @return SheetCellHeaders A list of cell headers
	 */
	public SheetCellHeaders getCellHeaders(String wbConfigName, String filename, String sheet) {
		log.info("in getCellHeaders with filename="+filename+" and sheet="+sheet);
		List<String> headers = new ArrayList<String>();
		SheetCellHeaders sch = new SheetCellHeaders();
		File f = null;
		Session session = null;
		try {
			f = new File(filename);
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wch = new WorkbookConfigDAOHibernate();
			wch.setSession(session);

			Transaction tx = session.beginTransaction();

			//String path = filename.replace(DOUBLE_BACK_SLASH, FORWARD_SLASH);
			WorkbookConfig wbConfig = wch.findBySynonym(wbConfigName);//wch.findByFile(path);
			if (wbConfig == null) {
				log.warn("Workbook config was null for name="+wbConfigName);
				return null;
			}
			if (filename.endsWith(EXCEL97)) {
				ExcelParser parser = new ExcelParser(f, wbConfig, session);
				headers = parser.getHeaders(sheet);
			} else if (filename.endsWith(EXCEL07)) {
				ExcelSXParser parser = new ExcelSXParser(f, wbConfig, session);
				headers = parser.getHeaders(sheet);
			} else  if (filename.endsWith(EXCELMACRO)) {
				ExcelSXParser parser = new ExcelSXParser(f, wbConfig, session);
				headers = parser.getHeaders(sheet);
			} else  if (filename.endsWith(CSV)) {
				DelimitedFileParser parser = new DelimitedFileParser(f);
				parser.setDELIMITER(COMMA_DELIMITER);
				parser.setWbConfig(wbConfig);
				parser.setSession(session);
				headers = parser.getHeaders(sheet);
			} else  if (filename.endsWith(TAB)) {
				DelimitedFileParser parser = new DelimitedFileParser(f);
				parser.setDELIMITER(TAB_DELIMITER);
				parser.setWbConfig(wbConfig);
				parser.setSession(session);
				headers = parser.getHeaders(sheet);
			} 
			if(!headers.isEmpty()){
				sch.setHeaders(headers);
				sch.setSheet(sheet);
				tx.commit();
			} else
				session.getTransaction().rollback();
				
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Error accessing spreadsheet file to disk! File: "
					+ f.getPath() + "; Error: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return sch;
  	}

	/**
	 * Method to get metadata headers by type (INTERNAL, EXTERNAL, ALL)
	 * 
	 * @param config_name String
	 * @param type int - (INTERNAL, EXTERNAL, ALL)
	 * @return <List<String>> A list of header strings
	 */
	public List<NameValue> getMetaDataHeaders(String config_name, int type) {
		Session session = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate ctdh = new WorkbookConfigDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			mdh.setSession(session);
			ctdh.setSession(session);

			Transaction tx = session.beginTransaction();

			WorkbookConfig config = ctdh.findByName(config_name);

			List<MetadataHeader> mdhs = mdh.findByConfig(config);
			Iterator<MetadataHeader> it = mdhs.iterator();
			while (it.hasNext()) {
				MetadataHeader header = it.next();
				if (type == ALL || (type == EXTERNAL && !header.isInternal()) || (type == INTERNAL && header.isInternal())) {
					NameValue nv = new NameValue();
					nv.setName(header.getName());
					DataType dt = header.getTypeId();
					if (dt != null)
						nv.setDataType(dt.getId());
					if (header.getData_format() != null)
						nv.setDataFormat(header.getData_format().getFormat());
					nv.setOrder(header.getHdr_order());
					headers.add(nv);
				}
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			log.error(AdminServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return headers;
	}

	/**
	 * A private method to determine if the header string exists in the list
	 *  of NameValues.
	 *  
	 * @param headers List<NameValue>
	 * @param header String
	 * @return boolean - found?
	 */
	private boolean contains(List<NameValue> headers, String header) {
		boolean found = false;
		Iterator<NameValue> nit = headers.iterator();
		while (nit.hasNext()&&!found) {
			NameValue nv = nit.next();
			if (nv.getName().equals(header)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * A private method to determine if the header string exists in the list
	 *  of NameValues.
	 *  
	 * @param headers List<NameValue>
	 * @param header String
	 * @return boolean - found?
	 */
	private boolean inList(List<MetadataHeader> headers, String header) {
		boolean found = false;
		Iterator<MetadataHeader> nit = headers.iterator();
		while (nit.hasNext()&&!found) {
			MetadataHeader nv = nit.next();
			if (nv.getName().equals(header)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * Method to get cell data headers
	 * 
	 * @param wbConfigName String
	 * @param shConfigName String
	 * @return List<NameValue> A list of headers
	 */
	public List<NameValue> getCellDataHeaders(String wbConfigName, String shConfigName) {
		Session session = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		try {
			session = HibernateSessionFactory.getSession();

			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			sdh.setSession(session);
			ctdh.setSession(session);
			wcdh.setSession(session);

			Transaction tx = session.beginTransaction();

			List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig config = wcdh.findByName(wbConfigName);
				configs.add(config);
			} else if (shConfigName != null && !shConfigName.isEmpty()) {
				WorkbookConfig config = wcdh.findBySheetAndName(shConfigName, wbConfigName);
				configs.add(config);
			} else {
				configs = wcdh.findAll();
			}
			
			Iterator<WorkbookConfig> wit = configs.iterator();
			while (wit.hasNext()) {
				WorkbookConfig config = wit.next();
				String wbConfig = config.getConfig_name();
				
				List<SheetConfig> sheets = new ArrayList<SheetConfig>();
				if (shConfigName != null && !shConfigName.isEmpty()) {
					sheets.add(wcdh.findSheetByConfigAndName(config, shConfigName));
				}
				else {
					sheets.addAll(config.getSheetConfigs());
				}
				Iterator<SheetConfig> sit = sheets.iterator();
				while (sit.hasNext()) {
					SheetConfig sheet = sit.next();
					String shConfig = sheet.getSheet_name();
					List<NameValue> cellHeaders = sdh.findBySheet(wbConfig,shConfig);
					if (cellHeaders != null)
						log.info("found "+cellHeaders.size()+" cellHeaders");
					else
						log.info("cellHeaders was null");
					if (cellHeaders != null) {
						Iterator<NameValue> cit = cellHeaders.iterator();
						while (cit.hasNext()) {
							NameValue header = cit.next();
							//log.info("got header="+header.getName());
							if (!contains(headers,header.getName())) {
								NameValue nv = new NameValue();
								nv.setName(header.getName());
								nv.setDataType(header.getDataType());
								if (header.getDataFormat() != null)
									nv.setDataFormat(header.getDataFormat());
								nv.setOrder(header.getOrder());
								//log.info("adding header="+nv.getName());
								headers.add(nv);
							}
						}
					}
				}
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			log.error(getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		log.info("returning "+headers.size()+" headers");
		return headers;
	}

	/**
	 * Public method to determine if the workbook configuration name already
	 *  exists in the database.
	 *  
	 *  @param name String - WorkbookConfig name
	 *  @return Boolean - found?
	 */
	public Boolean workbookConfigExists(String name) {
		Session session = null;
		Boolean found = false;

		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate wdh = new WorkbookConfigDAOHibernate();
			wdh.setSession(session);

			Transaction tx = session.beginTransaction();
			WorkbookConfig config = wdh.findBySynonym(name);
			if (config != null) 
				found = true;
			
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = AdminServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = AdminServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return found;
	}

	/**
	 * Public method to get the URLs of the navigation panel links.
	 * 
	 * @return List<NameValue>
	 */
	public List<NameValue> getUrls() {
		List<NameValue> urls = new ArrayList<NameValue>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			SetupInfoDAOHibernate sidh = new SetupInfoDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			List<SetupInfo> infos = sidh.findAll();
			Iterator<SetupInfo> iit = infos.iterator();
			while (iit.hasNext()) {
				SetupInfo info = iit.next();
				if (info.isDisplayUrls()) {
					Set<SetupUrls> surls = info.getUrls();
					Iterator<SetupUrls> sit = surls.iterator();
					while (sit.hasNext()) {
						SetupUrls url = sit.next();
						NameValue nv = new NameValue();
						nv.setName(url.getName());
						nv.setValue(url.getUrl());
						urls.add(nv);
					}
				}
			}
			
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting urls. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		
		return urls;
	}

	/**
	 * Public method to get the title text for the URLs.
	 * 
	 * @return String
	 */
	public String getTitle() {
		String title="";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			SetupInfoDAOHibernate sidh = new SetupInfoDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			List<SetupInfo> infos = sidh.findAll();
			Iterator<SetupInfo> iit = infos.iterator();
			while (iit.hasNext()) {
				SetupInfo info = iit.next();
				title = info.getTitle();
			}
			
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		
		return title;
	}

	/**
	 * Method to get sheet configs names
	 * 
	 * @param wbConfigName String
	 * @return List<String> - A list of sheet config names
	 */
	public List<String> getSheetConfigs(String wbConfigName) {
		Session session = null;
		List<String> configs = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();

			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			sdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			List<SheetConfig> shConfigs = new ArrayList<SheetConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig wbConfig = wcdh.findByName(wbConfigName);
				shConfigs.addAll(wbConfig.getSheetConfigs());
			}
			else
				shConfigs = sdh.findAll();
			
			Iterator<SheetConfig> scit = shConfigs.iterator();
			while (scit.hasNext()) {
				SheetConfig shConfig = scit.next();
				if (!configs.contains(shConfig.getSheet_name()))
					configs.add(shConfig.getSheet_name());
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return configs;
	}

	/**
	 * Method to get WorkbookConfig names
	 * 
	 * @return <List<String>> A list of WorkbookConfig names
	 */
	public List<String> getWorkbookConfigs() {
		Session session = null;
		List<String> configs = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate ctdh = new WorkbookConfigDAOHibernate();
			ctdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			configs = ctdh.findAllNames();

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return configs;
	}
	
	/**
	 * Public method to get all of the DataTypes
	 * 
	 * @return List<NameValue>
	 */
	public List<NameValue> getDataTypes() {
		Session session = null;
		List<NameValue> configs = null;
		try {
			session = HibernateSessionFactory.getSession();

			DataTypeDAOHibernate ctdh = new DataTypeDAOHibernate();
			ctdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			List<DataType> dataTypes = ctdh.findAll();
			
			configs = new ArrayList<NameValue>();
			
			if (dataTypes != null) {
				Iterator<DataType> it = dataTypes.iterator();
				while (it.hasNext()) {
					DataType dataType = it.next();
					NameValue nv = new NameValue();
					nv.setName(dataType.getDescription());
					nv.setDataType(dataType.getId());
					configs.add(nv);
				}
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting data types. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return configs;
	}
	
	/**
	 * Public method to get a delete workbook.
	 * 
	 * @param workbook_id - id of the workbook to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteWorkbook(long workbook_id) {
		Boolean ret = true;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			WorkbookDataDAOHibernate wbdh = new WorkbookDataDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			rddh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookData workbook = wbdh.findById(workbook_id, true);
			String wbConfig = workbook.getWorkbook_config_id().getConfig_name();
			Set<SheetData> sheets = workbook.getSheets();
			Iterator<SheetData> sit = sheets.iterator();
			while (sit.hasNext()) {
				SheetData sd = sit.next();
				String shConfig = sd.getSheet_config_id().getSheet_name();
				long sid = sd.getSheet_config_id().getSheet_config_id();
				String sql = "delete from `"+AdminServiceImpl.getTableName(wbConfig, shConfig)+
						     "` where sheet_config_id = "+sid+" and row_data_id in (";
				List<RowData> rows = rddh.findBySheet(sd);
				int ctr=0;
				Iterator<RowData> rit = rows.iterator();
				while (rit.hasNext()) {
					RowData rd = rit.next();
					long rid = rd.getId();
					if (ctr>0)
						sql += ",";
					sql += rid;
					ctr++;
					session.delete(rd);
				}
				sql += ")";
				log.info("delete sql = "+sql);
				int ret1 = stmt.executeUpdate(sql);
				log.info("deleted "+ret1+" rows");
				session.delete(sd);
			}
			if (workbook != null) {
				session.delete(workbook);
			} else
				ret = false;
			tx.commit();

		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting workbook. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete workbook data.
	 * 
	 * @param workbook_id - id of the workbook data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteWorkbookData(long id) {
		return deleteWorkbook(id);
	}

	/**
	 * private method to get a delete a column header from the cell_data table
	 * 
	 * @param id - id of the meta data to delete
	 */
	private void dropMetaOrCellHeader(long id, int M_or_C,Session session) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			wcdh.setSession(session);
			chdh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig wbConfig = null;
			if (M_or_C==META) {
				MetadataHeader wbc = wbdh.findById(id, false);
				wbConfig = wbc.getWorkbook_config_id();
				String wbConfigName = wbConfig.getConfig_name();
				Set<SheetConfig> shConfigs = wbConfig.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while(sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					String shConfigName = shConfig.getSheet_name();
					String sql = "";
					String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
					sql = "alter table `"+table+"` drop column `"+wbc.getName()+"`";
			        log.info("alter table sql="+sql);
			        int ret1 = stmt.executeUpdate(sql);
					log.info("return from alter="+ret1);				
				}
			} else {
				CellDataHeader cdh = chdh.findById(id, false);
				SheetConfig shConfig = cdh.getSheet_config_id();
				String shConfigName = shConfig.getSheet_name();
				wbConfig = wcdh.findBySheet(shConfig);
				String wbConfigName = wbConfig.getConfig_name();
				String sql = "";
				String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
				sql = "alter table `"+table+"` drop column `"+cdh.getName()+"`";
		        log.info("alter table sql="+sql);
		        int ret1 = stmt.executeUpdate(sql);
				log.info("return from alter="+ret1);				
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
	}

	/**
	 * private method to get a delete a column header from the cell_data table
	 * 
	 * @param id - id of the meta data to delete
	 */
	private void dropCellHeaders(SheetConfig shConfig,Session session) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			wcdh.setSession(session);
			chdh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig wbConfig = null;
			String shConfigName = shConfig.getSheet_name();
			wbConfig = wcdh.findBySheet(shConfig);
			String wbConfigName = wbConfig.getConfig_name();
			String sql = "";
			String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
			sql = "delete from table `"+table+"`";
			int ret1 = stmt.executeUpdate(sql);
			log.info("return from delete="+ret1);				
			
			sql = "drop table `"+table+"`";
		    log.info("dtop table sql="+sql);
		    ret1 = stmt.executeUpdate(sql);
			log.info("return from drop="+ret1);				
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
	}

	/**
	 * private method to get a delete a column header from the cell_data table
	 * 
	 * @param id - id of the meta data to delete
	 */
	private void dropWorkbookTable(WorkbookConfig wbConfig, Session session) {
		log.info("in dropWorkbookTable");
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			SheetDataDAOHibernate sddh = new SheetDataDAOHibernate();
			sddh.setSession(session);
			wcdh.setSession(session);
			chdh.setSession(session);
			wbdh.setSession(session);
			
			Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
			Iterator<SheetConfig> sit = sheets.iterator();
			while (sit.hasNext()) {
				SheetConfig shConfig = sit.next();
				String shConfigName = shConfig.getSheet_name();
				wbConfig = wcdh.findBySheet(shConfig);
				String wbConfigName = wbConfig.getConfig_name();
				String sql = "";
				String table = AdminServiceImpl.getTableName(wbConfigName,shConfigName);
				sql = "delete from row_data where row_data_id in "
					+ "(select row_data_id from `"+table+"`)";
				int ret1 = stmt.executeUpdate(sql);
				log.info("return from delete="+ret1);				
				
				sql = "drop table `"+table+"`";
			    log.info("drop table sql="+sql);
			    ret1 = stmt.executeUpdate(sql);
				log.info("return from drop="+ret1);				
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
	}

	/**
	 * Public method to get a delete metadata header.
	 * 
	 * @param id - id of the meta data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteMetaDataHeader(long id) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			wbdh.setSession(session);
			
			MetadataHeader wbc = wbdh.findById(id, true);
			
			if (wbc != null) {
				dropMetaOrCellHeader(id,META,session);
				session.delete(wbc);
			} else
				ret = false;
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting Metadata Header. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete metadata header.
	 * 
	 * @param id - id of the meta data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteExternalMetadataHeaders(String wbConfig) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig config = wcdh.findByName(wbConfig);
			List<MetadataHeader> list = wbdh.findByConfig(config);
			if (list != null) {
				Iterator<MetadataHeader> mit = list.iterator();
				while (mit.hasNext()) {
					MetadataHeader mdh = mit.next();
					dropMetaOrCellHeader(mdh.getMetadata_hdr_id(),META,session);
					session.delete(mdh);
				}
			} else
				ret = false;
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting Metadata Header. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete metadata header.
	 * 
	 * @param id - id of the meta data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteInternalMetadataHeaders(String wbConfig) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			MetadataHeaderDAOHibernate wbdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig config = wcdh.findByName(wbConfig);
			List<MetadataHeader> list = wbdh.findByConfig(config);
			if (list != null) {
				Iterator<MetadataHeader> mit = list.iterator();
				while (mit.hasNext()) {
					MetadataHeader mdh = mit.next();
					dropMetaOrCellHeader(mdh.getMetadata_hdr_id(),META,session);
					session.delete(mdh);
				}
			} else
				ret = false;
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting Metadata Header. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete metadata header.
	 * 
	 * @param id - id of the meta data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteCellDataHeaders(String wbConfigName, String shConfigName) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			CellHeaderDAOHibernate wbdh = new CellHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			SheetDataDAOHibernate sddh = new SheetDataDAOHibernate();
			sddh.setSession(session);
			wcdh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig wbConfig = wcdh.findByName(wbConfigName);
			SheetConfig shConfig = null;
			Set<SheetConfig> set = wbConfig.getSheetConfigs();
			Iterator<SheetConfig> sit = set.iterator();
			boolean found = false;
			while (sit.hasNext() && !found) {
				SheetConfig sc = sit.next();
				if (sc.getSheet_name().equals(shConfigName)) {
					found = true;
					shConfig = sc;
				}
			}
			
			dropCellHeaders(shConfig,session);
			session.delete(shConfig);
			
			/* Don't delete cell headers - deleting sheet will delete headers
			List<CellDataHeader> list = sddh.findBySheet(shConfig);
			if (list != null) {
				Iterator<CellDataHeader> mit = list.iterator();
				while (mit.hasNext()) {
					CellDataHeader mdh = mit.next();
					session.delete(mdh);
				}
			} else
				ret = false;
			 */
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting cell data headers. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete metadata.
	 * 
	 * @param id - id of the meta data to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteMetaData(long id) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			MetadataDAOHibernate wbdh = new MetadataDAOHibernate();
			wbdh.setSession(session);
			
			Metadata wbc = wbdh.findById(id);
			if (wbc != null) {
				dropMetaOrCellHeader(id,META,session);
				session.delete(wbc);
			} else
				ret = false;
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting Metadata. Error: " + he);
			log.error(AdminServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get a delete workbook config.
	 * 
	 * @param workbook_config_id - id of the workbook to delete
	 * @return Boolean - successful?
	 */
	public Boolean deleteWorkbookConfig(String config) {
		log.info("in deleteWorkbookConfig with config="+config);
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			WorkbookConfigDAOHibernate wbdh = new WorkbookConfigDAOHibernate();
			WorkbookDataDAOHibernate wddh = new WorkbookDataDAOHibernate();
			wddh.setSession(session);
			wbdh.setSession(session);
			
			WorkbookConfig wbc = wbdh.findByName(config);
			List<WorkbookData> list = wddh.findByConfig(wbc);

			Iterator<WorkbookData> wit = list.iterator();
			while (wit.hasNext()) {
				WorkbookData wbd = wit.next();
				session.delete(wbd);
			}
			
			if (wbc != null) {
				log.info("wbc not null");
				dropWorkbookTable(wbc,session);
				session.delete(wbc);
			} else {
				ret = false;
				log.info("wbc IS null");
			}
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.error("Hibernate exception on deleting workbook config. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public static method to return a stack trace
	 * 
	 * @param t <Throwable> Exception
	 * @return <String> stack trace
	 */
	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
