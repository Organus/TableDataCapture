package gov.nrel.nbc.labelprinting.test;

import gov.nrel.nbc.labelprinting.utils.JLogger;

import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JLoggerTest extends TestCase {
	ClassLoader thisLoader;
	/**
	 * JLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final JLogger log = new JLogger(JLogger.INFO);
	
	@Override
	protected void setUp() {
    	thisLoader = JLogger.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(JLoggerTest.class);
	}

	  /**
	   * Add as many tests as you like.
	   */

	public void testMethod() {
	    log.entering();
	    JLogger log1 = JLogger.getXLogger();
	    try {
	    	log1.throwing(new Exception("a sample exception"));
	    }
	    catch (Exception e1) {
	    	System.out.println("caught: "+e1);
	    }
	    log1 = new JLogger();
	    log1 = new JLogger(Level.INFO);
	    log1 = new JLogger("MyLog");
	    JLogger.removeHandlers(log1);
	    System.out.println(log.toString());
	    log.exiting();
	}
}
