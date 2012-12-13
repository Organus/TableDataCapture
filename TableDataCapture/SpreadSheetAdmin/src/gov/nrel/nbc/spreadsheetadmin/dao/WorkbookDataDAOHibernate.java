package gov.nrel.nbc.spreadsheetadmin.dao;

import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookData;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookFileData;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class WorkbookDataDAOHibernate extends GenericHibernateDAO<WorkbookData, Long>
		implements WorkbookDataDAO {

	private static final String WORKBOOK_FILE_ID = "workbook_file_id";
	private static final String PATH = "path";
	private static final String CONFIG = "workbook_config_id";

	/**
	 * Public method to find a WorkbookData by file path.
	 * 
	 * @param path String
	 * @return WorkbookData
	 */
	public WorkbookData findByFile(String path) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(WorkbookFileData.class);
		
		crit.add(Restrictions.eq(PATH, path));
		
		WorkbookFileData wfd = (WorkbookFileData)crit.uniqueResult();
			
		Criteria crit1 = session.createCriteria(WorkbookData.class)
					.add(Restrictions.eq(WORKBOOK_FILE_ID, wfd));

		return (WorkbookData)crit1.uniqueResult();
	}

	/**
	 * Public method to get a list of WorkbookData.
	 * 
	 * @param config WorkbookConfig
	 * @return List<WorkbookData>
	 */
	@SuppressWarnings("unchecked")
	public List<WorkbookData> findByConfig(WorkbookConfig config) {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookData.class)
			.add(Restrictions.eq(CONFIG, config));

		return (List<WorkbookData>) crit.list();
	}
}
