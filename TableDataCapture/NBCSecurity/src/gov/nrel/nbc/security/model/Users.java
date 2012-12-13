package gov.nrel.nbc.security.model;

import java.io.Serializable;
import java.util.Date;

public class Users implements Serializable
{
	private static final long serialVersionUID = -2398566069759867258L;
    private long pk;
    private boolean enabled;
    private String first;
    private String domain;
    private String last;
    private String email;
    private String userId;
    private String password;
    private String printer;
    private Date lastUsed;

    public Users() {
        pk = 0;
        enabled = false;
        userId = "";
        first = "";
        domain = "";
        last = "";
        email = "";
        password = "";
        setPrinter("");
    }
    public Users(String userId, String password, String first, String domain, String last, String email) {
    	this();
        this.userId = userId;
        this.first = first;
        this.domain = domain;
        this.last = last;
        this.email = email;
        this.password = password;
        this.enabled = false;
    }

	/*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "UserInfo[" + "pk=" + pk + ", userId=" + userId + ", first="
                + first + ", domain=" + domain + ", last=" + last + ", email=" 
                + email + ", enabled=" + enabled + "]";
    }

    public long getPk()
    {
        return pk;
    }
    public boolean isEnabled()
    {
        return enabled;
    }
    public String getFirst()
    {
        return first;
    }
    public String getLast()
    {
        return last;
    }
    public String getEmail()
    {
        return email;
    }
    public String getUserId()
    {
        return userId;
    }
    public String getPassword()
    {
        return password;
    }

    public void setPk(long val)
    {
        pk = val;
    }
    public void setEnabled(boolean val)
    {
    	enabled = val;
    }
    public void setFirst(String val)
    {
        first = val;
    }
    public void setLast(String val)
    {
        last = val;
    }
    public void setEmail(String val)
    {
        email = val;
    }
    public void setUserId(String val)
    {
        userId = val;
    }
    public void setPassword(String val)
    {
        password = val;
    }
	/**
	 * @return the lastUsed
	 */
	public Date getLastUsed() {
		return lastUsed;
	}
	/**
	 * @param lastUsed the lastUsed to set
	 */
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setPrinter(String printer) {
		this.printer = printer;
	}
	public String getPrinter() {
		return printer;
	}
}
