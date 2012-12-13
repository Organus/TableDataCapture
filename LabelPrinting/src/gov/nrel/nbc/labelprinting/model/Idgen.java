package gov.nrel.nbc.labelprinting.model;

/**
 * This is a data transfer object class. An Idgen represents a
 * identifying number generator designated by type.
 * 
 * @author jalbersh
 */
public class Idgen {
	
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Differentiator: type of ID
	 */
	private String type;
	
	/**
	 * Description
	 */
	private String description;
	
	/**
	 * ID value
	 */
	private long value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
