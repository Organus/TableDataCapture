package gov.nrel.nbc.spreadsheet.dto;

import java.util.Date;

/**
 * A public class that holds the data values for the <GenericData>
 *  class structure.
 * 
 * @author James Albersheim
 *
 */
public class GenericValue {

	/**
	 * Holder for longs
	 */
	private long longValue = -1;

	/**
	 * Holder for Strings
	 */
	private String stringValue = null;

	/**
	 * Holder for doubles
	 */
	private double realValue = -1.0;

	/**
	 * Holder for Dates
	 */
	private Date dateValue = null;

	/**
	 * Holder for booleans
	 */
	private Boolean booleanValue = false;
	
	/**
	 * @return the intValue
	 */
	public long getLongValue() {
		return longValue;
	}

	/**
	 * @param longValue the longValue to set
	 */
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the realValue
	 */
	public double getRealValue() {
		return realValue;
	}

	/**
	 * @param realValue the realValue to set
	 */
	public void setRealValue(double realValue) {
		this.realValue = realValue;
	}

	/**
	 * @return the dateValue
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * @param dateValue the dateValue to set
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
}
