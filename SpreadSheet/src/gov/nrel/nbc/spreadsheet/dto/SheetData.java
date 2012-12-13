package gov.nrel.nbc.spreadsheet.dto;

public class SheetData {
	private long sheet_data_id;
	private WorkbookData workbook_id;
	private SheetConfig sheet_config_id;
	/**
	 * @return the sheet_data_id
	 */
	public long getSheet_data_id() {
		return sheet_data_id;
	}
	/**
	 * @param sheet_data_id the sheet_data_id to set
	 */
	public void setSheet_data_id(long sheet_data_id) {
		this.sheet_data_id = sheet_data_id;
	}
	/**
	 * @return the workbook_id
	 */
	public WorkbookData getWorkbook_id() {
		return workbook_id;
	}
	/**
	 * @param workbook_id the workbook_id to set
	 */
	public void setWorkbook_id(WorkbookData workbook_id) {
		this.workbook_id = workbook_id;
	}
	/**
	 * @return the sheet_config_id
	 */
	public SheetConfig getSheet_config_id() {
		return sheet_config_id;
	}
	/**
	 * @param sheet_config_id the sheet_config_id to set
	 */
	public void setSheet_config_id(SheetConfig sheet_config_id) {
		this.sheet_config_id = sheet_config_id;
	}

}
