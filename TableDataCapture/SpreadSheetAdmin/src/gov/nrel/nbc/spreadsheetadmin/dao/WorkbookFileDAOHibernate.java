package gov.nrel.nbc.spreadsheetadmin.dao;

import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookFileData;

import org.hibernate.criterion.Projections;

public class WorkbookFileDAOHibernate extends
		GenericHibernateDAO<WorkbookFileData, Long> implements WorkbookFileDAO {
    public long getNextId(){
    	long id = 0;

    	Long l = (Long) getSession().createCriteria( getPersistentClass() ).setProjection( Projections.max( "workbook_file_id" )).uniqueResult();
    	if (l != null) id = l.longValue();
    	return id+1;
    }
}
