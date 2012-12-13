package gov.nrel.nbc.spreadsheetadmin.dto;

/**
 * This is a data transfer object class. <DataType> is a class
 * that represents the type of a data element.
 *
 * @author James Albersheim
 */
public class DataType {
	
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Text the may be displayed to a user. Pulled from the spreadsheet
	 * headings.
	 */
	private String description;
	
	/**
	 * simple constructor.
	 */
	public DataType() {
		
	}

	/**
	 * Complex constructor
	 */
	public DataType(String type) {
		this.description = type;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
}
