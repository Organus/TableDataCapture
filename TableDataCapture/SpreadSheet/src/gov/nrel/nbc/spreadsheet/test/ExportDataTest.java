package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import gov.nrel.nbc.spreadsheet.server.ExportData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExportDataTest extends TestCase {

	ClassLoader thisLoader;

	private static int testCounter=0;
	private ExportData edata = null;
	
	//@Override
	protected void setUp() {
    	thisLoader = ExportData.class.getClassLoader();
    	edata = new ExportData(6); 
	}
	
	public static Test suite() {
		return new TestSuite(ExportDataTest.class);
	}
	
	public void testCreateExcelFile() throws Exception {
		List<List<String>> data = null;
		testCounter++;
		String path = edata.createExcelFile(data, "output", "test", "test", null);
		Assert.assertNull(path);
		String[] s = {"Sample ID","2","3","4","5","6","7","8","9","10"};
		ArrayList<String> row = new ArrayList<String>();
		data = new ArrayList<List<String>>();
		for (int i=0;i<s.length;i++) row.add(s[i]);
		for (int i=0;i<10;i++) data.add(row);
		testCounter++;
		path = edata.createExcelFile(data, "output", "test", "test", null);
		assertNotNull(path);
		assertTrue(path.length()>0);
		System.out.println("created file="+path);
		File f = new File(path);
		assertTrue(f.exists());
		System.out.println("file exists!");
		assertTrue(f.length()>0);
		System.out.println("file has data!");
	}

}
