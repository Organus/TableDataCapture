package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class SheetDAOHibernate extends GenericHibernateDAO<SheetConfig, Long>
		implements SheetDAO {

	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param name String
	 * @return CellDataHeader
	 */
	public CellDataHeader findByNameAndConfig(WorkbookConfig config, String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq("sheet", name));
				
		return (CellDataHeader)crit.uniqueResult();
	}
	
	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param name String
	 * @return CellDataHeader
	 */
	@SuppressWarnings("unchecked")
	public List<CellDataHeader> findBySheetName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq("sheet", name));
				
		return (List<CellDataHeader>)crit.list();
	}
	
	/**
	 * Public method to return a list of CellDataHeader given the sheet.
	 * Could return null.
	 * 
	 * @param sheet <SheetConfig>
	 * @return <List<CellDataHeader>>
	 */
	@SuppressWarnings("unchecked")
	public List<CellDataHeader> findBySheet(SheetConfig sheet) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(SheetConfig.class)
			.createAlias("cell_hdrs", "ch")
			.add(Restrictions.eq("sheet_config_id", sheet.getSheet_config_id()));
				
		SheetConfig shConfig = (SheetConfig)crit.uniqueResult();
		if (shConfig != null) {

			Set<CellDataHeader> cellHeaders = (Set<CellDataHeader>)shConfig.getCell_hdrs();
		
			if (cellHeaders != null) {
				List<Long> idList = new ArrayList<Long>();
				if (cellHeaders != null && !cellHeaders.isEmpty()) {
					Iterator<CellDataHeader> it = cellHeaders.iterator();
					while (it.hasNext()) {
						CellDataHeader cellHeader = it.next();
						idList.add(cellHeader.getCell_hdr_id());
					}
				
					Criteria crit2 = session.createCriteria(CellDataHeader.class)
						.add(Restrictions.in("cell_hdr_id", idList))
						.addOrder(Order.asc("hdr_index"));
					
					return (List<CellDataHeader>)crit2.list();
				}
			}
		}
		return null;
	}
	
}
