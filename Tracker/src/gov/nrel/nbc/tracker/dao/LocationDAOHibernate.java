package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Location;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author jalbersh
 *
 */
public class LocationDAOHibernate extends GenericHibernateDAO<Location, Long>
		implements LocationDAO {
	
	private static final String SHELF = "shelf";
	private static final String HOLDER = "holder";
	private static final String ROOM = "room";
	private static final String BUILDING = "building";
	private static final String PACKAGING = "packaging";
	private static final String SUB_LOCATION = "subLocation";

	/**
	 * Public method to return all buildings
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllBuildings() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(BUILDING)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all rooms
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllRooms() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(ROOM)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all holders
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllHolders() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(HOLDER)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all shelves
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllShelves() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(SHELF)));
				
		return (List<String>)crit.list();
	}

	/**
	 * Public method to return all packaging
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllPackaging() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(PACKAGING)));
				
		return (List<String>)crit.list();
	}

	/**
	 * Public method to return all sublocations
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllSubLocations() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Location.class)
			.setProjection(Projections.distinct(Projections.property(SUB_LOCATION)));
				
		return (List<String>)crit.list();
	}
}
