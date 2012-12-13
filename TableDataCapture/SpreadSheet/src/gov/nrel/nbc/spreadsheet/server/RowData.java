package gov.nrel.nbc.spreadsheet.server;

import java.util.List;

/**
 * Public class to that is used to hold a row of
 * data, including a list of cells <CellData>.
 * 
 * @author James Albersheim
 *
 */
public class RowData {
	private long row;
	private List<CellData> data;
	/**
	 * @return the order
	 */
	public long getRow() {
		return row;
	}
	/**
	 * @param order the order to set
	 */
	public void setRow(long row) {
		this.row = row;
	}
	/**
	 * @return the data
	 */
	public List<CellData> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<CellData> data) {
		this.data = data;
	}
	
}
