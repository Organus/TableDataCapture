package gov.nrel.nbc.spreadsheetadmin.test;

import java.util.logging.Level;

import gov.nrel.nbc.spreadsheetadmin.utilities.XLogger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class XLoggerTest extends TestCase {
	ClassLoader thisLoader;
	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(XLogger.INFO);
	
	@Override
	protected void setUp() {
    	thisLoader = XLogger.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(XLoggerTest.class);	
	}

	  /**
	   * Add as many tests as you like.
	   */

	public void testMethod() {
	}
}
