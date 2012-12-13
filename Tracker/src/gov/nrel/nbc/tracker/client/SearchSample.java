package gov.nrel.nbc.tracker.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

/**
 * Panel to search for samples.
 * 
 * @author jalbersh
 *
 */
public class SearchSample extends Composite implements AppConstants, ClickHandler, ChangeHandler {

	private static final String DOWNLOAD_IN_PROGRESS = "Download in progress. Please wait...";
	private static final String SEARCH_IN_PROGRESS = "Search in progress. Please wait...";
	private static final String WAIT_JPG = "hourglass1.jpg";
	private static final String RESULTS_GRID_WIDTH = "750px";
	private static final String RESULTS_GRID_HEIGHT = "250px";
	private static final String SAMPLE_GRID_WIDTH = "750px";
	private static final String SAMPLE_GRID_HEIGHT = "230px";
	private static final String HORIZONTAL_RULE = "<hr/>";
	private static final String EXCELFILE = "excelfile";
	private static final String CLEAR_BUTTON_TEXT = "Clear";
	private static final String SEARCH_BUTTON_TEXT = "Search";
	private static final String PAGE_LABEL = "TRB Page";
	private static final String NUMBER_LABEL = "TRB Number";
	private static final String SHELF_LABEL = "Shelf";
	private static final String HOLDER_LABEL = "Holder";
	private static final String ROOM_LABEL = "Room";
	private static final String BUILDING_LABEL = "Building";
	private static final String STATUS_LABEL = "Status";
	private static final String FRACTION_LABEL = "Fraction";
	private static final String BIOMASS_LABEL = "Biomass Lot";
	private static final String FORM_LABEL = "Storage State";
	//private static final String PROJECT_TASK_LABEL = "Project Task No";
	private static final String DESTINATION_LABEL_SABC = "Destination";
	private static final String STRAIN_LABEL = "Strain";
	private static final String FEEDSTOCK_LABEL = "Feedstock";
	private static final String TREATMENT_LABEL = "Treatment";
	private static final String CUSTODIAN_NAME_LABEL = "Custodian Name";
	private static final String COMPOSITION_LABEL = "Composition";
	private static final String EXTERNAL_ID_LABEL = "External ID";
	private static final String DATE_CREATED_LABEL = "Date Created";
	private static final String AMOUNT_LABEL = "Approximate Amount";
	private static final String COMMENTS_LABEL = "Comments";
	private static final String DESCRIPTOR_LABEL = "Label Descriptor";
	private static final String SAMPLE_ID_LABEL = "Sample ID";
	private static final String TRACKING_ID_LABEL = "Tracking ID";
	private static final String SAMPLE_SEARCHING_LABEL = "Sample Searching";
	private static final int GRID_COLUMNS = 32;
	private static final int GRID_COLUMNS_SABC = 22;
	private static final String URL_TRACKER_SERVICE = "trackerService";
	private static final String SUBTITLE_STYLE = "SubTitle";
	private static final String URL_TRACKER_CLIENT_DOWNLOAD_SERVICE = "clientDownloadService";
	private static final String OWNER_LABEL = "Owner Name";
	private static final String SUBLOCATION_LABEL = "Sublocation";
	private static final String PACKAGING_LABEL = "Packaging";
	private static final String STORAGE_NOTES_LABEL = "Storage Notes";
	private static final String ORIGIN_LABEL = "Origin";
	private static final String UNITS_LABEL = "Units";
	private static final String ATTACHMENT_EXT_COLON = "Attachment Extension";
	private static final String SUGGEST_BOX = "SuggestBox";
	private static final String TEXT_BOX = "TextBox";
	private static final String DATE_BOX = "DateBox";
	private static final String EMPTY_STRING = "";
	private static final String[] operators = {">","<","=","!="};
	private static String[] headerList = {AMOUNT_LABEL,	BUILDING_LABEL, COMMENTS_LABEL,COMPOSITION_LABEL, CUSTODIAN_NAME_LABEL, DATE_CREATED_LABEL,	DESTINATION_LABEL_SABC, EXTERNAL_ID_LABEL,	 FEEDSTOCK_LABEL, FRACTION_LABEL,HOLDER_LABEL, DESCRIPTOR_LABEL,NUMBER_LABEL, ORIGIN_LABEL, OWNER_LABEL,PAGE_LABEL,		 ROOM_LABEL,	 	 SAMPLE_ID_LABEL,	 SHELF_LABEL,STATUS_LABEL,	 	 	 FORM_LABEL,	 	 	 STRAIN_LABEL,	 	 SUBLOCATION_LABEL,	 TREATMENT_LABEL,	 	 	   UNITS_LABEL, 	 	 TRACKING_ID_LABEL,	 	 PACKAGING_LABEL,	 STORAGE_NOTES_LABEL};
	private static final String[] algaeList = {	 AMOUNT_LABEL,BIOMASS_LABEL,	 COMPOSITION_LABEL,	 COMMENTS_LABEL,	 CUSTODIAN_NAME_LABEL,DATE_CREATED_LABEL,	 FRACTION_LABEL,ORIGIN_LABEL, OWNER_LABEL,PACKAGING_LABEL,SAMPLE_ID_LABEL,	 STATUS_LABEL,	FORM_LABEL,STRAIN_LABEL,	 TRACKING_ID_LABEL,	 	 PAGE_LABEL,	 NUMBER_LABEL,	   	 	 UNITS_LABEL};
	private static final String[] biomassList = {	  	 AMOUNT_LABEL,BUILDING_LABEL,COMMENTS_LABEL,	 CUSTODIAN_NAME_LABEL,	DATE_CREATED_LABEL,EXTERNAL_ID_LABEL,FEEDSTOCK_LABEL,FRACTION_LABEL,HOLDER_LABEL,DESCRIPTOR_LABEL,ORIGIN_LABEL ,OWNER_LABEL,	ROOM_LABEL,SAMPLE_ID_LABEL,	 SHELF_LABEL,	 	 	 STATUS_LABEL,	STORAGE_NOTES_LABEL,  	 	 SUBLOCATION_LABEL,	 	 TRACKING_ID_LABEL,	  PAGE_LABEL,	 NUMBER_LABEL,	 TREATMENT_LABEL,	  	 	 UNITS_LABEL};
	private static final String[] longHeaders = {PAGE_LABEL,	 NUMBER_LABEL, AMOUNT_LABEL,	TRACKING_ID_LABEL };
	//private static final String[] realHeaders = {};
	private static final String[] stringHeaders = {SHELF_LABEL,	 HOLDER_LABEL,	 ROOM_LABEL,	 BUILDING_LABEL,	 FRACTION_LABEL,	 BIOMASS_LABEL,	 FORM_LABEL,	 DESTINATION_LABEL_SABC,	 STRAIN_LABEL,	 FEEDSTOCK_LABEL,	 TREATMENT_LABEL,	 CUSTODIAN_NAME_LABEL,	 COMPOSITION_LABEL,	 EXTERNAL_ID_LABEL,COMMENTS_LABEL,	 DESCRIPTOR_LABEL,	 SAMPLE_ID_LABEL,  OWNER_LABEL,	 STATUS_LABEL, SUBLOCATION_LABEL,	 PACKAGING_LABEL,	 STORAGE_NOTES_LABEL,	 ORIGIN_LABEL, UNITS_LABEL};
	private static final String[] dateHeaders = {DATE_CREATED_LABEL};
	private int PAGE_SIZE = 5;
	private int position = 0;
	private int pageCount = 0;
	private int resultsCount = 0;
	private SearchSample ss = null;
	
	private final ListBox pageSize = new ListBox();
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
	 * String constant for the URL of the client download service
	 */
	private static final String URL_PDF_DOWNLOAD_SERVICE = "pdfDownloadService";

	/**
	 * Holder for TrackerService
	 */
	private TrackerServiceAsync tsa = null;
	
	/** 
	 * Units <ListBox>
	 */
	private final ListBox unitBox = new ListBox();
	
	/**
	 * Status <ListBox>
	 */
	private final ListBox statusBox = new ListBox();
	
	private final ListBox groupBox = new ListBox(true);

	/**
	 * Main panel <VerticalPanel>
	 */
	private final VerticalPanel mainPanel = new VerticalPanel();
	
	/**
	 * Sample ID <TextBox>
	 */
	private final FilteredNumberTextBox idBox = new FilteredNumberTextBox();

	/**
	 * Variable to hold the GWT FormPanel for containing the
	 * download web form.
	 */
	private final FormPanel downloadForm = new FormPanel();
	
	/**
	 * GWT Hidden variable to hold the name of the Excel file to download
	 * to the users workstation from a search.
	 */
	private final Hidden downloadFile = new Hidden();
	
	/**
	 * Results <Grid> from query
	 */
	private Grid resultsGrid = new Grid(0, GRID_COLUMNS);
	
	/**
	 * Download results <Button>
	 */
	private Button downloadButton = new Button("Download Results");

	private Button printAllButton = new Button("Print All Labels");
	/**
	 * Sample data <SampleCriteria>
	 */
	private SampleCriteria crit = new SampleCriteria();
	private List<SampleCriteria> crits = new ArrayList<SampleCriteria>();
	
	/**
	 * @return the critList
	 */
	public List<SampleCriteria> getCritList() {
		return crits;
	}

	/**
	 * @param critList the critList to set
	 */
	public void setCritList(List<SampleCriteria> crits) {
		this.crits = crits;
	}

	/**
	 * @return the resultsCount
	 */
	public int getResultsCount() {
		return resultsCount;
	}

	/**
	 * @param resultsCount the resultsCount to set
	 */
	public void setResultsCount(int resultsCount) {
		this.resultsCount = resultsCount;
	}

