package gov.nrel.nbc.tracker.dao;

import java.util.List;

import gov.nrel.nbc.tracker.model.Status;

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
