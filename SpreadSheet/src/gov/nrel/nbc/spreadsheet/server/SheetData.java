package gov.nrel.nbc.spreadsheet.server;

import java.util.List;

/**
 * Public class to that is used to transfer sheets of
 * data, including a list of Rows <RowData>.
 * 
 * @author James Albersheim
 *
 */
public class SheetData {
	private long id;
	private List<RowData> rows;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the rows
	 */
	public List<RowData> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<RowData> rows) {
		this.rows = rows;
	}
	
}