	/**
	 * @return the crit
	 */
	public SampleCriteria getCrit() {
		return crit;
	}

	/**
	 * @param crit the crit to set
	 */
	public void setCrit(SampleCriteria crit) {
		this.crit = crit;
	}

	/**
	 * <ScrollPanel> for the search results
	 */
	private ScrollPanel resultsScrollPanel = new ScrollPanel();
	
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
	 * <ListBox> for label printers
	 */
	private final ListBox printerBox = new ListBox();

	private PopupPanel downloadDialog = new PopupPanel();
	private PopupPanel searchDialog = new PopupPanel();
	private final ScrollPanel sampleScrollPanel = new ScrollPanel();
	private final HorizontalPanel printerPanel = new HorizontalPanel();
    private String sessionId = "";
    private String group = "";
    private final Hidden sabcPrinterHidden = new Hidden();
	private static final String FILTERED_SUGGEST_BOX = "FilteredSuggestBox";
	private final Label pageOf = new Label("");

	/**
	 * A mapping table that holds the widgets to display for any selected heading
	 */
	private HashMap<String, List<Widget>> critWidgetMap = new HashMap<String, List<Widget>>();

	/**
	 * A list containing the header for a user to choose from.
	 */
	private ArrayList<ListBox> headers = new ArrayList<ListBox>();

	private final Label hdrLabel = new Label(HEADING);
	private final Label opLabel = new Label(OPERATOR);
	private final Label valLabel = new Label(VALUE);
	private final Button nextButton = new Button("Next Page");
	private final Button prevButton = new Button("Previous Page");
	private final FilteredNumberTextBox gotoPage = new FilteredNumberTextBox();
	private final Button gotoPageBtn = new Button("Go To Page");
	private final HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel pagePnl = new HorizontalPanel();

	/**
	 * Maximum number of search criteria a user may search.
	 */
	private static final int NUM_TRIOS = 5;
	/**
	 * A container for the search criteria
	 */
	private Grid criteriaGrid = new Grid(NUM_TRIOS+1, 3);

