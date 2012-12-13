package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.client.FileInfo;
import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.client.SpreadSheetService;
import gov.nrel.nbc.spreadsheet.dao.AttachmentTypeDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.AttachmentsDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.DataFormatDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.OperatorsDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.RowDataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SetupInfoDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookFileDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataFormat;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.GenericValue;
import gov.nrel.nbc.spreadsheet.dto.Metadata;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.SetupInfo;
import gov.nrel.nbc.spreadsheet.dto.SetupUrls;
import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.ValueData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookFileData;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
//import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This is the implementation class for the SpreadSheetService interface. This class
 * implements the methods for searching and exporting data reports.
 * 
 * @author James Albersheim
 * 
 */
public class SpreadSheetServiceImpl extends RemoteServiceServlet implements
		SpreadSheetService, AppConstants, Serializable {

	private static final String DATE = "DATE";

	private static final String BOOLEAN = "BOOLEAN";

	private static final String LONG = "LONG";

	private static final String REAL = "REAL";

	//private static final String REAL_FORMAT = "##0.00";

	private static final String STRING = "STRING";

	//private static final String MM_DD_YYYY = "MM/dd/yyyy";
	
	private static QueryStorage query;
	
	private SpreadSheetUploadServiceImpl susi = null; 
	
	//private MyJdbcDao jds = null;

	/**
	 * XLogger.<OFF.error.warn|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	//private static final XLogger log = new XLogger(SpreadSheetServiceImpl.class);
	private static final Logger log = Logger.getLogger(SpreadSheetServiceImpl.class);

	/**
	 * A serialization identifier.
	 */
	private static final long serialVersionUID = -6913492786893791672L;

	public SpreadSheetServiceImpl() {
		super();
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
		} catch (ClassNotFoundException cnf) {
			log.error("Class for "+MYSQL_DRIVER+" not found. Cannot proceed. Exiting...");
			return;
		}
		String config = "";
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
        	config = "dev";
        else
        	config = DevTestProdConstants.DEV_PROD_TEST;
        config += ".Spring.Config.xml";
        log.info("DEV_PROD_TEST="+DevTestProdConstants.DEV_PROD_TEST);
	    //ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
	    //DataSource ds = (DataSource)ctx.getBean("dataSourceDBDirect");
	    //jds = new MyJdbcDao();
	    //jds.setDataSource(ds);
        susi = new SpreadSheetUploadServiceImpl();
        susi.getProperties();
	}
	/*
	public class MyJdbcDao extends JdbcDaoSupport {
		int columns = 0;
		class Rows extends ArrayList<String> {
			private static final long serialVersionUID = -428108793050108229L;
		}
		@Suppres.warns("unchecked")
		public List<Rows> getAllValues(String sql, int columns) {
	    	this.columns = columns;
	        JdbcTemplate jt = getJdbcTemplate();
	        return (List<Rows>) jt.query ( sql,
	                new RowMapperResultSetExtractor((RowMapper) new ValueRowMapper()));
	    }//getAllValues    

	    // this inner class is invoked for each Row in the ResultSet. It implements
	    // the Spring RowMapper interface, that is used to convert ResultSet records
	    // to Domain Objects. RowMappers are used when the results are not simple.
		@Suppres.warns("unchecked")
		class ValueRowMapper implements RowMapper {
		      public Rows mapRow(ResultSet rs, int index) throws SQLException {
		        Rows rowValues = new Rows();
		        int colCtr=1;
		        if (rs.isClosed()) return rowValues;
		        boolean ret = true;
		        if (index > 0)
		        	ret = rs.absolute(index);
		        else
		        	rs.first();
		        if (ret) {
		        	if (!rs.isBeforeFirst()&&!rs.isAfterLast())
				        while (colCtr < columns) {
				        	rowValues.add(rs.getString(colCtr++));
				        }
		        }
		        return rowValues;
		      }
	    }//class ValueRowMapper
	}
	*/
	/**
	 * Method to get attachments
	 * 
	 * @param id the <Attachments> id
	 * @return <Collection<String>> A list of attachment fileInfo
	 */
	public Collection<FileInfo> getAttachments(long id) {
		Session session = null;
		Collection<FileInfo> files = new ArrayList<FileInfo>();

		try {
			session = HibernateSessionFactory.getSession();

			WorkbookDAOHibernate cddh = new WorkbookDAOHibernate();
			cddh.setSession(session);

			Transaction tx = session.beginTransaction();

			WorkbookData cmd = cddh.findById(id, false);
			Set<Attachments> attachments = cmd.getAttachments();
			Iterator<Attachments> ait = attachments.iterator();
			while (ait.hasNext()) {
				Attachments attachment = ait.next();
				FileInfo info = new FileInfo();
				info.setAttachment_id(String.valueOf(attachment.getAttachment_id()));
				info.setFilename(attachment.getFilename());
				info.setPath(attachment.getPath());
				files.add(info);
			}

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting Tech Names. error: "
							+ he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		if (files != null && files.isEmpty())
			files = null;
		return files;
	}

	/**
	 * Public method to remove an attachment, given an attachment ID
	 *  and its parent sample if it exists.
	 * 
	 * @param attachmentId long <Attachments> ID
	 * @param workbook_id long <WorkbookData> ID. May be zero.
	 * 
	 * @return <Boolean> success or failure to remove
	 */
	public Boolean removeAttachment(long attachmentId, long workbook_id) {
		Session session = null;
		Boolean worked = true;
		try {
			session = HibernateSessionFactory.getSession();
		
			AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
			adh.setSession(session);
			
			WorkbookDAOHibernate sdh = new WorkbookDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			// Update Workbook
			if (workbook_id != 0) {
				WorkbookData workbook = sdh.findById(workbook_id, false);
				if (workbook != null) {
					Set<Attachments> attachments = workbook.getAttachments();
					if (attachments != null && !attachments.isEmpty()) {
						List<Attachments> attachmentsList = new ArrayList<Attachments>(attachments);
						ListIterator<Attachments> it = attachmentsList.listIterator();
						while (it.hasNext()) {
							Attachments attachment = it.next();
							if (attachment.getAttachment_id() == attachmentId) {
								it.remove();
							}
						}
						attachments.clear();
						attachments.addAll(attachmentsList);
					}
					session.saveOrUpdate(workbook);
				}
			}
			
			// Remove file from server and delete row in DB.
			Attachments theAttachment = adh.findById(attachmentId, false);
			
			String path = theAttachment.getPath();
			
			File attachmentFile = new File(path);
			
			FileUtils.deleteQuietly(attachmentFile);
			
			session.delete(theAttachment);
		
			tx.commit();
		} catch (HibernateException he) {
			worked = false;
    		log.error("Hibernate exception: " + he);
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.error("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception ex) {
			worked = false;
    		log.error("Exception caught: " + ex);
    		log.error(SpreadSheetServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
    				if (session.isOpen()) {
    					session.flush();
    					session.close();
    				}
        }
		
		return worked;
	}

	/**
	 * Method to get headers
	 * 
	 * @return <Collection<String>> A list of config strings
	 */
	public List<String> getWorkbookConfigs() {
		Session session = null;
		List<String> configs = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate ctdh = new WorkbookConfigDAOHibernate();
			ctdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			configs = ctdh.findAllSynonyms();

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
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
		}
		return configs;
	}

	/**
	 * Method to get headers
	 * 
	 * @return <Collection<String>> A list of config strings
	 */
	public List<String> getAttachmentExtensions() {
		Session session = null;
		List<String> extensions = null;
		try {
			session = HibernateSessionFactory.getSession();

			AttachmentTypeDAOHibernate ath = new AttachmentTypeDAOHibernate();
			ath.setSession(session);
			
			Transaction tx = session.beginTransaction();

			extensions = ath.findExtensions();

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
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
		}
		return extensions;
	}

	/**
	 * Method to get headers
	 * 
	 * @param wbConfigName String The name of the <WorkbookConfig>. May be null.
	 * @return <Collection<String>> A list of config strings
	 */
	public List<String> getSheetConfigs(String wbConfigName) {
		Session session = null;
		List<String> configs = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();

			SheetDAOHibernate sdh = new SheetDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			sdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			List<SheetConfig> shConfigs = new ArrayList<SheetConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig wbConfig = wcdh.findBySynonym(wbConfigName);
				shConfigs.addAll(wbConfig.getSheetConfigs());
			}
			else
				shConfigs = sdh.findAll();
			
			Iterator<SheetConfig> scit = shConfigs.iterator();
			while (scit.hasNext()) {
				SheetConfig shConfig = scit.next();
				log.info("Config :"+shConfig.getSynonym()+".");
				if (!configs.contains(shConfig.getSynonym()))
					configs.add(shConfig.getSynonym());
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
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
		}
		return configs;
	}

	public String getSampleFileName(String config) {
		String filename = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			Transaction tx = session.beginTransaction();
			WorkbookConfig wc = wcdh.findByName(config);
			Set<WorkbookFileData> files = wc.getWorkbook_file();
			long id = 9999999;
			Iterator<WorkbookFileData> fit = files.iterator();
			while (fit.hasNext()) {
				WorkbookFileData wfd = fit.next();
				if (wfd.getWorkbook_file_id()<id && !wfd.getFilename().startsWith("calc")) {
					id = wfd.getWorkbook_file_id();
					filename = wfd.getFilename();
				}
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
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
		}
		return filename;
	}
	/**
	 * Method to get headers
	 * 
	 * @param wbConfigName String The name of the <WorkbookConfig>. May be null.
	 * @return <Collection<String>> A list of config strings
	 */
	public List<String> getSheetConfigs(String wbConfigName, Session session) {
		//Session session = null;
		List<String> configs = new ArrayList<String>();
		try {
			//session = HibernateSessionFactory.getSession();

			SheetDAOHibernate sdh = new SheetDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			sdh.setSession(session);
			
			//Transaction tx = session.beginTransaction();

			List<SheetConfig> shConfigs = new ArrayList<SheetConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig wbConfig = wcdh.findBySynonym(wbConfigName);
				shConfigs.addAll(wbConfig.getSheetConfigs());
			}
			else
				shConfigs = sdh.findAll();
			
			Iterator<SheetConfig> scit = shConfigs.iterator();
			while (scit.hasNext()) {
				SheetConfig shConfig = scit.next();
				if (!configs.contains(shConfig.getSynonym()))
					configs.add(shConfig.getSynonym());
			}

			//tx.commit();

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
					if (session.isOpen()) {
						//session.flush();
						//session.close();
					}
		}
		return configs;
	}

	/**
	 * Method to get header values
	 * 
	 * @param config_name String the name of the <WorkbookConfig>.
	 * @param header String the name of the <MetadataHeader> whose values are to be retrieved.
	 * @return <Collection<String>> A list of header strings
	 */
	public List<String> getMetaDataValues(String config_name, String header) {
		Session session = null;
		List<String> values = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate ctdh = new WorkbookConfigDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			MetadataDAOHibernate mddh = new MetadataDAOHibernate();
			mddh.setSession(session);
			mhdh.setSession(session);
			ctdh.setSession(session);

			Transaction tx = session.beginTransaction();

			WorkbookConfig config = ctdh.findBySynonym(config_name);
			//MetadataHeader mdh = mhdh.findBySynonymAndConfig(config, header);
			//values = mddh.findDataByHeader(config, mdh);
			List<String> tables = getDataTables(config.getConfig_name());

			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;

			Iterator<String> nit = tables.iterator();
			while (nit.hasNext()) {
				String table = nit.next().toLowerCase();
				ResultSet rs = null;
				// TODO - determine criteria
				String sql = "select distinct(`"+header+"`) from `" + table + "`";// + " where ";
				try {
					rs = stmt.executeQuery( sql ) ;
				} catch (Exception e1) {
					log.warn("exception caught: "+e1.getMessage());
					log.warn("problem with sql="+sql);
				}
				while (rs != null && rs.next()) {
					String value = rs.getString(1);
					if (value != null && !value.isEmpty() && !values.contains(value))
						values.add(value);
				}
			}
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			log.error(SpreadSheetServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return values;
	}

	/**
	 * Method to get headers
	 * 
	 * @param config_name String The name of the <WorkbookConfig>
	 * @return <Collection<String>> A list of header strings
	 */
	public List<NameValue> getMetaDataHeaders(String config_name) {
		Session session = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate ctdh = new WorkbookConfigDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			mdh.setSession(session);
			ctdh.setSession(session);

			Transaction tx = session.beginTransaction();

			WorkbookConfig config = ctdh.findBySynonym(config_name);

			List<MetadataHeader> mdhs = mdh.findByConfig(config);
			Iterator<MetadataHeader> it = mdhs.iterator();
			while (it.hasNext()) {
				MetadataHeader header = it.next();
				NameValue nv = new NameValue();
				nv.setName(header.getName());
				nv.setSynonym(header.getSynonym());
				DataType dt = header.getTypeId();
				if (dt != null)
					nv.setDataType(dt.getId());
				nv.setOrder(header.getHdr_order());
				headers.add(nv);
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			log.error(SpreadSheetServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
		}
		return headers;
	}

	/**
	 * Method to get metadata headers by type 
	 * 
	 * @param config_name String The name of the <WorkbookConfig>
	 * @param type int One of (INTERNAL, EXTERNAL, ALL)
	 * @return <Collection<String>> A list of header strings
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

			WorkbookConfig config = ctdh.findBySynonym(config_name);

			List<MetadataHeader> mdhs = mdh.findByConfig(config);
			Iterator<MetadataHeader> it = mdhs.iterator();
			while (it.hasNext()) {
				MetadataHeader header = it.next();
				if (type == ALL || (type == EXTERNAL && !header.isInternal()) || (type == INTERNAL && header.isInternal())) {
					NameValue nv = new NameValue();
					nv.setName(header.getName());
					nv.setSynonym(header.getSynonym());
					DataType dt = header.getTypeId();
					if (dt != null)
						nv.setDataType(dt.getId());
					nv.setOrder(header.getHdr_order());
					headers.add(nv);
				}
			}

			tx.commit();

		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			log.error(SpreadSheetServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return headers;
	}

	/*
	 * 
	 * A private method to search a list of <NameValue> headers for a particular header
	 * 
	 * @param headers List<NameValue> The list of headers to search
	 * @param header String the header to search for.
	 * @return NameValue the header in the list, null otherwise.
	 */
	private List<NameValue> getHeader(List<NameValue> headers, String header, short DB_OR_SS) {
		List<NameValue> nvs = new ArrayList<NameValue>();
		Iterator<NameValue> nit = headers.iterator();
		while (nit.hasNext()) {
			NameValue nv = nit.next();
			if (DB_OR_SS == DBHEADER) {
				if (nv.getName().equals(header)) {
					nvs.add(nv);
				}
			} else {
				if (nv.getSynonym().equals(header)) {
					nvs.add(nv);
				}				
			}
		}
		return nvs;
	}

	/*
	 * 
	 * A private method to search a list of <NameValue> headers for a particular header
	 * 
	 * @param headers List<NameValue> The list of headers to search
	 * @param header String the header to search for.
	 * @return boolean True if the header is in the list, false otherwise.
	 */
	private boolean contains(List<NameValue> headers, String header, short DB_OR_SS) {
		boolean found = false;
		Iterator<NameValue> nit = headers.iterator();
		while (nit.hasNext()&&!found) {
			NameValue nv = nit.next();
			if (DB_OR_SS == DBHEADER) {
				if (nv.getName().equals(header)) {
					found = true;
					break;
				}
			} else {
				if (nv.getSynonym().equals(header)) {
					found = true;
					break;
				}				
			}
		}
		return found;
	}

	/**
	 * Method to get headers
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @return <List<NameValue>> A list of header strings
	 */
	public List<NameValue> getHeaders(String wbConfigName, String shConfigName) {
		List<NameValue> headers = new ArrayList<NameValue>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			headers.addAll(getMetaHeaders(wbConfigName,shConfigName,session));
			headers.addAll(getCellHeaders(wbConfigName,shConfigName,session));
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		
		return headers;
	}
	/**
	 * Method to get headers
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @return <List<NameValue>> A list of header strings
	 */
	public List<NameValue> getHeaders(String wbConfigName, String shConfigName, Session session) {
		List<NameValue> headers = new ArrayList<NameValue>();
		if (!session.isOpen()) {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
		}
		headers.addAll(getMetaHeaders(wbConfigName,shConfigName,session));
		headers.addAll(getCellHeaders(wbConfigName,shConfigName,session));
		return headers;
	}
	/**
	 * Method to get headers
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @return <List<NameValue>> A list of header strings
	 */
	private List<NameValue> getMetaHeaders(String wbConfigName, String shConfigName, Session session) {
		//Session session = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		try {
			if (!session.isOpen()) {
				session = HibernateSessionFactory.getSession();
				session.beginTransaction();
			}

			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			sdh.setSession(session);
			mdh.setSession(session);
			ctdh.setSession(session);
			wcdh.setSession(session);

			//Transaction tx = session.beginTransaction();

			List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig config = wcdh.findBySynonym(wbConfigName);
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
				List<MetadataHeader> mdhs = mdh.findByConfig(config);
				Iterator<MetadataHeader> mit = mdhs.iterator();
				while (mit.hasNext()) {
					MetadataHeader header = mit.next();
					if (!contains(headers,header.getName(),DBHEADER)) {
						NameValue nv = new NameValue();
						nv.setName(header.getName());
						nv.setSynonym(header.getSynonym());
						nv.setWbName(config.getConfig_name());
						nv.setShName("");
						DataType dt = header.getTypeId();
						if (dt != null)
							nv.setDataType(dt.getId());
						DataFormat df = header.getData_format();
						if (df != null)
							if (df.getFormat() != null)
								nv.setDataFormat(df.getFormat());						
						nv.setOrder(header.getHdr_order());
						headers.add(nv);
					}
				}
			}

			//tx.commit();

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
				if (session.isOpen()) {
					//session.flush();
					//session.close();
				}
		}
		return headers;
	}

	/**
	 * Method to get cell headers
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @return <List<NameValue>> A list of header strings
	 */
	private List<NameValue> getCellHeaders(String wbConfigName, String shConfigName, Session session) {
		//Session session = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		try {
			if (!session.isOpen()) {
				session = HibernateSessionFactory.getSession();
				session.beginTransaction();
			}

			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			sdh.setSession(session);
			mdh.setSession(session);
			ctdh.setSession(session);
			wcdh.setSession(session);

			//Transaction tx = session.beginTransaction();

			List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
			
			if (wbConfigName != null && !wbConfigName.isEmpty()) {
				WorkbookConfig config = wcdh.findBySynonym(wbConfigName);
				configs.add(config);
			} else if (shConfigName != null && !shConfigName.isEmpty()) {
				WorkbookConfig config = wcdh.findBySheetAndSynonym(shConfigName, wbConfigName);
				configs.add(config);
			} else {
				configs = wcdh.findAll();
			}
			
			Iterator<WorkbookConfig> wit = configs.iterator();
			while (wit.hasNext()) {
				WorkbookConfig config = wit.next();
		
				List<SheetConfig> sheets = new ArrayList<SheetConfig>();
				if (config != null && shConfigName != null && !shConfigName.isEmpty()) {
					sheets.add(wcdh.findSheetByConfigAndSynonym(config, shConfigName));
				}
				else {
					if (config != null)
						sheets.addAll(config.getSheetConfigs());
				}
				Iterator<SheetConfig> sit = sheets.iterator();
				while (sit.hasNext()) {
					SheetConfig sheet = sit.next();
					if (sheet != null) {
						List<CellDataHeader> cellHeaders = sdh.findBySheet(sheet);
						if (cellHeaders != null) {
							Iterator<CellDataHeader> cit = cellHeaders.iterator();
							while (cit.hasNext()) {
								CellDataHeader header = cit.next();
								if (!contains(headers,header.getName(),DBHEADER)) {
									NameValue nv = new NameValue();
									nv.setName(header.getName());
									nv.setSynonym(header.getSynonym());
									nv.setWbName(config.getConfig_name());
									nv.setShName(sheet.getSheet_name());
									DataType dt = header.getTypeId();
									if (dt != null) 
										nv.setDataType(dt.getId());
									nv.setOrder(header.getHdr_index());
									DataFormat df = header.getData_format();
									if (df != null)
										if (df.getFormat() != null)
											nv.setDataFormat(df.getFormat());
									headers.add(nv);
								}
							}
						}
					}
				}
			}

			//tx.commit();

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
				if (session.isOpen()) {
					//session.flush();
					//session.close();
				}
		}
		return headers;
	}

	/**
	 * Method to get type based on the selected header
	 * 
	 * @param selection <String> The selected header
	 * @return <String> A type string based on the parameterized types, such as STRING
	 */
	public String getType(String selection) {
		Session session = null;
		String type = "";
		try {
			session = HibernateSessionFactory.getSession();

			if (selection.equals(WORKBOOK_ID)) {
				type = "LONG";
			} else if (selection.equals(ATTACHMENT_EXT)) {
					type = "STRING";
			} else {
				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
				mdh.setSession(session);
				ctdh.setSession(session);

				Transaction tx = session.beginTransaction();

				selection = selection.trim();
				DataType dataType = ctdh.getTypeBySynonym(selection);
				if (dataType == null) {
					dataType = mdh.getTypeByName(selection);
				}
	
				if (dataType != null)
					type = dataType.getDescription();
	
				tx.commit();
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return type;
	}

	/**
	 * Method to get type based on the selected header
	 * 
	 * @param selection <String> The selected header
	 * @return <String> A type string based on the parameterized types, such as STRING
	 */
	/*
	private String getType(String wbConfigName, String shConfigName, String selection, short kind) {
		Session session = null;
		String type = "";
		try {
			session = HibernateSessionFactory.getSession();

			if (selection.equals(WORKBOOK_ID)) {
				type = "LONG";
			} else if (selection.equals(ATTACHMENT_EXT)) {
					type = "STRING";
			} else {
				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
				WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
				SheetDAOHibernate sdh = new SheetDAOHibernate();
				sdh.setSession(session);
				mdh.setSession(session);
				ctdh.setSession(session);
				wcdh.setSession(session);

				session.beginTransaction();

				selection = selection.trim();
				List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
				
				if (wbConfigName != null && !wbConfigName.isEmpty()) {
					WorkbookConfig config = wcdh.findBySynonym(wbConfigName);
					configs.add(config);
				} else if (shConfigName != null && !shConfigName.isEmpty()) {
					WorkbookConfig config = wcdh.findBySheetAndName(shConfigName, wbConfigName);
					configs.add(config);
				} else {
					configs = wcdh.findAll();
				}
				
				Iterator<WorkbookConfig> wit = configs.iterator();
				while (wit.hasNext() && type != null && type.isEmpty()) {
					WorkbookConfig config = wit.next();
					
					if (kind == META) {
						List<MetadataHeader> mdhs = mdh.findByConfig(config);
						Iterator<MetadataHeader> mit = mdhs.iterator();
						while (mit.hasNext()) {
							MetadataHeader header = mit.next();
							if (selection.equals(header.getName())) {
								DataType dt = header.getTypeId();
								type = dt.getDescription();
								break;
							}
						}
					} else {
			
						List<SheetConfig> sheets = new ArrayList<SheetConfig>();
						if (config != null && shConfigName != null && !shConfigName.isEmpty()) {
							sheets.add(wcdh.findSheetByConfigAndName(config, shConfigName));
						}
						else {
							if (config != null)
								sheets.addAll(config.getSheetConfigs());
						}
						Iterator<SheetConfig> sit = sheets.iterator();
						while (sit.hasNext() && type != null && type.isEmpty()) {
							SheetConfig sheet = sit.next();
							if (sheet != null) {
								List<CellDataHeader> cellHeaders = sdh.findBySheet(sheet);
								if (cellHeaders != null) {
									Iterator<CellDataHeader> cit = cellHeaders.iterator();
									while (cit.hasNext() && type != null && type.isEmpty()) {
										CellDataHeader header = cit.next();
										if (selection.equals(header.getName())) {
											DataType dt = header.getTypeId();
											type = dt.getDescription();
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return type;
	}
	*/
	/**
	 * Method to get type based on the selected header
	 * 
	 * @param selection <String> The selected header
	 * @return <String> A type string based on the parameterized types, such as STRING
	 */
	private String getType(String wbConfigName, String shConfigName, String selection, short kind, Session session) {
		//Session session = null;
		String type = "";
		try {
			//session = HibernateSessionFactory.getSession();

			if (selection.equals(WORKBOOK_ID)) {
				type = "LONG";
			} else if (selection.equals(ATTACHMENT_EXT)) {
					type = "STRING";
			} else {
				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
				WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
				SheetDAOHibernate sdh = new SheetDAOHibernate();
				sdh.setSession(session);
				mdh.setSession(session);
				ctdh.setSession(session);
				wcdh.setSession(session);

				//session.beginTransaction();

				selection = selection.trim();
				List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
				
				if (wbConfigName != null && !wbConfigName.isEmpty()) {
					WorkbookConfig config = wcdh.findBySynonym(wbConfigName);
					configs.add(config);
				} else if (shConfigName != null && !shConfigName.isEmpty()) {
					WorkbookConfig config = wcdh.findBySheetAndName(shConfigName, wbConfigName);
					configs.add(config);
				} else {
					configs = wcdh.findAll();
				}
				
				Iterator<WorkbookConfig> wit = configs.iterator();
				while (wit.hasNext() && type != null && type.isEmpty()) {
					WorkbookConfig config = wit.next();
					
					if (kind == META) {
						List<MetadataHeader> mdhs = mdh.findByConfig(config);
						Iterator<MetadataHeader> mit = mdhs.iterator();
						while (mit.hasNext()) {
							MetadataHeader header = mit.next();
							if (selection.equals(header.getName())) {
								DataType dt = header.getTypeId();
								type = dt.getDescription();
								break;
							}
						}
					} else {
			
						List<SheetConfig> sheets = new ArrayList<SheetConfig>();
						if (config != null && shConfigName != null && !shConfigName.isEmpty()) {
							sheets.add(wcdh.findSheetByConfigAndName(config, shConfigName));
						}
						else {
							if (config != null)
								sheets.addAll(config.getSheetConfigs());
						}
						Iterator<SheetConfig> sit = sheets.iterator();
						while (sit.hasNext() && type != null && type.isEmpty()) {
							SheetConfig sheet = sit.next();
							if (sheet != null) {
								List<CellDataHeader> cellHeaders = sdh.findBySheet(sheet);
								if (cellHeaders != null) {
									Iterator<CellDataHeader> cit = cellHeaders.iterator();
									while (cit.hasNext() && type != null && type.isEmpty()) {
										CellDataHeader header = cit.next();
										if (selection.equals(header.getName())) {
											DataType dt = header.getTypeId();
											type = dt.getDescription();
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					//session.flush();
					//session.close();
				}
		}
		return type;
	}

	/**
	 * Method to set all of the <MetaData> items for a given <Workbook>
	 * 
	 * @param metaDatas <Collection<NameValue>> The <MetaData> list.
	 * @param id long <WorkbookData> ID
	 * @return <Boolean> Successful?
	 */
	public Boolean setMetaData(Collection<NameValue> metaDatas, long id) {
		Session session = null;
		boolean worked = true;
		log.info("in setMetaData with id="+id);
		String DB_DATE_FORMAT = "MM/dd/yy";

		try {
			session = HibernateSessionFactory.getSession();

			WorkbookDAOHibernate cmddh = new WorkbookDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			MetadataDAOHibernate mdh = new MetadataDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			rddh.setSession(session);
			cmddh.setSession(session);
			mhdh.setSession(session);
			mdh.setSession(session);

			Transaction tx = session.beginTransaction();

			WorkbookData cmd = cmddh.findById(id, false);

			WorkbookConfig config = cmd.getWorkbook_config_id();
			
			Set<gov.nrel.nbc.spreadsheet.dto.SheetData> sheets = cmd.getSheets();
			Iterator<gov.nrel.nbc.spreadsheet.dto.SheetData> sit = sheets.iterator();
			while (sit.hasNext()) {
				gov.nrel.nbc.spreadsheet.dto.SheetData sheet = sit.next();
				List<gov.nrel.nbc.spreadsheet.dto.RowData> rows = rddh.findBySheet(sheet);
				Iterator<gov.nrel.nbc.spreadsheet.dto.RowData> rit = rows.iterator();
				while(rit.hasNext()) {
					gov.nrel.nbc.spreadsheet.dto.RowData row = rit.next();
					List<MetadataHeader> mdhs = mhdh.findByConfig(config);
					Iterator<MetadataHeader> mit = mdhs.iterator();
					while (mit.hasNext()) {
						MetadataHeader header = mit.next();
						Metadata metadata = mdh.findByHeader(header, cmd);
						Iterator<NameValue> it = metaDatas.iterator();
						while (it.hasNext()) {
							NameValue nv = it.next();
							String name = nv.getName();
							//String synonym = nv.getSynonym();
							String value = nv.getValue();
							ValueData gd = null;
							if (metadata != null) gd = metadata.getValue();
							if (header.getName().equals(name)) {
								String stype = header.getTypeId().getDescription();
								if (stype.equals(STRING)) {
									if (gd != null) gd.setSvalue(value);
								} else if (stype.equals(LONG)) {
									long data = 0;
									try {
										data = Long.parseLong(value);
									} catch (NumberFormatException nfe) {
										log.warn("problems parsing long data = "+value);
									}
									if (gd != null) gd.setLvalue(data);						
								} else if (stype.equals(REAL)) {
									double data = 0;
									try {
										data = Double.parseDouble(value);
									} catch (NumberFormatException nfe) {
										log.warn("problems parsing real data = "+value);
									}
									if (gd != null) gd.setRvalue(data);			
								} else if (stype.equals(BOOLEAN)) {
									boolean data = false;
									try {
										data = Boolean.parseBoolean(value);
									} catch (NumberFormatException nfe) {
										log.warn("problems parsing boolean data = "+value);
									}
									if (gd != null) gd.setBvalue(data);
								} else if (stype.equals(DATE)) {
									DataFormat format = header.getData_format();
									String sformat = DB_DATE_FORMAT;
									if (format != null) {
											sformat = format.getFormat();
									}
									SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
									Date dvalue = null;
									try {
										dvalue = dFormat.parse(value);
									} catch (ParseException pe) {
										log.warn("Invalid date format: "+value);
									}
									if (dvalue != null) {
										Date collectionTime = dvalue;
										GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
										cal.setTime(collectionTime);
										GenericValue gValue = new GenericValue();
										gValue.setDateValue(cal.getTime());
										if (gd != null) gd.setDvalue(gValue.getDateValue());	
									}
								}
		
								if (gd != null) {
									//session.save(gd);
									if (metadata != null) metadata.setValue(gd);
									//session.saveOrUpdate(metadata);
								}
								Connection conn = null;
								Statement stmt = null;
								try {
									// Load the database driver
									Class.forName( MYSQL_DRIVER ) ;
									// Get a connection to the database
							        String url = "";
							        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
							        	url = susi.db_local;
							        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
							        	url = susi.db_dev;
							        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
							        	url += susi.db_test;
							        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
								        url += susi.db_prod;
									String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
									String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
							        conn = DriverManager.getConnection(url,user,pass);
							        stmt = conn.createStatement();
							        String table = SpreadSheetServiceImpl.getTableName(config.getConfig_name(), sheet.getSheet_config_id().getSheet_name());
							        String sql = "";
									if (value != null) {
										sql = "update `"+table+"` set `"+header.getName()+"` = '"+
													value
													+ "' where row_data_id="+row.getId();
									} else {
										sql = "update "+table+" set `"+header.getName()+"` = null where row_data_id="+row.getId();
									}
									//log.info("sql = "+sql);
									//int ret = 
									stmt.executeUpdate( sql ) ;
									//log.info(ret+" rows updated");
								}catch( SQLException se ) {
								      System.out.println( "SQL Exception:" ) ;
									  log.warn(getStackTrace(se));
								      // Loop through the SQL Exceptions
								      //while( se != null )
								      //{
								          System.out.println( "Message: " + se.getMessage()   ) ;
								      //    System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
								      //}
								} finally {
									if (stmt != null)
										stmt.close();
									if (conn != null)
										conn.close();
								}
							}
						}
					}
				}
			}
			Iterator<NameValue> it = metaDatas.iterator();
			while (it.hasNext()) {
				NameValue nv = it.next();
				String name = nv.getName();
				String value = nv.getValue();
				if (name.equals("attachment")) {
					AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
					adh.setSession(session);
					long attachment_id = 0;
					try {
						attachment_id = Long.parseLong(value);
					} catch (NumberFormatException nfe) {
					}
					if (attachment_id > 0) {
						Attachments attachment = adh.findById(attachment_id);
						cmd.getAttachments().add(attachment);
					}
				}
			}
			
			session.saveOrUpdate(cmd);
			
			tx.commit();
		} catch (HibernateException he) {
			worked = false;
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			worked = false;
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return worked;
	}

	/*
	 * Gets the <WorkbookConfig> name, given the <WorkbookConfig> ID
	 * 
	 * @param id long the <WorkbookConfig> ID
	 * @return String the <WorkbookConfig> name
	 */
	public String getWorkbookConfigName(long id) {
		Session session = null;
		String name = null;

		try {
			session = HibernateSessionFactory.getSession();

			WorkbookDAOHibernate wdh = new WorkbookDAOHibernate();
			wdh.setSession(session);

			Transaction tx = session.beginTransaction();
			WorkbookData workbook = wdh.findById(id, false);
			WorkbookConfig config = workbook.getWorkbook_config_id();
			name = config.getConfig_name();
			
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return name;
	}
	/**
	 * Method to get a list of <MetaData> for a given <Workbook>.
	 * 
	 * @param id long The <Workbook> ID
	 * @return <List<NameValue>> The <MetaData> object
	 */
	public List<NameValue> getMetaData(long id) {
		Session session = null;
		List<NameValue> metaData = new ArrayList<NameValue>();
		Connection conn = null;
		Statement stmt = null;

		try {
			session = HibernateSessionFactory.getSession();

			WorkbookDAOHibernate wdh = new WorkbookDAOHibernate();
			MetadataDAOHibernate cmddh = new MetadataDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			rddh.setSession(session);
			cmddh.setSession(session);
			mhdh.setSession(session);
			wdh.setSession(session);

			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			stmt = conn.createStatement();
	        
			Transaction tx = session.beginTransaction();
			WorkbookData workbook = wdh.findById(id, false);
			WorkbookConfig config = workbook.getWorkbook_config_id();
			List<MetadataHeader> metas = mhdh.findByConfig(config);
			Set<gov.nrel.nbc.spreadsheet.dto.SheetData> sheets = workbook.getSheets();
			Iterator<gov.nrel.nbc.spreadsheet.dto.SheetData> sit = sheets.iterator();
			if (sit.hasNext()) {
				gov.nrel.nbc.spreadsheet.dto.SheetData sheet = sit.next();
				List<gov.nrel.nbc.spreadsheet.dto.RowData> rows = rddh.findBySheet(sheet);
				Iterator<gov.nrel.nbc.spreadsheet.dto.RowData> rit = rows.iterator();
				if (rit.hasNext()) {
					gov.nrel.nbc.spreadsheet.dto.RowData row = rit.next();
					String table = getTableName(config.getConfig_name(),sheet.getSheet_config_id().getSheet_name());
					String sql = "select ";
					List<MetadataHeader> metasUsed = new ArrayList<MetadataHeader>();
					Iterator<MetadataHeader> mit = metas.iterator();
					while (mit.hasNext()) {
						MetadataHeader meta = mit.next();
						if (!meta.isInternal()) {
							if (!sql.equals("select "))
								sql += ",";
							sql += "`"+meta.getName()+"`";
							metasUsed.add(meta);
						}
					}
					sql += " from `" + table+"` where row_data_id = "+row.getId();
					int numHeaders = metasUsed.size();
					String text = "";
					ResultSet rs = stmt.executeQuery(sql);
					if (rs.next()) {
						for (int col=0;col<numHeaders;col++) {
							text = rs.getString(col+1);
							NameValue nv = new NameValue();
							nv.setName(metasUsed.get(col).getName());
							nv.setSynonym(metasUsed.get(col).getSynonym());
							DataType dt = metasUsed.get(col).getTypeId();
							String sValue = "";
							String stype = "";
							if (dt != null)
								stype = dt.getDescription();
							if (dt != null)
								nv.setDataType(dt.getId());
							if (stype.equals(LONG)) {
								sValue = text;
							} else if (stype.equals(REAL)) {
								sValue = text;
							} else if (stype.equals(DATE)) {
								Calendar cal = Calendar.getInstance();
								Date date = rs.getDate(col+1);
								cal.setTime(date);
								String smon = "";
								int mon = cal.get(Calendar.MONTH)+1;
								if (mon > 9) 
									smon = String.valueOf(mon);
								else
									smon = "0"+String.valueOf(mon);
								String sday = "";
								int day = cal.get(Calendar.DAY_OF_MONTH);
								if (day > 9) 
									sday = String.valueOf(day);
								else
									sday = "0" + String.valueOf(day);
								String syear = "";
								int year = cal.get(Calendar.YEAR);
								if (year > 9)
									syear = String.valueOf(year);
								else
									syear = "0"+String.valueOf(year);
								sValue = smon + "/" + sday + "/" + syear;
							} else if (stype.equals(BOOLEAN)) {
								sValue = text;
							} else {
								sValue = text;
							}
							nv.setValue(sValue);
							metaData.add(nv);
						}
					}
				}
			}
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e1) {
				
			}
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return metaData;
	}

	/*
	 * 
	 * A method to add two lists together
	 * 
	 * @param metas List<NameValue> a list of metadata headers
	 * @param cells List<NameValue> a list of cell data headers
	 * @return List<String> the list of header strings
	 */
	private List<String> addAll(List<NameValue> metas, List<NameValue> cells, short kind) {
		List<String> allHeaders = new ArrayList<String>();
		Iterator<NameValue> it = metas.iterator();
		while (it.hasNext()) {
			NameValue hdr = it.next();
			if (kind == DBHEADER)
				allHeaders.add(hdr.getName());
			else
				allHeaders.add(hdr.getSynonym());
		}
		allHeaders.add("Attachments");
		it = cells.iterator();
		while (it.hasNext()) {
			NameValue hdr = it.next();
			if (kind == DBHEADER)
				allHeaders.add(hdr.getName());
			else
				allHeaders.add(hdr.getSynonym());
		}		
		return allHeaders;
	}
	
	private String addDayToDate(String d1) {
		String d2 = d1;
		try {
			// 08/08/2011
			SimpleDateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
			Date dt1 = dformat.parse(d1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt1);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int year = cal.get(Calendar.YEAR);
			String syear = String.valueOf(year);
			String smon = "";
			String sday = "";
			int mon = cal.get(Calendar.MONTH)+1;
			if (mon < 10)
				smon = "0";
			smon += String.valueOf(mon);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			if (day < 10)
				sday = "0";
			sday += String.valueOf(day);
			d2 = smon+"/"+sday+"/"+syear;
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.warn(SpreadSheetServiceImpl.getStackTrace(e));
		} 
		
		return d2;
	}
	/*
	 * 
	 * A method to construct the where sub-clauses
	 * 
	 * @param sql String the current sql
	 * @param trio <CriteriaTrioDTO> the current header-operator-value trio to add
	 * @return String the new sql
	 */
	public String constructWherePart(String sql, CriteriaTrioDTO trio, String wbConfig, String shConfig, Session session) {
		int oper = 0;
		if (trio.getOperator().equals("=")) oper=1;
		else if (trio.getOperator().equals("!=")) oper=2;
		else if (trio.getOperator().equals(">")) oper=3;
		else if (trio.getOperator().equals("<")) oper=4;		
		short kind = 0;
		String wbSyn = getWorkbookSynonymFromName(wbConfig,session);
		String shSyn = getSheetSynonymFromName(wbConfig,shConfig,session);
		String header = "";
		List<String> hdrs = new ArrayList<String>();
		if (!trio.getHeader().equals(WORKBOOK_ID) && !trio.getHeader().equals(ATTACHMENT_EXT)) {
			List<NameValue> headers = getMetaHeaders(wbSyn, shSyn, session);
			List<NameValue> nvhs = getHeader(headers,trio.getHeader(),SSHEADER);
			if (nvhs == null || nvhs.size()==0) {
				headers = getCellHeaders(wbSyn,shSyn,session);
				nvhs = getHeader(headers,trio.getHeader(),SSHEADER);
				if (nvhs != null) {
					Iterator<NameValue> nit = nvhs.iterator();
					while (nit.hasNext()) {
						NameValue nv = nit.next();
						hdrs.add(nv.getName());
					}
				}
				kind = CELL;
			} else {
				if (nvhs != null) {
					Iterator<NameValue> nit = nvhs.iterator();
					while (nit.hasNext()) {
						NameValue nv = nit.next();
						hdrs.add(nv.getName());
					}
				}
				kind = META;
			}
		} else header = trio.getHeader();
		if (trio.getValue().length()>0 && !header.equals(ATTACHMENT_EXT)) {
			String type = "";
			if (!header.equals(WORKBOOK_ID)) {
				type = getType(wbSyn,shSyn,header,kind,session);					
			} else 
				type = LONG;
			if (!header.equals(WORKBOOK_ID) && !header.equals(ATTACHMENT_EXT)) {
				sql += " and (";
				if (hdrs != null && hdrs.size()>0) {
					for (int i=0;i<hdrs.size();i++) {
						header = hdrs.get(i);
						if (!header.equals(WORKBOOK_ID))
							type = getType(wbSyn,shSyn,header,kind,session);
						else 
							type = LONG;
						if (i>0) sql += " or ";
						sql += " (cd.`"+header+"`";
						if (type.equals(STRING)) {
							int star = trio.getValue().indexOf("*");
							if (star != -1) {
								if (trio.getOperator().equals("="))
									trio.setOperator("like");
								else
									trio.setOperator("not like");
								trio.setValue(trio.getValue().replace("*", "%"));
							}
						}
						// TODO - test this
						if (!type.equals(DATE))
							sql += trio.getOperator() + " ";
						if (type.equals(LONG) || type.equals(REAL)) {
							sql += trio.getValue()+" ";
						} else if (type.equals(STRING)) {
							sql += "'" + trio.getValue() + "' ";
						} else if (type.equals(DATE)) {
							switch (oper) {
								case 1://=
									sql += " >= ";
									sql += FRONT_DATE_PART;
									sql += "'" + trio.getValue() + "' ";
									sql += BACK_DATE_PART;
									sql += " and ";
									sql += " cd.`"+header+"`";
									sql += " < ";
									sql += FRONT_DATE_PART;
									sql += "'" + addDayToDate(trio.getValue()) + "' ";
									sql += BACK_DATE_PART;
									break;
								case 2://!=
									sql += " < ";
									sql += FRONT_DATE_PART;
									sql += "'" + trio.getValue() + "' ";
									sql += BACK_DATE_PART;
									sql += " or ";
									sql += " cd.`"+header+"`";
									sql += " >= ";
									sql += FRONT_DATE_PART;
									sql += "'" + addDayToDate(trio.getValue()) + "' ";
									sql += BACK_DATE_PART;
									break;
								case 3://>
									sql += " >= ";
									sql += FRONT_DATE_PART;
									sql += "'" + addDayToDate(trio.getValue()) + "' ";
									sql += BACK_DATE_PART;
									break;
								case 4://<
									sql += " < ";
									sql += FRONT_DATE_PART;
									sql += "'" + trio.getValue() + "' ";
									sql += BACK_DATE_PART;
									break;
							}
						} else if (type.equals(BOOLEAN)&&!header.toLowerCase().endsWith("date")) {
							sql += trio.getValue();
						} else {
							sql += "'" + trio.getValue() + "' ";
						}
						sql += ")";
					}
					sql += ")";
				}
			}
			else if (header.equals(WORKBOOK_ID)) {
				sql += " and wd.`"+WORKBOOK_ID_COLUMN+"`";
				if (type.equals(STRING)) {
					int star = trio.getValue().indexOf("*");
					if (star != -1) {
						if (trio.getOperator().equals("="))
							trio.setOperator("like");
						else
							trio.setOperator("not like");
						trio.setValue(trio.getValue().replace("*", "%"));
					}
				}
				sql += " " + trio.getOperator() + " ";
				if (type.equals(LONG) || type.equals(REAL)) {
					sql += trio.getValue()+" ";
				} else if (type.equals(STRING)) {
					sql += "'" + trio.getValue() + "' ";
				} else if (type.equals(DATE)) {
					sql += FRONT_DATE_PART;
					sql += "'" + trio.getValue() + "' ";
					sql += BACK_DATE_PART;
				} else if (type.equals(BOOLEAN)) {
					sql += trio.getValue();
				} else {
					sql += "'" + trio.getValue() + "' ";
				}
			} 
		}
		return sql;
	}

	/*
	 * A method to retrieve the number of results for each workbook config
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @param trioList List<CriteriaTrioDTO> The list of the header-operator-value trios for this search
	 * @param List<List<String>> a list of counts by <WorkbookConfig> name and <SheetConfig> name
	 */
	public List<List<String>> getWorkbookCountsOld(String wbConfigName, String shConfigName, List<CriteriaTrioDTO> trioList) {
		log.info("begin count at "+getTime());
		List<List<String>> results = new ArrayList<List<String>>();
		query = new QueryStorage();
		List<List<String>> data = performSelect(wbConfigName,shConfigName,trioList,0,5);
		List<gov.nrel.nbc.spreadsheet.server.WorkbookData> wbData = new ArrayList<gov.nrel.nbc.spreadsheet.server.WorkbookData>();
		query.setUsed(QueryStorage.State.FIRST);
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookDAOHibernate wbdh = new WorkbookDAOHibernate();
			wbdh.setSession(session);
			wcdh.setSession(session);
			session.beginTransaction();
			List<NameValue> headers = getHeaders(null,null);
			int headerRow = 0;
			log.info("b4 for to "+data.size()+" at "+getTime());
			int ctr=0;
			String oldSheetName = "";
			for (int i=1;data!=null&&i<data.size();i++) {
				if (session != null) {
					if (!session.isOpen()) {
						log.info("reconnecting");
						session = HibernateSessionFactory.getSession();
						wbdh.setSession(session);
						wcdh.setSession(session);
						session.beginTransaction();
						log.info("after reconnecting");
					}
				}
				String swb = data.get(i).get(0);
				if (!contains(headers,swb,SYNONYM)) {
					long id = 0;
					try {
						id = Long.parseLong(swb);
					} catch (NumberFormatException nfe) {
						log.warn("problems parsing long="+swb);
					}
					WorkbookData wb = wbdh.findById(id, false);
					WorkbookConfig wc = wb.getWorkbook_config_id();
					wbConfigName = wc.getConfig_name();
					String wbSyn = getWorkbookSynonymFromName(wbConfigName, session);
					List<QueryStorage.Results> rs = query.getResults();
					Iterator<QueryStorage.Results> rit = rs.iterator();
					String sheetName = "";
					Set<SheetConfig> shConfigs = wc.getSheetConfigs();
					Iterator<SheetConfig> sit = shConfigs.iterator();
					log.info("b4 while - " + (++ctr) + " times");
					while (sit.hasNext()) {
						SheetConfig shConfig = sit.next();
						String shName = shConfig.getSheet_name();
						if (shName.equals(oldSheetName) && shName != null && !shName.isEmpty()) {
							sheetName = oldSheetName;
							break;
						}
						String shSyn = getSheetSynonymFromName(wbConfigName,shName, session);
						List<NameValue> shHeaders = getHeaders(wbSyn,shSyn,session);
						boolean headerOK = true;
						for (int colCtr=0;colCtr<data.get(headerRow).size()&&headerOK;colCtr++) {
							String header = data.get(headerRow).get(colCtr);
							if (!contains(shHeaders,header,SYNONYM)&!header.equals("Attachments")) {
								headerOK = false;
								log.info("bad header="+header+" for sheet="+shName);
							}
						}
						if (headerOK) {
							sheetName = shName;
							oldSheetName = sheetName;
							break;
						}
					}
					log.info("after while");
					if (!sheetName.isEmpty()) {
						String shSyn = getSheetSynonymFromName(wbConfigName,sheetName,session);
						boolean found = false;
						//log.info("b4 2nd while");
						while (rit.hasNext()) {
							QueryStorage.Results r = rit.next();
							if (r.getWbConfig().equals(wbSyn)&&r.getShConfig().equals(shSyn)) {
								r.getResults().add(data.get(i));
								found = true;
								break;
							}
						}
						//log.info("after 2nd while");
						if (!found) {
							QueryStorage.Results r = query.new Results();
							r.setWbConfig(wbSyn);
							r.setShConfig(shSyn);
							List<List<String>> datum = new ArrayList<List<String>>();
							datum.add(data.get(headerRow));
							datum.add(data.get(i));
							r.setResults(datum);
							query.getResults().add(r);
						}
						gov.nrel.nbc.spreadsheet.server.WorkbookData wd = null;
						//log.info("b4 wbData while");
						for (int w=0;w<wbData.size();w++) {
							gov.nrel.nbc.spreadsheet.server.WorkbookData wbd = wbData.get(w);
							if (wbd.getConfig().equals(wbSyn) && wbd.getSheet().equals(shSyn)) {
								wd = wbd;
								break;
							}
						}
						//log.info("after wbData while");
						if (wd == null) {
							wd = new gov.nrel.nbc.spreadsheet.server.WorkbookData();
							wd.setId(id);
							wd.setConfig(wbSyn);
							wd.setSheet(shSyn);
							wd.setCount(0);
							wbData.add(wd);
						}
						wd.setCount(wd.getCount()+1);
					}
				} else {
					headerRow = i;
				}
			}
			log.info("after for at "+getTime());
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
			log.info("end count at "+getTime());
		}			
		log.info("b4 wbdata for with "+wbData.size()+" at "+getTime());
		for (int i=0;i<wbData.size();i++) {
			gov.nrel.nbc.spreadsheet.server.WorkbookData wd = wbData.get(i);
			List<String> row = new ArrayList<String>();
			row.add(wd.getConfig());
			row.add(wd.getSheet());
			row.add(String.valueOf(wd.getCount()));
			results.add(row);
		}
		log.info("returning from workCounts");
		return results;
	}
	
	public List<List<String>> getWorkbookCounts(String wbConfigName, String shConfigName, List<CriteriaTrioDTO> trioList, int pos, int page) {
		log.info("begin count at "+getTime());
		List<List<String>> results = new ArrayList<List<String>>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookDAOHibernate wbdh = new WorkbookDAOHibernate();
			wbdh.setSession(session);
			wcdh.setSession(session);
			session.beginTransaction();
			List<ConfigCounts> counts = performCountSelect(wbConfigName, shConfigName, trioList, pos, session, page);
			Iterator<ConfigCounts> cit = counts.iterator();
			while (cit.hasNext()) {
				ConfigCounts cc = cit.next();
				List<String> row = new ArrayList<String>();
				row.add(cc.getConfig());
				String sheetName = "";
				String wbConfig = cc.getConfig();
				WorkbookConfig wc = wcdh.findByName(wbConfig);
				Set<SheetConfig> shConfigs = wc.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					String shName = shConfig.getSheet_name();
					sheetName = shName;
				}
				row.add(sheetName);
				row.add(String.valueOf(cc.getCount()));
				results.add(row);
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
			log.info("end count at "+getTime());
		}			
		log.info("returning from workCounts");
		return results;
	}
	
	/*
	 * a method to return the current time, used for logging
	 * @return String the current time in the form of HH:MM:SS
	 */
	private String getTime() {
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR);
		int mins = cal.get(Calendar.MINUTE);
		int secs = cal.get(Calendar.SECOND);
		String sdate = String.valueOf(hours)+":"+String.valueOf(mins)+":"+String.valueOf(secs);
		return sdate;
	}
	
	/* a method to retrieve the trio from the list of header-operator-value trios, if present
	 * 
	 * @param trioList List<CriteriaTrioDTO> The list of header-operator-value trios
	 * @param header String the header to be searched for
	 * @return CriteriaTrioDTO the header-operator-value trio for that header
	 */
	private CriteriaTrioDTO contains(List<CriteriaTrioDTO> trioList, String header) {
		CriteriaTrioDTO trio = null;
		boolean found = false;
		Iterator<CriteriaTrioDTO> trit = trioList.iterator();
		while (trit.hasNext()&&!found) {
			trio = trit.next();
			if (trio.getHeader().equals(header)) {
				found = true;
			}
		}
		if (found) return trio;
		return null;
	}
	/*
	private List<Long> getRangeIds(String table, int pos, boolean all, int page) {
		log.info("in getRangeIds with pos="+pos);
		List<Long> ids = new ArrayList<Long>();
		Connection conn = null;
		Statement stmt = null;
        String url = "";
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			//log.info("url="+url);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "SELECT "+ID_COLUMN+" FROM `"+table+"` WHERE "+ID_COLUMN+" >= ";
			long id = getMinValueId(table,ID_COLUMN);
			sql += id + " ORDER BY "+ID_COLUMN+" ASC ";
			if (!all)
				sql += "LIMIT "+page*pos+","+page;
			ResultSet rs = stmt.executeQuery( sql ) ;
			while( rs.next() ) {
				id = rs.getLong(1);
				ids.add(id);
			}
		}catch( SQLException se ) {
		      //System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return ids;
	}
	*/
	private long getMinValueId(String table, String field) {
		Connection conn = null;
		Statement stmt = null;
		long ret = 0;
        String url = "";
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			//log.info("url="+url);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "select min("+field+") from `"+table+"`";
			ResultSet rs = stmt.executeQuery( sql ) ;
			if( rs.next() ) {
				ret = rs.getLong(1);
			}
		}catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return ret;
	}

	/*
	 * 
	 * A method to construct and execute the query used for searching the 
	 * spreadsheet cell and meta data.
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @param trioList List<CriteriaTrioDTO> the list of header-operator-value trios
	 * @param conn <Connection> the JDBC connection
	 * @param stmt <Statement> the JDBC statement
	 * @param metaHeaders List<NameValue> a list of the meta data headers
	 * @param cellHeaders List<NameValue> a list of the cell data headers
	 * @param header_data List<String> a list of the headers
	 * @param Attachments List<AttachmentData> a list of the attachments
	 * @return List<List<String>> the results of the query
	 */
	private List<List<String>> doSQL(
			String wbConfigName,
			String shConfigName,
			List<CriteriaTrioDTO> trioList,
			Connection conn,
			Statement stmt,
			List<NameValue> metaHeaders,
			List<NameValue> cellHeaders,
			List<String> header_data,
			List<AttachmentData> attachments, 
			Session session, int pos, int page) throws SQLException, HibernateException, Exception {

		int rowCtr = 1;
		List<List<String>> tableData = new ArrayList<List<String>>();
		if (wbConfigName == null || wbConfigName.isEmpty())
			return null;

		if (shConfigName != null && !shConfigName.isEmpty()) {
			String wbSyn = getWorkbookSynonymFromName(wbConfigName,session);
			String shSyn = getSheetSynonymFromName(wbConfigName,shConfigName,session);
			metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
			cellHeaders = getCellHeaders(wbSyn,shSyn,session);
			List<Long> wids = new ArrayList<Long>();
			CriteriaTrioDTO atrio = contains(trioList,AppConstants.ATTACHMENT_EXT);
			if (atrio != null) {
				Statement attach_stmt = conn.createStatement();
				String attach_sql = ATTACHMENT_WORKBOOK_SQL;
				attach_sql += " and attt." + AppConstants.ATTACHMENT_EXT_COLUMN + " " + atrio.getOperator() + " '" + atrio.getValue() + "' " ;
				attach_sql += GROUP_BY;
				log.info("attach_sql="+attach_sql);
				ResultSet rs1 = attach_stmt.executeQuery(attach_sql);
				while( rs1.next() ) {
					long id = rs1.getLong(1);
					wids.add(id);
				}
				rs1.close();
				attach_stmt.close();
			}
			String sql = "";
			sql = BEGIN_SELECT;
			if (!metaHeaders.isEmpty()) {
				Iterator<NameValue> mit = metaHeaders.iterator();
				while (mit.hasNext()) {
					NameValue meta = mit.next();
					sql += ", cd.`"+ meta.getName() +"`";
				}
			}
			if (!cellHeaders.isEmpty()) {
				Iterator<NameValue> cit = cellHeaders.iterator();
				while (cit.hasNext()) {
					NameValue cell = cit.next();
					sql += ", cd.`"+ cell.getName() +"`";
				}
			}
			if ((wbConfigName != null && !wbConfigName.isEmpty() && 
					shConfigName != null && !shConfigName.isEmpty()))
			{
				sql += " from `"+getTableName(wbConfigName,shConfigName)+"` cd, "; 
			}
			sql += FROM_WHERE_PART;
			if (atrio != null) {
				if (wids != null && wids.size()>0) {
					sql += " and wd.workbook_id in (";
					int ctr=0;
					Iterator<Long> wit = wids.iterator();
					while (wit.hasNext()) {
						Long wid = wit.next();
						if (ctr > 0)
							sql += ",";
						sql += String.valueOf(wid.longValue());
						ctr++;
					}
					sql += ") ";
				} else return null;
			}
			if (wbConfigName != null && !wbConfigName.isEmpty())
				sql += " and wc.config_name = '" + wbConfigName + "' ";
			if (shConfigName != null && !shConfigName.isEmpty())
				sql += " and sc.sheet_name = '" + shConfigName + "' ";
			/*
			String table = SpreadSheetServiceImpl.getTableName(wbConfigName,shConfigName);
			List<Long> ids = getRangeIds(table, pos, false);
			if (ids.size()>0) {
				sql += " and (cd."+ID_COLUMN+" in (";
				int ctr=1;
				Iterator<Long> it = ids.iterator();
				while (it.hasNext()) {
					Long l = it.next();
					if (ctr>1) sql += ",";
					sql += l;
					ctr++;
				}
				sql += "))";
			}
			*/
			Iterator<CriteriaTrioDTO> trit = trioList.iterator();
			while (trit.hasNext()) {
				CriteriaTrioDTO trio = trit.next();
				sql = constructWherePart(sql, trio, wbConfigName, shConfigName,session);
			}
			sql += ORDER_BY;
			if (page > 0)
				sql += " LIMIT "+page*pos+","+page;
			//log.info("MAIN sql="+sql);
			log.warn("MAIN sql="+sql);
			// Execute the query
			////PreparedStatement ps = conn.prepareStatement(sql);
			////ResultSet rs = ps.executeQuery();
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery( sql ) ;
			} catch (Exception e1) {}
			//List<Rows> values = jds.getAllValues(sql,numCols);
			// Loop through the result set
			//int colCtr = 1;
			int colCtr = 0;
			long previous_workbook = 0;
			long previous_workbook_attachments = 0;
			List<String> rowData = null;
			//Iterator<Rows> vit = values.iterator(); 
			//while( vit.hasNext() ) {
			//	Rows row = vit.next();
			while (rs != null && rs.next()) {
				rowData = new ArrayList<String>();
				if (rowCtr==1)
					tableData.add(header_data);
				rowCtr++;
				colCtr=1;
				//colCtr = 0;
				//long workbook = 0;
				//String text = row.get(colCtr++);
				//try {
				//	workbook = Long.parseLong(text);
				//} catch (NumberFormatException nfe) {}
				long workbook = rs.getLong(colCtr++);
				rowData.add(String.valueOf(workbook));
				Iterator<NameValue> mit = metaHeaders.iterator();
				while (mit.hasNext()) {
					NameValue nv = mit.next();
					//String header = nv.getName();
					long type = nv.getDataType();
					String value = "";
					// format data
					if (type==AppConstants.DATE || type==AppConstants.REAL) {
						String format = nv.getDataFormat();
						if (type==AppConstants.REAL) {
							String rvalue = rs.getString(colCtr++);
							//String rvalue = row.get(colCtr++);
							int digits = 6;
							try {
								digits = Integer.parseInt(format);
							} catch (NumberFormatException nfe) {
								digits = 6;
							}
							if (rvalue != null) {
								int dot = rvalue.indexOf('.');
								if (dot != -1 && dot+digits+1 < rvalue.length()) {
									rvalue = rvalue.substring(0,dot+digits+1);
								} 
								if (rvalue.contains(String.valueOf(AppConstants.DEFAULT_LONG)))
									value = null;
								else
									value = rvalue;
							} else
								value = null;
						} else {
							SimpleDateFormat dFormat = new SimpleDateFormat(format);
							String DBDATEFORMAT = "yyyy-MM-dd HH:mm:ss";
							SimpleDateFormat dbFormat = new SimpleDateFormat(DBDATEFORMAT);
							//log.info("header="+header+";format="+format);
							String dvalue = rs.getString(colCtr++);
							//String dvalue = row.get(colCtr++);
							Date ddvalue = null;
							try {
								ddvalue = dbFormat.parse(dvalue);
								dvalue = dFormat.format(ddvalue);
								//log.info("stringDate="+value);
							} catch (ParseException pe) {
								log.warn("Invalid date format: "+dvalue);
							} catch (Exception e) {
								//log.warn("Exception: "+e);
								//log.warn(getStackTrace(e));
							}
							value = dvalue;
						}
					} else {
						value = rs.getString(colCtr++);
						//value = row.get(colCtr++);
						if (type == AppConstants.LONG) {
							if (value != null && value.contains(String.valueOf(AppConstants.DEFAULT_LONG)))
								value = null;
						} else if (type == AppConstants.BOOLEAN) 
							if (value.equals("1")) value = "TRUE";
							else if (value.equals("0")) value = "FALSE";
							else value = null;
					}
					rowData.add(value);
				}
				if (workbook != previous_workbook) { 
					previous_workbook = workbook;
					Statement attach_stmt = conn.createStatement();
					String attach_sql = ATTACHMENT_COUNT_SQL;
					attach_sql += " and wd.workbook_id = "+workbook;
					trit = trioList.iterator();
					while (trit.hasNext()) {
						CriteriaTrioDTO trio = trit.next();
						if (trio.getHeader().equals(ATTACHMENT_EXT)) {
							attach_sql += " and attt.`"+ATTACHMENT_EXT_COLUMN+"`";
							attach_sql += trio.getOperator() + " '" + trio.getValue() + "' ";
						}
					}
					attach_sql += GROUP_BY;
					//log.info("attach_count_sql="+attach_sql);
					ResultSet rs1 = attach_stmt.executeQuery(attach_sql);
					long count = 0;
					while( rs1.next() ) {
						count = rs1.getLong(1);
					}
					rs1.close();
					if (attach_stmt != null)
						attach_stmt.close();
					previous_workbook_attachments = count;
				}
				rowData.add(String.valueOf(previous_workbook_attachments));
				Iterator<NameValue> cit = cellHeaders.iterator();
				while (cit.hasNext()) {
					NameValue nv = cit.next();
					long type = nv.getDataType();
					//String header = nv.getName();
					//log.info("header="+header+" has type="+type);
					String value = "";
					// format data
					if (type==AppConstants.DATE || type==AppConstants.REAL) {
						String format = nv.getDataFormat();
						if (type==AppConstants.REAL) {
							String rvalue = rs.getString(colCtr++);
							//String rvalue = row.get(colCtr++);
							int digits = 6;
							try {
								digits = Integer.parseInt(format);
							} catch (NumberFormatException nfe) {
								digits = 6;
							}
							if (rvalue != null) {
								int dot = rvalue.indexOf('.');
								if (dot != -1 && dot+digits+1 < rvalue.length()) {
									rvalue = rvalue.substring(0,dot+digits+1);
								} 
								if (rvalue.contains(String.valueOf(AppConstants.DEFAULT_LONG)))
									value = null;
								else
									value = rvalue;
							} else value = null;
						} else {
							SimpleDateFormat dFormat = new SimpleDateFormat(format);
							String DBDATEFORMAT = "yyyy-MM-dd HH:mm:ss";
							SimpleDateFormat dbFormat = new SimpleDateFormat(DBDATEFORMAT);
							//log.info("header="+header+";format="+format);
							String dvalue = rs.getString(colCtr++);
							//String dvalue = row.get(colCtr++);
							if (dvalue != null) {
							Date ddvalue = null;
							try {
								ddvalue = dbFormat.parse(dvalue);
								dvalue = dFormat.format(ddvalue);
								//log.info("stringDate="+value); 
							} catch (ParseException pe) {
								log.warn("Invalid date format: "+dvalue);
							} catch (Exception e) {
								//log.warn("Exception: "+e);
								//log.warn(getStackTrace(e));
							}
							value = dvalue;
							} else value = null;
						}
					} else {
						value = rs.getString(colCtr++);
						//value = row.get(colCtr++);
						if (type == AppConstants.LONG) {
							if (value != null && value.contains(String.valueOf(AppConstants.DEFAULT_LONG)))
								value = null;
						} else if (type == AppConstants.BOOLEAN) {
								if (value.equals("1")) value = "TRUE";
								else if (value.equals("0")) value = "FALSE";
								else value = null;
						}
					}
					rowData.add(value);
				}
				//log.info( header+","+order+","+workbook+","+sheet+","+row+","+value ) ;
				//if (!tableData.contains(rowData))
					tableData.add(rowData);
			}
		} else
			log.info("shConfigName was NULL");

		log.info("JDBC DONE AT: "+getTime()+" with "+rowCtr+" rows returned");
		return tableData;
	}
	
	/*
	 * 
	 * A method to construct and execute the query used for searching the 
	 * spreadsheet cell and meta data.
	 * 
	 * @param wbConfigName String the name of the <WorkbookConfig>
	 * @param shConfigName String the name of the <SheetConfig>
	 * @param trioList List<CriteriaTrioDTO> the list of header-operator-value trios
	 * @param conn <Connection> the JDBC connection
	 * @param stmt <Statement> the JDBC statement
	 * @param metaHeaders List<NameValue> a list of the meta data headers
	 * @param cellHeaders List<NameValue> a list of the cell data headers
	 * @param header_data List<String> a list of the headers
	 * @param Attachments List<AttachmentData> a list of the attachments
	 * @return List<List<String>> the results of the query
	 */
	private long doSQLCount(
			String wbConfigName,
			String shConfigName,
			List<CriteriaTrioDTO> trioList,
			Connection conn,
			Statement stmt,
			List<NameValue> metaHeaders,
			List<NameValue> cellHeaders,
			List<String> header_data,
			List<AttachmentData> attachments,
			int pos,
			Session session, int page) throws SQLException, HibernateException, Exception {

		//int rowCtr = 1;
		long count = 0;
		if (wbConfigName == null || wbConfigName.isEmpty())
			return count;

		if (shConfigName != null && !shConfigName.isEmpty()) {
			String wbSyn = getWorkbookSynonymFromName(wbConfigName,session);
			String shSyn = getSheetSynonymFromName(wbConfigName,shConfigName,session);
			metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
			cellHeaders = getCellHeaders(wbSyn,shSyn,session);
			List<Long> wids = new ArrayList<Long>();
			CriteriaTrioDTO atrio = contains(trioList,AppConstants.ATTACHMENT_EXT);
			if (atrio != null) {
				Statement attach_stmt = conn.createStatement();
				String attach_sql = ATTACHMENT_WORKBOOK_SQL;
				attach_sql += " and attt." + AppConstants.ATTACHMENT_EXT_COLUMN + " " + atrio.getOperator() + " '" + atrio.getValue() + "' " ;
				attach_sql += GROUP_BY;
				log.info("attach_sql="+attach_sql);
				ResultSet rs1 = attach_stmt.executeQuery(attach_sql);
				while( rs1.next() ) {
					long id = rs1.getLong(1);
					wids.add(id);
				}
				rs1.close();
				attach_stmt.close();
			}
			String sql = "";
			sql = "select count(1) ";
			if ((wbConfigName != null && !wbConfigName.isEmpty() && 
					shConfigName != null && !shConfigName.isEmpty()))
			{
				sql += " from `"+getTableName(wbConfigName,shConfigName)+"` cd, "; 
			} 
			sql += FROM_WHERE_PART;
			if (atrio != null) {
				if (wids != null && wids.size()>0) {
					sql += " and wd.workbook_id in (";
					int ctr=0;
					Iterator<Long> wit = wids.iterator();
					while (wit.hasNext()) {
						Long wid = wit.next();
						if (ctr > 0)
							sql += ",";
						sql += String.valueOf(wid.longValue());
						ctr++;
					}
					sql += ") ";
				} else return count;
			}
			if (wbConfigName != null && !wbConfigName.isEmpty())
				sql += " and wc.config_name = '" + wbConfigName + "' ";
			if (shConfigName != null && !shConfigName.isEmpty())
				sql += " and sc.sheet_name = '" + shConfigName + "' ";
			/*
			String table = SpreadSheetServiceImpl.getTableName(wbConfigName,shConfigName);
			List<Long> ids = getRangeIds(table, pos, true, page);
			if (ids.size()>0) {
				sql += " and (cd."+ID_COLUMN+" in (";
				int ctr=1;
				Iterator<Long> it = ids.iterator();
				while (it.hasNext()) {
					Long l = it.next();
					if (ctr>1) sql += ",";
					sql += l;
					ctr++;
				}
				sql += "))";
			}
			*/
			Iterator<CriteriaTrioDTO> trit = trioList.iterator();
			while (trit.hasNext()) {
				CriteriaTrioDTO trio = trit.next();
				sql = constructWherePart(sql, trio, wbConfigName, shConfigName,session);
			}
			//sql += " ORDER BY "
			log.info("MAIN COUNT sql="+sql);
			// Execute the query
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery( sql ) ;
			} catch (Exception e1) {}
			while (rs != null && rs.next()) {
				count = rs.getLong(1);
			}
		} else
			log.info("shConfigName was NULL");

		log.info("JDBC DONE AT: "+getTime()+" with "+count+" rows returned");
		return count;
	}
	
	/**
	 * Method to retrieve data table names
	 * 
	 * @return List<String> tables A list of table names
	 */
	private List<String> getDataTables(String wbConfig) {
		List<String> tables = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
	        String sql = "select table_name from data_tables";
	        if (!wbConfig.isEmpty()) 
	        	sql += " where table_name like '"+wbConfig+"_%'";
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	        	String name = rs.getString(1);
	        	tables.add(name);
	        }
	        rs.close();
		}  catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
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
		return tables;
	}
	
	/**
	 * Returns a list of values for a given column, and given beginning letter(s)
	 * 
	 * @param config String The workbook config name
	 * @param column String column The name of the column
	 * @return List<String> The list of values
	 */
	public List<String> getStringValues(String wbSyn, String colSyn) {
		List<String> values = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try {
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
    		String config = "";
        	if (wbSyn != null && !wbSyn.isEmpty()) 
    			config = getWorkbookNameFromSynonym(wbSyn);
	        List<String> tables = getDataTables(config);
	        Iterator<String> it = tables.iterator();
	        while (it.hasNext()) {
	        	String table = it.next();
		        try {
		        	if (wbSyn != null && !wbSyn.isEmpty()) {
		        		log.info("calling getColumnsFromSyn with "+wbSyn+" and "+colSyn);
		        		List<String> columns = getColumnsFromSynonym(wbSyn,colSyn);
		        		log.info("got back "+columns.size()+" columns");
		        		Iterator<String> cit = columns.iterator();
		        		while (cit.hasNext()) {
		        			String column = cit.next();
			        		if (table.toUpperCase().startsWith(config.toUpperCase())) {
						        String sql = "select `"+column+"` from `"+table+"` where `"+column+"` not null";
						        log.info("values sql = "+sql);
						        ResultSet rs = stmt.executeQuery(sql);
						        while (rs.next()) {
						        	String value = rs.getString(1);
						        	values.add(value);
						        }
						        rs.close();
			        		}
						}
		        	} else {
		        		config = getWbFromTable(table);
		        		String wbSyn1 = getWorkbookSynonymFromName(config);
		        		log.info("calling getColumnsFromSyn with "+wbSyn1+" and "+colSyn);
		        		List<String> columns = getColumnsFromSynonym(wbSyn1,colSyn);
		        		log.info("got back "+columns.size()+" columns");
		        		Iterator<String> cit = columns.iterator();
		        		while (cit.hasNext()) {
		        			String column = cit.next();
		        			String sql = "select `"+column+"` from `"+table+"`";
				        	log.info("values sql = "+sql);
					        ResultSet rs = stmt.executeQuery(sql);
					        while (rs.next()) {
					        	String value = rs.getString(1);
					        	values.add(value);
					        }
					        rs.close();
		        		}
		        	}
		        } catch (SQLException se1) {
		        	//log.info("SQL Exception caught: "+se1.getMessage());
		        }
	        }
		}  catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.warn("Exception caught: " + e);
			log.warn(getStackTrace(e));
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
		log.info(colSyn+" has "+values.size()+" values");
		return values;
	}
	
	private List<String> getColumnsFromSynonym(String config,String synonym) {
		List<String> columns = new ArrayList<String>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			session.beginTransaction();
			
			WorkbookConfig wbConfig = sidh.findBySynonym(config);
			if (wbConfig != null) {
				Set<SheetConfig> shConfigs = wbConfig.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					if (!session.isOpen()) {
						session = HibernateSessionFactory.getSession();
						sidh.setSession(session);
						session.beginTransaction();
					}
					List<NameValue> metas = getMetaHeaders(wbConfig.getSynonym(),shConfig.getSynonym(),session);
					List<NameValue> nvs = getHeader(metas,synonym,SYNONYM);
					if (nvs != null && nvs.size()==1) {
						if (nvs != null) {
							Iterator<NameValue> nit = nvs.iterator();
							if (nit.hasNext()) {
								NameValue nv = nit.next();
								columns.add(nv.getName());
								metas.remove(nv);
							}
						}						
					} else {
						boolean added = true;
						while (nvs != null && added) {
							added = false;
							//log.info("# of headers = "+nvs.size());
							Iterator<NameValue> nit = nvs.iterator();
							while (nit.hasNext()) {
								NameValue nv = nit.next();
								columns.add(nv.getName());
								added = true;
								metas.remove(nv);
							}
							nvs = getHeader(metas,synonym,SYNONYM);
							if (!added) break;
						}
					}
					List<NameValue> cells = getCellHeaders(wbConfig.getSynonym(),shConfig.getSynonym(),session);
					nvs = getHeader(cells,synonym,SYNONYM);
					if (nvs != null && nvs.size()==1) {
						if (nvs != null) {
							Iterator<NameValue> nit = nvs.iterator();
							if (nit.hasNext()) {
								NameValue nv = nit.next();
								columns.add(nv.getName());
								metas.remove(nv);
							}
						}						
					} else {
						boolean added = true;
						while (nvs != null && added) {
							//log.info("# of headers = "+nvs.size());
							Iterator<NameValue> nit = nvs.iterator();
							added = false;
							while (nit.hasNext()) {
								added = true;
								NameValue nv = nit.next();
								columns.add(nv.getName());
								metas.remove(nv);
							}
							nvs = getHeader(cells,synonym,SYNONYM);
							if (!added) break;
						}
					}
				}
			}
			//tx.commit();
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		//log.info("# of columns="+columns.size());
		return columns;
	}
	/**
	 * Method to get the rows of data for a query, based on a header-operator-value trio
	 * 
	 * @param wbConfigName String The name of the <WorkbookConfig>
	 * @param shConfigName String The name of the <SheetConfig>
	 * @param trioList <List<CriteriaTrioDTO>> The header-operator-value trio
	 * @return <List<List<String>>> The rows of data
	 */
	public List<List<String>> performSelect(String wbConfigName, String shConfigName, List<CriteriaTrioDTO> trioList, int pos, int page) {
		log.info("in performSelect at "+getTime());
		List<List<String>> tableData = new ArrayList<List<String>>();
		List<NameValue> metaHeaders = null;
		List<NameValue> cellHeaders = null;
		List<String> header_data = null;
		Session session = null;
		/*
		if (query != null && query.getUsed()!=QueryStorage.State.INIT) {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			metaHeaders = getMetaHeaders(wbConfigName,shConfigName,session);
			cellHeaders = getCellHeaders(wbConfigName,shConfigName,session);
			header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
			tableData.add(header_data);
			query.setUsed(QueryStorage.State.SECOND);
			List<QueryStorage.Results> rs = query.getResults();
			Iterator<QueryStorage.Results> rit = rs.iterator();
			while (rit.hasNext()) {
				QueryStorage.Results r = rit.next();
				if (wbConfigName == null || wbConfigName.isEmpty() || (r.getWbConfig().equals(wbConfigName)&&(r.getShConfig().equals(shConfigName)))) {
					for (int rowCtr=1;rowCtr<r.getResults().size();rowCtr++) {
						List<String> row = new ArrayList<String>();
						List<String> rr = r.getResults().get(rowCtr);
						row.addAll(rr);
						//for (int colCtr=0;colCtr<header_data.size()+1;colCtr++) {
						//	if (colCtr<rr.size())
						//		row.add(rr.get(colCtr));
						//}
						tableData.add(row);
					}
				}
			}
			log.info("returning from server at "+getTime());
			return tableData;
		}
		*/
		log.info("JDBC begin: "+getTime());
		Connection conn = null;
		Statement stmt = null;
		List<AttachmentData> attachments = new ArrayList<AttachmentData>();
		
		try {
			session = HibernateSessionFactory.getSession();

			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookDAOHibernate wbdh = new WorkbookDAOHibernate();
			wbdh.setSession(session);
			wcdh.setSession(session);
			dfdh.setSession(session);
			mdh.setSession(session);
			ctdh.setSession(session);

			session.beginTransaction();
				
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;

			if (wbConfigName == null || wbConfigName.isEmpty()) {
				List<String> configs = wcdh.findAllSynonyms();
				for (int i=0;i<configs.size();i++) {
					String wbSyn = configs.get(i);
					List<String> sheetConfigs = getSheetConfigs(wbSyn, session);
					Iterator<String> scit = sheetConfigs.iterator();
					while (scit.hasNext()) {
						if (!session.isOpen()) {
							session = HibernateSessionFactory.getSession();
							wbdh.setSession(session);
							wcdh.setSession(session);
							dfdh.setSession(session);
							mdh.setSession(session);
							ctdh.setSession(session);
							session.beginTransaction();
						}
						String shSyn = scit.next();
						WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
						wbConfigName = wbConfig.getConfig_name();
						Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
						Iterator<SheetConfig> sit = sheets.iterator();
						while (sit.hasNext()) {
							SheetConfig shConfig = sit.next();
							if (shConfig.getSynonym().equals(shSyn)) {
								shConfigName = shConfig.getSheet_name();
								break;
							}
						}
						List<NameValue> mhList = getMetaHeaders(wbSyn,shSyn,session);
						List<NameValue> chList = getCellHeaders(wbSyn,shSyn,session);
						boolean [] founds = new boolean[trioList.size()];
						for (int ctr=0;ctr<trioList.size();ctr++) {
							founds[ctr] = false;
						}
						for (int ctr=0;ctr<trioList.size();ctr++) {
							CriteriaTrioDTO trio = trioList.get(ctr);
							String header = trio.getHeader();
							if (contains(mhList,header,SSHEADER)) {
								founds[ctr] = true;
							} else if (contains(chList,header,SSHEADER)) {
								founds[ctr] = true;
							} else if (header.equals(WORKBOOK_ID)) {
								founds[ctr] = true;
							} else if (header.equals(ATTACHMENT_EXT)) {
								founds[ctr] = true;
							}
						}
						boolean found = true;
						for (int ctr=0;ctr<trioList.size();ctr++) {
							if (!founds[ctr]) {
								found = false;
								break;
							}
						}
						if (found) {
							metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
							cellHeaders = getCellHeaders(wbSyn,shSyn,session);
							header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
							List<List<String>> results = doSQL(wbConfigName,shConfigName,trioList,conn,stmt,
									metaHeaders,cellHeaders,header_data,attachments,session,pos,page);
							if (results != null && !tableData.contains(results))
								if (results.size()>1 && !tableData.contains(results.get(1))) {
									tableData.addAll(results);
								} else if (results.size()>1){
									log.warn("rejecting "+results.size()+" results");
								}
						}
					}
				}
				wbConfigName = null;
			} else {
				if (shConfigName != null && !shConfigName.isEmpty()) {
					String wbSyn = wbConfigName;
					String shSyn = shConfigName;
					WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
					wbConfigName = wbConfig.getConfig_name();
					Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
					Iterator<SheetConfig> sit = sheets.iterator();
					while (sit.hasNext()) {
						SheetConfig shConfig = sit.next();
						if (shConfig.getSynonym().equals(shSyn)) {
							shConfigName = shConfig.getSheet_name();
							break;
						}
					}
					metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
					cellHeaders = getCellHeaders(wbSyn,shSyn,session);
					header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
					wbConfigName = wbConfig.getConfig_name();
					tableData = doSQL(wbConfigName,shConfigName,trioList,conn,stmt,
							metaHeaders,cellHeaders,header_data,attachments,session,pos,page);
				} else {
					String wbSyn = wbConfigName;
					WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
					wbConfigName = wbConfig.getConfig_name();
					Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
					Iterator<SheetConfig> sit = sheets.iterator();
					while (sit.hasNext()) {
						SheetConfig shConfig = sit.next();
						shConfigName = shConfig.getSheet_name();
						String shSyn = shConfig.getSynonym();
						metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
						cellHeaders = getCellHeaders(wbSyn,shSyn,session);
						header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
						wbConfigName = wbConfig.getConfig_name();
						List<List<String>> results = doSQL(wbConfigName,shConfigName,trioList,conn,stmt,
								metaHeaders,cellHeaders,header_data,attachments,session,pos,page);					
						if (results != null && !tableData.contains(results))
							if (results.size()>1 && !tableData.contains(results.get(1))) {
								tableData.addAll(results);
							} else if (results.size()>1){
								log.warn("rejecting "+results.size()+" results");
							}
					}
				}
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
			log.info("JDBC END: "+getTime());
		}
		return tableData;
	}

	/**
	 * Method to get the rows of data for a query, based on a header-operator-value trio
	 * 
	 * @param wbConfigName String The name of the <WorkbookConfig>
	 * @param shConfigName String The name of the <SheetConfig>
	 * @param trioList <List<CriteriaTrioDTO>> The header-operator-value trio
	 * @return <List<List<String>>> The rows of data
	 */
	private List<ConfigCounts> performCountSelect(String wbConfigName, String shConfigName, 
			List<CriteriaTrioDTO> trioList, int pos, Session session, int page) {
		log.info("in performSelect at "+getTime());
		List<ConfigCounts> tableData = new ArrayList<ConfigCounts>();
		List<NameValue> metaHeaders = null;
		List<NameValue> cellHeaders = null;
		List<String> header_data = null;
		/*
		if (query != null && query.getUsed()!=QueryStorage.State.INIT) {
			metaHeaders = getMetaHeaders(wbConfigName,shConfigName,session);
			cellHeaders = getCellHeaders(wbConfigName,shConfigName,session);
			header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
			//tableData.add(header_data);
			query.setUsed(QueryStorage.State.SECOND);
			List<QueryStorage.Results> rs = query.getResults();
			Iterator<QueryStorage.Results> rit = rs.iterator();
			while (rit.hasNext()) {
				QueryStorage.Results r = rit.next();
				if (wbConfigName == null || wbConfigName.isEmpty() || (r.getWbConfig().equals(wbConfigName)&&(r.getShConfig().equals(shConfigName)))) {
					for (int rowCtr=1;rowCtr<r.getResults().size();rowCtr++) {
						List<String> row = new ArrayList<String>();
						List<String> rr = r.getResults().get(rowCtr);
						row.addAll(rr);
						//for (int colCtr=0;colCtr<header_data.size()+1;colCtr++) {
						//	if (colCtr<rr.size())
						//		row.add(rr.get(colCtr));
						//}
						//tableData.add(row);
					}
				}
			}
			log.info("returning from server at "+getTime());
			return tableData;
		}
		*/
		log.info("JDBC begin: "+getTime());
		Connection conn = null;
		Statement stmt = null;
		List<AttachmentData> attachments = new ArrayList<AttachmentData>();
		
		try {
			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			DataFormatDAOHibernate dfdh = new DataFormatDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookDAOHibernate wbdh = new WorkbookDAOHibernate();
			wbdh.setSession(session);
			wcdh.setSession(session);
			dfdh.setSession(session);
			mdh.setSession(session);
			ctdh.setSession(session);

			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;

			if (wbConfigName == null || wbConfigName.isEmpty()) {
				List<String> configs = wcdh.findAllSynonyms();
				for (int i=0;i<configs.size();i++) {
					String wbSyn = configs.get(i);
					List<String> sheetConfigs = getSheetConfigs(wbSyn, session);
					Iterator<String> scit = sheetConfigs.iterator();
					while (scit.hasNext()) {
						if (!session.isOpen()) {
							session = HibernateSessionFactory.getSession();
							wbdh.setSession(session);
							wcdh.setSession(session);
							dfdh.setSession(session);
							mdh.setSession(session);
							ctdh.setSession(session);
							session.beginTransaction();
						}
						String shSyn = scit.next();
						WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
						if (wbConfig == null) {
							log.warn("wbConfig was null for wbSyn="+wbSyn);
						}
						wbConfigName = wbConfig.getConfig_name();
						Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
						Iterator<SheetConfig> sit = sheets.iterator();
						while (sit.hasNext()) {
							SheetConfig shConfig = sit.next();
							if (shConfig.getSynonym().equals(shSyn)) {
								shConfigName = shConfig.getSheet_name();
								break;
							}
						}
						List<NameValue> mhList = getMetaHeaders(wbSyn,shSyn,session);
						List<NameValue> chList = getCellHeaders(wbSyn,shSyn,session);
						boolean [] founds = new boolean[trioList.size()];
						for (int ctr=0;ctr<trioList.size();ctr++) {
							founds[ctr] = false;
						}
						for (int ctr=0;ctr<trioList.size();ctr++) {
							CriteriaTrioDTO trio = trioList.get(ctr);
							String header = trio.getHeader();
							if (contains(mhList,header,SSHEADER)) {
								founds[ctr] = true;
							} else if (contains(chList,header,SSHEADER)) {
								founds[ctr] = true;
							} else if (header.equals(WORKBOOK_ID)) {
								founds[ctr] = true;
							} else if (header.equals(ATTACHMENT_EXT)) {
								founds[ctr] = true;
							}
						}
						boolean found = true;
						for (int ctr=0;ctr<trioList.size();ctr++) {
							if (!founds[ctr]) {
								found = false;
								break;
							}
						}
						if (found) {
							metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
							cellHeaders = getCellHeaders(wbSyn,shSyn,session);
							header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
							long count = doSQLCount(wbConfigName,shConfigName,trioList,conn,stmt,
									metaHeaders,cellHeaders,header_data,attachments,pos,session,page);
							ConfigCounts cc = new ConfigCounts();
							cc.setCount(count);
							cc.setConfig(wbConfigName);
							tableData.add(cc);
						}
					}
				}
				wbConfigName = null;
			} else {
				if (shConfigName != null && !shConfigName.isEmpty()) {
					String wbSyn = wbConfigName;
					String shSyn = shConfigName;
					WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
					wbConfigName = wbConfig.getConfig_name();
					Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
					Iterator<SheetConfig> sit = sheets.iterator();
					while (sit.hasNext()) {
						SheetConfig shConfig = sit.next();
						if (shConfig.getSynonym().equals(shSyn)) {
							shConfigName = shConfig.getSheet_name();
							break;
						}
					}
					metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
					cellHeaders = getCellHeaders(wbSyn,shSyn,session);
					header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
					wbConfigName = wbConfig.getConfig_name();
					long count = doSQLCount(wbConfigName,shConfigName,trioList,conn,stmt,
							metaHeaders,cellHeaders,header_data,attachments,pos,session,page);
					ConfigCounts cc = new ConfigCounts();
					cc.setCount(count);
					cc.setConfig(wbConfigName);
					tableData.add(cc);
				} else {
					String wbSyn = wbConfigName;
					WorkbookConfig wbConfig = wcdh.findBySynonym(wbSyn);
					wbConfigName = wbConfig.getConfig_name();
					Set<SheetConfig> sheets = wbConfig.getSheetConfigs();
					Iterator<SheetConfig> sit = sheets.iterator();
					while (sit.hasNext()) {
						SheetConfig shConfig = sit.next();
						shConfigName = shConfig.getSheet_name();
						String shSyn = shConfig.getSynonym();
						metaHeaders = getMetaHeaders(wbSyn,shSyn,session);
						cellHeaders = getCellHeaders(wbSyn,shSyn,session);
						header_data = addAll(metaHeaders,cellHeaders,SYNONYM);
						wbConfigName = wbConfig.getConfig_name();
						long count= doSQLCount(wbConfigName,shConfigName,trioList,conn,stmt,
								metaHeaders,cellHeaders,header_data,attachments,pos,session,page);					
						ConfigCounts cc = new ConfigCounts();
						cc.setCount(count);
						cc.setConfig(wbConfigName);
						tableData.add(cc);
					}
				}
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			//if (session != null && session.isConnected())
				//if (session.isOpen())
					//session.close();
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
			log.info("JDBC END: "+getTime());
		}
		return tableData;
	}

	/**
	 * Method to perform the query, save the results to a file, 
	 * and download the file from server to client
	 * 
	 * @param wbConfig String the name of the <WorkbookConfig>
	 * @param shConfig String the name of the <SheetConfig>
	 * @param trioList <List<CriteriaTrioDTO>> The header-operator-value trio
	 * @return <String> the path to the file
	 */
	public List<String> downloadSelect(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList) {
		List<List<String>> dataList = performSelect(wbConfig, shConfig, trioList, 0, 0);
		
		Session session = null;
		
		int numHeaders = 0;
		try {
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			wcdh.setSession(session);
			mhdh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			WorkbookConfig wConfig = wcdh.findBySynonym(wbConfig);
			List<MetadataHeader> headers = mhdh.findByConfig(wConfig);
			numHeaders = headers.size();
			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting headers. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
			
		log.warn("in downloadSelect - exporting data/creating excelFile");
		ExportData ed = new ExportData(numHeaders);
		String filePath = ed.createExcelFile(dataList, DEFAULT_WORKSHEET_NAME, wbConfig, shConfig, trioList);
		String fileName = filePath;
		List<String> fileAndPath = new ArrayList<String>();
		if (filePath != null) {
			int lastSep = filePath.lastIndexOf(File.separator);
			if (lastSep > 0)
				fileName = filePath.substring(lastSep+1);
			fileAndPath.add(fileName);
			fileAndPath.add(filePath);
			return fileAndPath;
		} else {
			log.warn("Problems creating Excel file");
			return null;
		}
	}

	/**
	 * Method to get the fileName based on the id of the spreadsheet
	 * 
	 * @param id <String> The id of the spreadsheet (workbook_id)
	 * @return <String> the path to the file
	 */
	public String getFileName(String id) {
		Session session = null;
		String filePath = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookFileDAOHibernate cmddh = new WorkbookFileDAOHibernate();
			cmddh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			long lid = 0;
			try {
				lid = Long.parseLong(id);
			} catch (NumberFormatException nfe) {
				// if parsing fails then lid will be zero.
			}

			filePath = cmddh.findPathByID(lid);

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return filePath;
	}

	/**
	 * Method to get the fileName based on the id of the spreadsheet
	 * 
	 * @param id <String> The id of the spreadsheet (workbook_id)
	 * @return <String> the path to the file
	 */
	public List<String> getFileNameAndPath(String id) {
		Session session = null;
		List<String> nameAndPath = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookFileDAOHibernate cmddh = new WorkbookFileDAOHibernate();
			WorkbookDAOHibernate wbddh = new WorkbookDAOHibernate();
			wbddh.setSession(session);
			cmddh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			long lid = 0;
			try {
				lid = Long.parseLong(id);
			} catch (NumberFormatException nfe) {
				// if parsing fails then lid will be zero.
			}
			
			WorkbookData wbData = wbddh.findById(lid, false);
			WorkbookFileData wbFile = wbData.getWorkbook_file_id();

			String filePath = wbFile.getPath();
			
			nameAndPath.add(filePath);
			
			String fileName = wbFile.getFilename();
			
			nameAndPath.add(fileName);

			tx.commit();
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return nameAndPath;
	}
	
	/*
	 * A method to get a list of operators
	 * 
	 * @param config_name String the name of the <workbookConfig>
	 * @param header_name String the name of the header
	 * @return List<String> a list of the operators
	 */
	public List<String> getOperators(String config_name, String header_name) {
		Session session = null;
		List<String> operators = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();

			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			OperatorsDAOHibernate odh = new OperatorsDAOHibernate();
			mdh.setSession(session);
			ctdh.setSession(session);
			wcdh.setSession(session);
			odh.setSession(session);

			session.beginTransaction();

			List<WorkbookConfig> configs = new ArrayList<WorkbookConfig>();
			if (config_name != null && !config_name.isEmpty()) {
				WorkbookConfig config = wcdh.findBySynonym(config_name);
				configs.add(config);
			} else {
				configs = wcdh.findAll();
			}
			
			Iterator<WorkbookConfig> wit = configs.iterator();
			boolean found = false;
			while (wit.hasNext() && !found) {
				WorkbookConfig config = wit.next();
				List<MetadataHeader> mdhs = mdh.findByConfig(config);
				Iterator<MetadataHeader> mit = mdhs.iterator();
				while (mit.hasNext() && !found) {
					MetadataHeader header = mit.next();
					if (header.getSynonym().trim().equals(header_name.trim())) {
						DataType dt = header.getTypeId();
						if (dt != null) {
							List<String> opers = odh.getByDataType(dt);
							Iterator<String> sit = opers.iterator();
							while (sit.hasNext() && !found) {
								String oper = sit.next();
								if (!operators.contains(oper))
								{
									operators.addAll(opers);
									found = true;
								}
							}
						}
					}
				}
				
				if (!found) {
					Set<SheetConfig> sheets = config.getSheetConfigs();
					Iterator<SheetConfig> sit = sheets.iterator();
					while (sit.hasNext() && !found) {
						SheetConfig sheet = sit.next();
						Set<CellDataHeader> cellHeaders = sheet.getCell_hdrs();
						Iterator<CellDataHeader> cit = cellHeaders.iterator();
						while (cit.hasNext() && !found) {
							CellDataHeader header = cit.next();
							if (header.getSynonym().trim().equals(header_name.trim())) {
								DataType dt = header.getTypeId();
								if (dt != null) {
									List<String> opers = odh.getByDataType(dt);
									Iterator<String> oit = opers.iterator();
									while (oit.hasNext() && !found) {
										String oper = oit.next();
										if (!operators.contains(oper))
										{
											operators.addAll(opers);
											found = true;
										}
									}
								}
							}
						}
					}
				}
			}

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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return operators;
	}
	
	/*
	 * A method to get a list of operators
	 * 
	 * @param config_name String the name of the <workbookConfig>
	 * @param header_name String the name of the header
	 * @return List<String> a list of the operators
	 */
	public List<String> getOperators1(String config_name, String header_name) {
		List<String> operators = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try {
			String sql = "select distinct(o.operator) from operators o, meta_data_hdr m, workbook_config w"
			       + " where o.data_type_id = m.type_id and"
			       + " m.workbook_config_id = w.workbook_config_id and"
				   + " m.synonym = '" + header_name + "' ";
			if (config_name != null && !config_name.isEmpty())
				sql += "and w.synonym = '"+config_name+"' ";
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = susi.db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = susi.db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += susi.db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += susi.db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String operator = rs.getString(1);
				if (!operators.contains(operator))
					operators.add(operator);
			}
			rs.close();
			if (operators.isEmpty()) {
				sql = "select distinct(o.operator) from operators o, "
				    + "cell_data_hdr cd,data_type dt,workbook_config wc, "
				    + "workbook_config_sheet_join wj,sheet_config sc "
				    + "where o.data_type_id=cd.type_id and "
				    + "cd.sheet_config_id = sc.sheet_config_id and " 
				    + "wc.workbook_config_id = wj.workbook_config_id and "
				    + "sc.sheet_config_id = wj.sheet_config_id and ";
				if (config_name != null && !config_name.isEmpty())
				    sql += "wc.synonym = '"+config_name+"' and ";
				sql += "cd.synonym = '"+header_name+"'";				
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String operator = rs.getString(1);
					if (!operators.contains(operator))
						operators.add(operator);
				}
				rs.close();
			}
		} catch( SQLException se ) {
	      System.out.println( "SQL Exception:" ) ;
		  log.warn(getStackTrace(se));
	      // Loop through the SQL Exceptions
	      //while( se != null )
	      //{
	          System.out.println( "Message: " + se.getMessage()   ) ;
	          System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
	      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
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
		return operators;
	}
	
	/*
	 * A method to retreive the urls for the other available applications
	 * 
	 * @return List<NameValue> the list of urls
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
			//log.error("Hibernate exception on getting urls. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				//log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		
		
		return urls;
	}

	/*
	 * Retrieves the title of the application
	 * 
	 * @return String the title
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		
		
		return title;
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
	 *  returns the name of the workbook config
	 *  
	 *   @param String synonym the synonym of the workbook config
	 *   @return String The name of the workbook config
	 */
	private String getWorkbookNameFromSynonym(String synonym) {
		String wbConfig = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = sidh.findBySynonym(synonym);
			if (config != null) {
				wbConfig = config.getConfig_name();
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return wbConfig;
	}
	
	/**
	 *  returns the name of the workbook config
	 *  
	 *   @param String synonym the synonym of the workbook config
	 *   @return String The name of the workbook config
	 */
	private String getWorkbookSynonymFromName(String name) {
		String synonym = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = sidh.findByName(name);
			if (config != null) {
				synonym = config.getSynonym();
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return synonym;
	}
	
	/**
	 *  returns the name of the workbook config
	 *  
	 *   @param String synonym the synonym of the workbook config
	 *   @return String The name of the workbook config
	 */
	private String getWorkbookSynonymFromName(String name, Session session) {
		String synonym = "";
		//Session session = null;
		try {
			//session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			//Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = sidh.findByName(name);
			if (config != null) {
				synonym = config.getSynonym();
			}
			
			//tx.commit();
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
				if (session.isOpen()) {
					//session.flush();
					//session.close();
				}
		}
		return synonym;
	}
	
	/**
	 *  returns the name of the workbook config
	 *  
	 *   @param String synonym the synonym of the workbook config
	 *   @return String The name of the workbook config
	 */
	/*
	private String getSheetSynonymFromName(String wbName, String shName) {
		String synonym = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = sidh.findByName(wbName);
			if (config != null) {
				Set<SheetConfig> shConfigs = config.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					if (shConfig.getSheet_name().equals(shName)) {
						synonym = shConfig.getSynonym();
						break;
					}
				}
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return synonym;
	}
	*/
	/**
	 *  returns the name of the workbook config
	 *  
	 *   @param String synonym the synonym of the workbook config
	 *   @return String The name of the workbook config
	 */
	private String getSheetSynonymFromName(String wbName, String shName, Session session) {
		String synonym = "";
		//Session session = null;
		try {
			//session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			//Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = sidh.findByName(wbName);
			if (config != null) {
				Set<SheetConfig> shConfigs = config.getSheetConfigs();
				Iterator<SheetConfig> sit = shConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig shConfig = sit.next();
					if (shConfig.getSheet_name().equals(shName)) {
						synonym = shConfig.getSynonym();
						break;
					}
				}
			}
			
			//tx.commit();
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
				if (session.isOpen()) {
					//session.flush();
					//session.close();
				}
		}
		return synonym;
	}
	
	/**
	 *  gets the workbook config from the workbook id
	 *  
	 *   @param id of the workbook 
	 *   @return config name for the workbook
	 */
	public String getWorkbookConfigFromWorkbook(long id) {
		String wbConfig = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookDAOHibernate wddh = new WorkbookDAOHibernate();
			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			wddh.setSession(session);
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			WorkbookData wbd = wddh.findById(id, false);
			WorkbookConfig wbc = wbd.getWorkbook_config_id();
			wbConfig = wbc.getConfig_name();
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return wbConfig;
	}
	
	/**
	 *  gets the workbook config from the table name
	 *  
	 *   @param table name of the table
	 *   @return table name of the workbook
	 */
	private String getWbFromTable(String table) {
		String wbConfig = "";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			int index = table.indexOf(DATA_TABLE_ENDING);
			table = table.substring(0,index);
			
			WorkbookConfigDAOHibernate sidh = new WorkbookConfigDAOHibernate();
			sidh.setSession(session);
			Transaction tx = session.beginTransaction();
			
			String[] parts = table.split("_");
			
			int ctr=0;
			String part = "";
			while (ctr < parts.length) {
				if (ctr > 0)
					part += "_";
				part += parts[ctr++];
				WorkbookConfig config = sidh.findByName(part);
				if (config != null) {
					wbConfig = config.getConfig_name();
					break;
				}
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
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
		}
		return wbConfig;
	}
	
	/**
	 * Public static method to return a stack trace
	 * 
	 * @param t
	 * @return <String> stack track
	 */
	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        //StringWriter sw1 = new StringWriter();
        int start = sw.toString().indexOf("at gov.nrel.nbc.");
        if (start < 0) start = 0;
        int end = sw.toString().lastIndexOf("at gov.nrel.nbc.");
        if (end < 0) end = sw.toString().length();
        String s = sw.toString().substring(start,end);
        return s;
    }

}
