package gov.nrel.nbc.tracker.dao;

import java.util.Collection;
import java.util.List;

import gov.nrel.nbc.tracker.client.SampleCriteria;
import gov.nrel.nbc.tracker.model.Sample;

/**
 * An interface for the Sample business data access object.
 * 
 * @author jalbersh
 *
 */
public interface SampleDAO extends GenericDAO<Sample, Long> {

	public Sample findById(Long id, boolean lock);
	
	public List<Sample> findByCriteria(SampleCriteria criteria);
	
	public Collection<SampleCriteria> convertSamples2Criteria(List<Sample> samples);
	
	public SampleCriteria convertSample2Criterium(Sample sample);
	
	public Collection<String> findAllSampleIds();
	
	public Collection<String> findAllCustodians();
	
	public Collection<String> findAllOwners();
}