	/**
	 * Simple constructor.
	 */
	public SearchSample() {
		
		initWidget(mainPanel);
				
		initServices();
		
		final VerticalPanel superPanel = new VerticalPanel();
		superPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		Label titleLabel = new Label(SAMPLE_SEARCHING_LABEL);
		titleLabel.addStyleDependentName(SUBTITLE_STYLE);
		titleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		superPanel.add(titleLabel);

		final FlexTable samplePanel = new FlexTable();
		sampleScrollPanel.setWidth(SAMPLE_GRID_WIDTH);
		sampleScrollPanel.setHeight(SAMPLE_GRID_HEIGHT);
		sampleScrollPanel.add(samplePanel);
		superPanel.add(sampleScrollPanel);
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE))
			headerList = biomassList;
		else
			headerList = algaeList;
		
		int row = 0;

		nextButton.addClickHandler(this);
		prevButton.addClickHandler(this);
		gotoPageBtn.addClickHandler(this);
		Label pageSizeLbl = new Label("Page size: ");
		pageSizeLbl.setStyleName("Black");
		pagePnl.add(pageSizeLbl);
		pagePnl.add(pageSize);
		getPageSizes();
		pageSize.addChangeHandler(this);

		/* TODO - add back in when security is in place
		final HorizontalPanel hp4 = new HorizontalPanel();
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result2) {
				//Window.alert("From server:session="+result);
				sessionId = result2;
				tsa.getGroupsForUser(sessionId, new AsyncCallback<List<String>> () {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub						
					}
					@Override
					public void onSuccess(List<String> result) {
						if (result.size()>1) {
							Label groupLabel = new Label("Groups: ");
							hp4.add(groupLabel);
							Iterator<String> git = result.iterator();
							while (git.hasNext()) {
								String group = git.next();
								groupBox.addItem(group);
							}
							hp4.add(groupBox);
						}
						//Window.alert("found "+result.size()+" groups");
					}
				});
			}
		});
		samplePanel.setWidget(row++, 1, hp4);
		*/
		
		final int rowf = row;
		ss = this;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result2) {
				//Window.alert("From server:session="+result);
				sessionId = result2;
			    tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-6");
					}

					@Override
					public void onSuccess(String result1) {
						//Window.alert("got group="+group);
						group = result1;
						int row1 = rowf;
						
						HTML ruler4Label = new HTML(HORIZONTAL_RULE, false);

						criteriaGrid.setWidget(0, 0, hdrLabel);
						criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
						criteriaGrid.setWidget(0, 1, opLabel);
						opLabel.setVisible(false);
						criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
						criteriaGrid.setWidget(0, 2, valLabel);
						valLabel.setVisible(false);
						criteriaGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);

						samplePanel.setWidget(row1, 0, ruler4Label);
						samplePanel.getFlexCellFormatter().setColSpan(row1++, 0, 3);
						
						// get headers from DB
						for (int i = 0; i < NUM_TRIOS; i++) {
							ListBox hdr = new ListBox();
							hdr.addChangeHandler(new HdrChangeHandler());
							headers.add(hdr);
							criteriaGrid.setWidget(1 + i, 0, hdr);
						}
						loadHeadingsAndValues();

						samplePanel.setWidget(row1++, 0, criteriaGrid);

						samplePanel.setWidget(row1++, 0, pagePnl);

						HTML ruler5Label = new HTML(HORIZONTAL_RULE, false);
						samplePanel.setWidget(row1, 0, ruler5Label);
						samplePanel.getFlexCellFormatter().setColSpan(row1++, 0, 3);

						Button submitButton = new Button(SEARCH_BUTTON_TEXT);
						submitButton.addClickHandler(new SearchClickHander());
						superPanel.add(submitButton);
						superPanel.setCellHorizontalAlignment(submitButton, HasHorizontalAlignment.ALIGN_CENTER);

						Button clearButton = new Button(CLEAR_BUTTON_TEXT);
						clearButton.addClickHandler(new ClearClickHander());
						superPanel.add(clearButton);
						superPanel.setCellHorizontalAlignment(clearButton, HasHorizontalAlignment.ALIGN_CENTER);
						
						// Printer listbox
						HTML ruler6Label = new HTML(HORIZONTAL_RULE, false);
						superPanel.add(ruler6Label);
						
						Label printerLabel = new Label("Select a label printer: ");
						printerPanel.add(printerLabel);
						printerPanel.add(printerBox);
						Label noteLabel = new Label("Note: Printers missing from list are unavailable for printing.");
						printerPanel.add(noteLabel);
						superPanel.add(printerPanel);
						
						if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE) || PRINT_MODE.equals(ALGAE)) {
							HTML ruler7Label = new HTML(HORIZONTAL_RULE, false);
							superPanel.add(ruler7Label);
						}
						
						// Results section
						superPanel.add(pageOf);
						buttonPanel.add(prevButton);
						buttonPanel.add(nextButton);
						buttonPanel.add(gotoPage);
						buttonPanel.add(gotoPageBtn);
						superPanel.add(buttonPanel);
						buttonPanel.setVisible(false);
						resultsScrollPanel.setVisible(false);
						resultsScrollPanel.setWidth(RESULTS_GRID_WIDTH);
						resultsScrollPanel.setHeight(RESULTS_GRID_HEIGHT);
						superPanel.add(resultsScrollPanel);
						resultsScrollPanel.add(resultsGrid);
						resultsGrid.setBorderWidth(1);
						
						superPanel.add(downloadButton);
						downloadButton.setVisible(false);
						downloadButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								dowloadData();
							}			
						});

						superPanel.add(printAllButton);
						printAllButton.setVisible(false);
						printAllButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
									PrintAllDialog dialog = new PrintAllDialog(ss,resultsCount);
									dialog.show();
							}			
						});

						mainPanel.add(superPanel);
						
						setUpDownloadForm();
						setUpPdfDownloadForm();
						
						mainPanel.add(downloadForm);
						VerticalPanel vpanel1 = new VerticalPanel();
						vpanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						Label waitLabel1 = new Label(DOWNLOAD_IN_PROGRESS);
						vpanel1.add(waitLabel1);
						Image hourGlass = new Image(WAIT_JPG);
						vpanel1.add(hourGlass);
						downloadDialog.add(vpanel1);
						downloadDialog.setPopupPositionAndShow(new PositionCallback() {
							public void setPosition(int offsetWidth, int offsetHeight) {
								int left = ((Window.getClientWidth() - offsetWidth) / 2) - 100;
								int top = ((Window.getClientWidth() - offsetWidth) / 2) - 100;
								if (top < 0)
									top = 0;
								if (left < 0)
									left = 0;
								downloadDialog.setPopupPosition(left, top);
								downloadDialog.hide();
							}			
						});
						VerticalPanel vpanel2 = new VerticalPanel();
						vpanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						Label waitLabel2 = new Label(SEARCH_IN_PROGRESS);
						hourGlass = new Image(WAIT_JPG);
						vpanel2.add(waitLabel2);
						vpanel2.add(hourGlass);
						searchDialog.add(vpanel2);
						searchDialog.setPopupPositionAndShow(new PositionCallback() {
							public void setPosition(int offsetWidth, int offsetHeight) {
								int left = ((Window.getClientWidth() - offsetWidth) / 2) - 100;
								int top = ((Window.getClientWidth() - offsetWidth) / 2) - 100;
								if (top < 0)
									top = 0;
								if (left < 0)
									left = 0;
								searchDialog.setPopupPosition(left, top);
								searchDialog.hide();
							}			
						});
					}

			    });
			}
		});

		downloadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) { 
				downloadDialog.hide();
			}
		});
		mainPanel.add(this.pdfDownloadForm);
		
		printerPanel.setVisible(false);
		printerPanel.clear();
		
		if (DevTestProdConstants.SECURITY_MODE==DevTestProdConstants.ON) {
			tsa.getSessionId(new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("tsa.getSessionId failed.");
				}
	
				@Override
				public void onSuccess(String result) {
					//Window.alert("From server:session="+result);
					sessionId = result;
					tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
	
						@Override
						public void onFailure(Throwable caught) {
							//Window.alert("Shouldn't get to here");
						}
	
						@Override
						public void onSuccess(Boolean result) {
							if (!result) {
								printerPanel.setVisible(false);
								printerPanel.clear();
								printerPanel.add(new Label("Label printing is not permitted."));
								printerPanel.setVisible(true);
							} else {
								loadPrinters();
								//Window.alert("Loading Printers.");
							}
						}
						
					});
				}
			});
		} else {	
			loadPrinters();
			printerPanel.setVisible(true);
		}
	}

	private void handlePageSizeChanges() {
		String spage = pageSize.getItemText(pageSize.getSelectedIndex());
		if (spage != null && !spage.equals(EMPTY_STRING)) {
			try {
				PAGE_SIZE = Integer.parseInt(spage);
			} catch (Exception e) {}
			SearchSamples(DevTestProdConstants.DISPLAY_MODE, JUNIT_SESSION_ID);
			//handleUpDownButtonClick(0);
		}
	}

	//@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == pageSize) {
			handlePageSizeChanges();
		}
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

	private boolean inList(String[] list, String hdr) {
		int i=0;
		while (i<list.length) {
			if (list[i++].equals(hdr)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Private method to load headings and create default
	 * widgets for values
	 */
	private void loadHeadingsAndValues() {
		// clear values first
		// Reset headers and operators
		for (int row = 1; row <= NUM_TRIOS; row++) {
			// Operator
			criteriaGrid.clearCell(row, 1);

			// Values
			criteriaGrid.clearCell(row, 2);
		}
		critWidgetMap.clear();
		//if (headerTypes != null) {
			Iterator<ListBox> it = headers.iterator();
			while (it.hasNext()) {
				ListBox listBox = it.next();
				listBox.clear();
				listBox.addItem(EMPTY_STRING);
				int hdr = 0;
				while (hdr < headerList.length) {
					String headerName = headerList[hdr++];
					long type = 0;
					if (inList(longHeaders,headerName))
						type = 1;
					else if (inList(stringHeaders,headerName))
						type = 4;
					else if (inList(dateHeaders,headerName))
						type = 3;
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
						suggestOracle.add("true");
						suggestOracle.add("false");
						for (int i = 0; i < NUM_TRIOS; i++) {
							widgets.add(new FilteredSuggestBox(suggestOracle));
						}
					} else if (type == 4) { // String
						final MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();
						loadValues(headerName,suggestOracle);
						for (int i = 0; i < NUM_TRIOS; i++) {
							widgets.add(new FilteredSuggestBox(suggestOracle));
						}
					}
					critWidgetMap.put(headerName, widgets);
				}
			}
		//}
	}
	
	private void loadValues(String headerName, MultiWordSuggestOracle suggestOracle) {
		if (headerName.equals(SearchSample.SAMPLE_ID_LABEL))
			loadSampleIds(suggestOracle);
		else if (headerName.equals(SearchSample.OWNER_LABEL))
			loadOwnerNames(suggestOracle);
		else if (headerName.equals(SearchSample.CUSTODIAN_NAME_LABEL))
			loadCustodians(suggestOracle);
		else if (headerName.equals(SearchSample.ORIGIN_LABEL))
			loadOrigins(suggestOracle);
		else if (headerName.equals(SearchSample.STATUS_LABEL))
			loadStatuses(suggestOracle);
		else if (headerName.equals(SearchSample.FEEDSTOCK_LABEL))
			loadFeedstocks(suggestOracle);
		else if (headerName.equals(SearchSample.TREATMENT_LABEL))
			loadTreatments(suggestOracle);
		else if (headerName.equals(SearchSample.BIOMASS_LABEL))
			loadBiomasses(suggestOracle);
		else if (headerName.equals(SearchSample.FORM_LABEL))
			loadForms(suggestOracle);
		else if (headerName.equals(SearchSample.STRAIN_LABEL))
			loadStrains(suggestOracle);
		//else if (headerName.equals(SearchSample.PROJECT_TASK_LABEL)||
		else if (headerName.equals(SearchSample.DESTINATION_LABEL_SABC))
			loadDestinations(suggestOracle);
		else if (headerName.equals(SearchSample.COMPOSITION_LABEL))
			loadCompositions(suggestOracle);
		else if (headerName.equals(SearchSample.FRACTION_LABEL))
			loadFractions(suggestOracle);
		else if (headerName.equals(SearchSample.UNITS_LABEL))
			loadUnits(suggestOracle);
		//else if (headerName.equals(SearchSample.EXTERNAL_ID_LABEL))
		//	this.lo
		else if (headerName.equals(SearchSample.BUILDING_LABEL))
			loadBuildings(suggestOracle);
		else if (headerName.equals(SearchSample.ROOM_LABEL))
			loadRooms(suggestOracle);
		else if (headerName.equals(SearchSample.SUBLOCATION_LABEL))
			loadSubLocations(suggestOracle);
		else if (headerName.equals(SearchSample.SHELF_LABEL))
			loadShelves(suggestOracle);
		else if (headerName.equals(SearchSample.HOLDER_LABEL))
			loadHolders(suggestOracle);
		else if (headerName.equals(SearchSample.PACKAGING_LABEL))
			loadPackaging(suggestOracle);
		else if (headerName.equals(SearchSample.ATTACHMENT_EXT_COLON))
			loadAttachmentExtensions(suggestOracle);		
	}

	///*
	private void loadCompositions(final MultiWordSuggestOracle suggestOracle) {
		tsa.getCompositions(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					suggestOracle.clear();
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							suggestOracle.add(suggestion);
					}
				}
			}			
		});		
	}
	//*/
	
	private void loadStrains(final MultiWordSuggestOracle suggestOracle) {
		tsa.getStrains(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					suggestOracle.clear();
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							suggestOracle.add(suggestion);
							//strainsListBox.addItem(suggestion);
					}
				}
			}			
		});		
	}
	
	private void loadDestinations(final MultiWordSuggestOracle suggestOracle) {
		tsa.getDestinations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					suggestOracle.clear();
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							suggestOracle.add(suggestion);
					}
				}
			}			
		});		
	}
	
	/**
	 * Private method to load the form list box.
	 */
	private void loadForms(final MultiWordSuggestOracle suggestOracle) {
		tsa.getForms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					suggestOracle.clear();
					int ctr=0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							suggestOracle.add(name);
						}
						ctr++;
					}
				} else {
					//Window.alert("There are no treatments loaded.\nCheck with administrator.");
				}
			}			
		});		
	}

	/**
	 * Private method to load the biomass list box.
	 */
	private void loadBiomasses(final MultiWordSuggestOracle suggestOracle) {
		tsa.getBiomasses(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Biomasses failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					suggestOracle.clear();
					int ctr=0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							suggestOracle.add(name);
						}
						ctr++;
					}
				} else {
					//Window.alert("There are no biomasses loaded.\nCheck with administrator.");
				}
			}			
		});		
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
				final String selection = tempBox.getItemText(tempBox.getSelectedIndex());
				opLabel.setVisible(true);
				valLabel.setVisible(true);
				//tsa.getOperators(selection, new AsyncCallback<List<String>>() {

				//	public void onFailure(Throwable caught) {
				//		Window.alert("Operators failed to load. Error: " + caught.getMessage());
				//	}

				//	public void onSuccess(List<String> result) {
						if (operators != null) {
							// based on header selection, get operators from DB
							ListBox oper = new ListBox();
							//int row = 0;
							for (int r = 1; r < criteriaGrid.getRowCount(); r++)
								if (tempBox == criteriaGrid.getWidget(r, 0)) {
									row = r;
								}

							int i = 0;
							while (i<operators.length) {
								String op = operators[i++];
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
								widget = new FilteredSuggestBox(suggestOracle);
								loadValues(selection,suggestOracle);
								((FilteredSuggestBox)widget).setValue(EMPTY_STRING);
							}
							criteriaGrid.setWidget(row, 2, widget);
						}
					//}
				//});
			}
		}
	}

	/**
	 * Private method to get list of label printers.
	 * 
	**/
	private void loadPrinters() {
		tsa.getPrinterList(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("There was a problem finding printers.\nYou will not be able to print labels.");
			}

			@Override
			public void onSuccess(List<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					printerBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						printerBox.addItem(it.next());
						//Window.alert("Printer added: "+printerBox.getItemText(printerBox.getItemCount() - 1));
					}
					sabcPrinterHidden.setValue(printerBox.getItemText(1));
				} else {
					if (!DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST)) {
						Window.alert("No printers were found.\nYou will not be able to print labels.");
					}
				}
			}			
		});
	}

	/**
	 * Protected method to download the query results
	 */
	protected void dowloadData() {
		downloadDialog.show();
		String id = Cookies.getCookie("SessionID");

		tsa.getDataFile(crits, id, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				downloadDialog.hide();
				Window.alert("The data failed to export. Error: " + caught.getMessage());
			}

			public void onSuccess(String result) {
				downloadFile.setValue(result);
				downloadForm.submit();
				downloadDialog.hide();
			}			
		});
	}

	/**
	 * public method to print the query results
	 */
	public void printAllData(List<SampleCriteria> criterias, final int results) {
		String id = Cookies.getCookie("SessionID");

		String printerName = "";
		//if (PRINT_MODE.equals(ALGAE)) {
			printerName = printerBox.getItemText(printerBox.getSelectedIndex());
		//}
		//else {
		//	if (!sabcPrinterHidden.getValue().isEmpty()) {
		//		printerName = sabcPrinterHidden.getValue();
		//	}
		//}
		if (printerName != null && !printerName.isEmpty()) {
		
		tsa.printAllLabels(criterias, id, printerName, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				//Window.alert("The data failed to export. Error: " + caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				Window.alert("All "+results+" labels queued for printing");
			}			
		});
		} else {
			Window.alert("No printer selected. Please select a printer.");
		}
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

		trbPage.setName(TRB_PAGE);
		trbNum.setName(TRB_NUM);

		downloadPanel.add(trbPage);
		downloadPanel.add(trbNum);
	}

	/**
	 * Private method to create and set up the form for downloading
	 * the Excel file returned from a search.
	 */
	private void setUpDownloadForm() {
		String url = GWT.getModuleBaseURL() + URL_TRACKER_CLIENT_DOWNLOAD_SERVICE;
		downloadForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		downloadForm.setMethod(FormPanel.METHOD_GET);
		downloadForm.setAction(url);
		
		VerticalPanel downloadPanel = new VerticalPanel();
	    downloadForm.setWidget(downloadPanel);
	    
	    downloadFile.setName(EXCELFILE);
	    
	    downloadPanel.add(downloadFile);
	}

	/**
	 * Private <KeyDownHandler> to handle the enter key for searching
	 * 
	 * @author jalbersh
	 *
	 */
	/*
	private class SearchKeyDownHandler implements KeyDownHandler {

		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				tsa.getSessionId(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-5");
					}

					@Override
					public void onSuccess(String result2) {
						//Window.alert("From server:session="+result);
						sessionId = result2;
					    tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								//Window.alert("Shouldn't get to here-6");
							}

							@Override
							public void onSuccess(String result1) {
								//Window.alert("got group="+group);
								group = result1;
								SearchSamples(group, sessionId);
							}
					    });
					}
				});
		}		
	}
	*/
	/**
	 * Private <ClickHandler> to handle clicks in the results <Grid>.
	 *  This allows the user to edit the sample data
	 * 
	 * @author jalbersh
	 *
	 */
	private class EditClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			sampleScrollPanel.setVisible(false);
			PushButton editButton = (PushButton)event.getSource();
			String sampleID = editButton.getTitle();
			ModifySampleDialog modDialog = new ModifySampleDialog(sampleID);
			
			modDialog.show();
			
			sampleScrollPanel.setVisible(true);
		}
	}
	
	/**
	 * Private <ClickHandler> to handle clicks in the results <Grid>.
	 *  This allows the user to edit the sample data
	 * 
	 * @author jalbersh
	 *
	 */
	private class EditLabelClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			PushButton editButton = (PushButton)event.getSource();
			String sampleID = editButton.getTitle();
			ModifyLabelDialog modDialog = new ModifyLabelDialog(sampleID);
			//ModifySampleDialog modDialog = new ModifySampleDialog(sampleID);
			
			modDialog.show();
		}
	}
	
	/**
	 * Private <ClickHandler> that handles the search <Button>.
	 * 
	 * @author jalbersh
	 *
	 */
	private class SearchClickHander implements ClickHandler {

		public void onClick(ClickEvent event) {
			if (validForm()) {
				/*
				tsa.getSessionId(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-5");
					}

					@Override
					public void onSuccess(String result2) {
						//Window.alert("From server:session="+result);
						sessionId = result2;
					    tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								//Window.alert("Shouldn't get to here-6");
							}

							@Override
							public void onSuccess(String result1) {
								//Window.alert("got group="+group);
								group = result1;
								SearchSamples(group, sessionId);
							}
					    });
					}
				});
				*/
				SearchSamples(DevTestProdConstants.DISPLAY_MODE, JUNIT_SESSION_ID);
			}
		}

		private boolean validForm() {
			return true;
		}		
	}
	
	/**
	 * Private <ClickHandler> to handle the clear <Button>
	 * 
	 * @author jalbersh
	 *
	 */
	private class ClearClickHander implements ClickHandler {

		//private static final String NULL_STRING = "";

		public void onClick(ClickEvent event) {
			for (int row = 1; row <= NUM_TRIOS; row++) {
				// Header
				ListBox box = (ListBox)criteriaGrid.getWidget(row, 0);
				int cnt = box.getItemCount();
				for (int i=0;i<cnt;i++) 
					box.setItemSelected(i, false);
				
				// Operator
				criteriaGrid.clearCell(row, 1);

				// Values
				criteriaGrid.clearCell(row, 2);
			}
			/*
			unitBox.setSelectedIndex(0);
			statusBox.setSelectedIndex(0);
			feedstockBox.setValue(NULL_STRING);
			fractionBox.setSelectedIndex(0);
			if (treatmentBox != null)
				treatmentBox.setSelectedIndex(0);
			originListBox.setSelectedIndex(0);
			attachmentExtBox.setSelectedIndex(0);
			idBox.setValue(NULL_STRING);
			sampleIdBox.setValue(NULL_STRING);
			commentBox.setValue(NULL_STRING);
			descriptorBox.setValue(NULL_STRING);
			amountBox.setValue(NULL_STRING);
			externalIdBox.setValue(NULL_STRING);
			custodianBox.setValue(NULL_STRING);
			ownerBox.setValue(NULL_STRING);
			buildingBox.setValue(NULL_STRING);
			roomBox.setValue(NULL_STRING);
			holderBox.setValue(NULL_STRING);
			shelfBox.setValue(NULL_STRING);
			packagingBox.setValue(NULL_STRING);
			subLocationBox.setValue(NULL_STRING);
			storageNotesBox.setValue(NULL_STRING);
			numBox.setValue(NULL_STRING);
			pageBox.setValue(NULL_STRING);
			formBox.setSelectedIndex(0);
			destinationBox.setSelectedIndex(0);
			biomassBox.setSelectedIndex(0);
			createFromDateBox.getTextBox().setValue(NULL_STRING);
			createToDateBox.getTextBox().setValue(NULL_STRING);
			*/
			// Clear out results
			//resultsGrid.resizeRows(0);
			opLabel.setVisible(false);
			valLabel.setVisible(false);
			pageOf.setText("");
			gotoPage.setText("");;
			resultsCount = 0;
			position = 0;
			pageCount = 0;
			resultsGrid.setVisible(false);
			buttonPanel.setVisible(false);
			printAllButton.setVisible(false);
			downloadButton.setVisible(false);
		}		
	}

	/**
	 * Private method to initiate the search for samples.
	 */
	private void SearchSamples(String group, String id) {
		searchDialog.show();
		gotoPage.setText("");
		position=0;
		pageCount=0;
		resultsCount=0;
		crits = new ArrayList<SampleCriteria>(); 
		
		for (int row = 1; row < criteriaGrid.getRowCount(); row++) {
			SampleCriteria crit = new SampleCriteria();
			// get header
			ListBox headerBox = (ListBox) criteriaGrid.getWidget(row, 0);
			int idx = headerBox.getSelectedIndex();
			if (idx > 0) {
				String header = headerBox.getValue(idx);
				// get operator
				ListBox operatorBox = (ListBox) criteriaGrid.getWidget(row, 1);
				int idx1 = operatorBox.getSelectedIndex();
				crit.setOperator(operatorBox.getValue(idx1));
				// get value
				Widget widget = critWidgetMap.get(header).get(row - 1);
				String widgetName = widget.getClass().getName();
			    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
			    // prints 12/17/2007 in the default locale
			    //GWT.log(fmt.format(today), null);
				if (widgetName.endsWith(DATE_BOX)) {
					DateBox dateBox = (DateBox) criteriaGrid.getWidget(row, 2);
					//trio.setValue(DateTimeFormat.getShortDateFormat().format(dateBox.getValue()));
					crit=setValue(crit,header,dateBox.getValue());//fmt.format(dateBox.getValue()));
				} else if (widgetName.endsWith(TEXT_BOX)) {
					FilteredTextBox textBox = (FilteredTextBox) criteriaGrid.getWidget(row, 2);
					crit = setValue(crit,header,textBox.getValue());
					//Window.alert("value="+textBox.getValue());
					//trio.setValue(textBox.getValue());
				} else if (widgetName.endsWith(SUGGEST_BOX)) {
					FilteredSuggestBox valueBox = (FilteredSuggestBox) criteriaGrid.getWidget(row, 2);
					crit = setValue(crit,header,valueBox.getValue());
					//trio.setValue(valueBox.getValue());
				} else {
					//Window.alert("undefined: "+widgetName);
				}
				crits.add(crit);
			}
			//if (trio.getValue() != null && trio.getValue().length() > 0) {
			//	trioList.add(trio);
			//}
		}
		if ((idBox.getValue() != null) && (!idBox.getValue().isEmpty())) {
			crit = new SampleCriteria();
			long trackingId;
			try {
				trackingId = Long.parseLong(idBox.getValue());
			} catch(NumberFormatException nfe) {
				Window.alert("Bad text. Please use numerals.");
				idBox.setValue("");
				trackingId = 0;
				return;
			}
			crit.setTrackingId(trackingId);
			crits.add(crit);
		}
		
		if (groupBox.getItemCount()>0) {
			crit = new SampleCriteria();
			int count=0;
			for (int i=0;i<groupBox.getItemCount();i++) {
				if (groupBox.isItemSelected(i)) {
					count++;
					crit.getGroups().add(groupBox.getItemText(i));
				}
			}
			if (count == 0) {
				for (int i=0;i<groupBox.getItemCount();i++) {
					crit.getGroups().add(groupBox.getItemText(i));
				}
			}
			crits.add(crit);
		}
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			resultsGrid.resizeColumns(GRID_COLUMNS);
		} else {
			resultsGrid.resizeColumns(GRID_COLUMNS_SABC);
		}
		tsa.searchSamplesCount(crits, id, new SampleSearchCountCallback());
		tsa.searchSamples(crits, id, position, PAGE_SIZE, new SampleSearchCallback());
	}
	
	private SampleCriteria setValue(SampleCriteria crit,String header,String value) {
		//Window.alert("in setValue with header="+header+";value="+value);
		if (header.equals(SearchSample.SAMPLE_ID_LABEL))
			crit.setSampleId(value);
		else if (header.equals(SearchSample.OWNER_LABEL))
			crit.setOwnerName(value);
		else if (header.equals(SearchSample.CUSTODIAN_NAME_LABEL))
			crit.setCustodianName(value);
		else if (header.equals(SearchSample.ORIGIN_LABEL))
			crit.setOrigin(value);
		else if (header.equals(SearchSample.STATUS_LABEL))
			crit.setStatus(value);
		else if (header.equals(SearchSample.FEEDSTOCK_LABEL))
			crit.setFeedstock(value);
		else if (header.equals(SearchSample.TREATMENT_LABEL))
			crit.setTreatment(value);
		else if (header.equals(SearchSample.BIOMASS_LABEL))
			crit.setBiomass_lots(value);
		else if (header.equals(SearchSample.FORM_LABEL))
			crit.setForm(value);
		else if (header.equals(SearchSample.STRAIN_LABEL))
			crit.setStrain(value);
		//else if (header.equals(SearchSample.PROJECT_TASK_LABEL)||
		else if (header.equals(SearchSample.DESTINATION_LABEL_SABC))
			crit.setDestination(value);
		else if (header.equals(SearchSample.COMPOSITION_LABEL))
			crit.setComposition(value);
		else if (header.equals(SearchSample.FRACTION_LABEL))
			crit.setFraction(value);
		else if (header.equals(SearchSample.UNITS_LABEL))
			crit.setUnits(value);
		else if (header.equals(SearchSample.BUILDING_LABEL))
			crit.setBuilding(value);
		else if (header.equals(SearchSample.ROOM_LABEL))
			crit.setRoom(value);
		else if (header.equals(SearchSample.SUBLOCATION_LABEL))
			crit.setSubLocation(value);
		else if (header.equals(SearchSample.SHELF_LABEL))
			crit.setShelf(value);
		else if (header.equals(SearchSample.HOLDER_LABEL))
			crit.setHolder(value);
		else if (header.equals(SearchSample.PACKAGING_LABEL))
			crit.setPackaging(value);
		else if (header.equals(SearchSample.DESCRIPTOR_LABEL))
			crit.setLabelDescription(value);
		else if (header.equals(SearchSample.COMMENTS_LABEL))
			crit.setComments(value);
		else if (header.equals(SearchSample.AMOUNT_LABEL))
			crit.setAmount(value);
		else if (header.equals(SearchSample.STORAGE_NOTES_LABEL))
			crit.setStorageNotes(value);
		else if (header.equals(SearchSample.EXTERNAL_ID_LABEL))
			crit.setExternalId(value);
		else if (header.equals(SearchSample.NUMBER_LABEL)) {
			int num = 0;
			try {
				num = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {}
			crit.setTrbNum(num);
		}
		else if (header.equals(SearchSample.PAGE_LABEL)) {
			int page = 0;
			try {
				page = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {}
			crit.setTrbPage(page);
		}
		else if (header.equals(SearchSample.HOLDER_LABEL))
			crit.setHolder(value);
		else if (header.equals(SearchSample.TRACKING_ID_LABEL)) {
			if (value.contains(",")) {
				String[] vals = value.split(",");
				for (int i=0;i<vals.length;i++) {
					String val = vals[i];
					long id = 0;
					try {
						id = Long.parseLong(val);
					} catch (NumberFormatException nfe) {
						id = 0;
					}
					if (id > 0)
						crit.getTrackingIds().add(id);
				}
			} else {
				long id = 0;
				try {
					id = Long.parseLong(value);
				} catch (NumberFormatException nfe) {
					id = 0;
				}
				if (id > 0)
					crit.setTrackingId(id);
			}
		}
		else if (header.equals(SearchSample.ATTACHMENT_EXT_COLON)) {
			List<String> exts = new ArrayList<String>();
			exts.add(value);
			crit.setAttachment_ext(exts);		
		}
		return crit;
	}
	
	private SampleCriteria setValue(SampleCriteria crit,String header,Date date) {
		crit.setStartCreateDate(date);
		return crit;
	}
	
	private class SampleSearchCountCallback implements AsyncCallback<Integer> {
		@Override
		public void onFailure(Throwable caught) {
		}
		@Override
		public void onSuccess(Integer result) {
			// resulting count includes header
			if (result > 0) {
				int num=0;
				resultsCount=result;
				int inum = new Integer((result)/PAGE_SIZE);
				double div = (double)(result)/(double)PAGE_SIZE;
				if (div > inum)
					num = inum+1;
				else 
					num = inum;
				//num -= 1;
				position = 0;
				prevButton.setEnabled(false);
				if (position+1 < num)
					nextButton.setEnabled(true);
				else
					nextButton.setEnabled(false);
				pageOf.setText("Page "+(position+1)+" of "+num+" ("+resultsCount+" samples returned)");
				pageCount = num;
				buttonPanel.setVisible(true);
			} else {
				buttonPanel.setVisible(false);
				pageOf.setText("");
			}
		}
	}
	/**
	 * Private <AsyncCallback<Collection<SampleCriteria>>> that handles the search anynchronous callback.
	 *  It also populates the results <Grid>.
	 * 
	 * @author jalbersh
	 *
	 */
	private class SampleSearchCallback implements AsyncCallback<Collection<SampleCriteria>> {

		private static final String PRINT_JPG = "print.jpg";
		private static final String EMAIL_JPG = "email_icon.JPG";
		private static final String EDIT_JPG = "edit-16x16.JPG";
		private static final String BLANKS = "---";
		private static final String EDIT = "Edit\nSample";
		private static final String TRB_PAGE_NUMBER_HEADING = "TRB\nPage";
		private static final String TRB_NUMBER_HEADING = "TRB\nNumber";
		private static final String SHELF_HEADING = "Shelf";
		private static final String HOLDER_HEADING = "Holder";
		private static final String ROOM_HEADING = "Room";
		private static final String BUILDING_HEADING = "Building";
		private static final String STATUS_HEADING = "Status";
		private static final String FRACTION_HEADING = "Fraction";
		private static final String FORM_HEADING = "Storage\nState";
		private static final String PROJECT_HEADING = "Project\nTask No.";
		private static final String COMP_HEADING = "Composition";
		private static final String FEEDSTOCK_HEADING = "Feedstock";
		private static final String TREATMENT_HEADING = "Treatment";
		private static final String ORIGIN_HEADING = "Origin";
		private static final String CUSTODIAN_NAME_HEADING = "Custodian\nName";
		private static final String EXTERNAL_ID_HEADING = "External\nID";
		private static final String DATE_ENTERED_HEADING = "Date Created";
		private static final String UNITS_HEADING = "Units";
		private static final String APPROX_AMOUNT_HEADING = "Approximate\nAmount";
		private static final String COMMENTS_HEADING = "Comments";
		private static final String DESCRIPTOR_HEADING = "Label Descriptor";
		private static final String BIOMASS_LOT_HEADING = "Biomass\nLot ID";
		private static final String SAMPLE_ID_HEADING = "Sample\nID";
		private static final String TRACKING_ID_HEADING = "Tracking\nID";
		private static final String PRINT_LABEL_HEADING = "Print\nLabel";
		private static final String EMAIL_LABEL_HEADING = "Email\nLabel";
		private static final String VIEW_LABEL_HEADING = "Edit\nLabel";
		private static final String SUB_LOCATION_HEADING = "Sublocation";
		private static final String PACKAGING_HEADING = "Packaging";
		private static final String STORAGE_NOTES_HEADING = "Storage\nNotes";
		private static final String OWNER_NAME_HEADING = "Owner\nName";
		private static final String FIRE_HEADING = "Fire\nHazard";
		private static final String REACTIVITY_HEADING = "Reactivity\nHazard";
		private static final String SPECIFIC_HEADING = "Specific\nHazard";
		private static final String HEALTH_HEADING = "Health\nHazard";
		private static final String STRAIN_HEADING = "Strain";

		public void onFailure(Throwable caught) {
			//Window.alert("Search Failed: " + caught.getMessage());
		}

		public void onSuccess(final Collection<SampleCriteria> result) {
			searchDialog.hide();
			if ((result != null) && (!result.isEmpty())) {
				tsa.getSessionId(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-5");
					}

					@Override
					public void onSuccess(String result2) {
						//Window.alert("From server:session="+result);
						sessionId = result2;
					    tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								//Window.alert("Shouldn't get to here-6");
							}

							@Override
							public void onSuccess(String result1) {
								//Window.alert("got group="+group);
								group = result1;
								loadResultsTable(result, group, sessionId);
							}
					    });
					}
				});
			} else {
				Window.alert("No samples were found matching your criteria.");
				//superPanel.clear();
				resultsGrid.resizeRows(0);
				resultsGrid.setVisible(false);
			}
		}

		private void loadResultsTable(final Collection<SampleCriteria> result, String group, String sessionId) {
			if ((result == null) || (result.isEmpty())) {
				return;
			}
			
			// Clear out results
			resultsGrid.resizeRows(0);
			resultsGrid.setVisible(false);
			
			int row = 0;
			int col = 0;
			
			// Set up header row
			resultsGrid.insertRow(row);
			resultsGrid.setText(row, col, EDIT);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			final int rowf1 = row;
			final int colf1 = col;
			tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("Shouldn't get to here");
				}
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						resultsGrid.setText(rowf1, colf1, PRINT_LABEL_HEADING);
					} else {
						resultsGrid.setText(rowf1, colf1, EMAIL_LABEL_HEADING);
					}				
				}
			});
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, VIEW_LABEL_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, TRACKING_ID_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, SAMPLE_ID_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			
			if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {			
				resultsGrid.setText(row, col, EXTERNAL_ID_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, OWNER_NAME_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
			} else {
				resultsGrid.setText(row, col, BIOMASS_LOT_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
			}
			resultsGrid.setText(row, col, CUSTODIAN_NAME_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
				resultsGrid.setText(row, col, PROJECT_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;				
			}
			resultsGrid.setText(row, col, ORIGIN_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, STATUS_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, TRB_NUMBER_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, TRB_PAGE_NUMBER_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {			
				resultsGrid.setText(row, col, FEEDSTOCK_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, TREATMENT_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
			} else {
				resultsGrid.setText(row, col, FORM_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;				
				resultsGrid.setText(row, col, STRAIN_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;				
				///*
				resultsGrid.setText(row, col, COMP_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;	
				//*/			
			}
			resultsGrid.setText(row, col, FRACTION_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, APPROX_AMOUNT_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, UNITS_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
				resultsGrid.setText(row, col, FIRE_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, REACTIVITY_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, SPECIFIC_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, HEALTH_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, BUILDING_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, ROOM_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, SUB_LOCATION_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, SHELF_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, HOLDER_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, PACKAGING_HEADING);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
			}
			resultsGrid.setText(row, col, STORAGE_NOTES_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, DATE_ENTERED_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, COMMENTS_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			col++;
			resultsGrid.setText(row, col, DESCRIPTOR_HEADING);
			resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);

			row++;
			Iterator<SampleCriteria> it = result.iterator();
			while (it.hasNext()) {
				col = 0;
				final SampleCriteria sample = it.next();
				resultsGrid.insertRow(row);
				Image image = new Image(EDIT_JPG);
				PushButton editBtn = new PushButton(image, new EditClickHandler());
				editBtn.setTitle(String.valueOf(sample.getTrackingId()));
				resultsGrid.setWidget(row, col, editBtn);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				/*
				if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					final Image printImage = new Image(PRINT_JPG);
					final int rowf = row;
					final int colf = col;
					tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							//Window.alert("Shouldn't get to here");
						}
						@Override
						public void onSuccess(Boolean result) {
							if (!result) {
								PushButton labelBtn = new PushButton(printImage);
								labelBtn.setTitle(String.valueOf(sample.getTrackingId()));
								resultsGrid.setWidget(rowf, colf, labelBtn);
							} else {
								PushButton labelBtn = new PushButton(printImage, new LabelClickHandler());
								labelBtn.setTitle(String.valueOf(sample.getTrackingId()));
								resultsGrid.setWidget(rowf, colf, labelBtn);
							}
						}
					});
				} else {
					final Image emailImage = new Image(EMAIL_JPG);
					final int rowf = row;
					final int colf = col;
					tsa.hasPermission(sessionId, "Email", new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							//Window.alert("Shouldn't get to here");
						}
						@Override
						public void onSuccess(Boolean result) {
							if (!result) {
								PushButton emailBtn = new PushButton(emailImage);
								emailBtn.setTitle(String.valueOf(sample.getTrackingId()));
								resultsGrid.setWidget(rowf, colf, emailBtn);
							} else {
								PushButton emailBtn = new PushButton(emailImage, new EmailClickHandler());
								emailBtn.setTitle(String.valueOf(sample.getTrackingId()));
								resultsGrid.setWidget(rowf, colf, emailBtn);					
							}
						}
					});
				}
				*/
				final Image printImage = new Image(PRINT_JPG);
				final Image emailImage = new Image(EMAIL_JPG);
				final int rowf = row;
				final int colf = col;
				/*
				tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here");
					}
					@Override
					public void onSuccess(Boolean result) {
						if (!result) {
							PushButton emailBtn = new PushButton(emailImage, new EmailClickHandler());
							emailBtn.setTitle(String.valueOf(sample.getTrackingId()));
							resultsGrid.setWidget(rowf, colf, emailBtn);					
						} else {
						*/
							PushButton labelBtn = new PushButton(printImage, new LabelClickHandler());
							labelBtn.setTitle(String.valueOf(sample.getTrackingId()));
							resultsGrid.setWidget(rowf, colf, labelBtn);
						//}
					//}
				//});
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				image = new Image(EDIT_JPG);
				PushButton editLabelBtn = new PushButton(image, new EditLabelClickHandler());
				editLabelBtn.setTitle(String.valueOf(sample.getTrackingId()));
				resultsGrid.setWidget(row, col, editLabelBtn);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				col++;
				resultsGrid.setText(row, col, sample.getTrackingId() > 0 ? String.valueOf(sample.getTrackingId()) : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getSampleId() != null && sample.getSampleId().length() > 0 ? sample.getSampleId() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					resultsGrid.setText(row, col, sample.getExternalId() != null && sample.getExternalId().length() > 0 ? sample.getExternalId() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getOwnerName() != null && sample.getOwnerName().length() > 0 ? sample.getOwnerName() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				} else {
					resultsGrid.setText(row, col, sample.getBiomass_lots() != null && sample.getBiomass_lots().length() > 0 ? sample.getBiomass_lots() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				}
				resultsGrid.setText(row, col, sample.getCustodianName() != null && sample.getCustodianName().length() > 0 ? sample.getCustodianName() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					resultsGrid.setText(row, col, sample.getDestination() != null && sample.getDestination().length() > 0 ? sample.getDestination() : BLANKS);
				//} else {
				//	resultsGrid.setText(row, col, sample.getProject() != null && sample.getProject().length() > 0 ? sample.getProject() : BLANKS);					
				//}
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				}
				resultsGrid.setText(row, col, sample.getOrigin() != null && sample.getOrigin().length() > 0 ? sample.getOrigin() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getStatus() != null && sample.getStatus().length() > 0 ? sample.getStatus() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, Integer.toString(sample.getTrbNum()));
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				Button trbBtn = new Button(Integer.toString(sample.getTrbPage()));
				trbBtn.setTitle(Integer.toString(sample.getTrbNum()));
				trbBtn.addClickHandler(new TrbClickHandler());
				resultsGrid.setWidget(row, col, trbBtn);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					resultsGrid.setText(row, col, sample.getFeedstock() != null && sample.getFeedstock().length() > 0 ? sample.getFeedstock() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getTreatment() != null && sample.getTreatment().length() > 0 ? sample.getTreatment() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				} else {
					resultsGrid.setText(row, col, sample.getForm() != null && sample.getForm().length() > 0 ? sample.getForm() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getStrain() != null && sample.getStrain().length() > 0 ? sample.getStrain() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getComposition() != null && sample.getComposition().length() > 0 ? sample.getComposition() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				}
				resultsGrid.setText(row, col, sample.getFraction() != null && sample.getFraction().length() > 0 ? sample.getFraction() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getAmount() != null && sample.getAmount().length() > 0 ? sample.getAmount() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getUnits() != null && sample.getUnits().length() > 0 ? sample.getUnits() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					resultsGrid.setText(row, col, sample.getFire() != null && sample.getFire().length() > 0 ? sample.getFire() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getReactivity() != null && sample.getReactivity().length() > 0 ? sample.getReactivity() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getSpecific() != null && sample.getSpecific().length() > 0 ? sample.getSpecific() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getHealth() != null && sample.getHealth().length() > 0 ? sample.getHealth() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getBuilding() != null && sample.getBuilding().length() > 0 ? sample.getBuilding() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getRoom() != null && sample.getRoom().length() > 0 ? sample.getRoom() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getSubLocation() != null && sample.getSubLocation().length() > 0 ? sample.getSubLocation() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getShelf() != null && sample.getShelf().length() > 0 ? sample.getShelf() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getHolder() != null && sample.getHolder().length() > 0 ? sample.getHolder() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
					resultsGrid.setText(row, col, sample.getPackaging() != null && sample.getPackaging().length() > 0 ? sample.getPackaging() : BLANKS);
					resultsGrid.getCellFormatter().setWordWrap(row, col, false);
					resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				}
				
				resultsGrid.setText(row, col, sample.getStorageNotes() != null && sample.getStorageNotes().length() > 0 ? sample.getStorageNotes() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				if (sample.getStartCreateDate() != null) resultsGrid.setText(row, col, sample.getStartCreateDate().toString());
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getComments() != null && sample.getComments().length() > 0 ? sample.getComments() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				resultsGrid.setText(row, col, sample.getLabelDescription() != null && sample.getLabelDescription().length() > 0 ? sample.getLabelDescription() : BLANKS);
				resultsGrid.getCellFormatter().setWordWrap(row, col, false);
				resultsGrid.getCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);
				row++;
			}
			downloadButton.setVisible(true);
			printAllButton.setVisible(true);
			resultsScrollPanel.setVisible(true);
			searchDialog.hide();
			resultsGrid.setVisible(true);
		}
	}

	/**
	 * Private <ClickHandler> that handles printing a label
	 * @author jalbersh
	 *
	 */
	private class LabelClickHandler implements ClickHandler {

		private static final int TRACKING_ID_COLUMN = 1;
		private static final int SAMPLE_ID_COLUMN = 2;
		private static final int OWNER_NAME_COLUMN = 4;
		private static final int STRAIN_COLUMN = 15;
		private static final int DESTINATION_COLUMN = 16;
		private static final int TRB_NUM_COLUMN = 7;
		private static final int TRB_PAGE_COLUMN = 8;
		private static final int ENTRY_DATE_COLUMN = 21;
		private static final int ENTRY_DATE_COLUMN_SABC = 11;
		private static final int CUSTODIAN_COLUMN = 3;
		//private static final int FORM_COLUMN = 6;
		private static final int SABC_FORM_COLUMN = 10;

		public void onClick(ClickEvent event) {
			//Window.alert("in LabelClickHandler");
			//Window.alert("Requested label print");
			final LabelDTO label = new LabelDTO();
			
			Cell clickedCell = resultsGrid.getCellForEvent(event);
			final int clickedRow = clickedCell.getRowIndex();
			
			tsa.getSessionId(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("Shouldn't get to here-2");
				}

				@Override
				public void onSuccess(String result) {
					//Window.alert("From server:session="+result);
					sessionId = result;
				}
			});
			if (printerBox != null && printerBox.getSelectedIndex() == -1) {
				Window.alert("No printer selected. Please select a printer.");
			} else {
				if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
					try {
						for (int i = 0; i < resultsGrid.getColumnCount(); i++) {
							Widget w = resultsGrid.getWidget(clickedRow, i);
							if (w == null) {						
								String textValue = resultsGrid.getText(clickedRow, i);
								switch (i) {
								case SAMPLE_ID_COLUMN:
									label.setSampleId(textValue);
									break;
								case OWNER_NAME_COLUMN:
									label.setOwnerName(textValue);
									break;
								case TRB_NUM_COLUMN:
									label.setTrbNum(textValue);
									break;
								case ENTRY_DATE_COLUMN:
									label.setEntryDate(textValue);
									break;
								}
							} else {
								if (i == TRACKING_ID_COLUMN)
									label.setTrackingId(((PushButton)w).getTitle());
								else if (i == TRB_PAGE_COLUMN)
									label.setTrbPage(((Button)w).getText());
							}
						}	
					} catch (Exception e1) {
						Window.alert(PROBLEM_PRINTING);
					}
		
					String printerName = "";
					//if (PRINT_MODE.equals(ALGAE)) 
						printerName = printerBox.getItemText(printerBox.getSelectedIndex());
					if (printerName != null && !printerName.trim().isEmpty()) {
					//else
					//	if (!sabcPrinterHidden.getValue().isEmpty())
					//		printerName = sabcPrinterHidden.getValue();
					
					tsa.printLabel(sessionId, label, printerName, new AsyncCallback<Boolean>() {
					
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(PROBLEM_PRINTING);
						}
		
						@Override
						public void onSuccess(Boolean result) {
							if (!result.booleanValue()) {
								Window.alert(PROBLEM_PRINTING);							
							} else {
								Window.alert(SUCCESS_PRINTING);
							}
						}				
					});
					} else {
						Window.alert("No printer selected. Please select a printer.");
					}
				} else {
					try {
						for (int i = 0; i < resultsGrid.getColumnCount(); i++) {
							Widget w = resultsGrid.getWidget(clickedRow, i);
							if (w == null) {						
								String textValue = resultsGrid.getText(clickedRow, i);
								switch (i) {
								case SAMPLE_ID_COLUMN:
									label.setSampleId(textValue);
									break;
								case CUSTODIAN_COLUMN:
									label.setCustodian(textValue);
									break;
								case STRAIN_COLUMN:
									label.setStrain(textValue);
									break;
								case SABC_FORM_COLUMN:
									label.setForm(textValue);
									break;
								//case COMPOSITION_COLUMN:
								//	label.setComposition(textValue);
								//	break;
								case DESTINATION_COLUMN:
									label.setDestination(textValue);
									break;
								case ENTRY_DATE_COLUMN_SABC:
									label.setEntryDate(textValue);
									break;
								}
							} else {
								if (i == TRACKING_ID_COLUMN)
									label.setTrackingId(((PushButton)w).getTitle());
							}
						}	
					} catch (Exception e1) {
						Window.alert(PROBLEM_PRINTING);
					}
				
					String printerName = "";
					//if (PRINT_MODE.equals(ALGAE)) 
					printerName = printerBox.getItemText(printerBox.getSelectedIndex());
					if (printerName != null && !printerName.trim().isEmpty()) {
					//else
					//	if (!sabcPrinterHidden.getValue().isEmpty())
					//		printerName = sabcPrinterHidden.getValue();
	
					tsa.printLabel(sessionId, label, printerName, new AsyncCallback<Boolean>() {
					
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(PROBLEM_PRINTING);
						}
					
						@Override
						public void onSuccess(Boolean result) {
							if (!result.booleanValue()) {
								Window.alert(PROBLEM_PRINTING);							
							} else {
								Window.alert(SUCCESS_PRINTING);
							}
						}				
					});
					} else {
						Window.alert("No printer selected. Please select a printer.");
					}
				}
			}
		}
	}

	/**
	 * Private <ClickHandler> that handles printing a label
	 * @author jalbersh
	 *
	 */
	private class EmailClickHandler implements ClickHandler {

		private static final int TRACKING_ID_COLUMN = 1;
		private static final int SAMPLE_ID_COLUMN = 2;
		private static final int OWNER_NAME_COLUMN = 4;
		private static final int STRAIN_COLUMN = 15;
		private static final int DESTINATION_COLUMN = 16;
		private static final int TRB_NUM_COLUMN = 7;
		private static final int TRB_PAGE_COLUMN = 8;
		private static final int ENTRY_DATE_COLUMN = 21;
		private static final int ENTRY_DATE_COLUMN_SABC = 11;
		private static final int CUSTODIAN_COLUMN = 3;
		private static final int FORM_COLUMN = 6;

		public void onClick(ClickEvent event) {
			Window.alert("Requested label email");
			final LabelDTO label = new LabelDTO();
			
			Cell clickedCell = resultsGrid.getCellForEvent(event);
			final int clickedRow = clickedCell.getRowIndex();
			
			tsa.getSessionId(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("Shouldn't get to here-2");
				}

				@Override
				public void onSuccess(String result) {
					//Window.alert("From server:session="+result);
					sessionId = result;
				}
			});
			/*
			tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("Shouldn't get to here-6");
				}

				@Override
				public void onSuccess(String group) {
					//Window.alert("got group="+group);
			 */
					if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							for (int i = 0; i < resultsGrid.getColumnCount(); i++) {
								Widget w = resultsGrid.getWidget(clickedRow, i);
								if (w == null) {						
									String textValue = resultsGrid.getText(clickedRow, i);
									switch (i) {
									case SAMPLE_ID_COLUMN:
										label.setSampleId(textValue);
										break;
									case OWNER_NAME_COLUMN:
										label.setOwnerName(textValue);
										break;
									case TRB_NUM_COLUMN:
										label.setTrbNum(textValue);
										break;
									case ENTRY_DATE_COLUMN:
										label.setEntryDate(textValue);
										break;
									}
								} else {
									if (i == TRACKING_ID_COLUMN)
										label.setTrackingId(((PushButton)w).getTitle());
									else if (i == TRB_PAGE_COLUMN)
										label.setTrbPage(((Button)w).getText());
								}
							}				
							tsa.emailLabel(sessionId, label, new AsyncCallback<Boolean>() {
				
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(PROBLEM_EMAILING);
								}
				
								@Override
								public void onSuccess(Boolean result) {
									if (!result.booleanValue()) {
										Window.alert(PROBLEM_EMAILING);							
									} else
										Window.alert(SUCCESS_EMAILING);
								}				
							});
					} else {
							for (int i = 0; i < resultsGrid.getColumnCount(); i++) {
								Widget w = resultsGrid.getWidget(clickedRow, i);
								if (w == null) {						
									String textValue = resultsGrid.getText(clickedRow, i);
									switch (i) {
									case SAMPLE_ID_COLUMN:
										label.setSampleId(textValue);
										break;
									case CUSTODIAN_COLUMN:
										label.setCustodian(textValue);
										break;
									case STRAIN_COLUMN:
										label.setStrain(textValue);
										break;
									case FORM_COLUMN:
										label.setForm(textValue);
										break;
									//case COMPOSITION_COLUMN:
									//	label.setComposition(textValue);
									//	break;
									case DESTINATION_COLUMN:
										label.setDestination(textValue);
										break;
									case ENTRY_DATE_COLUMN_SABC:
										label.setEntryDate(textValue);
										break;
									}
								} else {
									if (i == TRACKING_ID_COLUMN)
										label.setTrackingId(((PushButton)w).getTitle());
								}
							}	
			
							tsa.emailLabel(sessionId, label, new AsyncCallback<Boolean>() {
				
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(PROBLEM_EMAILING);
								}
				
								@Override
								public void onSuccess(Boolean result) {
									if (!result.booleanValue()) {
										Window.alert(PROBLEM_EMAILING);							
									} else 
										Window.alert(SUCCESS_EMAILING);
								}				
							});
					}						
				//}
			//});
		}
	}

	/**
	 * Private <ClickHandler> to handle downloading a TRB page PDF.
	 * 
	 * @author jalbersh
	 *
	 */
	private class TrbClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			Button btn = (Button)event.getSource();
			final String page = btn.getText();
			final String num = btn.getTitle();
			trbNum.setValue(num);
			trbPage.setValue(page);
			tsa.trbHasFile(num, page, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					System.err.println("Failed to call trbHasFile");
				}

				public void onSuccess(Boolean result) {
					boolean there = result.booleanValue();
					if (there)
						pdfDownloadForm.submit();
					else
						Window.alert("File has not been uploaded for book#"+num+" and page#"+page);
				}				
			});
		}		
	}

	/**
	 * Private method to initialize the services
	 */
	private void initServices() {
		tsa = (TrackerServiceAsync) GWT.create(TrackerService.class);		

		ServiceDefTarget endpoint = (ServiceDefTarget) tsa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + URL_TRACKER_SERVICE);
	}

	/**
	 * Private method to populate the attachment extension data
	 */
	private void loadAttachmentExtensions(final MultiWordSuggestOracle suggestOracle) {
		tsa.getAttachmentExtensions(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Do not report here
			}
	
			@Override
			public void onSuccess(List<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String ext = it.next();
						suggestOracle.add(ext);
					}
				}
			}
		});
	}
	
	/**
	 * Private method to populate the fraction data
	 */
	private void loadFractions(final MultiWordSuggestOracle suggestOracle) {
		tsa.getFractionNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Fractions failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						suggestOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the owner name data
	 */
	private void loadOwnerNames(final MultiWordSuggestOracle suggestOracle) {
		tsa.getOwnerNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Owner names failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});		
	}

	private void loadOrigins(final MultiWordSuggestOracle suggestOracle) {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
				tsa.getOrigins(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						//Window.alert("Origins failed to load. Error: " + caught.getMessage());
					}
		
					public void onSuccess(Collection<String> result) {
						suggestOracle.clear();
						if (result != null) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String item = it.next();
								suggestOracle.add(item);
							}
						}
					}			
				});
			}
		});	
	}

	/**
	 * Private method to populate the sample name data
	 */
	private void loadSampleIds(final MultiWordSuggestOracle suggestOracle) {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-2");
			}

			@Override
			public void onSuccess(String sessionId) {
				tsa.getSampleIds(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						Window.alert("Sample IDs failed to load.\nDatabase may not be available.");				
					}
		
					public void onSuccess(Collection<String> result) {
						if ((result != null) && (!result.isEmpty())) {
							suggestOracle.clear();
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String name = it.next();
								if (name != null)
									suggestOracle.add(name);
							}
						}
					}			
				});		
			}
		});
	}

	/**
	 * Private method to populate the feedstock data
	 */
	private void loadFeedstocks(final MultiWordSuggestOracle suggestOracle) {
		tsa.getFeedstockNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Feedstocks failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					suggestOracle.clear();
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the status data
	 */
	private void loadStatuses(final MultiWordSuggestOracle suggestOracle) {
		/*
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
				*/
				tsa.getStatusNames(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						Window.alert("Statuses failed to load.\nDatabase may not be available.");				
					}
		
					public void onSuccess(Collection<String> result) {
						statusBox.clear();
						if ((result != null) && (!result.isEmpty())) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String name = it.next();
								suggestOracle.add(name);
							}
						}
					}			
				});	
			//}
		//});
	}

	/**
	 * Private method to populate the units data
	 */
	private void loadUnits(final MultiWordSuggestOracle suggestOracle) {
		tsa.getUnits(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Units failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				unitBox.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						suggestOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the location shelf data
	 */
	private void loadShelves(final MultiWordSuggestOracle suggestOracle) {
		tsa.getShelves(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Shelves failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the location holder data
	 */
	private void loadHolders(final MultiWordSuggestOracle suggestOracle) {
		tsa.getHolders(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Holders failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the location packaging data
	 */
	private void loadPackaging(final MultiWordSuggestOracle suggestOracle) {
		tsa.getPackaging(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Packaging failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the location sublocation data
	 */
	private void loadSubLocations(final MultiWordSuggestOracle suggestOracle) {
		tsa.getSubLocations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Sublocations failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the location room data
	 */
	private void loadRooms(final MultiWordSuggestOracle suggestOracle) {
		tsa.getRooms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Rooms failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the location building data
	 */
	private void loadBuildings(final MultiWordSuggestOracle suggestOracle) {
		tsa.getBuildings(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Buildings failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});
	}

	/**
	 * Private method to populate the treatment data
	 */
	private void loadTreatments(final MultiWordSuggestOracle suggestOracle) {
		tsa.getTreatmentNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						suggestOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the custodian data
	 */
	private void loadCustodians(final MultiWordSuggestOracle suggestOracle) {
		tsa.getCustodianNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Custodian names failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				suggestOracle.clear();
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null)
							suggestOracle.add(name);
					}
				}
			}			
		});		
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		if (sender == nextButton) {
			handleUpDownButtonClick(1);
		} else if (sender == prevButton) {
			handleUpDownButtonClick(-1);
		} else if (sender == gotoPageBtn)
			handlePageButtonClick();
		else if (sender == pageSize) {
			handlePageSizeChanges();
		}
	}

	private List<SampleCriteria> getCriteria() {
		List<SampleCriteria> crits1 = new ArrayList<SampleCriteria>(); 
		
		for (int row = 1; row < criteriaGrid.getRowCount(); row++) {
			crit = new SampleCriteria();
			// get header
			ListBox headerBox = (ListBox) criteriaGrid.getWidget(row, 0);
			int idx = headerBox.getSelectedIndex();
			if (idx > 0) {
				String header = headerBox.getValue(idx);
				// get operator
				ListBox operatorBox = (ListBox) criteriaGrid.getWidget(row, 1);
				int idx1 = operatorBox.getSelectedIndex();
				crit.setOperator(operatorBox.getValue(idx1));
				// get value
				Widget widget = critWidgetMap.get(header).get(row - 1);
				String widgetName = widget.getClass().getName();
			    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
			    // prints 12/17/2007 in the default locale
			    //GWT.log(fmt.format(today), null);
				if (widgetName.endsWith(DATE_BOX)) {
					DateBox dateBox = (DateBox) criteriaGrid.getWidget(row, 2);
					//trio.setValue(DateTimeFormat.getShortDateFormat().format(dateBox.getValue()));
					crit=setValue(crit,header,dateBox.getValue());//fmt.format(dateBox.getValue()));
				} else if (widgetName.endsWith(TEXT_BOX)) {
					FilteredTextBox textBox = (FilteredTextBox) criteriaGrid.getWidget(row, 2);
					crit = setValue(crit,header,textBox.getValue());
					//trio.setValue(textBox.getValue());
				} else if (widgetName.endsWith(SUGGEST_BOX)) {
					FilteredSuggestBox valueBox = (FilteredSuggestBox) criteriaGrid.getWidget(row, 2);
					crit = setValue(crit,header,valueBox.getValue());
					//trio.setValue(valueBox.getValue());
				}
				crits1.add(crit);
			}
			//if (trio.getValue() != null && trio.getValue().length() > 0) {
			//	trioList.add(trio);
			//}
		}
		if ((idBox.getValue() != null) && (!idBox.getValue().isEmpty())) {
			crit = new SampleCriteria();
			long trackingId;
			try {
				trackingId = Long.parseLong(idBox.getValue());
			} catch(NumberFormatException nfe) {
				Window.alert("Bad text. Please use numerals.");
				idBox.setValue("");
				trackingId = 0;
				return null;
			}
			crit.setTrackingId(trackingId);
			crits1.add(crit);
		}
		
		if (groupBox.getItemCount()>0) {
			crit = new SampleCriteria();
			int count=0;
			for (int i=0;i<groupBox.getItemCount();i++) {
				if (groupBox.isItemSelected(i)) {
					count++;
					crit.getGroups().add(groupBox.getItemText(i));
				}
			}
			if (count == 0) {
				for (int i=0;i<groupBox.getItemCount();i++) {
					crit.getGroups().add(groupBox.getItemText(i));
				}
			}
			crits1.add(crit);
		}
		
		return crits1;
	}

	private void handlePageButtonClick() {
		String spage = gotoPage.getText();
		int page = 0;
		try {
			page = Integer.parseInt(spage);
		} catch (NumberFormatException nfe) {
			
		}
		if (page < 1 || page > pageCount) {
			Window.alert("Invalid number entered. Please enter a page between 1 and "+pageCount);
			return;
		}
		List<SampleCriteria> crits1 = getCriteria();
		if (crits1 == null) return;
		searchDialog.show();
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			resultsGrid.resizeColumns(GRID_COLUMNS);
		} else {
			resultsGrid.resizeColumns(GRID_COLUMNS_SABC);
		}
		position = page-1;
		if (position < 0) {
			position = 0;
		} else if (position == 0) {
			prevButton.setEnabled(false);
			if (pageCount > 1)
				nextButton.setEnabled(true);
		} else if (position+1 == pageCount) {
			nextButton.setEnabled(false);
			if (pageCount > 1)
				prevButton.setEnabled(true);
		} else {
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
		tsa.searchSamples(crits1, sessionId, position, PAGE_SIZE, new SampleSearchCallback());
		pageOf.setText("Page "+(position+1)+" of "+pageCount+" ("+resultsCount+" samples returned)");
	}

	/**
	 * Private method to handle the submit button.
	 *  This method pulls the values from the widgets and submits
	 *  a call to the servlet for processing.
	 */
	private void handleUpDownButtonClick(int plusMinus) {
		if (pageCount==0) return;
		List<SampleCriteria> crits1 = getCriteria();
		if (crits1 == null) return;
		searchDialog.show();
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			resultsGrid.resizeColumns(GRID_COLUMNS);
		} else {
			resultsGrid.resizeColumns(GRID_COLUMNS_SABC);
		}
		position = position+plusMinus;
		if (position < 0) {
			position = 0;
		}
		if (position == 0) {
			prevButton.setEnabled(false);
			if (position+1 < pageCount) {
				nextButton.setEnabled(true);
			} else {
				nextButton.setEnabled(false);
			}
		} else if (position+1 == pageCount) {
			prevButton.setEnabled(true);
			nextButton.setEnabled(false);
		} else {
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
			tsa.searchSamples(crits1, sessionId, position, PAGE_SIZE, new SampleSearchCallback());
			pageOf.setText("Page "+(position+1)+" of "+pageCount+" ("+resultsCount+" samples returned)");
	}
}
