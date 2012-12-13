package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.parse.NumbersParser;
import gov.nrel.nbc.spreadsheet.parse.RowDTO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

public class NumbersParserTest extends TestCase {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = NumbersParser.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(NumbersParserTest.class);
	}
	
	public void testGetHeaders() throws Exception {
		NumbersParser np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		List<String> headers = np.getHeaders(np.getDataFile());
		Iterator<String> hit = headers.iterator();
		while (hit.hasNext()) {
			String header = hit.next();
			System.out.println("header="+header);
		}
		np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		Session session = HibernateSessionFactory.getSession();
		WorkbookConfigDAOHibernate wch = new WorkbookConfigDAOHibernate();
		wch.setSession(session);
		session.beginTransaction();
		WorkbookConfig wbConfig = wch.findBySynonym("num10");//wch.findByFile(path);
		np.setWbConfig(wbConfig);
		headers = np.getHeaders("main",np.getDataFile());
		hit = headers.iterator();
		while (hit.hasNext()) {
			String header = hit.next();
			System.out.println("header="+header);
		}
	}
	
	public void testGetData() throws Exception {
		NumbersParser np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));//"/Users/jalbersh/Downloads/tika/ us_cities.xml"));
		//List<RowDTO> rows = np.getData("");
		np.getDataRows(np.getDataFile());
		np.setWsheet("main");
		List<RowDTO> rows = np.getData();
		System.out.println("has "+rows.size()+" rows of data");
	}
	
	public void testGetAllData() throws Exception {
		NumbersParser np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		//List<RowDTO> rows = np.getData("");
		np.getDataRows(np.getDataFile());
		List<RowDTO> rows = np.getData();
		System.out.println("has "+rows.size()+" rows of data");
	}
	
	public void testGetContent() throws Exception {
		NumbersParser np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		String content = np.getContent(np.getDataFile());
		System.out.println("content="+content);
		np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/testNumbers.numbers"));
		content = np.getContent(np.getDataFile());
		System.out.println("content="+content);
	}
	
	public void testGetDataRows() throws Exception {
		NumbersParser np = new NumbersParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		np.getDataRows(np.getDataFile());
		//System.out.println("content="+content);
	}
	
	public void testOS () throws Exception {
		  String nameOS = "os.name";  
		  String versionOS = "os.version";  
		  String architectureOS = "os.arch";
		  System.out.println("\n  The information about OS");
		  System.out.println("\nName of the OS: " + 
		  System.getProperty(nameOS));
		  System.out.println("Version of the OS: " + 
		  System.getProperty(versionOS));
		  System.out.println("Architecture of THe OS: " + 
		  System.getProperty(architectureOS));
	}
	
}
