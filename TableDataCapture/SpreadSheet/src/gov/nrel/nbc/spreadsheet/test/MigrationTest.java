package gov.nrel.nbc.spreadsheet.test;


import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.dao.CellDataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.RowDataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.CellData;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.Metadata;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.RowData;
import gov.nrel.nbc.spreadsheet.dto.SheetData;
import gov.nrel.nbc.spreadsheet.dto.ValueData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

public class MigrationTest extends TestCase implements AppConstants {

	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MigrationTest.class);
	}
	
	public void testMigrate() throws Exception {
			Connection conn = null;
			Statement stmt = null;
			Session session = null;
			int ret = 0;
			try {
				Class.forName( MYSQL_DRIVER ) ;
				SpreadSheetUploadServiceImpl susi = new SpreadSheetUploadServiceImpl(); 			
		        String url = susi.db_first;
		        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
		        	url += DevTestProdConstants.DEV;
		        else
		        	url += DevTestProdConstants.DEV_PROD_TEST;
		        url += susi.db_last;
				String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
				String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
		        conn = DriverManager.getConnection(url,user,pass);
				stmt = conn.createStatement() ;
				String sql = "";
				
				session = HibernateSessionFactory.getSession();

				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
				WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
				SheetDAOHibernate sdh = new SheetDAOHibernate();
				RowDataDAOHibernate rddh = new RowDataDAOHibernate();
				CellDataDAOHibernate cdd = new CellDataDAOHibernate();
				MetadataDAOHibernate mdd = new MetadataDAOHibernate();
				WorkbookDAOHibernate wbd = new WorkbookDAOHibernate();
				rddh.setSession(session);
				mdd.setSession(session);
				wbd.setSession(session);
				cdd.setSession(session);
				sdh.setSession(session);
				mdh.setSession(session);
				ctdh.setSession(session);
				wcdh.setSession(session);

				session.beginTransaction();

				List<WorkbookData> wbds = wbd.findAll();
				Iterator<WorkbookData> wit = wbds.iterator();
				while (wit.hasNext()) {
					WorkbookData wb = wit.next();
					WorkbookConfig wbc = wb.getWorkbook_config_id();
					if (wbc != null) {
						String wbConfig = wbc.getConfig_name();
						String table = wbConfig+"_Digestion_data";
						Set<SheetData> sheets = wb.getSheets();
						Iterator<SheetData> sit = sheets.iterator();
						while (sit.hasNext()) {
							SheetData sd = sit.next();
							long sid = sd.getSheet_config_id().getSheet_config_id();
							// get all cells
							List<RowData> rows = rddh.findBySheet(sd);
							Iterator<RowData> rit = rows.iterator();
							while (rit.hasNext()) {
								RowData row = rit.next();
								long rid = row.getId();
								String column_part = "";
								String value_part = " values (";
								List<MetadataHeader> mdhs = mdh.findByConfig(wbc);
								Iterator<MetadataHeader> mit = mdhs.iterator();
								while (mit.hasNext()) {
									MetadataHeader header = mit.next();
									Metadata md = mdd.findByHeader(header, wb);
									if (column_part.length()!=0)
										column_part += ", ";
									column_part += "`"+header.getName()+"`";
									ValueData gd = md.getValue();
									DataType dt = header.getTypeId();
									String value = "";
									int type = (int)dt.getId();
									switch (type) {
										case 1: if (gd.getLvalue() != null) value = String.valueOf(gd.getLvalue()); else value = null;break;
										case 2: if (gd.getRvalue() != null) value = String.valueOf(gd.getRvalue()); else value = null;break;
										case 3: 
											if (gd.getDvalue() != null) {
												Date ddvalue = gd.getDvalue();
												Calendar cal = Calendar.getInstance();
												cal.setTime(ddvalue);
												int year = cal.get(Calendar.YEAR);
												int mon = cal.get(Calendar.MONTH)+1;
												String smon = "";
												if (mon < 10) 
													smon = "0";
												smon += String.valueOf(mon);
												int day = cal.get(Calendar.DAY_OF_MONTH);
												//log.info("day = "+day);
												String sday = "";
												if (day < 10) 
													sday = "0";
												sday += String.valueOf(day);
												value = String.valueOf(year) + "-" + smon + "-" + sday;
											} else value = null;
											break;
										case 4: if (gd.getSvalue() != null) value = gd.getSvalue(); else value = null; break;
										case 5: if (gd.getBvalue() != null) value = String.valueOf(gd.getBvalue()); else value = null; break;
									}
									if (!value_part.equals(" values ("))
										value_part += ", ";
									if (value != null) {
										if (type == 1 || type == 2)
											value_part += " "+value+" ";
										else
											value_part += " '"+ value +"' ";
									}
									else
										value_part += " null ";
								}
								column_part += ", row_data_id, sheet_config_id";
								value_part += ", "+rid+", "+sid;
								List<CellData> cds = cdd.findByRowId(row);
								Iterator<CellData> cdit = cds.iterator();
								while (cdit.hasNext()) {
									// for each cell, get type and value
									CellData cd = cdit.next();
									CellDataHeader cdh = cd.getCell_hdr_id();
									column_part += ", `"+cdh.getName()+"`";
									DataType dt = cdh.getTypeId();
									int type = (int)dt.getId();
									ValueData gd = cd.getValueId();
									String value = "";
									switch (type) {
										case 1: if (gd.getLvalue() != null) value = String.valueOf(gd.getLvalue()); else value = null;break;
										case 2: if (gd.getRvalue() != null) value = String.valueOf(gd.getRvalue()); else value = null;break;
										case 3: 
											if (gd.getDvalue() != null) {
												Date ddvalue = gd.getDvalue();
												Calendar cal = Calendar.getInstance();
												cal.setTime(ddvalue);
												int year = cal.get(Calendar.YEAR);
												int mon = cal.get(Calendar.MONTH)+1;
												String smon = "";
												if (mon < 10) 
													smon = "0";
												smon += String.valueOf(mon);
												int day = cal.get(Calendar.DAY_OF_MONTH);
												//log.info("day = "+day);
												String sday = "";
												if (day < 10) 
													sday = "0";
												sday += String.valueOf(day);
												value = String.valueOf(year) + "-" + smon + "-" + sday;
											} else value = null;
											break;
										case 4: if (gd.getSvalue() != null) value = gd.getSvalue(); else value = null; break;
										case 5: if (gd.getBvalue() != null) value = String.valueOf(gd.getBvalue()); else value = null; break;
									}
									if (value != null) {
										if (type == 1 || type == 2)
											value_part += ", "+value+" ";
										else
											value_part += ", '"+ value +"' ";
									}
									else
										value_part += ", null ";
								}
								column_part += ")";
								value_part += ")";
								sql = "insert into "+table+" ("+column_part+value_part;
								System.out.println("sql="+sql);
								try {
									stmt.executeUpdate(sql);
								}catch( SQLException se ) {
								      System.out.println( "SQL Exception:" ) ;
								      System.out.println(se);
								}
							}
						}
					}
				}
				
				System.out.println(ret);
			}catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
			      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
			      // Loop through the SQL Exceptions
			      while( se != null )
			      {
			          System.out.println( "Message: " + se.getMessage()   ) ;
			          se = se.getNextException() ;
			      }
			} catch (HibernateException he) {
				System.out.println("Hibernate exception on getting headers. error: " + he);
				he.printStackTrace();
				try {
					if (session != null && session.isConnected())
						session.getTransaction().rollback();
				} catch (HibernateException rbEx) {
					System.out.println("Couldn't roll back transaction! Error: " + rbEx);
				}
				return;
			} catch (Exception e) {
				System.out.println("Exception on getting type. error: " + e);
				System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
			} finally {
				if (session != null && session.isConnected())
					if (session.isOpen())
						session.close();
				// Close the result set, statement and the connection
				try {
					stmt.close() ;
					conn.close() ;
					//tx.commit();
				} catch( SQLException se ) {
				      System.out.println( "SQL Exception:" ) ;
				      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
				} catch (Exception e) {
					System.out.println("Exception on getting type. error: " + e);
					System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
				}
			}
	}
}
