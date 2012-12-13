package gov.nrel.nbc.labelprinting.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.labelprinting.model.Custodians;

public class CustodiansDAOHibernate extends
		GenericHibernateDAO<Custodians, Long> implements CustodiansDAO {

	private static final String NAME = "name";

	public Custodians findByName(String value) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Custodians.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Custodians)crit.uniqueResult();
	}
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Custodians.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to retrieve all Custodian names.
	 * 
	 * @return <Collection<String>> names
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames(List<Long> sids) {

		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Custodians.class);
		List<Custodians> os = (List<Custodians>)crit.list();
		Iterator<Custodians> oit = os.iterator();
		while (oit.hasNext()) {
			Custodians o = oit.next();
			String name = o.getName();
			if (sids.contains(new Long(o.getId())))
				names.add(name);
		}
		return names;
	}

}
