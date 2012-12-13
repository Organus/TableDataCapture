package gov.nrel.nbc.security.model;

public class Logins {
	private long pk;
	private long user_pk;
	private String id;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param ret the id to set
	 */
	public void setId(String ret) {
		this.id = ret;
	}
	/**
	 * @param user_pk the user_pk to set
	 */
	public void setUser_pk(long user_pk) {
		this.user_pk = user_pk;
	}
	/**
	 * @return the user_pk
	 */
	public long getUser_pk() {
		return user_pk;
	}
}
