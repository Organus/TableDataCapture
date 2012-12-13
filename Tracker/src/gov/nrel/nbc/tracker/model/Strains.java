package gov.nrel.nbc.tracker.model;

public class Strains {
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
	public Strains() {
		name = null;
	}
	
	/**
	 * Complex constructor
	 * 
	 * @param value
	 */
	public Strains(String value) {
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
