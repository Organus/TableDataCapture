package gov.nrel.nbc.spreadsheet.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Public class to that is used to transfer rows of
 * data along with their metadata.
 * 
 * @author James Albersheim
 *
 */
public class WorkbookRows {
	
	/**
	 * workbook reference
	 */
	private long workbook_id;
	
	/**
	 * config reference
	 */
	private long config_id;
	
	/**
	 * List of rows of data
	 */
	private List<RowCellData> rowDataList;

	/**
	 * The default constructor
	 */
	public WorkbookRows() {
		rowDataList = new ArrayList<RowCellData>();
	}
	/**
	 * Method to add a RowCellData to the rowDataList object
	 * @param rowData <RowCellData> the rowData to add
	 */
	public void add(RowCellData rowData) {
		rowDataList.add(rowData);
	}
	/**
	 * @return the rowDataList
	 */
	public List<RowCellData> getRowDataList() {
		return rowDataList;
	}
	/**
	 * @param rowDataList the rowDataList to set
	 */
	public void setRowDataList(List<RowCellData> rowDataList) {
		this.rowDataList = rowDataList;
	}
	/**
	 * @param workbook_id the workbook_id to set
	 */
	public void setWorkbook_id(long workbook_id) {
		this.workbook_id = workbook_id;
	}
	/**
	 * @return the workbook_id
	 */
	public long getWorkbook_id() {
		return workbook_id;
	}
	/**
	 * @param config_id the config_id to set
	 */
	public void setConfig_id(long config_id) {
		this.config_id = config_id;
	}
	/**
	 * @return the config_id
	 */
	public long getConfig_id() {
		return config_id;
	}
}
