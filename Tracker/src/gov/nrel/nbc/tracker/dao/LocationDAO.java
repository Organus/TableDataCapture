package gov.nrel.nbc.tracker.dao;

import java.util.List;

import gov.nrel.nbc.tracker.model.Location;

/**
 * An interface for the Location business data access object.
 * 
 * @author jalbersh
 *
 */
public interface LocationDAO extends GenericDAO<Location, Long> {

	public List<String> findAllBuildings();
	
	public List<String> findAllRooms();
	
	public List<String> findAllHolders();
	
	public List<String> findAllShelves();
	
	public List<String> findAllPackaging();
	
	public List<String> findAllSubLocations();
}
