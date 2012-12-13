package gov.nrel.nbc.spreadsheetadmin.dao;

import java.util.List;

import gov.nrel.nbc.spreadsheetadmin.dto.RowData;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetData;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 * 
 */
public class RowDataDAOHibernate extends GenericHibernateDAO<RowData, Long>
		implements RowDataDAO {

	private static final String SHEET_DATA_ID = "sheet_data_id";
	private static final String ROW_NUM = "rowNum";

	/**
	 * Public method to find a <RowData> object using the row number and the
	 * <SheetData>.
	 * 
	 * @param sheetRow - int index number of row in spreadsheet
	 * @param sheet - <SheetData> spreadsheet object.
	 * @return <RowData> - unique row
	 */
	public RowData findByRowNumAndSheet(int sheetRow, SheetData sheet) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(RowData.class)
			.add(Restrictions
				.and(Restrictions.eq(ROW_NUM, sheetRow),
						Restrictions.eq(SHEET_DATA_ID, sheet)));
				
		return (RowData)crit.uniqueResult();
	}
	/**
	 * Public method to find the <RowData> objects using the <SheetData>.
	 * 
	 * @param sheet - <SheetData> spreadsheet object.
	 * @return <RowData> - list of rows
	 */
	@SuppressWarnings("unchecked")
	public List<RowData> findBySheet(SheetData sheet) {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(RowData.class)
			.add(Restrictions.eq(SHEET_DATA_ID, sheet));
				
		return (List<RowData>)crit.list();
	}
}
