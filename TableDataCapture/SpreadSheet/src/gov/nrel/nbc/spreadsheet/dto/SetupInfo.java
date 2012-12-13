package gov.nrel.nbc.spreadsheet.dto;

import java.util.HashSet;
import java.util.Set;

public class SetupInfo {
	private long setup_id;
	private String title;
	private boolean displayUrls;
	private Set<SetupUrls> urls = new HashSet<SetupUrls> ();
	/**
	 * @return the setup_id
	 */
	public long getSetup_id() {
		return setup_id;
	}
	/**
	 * @param setup_id the setup_id to set
	 */
	public void setSetup_id(long setup_id) {
		this.setup_id = setup_id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the urls
	 */
	public Set<SetupUrls> getUrls() {
		return urls;
	}
	/**
	 * @param urls the urls to set
	 */
	public void setUrls(Set<SetupUrls> urls) {
		this.urls = urls;
	}
	/**
	 * @param displayUrls the displayUrls to set
	 */
	public void setDisplayUrls(boolean displayUrls) {
		this.displayUrls = displayUrls;
	}
	/**
	 * @return the displayUrls
	 */
	public boolean isDisplayUrls() {
		return displayUrls;
	}
}
