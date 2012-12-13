package gov.nrel.nbc.spreadsheet.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import org.mortbay.log.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * A public class representing the search panel.
 * 
 * @author James Albersheim
 *
 */
public class SearchSpreadsheet extends Composite implements AppConstants, ClickHandler, ChangeHandler {
	
	//Constants

	private static final String EQUAL_STRING = "=";

	private static final String FALSE = "false";

	private static final String TRUE = "true";

	private static final String WORKSHEET_TYPE_COLON = "Worksheet Type:";

	private static final String WORKBOOK_TYPE_COLON = "Workbook Type:";

	private static final String WORKBOOK_ID_COLON = "Workbook ID:";

	private static final String ATTACHMENT_EXT_COLON = "Attachment Extension:";

	private static final String BLANKS = "---";

	private static final String EMPTY_STRING = "";

	private static final String EDIT_ICON = "edit-16x16.JPG";

	private static final String DOWNLOAD_IN_PROGRESS = "Download in progress. Please wait...";

	private static final String WAIT_MESSAGE = "Please wait for search results.";

	private static final String WAIT_JPG = "hourglass1.jpg";

	private static final String SS_FILE_PATH = "excelfile";

	private static final String SS_FILE_NAME = "ssFileName";

	private static final String DATE_FORMAT_DISPLAY = "MM/dd/yyyy";

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";

	private static final String DOCK_PANEL_WIDTH = "500px";

	private static final String LOWER_PANEL_HEIGHT = "600px";

	private static final String LOWER_PANEL_WIDTH = "500px";

	private static final String RESULTS_DATA_WIDTH = "500px";

	private static final String RESULTS_PANEL_WIDTH = "500px";

	private static final String SCROLL_PANEL_WIDTH = "600px";
	private static final String SCROLL_PANEL_HEIGHT = "400px";
	private static final String GRID_BUTTON_STYLE = "gwt-Button-GridButton";
	private static final String SPREADSHEET_BUTTON_TEXT = "Workbook ";
	private static final String GRID_LABEL_STYLE = "Grid";
	private static final String DOWNLOAD = "Download";
	private static final String EDIT = "Edit";
	private static final String VALUE_MISSING_MESSAGE = "Value missing.";
	private static final String SUGGEST_BOX = "SuggestBox";
	private static final String TEXT_BOX = "TextBox";
	private static final String DATE_BOX = "DateBox";

	private static final String FILTERED_SUGGEST_BOX = "FilteredSuggestBox";

	private static final String RESULT_GRID_RADIO_BUTTON_GROUP = "resultGrid";
	private static final String ZERO_STRING = "0";
	private static final String COUNT = "Count";
	private static final String WORKSHEET_TYPE = "Worksheet Type";
	private static final String WORKBOOK_TYPE = "Workbook Type";
	private static final String SELECT = "Select";

	/**
	 * Message shown before a search is performed or after the clear button has been selected.
	 */
	private static final String DEFAULT_TABLE_MESSAGE = "Please enter search criteria, above.";

	/**
	 * Text for clear button
	 */
	private static final String CLEAR = "Clear";

	/**
	 * Maximum number of search criteria a user may search.
	 */
	private static final int NUM_TRIOS = 5;

	/**
	 * String constant for the name of the client upload service.
	 */
	private static final String URL_CALC_SUBMISSION_SERVICE = "spreadSheetService";

	/**
	 * String constant for the URL of the client download service
	 */
	private static final String URL_CLIENT_DOWNLOAD_SERVICE = "clientDownloadService";

	/**
	 * String constant for the URL of the client download service
	 */
	private static final String URL_PDF_DOWNLOAD_SERVICE = "pdfDownloadService";

	/**
	 * Holder for the SubmissionService
	 */
	private SpreadSheetServiceAsync ssa = null;

	/**
	 * Title for the Search box
	 */
	private static final String SEARCHTITLE = "Search";
	
	/**
	 * Header for the heading column of the search box.
	 */
	private static final String HEADING = "Heading";
	
	/**
	 * Header for the operator column of the search box.
	 */
	private static final String OPERATOR = "Operator";
	
	/**
	 * Header for the value column of the search box.
	 */
	private static final String VALUE = "Value";
	
	/**
	 * Title for the results box
	 */
	private static final String RESULTS_TITLE = "Detail Data";
	
	/**
	 * Title of the button to download the search results
	 */
	private static final String DOWNLOAD2 = "Download Data";
	
	/**
	 * Title of the button to submit the query to the server
	 */
	private static final String SUBMIT = "Submit";
	
	/**
	 * A list containing the header for a user to choose from.
	 */
	private ArrayList<ListBox> headers = new ArrayList<ListBox>();
	
	/**
	 * A container for the search criteria
	 */
	private Grid criteriaGrid = new Grid(6, 3);
	
	/**
	 * Main container for the Search Panel
	 */
	private VerticalPanel dock;
	
	/**
	 * Container that paints a box around the results box.
	 */
	private DecoratorPanel lower = new DecoratorPanel();
	
	/**
	 * Container that organizes the elements of he results box.
	 */
	private VerticalPanel lowervp = new VerticalPanel();
	
	/**
	 * Container for the results; Initially empty.
	 */
	//@SuppressWarnings("unchecked")
	//private CellTable tableGrid = new CellTable(0);//(0, 0);
	private Grid tableGrid = new Grid(0, 0);

	/**
	 * A list of the search criteria (heading, operator and value)
	 */
	private List<CriteriaTrioDTO> trioList = new ArrayList<CriteriaTrioDTO>();

	/**
	 * A mapping table that holds the widgets to display for any selected heading
	 */
	private HashMap<String, List<Widget>> critWidgetMap = new HashMap<String, List<Widget>>();

	/**
	 * A scrolling container for the results data
	 */
	private ScrollPanel lowersp = new ScrollPanel();

	/**
	 * Variable to hold the GWT FormPanel for holding the download web form.
	 */
	private final FormPanel pdfDownloadForm = new FormPanel();

	/**
	 * GWT Hidden variable to hold the name of the Excel file to download to the
	 * users workstation from a search
	 */
	private final Hidden trbPage = new Hidden();

	/**
	 * GWT Hidden variable to hold the name of the Excel file to download to the
	 * users workstation from a search
	 */
	private final Hidden trbNum = new Hidden();

	/**
	 * Variable to hold the GWT FormPanel for holding the download web form.
	 */
	private final FormPanel downloadForm = new FormPanel();

