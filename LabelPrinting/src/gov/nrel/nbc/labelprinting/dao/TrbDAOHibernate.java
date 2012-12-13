package gov.nrel.nbc.labelprinting.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.labelprinting.model.Trb;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author jalbersh
 *
 */
public class TrbDAOHibernate extends CalcSheetHibernateDAO<Trb, Long> implements
		TrbDAO {

	private static final String PAGE = "page";
	private static final String NUM = "num";

	/**
	 * Public method to find a Trb using the trb number and
	 *  trb page number.
	 * 
	 * @param trbNum int
	 * @param page int
	 * @return <Trb>
	 */
	public Trb findByNumAndPage(int trbNum, int page) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Trb.class)
		.add(Restrictions
			.and(Restrictions.eq(NUM, trbNum),
				Restrictions.eq(PAGE, page)));
				
		return (Trb)crit.uniqueResult();
	}
}
