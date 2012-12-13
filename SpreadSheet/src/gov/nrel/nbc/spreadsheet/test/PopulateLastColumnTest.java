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

public class PopulateLastColumnTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(PopulateLastColumnTest.class);
	}
	
	public void testPopulateLastColumnTest() throws Exception {
			Connection connTest = null;
			Statement stmtTest1 = null;
			Statement stmtTest2 = null;
			Statement stmtTest3 = null;
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
				String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
				String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
		        connTest = DriverManager.getConnection(urlTest,user,pass);
				// Get a statement from the connection
				stmtTest1 = connTest.createStatement() ;
				stmtTest2 = connTest.createStatement() ;
				stmtTest3 = connTest.createStatement() ;
				
				String sampleSQL = "select `cell_data_id`, `Work Type`,`Acid Concentration (%)`, `Temperature (deg. C)`, `time (min)`,`Reactor/Temperature/[Acid]/Time` from `Mels Total Mass Closure_Total Mass Closure_data`";
				ResultSet rsTest = stmtTest1.executeQuery( sampleSQL ) ;
				while (rsTest.next()) {
					long id = rsTest.getLong(1);
					String workType = rsTest.getString(2);
					double acid = rsTest.getDouble(3);
					long temp = rsTest.getLong(4);
					double time = rsTest.getDouble(5);
					String rtat = rsTest.getString(6);
					if (workType.equals("Steam Gun"))
						workType = "SG";
					else if (workType.equals("Zip Clave"))
						workType = "ZC";
					String newRtat = workType+"/"+(acid*100)+"%/"+temp+"/"+time;
					try {
						if (rtat != null && !rtat.isEmpty()) {
							System.out.println("0");
						}
						else {
							String updateSQL = "update `Mels Total Mass Closure_Total Mass Closure_data`"
											+ " set `Reactor/Temperature/[Acid]/Time` = '"+newRtat+"' where cell_data_id = "+id;
							ret = stmtTest3.executeUpdate(updateSQL);
		 					System.out.println(ret);
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
					stmtTest3.close() ;
					connTest.close() ;
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
