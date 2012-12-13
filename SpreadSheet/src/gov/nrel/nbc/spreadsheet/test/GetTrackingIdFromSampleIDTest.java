package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class GetTrackingIdFromSampleIDTest extends TestCase implements AppConstants {

	private final String TRACKERUSER = "user";
	private final String TRACKERPASS = "password";
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(GetTrackingIdFromSampleIDTest.class);
	}
	
	public void testGetTrackingIdFromSampleId() throws Exception {
			Connection connTest = null;
			Connection connProd = null;
			Statement stmtTest1 = null;
			Statement stmtProd = null;
			Statement stmtTest2 = null;
			int ret = 0;
			try {
				// Load the database driver
				Class.forName( MYSQL_DRIVER ) ;
				// Get a connection to the database
				SpreadSheetUploadServiceImpl susi = new SpreadSheetUploadServiceImpl(); 			
		        String urlTest = susi.db_first;
		        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
		        	urlTest += DevTestProdConstants.DEV;
		        else
		        	urlTest += DevTestProdConstants.DEV_PROD_TEST;
		        urlTest += susi.db_last;
		        String urlProd = susi.db_first;
	        	urlProd += DevTestProdConstants.PROD;
		        urlProd += susi.db_tracker;
				String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
				String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
		        connTest = DriverManager.getConnection(urlTest,user,pass);
		        connProd = DriverManager.getConnection(urlProd,TRACKERUSER,TRACKERPASS);
				// Get a statement from the connection
				stmtTest1 = connTest.createStatement() ;
				stmtTest2 = connTest.createStatement() ;
				
				stmtProd = connProd.createStatement() ;
				
				String sampleSQL = "select `sample ID`,cell_data_id, `tracking ID` from `Mels Total Mass Closure_Total Mass Closure_data`";
				ResultSet rsTest = stmtTest1.executeQuery( sampleSQL ) ;
				while (rsTest.next()) {
					String sampleID = rsTest.getString(1);
					long id = rsTest.getLong(2);
					String oldTrackingId = rsTest.getString(3);
					int dash = sampleID.indexOf('-');
					if (dash < 5 && dash > -1)
						sampleID = sampleID.substring(0,dash)+sampleID.substring(dash+1);
					dash = sampleID.lastIndexOf('-');
					if (dash == -1)
						dash = sampleID.lastIndexOf(' ');
					if (dash != -1)
						sampleID = sampleID.substring(0,dash)+"%"+sampleID.substring(dash+1,dash+2);
					String trackerSQL = "select id from sample where sample_id like '"+sampleID+"'";
					try {
						ResultSet rsProd = stmtProd.executeQuery(trackerSQL);
						while (rsProd.next()) {
							long trackingID = rsProd.getLong(1);
							if (oldTrackingId == null) {
								String updateSQL = "update `Mels Total Mass Closure_Total Mass Closure_data`"
												+ " set `Tracking ID` = "+trackingID+" where cell_data_id = "+id;
								ret = stmtTest2.executeUpdate(updateSQL);
		 						System.out.println(ret);
							}
						}
					} catch( SQLException se ) {
					      System.out.println( "SQL Exception:" ) ;
					      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
					      // Loop through the SQL Exceptions
					      while( se != null )
					      {
					          System.out.println( "Message: " + se.getMessage()   ) ;
					          se = se.getNextException() ;
					      }
					} 
				}
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
					stmtTest1.close() ;
					stmtTest2.close() ;
					connTest.close() ;
					stmtProd.close() ;
					connProd.close() ;
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
