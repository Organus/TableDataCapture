package gov.nrel.nbc.spreadsheet.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheet.dto.DataFormat;

public class DataFormatDAOHibernate extends
		GenericHibernateDAO<DataFormat, Long> implements DataFormatDAO {

	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param format String
	 * @return DataFormat
	 */
	public DataFormat findByName(String format) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataFormat.class)
			.add(Restrictions.eq("format", format));
				
		return (DataFormat)crit.uniqueResult();
	}
	
}
