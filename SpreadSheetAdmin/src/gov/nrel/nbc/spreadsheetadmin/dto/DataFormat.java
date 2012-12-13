package gov.nrel.nbc.spreadsheetadmin.dto;

public class DataFormat {
	private long data_format_id;
	private String format;
	private String description;
	private String type;
	/**
	 * @return the data_format_id
	 */
	public long getData_format_id() {
		return data_format_id;
	}
	/**
	 * @param data_format_id the data_format_id to set
	 */
	public void setData_format_id(long data_format_id) {
		this.data_format_id = data_format_id;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
