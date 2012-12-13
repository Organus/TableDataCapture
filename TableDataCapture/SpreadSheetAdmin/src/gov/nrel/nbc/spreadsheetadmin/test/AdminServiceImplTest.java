package gov.nrel.nbc.spreadsheetadmin.test;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheetadmin.client.NameValue;
import gov.nrel.nbc.spreadsheetadmin.client.SheetCellHeaders;
import gov.nrel.nbc.spreadsheetadmin.dao.SheetDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelParser;
import gov.nrel.nbc.spreadsheetadmin.server.AdminServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;

import org.hibernate.Session;
import org.mortbay.log.Log;

public class AdminServiceImplTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	private AdminServiceImpl asImpl=new AdminServiceImpl();
	String configName;
	String sheetName;
	
	@Override
	protected void setUp() {
    	thisLoader = AdminServiceImpl.class.getClassLoader();
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

        configName = "NIR2";
        sheetName = "Sheet2";
//        sheetName = "ingest";
    	//asImpl = new AdminServiceImpl();
	}
	
	public static Test suite() {
		return new TestSuite(AdminServiceImplTest.class);
	}
	
	public void testGetCellHeaders() throws Exception {
		System.out.println("testGetCellHeaders");
		List<String> headerStrings;
		asImpl = new AdminServiceImpl();
		SheetCellHeaders headers = asImpl.getCellHeaders(configName, System.getProperty("java.io.filedir") + "calc1275578109577NIR Ingest1.xls", sheetName);//
		assertNotNull(headers);
		headerStrings=headers.getHeaders();
		assertNotNull(headerStrings);
		if(headerStrings!=null){
			assertTrue(headers.getHeaders().size()>0);
			if (headerStrings.size()>0){
				for (int i=0;i<headers.getHeaders().size();i++)
					System.out.println("got header#"+i+"="+headers.getHeaders().get(i));}
		}
	}
	
	public void testGetCellDataHeaders() throws Exception {
		System.out.println("testGetCellDataHeaders");
		createWbConfig(configName, sheetName);
		testSetCellDataHeaders();
		asImpl = new AdminServiceImpl();
		List<NameValue> headers = asImpl.getCellDataHeaders(configName, sheetName);
		assertNotNull(headers);
		if(headers!=null){
			assertTrue(headers.size()>0);
			if(headers.size()>0){
				for (int i=0;i<headers.size();i++)
					System.out.println("got header#"+i+"="+headers.get(i).getName());
			}
		}
	}
	
	public void testDeleteWorkbook() throws Exception {
		System.out.println("testDeleteWorkbook");
		asImpl = new AdminServiceImpl();
//TODO: This needs a setup to create a workbook to then delete.
		Boolean ret = asImpl.deleteWorkbook(1);
		assertTrue(ret);
	}
	
	public void testDeleteWorkbookConfig() throws Exception {
		System.out.println("testDeleteWorkbookConfig");
		asImpl = new AdminServiceImpl();
		Boolean ret = asImpl.deleteWorkbookConfig("James123");
		assertTrue(ret);
		ret = asImpl.workbookConfigExists("James123");
		assertFalse(ret);
	}
	
	public void testDeleteWorkbookData() throws Exception {
		System.out.println("testDeleteWorkbookData");
		asImpl = new AdminServiceImpl();
		for (int i=16; i<32;i++) {
			Boolean ret = asImpl.deleteWorkbookData(i);
			assertTrue(ret);
		}
	}
	
	public void testDeleteMetaData() throws Exception {
		System.out.println("testDeleteMetaData");
		asImpl = new AdminServiceImpl();
		//for (long i=1; i<7;i++) {
			Boolean ret = asImpl.deleteMetaData(4);
			assertTrue(ret);
		//}
	}
	
	public void testDeleteMetaDataHeader() throws Exception {
		System.out.println("testDeleteMetaDataHeader");
		asImpl = new AdminServiceImpl();
		//for (long i=1; i<7;i++) {
			Boolean ret = asImpl.deleteMetaDataHeader(4);
			assertTrue(ret);
		//}
	}
	
	public void testDeleteExternalMetadataHeader() throws Exception {
		System.out.println("testDeleteExternalMetadataHeader");
		asImpl = new AdminServiceImpl();
		Boolean ret = asImpl.deleteExternalMetadataHeaders("new nir#3");
		assertTrue(ret);
	}
	
	public void testDeleteInternalMetadataHeader() throws Exception {
		System.out.println("testDeleteInternalMetadataHeader");
		asImpl = new AdminServiceImpl();
		Boolean ret = asImpl.deleteInternalMetadataHeaders("new nir#3");
		assertTrue(ret);
	}
	
	public void testDeleteCellDataHeaders() throws Exception {
		System.out.println("testDeleteCellDataHeaders");
		testSetCellDataHeaders();
		asImpl = new AdminServiceImpl();
		Boolean ret = asImpl.deleteCellDataHeaders("new nir#3", sheetName);
		assertTrue(ret);
	}
	
	public void testGetDataFormats() throws Exception {
		System.out.println("testGetDataFormats");
		asImpl = new AdminServiceImpl();
		List<NameValue> list = asImpl.getDataFormats(DATE_TYPE);
		assertNotNull(list);
		assertTrue(list.size()>0);
	}
	
	public void testGetDataType() throws Exception {
		System.out.println("testGetDataType");
		asImpl = new AdminServiceImpl();
		long id = asImpl.getTypeId("STRING");
		assertNotNull(id);
		assertTrue(id==4);
	}
	
	public void testGetMetaDataHeaders() throws Exception {
		System.out.println("testGetMetaDataHeaders");
		asImpl = new AdminServiceImpl();
		List<NameValue> list = asImpl.getMetaDataHeaders("new nir#3", INTERNAL);
		assertNotNull(list);
		assertTrue(list.size()>0);
	}
	
	public void testWorkbookConfigExists() throws Exception {
		System.out.println("testWorkbookConfigExists");
		asImpl = new AdminServiceImpl();
		boolean ret = asImpl.workbookConfigExists("new nir#3");
		assertTrue(ret);
	}
	
	public void testGetUrls() throws Exception {
		System.out.println("testGetUrls");
		asImpl = new AdminServiceImpl();
		List<NameValue> list = asImpl.getUrls();
		assertNotNull(list);
		assertTrue(list.size()>0);
	}
	
	public void testGetDataTypes() throws Exception {
		System.out.println("testGetDataTypes");
		asImpl = new AdminServiceImpl();
		List<NameValue> list = asImpl.getDataTypes();
		assertNotNull(list);
		assertTrue(list.size()>0);
	}
	
	public void testGetTitle() throws Exception {
		System.out.println("testGetTitle");
		asImpl = new AdminServiceImpl();
		String name = asImpl.getTitle();
		assertNotNull(name);
		assertTrue(name.length()>0);
	}
	
	public void testGetWorkbookConfigNames() throws Exception {
		System.out.println("testGetWorkbookConfigNames");
		asImpl = new AdminServiceImpl();
		List<String> names = asImpl.getWorkbookConfigs();
		assertNotNull(names);
		assertTrue(names.size()>0);
		for (int i=0;i<names.size();i++)
			System.out.println("got config#"+i+"="+names.get(i));
	}
	
	public void testGetSheetConfigNames() throws Exception {
		System.out.println("testGetSheetConfigNames");
		testSetCellDataHeaders();
		asImpl = new AdminServiceImpl();
		List<String> names = asImpl.getSheetConfigs("new nir#3");
		assertNotNull(names);
		assertTrue(names.size()>0);
		for (int i=0;i<names.size();i++)
			System.out.println("got new nir#3:config#"+i+"="+names.get(i));
	}
	
	public void testContains() throws Exception {
		System.out.println("testContains");
		asImpl = new AdminServiceImpl();
		List<NameValue> nvs = new ArrayList<NameValue>();
		NameValue nv = new NameValue("booger1", "1");nvs.add(nv);
		nv = new NameValue("booger2", "2");nvs.add(nv);
		nv = new NameValue("booger3", "3");nvs.add(nv);
		nv = new NameValue("booger4", "4");nvs.add(nv);
		nv = new NameValue("booger5", "5");nvs.add(nv);
		nv = new NameValue("booger6", "6");nvs.add(nv);
		boolean ret = true;
		try {
			ret = (Boolean)PrivateAccessor.invoke(asImpl, "contains", new Class[]{List.class,String.class}, new Object[]{nvs,"booger3"});
		} catch (Throwable t) {
			System.out.println("problem invoking contains: "+t);
		}
		assertTrue(ret);
	}
	
	public void testGetInternalMetadataHeaders() throws Exception {
		System.out.println("testGetInternalMetadataHeaders");
		asImpl = new AdminServiceImpl();
		List<String> headers = asImpl.getInternalMetadataHeaders(configName, System.getProperty("java.io.filedir") + "calc1275928136686NIR Ingest1.xls", sheetName);
		assertNotNull(headers);
		assertTrue(headers.size()>0);
		for (int i=0;i<headers.size();i++)
			System.out.println("got header#"+i+"="+headers.get(i));
	}
	
	public void testSetMetaDataHeaders() throws Exception {
		System.out.println("testSetMetaDataHeaders");
		Session session = HibernateSessionFactory.getSession();
		
		session.beginTransaction();

		asImpl = new AdminServiceImpl();
		File f = new File("C:\\projects\\james\\SpreadSheetAdmin\\NIR Ingest1.xls");
		
		SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
		sdh.setSession(session);
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName("new nir#3");
		if (config == null) {
			String sret = createWbConfig("new nir#3",sheetName);
			assertNotNull(sret);
			config = wcdh.findByName("new nir#3");
		}
		//SheetConfig dbSheet = wcdh.findSheetByConfigAndName(configName, "ingest");
		ExcelParser parser = new ExcelParser(f,config,session);
		
		List<NameValue> mdNames = parser.getMetadataNames(sheetName);
		boolean ret = asImpl.setMetaDataHeaders(mdNames, "new nir#3");
		assertTrue(ret);
		ret = asImpl.setMetaDataHeader(mdNames.get(0), "new nir#3");
		assertTrue(ret);
	}
	
	public void testSetCellDataHeaders() throws Exception {
		System.out.println("testSetCellDataHeaders");
		Session session = HibernateSessionFactory.getSession();
		
		session.beginTransaction();

		asImpl = new AdminServiceImpl();
		File f = new File(System.getProperty("java.io.tmpdir") + "NIR Ingest1.xls");
		
		SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
		sdh.setSession(session);
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName("new nir#3");
		if (config == null) {
			String sret = createWbConfig("new nir#3",sheetName);
			assertNotNull(sret);
			config = wcdh.findByName("new nir#3");
		}
		//SheetConfig dbSheet = wcdh.findSheetByConfigAndName(configName, "ingest");
		ExcelParser parser = new ExcelParser(f,config,session);
		
		List<String> cellNames = parser.getHeaders(sheetName);
		assertNotNull(cellNames);
		
		List<NameValue> cellDatas = new ArrayList<NameValue>();
		NameValue nv = new NameValue();
		int ctr=0;
		Iterator<String> cit = cellNames.iterator();
		while (cit.hasNext()) {
			String hdr = cit.next();
			ctr++;
			nv = new NameValue();
			if (hdr.equals("\"Sample ID\""))
				nv.setDataType(4);
			else
				nv.setDataType(2);
			nv.setName(hdr);
			nv.setOrder(ctr);
			cellDatas.add(nv);
		}
		boolean ret = asImpl.setCellDataHeaders(cellDatas, "new nir#3", sheetName);
		assertTrue(ret);
		ret = asImpl.setCellDataHeader(cellDatas.get(0), "new nir#3", sheetName);
		assertTrue(ret);
	}
	
	private String createWbConfig(String wbname, String shname) {
		List<NameValue> specs = new ArrayList<NameValue>();
		NameValue nv = new NameValue();
		nv.setName("hdr_row");
		nv.setValue("15");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("hdr_col");
		nv.setValue("1");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("data_row");
		nv.setValue("16");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("data_col");
		nv.setValue("1");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("meta_start_row");
		nv.setValue("2");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("meta_start_col");
		nv.setValue("1");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("meta_end_row");
		nv.setValue("7");
		specs.add(nv);
		nv = new NameValue();
		nv.setName("meta_end_col");
		nv.setValue("2");
		specs.add(nv);
		String sret = asImpl.setSheetConfig(specs, wbname, shname);
		return sret;
	}
	public void testSetSheetConfig() throws Exception {
		System.out.println("testSetSheetConfig");
		asImpl = new AdminServiceImpl();
		String sret = createWbConfig("new nir#3", sheetName);
		assertTrue(sret.length()>0);
		boolean ret = asImpl.deleteWorkbookConfig("new nir#3");
		assertTrue(ret);
		ret = asImpl.workbookConfigExists("new nir#3");
		assertFalse(ret);
	}
}
