package gov.nrel.nbc.spreadsheetadmin.test;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelParser;
import gov.nrel.nbc.spreadsheetadmin.parse.RowDTO;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;

public class ExcelParserTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	ExcelParser parser;
	String configName;
	String sheetName;
	String fileName;
	Session session;
	File f;
	int rowCount;
	int headerCount;
	
	
	
	@Override
	protected void setUp() {
    	thisLoader = ExcelParser.class.getClassLoader();
		ResourceBundle rBundle = ResourceBundle.getBundle(SPREADSHEET_PROPERTIES_FILE_NAME);

        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.local.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.local.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.local.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.dev.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.dev.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.dev.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.prod.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.prod.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.prod.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.test.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.test.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.test.logDirectory"));

        }
        
		fileName = System.getProperty("java.io.tmpdir") + "nir2-b.xls";
		configName = "BooleanTest1";
		sheetName = "ingest";
		
/*		System.out.println(Long.toString(System.currentTimeMillis())+": testXLSXParser open");
		System.out.println("fileName = " + fileName);
		System.out.println("configName = " + configName);
		System.out.println("sheetName = " + sheetName);
*/	
		session = HibernateSessionFactory.getSession();
		f = new File(fileName);

		rowCount = 24;
		headerCount = 31;
	}
	
	protected void tearDown(){
		session.close();

	}
	
	public static Test suite() {
		return new TestSuite(ExcelParserTest.class);
	}

	public void testGetSheets() throws Exception {
		
		parser = new ExcelParser(f,null,session);
		
		List<String> sheets = parser.getSheets();
		assertNotNull(sheets);
		assertTrue(sheets.size()>0);
		for (int i=0;i<sheets.size();i++)
			System.out.println("workbook has sheet: "+sheets.get(i));
		assertTrue(sheets.get(0).equals(sheetName));
		System.out.println(Long.toString(System.currentTimeMillis())+"testGetSheets pass");
	}
	
/*	public void testGetMetadataNames() throws Exception{
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName(configName);
		parser = new ExcelParser(f,config,session);
		assertNotNull(parser);
		
		List<NameValue> mdNames = parser.getMetadataNames(sheetName);
		assertNotNull(mdNames);
		System.out.println(Long.toString(mdNames.size()));
		System.out.println(Long.toString(System.currentTimeMillis())+": Metadata names should be empty.");

		System.out.println(Long.toString(System.currentTimeMillis())+"testGetMetadataNames pass");
		
	}
*/
	
	/**
	 * Confirmation that getHeaders returns a non-null 
	 * list with 31 members.
	 * 
	 */
	public void testGetHeaders() throws Exception{
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName(configName);
		parser = new ExcelParser(f,config,session);
		assertNotNull(parser);
		
		List<String> headers = parser.getHeaders(sheetName);
		assertTrue(headers.size()==headerCount);
		System.out.println("Headers");
		
		assertTrue(headers.size()==headerCount);

		for (int i=0;i<headers.size();i++) {
			System.out.print("#"+i+" "+ headers.get(i) + ", ");
		}
		System.out.println();
		
		System.out.println(Long.toString(System.currentTimeMillis())+"testGetHeaders pass");
		
	}
	
	/**
	 *  	The getData() method returns a COMPACTED table.
	 *  
	 *  	If a cell is null it will not be included in the row,
	 *	so the position of the DataValue does not correspond to
	 *  position of the header: null must be accounted for by
	 *  subtraction.
	 * 
	 */
	public void testGetData() throws Exception{
		
		
		int sumNullHeaders = 1;
		
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName(configName);
		parser = new ExcelParser(f,config,session);
		assertNotNull(parser);
		
		List<RowDTO> rows = parser.getData(sheetName);
		assertTrue(rows.size()==rowCount);
		System.out.println("Data");
	
		//first row, position 0, 1, last
		assertTrue(rows.get(0).getRow().get(0).getValue().getLvalue()==1);
		System.out.println(rows.get(0).getRow().get(1).getValue().getSvalue());
		assertTrue(rows.get(0).getRow().get(1).getValue().getSvalue().equals("\"pcs93 ck cel\""));
		assertTrue(rows.get(0).getRow().get(headerCount -1).getValue().getRvalue()==1.5470015);
		System.out.println("First data row good.");
		
		//last row, position 0, 1, last
		assertTrue(rows.get(rowCount-1).getRow().get(0).getValue().getLvalue()==0);
		assertTrue(rows.get(rowCount-1).getRow().get(1).getValue().getSvalue()==null);
//		System.out.println((int) rows.get(rowCount-1).getRow().get(2).getValue().getBvalue().compareTo(new Boolean(false)));
		assertTrue(rows.get(rowCount-1).getRow().get(2).getValue().getBvalue().compareTo(new Boolean(false))==0);
		System.out.println("rows "+rows.size());
		System.out.println("headers "+rows.get(rows.size()-1).getRow().size());
		System.out.println(rows.get(rowCount-1).getRow().get(headerCount -1 -sumNullHeaders).getValue().getRvalue());
		assertTrue(rows.get(rowCount-1).getRow().get(headerCount -1 -sumNullHeaders).getValue().getRvalue()==0.0);
		System.out.println("Last data row good.");

		System.out.println(Long.toString(System.currentTimeMillis())+"testGetData pass");
		
	}
	
	/**
	 * 		This method uses the BooleanTest1 configuration to
	 * exercise the getTags() method.
	 * @throws Exception
	 */
	public void testGetTags() throws Exception{
		String metaHeader1 = "Position";
		String metaHeader11 = "H-Lignin";
		String metaHeader28 = "NH-Acetate";

		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName(configName);
		parser = new ExcelParser(f,config,session);
		assertNotNull(parser);
		
		List <CellDataHeader> cdhList = parser.getTags(sheetName);
		assertNotNull(cdhList);
		assertTrue(cdhList.size() == headerCount);
		System.out.println(cdhList.get(0).getName());
		assertTrue(cdhList.get(0).getName().equals(metaHeader1));
		System.out.println(cdhList.get(10).getName());
		assertTrue(cdhList.get(11).getName().equals(metaHeader11));
		System.out.println(cdhList.get(27).getName());
		assertTrue(cdhList.get(cdhList.size()-1).getName().equals(metaHeader28));
		System.out.println(cdhList.get(cdhList.size()-1).getName());
		System.out.println("Method getTags() checks out.");
		
		System.out.println(Long.toString(System.currentTimeMillis())+"testGetTags pass");
		
	}
	
}
