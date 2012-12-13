package gov.nrel.nbc.tracker.client;

import gov.nrel.nbc.tracker.client.DevTestProdConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Composite panel that handles the screen navigation.
 * 
 * @author jalbersh
 *
 */
public class NavigationPanel extends Composite implements AppConstants {

	private static final String SEARCH_SAMPLE_LINK_SAMPLE = "Search Sample";
	private static final String CREATE_SAMPLE_LINK_LABEL = "Enter Sample";

	private TrackerServiceAsync tsa = null;
	//private String sessionId = "";
	public NavigationPanel() {
		initService();
		final VerticalPanel navPanel = new VerticalPanel();
		
		initWidget(navPanel);
		
		navPanel.setSpacing(20);
		
		Hyperlink createLink = new Hyperlink(CREATE_SAMPLE_LINK_LABEL, INIT_STATE);
		navPanel.add(createLink);
		
		Hyperlink searchLink = new Hyperlink(SEARCH_SAMPLE_LINK_SAMPLE, SEARCH_STATE);
		navPanel.add(searchLink);
		
		HTML spacer = new HTML("<hr>");
		navPanel.add(spacer);
		navPanel.setCellHorizontalAlignment(spacer, HasHorizontalAlignment.ALIGN_CENTER);
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							HTML trackerLink = new HTML("<a target=\"_blank\" href=" + escapeHtml("/workmgmt/") + ">Work Management</a>");
							navPanel.add(trackerLink);
					
							HTML arLink = new HTML("<a target=\"_blank\" href=" + escapeHtml("/ar/") + ">Analytical Request</a>");
							navPanel.add(arLink);
					
							HTML caLink = new HTML("<a target=\"_blank\" href=" + escapeHtml("/spreadsheet/") + ">Spreadsheet Management</a>");
							navPanel.add(caLink);
					
							HTML trbLink = new HTML("<a target=\"_blank\" href=" + escapeHtml("/trbmgmt/") + ">TRB Management</a>");
							navPanel.add(trbLink);
						} else {
							HTML workLink = new HTML("<a target=\"_blank\" href=" + escapeHtml("/algae-workmgmt/") + ">Work Management</a>");
							navPanel.add(workLink);							
						}
	}

	private void initService() {
		tsa = (TrackerServiceAsync) GWT.create(TrackerService.class);		

		ServiceDefTarget endpoint = (ServiceDefTarget) tsa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "trackerService");
 	}

	private String escapeHtml(String maybeHtml) {
		final Element div = DOM.createDiv();
		DOM.setInnerText(div, maybeHtml);
		return DOM.getInnerHTML(div);
	}
}
