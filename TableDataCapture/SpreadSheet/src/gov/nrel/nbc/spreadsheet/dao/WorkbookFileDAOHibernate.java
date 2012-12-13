package gov.nrel.nbc.spreadsheet.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.spreadsheet.dto.WorkbookFileData;

public class WorkbookFileDAOHibernate extends
		GenericHibernateDAO<WorkbookFileData, Long> implements WorkbookFileDAO {

	private static final String PATH = "path";
	private static final String NAME = "filename";
	private static final String WORKBOOK_FILE_ID = "workbook_file_id";

	public String findPathByID(long lid) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(WorkbookFileData.class)
			.add(Restrictions.eq(WORKBOOK_FILE_ID, lid))
			.setProjection(Projections.property(PATH));
				
		return (String)crit.uniqueResult();
	}
	public WorkbookFileData findByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(WorkbookFileData.class)
			.add(Restrictions.eq(NAME, name));
				
		return (WorkbookFileData)crit.uniqueResult();
	}
}
