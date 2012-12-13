package gov.nrel.nbc.spreadsheetadmin.client;

import java.io.Serializable;

/**
 * Public class to serve as a data transfer object that 
 *  contains name/value pairs and type information.
 * 
 * @author James Albersheim
 */
public class NameValue implements Serializable {
	private static final long serialVersionUID = 713055130856442880L;
	private String name;
	private String synonym;
	private String value;
	private boolean internal;
	private long dataType;
	private String dataFormat;
	private int order;
	
	public NameValue() {}
	public NameValue(String string, String string2) {
		name = string;
		value = string2;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(long dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the dataType
	 */
	public long getDataType() {
		return dataType;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param internal the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	/**
	 * @return the internal
	 */
	public boolean isInternal() {
		return internal;
	}
	/**
	 * @param dataFormat the dataFormat to set
	 */
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	/**
	 * @return the dateFormat
	 */
	public String getDataFormat() {
		return dataFormat;
	}
	/**
	 * @param synonym the synonym to set
	 */
	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}
	/**
	 * @return the synonym
	 */
	public String getSynonym() {
		return synonym;
	}
}
