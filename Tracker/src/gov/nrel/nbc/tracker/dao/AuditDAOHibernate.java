package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.client.AppConstants;
import gov.nrel.nbc.tracker.model.Audit;

public class AuditDAOHibernate extends GenericHibernateDAO<Audit, Long>
		implements AuditDAO, AppConstants {

}
