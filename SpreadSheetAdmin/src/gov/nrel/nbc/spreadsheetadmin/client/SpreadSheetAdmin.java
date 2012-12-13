package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SpreadSheetAdmin implements EntryPoint {
	
	private static final String PERCENT_100 = "100%";
	private static final String HTML_SAMPLE_ID = "sheetContainer";
	private static final String HTML_TITLE_ID = "sheetContainer";
	
	public void onModuleLoad() {		
		TitlePanel title = new TitlePanel();
		RootPanel.get(HTML_TITLE_ID).add(title);
		title.setWidth(PERCENT_100);
		
		SpreadSheetAdminPanel tp = new SpreadSheetAdminPanel();
		RootPanel.get(HTML_SAMPLE_ID).add(tp);
		RootPanel.get(HTML_SAMPLE_ID).setWidth(PERCENT_100);
		tp.setWidth(PERCENT_100);}
}