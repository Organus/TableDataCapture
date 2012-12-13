package gov.nrel.nbc.security.model;

public class DBLogins {
	private String user;
	private String password;
	private DBNames db;
	private long pk;
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the db
	 */
	public DBNames getDb() {
		return db;
	}
	/**
	 * @param db the db to set
	 */
	public void setDb(DBNames db) {
		this.db = db;
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
	
}
