package gov.nrel.nbc.labelprinting.model;

public class Sources {
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Display name.
	 */
	private String name;
	
	/**
	 * Simple constructor
	 */
	public Sources() {
		name = null;
	}
	
	/**
	 * Complex constructor
	 * 
	 * @param value
	 */
	public Sources(String value) {
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
}
