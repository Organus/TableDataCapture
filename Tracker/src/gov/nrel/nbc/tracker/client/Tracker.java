package gov.nrel.nbc.tracker.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tracker implements EntryPoint {
	
	private static final String ROOT_PANEL_WIDTH = "100%";
	private static final String TRACKER_PANEL_WIDTH = "100%";
	private static final String TITLE_PANEL_WIDTH = "100%";
	private static final String HTML_SAMPLE_ID = "sampleContainer";
	private static final String HTML_TITLE_ID = "sampleContainer";

	private TrackerPanel tp = null;

	public void onModuleLoad() {
		TitlePanel title = new TitlePanel();
		RootPanel.get(HTML_TITLE_ID).add(title);
		title.setWidth(TITLE_PANEL_WIDTH);
		
		tp = new TrackerPanel();
		RootPanel.get(HTML_SAMPLE_ID).add(tp);
		RootPanel.get(HTML_SAMPLE_ID).setWidth(ROOT_PANEL_WIDTH);
		tp.setWidth(TRACKER_PANEL_WIDTH);
	}
}
