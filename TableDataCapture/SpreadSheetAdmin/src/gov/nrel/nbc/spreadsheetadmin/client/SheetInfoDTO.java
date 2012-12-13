package gov.nrel.nbc.spreadsheetadmin.client;

public class SheetInfoDTO {
	int headerRow;
	int headerColumn;
	int dataRow;
	int dataColumn;
	boolean hasMetaData;
	int startMetaRow;
	int endMetaRow;
	int startMetaColumn;
	int endMetaColumn;
	
	/**
	 * @return the headerRow
	 */
	public int getHeaderRow() {
		return headerRow;
	}
	/**
	 * @param headerRow the headerRow to set
	 */
	public void setHeaderRow(int headerRow) {
		this.headerRow = headerRow;
	}
	/**
	 * @return the headerColumn
	 */
	public int getHeaderColumn() {
		return headerColumn;
	}
	/**
	 * @param headerColumn the headerColumn to set
	 */
	public void setHeaderColumn(int headerColumn) {
		this.headerColumn = headerColumn;
	}
	/**
	 * @return the dataRow
	 */
	public int getDataRow() {
		return dataRow;
	}
	/**
	 * @param dataRow the dataRow to set
	 */
	public void setDataRow(int dataRow) {
		this.dataRow = dataRow;
	}
	/**
	 * @return the dataColumn
	 */
	public int getDataColumn() {
		return dataColumn;
	}
	/**
	 * @param dataColumn the dataColumn to set
	 */
	public void setDataColumn(int dataColumn) {
		this.dataColumn = dataColumn;
	}
	/**
	 * @return the hasMetaData
	 */
	public boolean isHasMetaData() {
		return hasMetaData;
	}
	/**
	 * @param hasMetaData the hasMetaData to set
	 */
	public void setHasMetaData(boolean hasMetaData) {
		this.hasMetaData = hasMetaData;
	}
	/**
	 * @return the startMetaRow
	 */
	public int getStartMetaRow() {
		return startMetaRow;
	}
	/**
	 * @param startMetaRow the startMetaRow to set
	 */
	public void setStartMetaRow(int startMetaRow) {
		this.startMetaRow = startMetaRow;
	}
	/**
	 * @return the endMetaRow
	 */
	public int getEndMetaRow() {
		return endMetaRow;
	}
	/**
	 * @param endMetaRow the endMetaRow to set
	 */
	public void setEndMetaRow(int endMetaRow) {
		this.endMetaRow = endMetaRow;
	}
	/**
	 * @return the startMetaColumn
	 */
	public int getStartMetaColumn() {
		return startMetaColumn;
	}
	/**
	 * @param startMetaColumn the startMetaColumn to set
	 */
	public void setStartMetaColumn(int startMetaColumn) {
		this.startMetaColumn = startMetaColumn;
	}
	/**
	 * @return the endMetaColumn
	 */
	public int getEndMetaColumn() {
		return endMetaColumn;
	}
	/**
	 * @param endMetaColumn the endMetaColumn to set
	 */
	public void setEndMetaColumn(int endMetaColumn) {
		this.endMetaColumn = endMetaColumn;
	}
}
