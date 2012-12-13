package gov.nrel.nbc.labelprinting.dao;

import java.util.List;

import gov.nrel.nbc.labelprinting.model.Status;

/**
 * An interface for the Status business data access object.
 * 
 * @author jalbersh
 *
 */
public interface StatusDAO extends GenericDAO<Status, Long> {

	public List<String> findAllNames();
	
	public Status findByName(String name);
}
