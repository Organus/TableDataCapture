package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.DataType;

/**
 * An interface for the <DataType> business data access object.
 * @author James Albersheim
 *
 */
public interface DataTypeDAO extends GenericDAO<DataType, Long> {

	public DataType findById(long id);
	
	public DataType findByDescription(String desc);
}