	/**
	 * GWT Hidden variable to hold the name of the Excel file to download to the
	 * users workstation from a search
	 */
	private final Hidden downloadFile = new Hidden();

	/**
	 * GWT Hidden variable to hold the path of the Excel file to download to the
	 * users workstation from a search
	 */
	private final Hidden downloadPath = new Hidden();
	/**
	 * Variable to hold the GWT FormPanel for holding the download spreadsheet web form.
	 */
	private final FormPanel ssDownloadForm = new FormPanel();

	/**
	 * GWT Hidden variable to hold the name of the Excel file to download to the
	 * users workstation from a request to download the CA spreadsheet.
	 */
	private final Hidden ssDownloadFilePath = new Hidden();
	
	private final Hidden ssDownloadFileName = new Hidden();

	private PopupPanel downloadDialog = new PopupPanel();

	private final ListBox workbookConfigBox = new ListBox();

	private final ListBox worksheetConfigBox = new ListBox();

	private final Grid resultGrid = new Grid(0, 4);

	private final VerticalPanel centervp = new VerticalPanel();

	private final FilteredNumberTextBox workbookIdBox = new FilteredNumberTextBox();

	private final ListBox attachmentExtBox = new ListBox();

	private final Button submitButton = new Button(SUBMIT);

	private final Button clearButton = new Button(CLEAR);
	
	private final Button nextButton = new Button("Next Page");
	private final Button prevButton = new Button("Previous Page");

	private final Button downloadButton = new Button(DOWNLOAD2);
	
	private HorizontalPanel hp4 = new HorizontalPanel();
	private List<Integer> position = new ArrayList<Integer>();
	private int which=1;
	private int page=PAGE;
	private final ListBox pageSize = new ListBox();

