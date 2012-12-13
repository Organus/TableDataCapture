package gov.nrel.nbc.tracker.model;

public class Destinations {
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Display name.
	 */
	private String name;
	
	private String description;
	
	/**
	 * Simple constructor
	 */
	public Destinations() {
		name = null;
	}
	
	/**
	 * Complex constructor
	 * 
	 * @param value
	 */
	public Destinations(String value) {
		name = value;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
