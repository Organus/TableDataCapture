package gov.nrel.nbc.spreadsheet.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SheetConfig implements Serializable {
	private static final long serialVersionUID = 1132990081439581073L;
	private long sheet_config_id;
	private String sheet_name;
	private String synonym;
	private long hdr_row;
	private long hdr_col;
	private long data_row;
	private long data_col;
	private long meta_start_row;
	private long meta_start_col;
	private long meta_end_row;
	private long meta_end_col;
	private Set<CellDataHeader> cell_hdrs = new HashSet<CellDataHeader> ();
	/**
	 * @return the sheet_name
	 */
	public String getSheet_name() {
		return sheet_name;
	}
	/**
	 * @param sheet_name the sheet_name to set
	 */
	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}
	/**
	 * @return the hdr_row
	 */
	public long getHdr_row() {
		return hdr_row;
	}
	/**
	 * @param hdr_row the hdr_row to set
	 */
	public void setHdr_row(long hdr_row) {
		this.hdr_row = hdr_row;
	}
	/**
	 * @return the hdr_col
	 */
	public long getHdr_col() {
		return hdr_col;
	}
	/**
	 * @param hdr_col the hdr_col to set
	 */
	public void setHdr_col(long hdr_col) {
		this.hdr_col = hdr_col;
	}
	/**
	 * @return the data_row
	 */
	public long getData_row() {
		return data_row;
	}
	/**
	 * @param data_row the data_row to set
	 */
	public void setData_row(long data_row) {
		this.data_row = data_row;
	}
	/**
	 * @return the data_col
	 */
	public long getData_col() {
		return data_col;
	}
	/**
	 * @param data_col the data_col to set
	 */
	public void setData_col(long data_col) {
		this.data_col = data_col;
	}
	/**
	 * @param cell_hdrs the cell_hdrs to set
	 */
	public void setCell_hdrs(Set<CellDataHeader> cell_hdrs) {
		this.cell_hdrs = cell_hdrs;
	}
	/**
	 * @return the cells
	 */
	public Set<CellDataHeader> getCell_hdrs() {
		return cell_hdrs;
	}
	/**
	 * @param sheet_config_id the sheet_config_id to set
	 */
	public void setSheet_config_id(long sheet_config_id) {
		this.sheet_config_id = sheet_config_id;
	}
	/**
	 * @return the sheet_config_id
	 */
	public long getSheet_config_id() {
		return sheet_config_id;
	}
	/**
	 * @return the meta_start_row
	 */
	public long getMeta_start_row() {
		return meta_start_row;
	}
	/**
	 * @param meta_start_row the meta_start_row to set
	 */
	public void setMeta_start_row(long meta_start_row) {
		this.meta_start_row = meta_start_row;
	}
	/**
	 * @return the meta_start_col
	 */
	public long getMeta_start_col() {
		return meta_start_col;
	}
	/**
	 * @param meta_start_col the meta_start_col to set
	 */
	public void setMeta_start_col(long meta_start_col) {
		this.meta_start_col = meta_start_col;
	}
	/**
	 * @return the meta_end_row
	 */
	public long getMeta_end_row() {
		return meta_end_row;
	}
	/**
	 * @param meta_end_row the meta_end_row to set
	 */
	public void setMeta_end_row(long meta_end_row) {
		this.meta_end_row = meta_end_row;
	}
	/**
	 * @return the meta_end_col
	 */
	public long getMeta_end_col() {
		return meta_end_col;
	}
	/**
	 * @param meta_end_col the meta_end_col to set
	 */
	public void setMeta_end_col(long meta_end_col) {
		this.meta_end_col = meta_end_col;
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
