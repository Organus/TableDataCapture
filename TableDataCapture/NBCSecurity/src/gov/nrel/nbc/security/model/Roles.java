package gov.nrel.nbc.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Roles implements Serializable
{
	private static final long serialVersionUID = 4900665360585750444L;
	private long pk;
	private String description;
	private String name;
	private Set<Permissions> permissions = new HashSet<Permissions> ();

    public Roles()
    {
        pk = 0;
        name = "";
    }
    public Roles(String name)
    {
        this();
        this.name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * @return Returns the pk.
     */
    public long getPk()
    {
        return pk;
    }
    
    /**
     * @param pk The pk to set.
     */
    public void setPk(long pk)
    {
        this.pk = pk;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "RoleInfo[" + "pk=" + pk + ", name=" + name + "]";
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
	 * @return the permissions
	 */
	public Set<Permissions> getPermissions() {
		return permissions;
	}
	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(Set<Permissions> permissions) {
		this.permissions = permissions;
	}
}