package gov.nrel.nbc.spreadsheetadmin.dto;

public class Metadata {
	private long metadata_id;
	private ValueData value;
	private MetadataHeader metadata_hdr_id;
	private WorkbookData workbook;
	/**
	 * @return the metadata_id
	 */
	public long getMetadata_id() {
		return metadata_id;
	}
	/**
	 * @param metadata_id the metadata_id to set
	 */
	public void setMetadata_id(long metadata_id) {
		this.metadata_id = metadata_id;
	}
	/**
	 * @return the value
	 */
	public ValueData getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(ValueData value) {
		this.value = value;
	}
	/**
	 * @return the metadata_hdr_id
	 */
	public MetadataHeader getMetadata_hdr_id() {
		return metadata_hdr_id;
	}
	/**
	 * @param metadata_hdr_id the metadata_hdr_id to set
	 */
	public void setMetadata_hdr_id(MetadataHeader metadata_hdr_id) {
		this.metadata_hdr_id = metadata_hdr_id;
	}
	/**
	 * @param workbook the WorkbookData to set
	 */
	public void setWorkbook(WorkbookData workbook) {
		this.workbook = workbook;
	}
	/**
	 * @return the workbook_id
	 */
	public WorkbookData getWorkbook() {
		return workbook;
	}
}
