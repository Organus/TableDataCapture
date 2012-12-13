package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.CellData;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.RowData;
import gov.nrel.nbc.spreadsheet.dto.ValueData;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 *
 */
public class CellDataDAOHibernate extends GenericHibernateDAO<CellData, Long>
		implements CellDataDAO {

	private static final String VALUEID = "valueId";
	private static final String ROWID = "rowId";
	private static final String HEADER = "cell_hdr_id";

	/**
	 * Public method to get the value for id.
	 * 
	 * @param id - <Generica> to get data.
	 * 
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<CellData> findByRowId(RowData rd) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(ROWID, rd));


		return (List<CellData>)crit.list();
	}
	/**
	 * Public method to get the value for id.
	 * 
	 * @param id - <Generica> to get data.
	 * 
	 * @return List<String> 
	 */
	public ValueData findDataByDataId(ValueData gd) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(VALUEID, gd));


		return (ValueData)crit.uniqueResult();
	}
	/**
	 * Public method to get the values for <CellDataHeader>s.
	 * 
	 * @param header - <CellDataHeader> to get data.
	 * 
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<String> findStringDataByHeader(CellDataHeader header) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUEID)));

		List<ValueData> ids = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = ids.iterator();//dataValues.iterator();
		while (it.hasNext()) { 
			ValueData o = it.next();
			values.add(o.getSvalue());
		}

		return values;
	}
	/**
	 * Public method to get the values for <CellDataHeader>s.
	 * 
	 * @param header - <CellDataHeader> to get data.
	 * 
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<String> findRealDataByHeader(CellDataHeader header) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUEID)));

		List<ValueData> ids = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = ids.iterator();//dataValues.iterator();
		while (it.hasNext()) { 
			ValueData o = it.next();
			values.add(String.valueOf(o.getRvalue()));
		}

		return values;
	}
	/**
	 * Public method to get the values for <CellDataHeader>s.
	 * 
	 * @param header - <CellDataHeader> to get data.
	 * 
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<String> findDateDataByHeader(CellDataHeader header) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUEID)));

		List<ValueData> ids = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = ids.iterator();//dataValues.iterator();
		while (it.hasNext()) { 
			ValueData o = it.next();
			//SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dvalue = o.getDvalue();
			if (dvalue != null) {
				Date collectionTime = dvalue;
				GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
				cal.setTime(collectionTime);
				String value = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
				//System.out.println("date = "+value);
				values.add(value);
			}
		}

		return values;
	}
	/**
	 * Public method to get the values for <CellDataHeader>s.
	 * 
	 * @param header - <CellDataHeader> to get data.
	 * 
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<String> findLongDataByHeader(CellDataHeader header) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellData.class).add(
				Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUEID)));

		List<ValueData> ids = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = ids.iterator();//dataValues.iterator();
		while (it.hasNext()) { 
			ValueData o = it.next();
			values.add(String.valueOf(o.getLvalue()));
		}

		return values;
	}
}
