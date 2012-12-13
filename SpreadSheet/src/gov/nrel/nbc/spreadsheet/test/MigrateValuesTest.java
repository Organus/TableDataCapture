package gov.nrel.nbc.spreadsheet.test;


import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class MigrateValuesTest extends TestCase implements AppConstants {

	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MigrateValuesTest.class);
	}
	
	public void testMigrate1() throws Exception {
			Connection conn = null;
			Statement stmt = null;
			int ret = 0;
			try {
				// Load the database driver
				Class.forName( MYSQL_DRIVER ) ;
				// Get a connection to the database
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
				// Get a statement from the connection
				stmt = conn.createStatement() ;
				
				String sql = "insert into value_data (data_id, data_type, lvalue) "+
							"select generic_data_id,'1',value from long_data";
				try {
					ret = stmt.executeUpdate( sql ) ;
				} catch (MySQLIntegrityConstraintViolationException cve) {
					System.out.println("Constraint violation. Value already added.");
				}
				sql = "insert into value_data (data_id, data_type, rvalue) "+
				"select generic_data_id,'2',value from real_data";
				try {
					ret = stmt.executeUpdate( sql ) ;
				} catch (MySQLIntegrityConstraintViolationException cve) {
					System.out.println("Constraint violation. Value already added.");
				}
				sql = "insert into value_data (data_id, data_type, dvalue) "+
				"select generic_data_id,'3',value from date_data";
				try {
					ret = stmt.executeUpdate( sql ) ;
				} catch (MySQLIntegrityConstraintViolationException cve) {
					System.out.println("Constraint violation. Value already added.");
				}
				sql = "insert into value_data (data_id, data_type, svalue) "+
				"select generic_data_id,'4',value from string_data";
				try {
					ret = stmt.executeUpdate( sql ) ;
				} catch (MySQLIntegrityConstraintViolationException cve) {
					System.out.println("Constraint violation. Value already added.");
				}
				sql = "insert into value_data (data_id, data_type, bvalue) "+
				"select generic_data_id,'5',value from boolean_data";
				try {
					ret = stmt.executeUpdate( sql ) ;
				} catch (MySQLIntegrityConstraintViolationException cve) {
					System.out.println("Constraint violation. Value already added.");
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
			} catch (Exception e) {
				System.out.println("Exception on getting type. error: " + e);
				System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
			} finally {
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
