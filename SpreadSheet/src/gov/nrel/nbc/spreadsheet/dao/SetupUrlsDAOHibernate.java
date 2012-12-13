package gov.nrel.nbc.spreadsheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import gov.nrel.nbc.spreadsheet.dto.SetupUrls;

public class SetupUrlsDAOHibernate extends GenericHibernateDAO<SetupUrls, Long>
		implements SetupUrlsDAO {

	private static final String ORDER = "url_id";
	/**
	 * Public method to get a list of synonyms.
	 * 
	 * @return <List<String>> - list of synonym values.
	 */
	@SuppressWarnings("unchecked")
	public List<SetupUrls> findAll() {

		Session session = getSession();

		Criteria crit = session.createCriteria(SetupUrls.class)
				.addOrder(Order.asc(ORDER));

		return (List<SetupUrls>) crit.list();
	}

}
