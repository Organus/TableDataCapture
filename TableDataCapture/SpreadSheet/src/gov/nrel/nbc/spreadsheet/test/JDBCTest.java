package gov.nrel.nbc.spreadsheet.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;


public class JDBCTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(JDBCTest.class);
	}
	
	public void testCreateStoredProc() throws Exception {
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
        Connection conn = DriverManager.getConnection(url,user,pass);
		// Get a statement from the connection
		//Statement stmt = conn.createStatement() ;
        ///*
        String createProcedure = 
        "CREATE PROCEDURE REAL_SRCH "+
        "BEGIN "+
        "SET @s = "+
		"(select ch.name, ch.hdr_index, wd.workbook_id, rd.sheet_data_id, rd.row_num, "+
		"dd.value, wc.config_name from real_data dd, generic_data gd,cell_data "+
		"cd,cell_data_hdr ch,row_data rd,sheet_data sd, sheet_config sc, workbook_data "+
		"wd, workbook_config wc where (dd.generic_data_id=gd.generic_data_id) and "+
		"cd.value_id=gd.generic_data_id and cd.row_data_id = rd.row_data_id and "+
		"sd.sheet_data_id=rd.sheet_data_id and sd.workbook_id=wd.workbook_id and "+
		"wd.workbook_config_id = wc.workbook_config_id and ch.cell_hdr_id = "+
		"cd.cell_hdr_id and sd.sheet_config_id = sc.sheet_config_id and sd.sheet_data_id "+
		"in (198,199) and rd.row_num in (7) order by wd.workbook_id desc, "+
		"rd.sheet_data_id desc, rd.row_num desc, ch.hdr_index desc); "+
        "PREPARE stmt FROM @s; "+
        "EXECUTE stmt; "+
        "END ";
        //*/
        /*
        String createProcedure = "create procedure SHOW_REALS " + "as " + 
				"(select ch.name, ch.hdr_index, wd.workbook_id, rd.sheet_data_id, rd.row_num, "+
				"dd.value, wc.config_name from real_data dd, generic_data gd,cell_data "+
				"cd,cell_data_hdr ch,row_data rd,sheet_data sd, sheet_config sc, workbook_data "+
				"wd, workbook_config wc where (dd.generic_data_id=gd.generic_data_id) and "+
				"cd.value_id=gd.generic_data_id and cd.row_data_id = rd.row_data_id and "+
				"sd.sheet_data_id=rd.sheet_data_id and sd.workbook_id=wd.workbook_id and "+
				"wd.workbook_config_id = wc.workbook_config_id and ch.cell_hdr_id = "+
				"cd.cell_hdr_id and sd.sheet_config_id = sc.sheet_config_id and sd.sheet_data_id "+
				"in (198,199) and rd.row_num in (7) order by wd.workbook_id desc, "+
				"rd.sheet_data_id desc, rd.row_num desc, ch.hdr_index desc) ";
		*/
		Statement stmt1 = conn.createStatement(); 
		stmt1.executeUpdate(createProcedure);

		//CallableStatement cs = conn.prepareCall("{call SHOW_SUPPLIERS}"); 
		//ResultSet rs = cs.executeQuery();
		stmt1.close();
		conn.close();
	}
}
