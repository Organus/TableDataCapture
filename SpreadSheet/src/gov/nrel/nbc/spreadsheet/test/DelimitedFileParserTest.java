package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.parse.CellDataDTO;
import gov.nrel.nbc.spreadsheet.parse.DelimitedFileParser;
import gov.nrel.nbc.spreadsheet.parse.NumbersParser;
import gov.nrel.nbc.spreadsheet.parse.RowDTO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

public class DelimitedFileParserTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = NumbersParser.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DelimitedFileParserTest.class);
	}
	
	public void testGetHeaders() throws Exception {
		DelimitedFileParser np = new DelimitedFileParser(false, new File("C:/projects/james/SpreadSheet/resources/solids.csv"));
		List<String> headers = null;
		Session session = HibernateSessionFactory.getSession();
		WorkbookConfigDAOHibernate wch = new WorkbookConfigDAOHibernate();
		wch.setSession(session);
		np.setSession(session);
		np.setDELIMITER(COMMA_DELIMITER);
		session.beginTransaction();
		WorkbookConfig wbConfig = wch.findBySynonym("solids-csv1");//wch.findByFile(path);
		np.setWbConfig(wbConfig);
		headers = np.getHeaders("main",np.getDataFile());
		Iterator<String> hit = headers.iterator();
		while (hit.hasNext()) {
			String header = hit.next();
			System.out.println("header="+header);
		}
	}
	
	public void testGetDataRows() throws Exception {
		DelimitedFileParser np = new DelimitedFileParser(false, new File("C:/projects/james/SpreadSheet/resources/solids.csv"));
		np.setDELIMITER(COMMA_DELIMITER);
		Session session = HibernateSessionFactory.getSession();
		WorkbookConfigDAOHibernate wch = new WorkbookConfigDAOHibernate();
		wch.setSession(session);
		np.setSession(session);
		session.beginTransaction();
		WorkbookConfig wbConfig = wch.findBySynonym("solids-csv1");//wch.findByFile(path);
		np.setWbConfig(wbConfig);
		np.getDataRows("main");
		List<CellDataHeader> headers = np.getHeaders();
		Iterator<CellDataHeader> hit = headers.iterator();
		while (hit.hasNext()) {
			CellDataHeader header = hit.next();
			System.out.println("header="+header.getName());
		}
		List<RowDTO> data = np.getData();
		Iterator<RowDTO> dit = data.iterator();
		while (dit.hasNext()) {
			RowDTO row = dit.next();
			List<CellDataDTO> cells = row.getRow();
			Iterator<CellDataDTO> cit = cells.iterator();
			while (cit.hasNext()) {
				CellDataDTO cell = cit.next();
				String type = cell.getType();
				if (type.equals("STRING"))
					System.out.println("data="+cell.getValue().getSvalue());
				else if (type.equals("REAL"))
					System.out.println("data="+cell.getValue().getRvalue());
				else if (type.equals("LONG"))
					System.out.println("data="+cell.getValue().getLvalue());
				else if (type.equals("DATE"))
					System.out.println("data="+cell.getValue().getDvalue().toString());
			}
		}
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
