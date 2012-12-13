package gov.nrel.nbc.tracker.model;

/**
 * This is a data transfer object class. A <Treatments> represents the
 * treatment of a sample.
 * 
 * @author jalbersh
 */
public class Treatments {
	
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
	public Treatments() {
		name = null;
	}
	
	/**
	 * Complex constructor
	 * @param value
	 */
	public Treatments(String value) {
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
