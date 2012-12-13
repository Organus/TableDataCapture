package gov.nrel.nbc.labelprinting.model;

import java.io.Serializable;

public class Attachments implements Serializable {
	private static final long serialVersionUID = 3349719293445599665L;
	private long attachment_id;
	private Sample sample;
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
	 * @return the sample
	 */
	public Sample getSample() {
		return sample;
	}
	/**
	 * @param sample the sample to set
	 */
	public void setSample(Sample sample) {
		this.sample = sample;
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
