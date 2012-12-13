package gov.nrel.nbc.spreadsheet.server;

/**
 * A public class to hold attachment data, corresponding to the database table.
 * 
 * @author James Albersheim
 * 
 */
public class AttachmentData {
	private long workbook_id;
	private String value;
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
}
