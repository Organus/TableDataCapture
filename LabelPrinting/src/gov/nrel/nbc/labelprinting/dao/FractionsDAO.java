package gov.nrel.nbc.labelprinting.dao;

import java.util.List;

import gov.nrel.nbc.labelprinting.model.Fractions;

/**
 * An interface for the Fractions business data access object.
 * 
 * @author jalbersh
 *
 */
public interface FractionsDAO extends GenericDAO<Fractions, Long> {

	public List<String> findAllNames();
	
	public Fractions findByName(String value);
}
