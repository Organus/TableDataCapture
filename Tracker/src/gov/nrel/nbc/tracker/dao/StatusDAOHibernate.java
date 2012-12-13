package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Status;

import java.util.ArrayList;
import java.util.Iterator;
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
public class StatusDAOHibernate extends GenericHibernateDAO<Status, Long>
		implements StatusDAO {
	
	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * @param sids 
	 * 
	 * @return <List<String>> List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames(List<Long> sids) {

		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Status.class);
			//.add(Restrictions.eq(ID, sids));
			//.addOrder(Order.asc(ID));
		List<Status> os = (List<Status>)crit.list();
		Iterator<Status> oit = os.iterator();
		while (oit.hasNext()) {
			Status o = oit.next();
			String name = o.getName();
			if (sids.contains(new Long(o.getId())))
				names.add(name);
		}
		
		return names;
	}
	
	/**
	 * Public method to return all names
	 * 
	 * @return <List<String>> List of Strings.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Status.class)
			.setProjection(Projections.distinct(Projections.property(NAME)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to return all names
	 * 
	 * @param name - <String>
	 * @return <Status>
	 */
	public Status findByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Status.class)
			.add(Restrictions.eq(NAME, name));
				
		return (Status)crit.uniqueResult();
	}

	/**
	 * Public method to find a Status where the name is null.
	 * 
	 * @return <List<Status>>
	 */
	@SuppressWarnings("unchecked")
	public List<Status> findNull() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Status.class)
			.add(Restrictions.isNull(NAME));
				
		return (List<Status>)crit.list();
	}

}
