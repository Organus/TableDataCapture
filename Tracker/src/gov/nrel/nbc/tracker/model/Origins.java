package gov.nrel.nbc.tracker.model;

public class Origins {
	private long originId;
	private String name;
	private String description;
	/**
	 * @return the originId
	 */
	public long getOriginId() {
		return originId;
	}
	/**
	 * @param originId the originId to set
	 */
	public void setOriginId(long originId) {
		this.originId = originId;
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
