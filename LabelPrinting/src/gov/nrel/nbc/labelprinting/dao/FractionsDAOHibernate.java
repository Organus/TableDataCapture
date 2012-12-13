package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.model.Fractions;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author jalbersh
 *
 */
public class FractionsDAOHibernate extends GenericHibernateDAO<Fractions, Long>
		implements FractionsDAO, AppConstants {
	
	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Fractions.class)
			.setProjection(Projections.distinct(Projections.property(NAME)))
			.addOrder(Order.desc(ID));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	public Fractions findByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Fractions.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Fractions)crit.uniqueResult();
	}
}
