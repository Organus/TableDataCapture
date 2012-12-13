package gov.nrel.nbc.tracker.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nrel.nbc.tracker.model.AttachmentType;

public class AttachmentTypeDAOHibernate extends
		GenericHibernateDAO<AttachmentType, Long> implements AttachmentTypeDAO {

	/**
	 * Public method to find attachments by file name.
	 * 
	 * @return <List<Attachments>>
	 */
	public AttachmentType findByExtension(String ext) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(AttachmentType.class)
						.add(Restrictions.eq("ext", ext));

		return (AttachmentType)crit.uniqueResult();
	}

	/**
	 * Public method to find attachments by file name.
	 * 
	 * @return <List<Attachments>>
	 */
	@SuppressWarnings("unchecked")
	public List<String> findExtensions() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(AttachmentType.class).setProjection(
				Projections.distinct(Projections.property("ext")));

		return (List<String>)crit.list();
	}

}
