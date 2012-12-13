package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Trb;

/**
 * An interface for the Trb business data access object.
 * 
 * @author jalbersh
 *
 */
public interface TrbDAO extends CalcSheetDAO<Trb, Long> {

	public Trb findByNumAndPage(int trbNum, int page);
}
