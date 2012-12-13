package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeleteSpreadsheet extends Composite implements ClickHandler,
		ChangeHandler, AppConstants {

	private static final String WORKBOOK_ID_COLON = "Workbook ID:";

	/**
	 * String constant for the name of the client upload service.
	 */
	private final String URL_ADMIN_SERVICE = "adminService";

	private static final String PERCENT_100 = "100%";

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";

	private static final String DOCK_PANEL_WIDTH = PERCENT_100;

	private static final String RESULTS_DATA_WIDTH = "500px";

	/**
	 * Title for the Search box
	 */
	private final String SUB_TITLE = "Delete Workbook";

	/**
	 * Holder for the SubmissionService
	 */
	private AdminServiceAsync asa = null;
	
	private VerticalPanel mainPanel = new VerticalPanel();

	private final VerticalPanel upperPanel = new VerticalPanel();

	private final Button submitButton = new Button("Submit");

	private final FilteredNumberTextBox workbookIdTextBox = new FilteredNumberTextBox();
	
	/**
	 * Default constructor
	 */
	public DeleteSpreadsheet() {
		super();

		initService();

		initWidget(mainPanel);
		
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	
		mainPanel.setSpacing(10);
		
		DecoratorPanel dp = new DecoratorPanel();
		mainPanel.add(dp);
		mainPanel.setCellHorizontalAlignment(dp, HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.setWidth(DOCK_PANEL_WIDTH);

		upperPanel.setWidth(PERCENT_100);
		upperPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		upperPanel.setSpacing(10);

		// Sub-Title
		Label subTitleLabel = new Label(SUB_TITLE);
		subTitleLabel.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		upperPanel.add(subTitleLabel);
		upperPanel.setCellHorizontalAlignment(subTitleLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		workbookIdTextBox.addChangeHandler(this);
		LabeledWidget workbookIdBox = new LabeledWidget(workbookIdTextBox);
		workbookIdBox.setLabelText(WORKBOOK_ID_COLON);
		workbookIdBox.setPanelWidth(RESULTS_DATA_WIDTH);
		upperPanel.add(workbookIdBox);
		
		submitButton.addClickHandler(this);
		submitButton.setEnabled(false);
		upperPanel.add(submitButton);
		
		dp.add(upperPanel);
	}

	/**
	 * Private method to initialize and configure the services needed for this
	 * class.
	 */
	private void initService() {
		asa = (AdminServiceAsync) GWT.create(AdminService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) asa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + URL_ADMIN_SERVICE);
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == submitButton) {
			submitDeleteWorkbook();
		}
	}

	/**
	 * Private method to delete a workbook
	 */
	private void submitDeleteWorkbook() {
		final String wbIdString = workbookIdTextBox.getText();
		long wbId = 0;
		try {
			wbId = Long.parseLong(wbIdString);
		} catch (NumberFormatException e) {
			Window.alert("The workbook ID is not a valid number, " + wbIdString);
			return;
		}
		if (wbId != 0) {
			asa.deleteWorkbook(wbId, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("There was a problem deleting the workbook. Error: " + caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					workbookIdTextBox.setValue("");
					submitButton.setEnabled(false);
					Window.alert("The workbook ID, " + wbIdString + " was deleted.");
				}
			});
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == workbookIdTextBox) {
			enableSubmitButton();
		}
	}

	/**
	 * Private method to enable/disable the submitButton
	 *  by checking to see if the text box contains a number.
	 */
	private void enableSubmitButton() {
		String value = workbookIdTextBox.getText();
		long wbId = 0;
		if (!value.isEmpty()) {
			try {
				wbId = Long.parseLong(value);
			} catch (NumberFormatException e) {
				// Do nothing
			}
		}
		submitButton.setEnabled(wbId != 0);
	}
}
