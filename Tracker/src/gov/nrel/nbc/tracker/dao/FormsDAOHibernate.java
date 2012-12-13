package gov.nrel.nbc.tracker.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.tracker.model.Forms;

public class FormsDAOHibernate extends GenericHibernateDAO<Forms, Long>
		implements FormsDAO {

	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Forms.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}

	public Forms findByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Forms.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Forms)crit.uniqueResult();
	}
	
}
