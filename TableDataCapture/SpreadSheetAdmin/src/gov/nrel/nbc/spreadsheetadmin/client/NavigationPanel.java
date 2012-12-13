package gov.nrel.nbc.spreadsheetadmin.client;

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

	private static final String DELETE_LINK_TEXT = "Delete Workbook";
	private static final String DELETE_CONFIG_LINK_TEXT = "Delete Configuration";
	private static final String CREATE_LINK_TEXT = "Create Configuration";
	//private static final String MODIFY_LINK_TEXT = "Modify Configuration";
	/**
	 * String constant for the name of the client upload service.
	 */
	private final String URL_ADMIN_SERVICE = "adminService";
	private final VerticalPanel navPanel = new VerticalPanel();

	/**
	 * Holder for the SubmissionService
	 */
	private AdminServiceAsync ssa = null;

	public NavigationPanel() {
		initWidget(navPanel);
		
		navPanel.setSpacing(20);
		
		Hyperlink createLink = new Hyperlink(CREATE_LINK_TEXT, INIT_STATE);
		navPanel.add(createLink);
		
		//Hyperlink modifyLink = new Hyperlink(MODIFY_LINK_TEXT, MODIFY_STATE);
		//navPanel.add(modifyLink);
		
		Hyperlink deleteConfigLink = new Hyperlink(DELETE_CONFIG_LINK_TEXT, DELETE_CONFIG_STATE);
		navPanel.add(deleteConfigLink);

		Hyperlink deleteLink = new Hyperlink(DELETE_LINK_TEXT, DELETE_STATE);
		navPanel.add(deleteLink);

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
		ssa = (AdminServiceAsync) GWT.create(AdminService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) ssa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ URL_ADMIN_SERVICE);
	}
	private void getUrls() {
		ssa.getUrls(new AsyncCallback<List<NameValue>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Do nothing
			}

			@Override
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
