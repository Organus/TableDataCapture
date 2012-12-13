package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.model.Treatments;

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
public class TreatmentsDAOHibernate extends GenericHibernateDAO<Treatments, Long>
		implements TreatmentsDAO {
	
	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List<String> names
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Treatments.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}

	/**
	 * Public method to find a Treatments by name.
	 * 
	 * @param value <String> name
	 * @return <Treatments>
	 */
	public Treatments findByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Treatments.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Treatments)crit.uniqueResult();
	}

	/**
	 * Public method to find a Treatments where the name is null.
	 * 
	 * @return <List<Treatments>>
	 */
	@SuppressWarnings("unchecked")
	public List<Treatments> findNull() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Treatments.class)
			.add(Restrictions.isNull(NAME));
				
		return (List<Treatments>)crit.list();
	}

}
