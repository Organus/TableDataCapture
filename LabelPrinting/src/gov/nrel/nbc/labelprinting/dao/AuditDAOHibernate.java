package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.model.Audit;

public class AuditDAOHibernate extends GenericHibernateDAO<Audit, Long>
		implements AuditDAO, AppConstants {

}
