package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.model.Origins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class OriginsDAOHibernate extends GenericHibernateDAO<Origins, Long>
		implements OriginsDAO {
	private static final String NAME = "name";
	//private static final String ID = "originId";

	@SuppressWarnings("unchecked")
	public List<String> findAllNames(List<Long> sids) {

		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Origins.class);
			//.add(Restrictions.eq(ID, sids));
			//.addOrder(Order.asc(ID));
		List<Origins> os = (List<Origins>)crit.list();
		Iterator<Origins> oit = os.iterator();
		while (oit.hasNext()) {
			Origins o = oit.next();
			String name = o.getName();
			if (sids != null && !sids.isEmpty()) {
				if (sids.contains(new Long(o.getOriginId())))
					names.add(name);
			}
		}
		
		return names;
	}


	public List<String> findAllNames() {

		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Origins.class);
			//.add(Restrictions.eq(ID, sids));
			//.addOrder(Order.asc(ID));
		List<Origins> os = (List<Origins>)crit.list();
		Iterator<Origins> oit = os.iterator();
		while (oit.hasNext()) {
			Origins o = oit.next();
			String name = o.getName();
			//if (sids != null && !sids.isEmpty()) {
			//	if (sids.contains(new Long(o.getOriginId())))
					names.add(name);
			//}
		}
		
		return names;
	}


	public Origins findByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Origins.class)
			.add(Restrictions.eq(NAME, name));
		
		return (Origins)crit.uniqueResult();
	}


}
