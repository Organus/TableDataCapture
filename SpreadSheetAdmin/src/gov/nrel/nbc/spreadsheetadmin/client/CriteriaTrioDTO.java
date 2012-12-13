package gov.nrel.nbc.spreadsheetadmin.client;

import java.io.Serializable;

/**
 * Public class that represents a data transfer object (DTO) for transferring
 * the user search criteria to the server.
 * 
 * @author James Albersheim
 * 
 */
public class CriteriaTrioDTO implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 9191058524285761660L;

	/**
	 * Private String holding the header pick-list selection
	 */
	private String header;

	/**
	 * Private String holding the comparison operator selection
	 */
	private String operator;

	/**
	 * Private String holding the search value
	 */
	private String value;

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return <String> the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
