package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.RowData;
import gov.nrel.nbc.spreadsheet.dto.SheetData;

/**
 * An interface for the <RowData> business data access object.
 * @author James Albersheim
 *
 */
public interface RowDataDAO extends GenericDAO<RowData, Long> {

	public RowData findByRowNumAndSheet(int sheetRow, SheetData sheet);
}
