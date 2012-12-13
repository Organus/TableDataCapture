package gov.nrel.nbc.tracker.model;

public class Forms {
	private long id;
	private String name;
	public Forms() {
		name = null;
	}
	public Forms(String value) {
		name = value;
	}
	/**
	 * @return the forms_id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param forms_id the forms_id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
