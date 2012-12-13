package gov.nrel.nbc.spreadsheetadmin.dao;

import gov.nrel.nbc.spreadsheetadmin.dto.DataType;

/**
 * An interface for the <DataType> business data access object.
 * @author James Albersheim
 *
 */
public interface DataTypeDAO extends GenericDAO<DataType, Long> {

	public DataType findById(long id);
	
	public DataType findByDescription(String desc);
}
