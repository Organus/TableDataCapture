package gov.nrel.nbc.spreadsheetadmin.test; 

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
		/**/
		suite.addTest(XLoggerTest.suite());
		suite.addTest(ExcelSXParserTest.suite());
		suite.addTest(ExcelParserTest.suite());
		suite.addTest(AdminServiceImplTest.suite());
		suite.addTest(SpreadSheetUploadServiceImplTest.suite());
	    return suite;
	}
}