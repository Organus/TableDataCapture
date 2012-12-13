package gov.nrel.nbc.spreadsheet.client;

import java.io.Serializable;

/**
 * Public class to serve as a data transfer object for 
 *  attachment file information.
 * 
 * @author James Albersheim
 *
 */
public class FileInfo implements Serializable {
	private static final long serialVersionUID = -3963360811119656100L;
	public String path;
	public String name;
	public String filename;
	public String attachment_id;

	public FileInfo() {
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the attachment_id
	 */
	public String getAttachment_id() {
		return attachment_id;
	}
	/**
	 * @param attachment_id the attachment_id to set
	 */
	public void setAttachment_id(String attachment_id) {
		this.attachment_id = attachment_id;
	}
}