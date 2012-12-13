package gov.nrel.nbc.spreadsheet.dto;

import java.io.Serializable;

public class Attachments implements Serializable {
	private static final long serialVersionUID = 3349719293445599665L;
	private long attachment_id;
	private WorkbookData workbook_id;
	private String filename;
	private String path;
	private AttachmentType type_id;
	/**
	 * @return the attachment_id
	 */
	public long getAttachment_id() {
		return attachment_id;
	}
	/**
	 * @param attachment_id the attachment_id to set
	 */
	public void setAttachment_id(long attachment_id) {
		this.attachment_id = attachment_id;
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
	/**
	 * @param workbook_id the workbook_id to set
	 */
	public void setWorkbook_id(WorkbookData workbook_id) {
		this.workbook_id = workbook_id;
	}
	/**
	 * @return the workbook_id
	 */
	public WorkbookData getWorkbook_id() {
		return workbook_id;
	}
	/**
	 * @param type_id the type_id to set
	 */
	public void setType_id(AttachmentType type_id) {
		this.type_id = type_id;
	}
	/**
	 * @return the type_id
	 */
	public AttachmentType getType_id() {
		return type_id;
	}
}
