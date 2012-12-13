package gov.nrel.nbc.spreadsheet.test; 

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestSuite that runs all the sample tests
 *
 */
public class AllTests extends GWTTestSuite {

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}
	public static Test suite ( ) {
		TestSuite suite= new TestSuite("All JUnit Tests");
		suite.addTest(ListTest.suite()); 
		suite.addTest(XLoggerTest.suite());
		suite.addTest(DataTypeDAOHibernateTest.suite());
		suite.addTest(MetadataHeaderDAOHibernateTest.suite());
		suite.addTest(CellDataDAOHibernateTest.suite());
		suite.addTest(ExportDataTest.suite());
		suite.addTest(SpreadsheetServiceImplTest.suite());
		suite.addTest(SpreadSheetSubmissionTest.suite());
		suite.addTest(CalcExcelParserTest.suite());
		suite.addTest(AttachmentFileUploadServiceImplTest.suite());
		suite.addTest(SpreadSheetUploadServiceImplTest.suite());
		suite.addTest(ExcelDownloadServiceTest.suite());
		suite.addTest(OperatorsTest.suite());
		suite.addTest(CellDataTest.suite());
		suite.addTest(GenericValueTest.suite());
		suite.addTest(SQLTest.suite());
		suite.addTest(SeleniumSearchTest.suite());
		return suite;
	}
}