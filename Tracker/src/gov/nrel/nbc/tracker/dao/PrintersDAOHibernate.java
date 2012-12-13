package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Printers;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PrintersDAOHibernate extends GenericHibernateDAO<Printers, Long>
		implements GenericDAO<Printers, Long> {

	private static final String NAME = "name";

	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	public Printers findByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Printers.class)
			.add(Restrictions.eq(NAME, value));
				
		return (Printers)crit.uniqueResult();
	}
	/**
	 * Public method to return all names
	 * 
	 * @return List of Strings.
	 */
	public boolean getStatusByName(String value) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Printers.class)
			.add(Restrictions.eq(NAME, value));
				
		Printers printer = (Printers)crit.uniqueResult();
		return printer.isStatus();
	}
}
