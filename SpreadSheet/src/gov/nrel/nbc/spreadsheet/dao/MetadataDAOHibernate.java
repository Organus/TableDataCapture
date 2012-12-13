package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.Metadata;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.ValueData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class MetadataDAOHibernate extends GenericHibernateDAO<Metadata, Long>
		implements MetadataDAO {

	private static final String WORKBOOK = "workbook";
	private static final String HEADER = "metadata_hdr_id";
	private static final String VALUE = "value";
	/**
	 * Public method to find a <MetadataHeader> using its synonym.
	 * 
	 * @param header - <MetadataHeader>
	 * @param workbook - <WorkbookData>
	 * @return <Metadata> - corresponding object
	 */
	@SuppressWarnings("unchecked")
	public Metadata findByHeader(MetadataHeader header, WorkbookData workbook) {

		Session session = getSession();

		Criteria crit = session.createCriteria(Metadata.class)
			.add(Restrictions.eq(HEADER, header))
			.add(Restrictions.eq(WORKBOOK, workbook));

		List<Metadata> mds = (List<Metadata>)crit.list();
		if (mds == null) return null;
		if (mds.size()>0) return mds.get(0);
		return null;
	}
	/**
	 * Public method to find a <MetadataHeader> using its synonym.
	 * 
	 * @param header - <MetadataHeader>
	 * @param workbook - <WorkbookData>
	 * @return <Metadata> - corresponding object
	 */
	@SuppressWarnings("unchecked")
	public List<Metadata> findByWorkbook(WorkbookData workbook) {

		Session session = getSession();

		Criteria crit = session.createCriteria(Metadata.class)
			.add(Restrictions.eq(WORKBOOK, workbook));

		List<Metadata> mds = (List<Metadata>)crit.list();
		return mds;
	}
	@SuppressWarnings("unchecked")
	public List<String> findDataByHeader(WorkbookConfig config, MetadataHeader header) {
		Session session = getSession();

		String type = header.getTypeId().getDescription();
		
		Criteria crit = session.createCriteria(Metadata.class)
				.add(Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUE)));

		List<ValueData> dataValues = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = dataValues.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			ValueData vd = (ValueData)o;
			if (type.equals("STRING"))
			{
				values.add(vd.getSvalue());
			}
			else if (type.equals("LONG"))
			{
				values.add(String.valueOf(vd.getLvalue()));
			}
			else if (type.equals("REAL"))
			{
				values.add(String.valueOf(vd.getRvalue()));
			}
			else if (type.equals("DATE"))
			{
				values.add(String.valueOf(vd.getDvalue()));
			}
		}

		return values;
	}
	@SuppressWarnings("unchecked")
	public List<ValueData> findData(MetadataHeader header) {
		Session session = getSession();

		Criteria crit = session.createCriteria(Metadata.class)
				.add(Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUE)));

		List<ValueData> dataValues = (List<ValueData>) crit.list();

		return dataValues;
	}
}
