package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.ValueData;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class ValueDataDAOHibernate extends
		GenericHibernateDAO<ValueData, Long> implements ValueDataDAO {

	public long getMaxId() {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(ValueData.class)
			.setProjection(Projections.max("data_id"));
			
				
		return (Long)crit.uniqueResult();
	}

}
