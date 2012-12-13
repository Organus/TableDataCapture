package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.model.Idgen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate-specific implementation of the <tt>Idgen</tt>
 * non-CRUD data access object.
 *
 * @see Idgen
 * @author Bobby Berry
 */
public class IdgenDAOHibernate extends GenericHibernateDAO<Idgen, Long> implements IdgenDAO {

	/**
	 * A public method to return the next id by the type.
	 * 
	 * @param inType type used to find the object in the database.
	 * @return long that was found in database. Could be null.
	 */
	@SuppressWarnings("unchecked")
	public long getNextIdByType(String inType) {
    	Session session = getSession();
    	
		Criteria crit = session.createCriteria(Idgen.class)
			.add(Restrictions.eq("type", inType));
		List<Idgen> idGenList = (List<Idgen>)crit.list();
		Idgen idGen = idGenList.get(0);
		Long value = Long.valueOf(incrementIDByType(idGen, 1));
		
		return value.longValue();
	}

	/**
	 * private method to increment an id by type id.
	 * 
	 * @param idGen <Idgen> ID to increment
	 * @param i int Amount to increment ID by.
	 * @return long ID
	 */
	private long incrementIDByType(Idgen idGen, int i) {
    	Session session = getSession();
		
    	idGen.setValue(idGen.getValue() + i);
		session.save(idGen);
		
		return idGen.getValue();
	}
}
