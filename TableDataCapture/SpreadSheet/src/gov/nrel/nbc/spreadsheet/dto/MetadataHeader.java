package gov.nrel.nbc.spreadsheet.dto;


/**
 * A mapping table between <MetadataHeader> fields and
 *  a synonym show to the user.
 *  
 *  @author James Albersheim
 */
public class MetadataHeader {

	/**
	 * Generated unique identifier with no business meaning
	 */
	private long metadata_hdr_id;
	
	/**
	 * name of the metadata header - also a column in the data table 
	 */
	private String name;
	
	/**
	 * synonym of the metadata header - as found in the spreadsheet 
	 */
	private String synonym;
	
	/**
	 * A reference to the type of data this heading
	 * represents.
	 */
	private DataType typeId;
	
	private int hdr_order;
	
	private boolean internal;
	
	private WorkbookConfig workbook_config_id;
	
	private DataFormat data_format;
	/**
	 * @return the typeId
	 */
	public DataType getTypeId() {
		return typeId;
	}
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(DataType typeId) {
		this.typeId = typeId;
	}
	/**
	 * @param metadata_hdr_id the metadata_hdr_id to set
	 */
	public void setMetadata_hdr_id(long metadata_hdr_id) {
		this.metadata_hdr_id = metadata_hdr_id;
	}
	/**
	 * @return the metadata_hdr_id
	 */
	public long getMetadata_hdr_id() {
		return metadata_hdr_id;
	}
	/**
	 * @param workbook_config_id the workbook_config_id to set
	 */
	public void setWorkbook_config_id(WorkbookConfig workbook_config_id) {
		this.workbook_config_id = workbook_config_id;
	}
	/**
	 * @return the workbook_config_id
	 */
	public WorkbookConfig getWorkbook_config_id() {
		return workbook_config_id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the hdr_name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param hdr_order the hdr_order to set
	 */
	public void setHdr_order(int hdr_order) {
		this.hdr_order = hdr_order;
	}
	/**
	 * @return the hdr_order
	 */
	public int getHdr_order() {
		return hdr_order;
	}
	/**
	 * @param internal the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	/**
	 * @return the internal
	 */
	public boolean isInternal() {
		return internal;
	}
	/**
	 * @param data_format the data_format to set
	 */
	public void setData_format(DataFormat data_format) {
		this.data_format = data_format;
	}
	/**
	 * @return the data_format
	 */
	public DataFormat getData_format() {
		return data_format;
	}
	/**
	 * @param synonym the synonym to set
	 */
	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}
	/**
	 * @return the synonym
	 */
	public String getSynonym() {
		return synonym;
	}
}
