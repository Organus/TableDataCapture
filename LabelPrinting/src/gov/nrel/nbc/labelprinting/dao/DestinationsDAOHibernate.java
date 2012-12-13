package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.model.Destinations;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DestinationsDAOHibernate extends
		GenericHibernateDAO<Destinations, Long> implements DestinationsDAO, AppConstants {

	private static final String NAME = "name";
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Destinations.class)
			.setProjection(Projections.distinct(Projections.property(NAME)))
			.addOrder(Order.asc(ID));
				
		return (List<String>)crit.list();
	}
	public Destinations findByName(String value) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Destinations.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Destinations)crit.uniqueResult();
	}
	
}
