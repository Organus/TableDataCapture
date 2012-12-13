package gov.nrel.nbc.labelprinting.model;

/**
 * This is a data transfer object class. A <State> represents the
 * status of a sample.
 * 
 * @author jalbersh
 */
public class Status {
	
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
	public Status() {
		name = null;
	}
	
	/**
	 * Complex constructor
	 * 
	 * @param name
	 */
	public Status(String name) {
		this.name = name;
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
