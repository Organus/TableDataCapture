package gov.nrel.nbc.spreadsheet.parse;

import gov.nrel.nbc.spreadsheet.dto.ValueData;

/**
 * Public class that represents a data transfer object (DTO) for transferring
 * the cell data from the spreadsheet.
 * 
 * @author James Albersheim
 * 
 */
public class CellDataDTO {

	private boolean empty;
	/**
	 * type of cell
	 */
	private String type;
	
	/**
	 * heading of cell
	 */
	private String tag;
	
	/**
	 * value of cell
	 */
	private ValueData value;
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @param empty the empty to set
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}
}
