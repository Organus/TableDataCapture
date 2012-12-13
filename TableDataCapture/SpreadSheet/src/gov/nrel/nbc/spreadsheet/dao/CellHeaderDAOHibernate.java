package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataFormat;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author James Albersheim
 * 
 */
public class CellHeaderDAOHibernate extends GenericHibernateDAO<CellDataHeader, Long> implements
		CellHeaderDAO {

	private static final String INDEX = "hdr_index";
	private static final String TYPE_ID = "typeId";
	private static final String NAME = "name";
	private static final String SYNONYM = "synonym";
	private static final String CONFIG_ID = "sheet_config_id";
	private static final String DATA_FORMAT = "data_format";

	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param name String
	 * @return CellDataHeader
	 */
	public CellDataHeader findByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(NAME, name));
				
		return (CellDataHeader)crit.uniqueResult();
	}
	
	/**
	 * Public method to return a DataType given a CellDataHeader name.
	 * 
	 * @param name String
	 * @return DataType
	 */
	@SuppressWarnings("unchecked")
	public List<SheetConfig> getSheetConfigsByHeader(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(NAME, name))
			.setProjection(Projections.distinct(Projections.property(CONFIG_ID)));
				
		return (List<SheetConfig>)crit.list();
	}

	/**
	 * Public method to return a DataType given a CalcTag name.
	 * 
	 * @param name String
	 * @return DataType
	 */
	@SuppressWarnings("unchecked")
	public List<WorkbookConfig> getWorkbookConfigsByHeader(String name) {

		Session session = getSession();
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(NAME, name))
			.setProjection(Projections.distinct(Projections.property(CONFIG_ID)));
				
		List<SheetConfig> sheetConfigs = crit.list();
		List<WorkbookConfig> workbookConfigs = new ArrayList<WorkbookConfig>();
		Iterator<SheetConfig> sit = sheetConfigs.iterator();
		while (sit.hasNext()) {
			SheetConfig sc = sit.next();
			WorkbookConfig wbc = wcdh.findBySheet(sc);
			workbookConfigs.add(wbc);
		}
		return workbookConfigs;
	}

	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param name String
	 * @return CellDataHeader
	 */
	public CellDataHeader findByNameAndConfig(SheetConfig config, String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(NAME, name));
		if (config != null)
			crit.add(Restrictions.eq(CONFIG_ID, config));
				
		return (CellDataHeader)crit.uniqueResult();
	}
	
	/**
	 * Public method to return a CellDataHeader given the name.
	 * Could return null.
	 * 
	 * @param config SheetConfig
	 * @return List<CellDataHeader>
	 */
	@SuppressWarnings("unchecked")
	public List<CellDataHeader> findByConfig(SheetConfig config) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class);
		if (config != null)
			crit.add(Restrictions.eq(CONFIG_ID, config))
				.addOrder(Order.asc(INDEX));
				
		return (List<CellDataHeader>)crit.list();
	}
	
	/**
	 * Public method to get a <DataFormat> using a <MetadataHeader> synonym.
	 * 
	 * @param name <String> - unique value
	 * @return <DataFormat> - corresponding DataFormat
	 */
	@SuppressWarnings("unchecked")
	public DataFormat getFormatByName(String name) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellDataHeader.class).add(
				Restrictions.eq(NAME, name)).setProjection(
				Projections.distinct(Projections.property(DATA_FORMAT)));

		List<DataFormat> list = (List<DataFormat>) crit.list();
		if (list.size()>0) return list.get(0);
		return null;
	}

	/**
	 * Public method to get a <DataFormat> using a <MetadataHeader> synonym.
	 * 
	 * @param name <String> - unique value
	 * @return <DataFormat> - corresponding DataFormat
	 */
	public DataFormat getFormatByNameAndSheet(String name, SheetConfig config) {

		Session session = getSession();

		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(CONFIG_ID, config))
			.add(Restrictions.eq(NAME, name)).setProjection(
				Projections.distinct(Projections.property(DATA_FORMAT)));

		return (DataFormat) crit.uniqueResult();
	}

	/**
	 * Public method to get a <DataFormat> using a <MetadataHeader> synonym.
	 * 
	 * @param name <String> - unique value
	 * @return <DataFormat> - corresponding DataFormat
	 */
	@SuppressWarnings("unchecked")
	public List<DataFormat> getFormats() {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class).setProjection(
				Projections.distinct(Projections.property(DATA_FORMAT)))
				.addOrder(Order.asc(INDEX));

		return (List<DataFormat>) crit.list();
	}

	/**
	 * Public method to return a DataType given a CellDataHeader name.
	 * 
	 * @param name String
	 * @return DataType
	 */
	@SuppressWarnings("unchecked")
	public DataType getTypeByName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(NAME, name))
			.setProjection(Projections.distinct(Projections.property(TYPE_ID)));
				
		List<DataType> types = (List<DataType>)crit.list();
		if (types != null && types.size()>0)
			return (DataType)types.get(0);
		else
			return null;
	}

	/**
	 * Public method to return a DataType given a CellDataHeader synonym.
	 * 
	 * @param synonym String
	 * @return DataType
	 */
	@SuppressWarnings("unchecked")
	public DataType getTypeBySynonym(String synonym) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(SYNONYM, synonym))
			.setProjection(Projections.distinct(Projections.property(TYPE_ID)));
				
		List<DataType> types = (List<DataType>)crit.list();
		if (types != null && types.size()>0)
			return (DataType)types.get(0);
		else
			return null;
	}

	/**
	 * Public method to return all tag names.
	 * 
	 * @return List<String>
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.setProjection(Projections.distinct(Projections.property(NAME)))
			.addOrder(Order.asc(INDEX));
				
		return (List<String>)crit.list();
	}

	/**
	 * Public method to retrieve the next index value.
	 * 
	 * @return int index value
	 */
	public int getNextIndexValue() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.setProjection(Projections.max(INDEX));
				
		Integer maxIndex = (Integer)crit.uniqueResult();
		
		if (maxIndex != null)
			return maxIndex.intValue() + 1;
		else
			return 1;
	}

}
