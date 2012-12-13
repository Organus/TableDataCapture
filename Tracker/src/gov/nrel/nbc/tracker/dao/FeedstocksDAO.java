package gov.nrel.nbc.tracker.dao;

import java.util.List;

import gov.nrel.nbc.tracker.model.Feedstocks;

/**
 * An interface for the Feedstocks business data access object.
 * 
 * @author jalbersh
 *
 */
public interface FeedstocksDAO extends GenericDAO<Feedstocks, Long> {

	public List<String> findAllNames();
	
	public Feedstocks findByName(String value);
}