	/**
	 * Constructor that sets up elements of the search panel.
	 */
	public SearchSpreadsheet() {

		initService();

		nextButton.addClickHandler(this);
		prevButton.addClickHandler(this);
		downloadButton.addClickHandler(new DownloadClickHandler());

		for (int i=0;i<100;i++) {
			position.add(0);
		}
		
		dock = new VerticalPanel();
		initWidget(dock);
		dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	
		dock.setSpacing(10);
		DecoratorPanel dp = new DecoratorPanel();
		dock.add(dp);
		dock.setCellHorizontalAlignment(dp, HasHorizontalAlignment.ALIGN_CENTER);
		lower.add(lowervp);
		lowervp.setWidth(RESULTS_PANEL_WIDTH);
		lowervp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
		lowervp.add(defaultLabel);
		
		centervp.add(resultGrid);
		//centervp.add(hp4);
		resultGrid.setVisible(false);
		resultGrid.setWidth("500px");
		hp4.setWidth("500px");
		dock.add(centervp);	
		
		HorizontalPanel pagePnl = new HorizontalPanel();
		Label pageSizeLbl = new Label("Page size: ");
		pageSizeLbl.setStyleName("Black");
		pagePnl.add(pageSizeLbl);
		pagePnl.add(pageSize);
		getPageSizes();
		pageSize.addChangeHandler(this);
		dock.add(pagePnl);
		
		resultGrid.setBorderWidth(1);

		setUpDownloadForm();
		setUpSSDownloadForm();
		setUpPdfDownloadForm();

		tableGrid.setWidth(RESULTS_DATA_WIDTH);

		lower.setWidth(LOWER_PANEL_WIDTH);
		lower.setHeight(LOWER_PANEL_HEIGHT);
		dock.add(lower);
		dock.setCellHorizontalAlignment(lower, HasHorizontalAlignment.ALIGN_CENTER);
		dock.setWidth(DOCK_PANEL_WIDTH);

		criteriaGrid.setBorderWidth(1);

		VerticalPanel upperLeft = new VerticalPanel();
		upperLeft.setSpacing(10);
		dp.add(upperLeft);

		// Upper West
		Label srchLabel = new Label(SEARCHTITLE);
		srchLabel.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		upperLeft.add(srchLabel);
		upperLeft.setCellHorizontalAlignment(srchLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Workbook Type
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth(DOCK_PANEL_WIDTH);
		hp.setSpacing(10);
		Label workbookConfigLabel = new Label(WORKBOOK_TYPE_COLON);
		hp.add(workbookConfigLabel);
		hp.add(workbookConfigBox);
		hp.setCellHorizontalAlignment(workbookConfigBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperLeft.add(hp);
		loadWorkbookConfigNames();
		
		// Worksheet Type
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setWidth(DOCK_PANEL_WIDTH);
		hp1.setSpacing(10);
		Label worksheetConfigLabel = new Label(WORKSHEET_TYPE_COLON);
		hp1.add(worksheetConfigLabel);
		worksheetConfigBox.addChangeHandler(this);
		hp1.add(worksheetConfigBox);
		hp1.setCellHorizontalAlignment(worksheetConfigBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperLeft.add(hp1);
		loadWorksheetConfigNames();
		
		// Workbook ID
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setWidth(DOCK_PANEL_WIDTH);
		hp2.setSpacing(10);
		Label workbookIdLabel = new Label(WORKBOOK_ID_COLON);
		hp2.add(workbookIdLabel);
		hp2.add(workbookIdBox);
		hp2.setCellHorizontalAlignment(workbookIdBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperLeft.add(hp2);

		// Attachment Extension
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.setWidth(DOCK_PANEL_WIDTH);
		hp3.setSpacing(10);
		Label attachmentExtLabel = new Label(ATTACHMENT_EXT_COLON);
		hp3.add(attachmentExtLabel);
		hp3.add(attachmentExtBox);
		hp3.setCellHorizontalAlignment(attachmentExtBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperLeft.add(hp3);
		loadAttachmentExtensions();

		Label hdrLabel = new Label(HEADING);
		criteriaGrid.setWidget(0, 0, hdrLabel);
		criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		Label opLabel = new Label(OPERATOR);
		criteriaGrid.setWidget(0, 1, opLabel);
		criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		Label valLabel = new Label(VALUE);
		criteriaGrid.setWidget(0, 2, valLabel);
		criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);

		// get headers from DB
		for (int i = 0; i < NUM_TRIOS; i++) {
			ListBox hdr = new ListBox();
			hdr.addChangeHandler(new HdrChangeHandler());
			headers.add(hdr);
			criteriaGrid.setWidget(1 + i, 0, hdr);
		}

		upperLeft.add(criteriaGrid);
		
		upperLeft.add(new Label("Use the * character for wildcard searches"));

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);

		submitButton.addClickHandler(this);
		buttonPanel.add(submitButton);

		clearButton.addClickHandler(this);
		buttonPanel.add(clearButton);

		upperLeft.add(buttonPanel);
		upperLeft.setCellHorizontalAlignment(buttonPanel,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		getHeadings();
		
		initDownloadDialog();
	}
	
	private void getPageSizes() {
		pageSize.addItem(EMPTY_STRING);
		pageSize.addItem("1");
		pageSize.addItem("2");
		pageSize.addItem("3");
		pageSize.addItem("4");
		pageSize.addItem("5");
		pageSize.addItem("6");
		pageSize.addItem("7");
		pageSize.addItem("8");
		pageSize.addItem("9");
		pageSize.addItem("10");
		pageSize.addItem("15");
		pageSize.addItem("20");
		pageSize.addItem("25");
		pageSize.addItem("30");
		pageSize.addItem("35");
		pageSize.addItem("40");
		pageSize.addItem("45");
		pageSize.addItem("50");
	}

	/**
	 * Private method to get meta data headers.
	 */
	private void getHeadings() {
		ssa.getHeaders(null, null, new AsyncCallback<List<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				// Do nothing
			}

			//@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty())
					loadHeadingsAndValues(result);
			}
		});
	}

	/**
	 * Private method to initalize the download dialog.
	 */
	private void initDownloadDialog() {
		VerticalPanel vpanel1 = new VerticalPanel();
		vpanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label waitLabel1 = new Label(DOWNLOAD_IN_PROGRESS);
		vpanel1.add(waitLabel1);
		Image hourGlass = new Image(WAIT_JPG);
		vpanel1.add(hourGlass);
		final int hourHeight = hourGlass.getHeight();
		final int hourWidth = hourGlass.getWidth();
		downloadDialog.add(vpanel1);
		downloadDialog.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int top = ((Window.getClientHeight() - offsetHeight) / 2) - hourHeight/2;
				int left = ((Window.getClientWidth() - offsetWidth) / 2) - hourWidth/2;
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				downloadDialog.setPopupPosition(left, top);
				downloadDialog.hide();
			}			
		});
	}

	/**
	 * Private method to call the server for workbook configuration names.
	 */
	private void loadWorkbookConfigNames() {
		workbookConfigBox.addItem(EMPTY_STRING);
		workbookConfigBox.addChangeHandler(this);
		ssa.getWorkbookConfigs(new WorkbookConfigCallback());
	}

	/**
	 * Private method to call the server for attachment extensions.
	 */
	private void loadAttachmentExtensions() {
		attachmentExtBox.addItem(EMPTY_STRING);
		ssa.getAttachmentExtensions(new AttachmentExtensionsCallback());
	}

	/**
	 * Private method to call the server for workbook sheet configuration names.
	 */
	private void loadWorksheetConfigNames() {
		ssa.getSheetConfigs(null, new WorksheetConfigCallback());
	}
	
	/**
	 * Private inner class to load the attachment extensions.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class AttachmentExtensionsCallback implements AsyncCallback<List<String>> {

		//@Override
		public void onFailure(Throwable caught) {
			// Do not report here
		}

		//@Override
		public void onSuccess(List<String> result) {
			if (result != null && !result.isEmpty()) {
				Iterator<String> it = result.iterator();
				while (it.hasNext()) {
					String ext = it.next();
					attachmentExtBox.addItem(ext);
				}
			}
		}
	}
	
	/**
	 * Private inner class to load the workbook configuration names.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class WorkbookConfigCallback implements AsyncCallback<List<String>> {

		//@Override
		public void onFailure(Throwable caught) {
			// Do not report here
		}

		//@Override
		public void onSuccess(List<String> result) {
			if (result != null && !result.isEmpty()) {
				Iterator<String> it = result.iterator();
				while (it.hasNext()) {
					String configName = it.next();
					workbookConfigBox.addItem(configName);
				}
			}
		}
	}
	
	/**
	 * Private inner class to load the workbook sheet configuration names.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class WorksheetConfigCallback implements AsyncCallback<List<String>> {

		//@Override
		public void onFailure(Throwable caught) {
			// Do not report here
		}

		//@Override
		public void onSuccess(List<String> result) {
			worksheetConfigBox.clear();
			worksheetConfigBox.addItem(EMPTY_STRING);
			if (result != null && !result.isEmpty()) {
				Iterator<String> it = result.iterator();
				while (it.hasNext()) {
					String configName = it.next();
					worksheetConfigBox.addItem(configName);
				}
			}
		}
	}

	/**
	 * Private method to load headings and create default
	 * widgets for values
	 */
	private void loadHeadingsAndValues(List<NameValue> headerTypes) {
		// clear values first
		// Reset headers and operators
		for (int row = 1; row <= NUM_TRIOS; row++) {
			// Operator
			criteriaGrid.clearCell(row, 1);

			// Values
			criteriaGrid.clearCell(row, 2);
		}
		critWidgetMap.clear();
		if (headerTypes != null) {
			Iterator<ListBox> it = headers.iterator();
			while (it.hasNext()) {
				ListBox listBox = it.next();
				listBox.clear();
				listBox.addItem(EMPTY_STRING);
				Iterator<NameValue> it2 = headerTypes.iterator();
				while (it2.hasNext()) {
					NameValue headerType = it2.next();
					String headerName = headerType.getSynonym();
					long type = headerType.getDataType();
					int count = listBox.getItemCount();
					boolean found = false;
					int ctr=0;
					while (ctr<count && !found) {
						String item = listBox.getItemText(ctr++);
						if (item.equals(headerName)) found = true;
					}
					if (!found) listBox.addItem(headerName);
					List<Widget> widgets = new ArrayList<Widget>();
					if (type == 3) { // Date
						for (int i = 0; i < NUM_TRIOS; i++) {
							DateBox db = new DateBox();
							Date date = new Date();
							db.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(DATE_FORMAT_DISPLAY)));
							db.setValue(date);
							widgets.add(db);
						}
					} else if ((type == 1) || (type == 2)) { // Long or Real
						for (int i = 0; i < NUM_TRIOS; i++) {
							widgets.add(new FilteredNumberTextBox());
						}
					} else if (type == 5) { // Boolean
						MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();
						suggestOracle.add(TRUE);
						suggestOracle.add(FALSE);
						for (int i = 0; i < NUM_TRIOS; i++) {
							widgets.add(new FilteredSuggestBox(suggestOracle));
						}
					} else if (type == 4) { // String
						final MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();
						for (int i = 0; i < NUM_TRIOS; i++) {
							widgets.add(new FilteredSuggestBox(suggestOracle));
						}
					}
					critWidgetMap.put(headerName, widgets);
				}
			}
		}
	}

	/**
	 * This protected method performs a search and returns
	 * and formats the results
	 * 
	 * @param trioList A list of CriteriaTrioDTO
	 */
	private void performSelect(List<CriteriaTrioDTO> trioList) {
		which = 1;
		position.set(which,0);
		final List<CriteriaTrioDTO> finalTrioList = trioList;
		String workbookConfig = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		if (workbookConfig.isEmpty())
			workbookConfig = null;
		String worksheetConfig = null;
		int selIndex = worksheetConfigBox.getSelectedIndex();
		if (selIndex != -1) {
			worksheetConfig = worksheetConfigBox.getItemText(selIndex);
			if (worksheetConfig.isEmpty())
				worksheetConfig = null;
		}
		ssa.getWorkbookCounts(workbookConfig, worksheetConfig, trioList, position.get(which), page, new AsyncCallback<List<List<String>>>() {
		//ssa.performSelect(workbookConfig, worksheetConfig, trioList, new AsyncCallback<List<List<String>>>() {
			
			public void onFailure(Throwable caught) {
				Window.alert("Table Data failed to load.");
			}
			
			public void onSuccess(List<List<String>> result) {
			
				if (result != null && !result.isEmpty()) {
					resultGrid.resizeRows(1);
					resultGrid.setText(0, 0, SELECT);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 1, WORKBOOK_TYPE);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 2, WORKSHEET_TYPE);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 3, COUNT);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
					// Populate list
					Iterator<List<String>> it = result.iterator();
					while (it.hasNext()) {
						List<String> rows = it.next();
						if (!rows.get(2).equalsIgnoreCase(ZERO_STRING)) {
							resultGrid.resizeRows(resultGrid.getRowCount() + 1);
							int rowCount = resultGrid.getRowCount() - 1;
							RadioButton rb = new RadioButton(RESULT_GRID_RADIO_BUTTON_GROUP);
							rb.addClickHandler(new ResultBoxClickHandler());
							resultGrid.setWidget(rowCount, 0, rb);
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 0, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 1, rows.get(0));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 1, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 2, rows.get(1));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 2, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 3, rows.get(2));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 3, HasHorizontalAlignment.ALIGN_CENTER);
						}
					}
					
					// Select first data row.
					if (resultGrid.getRowCount() > 1) {
						resultGrid.setVisible(true);
						RadioButton rb = (RadioButton)resultGrid.getWidget(which, 0);
						rb.setValue(true);
						String workbookConfig = resultGrid.getText(which, 1);
						String worksheetConfig = resultGrid.getText(which, 2);
						getDataRows(finalTrioList, workbookConfig, worksheetConfig);
					} else {
						Window.alert("Your criteria returned no data.");

						lowervp.clear();
						Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
						lowervp.add(defaultLabel);
					}
				} else {
					Window.alert("Your criteria returned no data.");

					lowervp.clear();
					Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
					lowervp.add(defaultLabel);
				}
			}
		});
	}

	/**
	 * This protected method performs a search and returns
	 * and formats the results
	 * 
	 * @param trioList A list of CriteriaTrioDTO
	 */
	private void performSelect(List<CriteriaTrioDTO> trioList, final int minusPlus) {
		final List<CriteriaTrioDTO> finalTrioList = trioList;
		String workbookConfig = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		if (workbookConfig.isEmpty())
			workbookConfig = null;
		String worksheetConfig = null;
		int selIndex = worksheetConfigBox.getSelectedIndex();
		if (selIndex != -1) {
			worksheetConfig = worksheetConfigBox.getItemText(selIndex);
			if (worksheetConfig.isEmpty())
				worksheetConfig = null;
		}
		ssa.getWorkbookCounts(workbookConfig, worksheetConfig, trioList, position.get(which), page, new AsyncCallback<List<List<String>>>() {
		//ssa.performSelect(workbookConfig, worksheetConfig, trioList, new AsyncCallback<List<List<String>>>() {
			
			public void onFailure(Throwable caught) {
				Window.alert("Table Data failed to load.");
			}
			
			public void onSuccess(List<List<String>> result) {
			
				if (result != null && !result.isEmpty()) {
					resultGrid.resizeRows(1);
					resultGrid.setText(0, 0, SELECT);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 1, WORKBOOK_TYPE);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 2, WORKSHEET_TYPE);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
					resultGrid.setText(0, 3, COUNT);
					resultGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
					// Populate list
					Iterator<List<String>> it = result.iterator();
					while (it.hasNext()) {
						List<String> rows = it.next();
						if (!rows.get(2).equalsIgnoreCase(ZERO_STRING)) {
							resultGrid.resizeRows(resultGrid.getRowCount() + 1);
							int rowCount = resultGrid.getRowCount() - 1;
							RadioButton rb = new RadioButton(RESULT_GRID_RADIO_BUTTON_GROUP);
							rb.addClickHandler(new ResultBoxClickHandler());
							resultGrid.setWidget(rowCount, 0, rb);
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 0, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 1, rows.get(0));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 1, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 2, rows.get(1));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 2, HasHorizontalAlignment.ALIGN_CENTER);
							resultGrid.setText(rowCount, 3, rows.get(2));
							resultGrid.getCellFormatter().setHorizontalAlignment(rowCount, 3, HasHorizontalAlignment.ALIGN_CENTER);
						}
					}
					
					// Select first data row.
					if (resultGrid.getRowCount() > 1) {
						resultGrid.setVisible(true);
						RadioButton rb = (RadioButton)resultGrid.getWidget(which, 0);
						rb.setValue(true);
						String workbookConfig = resultGrid.getText(which, 1);
						String worksheetConfig = resultGrid.getText(which, 2);
						int pos = position.get(which)+minusPlus;
						if (pos < 0) pos = 0;
						position.set(which,pos);
						int num = 0;
						String snum = resultGrid.getText(which, 3);
						try {
							num = Integer.parseInt(snum);
							int inum = new Integer(num/page);
							double div = (double)num/(double)page;
							if (div > inum)
								num = inum+1;
							else 
								num = inum;
						} catch (NumberFormatException nfe){}
						//Window.alert("position="+position.get(which)+";num="+num);
						if (position.get(which) >= num) position.set(which, num-1);
						getDataRows(finalTrioList, workbookConfig, worksheetConfig);
					} else {
						Window.alert("Your criteria returned no data.");

						lowervp.clear();
						Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
						lowervp.add(defaultLabel);
					}
				} else {
					Window.alert("Your criteria returned no data.");

					lowervp.clear();
					Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
					lowervp.add(defaultLabel);
				}
			}
		});
	}

	/**
	 * Private method to perform a search and process the results.
	 * 
	 * @param finalTrioList List<CriteriaTrioDTO>
	 * @param workbookConfig String
	 * @param worksheetConfig String
	 */
	private void getDataRows(final List<CriteriaTrioDTO> finalTrioList, String workbookConfig, String worksheetConfig) {
		ssa.performSelect(workbookConfig, worksheetConfig, finalTrioList, position.get(which), page, new AsyncCallback<List<List<String>>>() {
			public void onFailure(Throwable caught) {
				// Do not report
			}

			public void onSuccess(List<List<String>> result) {
				if ((result != null) && (!result.isEmpty())) {
					try {
						displayResults(result);
					} catch (Exception e) {
						String trace = e.getMessage();
						StackTraceElement[] stack = e.getStackTrace();
						for (int i=0;i<stack.length;i++) {
							trace += "\n" + stack[i];
						}
						Window.alert(trace);
					}
				} else {
					Window.alert("Your criteria returned no data.");

					lowervp.clear();
					Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
					lowervp.add(defaultLabel);
				}
			}
		});
	}

	/**
	 * Private method to display the results of the search.
	 * 
	 * @param rowList List<List<String>>
	 */
	//@SuppressWarnings("unchecked")
	private void displayResults(List<List<String>> rowList) {
		lowervp.clear();
		
		// Add title for results
		String snum = resultGrid.getText(which, 3);
		int num = 0;
		try {
			num = Integer.parseInt(snum);
			int inum = new Integer(num/page);
			double div = (double)num/(double)page;
			if (div > inum)
				num = inum+1;
			else 
				num = inum;
		} catch (NumberFormatException nfe){}
		String info = "Page "+(position.get(which)+1)+" of "+(num)+" pages";
		Label pageInfo = new Label(info);
		pageInfo.addStyleDependentName("SubSubTitle");
		lowervp.add(pageInfo);
		lowervp.setCellHorizontalAlignment(pageInfo, HasHorizontalAlignment.ALIGN_CENTER);

		// Add title for results
		Label title = new Label(RESULTS_TITLE);
		title.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		lowervp.add(title);
		lowervp.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
		
		int rowIndex = 0;
		
		//tableGrid = new CellTable(0);
		tableGrid = new Grid(0, 0);
		tableGrid.setBorderWidth(1);
		
		// Iterate through the rows
		Iterator<List<String>> rowIt = rowList.iterator();
		while (rowIt.hasNext()) {
			List<String> collist = rowIt.next();
			
			// Add row
			//tableGrid.setRowCount(tableGrid.getRowCount()+1);
			tableGrid.resizeRows(tableGrid.getRowCount() + 1);
			
			int colIndex = 0;
			
			// iterate through the columns
			Iterator<String> colIt = collist.iterator();
			while (colIt.hasNext()) {
				String cellValue = colIt.next();
				
				if (cellValue == null || cellValue.isEmpty())
					cellValue = BLANKS;
				
				if ((rowIndex == 0) && (colIndex == 0)) { // Add headers for button columns and first text column header
					tableGrid.resizeColumns(tableGrid.getColumnCount() + 3);
					
					Label dlLabel = new Label(DOWNLOAD);
					dlLabel.addStyleDependentName(GRID_LABEL_STYLE);
					//tableGrid.addColumn(tc1,DOWNLOAD);
					tableGrid.setWidget(rowIndex, colIndex, dlLabel);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
					
					colIndex++;
					
					Label editLabel = new Label(EDIT);
					editLabel.addStyleDependentName(GRID_LABEL_STYLE);
					//tableGrid.addColumn(tc1,EDIT);
					tableGrid.setWidget(rowIndex, colIndex, editLabel);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
				
					colIndex++;
					
					Label thirdLabel = new Label(cellValue);
					thirdLabel.addStyleDependentName(GRID_LABEL_STYLE);
					//tableGrid.addColumn(tc1,cellValue);
					tableGrid.setWidget(rowIndex, colIndex, thirdLabel);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
					
					colIndex++;
				} else if (colIndex == 0 && rowIndex != 0) { // Add buttons to first two columns
					Button ssDownload = new Button(SPREADSHEET_BUTTON_TEXT + cellValue);
					ssDownload.setTitle(cellValue);
					ssDownload.setStyleName(GRID_BUTTON_STYLE);
					ssDownload.addClickHandler(new SSDownloadClickHandler());
					//tableGrid.addColumn(btnColumn,"Select");
					tableGrid.setWidget(rowIndex, colIndex, ssDownload);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
					
					colIndex++;
					
					Image image = new Image(EDIT_ICON);
					PushButton editButton = new PushButton(image, new EditClickHandler());
					editButton.setTitle(String.valueOf(cellValue));
					tableGrid.setWidget(rowIndex, colIndex, editButton);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
				
					colIndex++;
				} else if (colIndex > 1) { // Handle other Cells and headers
					if (rowIndex == 0)
						tableGrid.resizeColumns(tableGrid.getColumnCount() + 1);
					
					Label label = new Label(cellValue);
					label.addStyleDependentName(GRID_LABEL_STYLE);
					
					// Set word wrap to false for cells but not headers
					if (rowIndex != 0)
						label.setWordWrap(false);
					
					//tableGrid.addColumn(tc1,cellValue);
					tableGrid.setWidget(rowIndex, colIndex, label);
					tableGrid.getCellFormatter().setHorizontalAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER);
					
					colIndex++;
				}
			}
			rowIndex++;
		}
		
		lowervp.setSpacing(10);

		lowervp.add(lowersp);
		lowersp.clear();
		
		hp4.clear();
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(prevButton);
		Label l = new Label("   ");
		vp1.add(l);
		vp1.add(nextButton);
		vp1.setCellVerticalAlignment(nextButton, HasVerticalAlignment.ALIGN_BOTTOM);
		vp1.setCellVerticalAlignment(l, HasVerticalAlignment.ALIGN_MIDDLE);
		vp1.setCellVerticalAlignment(prevButton, HasVerticalAlignment.ALIGN_TOP);
		hp4.add(vp1);
		hp4.add(tableGrid);
		lowersp.add(hp4);
		
		//owersp.add(tableGrid);
		lowersp.setHeight(SCROLL_PANEL_HEIGHT);
		lowersp.setWidth(SCROLL_PANEL_WIDTH);
		//lowervp.setCellHorizontalAlignment(tableGrid, HasHorizontalAlignment.ALIGN_CENTER);
		lowervp.setCellHorizontalAlignment(hp4, HasHorizontalAlignment.ALIGN_CENTER);
		
		lowervp.add(downloadButton);
		lowervp.setCellHorizontalAlignment(downloadButton, HasHorizontalAlignment.ALIGN_CENTER);
		
		lower.setVisible(true);
		
		lowervp.add(downloadForm);
		lowervp.add(ssDownloadForm);
		lowervp.add(pdfDownloadForm);
	}
	
	/**
	 * Private method get a configuration from the result grid.
	 * 
	 * @return NameValue
	 */
	private NameValue getSelectedConfig() {
		NameValue config = new NameValue();
		for (int i = 1; i < resultGrid.getRowCount(); i++) {
			RadioButton button = (RadioButton)resultGrid.getWidget(i, 0);
			if (button.getValue() == true) {
				config.setName(resultGrid.getText(i, 1));
				config.setValue(resultGrid.getText(i, 2));
			}
		}
		return config;
	}

	/**
	 * Private inner class to handle clicks in the edit button.
	 *  This allows the user to edit the request data
	 * 
	 * @author James Albersheim
	 *
	 */
	private class DownloadClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			//Window.alert("in DonwloadClickHandler-calling handleDownloadButtonClick");
			handleDownloadButtonClick();
		}
	}
	
	/**
	 * Private inner class to handle clicks in the edit button.
	 *  This allows the user to edit the request data
	 * 
	 * @author James Albersheim
	 *
	 */
	private static class EditClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			PushButton editButton = (PushButton)event.getSource();
			String ssID = editButton.getTitle();
			ModifySpreadSheetDialog modDialog = new ModifySpreadSheetDialog(ssID);					
			modDialog.show();
		}
	}
	
	/**
	 * Private inner class to handle clicks to the Spreadsheet X button.
	 * This causes the compositional analysis spreadsheet to be dowloaded.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class SSDownloadClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			String id = ((Button) event.getSource()).getTitle();
			ssa.getFileNameAndPath(id, new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {
					Window.alert("Download of data failed! Error: " + caught.getMessage());
				}

				public void onSuccess(List<String> result) {
					
					ssDownloadFilePath.setValue(result.get(0));
					ssDownloadFileName.setValue(result.get(1));
					ssDownloadForm.submit();
				}
			});
		}
	}
	
	/**
	 * A private inner ClickHandler class that gets the data
	 *  rows.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class ResultBoxClickHandler implements ClickHandler {

		//@Override
		public void onClick(ClickEvent event) {
			RadioButton button = (RadioButton)event.getSource();
			String wbConfig = "";
			String shConfig = "";
			for (int i = 1; i < resultGrid.getRowCount(); i++) {
				if (button == resultGrid.getWidget(i, 0)) {
					which = i;
					wbConfig = resultGrid.getText(i, 1);
					shConfig = resultGrid.getText(i, 2);
				}
			}
			getDataRows(trioList, wbConfig, shConfig);
		}
	}

	/**
	 * Private method to create and set up the form for downloading the Excel
	 * file returned from a search.
	 */
	private void setUpSSDownloadForm() {
		String url = GWT.getModuleBaseURL() + URL_CLIENT_DOWNLOAD_SERVICE;
		ssDownloadForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		ssDownloadForm.setMethod(FormPanel.METHOD_GET);
		ssDownloadForm.setAction(url);

		VerticalPanel downloadPanel = new VerticalPanel();
		ssDownloadForm.setWidget(downloadPanel);

		ssDownloadFilePath.setName(SS_FILE_PATH);
		ssDownloadFileName.setName(SS_FILE_NAME);

		downloadPanel.add(ssDownloadFilePath);
		downloadPanel.add(ssDownloadFileName);
	}

	/**
	 * public method to configure the message displayed while waiting
	 * on search results.
	 */
	public void pleaseWait() {
		lowervp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lowervp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label waitLabel = new Label(WAIT_MESSAGE);
		lowervp.add(waitLabel);
		Image hourGlass = new Image(WAIT_JPG);
		lowervp.add(hourGlass);
	}

	/**
	 * Private method to create and set up the form for downloading the Excel
	 * file returned from a search.
	 */
	private void setUpPdfDownloadForm() {
		String url = GWT.getModuleBaseURL() + URL_PDF_DOWNLOAD_SERVICE;
		pdfDownloadForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		pdfDownloadForm.setMethod(FormPanel.METHOD_GET);
		pdfDownloadForm.setAction(url);

		VerticalPanel downloadPanel = new VerticalPanel();
		pdfDownloadForm.setWidget(downloadPanel);

		trbPage.setName("trbPage");
		trbNum.setName("trbNum");

		downloadPanel.add(trbPage);
		downloadPanel.add(trbNum);
	}

	/**
	 * Private method to create and set up the form for downloading the Excel
	 * file returned from a search.
	 */
	private void setUpDownloadForm() {
		String url = GWT.getModuleBaseURL() + URL_CLIENT_DOWNLOAD_SERVICE;
		downloadForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		downloadForm.setMethod(FormPanel.METHOD_GET);
		downloadForm.setAction(url);

		VerticalPanel downloadPanel = new VerticalPanel();
		downloadForm.setWidget(downloadPanel);

		downloadPath.setName(SS_FILE_PATH);
		downloadFile.setName(SS_FILE_NAME);

		downloadPanel.add(downloadPath);
		downloadPanel.add(downloadFile);
	}

	/**
	 * Private inner class to handle a change to the heading
	 * pick-list. This causes the proper operators and appropriate
	 * value widget to be displayed.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class HdrChangeHandler implements ChangeHandler {

		public void onChange(ChangeEvent event) {
			final ListBox tempBox = (ListBox) event.getSource();
			int row = 0;
			for (int r = 1; r < criteriaGrid.getRowCount(); r++)
				if (tempBox == criteriaGrid.getWidget(r, 0))
					row = r;
			if (tempBox.getSelectedIndex() == 0) {
				criteriaGrid.clearCell(row, 1);
				criteriaGrid.clearCell(row, 2);
			} else {
				final String selection = tempBox.getItemText(tempBox
						.getSelectedIndex());
				ssa.getOperators(workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex()),
						selection, new AsyncCallback<List<String>>() {

					public void onFailure(Throwable caught) {
						Window.alert("Operators failed to load. Error: " + caught.getMessage());
					}

					public void onSuccess(List<String> result) {
						if (result != null) {
							// based on header selection, get operators from DB
							ListBox oper = new ListBox();
							int row = 0;
							for (int r = 1; r < criteriaGrid.getRowCount(); r++)
								if (tempBox == criteriaGrid.getWidget(r, 0)) {
									row = r;
								}

							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String op = it.next();
								oper.addItem(op);
							}

							criteriaGrid.setWidget(row, 1, oper);

							Widget widget = critWidgetMap.get(selection).get(
									row - 1);
							String widgetName = widget.getClass().getName();
							if (widgetName.endsWith(DATE_BOX)) {
								DateBox dateBox = (DateBox) widget;
								Date date = new Date();
								dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(DATE_FORMAT_DISPLAY)));
								dateBox.setValue(date);
							} else if (widgetName.endsWith(TEXT_BOX)) {
								FilteredTextBox textBox = (FilteredTextBox) widget;
								textBox.setValue(EMPTY_STRING);
							} else if (widgetName.endsWith(FILTERED_SUGGEST_BOX)) {
								final MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();
								/*
								ssa.getStringValues(workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex()),
										selection, new AsyncCallback<List<String>>() {
											public void onFailure(Throwable caught) {
												// Do not report
											}
			
											public void onSuccess(List<String> result) {
												if ((result != null) && (!result.isEmpty()))
													if (suggestOracle != null) {
														Iterator<String> rit = result.iterator();
														while (rit.hasNext()) {
															String r = rit.next();
															if (r != null) 
																suggestOracle.add(r);
														}
													}
											}
										});
								*/
								widget = new FilteredSuggestBox(suggestOracle);
								((FilteredSuggestBox)widget).setValue(EMPTY_STRING);
							}
							criteriaGrid.setWidget(row, 2, widget);
						}
					}
				});
			}
		}
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

	//@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		//Window.alert("in onClick");
		if (sender == submitButton) {
			handleSubmitButtonClick();
		} else if (sender == clearButton) {
			handleClearButtonClick();
		} else if (sender == downloadButton) {
			Window.alert("calling handleDownloadButtonClick");
			handleDownloadButtonClick();
		} else if (sender == nextButton) {
			handleUpDownButtonClick(1);
		} else if (sender == prevButton) {
			handleUpDownButtonClick(-1);
		}
	}

	/**
	 * Private method to handle the download button.
	 *  This method calls the server to download the selected
	 *  item.
	 */
	private void handleDownloadButtonClick() {
		downloadDialog.show();
		NameValue config = getSelectedConfig();
		ssa.downloadSelect(config.getName(), config.getValue(), trioList, new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				downloadDialog.hide();
				Window.alert("Download of data failed! Error: " + caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				downloadFile.setValue(result.get(0));
				downloadPath.setValue(result.get(1));
				downloadForm.submit();
				downloadDialog.hide();
			}
		});
	}

	/**
	 * Private method to handle the clear button.
	 *  This method clears the search criteria and resets
	 *   the picklists.
	 */
	private void handleClearButtonClick() {
		which = 1;
		trioList.clear();
		resultGrid.resizeRows(0);//setRowCount(0);//resizeRows(0);
		resultGrid.setVisible(false);
		
		workbookConfigBox.setSelectedIndex(0);
		
		worksheetConfigBox.setSelectedIndex(0);
		
		workbookIdBox.setValue("");

		attachmentExtBox.setItemSelected(0, true);

		// Reset headers and operators
		for (int row = 1; row <= NUM_TRIOS; row++) {
			// Header
			ListBox listBox = (ListBox) criteriaGrid.getWidget(row, 0);
			if (listBox != null)
				listBox.setItemSelected(0, true);

			// Operator
			criteriaGrid.clearCell(row, 1);

			// Values
			criteriaGrid.clearCell(row, 2);
		}

		lowervp.clear();
		Label defaultLabel = new Label(DEFAULT_TABLE_MESSAGE);
		lowervp.add(defaultLabel);
	}

	/**
	 * Private method to handle the submit button.
	 *  This method pulls the values from the widgets and submits
	 *  a call to the servlet for processing.
	 */
	private void handleUpDownButtonClick(int plusMinus) {
		//resultGrid.setVisible(false);
		trioList.clear();
		
		// Add worksheet ID if present
		if (!workbookIdBox.getValue().isEmpty() && !workbookIdBox.getValue().equals("0")) {
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			trio.setHeader(WORKBOOK_ID);
			trio.setOperator(EQUAL_STRING);
			trio.setValue(workbookIdBox.getValue());
			trioList.add(trio);
		}
		
		// Add attachment extension if present
		if (attachmentExtBox.getItemCount()>0 && attachmentExtBox.getSelectedIndex() > 0) {
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			trio.setHeader(ATTACHMENT_EXT);
			trio.setOperator(EQUAL_STRING);
			trio.setValue(attachmentExtBox.getItemText(attachmentExtBox.getSelectedIndex()));
			trioList.add(trio);
		}
		
		// Add other meta data
		for (int row = 1; row < criteriaGrid.getRowCount(); row++) {
			// get header
			ListBox headerBox = (ListBox) criteriaGrid.getWidget(row, 0);
			int idx = headerBox.getSelectedIndex();
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			if (idx > 0) {
				String header = headerBox.getValue(idx);
				// get operator
				ListBox operatorBox = (ListBox) criteriaGrid.getWidget(row, 1);
				int idx1 = operatorBox.getSelectedIndex();
				// get value
				trio.setOperator(operatorBox.getValue(idx1));
				trio.setHeader(header);
				Widget widget = critWidgetMap.get(header).get(row - 1);
				String widgetName = widget.getClass().getName();
			    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
			    // prints 12/17/2007 in the default locale
			    //GWT.log(fmt.format(today), null);
				if (widgetName.endsWith(DATE_BOX)) {
					DateBox dateBox = (DateBox) criteriaGrid.getWidget(row, 2);
					//trio.setValue(DateTimeFormat.getShortDateFormat().format(dateBox.getValue()));
					trio.setValue(fmt.format(dateBox.getValue()));
				} else if (widgetName.endsWith(TEXT_BOX)) {
					FilteredTextBox textBox = (FilteredTextBox) criteriaGrid.getWidget(row, 2);
					trio.setValue(textBox.getValue());
				} else if (widgetName.endsWith(SUGGEST_BOX)) {
					FilteredSuggestBox valueBox = (FilteredSuggestBox) criteriaGrid.getWidget(row, 2);
					trio.setValue(valueBox.getValue());
				}
			}
			if (trio.getValue() != null && trio.getValue().length() > 0) {
				trioList.add(trio);
			}
		}
		if (trioList.size() > 0) {
			lowervp.clear();
			pleaseWait();
			performSelect(trioList,plusMinus);
		} else
			Window.alert(VALUE_MISSING_MESSAGE);
	}
	
	/**
	 * Private method to handle the submit button.
	 *  This method pulls the values from the widgets and submits
	 *  a call to the servlet for processing.
	 */
	private void handleSubmitButtonClick() {
		which = 1;
		resultGrid.setVisible(false);
		trioList.clear();
		
		// Add worksheet ID if present
		if (!workbookIdBox.getValue().isEmpty() && !workbookIdBox.getValue().equals("0")) {
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			trio.setHeader(WORKBOOK_ID);
			trio.setOperator(EQUAL_STRING);
			trio.setValue(workbookIdBox.getValue());
			trioList.add(trio);
		}
		
		// Add attachment extension if present
		if (attachmentExtBox.getItemCount()>0 && attachmentExtBox.getSelectedIndex() > 0) {
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			trio.setHeader(ATTACHMENT_EXT);
			trio.setOperator(EQUAL_STRING);
			trio.setValue(attachmentExtBox.getItemText(attachmentExtBox.getSelectedIndex()));
			trioList.add(trio);
		}
		
		// Add other meta data
		for (int row = 1; row < criteriaGrid.getRowCount(); row++) {
			// get header
			ListBox headerBox = (ListBox) criteriaGrid.getWidget(row, 0);
			int idx = headerBox.getSelectedIndex();
			CriteriaTrioDTO trio = new CriteriaTrioDTO();
			if (idx > 0) {
				String header = headerBox.getValue(idx);
				// get operator
				ListBox operatorBox = (ListBox) criteriaGrid.getWidget(row, 1);
				int idx1 = operatorBox.getSelectedIndex();
				// get value
				trio.setOperator(operatorBox.getValue(idx1));
				trio.setHeader(header);
				Widget widget = critWidgetMap.get(header).get(row - 1);
				String widgetName = widget.getClass().getName();
			    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
			    // prints 12/17/2007 in the default locale
			    //GWT.log(fmt.format(today), null);
				if (widgetName.endsWith(DATE_BOX)) {
					DateBox dateBox = (DateBox) criteriaGrid.getWidget(row, 2);
					//trio.setValue(DateTimeFormat.getShortDateFormat().format(dateBox.getValue()));
					trio.setValue(fmt.format(dateBox.getValue()));
				} else if (widgetName.endsWith(TEXT_BOX)) {
					FilteredTextBox textBox = (FilteredTextBox) criteriaGrid.getWidget(row, 2);
					trio.setValue(textBox.getValue());
				} else if (widgetName.endsWith(SUGGEST_BOX)) {
					FilteredSuggestBox valueBox = (FilteredSuggestBox) criteriaGrid.getWidget(row, 2);
					trio.setValue(valueBox.getValue());
				}
			}
			if (trio.getValue() != null && trio.getValue().length() > 0) {
				//boolean found = false;
				//Iterator<CriteriaTrioDTO> it = trioList.iterator();
				//while (it.hasNext()) {
				//	CriteriaTrioDTO crit = it.next();
					//if (crit.getHeader().equalsIgnoreCase(trio.getHeader())) {
					//	found = true;
					//	break;
					//}
				//}
			
				//if (!found)
					trioList.add(trio);
				//else {
				//	Window.alert("Multiple occurrences of the same header not allowed!");
				//	return;
				//}
			}
		}
		if (trioList.size() > 0) {
			lowervp.clear();
			pleaseWait();
			performSelect(trioList);
		} else
			Window.alert(VALUE_MISSING_MESSAGE);
	}

	//@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == workbookConfigBox) {
			handleWorkbookConfigListBoxChanges();
		} else if (sender == worksheetConfigBox) {
			handleWorksheetConfigListBoxChanges();
		} else if (sender == pageSize) {
			handlePageSizeChanges();
		}
	}

	private void handlePageSizeChanges() {
		String spage = pageSize.getItemText(pageSize.getSelectedIndex());
		if (spage != null && !spage.equals(EMPTY_STRING)) {
			try {
				page = Integer.parseInt(spage);
			} catch (Exception e) {}
			handleUpDownButtonClick(0);
		}
	}

	/**
	 * Private method to handle the worksheet configuration list box changes.
	 *  This method calls the server and loads the headings and values.
	 */
	private void handleWorksheetConfigListBoxChanges() {
		String configName = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		String sheetConfigName = worksheetConfigBox.getItemText(worksheetConfigBox.getSelectedIndex());
		if (sheetConfigName.isEmpty())
			sheetConfigName = null;
		ssa.getHeaders(configName, sheetConfigName, new AsyncCallback<List<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("There was a problem loading the configuration.\nError: " + caught.getLocalizedMessage());
			}

			//@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty()) {
					loadHeadingsAndValues(result);
				}
			}
		});
	}

	/**
	 * Private method to handle Workbook configuration list box changes.
	 *  This method calls the server and gets the heading and values for
	 *  the selected workbook configuration.
	 */
	private void handleWorkbookConfigListBoxChanges() {
		String configName = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		if (configName.isEmpty())
			configName = null;
		ssa.getHeaders(configName, null, new AsyncCallback<List<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("There was a problem loading the configuration.\nError: " + caught.getLocalizedMessage());
			}

			//@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty()) {
					loadHeadingsAndValues(result);
				}
			}
		});
		
		String wbConfig = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		if (wbConfig.isEmpty()) {
			wbConfig = null;
		}
		ssa.getSheetConfigs(wbConfig, new WorksheetConfigCallback());
	}
}
