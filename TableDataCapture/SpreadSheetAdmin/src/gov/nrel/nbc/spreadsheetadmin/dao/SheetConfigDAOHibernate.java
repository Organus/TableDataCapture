package gov.nrel.nbc.spreadsheetadmin.dao;

import org.hibernate.criterion.Projections;

import gov.nrel.nbc.spreadsheetadmin.dto.SheetConfig;

public class SheetConfigDAOHibernate extends
		GenericHibernateDAO<SheetConfig, Long> implements SheetConfigDAO {

    public long getNextId(){
    	long id = 0;

    	Long l = (Long) getSession().createCriteria( getPersistentClass() ).setProjection( Projections.max( "sheet_config_id" )).uniqueResult();
    	if (l != null) id = l.longValue();
    	return id+1;
    }
}
