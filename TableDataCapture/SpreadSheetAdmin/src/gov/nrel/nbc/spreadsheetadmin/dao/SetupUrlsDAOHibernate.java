package gov.nrel.nbc.spreadsheetadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import gov.nrel.nbc.spreadsheetadmin.dto.SetupUrls;

public class SetupUrlsDAOHibernate extends GenericHibernateDAO<SetupUrls, Long>
		implements SetupUrlsDAO {

	private static final String ORDER = "url_id";
	/**
	 * Public method to get all SetupUrls in order.
	 * 
	 * @return <List<SetupUrls>>.
	 */
	@SuppressWarnings("unchecked")
	public List<SetupUrls> findAll() {

		Session session = getSession();

		Criteria crit = session.createCriteria(SetupUrls.class)
				.addOrder(Order.asc(ORDER));

		return (List<SetupUrls>) crit.list();
	}
}
