package gov.nrel.nbc.spreadsheet.dao;

import java.util.List;

import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.Operators;

/**
 * An interface for the <Operators> business data access object.
 * @author James Albersheim
 *
 */
public interface OperatorsDAO extends GenericDAO<Operators, Long> {

	public List<String> getByDataType(DataType dataType);
}
