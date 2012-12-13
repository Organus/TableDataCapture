package gov.nrel.nbc.spreadsheet.dto;

public class SetupUrls {
	private long url_id;
	private String name;
	private String url;
	private SetupInfo setup_id;
	/**
	 * @return the url_id
	 */
	public long getUrl_id() {
		return url_id;
	}
	/**
	 * @param url_id the url_id to set
	 */
	public void setUrl_id(long url_id) {
		this.url_id = url_id;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the setup_id
	 */
	public SetupInfo getSetup_id() {
		return setup_id;
	}
	/**
	 * @param setup_id the setup_id to set
	 */
	public void setSetup_id(SetupInfo setup_id) {
		this.setup_id = setup_id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
