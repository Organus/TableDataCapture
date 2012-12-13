package gov.nrel.nbc.spreadsheetadmin.server;

import gov.nrel.nbc.spreadsheetadmin.dto.CellData;
import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;

public class CellDataHeaderData {
	private CellData cellData;
	
	private CellDataHeader cellHeader;
	
	/**
	 * @param cellData the cellData to set
	 */
	public void setCellData(CellData cellData) {
		this.cellData = cellData;
	}
	
	/**
	 * @return the cellData
	 */
	public CellData getCellData() {
		return cellData;
	}
	
	/**
	 * @param ch1 the cellHeader to set
	 */
	public void setCellHeader(CellDataHeader ch1) {
		this.cellHeader = ch1;
	}
	
	/**
	 * @return the cellHeader
	 */
	public CellDataHeader getCellHeader() {
		return cellHeader;
	}
}