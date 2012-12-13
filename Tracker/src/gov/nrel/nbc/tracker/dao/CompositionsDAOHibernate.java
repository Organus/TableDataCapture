package gov.nrel.nbc.tracker.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.tracker.model.Composition;

public class CompositionsDAOHibernate extends
		GenericHibernateDAO<Composition, Long> implements CompositionsDAO {

	private static final String NAME = "name";
	@SuppressWarnings("unchecked")
	public Collection<String> findAllNames() {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Composition.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}
	public Composition findByName(String value) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Composition.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Composition)crit.uniqueResult();
	}

}
