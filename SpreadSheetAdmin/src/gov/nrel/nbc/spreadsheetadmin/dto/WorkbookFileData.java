package gov.nrel.nbc.spreadsheetadmin.dto;

public class WorkbookFileData {
	private long workbook_file_id;
	private String filename;
	private String path;
	/**
	 * @return the workbook_file_id
	 */
	public long getWorkbook_file_id() {
		return workbook_file_id;
	}
	/**
	 * @param workbook_file_id the workbook_file_id to set
	 */
	public void setWorkbook_file_id(long workbook_file_id) {
		this.workbook_file_id = workbook_file_id;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
