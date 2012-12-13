package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.model.Attachments;
import gov.nrel.nbc.labelprinting.model.Sample;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class AttachmentsDAOHibernate extends GenericHibernateDAO<Attachments, Long>
		implements AttachmentsDAO {

	private static final String FILENAME = "filename";
	private static final String SAMPLE = "sample";
	private static final String ATTACHMENT_ID = "attachment_id";

	@SuppressWarnings("unchecked")
	public List<Attachments> findByFilename(String filename) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(FILENAME, filename));
		
		return (List<Attachments>)crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Attachments> findBySample(Sample sample) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(SAMPLE, sample));
		
		return (List<Attachments>)crit.list();
	}

	public Attachments findById(long id) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Attachments.class)
			.add(Restrictions.eq(ATTACHMENT_ID, id));
		
		return (Attachments)crit.uniqueResult();
	}
}
