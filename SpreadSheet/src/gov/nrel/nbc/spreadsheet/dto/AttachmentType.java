package gov.nrel.nbc.spreadsheet.dto;

import java.io.Serializable;

public class AttachmentType implements Serializable {
	private static final long serialVersionUID = 8276489376236762837L;
	private long type_id;
	private String ext;
	/**
	 * @return the type_id
	 */
	public long getType_id() {
		return type_id;
	}
	/**
	 * @param type_id the type_id to set
	 */
	public void setType_id(long type_id) {
		this.type_id = type_id;
	}
	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}
	/**
	 * @param ext the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
}
