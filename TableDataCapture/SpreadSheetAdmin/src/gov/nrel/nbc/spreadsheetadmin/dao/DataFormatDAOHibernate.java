package gov.nrel.nbc.spreadsheetadmin.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheetadmin.dto.DataFormat;

public class DataFormatDAOHibernate extends
		GenericHibernateDAO<DataFormat, Long> implements DataFormatDAO {

	/**
	 * Public method to return a DataFormat given the format.
	 * Could return null.
	 * 
	 * @param format String
	 * @return DataFormat
	 */
	public DataFormat findByFormat(String format) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataFormat.class)
			.add(Restrictions.eq("format", format));
				
		return (DataFormat)crit.uniqueResult();
	}
	
	/**
	 * Public method to return a CellDataHeader given the description.
	 * 
	 * @param desc String Description
	 * @return DataFormat
	 */
	public DataFormat findByDesc(String desc) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(DataFormat.class)
			.add(Restrictions.eq("description", desc));
				
		return (DataFormat)crit.uniqueResult();
	}
}
