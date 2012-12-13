package gov.nrel.nbc.spreadsheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.Operators;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 * 
 */
public class OperatorsDAOHibernate extends GenericHibernateDAO<Operators, Long>
		implements OperatorsDAO {
	
	private static final String OPERATOR = "operator";
	private static final String DATA_TYPE_ID = "data_type_id";

	/**
	 * Public method to find operators by DataType
	 * 
	 * @param dataType <DataType>
	 * @return <List<String>>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getByDataType(DataType dataType) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Operators.class)
			.add(Restrictions.eq(DATA_TYPE_ID, dataType))
			.setProjection(Projections.distinct(Projections.property(OPERATOR)));
				
		return (List<String>)crit.list();
	}
}
