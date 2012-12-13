package gov.nrel.nbc.spreadsheetadmin.dto;


/**
 * This is a data transfer object class for <Tag>. <Tag> is a class
 * that represents that data's column headings.
 *
 * @author James Albersheim
 */
public class CellDataHeader {
	
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long cell_hdr_id;
	
	/**
	 * Display index
	 */
	private int hdr_index;
	
	/**
	 * Cell header - also a column in the data table
	 */
	private String name;
	
	/**
	 * synonym of the cell header - as found in the spreadsheet 
	 */
	private String synonym;
	
	/**
	 * A reference to the type of data this heading
	 * represents.
	 */
	private DataType typeId;
	
	private DataFormat data_format;

	/**
	 * A reference to the workbook config
	 */
	private SheetConfig sheet_config_id;

	/**
	 * Simple constructor.
	 */
	public CellDataHeader() {
		setName(null);
	}

	/**
	 * Complex constructor.
	 * 
	 * @param name <String>
	 */
	public CellDataHeader(String name) {
		this.setName(name);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

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
	 * @param cell_hdr_id the cell_hdr_id to set
	 */
	public void setCell_hdr_id(long cell_hdr_id) {
		this.cell_hdr_id = cell_hdr_id;
	}

	/**
	 * @return the cell_hdr_id
	 */
	public long getCell_hdr_id() {
		return cell_hdr_id;
	}

	/**
	 * @param hdr_index the hdr_index to set
	 */
	public void setHdr_index(int hdr_index) {
		this.hdr_index = hdr_index;
	}

	/**
	 * @return the hdr_index
	 */
	public int getHdr_index() {
		return hdr_index;
	}

	/**
	 * @param sheet_config_id the sheet_config_id to set
	 */
	public void setSheet_config_id(SheetConfig sheet_config_id) {
		this.sheet_config_id = sheet_config_id;
	}

	/**
	 * @return the sheet_config_id
	 */
	public SheetConfig getSheet_config_id() {
		return sheet_config_id;
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
