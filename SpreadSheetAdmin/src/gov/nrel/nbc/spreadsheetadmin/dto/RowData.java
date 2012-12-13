package gov.nrel.nbc.spreadsheetadmin.dto;

/**
 * This is a data transfer object class. This class represents the row from the
 * spreadsheetadmin.
 * 
 * @author James Albersheim
 */
public class RowData {

	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;

	/**
	 * An index into the spreadsheet rows
	 */
	private int rowNum;

	/**
	 * A reference to the workbook
	 */
	private SheetData sheet_data_id;

	/**
	 * Simple constructor
	 */
	public RowData() {

	}

	/**
	 * Complex constructor
	 */
	public RowData(int sheetRow, SheetData sheet_data_id) {
		this.rowNum = sheetRow;
		this.setSheet_data_id(sheet_data_id);
	}

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
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	/**
	 * @param sheet_data_id the sheet_data_id to set
	 */
	public void setSheet_data_id(SheetData sheet_data_id) {
		this.sheet_data_id = sheet_data_id;
	}

	/**
	 * @return the sheet_data_id
	 */
	public SheetData getSheet_data_id() {
		return sheet_data_id;
	}

}
