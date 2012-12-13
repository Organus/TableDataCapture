package gov.nrel.nbc.labelprinting.model;

/**
 * This is a data transfer object class. An <AmountUnits> represents a
 * unit of measure for a sample.
 * 
 * @author jalbersh
 */
public class AmountUnits {
	
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Display name.
	 */
	private String name;
	
	public AmountUnits() {
		name = null;
	}
	
	/**
	 * @param name
	 */
	public AmountUnits(String name) {
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
