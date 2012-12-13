package gov.nrel.nbc.spreadsheetadmin.dao;

import java.util.List;

import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.DataType;

/**
 * An interface for the <CellDataHeader> business data access object.
 * @author James Albersheim
 *
 */
public interface CellHeaderDAO extends GenericDAO<CellDataHeader, Long> {

	public CellDataHeader findByName(String name);
	
	public DataType getTypeByName(String name);
	
	public List<String> findAllNames();
	
	public int getNextIndexValue();
}
