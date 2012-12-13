package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.Operators;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OperatorsTest extends TestCase {
	ClassLoader thisLoader;
	private Operators oper;
	
	//@Override
	protected void setUp() {
    	thisLoader = Operators.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(OperatorsTest.class);
	}

	  /**
	   * Add as many tests as you like.
	   */

	public void testMethod() {
	    oper = new Operators();
	    oper.setId(1);
	    oper.setOperator("=");
	    assertEquals(1,oper.getId());
	    assertEquals("=",oper.getOperator());
	    DataType dt = new DataType();
	    dt.setDescription("STRING");
	    dt.setId(1);
	    oper.setData_type_id(dt);
	    assertEquals(dt,oper.getData_type_id());
	}
}
