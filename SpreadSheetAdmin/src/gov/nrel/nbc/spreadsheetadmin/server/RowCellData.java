package gov.nrel.nbc.spreadsheetadmin.server;

import gov.nrel.nbc.spreadsheetadmin.dto.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Public class to that is used to transfer a row of
 * data.
 * 
 * @author James Albersheim
 *
 */
public class RowCellData {
	private List<CellDataHeaderData> rowCellDataHeader;
	private List<Metadata> rowMetadata;
	private long row_id;
	
	public RowCellData() {
		setRowCellDataHeader(new ArrayList<CellDataHeaderData>());
		rowMetadata = new ArrayList<Metadata>();
		row_id = 0;
	}
	/**
	 * @param rowMetadata the rowMetadata to set
	 */
	public void setRowMetadata(List<Metadata> rowMetadata) {
		this.rowMetadata = rowMetadata;
	}
	/**
	 * @return the rowMetadata
	 */
	public List<Metadata> getRowMetadata() {
		return rowMetadata;
	}
	/**
	 * @param rowCellDataHeader the rowCellDataHeader to set
	 */
	public void setRowCellDataHeader(List<CellDataHeaderData> rowCellDataHeader) {
		this.rowCellDataHeader = rowCellDataHeader;
	}
	/**
	 * @return the rowCellDataHeader
	 */
	public List<CellDataHeaderData> getRowCellDataHeader() {
		return rowCellDataHeader;
	}
	/**
	 * @param row_id the row_id to set
	 */
	public void setRow_id(long row_id) {
		this.row_id = row_id;
	}
	/**
	 * @return the row_id
	 */
	public long getRow_id() {
		return row_id;
	}
}
