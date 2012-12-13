package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.dto.CellData;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.RowData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CellDataTest extends TestCase {
	ClassLoader thisLoader;
	private CellData cd;
	
	//@Override
	protected void setUp() {
    	thisLoader = CellData.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(CellDataTest.class);
	}

	  /**
	   * Add as many tests as you like.
	   */

	public void testMethod() {
	    cd = new CellData();
	    cd.setCell_data_id(1);
	    RowData rd = new RowData();
	    rd.setId(1);
	    rd.setRowNum(1);
	    cd.setRowId(rd);
	    CellDataHeader ct = new CellDataHeader();
	    cd = new CellData(ct);
	    ct.setCell_hdr_id(1);
	    ct.setName("tag1");
	    DataType dt = new DataType();
	    dt.setId(1);
	    dt.setDescription("STRING");
	    dt = new DataType("STRING");
	    ct.setTypeId(dt);
	    assertEquals(0,cd.getCell_data_id());
	}
}
