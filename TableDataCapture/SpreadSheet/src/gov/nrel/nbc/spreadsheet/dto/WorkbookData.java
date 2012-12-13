package gov.nrel.nbc.spreadsheet.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class WorkbookData  implements Serializable {
	private static final long serialVersionUID = -2765666207168226677L;
	private long workbook_id;
	private WorkbookFileData workbook_file_id;
	private WorkbookConfig workbook_config_id;
	private Set<Attachments> attachments = new HashSet<Attachments> ();
	private Set<SheetData> sheets = new HashSet<SheetData> ();

	/**
	 * @return the workbook_id
	 */
	public long getWorkbook_id() {
		return workbook_id;
	}
	/**
	 * @param workbook_id the workbook_id to set
	 */
	public void setWorkbook_id(long workbook_id) {
		this.workbook_id = workbook_id;
	}
	/**
	 * @return the workbook_file_id
	 */
	public WorkbookFileData getWorkbook_file_id() {
		return workbook_file_id;
	}
	/**
	 * @param workbook_file_id the workbook_file_id to set
	 */
	public void setWorkbook_file_id(WorkbookFileData workbook_file_id) {
		this.workbook_file_id = workbook_file_id;
	}
	/**
	 * @return the workbook_config_id
	 */
	public WorkbookConfig getWorkbook_config_id() {
		return workbook_config_id;
	}
	/**
	 * @param workbook_config_id the workbook_config_id to set
	 */
	public void setWorkbook_config_id(WorkbookConfig workbook_config_id) {
		this.workbook_config_id = workbook_config_id;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachments> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachments> getAttachments() {
		return attachments;
	}
	/**
	 * @param sheets the sheets to set
	 */
	public void setSheets(Set<SheetData> sheets) {
		this.sheets = sheets;
	}
	/**
	 * @return the sheets
	 */
	public Set<SheetData> getSheets() {
		return sheets;
	}
}
