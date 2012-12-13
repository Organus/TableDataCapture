package gov.nrel.nbc.spreadsheet.client;


import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class contains the main HorizontalSplitPanel and handles the events for the
 * navigation panel.
 * 
 * @author James Albersheim
 *
 */
public class SpreadSheetPanel extends Composite implements ValueChangeHandler<String>, AppConstants {
	
	private static final String PERCENT_100 = "100%";
	private static final String SPLIT_PANEL_POSITION = "150px";
	private static final int MENU_HEIGHT = 230;
	//private static final String MAIN_PANEL_WIDTH = "900px";
	private UploadSpreadSheet us = new UploadSpreadSheet();
	private SearchSpreadsheet ss = null;
	private NavigationPanel np = new NavigationPanel();
	private HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();

	/**
	 * Constructor to set everything up.
	 */
	public SpreadSheetPanel() {
		super();
		
		//mainPanel.setHeight(MAIN_PANEL_WIDTH);
		mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
		mainPanel.setSplitPosition(SPLIT_PANEL_POSITION);
		
		initWidget(mainPanel);
		
		mainPanel.setWidth(PERCENT_100);
		
		mainPanel.setRightWidget(us);
		History.newItem(INIT_STATE);
		
		us.setWidth(PERCENT_100);
		
		mainPanel.setLeftWidget(np);
		
		np.setWidth(PERCENT_100);
		
		History.addValueChangeHandler(this);

		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
			}
			
		});
	}

	/**
	 * Public method to capture the change in states due to use
	 * clicking in navigation panel.
	 * 
	 * @param event <ValueChangeEvent<String>>
	 */
	public void onValueChange(ValueChangeEvent<String> event) {
		updateState(event);
	}

	/**
	 * Private method to set the right panel of the HorizontalSplitPanel.
	 * 
	 * @param event <ValueChangeEvent<String>> representing user selection.
	 */
	private void updateState(ValueChangeEvent<String> event) {
		Widget rightWidget = mainPanel.getRightWidget();
		mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
		mainPanel.remove(rightWidget);
		if (event.getValue().equalsIgnoreCase(INIT_STATE)) {
			us = new UploadSpreadSheet();
			mainPanel.setRightWidget(us);
			History.newItem(INIT_STATE);
		} else if (event.getValue().equalsIgnoreCase(SEARCH_STATE)) {
			ss = new SearchSpreadsheet();
			mainPanel.setRightWidget(ss);
			History.newItem(SEARCH_STATE);
		}
	}
}
