package gov.nrel.nbc.spreadsheet.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Public class that represents the panel used to navigate
 * from one panel to another
 * 
 * @author James Albersheim
 *
 */
public class NavigationPanel extends Composite implements AppConstants {

	private static final String SEARCH_LINK_TEXT = "Search Spreadsheets";
	private static final String UPLOAD_SPREADSHEET_LINK_TEXT = "Upload Spreadsheet";
	private static final String UPLOAD_MSPREADSHEET_LINK_TEXT = "Upload Multiple Spreadsheets";
	/**
	 * String constant for the name of the client upload service.
	 */
	private static final String URL_CALC_SUBMISSION_SERVICE = "spreadSheetService";
	private final VerticalPanel navPanel = new VerticalPanel();

	/**
	 * Holder for the SubmissionService
	 */
	private SpreadSheetServiceAsync ssa = null;

	public NavigationPanel() {
		//VerticalPanel navPanel = new VerticalPanel();
		
		initWidget(navPanel);
		
		navPanel.setSpacing(20);
		
		Hyperlink createLink = new Hyperlink(UPLOAD_SPREADSHEET_LINK_TEXT, INIT_STATE);
		navPanel.add(createLink);
		
		//Hyperlink multiLink = new Hyperlink(UPLOAD_MSPREADSHEET_LINK_TEXT, MULTI_STATE);
		//navPanel.add(multiLink);
		
		Hyperlink searchLink = new Hyperlink(SEARCH_LINK_TEXT, SEARCH_STATE);
		navPanel.add(searchLink);		

		HTML spacer = new HTML("<hr>");
		navPanel.add(spacer);
		navPanel.setCellHorizontalAlignment(spacer, HasHorizontalAlignment.ALIGN_CENTER);
		
		initService();
		
		getUrls();
	}
	/**
	 * Private method to escape any characters that need escaping.
	 * 
	 * @param maybeHtml <String> String to be cleaned up.
	 * 
	 * @return <String> cleaned string.
	 */
	private String escapeHtml(String maybeHtml) {
		final Element div = DOM.createDiv();
		DOM.setInnerText(div, maybeHtml);
		return DOM.getInnerHTML(div);
	}
	/**
	 * Private method to initialize and configure the services needed for this
	 * class.
	 */
	private void initService() {
		ssa = (SpreadSheetServiceAsync) GWT.create(SpreadSheetService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) ssa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ URL_CALC_SUBMISSION_SERVICE);
	}
	private void getUrls() {
		ssa.getUrls(new AsyncCallback<List<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				// Do nothing
			}

			//@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty())
					loadUrls(result);
			}
		});
	}

	/**
	 * Private method to load headings and create default
	 * widgets for values
	 */
	private void loadUrls(List<NameValue> urls) {
		if (urls != null) {
			Iterator<NameValue> uit = urls.iterator();
			while (uit.hasNext()) {
				NameValue nv = uit.next();
				HTML trackerLink = new HTML("<a target=\"_blank\" href=" + escapeHtml(nv.getValue()) + ">"+nv.getName()+"</a>");
				navPanel.add(trackerLink);
			}
		}
	}
}
