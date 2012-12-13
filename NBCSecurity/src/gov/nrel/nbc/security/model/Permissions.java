package gov.nrel.nbc.security.model;

import java.io.Serializable;

public class Permissions implements Serializable
{
	private static final long serialVersionUID = -7736901444861663428L;
	private long pk;
	private String name;
	private String description;

	public Permissions() {
		pk = 0;
		name = "";
		description = "";
	}
	public Permissions(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}
    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
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
}

