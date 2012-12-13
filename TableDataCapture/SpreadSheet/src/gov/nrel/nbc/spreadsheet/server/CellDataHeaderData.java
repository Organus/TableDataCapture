package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.spreadsheet.dto.CellData;

/**
 * Public class to that is used to transfer cell data headers.
 * 
 * @author James Albersheim
 *
 */
public class CellDataHeaderData {
	private CellData cellData;
	private gov.nrel.nbc.spreadsheet.dto.CellDataHeader cellHeader;
	private String header;
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
	public void setCellHeader(gov.nrel.nbc.spreadsheet.dto.CellDataHeader ch1) {
		this.cellHeader = ch1;
	}
	/**
	 * @return the cellHeader
	 */
	public gov.nrel.nbc.spreadsheet.dto.CellDataHeader getCellHeader() {
		return cellHeader;
	}
	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}
}

