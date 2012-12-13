package gov.nrel.nbc.labelprinting.test; 

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
		suite.addTest(TrackerFileUploadServiceImplTest.suite());
		suite.addTest(TrackerServiceImplTest.suite());
		suite.addTest(JLoggerTest.suite());
		suite.addTest(PrinterTest.suite());
	    //suite.addTestSuite(TestSearchSpreadsheet.class); 
	    return suite;
	}
}