package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 *
 */
public class AttachmentsDAOHibernate extends GenericHibernateDAO<Attachments, Long>
		implements AttachmentsDAO {
	
	// Constants

	private static final String FILENAME = "filename";
	private static final String ATTACHMENT_ID = "attachment_id";
	private static final String WORKBOOK = "workbook";

	@SuppressWarnings("unchecked")
	/**
	 * Public method to find attachments by file name.
	 * 
	 * @return <List<Attachments>>
	 */
	public List<Attachments> findByFilename(String filename) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(FILENAME, filename));
		
		return (List<Attachments>)crit.list();
	}

	/**
	 * Public method to find attachments by workbook.
	 * 
	 * @param workbook <WorkbookData>
	 * 
	 * @return <List<Attachments>>
	 */
	@SuppressWarnings("unchecked")
	public List<Attachments> findByWorkbook(WorkbookData workbook) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(WORKBOOK, workbook));
		
		return (List<Attachments>)crit.list();
	}

	/**
	 * Public method to find an attachment by its ID.
	 * 
	 * @param id - attachment ID
	 * 
	 * @return <Attachments>
	 * 
	 */
	public Attachments findById(long id) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(ATTACHMENT_ID, id));
		
		return (Attachments)crit.uniqueResult();
	}
	
	/**
	 * Public method to find the last attachment
	 * 
	 * @return <Attachments>
	 */
	public Attachments findLatest() {
		Attachments attachment = new Attachments();
		List<Attachments> attachments = this.findAll();
		Iterator<Attachments> ait = attachments.iterator();
		while (ait.hasNext()) {
			Attachments attach = ait.next();
			if (attach == null)
				attachment = attach;
			else if (attachment.getAttachment_id() < attach.getAttachment_id())
				attachment = attach;
		}
		return attachment;
	}
}
