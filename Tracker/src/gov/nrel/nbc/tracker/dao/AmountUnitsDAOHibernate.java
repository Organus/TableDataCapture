package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.AmountUnits;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author jalbersh
 *
 */
public class AmountUnitsDAOHibernate extends
		GenericHibernateDAO<AmountUnits, Long> implements AmountUnitsDAO {

	private static final String NAME = "name";

	/**
	 * Public method to find all records.
	 * 
	 * @return <List<AmountUnits>>
	 */
	@SuppressWarnings("unchecked")
	public List<AmountUnits> findAll() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(AmountUnits.class);
				
		return (List<AmountUnits>)crit.list();
	}
	
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(AmountUnits.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to retrieve an AmountUnits by name.
	 * 
	 * @param name <String> AmountUnits name
	 * 
	 * @return <AmountUnits>
	 */
	public AmountUnits findByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(AmountUnits.class)
			.add(Restrictions.eq(NAME, name));
				
		return (AmountUnits)crit.uniqueResult();
	}
}
