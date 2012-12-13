package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;

import java.util.List;

/**
 * An interface for the <MetadataHeader> business data access object.
 * @author James Albersheim
 *
 */
public interface MetadataHeaderDAO extends GenericDAO<MetadataHeader, Long> {

	public DataType getTypeByName(String name);
	
	public List<String> findAllNames();
	
	public MetadataHeader findByName(String selection, WorkbookConfig config);
}
