package gov.nrel.nbc.spreadsheet.server;

import java.util.List;

/**
 * Public class to that is used to hold the workbook data,
 * including the workbook config and a list of sheets <SheetData>.
 * 
 * @author James Albersheim
 *
 */
public class WorkbookData {
	private long id;
	private String config;
	private long count;
	private List<SheetData> sheets;
	private String sheet;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the sheets
	 */
	public List<SheetData> getSheets() {
		return sheets;
	}
	/**
	 * @param sheets the sheets to set
	 */
	public void setSheets(List<SheetData> sheets) {
		this.sheets = sheets;
	}
	/**
	 * @param config the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}
	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(long count) {
		this.count = count;
	}
	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}
	
}
