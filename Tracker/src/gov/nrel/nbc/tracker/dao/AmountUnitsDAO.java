package gov.nrel.nbc.tracker.dao;

import java.util.List;

import gov.nrel.nbc.tracker.model.AmountUnits;

/**
 * An interface for the AmountUnits business data access object.
 * 
 * @author jalbersh
 *
 */
public interface AmountUnitsDAO extends GenericDAO<AmountUnits, Long> {

	public List<AmountUnits> findAll();
	
	public List<String> findAllNames();
	
	public AmountUnits findByName(String name);
}
