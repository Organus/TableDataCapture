package gov.nrel.nbc.tracker.dao;

import java.util.List;

import gov.nrel.nbc.tracker.model.Treatments;

/**
 * An interface for the Treatments business data access object.
 * 
 * @author jalbersh
 *
 */
public interface TreatmentsDAO extends GenericDAO<Treatments, Long> {

	public List<String> findAllNames();
	
	public Treatments findByName(String value);
}
