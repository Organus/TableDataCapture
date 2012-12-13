package gov.nrel.nbc.spreadsheetadmin.dto;

import java.util.Comparator;

/**
 * This is a data transfer object class for <CellData>. A CellData represents that
 * actual value captured during the running of an analysis.
 * 
 * @author James Albersheim
 */
public class CellData implements Comparable<CellData>, Comparator<CellData> {

	/**
	 * Generated unique identifier with no business meaning
	 */
	private long cell_data_id;

	/**
	 * The row that this data belongs to in the spreadsheetadmin.
	 */
	private RowData rowId;

	/**
	 * A reference to the actual value from the spreadsheet cell.
	 */
	private ValueData valueId;

	/**
	 * CellTag that represents column heading
	 */
	private CellDataHeader cell_hdr_id;

	/**
	 * Simple constructor
	 */
	public CellData() {
	}

	/**
	 * Complex constructor.
	 * 
	 * @param tag
	 */
	public CellData(CellDataHeader tag) {
		this.setCell_hdr_id(tag);
	}

	/**
	 * @return the rowId
	 */
	public RowData getRowId() {
		return rowId;
	}

	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(RowData rowId) {
		this.rowId = rowId;
	}

	/**
	 * @return the valueId
	 */
	public ValueData getValueId() {
		return valueId;
	}

	/**
	 * @param valueId
	 *            the valueId to set
	 */
	public void setValueId(ValueData valueId) {
		this.valueId = valueId;
	}

	public boolean equals(CellData o) {
		if (o == null) return false;
		if (this.getRowId().getId() == o.getRowId().getId()) return true;
		if (this.getRowId().getId() > o.getRowId().getId()) return false;
		else return false;
	}

	public int compareTo(CellData o) {
		if (o == null) return -1;
		if (this.getRowId().getId() == o.getRowId().getId()) return 0;
		if (this.getRowId().getId() > o.getRowId().getId()) return 1;
		else return -1;
	}

	public int compare(CellData o1, CellData o2) {
		if (o1 == null) return -1;
		if (o2.getRowId().getId() == o1.getRowId().getId()) return 0;
		if (o2.getRowId().getId() > o1.getRowId().getId()) return 1;
		else return -1;
	}

	/**
	 * @param cell_hdr_id the cell_hdr_id to set
	 */
	public void setCell_hdr_id(CellDataHeader cell_hdr_id) {
		this.cell_hdr_id = cell_hdr_id;
	}

	/**
	 * @return the cell_hdr_id
	 */
	public CellDataHeader getCell_hdr_id() {
		return cell_hdr_id;
	}

	/**
	 * @param cell_data_id the cell_data_id to set
	 */
	public void setCell_data_id(long cell_data_id) {
		this.cell_data_id = cell_data_id;
	}

	/**
	 * @return the cell_data_id
	 */
	public long getCell_data_id() {
		return cell_data_id;
	}

}
