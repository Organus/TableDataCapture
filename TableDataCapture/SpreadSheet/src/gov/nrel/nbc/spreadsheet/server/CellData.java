package gov.nrel.nbc.spreadsheet.server;

public class CellData {
	private long order;
	private String value;
	/**
	 * @return the order
	 */
	public long getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(long order) {
		this.order = order;
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
