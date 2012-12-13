package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.model.Strains;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class StrainsDAOHibernate extends GenericHibernateDAO<Strains, Long>
		implements StrainsDAO, AppConstants {

	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Strains.class)
			.setProjection(Projections.distinct(Projections.property(NAME)))
			.addOrder(Order.desc(ID));
				
		return (List<String>)crit.list();
	}

	public Strains findByName(String value) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Strains.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Strains)crit.uniqueResult();
	}
	
}
