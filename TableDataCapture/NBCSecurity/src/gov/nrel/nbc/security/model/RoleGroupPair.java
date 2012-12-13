package gov.nrel.nbc.security.model;

public class RoleGroupPair {
	private long pk;
	private long role_pk;
	private long group_pk;
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
	 * @return the role_pk
	 */
	public long getRole_pk() {
		return role_pk;
	}
	/**
	 * @param role_pk the role_pk to set
	 */
	public void setRole_pk(long role_pk) {
		this.role_pk = role_pk;
	}
	/**
	 * @return the group_pk
	 */
	public long getGroup_pk() {
		return group_pk;
	}
	/**
	 * @param group_pk the group_pk to set
	 */
	public void setGroup_pk(long group_pk) {
		this.group_pk = group_pk;
	}
}
