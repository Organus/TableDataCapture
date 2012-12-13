/**
 * 
 */
package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.CellData;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;

import java.util.List;

/**
 * An interface for the CellData business data access object.
 * @author James Albersheim
 *
 */
public interface CellDataDAO extends GenericDAO<CellData, Long> {

	public List<String> findStringDataByHeader(CellDataHeader header);
	
}
