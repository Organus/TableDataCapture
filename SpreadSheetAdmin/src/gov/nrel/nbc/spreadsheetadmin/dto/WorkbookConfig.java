package gov.nrel.nbc.spreadsheetadmin.dto;

import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookFileData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WorkbookConfig {
	private String config_name;
	private String synonym;
	private long workbook_config_id;
	private Set<SheetConfig> metadata_sheet = new HashSet<SheetConfig> ();
	private Set<WorkbookFileData> workbook_file = new HashSet<WorkbookFileData> ();
	private Set<SheetConfig> sheetConfigs = new HashSet<SheetConfig> ();
	private Set<String> table_names = new HashSet<String>();

	/**
	 * @return the metadata_sheet
	 */
	public Set<SheetConfig> getMetadata_sheet() {
		return metadata_sheet;
	}
	/**
	 * @param metadata_sheet the metadata_sheet to set
	 */
	public void setMetadata_sheet(Set<SheetConfig> metadata_sheet) {
		this.metadata_sheet = metadata_sheet;
	}
	/**
	 * @return the workbook_file
	 */
	public Set<WorkbookFileData> getWorkbook_file() {
		return workbook_file;
	}
	/**
	 * @param workbook_file the workbook_file to set
	 */
	public void setWorkbook_file(Set<WorkbookFileData> workbook_file) {
		this.workbook_file = workbook_file;
	}
	/**
	 * @param config_name the config_name to set
	 */
	public void setConfig_name(String config_name) {
		this.config_name = config_name;
	}
	/**
	 * @return the config_name
	 */
	public String getConfig_name() {
		return config_name;
	}
	/**
	 * @param workbook_config_id the workbook_config_id to set
	 */
	public void setWorkbook_config_id(long workbook_config_id) {
		this.workbook_config_id = workbook_config_id;
	}
	/**
	 * @return the workbook_config_id
	 */
	public long getWorkbook_config_id() {
		return workbook_config_id;
	}
	/**
	 * @param sheetConfigs the sheetConfigs to set
	 */
	public void setSheetConfigs(Set<SheetConfig> sheetConfigs) {
		this.sheetConfigs = sheetConfigs;
	}
	/**
	 * @return the sheetConfigs
	 */
	public Set<SheetConfig> getSheetConfigs() {
		return sheetConfigs;
	}
	public void setTable_names(Set<String> table_names) {
		String wbName = this.config_name;
		Iterator<SheetConfig> sit = sheetConfigs.iterator();
		while(sit.hasNext()){
			SheetConfig sc = sit.next();
			String scName = sc.getSheet_name();
			String table_name = wbName + "_" + scName + "_cell_data";
			this.table_names.add(table_name);
		}
	}
	
	public Set<String> getTable_names() {
		return table_names;
	}
	/**
	 * @param synonym the synonym to set
	 */
	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}
	/**
	 * @return the synonym
	 */
	public String getSynonym() {
		return synonym;
	}
	
}
