package gov.nrel.nbc.spreadsheetadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheetadmin.dto.DataType;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 * 
 */
public class DataTypeDAOHibernate extends GenericHibernateDAO<DataType, Long>
		implements DataTypeDAO {


	private static final String DESCRIPTION = "description";
	private static final String ID = "id";

	/**
	 * Public method to find a DataType using the id.
	 * 
	 * @param id long - <DataType> ID
	 * @return <DataType>
	 */
	public DataType findById(long id) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataType.class)
			.add(Restrictions.eq(ID, id));
				
		return (DataType)crit.uniqueResult();
	} 

	/**
	 * Public method to find a DataType using the description.
	 * 
	 * @param desc <String> - <DataType> description
	 * @return <DataType>
	 */
	public DataType findByDescription(String desc) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataType.class)
			.add(Restrictions.eq(DESCRIPTION, desc));
				
		return (DataType)crit.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataType> findAll() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataType.class)
			.addOrder(Order.asc(ID));
				
		return (List<DataType>)crit.list();
	}
}
