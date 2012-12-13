package gov.nrel.nbc.tracker.client;

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
 * Composite panel that contains the main container and
 *  handles the navigation events.
 * 
 * @author jalbersh
 *
 */
public class TrackerPanel extends Composite implements ValueChangeHandler<String>, AppConstants {
	
	private static final int MENU_HEIGHT = 230;

	private static final String PERCENT_100 = "100%";

	private static final String SPLIT_PANEL_POSITION = "150px";

	//private static final String MAIN_PANEL_HEIGHT = "780px";

	/**
	 * Create Sample panel
	 */
	private CreateSample cs = new CreateSample();
	
	/**
	 * Search Sample panel
	 */
	private SearchSample ss = null;
	
	/**
	 * Navigation panel
	 */
	private NavigationPanel np = new NavigationPanel();
	
	/**
	 * Main panel <HorizontalSplitPanel>
	 */
	private HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();

	/**
	 * Constructor
	 */
	public TrackerPanel() {
		super();
		
		//mainPanel.setHeight(MAIN_PANEL_HEIGHT);
		mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");

		mainPanel.setSplitPosition(SPLIT_PANEL_POSITION);
		
		initWidget(mainPanel);
		
		mainPanel.setWidth(PERCENT_100);
		
		mainPanel.setRightWidget(cs);
		History.newItem(INIT_STATE);
		
		cs.setWidth(PERCENT_100);
		
		mainPanel.setLeftWidget(np);
		
		np.setWidth(PERCENT_100);
		
		History.addValueChangeHandler(this);
		
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
			}
			
		});
	}

	/**
	 * Public method to handle the <ValueChangeEvent>
	 */
	public void onValueChange(ValueChangeEvent<String> event) {
		updateState(event);
	}

	/**
	 * Private method to handle the navigation.
	 * 
	 * @param event
	 */
	private void updateState(ValueChangeEvent<String> event) {
		Widget rightWidget = mainPanel.getRightWidget();
		mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
		//Window.alert(event.getValue());
		mainPanel.remove(rightWidget);
		if (event.getValue().equalsIgnoreCase(INIT_STATE)) {
			cs = new CreateSample();
			mainPanel.setRightWidget(cs);
			History.newItem(INIT_STATE);
		} else if (event.getValue().equalsIgnoreCase(SEARCH_STATE)) {
			ss = new SearchSample();
			mainPanel.setRightWidget(ss);
			History.newItem(SEARCH_STATE);
		}
	}

	/**
	 * @return the mainPanel
	 */
	public HorizontalSplitPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(HorizontalSplitPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
