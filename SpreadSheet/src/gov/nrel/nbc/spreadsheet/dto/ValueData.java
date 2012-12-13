package gov.nrel.nbc.spreadsheet.dto;

import java.util.Date;

/**
 * A public class that holds the data values for the <ValueData>
 *  class structure.
 * 
 * @author James Albersheim
 *
 */
public class ValueData {

	/**
	 * Holder for longs
	 */
	private Long lvalue = -1L;

	/**
	 * Holder for Strings
	 */
	private String svalue = null;

	/**
	 * Holder for doubles
	 */
	private Double rvalue = new Double(-1);

	/**
	 * Holder for Dates
	 */
	private Date dvalue = null;

	/**
	 * Holder for booleans
	 */
	private Boolean bvalue = false;

	/**
	 * Holder for data_id
	 */
	private long data_id = 0;

	/**
	 * Holder for data_type
	 */
	private long data_type = 0;

	/**
	 * @return the lvalue
	 */
	public Long getLvalue() {
		return lvalue;
	}

	/**
	 * @param lvalue the lvalue to set
	 */
	public void setLvalue(Long lvalue) {
		this.lvalue = lvalue;
	}

	/**
	 * @return the svalue
	 */
	public String getSvalue() {
		return svalue;
	}

	/**
	 * @param svalue the svalue to set
	 */
	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}

	/**
	 * @return the rvalue
	 */
	public Double getRvalue() {
		return rvalue;
	}

	/**
	 * @param rvalue the rvalue to set
	 */
	public void setRvalue(Double rvalue) {
		this.rvalue = rvalue;
	}

	/**
	 * @return the dvalue
	 */
	public Date getDvalue() {
		return dvalue;
	}

	/**
	 * @param dvalue the dvalue to set
	 */
	public void setDvalue(Date dvalue) {
		this.dvalue = dvalue;
	}

	/**
	 * @return the bvalue
	 */
	public Boolean getBvalue() {
		return bvalue;
	}

	/**
	 * @param bvalue the bvalue to set
	 */
	public void setBvalue(Boolean bvalue) {
		this.bvalue = bvalue;
	}

	/**
	 * @return the data_id
	 */
	public long getData_id() {
		return data_id;
	}

	/**
	 * @param data_id the data_id to set
	 */
	public void setData_id(long data_id) {
		this.data_id = data_id;
	}

	/**
	 * @return the data_type
	 */
	public long getData_type() {
		return data_type;
	}

	/**
	 * @param data_type the data_type to set
	 */
	public void setData_type(long data_type) {
		this.data_type = data_type;
	}
}
