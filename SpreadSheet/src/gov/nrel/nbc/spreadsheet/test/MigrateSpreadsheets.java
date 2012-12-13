package gov.nrel.nbc.spreadsheet.test;


import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class MigrateSpreadsheets  extends TestCase implements AppConstants {

	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MigrateSpreadsheets.class);
	}
	
	public void testMigrateSpreadsheets() throws Exception {
		//Connection connTest = null;
		Connection connProd = null;
		Connection connTest = null;
		Statement stmtProd1 = null;
		Statement stmtProd2 = null;
		Statement stmtTest1 = null;
		int ret = 0;
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
			SpreadSheetUploadServiceImpl susi = new SpreadSheetUploadServiceImpl(); 			
	        String urlTest = susi.db_first;
	        urlTest += DevTestProdConstants.TEST;
	        urlTest += susi.db_last;
	        String urlProd = susi.db_first;
	        urlProd += DevTestProdConstants.PROD;
	        urlProd += susi.db_last;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        connTest = DriverManager.getConnection(urlTest,user,pass);
	        connProd = DriverManager.getConnection(urlProd,user,pass);
			// Get a statement from the connection
			stmtProd1 = connProd.createStatement() ;
			stmtProd2 = connProd.createStatement();
			stmtTest1 = connTest.createStatement() ;

		    String service_url = "http://nbcappstest/spreadsheet/clientUploadService";
		    String webXML = "C:\\projects\\james\\SpreadSheet\\war\\WEB-INF\\web.xml";
		    File fXML = new File(webXML);
		    ServletRunner sr = new ServletRunner(fXML);
		    sr.registerServlet("/spreadsheet/clientUploadService",SpreadSheetUploadServiceImpl.class.getName());
		    ServletUnitClient sc = sr.newClient();
		    WebRequest request = null;
		    String wbFileSql = "select wd.workbook_id, wc.config_name, w1.filename, w1.path "
		    				  +" from workbook_file_data w1, workbook_config wc, workbook_data wd "
		    				  +" where wd.workbook_file_id = w1.workbook_file_id and "
		    				  +" wd.workbook_config_id = wc.workbook_config_id ";
			ResultSet rsProd = stmtProd1.executeQuery(wbFileSql);
			String config_name = null;
			while( rsProd.next() ) {
			    long workbook_id = rsProd.getLong(1);
				config_name = rsProd.getString(2);
				String filename = rsProd.getString(3);
				String inFile = rsProd.getString(4);	
				String checkSql = "select w1.filename from workbook_file_data w1 where "
								+" w1.filename like '%"+filename + "'";
				ResultSet rsTest1 = stmtTest1.executeQuery(checkSql);
				String checkFile = null;
				while( rsTest1.next() ) {
					checkFile = rsTest1.getString(1);
				}
				rsTest1.close();
				if (checkFile == null) {
					request = new PostMethodWebRequest(service_url, true);
				    if (config_name != null) {
				    	String sql = "select mdh.name, vd.lvalue, vd.rvalue, vd.dvalue, "
				    				+" vd.svalue, mdh.type_id from "
				    				+" meta_data md, meta_data_hdr mdh, value_data vd " 
				    				+" where "
				    				+" md.metadata_hdr_id = mdh.hdr_id and "
				    				+" md.value = vd.data_id and "
				    				+" md.workbook_id = "+workbook_id; 
						ResultSet rsTest = stmtProd2.executeQuery(sql);
						while( rsTest.next() ) {
							String metaname = rsTest.getString(1);
							long lval = rsTest.getLong(2);
							double rval = rsTest.getDouble(3);
							Date dval = rsTest.getDate(4);
					    	String sval = rsTest.getString(5);
					    	String value = "";
					    	long type = rsTest.getLong(6);
					    	if (type == LONG)
					    		value = String.valueOf(lval);
					    	else if (type == REAL)
					    		value = String.valueOf(rval);
					    	else if (type == STRING)
					    		value = sval;
					    	else if (type == DATE) {
								Date collectionTime = dval;
								if (dval != null)
								{
									GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
									cal.setTime(collectionTime);
									value = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
								} else
									value = null;
					    	}
							request.setParameter(metaname, value);
					    }
						rsTest.close();
					    request.setParameter("fileName",inFile);
					    request.selectFile("fileName", new File(inFile));
					    request.setParameter("workbookConfigName", config_name);
					    request.selectFile("excelFile", new File(inFile));
					    try
					    {
					         // Use the InvocationContext to create an instance
					         // of the servlet
					         InvocationContext ic = sc.newInvocation(request);
					         SpreadSheetUploadServiceImpl cfuServlet = null;
					    	 try {
						         cfuServlet = (SpreadSheetUploadServiceImpl)ic.getServlet();
					    	 }
					    	 catch (Exception e2) {System.out.println("Error initializing cdServlet. Exception is " + e2); }
					         HttpServletRequest cdServletRequest = ic.getRequest();
					         HttpServletResponse cdServletResponse = ic.getResponse();
					         if (cfuServlet != null) {
					        	 try {
					        		 cfuServlet.doPost(cdServletRequest,cdServletResponse);
					        	 } catch (Exception e5) {
					        		 System.out.println("Error calling servlet.doPost. Exception is " + e5);
					        		 e5.printStackTrace();
					        	 }
					         }
					    }
					    catch (Exception e) {
					         System.out.println("Error testing CalcFileUploadServiceImpl. Exception is " + e); 
					    	 e.printStackTrace(); 
					    }
				    }
				} else
					System.out.println(checkFile + " already added");
			}
			rsProd.close();

			System.out.println(ret);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
		      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			System.out.println("Exception on getting type. error: " + e);
			System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmtTest1 != null)
					stmtTest1.close();
				if (stmtProd1 != null)
					stmtProd1.close() ;
				if (stmtProd2 != null)
					stmtProd2.close();
				if (connTest != null)
					connTest.close();
				if (connProd != null)
					connProd.close();
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
	public static void main(String args[]) {
		MigrateSpreadsheets ms = new MigrateSpreadsheets();
		try {
			ms.testMigrateSpreadsheets();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
}
