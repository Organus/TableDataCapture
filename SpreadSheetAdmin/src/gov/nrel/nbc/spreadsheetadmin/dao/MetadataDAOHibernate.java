package gov.nrel.nbc.spreadsheetadmin.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheetadmin.dto.Metadata;
import gov.nrel.nbc.spreadsheetadmin.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.ValueData;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookData;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;

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
			.add(Restrictions.eq(HEADER, header));
		
		if (workbook != null)
			crit.add(Restrictions.eq(WORKBOOK, workbook));

		List<Metadata> mds = (List<Metadata>)crit.list();
		if (mds == null) return null;
		return mds.get(0);
	}
	/**
	 * Public method to find a <MetadataHeader> using its synonym.
	 * 
	 * @param id - <metadata>
	 * @return <Metadata> - corresponding object
	 */
	public Metadata findById(long id) {

		Session session = getSession();

		Criteria crit = session.createCriteria(Metadata.class)
			.add(Restrictions.eq("metadata_id", id));
		
		Metadata md = (Metadata)crit.uniqueResult();
		return md;
	}
	@SuppressWarnings("unchecked")
	public List<String> findDataByHeader(WorkbookConfig config, MetadataHeader header) {
		Session session = getSession();

		Criteria crit = session.createCriteria(Metadata.class)
				.add(Restrictions.eq(HEADER, header));

		crit.setProjection(Projections.distinct(Projections.property(VALUE)));

		List<ValueData> dataValues = (List<ValueData>) crit.list();

		List<String> values = new ArrayList<String>();
		Iterator<ValueData> it = dataValues.iterator();
		while (it.hasNext()) {
			Object o = it.next();
				ValueData sd = (ValueData)o;
				values.add(sd.getSvalue());
		}

		return values;
	}
}
