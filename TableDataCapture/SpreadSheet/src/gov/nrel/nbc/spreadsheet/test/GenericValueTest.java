package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.dto.GenericValue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GenericValueTest extends TestCase {
	ClassLoader thisLoader;
	private GenericValue gv;
	
	//@Override
	protected void setUp() {
    	thisLoader = GenericValue.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(GenericValueTest.class);
	}

	  /**
	   * Add as many tests as you like.
	   */

	public void testMethod() {
	    gv = new GenericValue();
	    boolean b = true;
	    gv.setBooleanValue(b);
	    assertTrue(gv.getBooleanValue().booleanValue());
	}
}
