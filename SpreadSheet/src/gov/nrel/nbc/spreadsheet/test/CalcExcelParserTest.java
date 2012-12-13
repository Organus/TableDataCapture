package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.parse.ExcelSParser;
import gov.nrel.nbc.spreadsheet.parse.ExcelSXParser;
import gov.nrel.nbc.spreadsheet.parse.RowDTO;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;

public class CalcExcelParserTest extends TestCase {
	ClassLoader thisLoader;
	private ExcelSParser cep;
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testSomething() {
			String ret = "";
			cep = new ExcelSParser();
			System.out.println(ret);
	  }
	
	//@Override
	protected void setUp() {
    	thisLoader = ExcelSParser.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(CalcExcelParserTest.class);
	}

	public void testMethod() throws Exception {
		File f = new File("C:\\Documents and Settings\\James Albersheim.NREL_NT\\My Documents\\Downloads\\translations.xls");
		Session session = HibernateSessionFactory.getSession();
			
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);

		session.beginTransaction();

		WorkbookConfig config = wcdh.findByName("t2");
		SheetDAOHibernate sdh = new SheetDAOHibernate();
		sdh.setSession(session);
		//SheetConfig dbSheet = wcdh.findSheetByConfigAndName(config, "Digestion");
		cep = new ExcelSParser(true,f,config,session);
		List<RowDTO> rds = cep.getData("sheet2 - table 1");
		assertNotNull(rds);
		List<CellDataHeader> tags = cep.getTags("sheet2 - table 1");
		assertNotNull(tags);
		List<RowDTO> data = cep.getData("sheet2 - table 1");
		assertNotNull(data);
	}

	public void testXLSXMethod() throws Exception {
		File f = new File("C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xlsx");
		Session session = HibernateSessionFactory.getSession();
		
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);

		session.beginTransaction();

		WorkbookConfig config = wcdh.findByName("SolidsPreDecember2010");
		SheetDAOHibernate sdh = new SheetDAOHibernate();
		sdh.setSession(session);
		//SheetConfig dbSheet = wcdh.findSheetByConfigAndName(config, "Digestion");
		ExcelSXParser cesxp = new ExcelSXParser(true,f,config,session);
		List<RowDTO> rds = cesxp.getData("Digestion");
		assertNotNull(rds);
		List<CellDataHeader> tags = cesxp.getTags("Digestion");
		assertNotNull(tags);
	}
}
