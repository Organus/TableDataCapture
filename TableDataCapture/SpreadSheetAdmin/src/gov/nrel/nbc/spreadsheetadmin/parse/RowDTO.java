package gov.nrel.nbc.spreadsheetadmin.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Public class that represents a data transfer object (DTO) for transferring
 * the row data from the spreadsheetadmin.
 * 
 * @author James Albersheim
 * 
 */
public class RowDTO {
	
	/**
	 * Row number from spreadsheet
	 */
	private int sheetRow;
	
	/**
	 * List of cell information
	 */
	private List<CellDataDTO> row = null;
	
	/**
	 * @return the row
	 */
	public List<CellDataDTO> getRow() {
		return row;
	}
	
	/**
	 * @param row the row to set
	 */
	public void setRow(List<CellDataDTO> row) {
		this.row = row;
	}
	
	/**
	 * @return the sheetRow
	 */
	public int getSheetRow() {
		return sheetRow;
	}
	
	/**
	 * @param sheetRow the sheetRow to set
	 */
	public void setSheetRow(int sheetRow) {
		this.sheetRow = sheetRow;
	}
	
	/**
	 * Public method to add a cell to the row.
	 * 
	 * @param cell <CellDataDTO>
	 */
	public void addCell(CellDataDTO cell) {
		if (this.row == null) {
			this.row = new ArrayList<CellDataDTO>();
		}
		if (cell != null)
			this.row.add(cell);
	}

}
