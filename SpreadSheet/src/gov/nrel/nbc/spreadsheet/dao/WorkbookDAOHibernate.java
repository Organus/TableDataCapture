package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.AttachmentType;
import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookFileData;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class WorkbookDAOHibernate extends GenericHibernateDAO<WorkbookData, Long>
		implements WorkbookDAO {

	private static final String CONFIG = "workbook_config_id";
	private static final String FILE_ID = "workbook_file_id";

	public int getNumberOfAttachments(long id) {
		int num = 0;

		WorkbookData workbook = this.findById(id, false);
		Set<Attachments> attachments = workbook.getAttachments();
		if (attachments != null) 
			num = attachments.size();
		
		return num;
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	@SuppressWarnings("unchecked")
	public List<WorkbookData> findByAttachmentExtension(AttachmentType type) {

		Session session = getSession();
		//List<WorkbookData> workbooks = new ArrayList<WorkbookData>();

		Criteria crit = session.createCriteria(WorkbookData.class)
					.setFetchMode("attachments", FetchMode.JOIN);
		crit.createCriteria("attachments")
			.add(Restrictions.eq("type_id", type));
//				.setProjection(Projections.distinct(Projections.property("attachments")))
	//			.add(Restrictions.eq("type_id", type));

		return (List<WorkbookData>) crit.list();
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	@SuppressWarnings("unchecked")
	public List<WorkbookData> findByConfig(WorkbookConfig config) {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookData.class)
			.add(Restrictions.eq(CONFIG, config));

		return (List<WorkbookData>) crit.list();
	}

	/**
	 * Public method to find by file id.
	 * 
	 * @return WorkbookData - The wookbookdata.
	 */
	public WorkbookData findByFileId(WorkbookFileData fileId) {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookData.class)
			.add(Restrictions.eq(FILE_ID, fileId));

		return (WorkbookData) crit.uniqueResult();
	}

}
