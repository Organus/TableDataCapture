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

public class ChangesSlashesTest  extends TestCase implements AppConstants {

	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
		thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	public static Test suite() {
		return new TestSuite(ChangesSlashesTest.class);
	}
	
	public void testChangeSlashes() throws Exception {
		Connection connTest = null;
		Statement stmtTest1 = null;
		Statement stmtTest2 = null;
		int ret = 0;
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
			SpreadSheetUploadServiceImpl susi = new SpreadSheetUploadServiceImpl(); 			
	        String urlTest = susi.db_first;
	        urlTest += DevTestProdConstants.TEST;
	        urlTest += susi.db_last;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        connTest = DriverManager.getConnection(urlTest,user,pass);
			// Get a statement from the connection
			stmtTest1 = connTest.createStatement() ;
			stmtTest2 = connTest.createStatement() ;
	
		    String wbFileSql = "select w1.path, w1.workbook_file_id from workbook_file_data w1";
			ResultSet rsTest = stmtTest1.executeQuery(wbFileSql);
			while( rsTest.next() ) {
			    String oldPath = rsTest.getString(1);
			    long wbfid = rsTest.getLong(2);
			    String newPath = oldPath.replaceAll("\\\\", "/"); 
				String updateSql = "update workbook_file_data set "
								+" path = '"+newPath+"' where workbook_file_id = "+wbfid;
				try {
					ret = stmtTest2.executeUpdate(updateSql);
				} catch( SQLException se ) {
				      System.out.println( "SQL Exception:"+se.getMessage() ) ;
				}
			}
			rsTest.close();
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
				if (stmtTest2 != null)
					stmtTest2.close();
				if (connTest != null)
					connTest.close();
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