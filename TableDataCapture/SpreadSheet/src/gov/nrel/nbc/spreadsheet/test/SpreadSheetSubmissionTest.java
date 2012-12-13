package gov.nrel.nbc.spreadsheet.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SpreadSheetSubmissionTest extends TestCase {
	ClassLoader thisLoader;
	
	  /**
	   * Add as many tests as you like.
	   */

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}
	
	//@Override
	protected void setUp() {
    	thisLoader = gov.nrel.nbc.spreadsheet.client.SpreadSheet.class.getClassLoader();
	}
	
	static public Test suite() {
		return new TestSuite(SpreadSheetSubmissionTest.class);
	}

	  /**
	   * Add as many tests as you like.
	   */
	  public void testOnModuleLoad() {
		  
	  }
}
