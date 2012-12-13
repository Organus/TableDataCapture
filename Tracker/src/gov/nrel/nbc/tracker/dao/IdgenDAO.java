package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Idgen;

/**
 * An interface for the IdgenDAO business data access object.
 * 
 * @author jalbersh
 *
 */
public interface IdgenDAO extends GenericDAO<Idgen, Long> {
	
	/**
	 * A method to find the object by a code string.
	 * 
	 * @param inType <String> code used to find the object in the database.
	 * @return long The next ID.
	 */
	long getNextIdByType(String inType);
}
