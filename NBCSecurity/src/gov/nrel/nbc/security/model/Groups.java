package gov.nrel.nbc.security.model;

import java.io.Serializable;

public class Groups implements Serializable
{
	private static final long serialVersionUID = -2252627549501165434L;
	private long pk;
	private String name;
	private String description;
	private long group_type;

	public Groups() {
		pk = 0;
		name = "";
	}
	public Groups(String name) {
		this();
		this.name = name;
	}
	/**
	 * @return the pk
	 */
	public long getPk() {
		return pk;
	}
	/**
	 * @param pk the pk to set
	 */
	public void setPk(long pk) {
		this.pk = pk;
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
	/**
	 * @return the group_type
	 */
	public long getGroup_type() {
		return group_type;
	}
	/**
	 * @param group_type the group_type to set
	 */
	public void setGroup_type(long group_type) {
		this.group_type = group_type;
	}
}

