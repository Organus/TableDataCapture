package gov.nrel.nbc.spreadsheet.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SpreadSheet implements EntryPoint {
	
	private static final String PERCENT_100 = "100%";
	private static final String HTML_SAMPLE_ID = "sheetContainer";
	private static final String HTML_TITLE_ID = "sheetContainer";
	
	public void onModuleLoad() {		
		TitlePanel title = new TitlePanel();
		RootPanel.get(HTML_TITLE_ID).add(title);
		title.setWidth(PERCENT_100);
		 
		SpreadSheetPanel tp = new SpreadSheetPanel(); 
		RootPanel.get(HTML_SAMPLE_ID).add(tp);
		RootPanel.get(HTML_SAMPLE_ID).setWidth(PERCENT_100);
		tp.setWidth(PERCENT_100);}
}
