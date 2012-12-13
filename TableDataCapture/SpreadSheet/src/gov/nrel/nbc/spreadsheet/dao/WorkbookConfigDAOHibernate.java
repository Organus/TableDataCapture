package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookFileData;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class WorkbookConfigDAOHibernate extends
		GenericHibernateDAO<WorkbookConfig, Long> implements WorkbookConfigDAO {

	private static final String CONFIG_NAME = "config_name";
	private static final String SYNONYM = "synonym";

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllNames() {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookConfig.class)
				.setProjection(
						Projections.distinct(Projections.property(CONFIG_NAME)));

		return (List<String>) crit.list();
	}

	/**
	 * Public method to get a list of config synonyms.
	 * 
	 * @return <List<String>> - list of config synonyms values.
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllSynonyms() {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookConfig.class)
				.setProjection(
						Projections.distinct(Projections.property(SYNONYM)));

		return (List<String>) crit.list();
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public WorkbookConfig findByName(String name) {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookConfig.class)
			.add(Restrictions.eq(CONFIG_NAME, name));

		return (WorkbookConfig) crit.uniqueResult();
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public WorkbookConfig findBySynonym(String synonym) {

		Session session = getSession();

		Criteria crit = session.createCriteria(WorkbookConfig.class)
			.add(Restrictions.eq(SYNONYM, synonym));

		return (WorkbookConfig) crit.uniqueResult();
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public WorkbookConfig findBySheet(SheetConfig shConfig) {

		List<WorkbookConfig> wbcs = this.findAll();

		WorkbookConfig wbc = null;
		
		Iterator<WorkbookConfig> wit = wbcs.iterator();
		boolean found = false;
		while (wit.hasNext() && !found) {
			WorkbookConfig wbc1 = wit.next();
			Set<SheetConfig> sheetConfigs = wbc1.getSheetConfigs();
			Iterator<SheetConfig> sit = sheetConfigs.iterator();
			while (sit.hasNext()) {
				SheetConfig sc = sit.next();
				if (sc.getSheet_config_id()==shConfig.getSheet_config_id()) {
					found = true;
					wbc = wbc1;
					break;
				}
			}
		}
		return wbc;
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public WorkbookConfig findBySheetAndName(String shConfigName, String wbConfigName) {

		List<WorkbookConfig> wbcs = this.findAll();

		WorkbookConfig wbc = null;
		
		Iterator<WorkbookConfig> wit = wbcs.iterator();
		boolean found = false;
		while (wit.hasNext() && !found) {
			WorkbookConfig wbc1 = wit.next();
			if (wbc1.getConfig_name().equals(wbConfigName)) {
				Set<SheetConfig> sheetConfigs = wbc1.getSheetConfigs();
				Iterator<SheetConfig> sit = sheetConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig sc = sit.next();
					if (sc.getSheet_name().equals(shConfigName)) {
						found = true;
						wbc = wbc1;
						break;
					}
				}
				}
		}
		return wbc;
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public WorkbookConfig findBySheetAndSynonym(String shConfigName, String wbConfigName) {

		List<WorkbookConfig> wbcs = this.findAll();

		WorkbookConfig wbc = null;
		
		Iterator<WorkbookConfig> wit = wbcs.iterator();
		boolean found = false;
		while (wit.hasNext() && !found) {
			WorkbookConfig wbc1 = wit.next();
			if (wbc1.getSynonym().equals(wbConfigName)) {
				Set<SheetConfig> sheetConfigs = wbc1.getSheetConfigs();
				Iterator<SheetConfig> sit = sheetConfigs.iterator();
				while (sit.hasNext()) {
					SheetConfig sc = sit.next();
					if (sc.getSynonym().equals(shConfigName)) {
						found = true;
						wbc = wbc1;
						break;
					}
				}
				}
		}
		return wbc;
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public SheetConfig findSheetByConfigAndName(String wbConfig, String sheet) {

		WorkbookConfig wbc = this.findByName(wbConfig);
		SheetConfig shConfig = null;

		if (wbc != null) {
			Set<SheetConfig> sheets = wbc.getSheetConfigs();
			if (sheets != null) {
				Iterator<SheetConfig> sit = sheets.iterator();
				boolean found = false;
				while (sit.hasNext() && !found) {
					SheetConfig sh = sit.next();
					if (sh.getSheet_name().equals(sheet)) {
						found = true;
						shConfig = sh;
						break;
					}
				}
			}
		}
		return shConfig;
	}

	/**
	 * Public method to find attachments by file name.
	 * 
	 * @return <List<Attachments>>
	 */
	public WorkbookFileData findFile(String path) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(WorkbookFileData.class);
		
		crit.add(Restrictions.eq("path", path));
		
		return (WorkbookFileData)crit.uniqueResult();
			
	}

	/**
	 * Public method to find attachments by file name.
	 * 
	 * @return <List<Attachments>>
	 */
	@SuppressWarnings("unchecked")
	public WorkbookConfig findByFile(String path) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(WorkbookFileData.class);
		
		crit.add(Restrictions.eq("path", path));
		
		WorkbookFileData wfd = (WorkbookFileData)crit.uniqueResult();
			
		Criteria crit1 = session.createCriteria(WorkbookConfig.class);
		crit1.setFetchMode("workbook_file", FetchMode.JOIN);
		crit1.createCriteria("workbook_file")
	    	.add(Restrictions.eq("workbook_file_id", wfd.getWorkbook_file_id()));

		List<WorkbookConfig> configs = (List<WorkbookConfig>)crit1.list();
		if (configs == null) return null;
		return configs.get(0);
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public SheetConfig findSheetByConfigAndName(WorkbookConfig wbConfig, String sheet) {

		if (wbConfig == null) return null; 
		WorkbookConfig wbc = this.findByName(wbConfig.getConfig_name());
		SheetConfig shConfig = null;

		if (wbc != null) {
			Set<SheetConfig> sheets = wbc.getSheetConfigs();
			if (sheets != null) {
				Iterator<SheetConfig> sit = sheets.iterator();
				boolean found = false;
				while (sit.hasNext() && !found) {
					SheetConfig sh = sit.next();
					if (sh.getSheet_name().equals(sheet)) {
						found = true;
						shConfig = sh;
						break;
					}
				}
			}
		}
		return shConfig;
	}

	/**
	 * Public method to get a list of config.
	 * 
	 * @return <List<String>> - list of config values.
	 */
	public SheetConfig findSheetByConfigAndSynonym(WorkbookConfig wbConfig, String sheet) {

		if (wbConfig == null) return null; 
		WorkbookConfig wbc = this.findByName(wbConfig.getConfig_name());
		SheetConfig shConfig = null;

		if (wbc != null) {
			Set<SheetConfig> sheets = wbc.getSheetConfigs();
			if (sheets != null) {
				Iterator<SheetConfig> sit = sheets.iterator();
				boolean found = false;
				while (sit.hasNext() && !found) {
					SheetConfig sh = sit.next();
					if (sh.getSynonym().equals(sheet)) {
						found = true;
						shConfig = sh;
						break;
					}
				}
			}
		}
		return shConfig;
	}
}
