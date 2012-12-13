package gov.nrel.nbc.spreadsheetadmin.dao;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.dto.DataFormat;
import gov.nrel.nbc.spreadsheetadmin.dto.DataType;
import gov.nrel.nbc.spreadsheetadmin.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;

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
public class MetadataHeaderDAOHibernate extends
		GenericHibernateDAO<MetadataHeader, Long> implements MetadataHeaderDAO, AppConstants {
	
	private static final String ORDER = "hdr_order";
	private static final String CONFIG_ID = "workbook_config_id";
	private static final String NAME = "name";
	private static final String SYNONYM = "synonym";
	// Constants
	private static final String TYPE_ID = "typeId";
	private static final String DATA_FORMAT = "data_format";

	/**
	 * Public method to get a <DataType> using a <MetadataHeader> synonym.
	 * 
	 * @param name <String> - unique value
	 * @return <DataType> - corresponding DataType
	 */
	public DataType getTypeByName(String name) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class).add(
				Restrictions.eq(NAME, name)).setProjection(
				Projections.distinct(Projections.property(TYPE_ID)));

		return (DataType) crit.uniqueResult();
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

		Criteria crit = session.createCriteria(MetadataHeader.class).add(
				Restrictions.eq(NAME, name)).setProjection(
				Projections.distinct(Projections.property(DATA_FORMAT)));

		List<DataFormat> list = (List<DataFormat>) crit.list();
		if (list != null && list.size()>0) return list.get(0);
		return null;
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
				.addOrder(Order.asc(ORDER));

		return (List<DataFormat>) crit.list();
	}

	/**
	 * Public method to get a list of synonyms.
	 * 
	 * @return <List<String>> - list of synonym values.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
				.setProjection(
						Projections.distinct(Projections.property(NAME)));

		return (List<String>) crit.list();
	}

	/**
	 * Public method to get a list of synonyms.
	 * 
	 * @return <List<String>> - list of synonym values.
	 */
	@SuppressWarnings("unchecked")
	public List<MetadataHeader> findByConfig(WorkbookConfig book_config_id) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
				.add(Restrictions.eq(CONFIG_ID, book_config_id))
				.addOrder(Order.asc(ORDER));

		return (List<MetadataHeader>) crit.list();
	}

	/**
	 * Public method to find a <MetadataHeader> using its synonym.
	 * 
	 * @param selection - <String>  - synonym
	 * @return <MetadataHeader> - corresponding object
	 */
	public MetadataHeader findByName(String selection, WorkbookConfig config) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
				.add(Restrictions.eq(NAME, selection))
				.add(Restrictions.eq(CONFIG_ID, config))
				;

		return (MetadataHeader) crit.uniqueResult();
	}

	/**
	 * Public method to return a DataType given a CellDataHeader name.
	 * 
	 * @param name String
	 * @return DataType
	 */
	@SuppressWarnings("unchecked")
	public List<WorkbookConfig> getConfigsByHeader(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(MetadataHeader.class)
			.add(Restrictions.eq(NAME, name))
			.setProjection(Projections.distinct(Projections.property(CONFIG_ID)));
				
		return (List<WorkbookConfig>)crit.list();
	}

	/**
	 * Public method to return a DataType given a CellDataHeader name.
	 * 
	 * @param shConfig <SheetConfig>
	 * @return <List<MetadataHeader>>
	 */
	public List<MetadataHeader> getHeadersBySheetConfig(SheetConfig shConfig) {

		Session session = getSession();
		
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		
		WorkbookConfig wbConfig = wcdh.findBySheet(shConfig);
		
		return findByConfig(wbConfig);
	}

	/**
	 * Public method to return a DataType given a CellDataHeader name.
	 * 
	 * @param name String
	 * @return DataType
	 */
	public MetadataHeader getHeaderBySheetConfigAndName(SheetConfig shConfig, String name) {

		Session session = getSession();
		
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		
		WorkbookConfig wbConfig = wcdh.findBySheet(shConfig);
		
		return findByNameAndConfig(wbConfig, name);
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
		
		Criteria crit = session.createCriteria(MetadataHeader.class)
			.add(Restrictions.eq(NAME, name))
			.setProjection(Projections.distinct(Projections.property(CONFIG_ID)));
				
		List<SheetConfig> shConfigs = new ArrayList<SheetConfig>();
		List<WorkbookConfig> wbConfigs = (List<WorkbookConfig>)crit.list();
		Iterator<WorkbookConfig> wit = wbConfigs.iterator();
		while (wit.hasNext()) {
			WorkbookConfig wbConfig = wit.next();
			shConfigs.addAll(wbConfig.getSheetConfigs());
		}
		return shConfigs;
	}

	/**
	 * Public method to find a <MetadataHeader> using its name.
	 * 
	 * @param selection - <String>  - name
	 * @return <MetadataHeader> - corresponding object
	 */
	@SuppressWarnings("unchecked")
	public List<MetadataHeader> findByName(String selection) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
			.add(Restrictions.eq(NAME, selection));
		return (List<MetadataHeader>) crit.list();
	}

	/**
	 * Public method to find a <MetadataHeader> using its name.
	 * 
	 * @param selection - <String>  - name
	 * @return <MetadataHeader> - corresponding object
	 */
	@SuppressWarnings("unchecked")
	public MetadataHeader findByNameAndConfig(WorkbookConfig config, String selection) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
			.add(Restrictions.eq(NAME, selection));
		if (config != null)
			crit.add(Restrictions.eq(CONFIG_ID, config));
		List<MetadataHeader> list = (List<MetadataHeader>) crit.list();
		if (list != null && list.size()>0)
			return (MetadataHeader) list.get(0);
		return null;
	}

	/**
	 * Public method to find a <MetadataHeader> using its synonym.
	 * 
	 * @param selection - <String>  - synonym
	 * @return <MetadataHeader> - corresponding object
	 */
	public MetadataHeader findBySynonymAndConfig(WorkbookConfig config, String selection) {

		Session session = getSession();

		Criteria crit = session.createCriteria(MetadataHeader.class)
			.add(Restrictions.eq(SYNONYM, selection));
		if (config != null)
			crit.add(Restrictions.eq(CONFIG_ID, config));
		return (MetadataHeader) crit.uniqueResult();
	}
}
