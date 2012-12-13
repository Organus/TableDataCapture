package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.model.Trb;

/**
 * An interface for the Trb business data access object.
 * 
 * @author jalbersh
 *
 */
public interface TrbDAO extends CalcSheetDAO<Trb, Long> {

	public Trb findByNumAndPage(int trbNum, int page);
}
