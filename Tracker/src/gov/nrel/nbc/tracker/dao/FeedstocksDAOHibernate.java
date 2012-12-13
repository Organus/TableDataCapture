package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Feedstocks;

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
public class FeedstocksDAOHibernate extends
		GenericHibernateDAO<Feedstocks, Long> implements FeedstocksDAO {
	
	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Feedstocks.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	public Feedstocks findByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Feedstocks.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Feedstocks)crit.uniqueResult();
	}

}
