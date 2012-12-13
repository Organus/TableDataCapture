package gov.nrel.nbc.spreadsheet.dto;

/**
 * This is a data transfer object class. This class
 * represents an operator that can be used in a query.
 * Some operators are not valid for business reasons
 * for some <DataType>'s.
 *
 * @author James Albersheim
 */
public class Operators {

	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * A reference to the type of data this operator
	 * will work for.
	 */
	private DataType data_type_id;
	
	private String operator;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DataType getData_type_id() {
		return data_type_id;
	}

	public void setData_type_id(DataType data_type_id) {
		this.data_type_id = data_type_id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
