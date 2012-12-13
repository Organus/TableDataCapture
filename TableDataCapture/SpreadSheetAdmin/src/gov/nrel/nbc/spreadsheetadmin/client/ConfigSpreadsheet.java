package gov.nrel.nbc.spreadsheetadmin.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * A public class representing the search panel.
 * 
 * @author James Albersheim
 *
 */
public class ConfigSpreadsheet extends Composite implements AppConstants, ClickHandler, ChangeHandler {
	
	private static final String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private static final String CELL_DATA_HEADERS_CONFIGURATION_TITLE = "Cell Data Headers Configuration";

	private static final String INTERNAL_METADATA_CONFIGURATION_TITLE = "Internal Metadata Configuration";

	private static final String PAIRED_SEPARATOR = " | ";

	private static final String END_COLUMN_ROW = "End Column:";

	private static final String START_COLUMN_ROW = "Start Column:";

	private static final String END_ROW_COLON = "End Row:";

	private static final String START_ROW_COLON = "Start Row:";

	private static final String SHEETS_PROCESSED_TITLE = "Sheets To Process";

	private static final String DATA_COLUMN_ROW = "Data Column:";

	private static final String DATA_ROW_COLON = "Data Row:";

	private static final String HEADER_COLUMN_COLON = "Header Column:";

	private static final String HEADER_ROW_COLON = "Header Row:";

	private static final String SHEET_PROCESSING_PANEL_TITLE = "Sheet Configuration";

	private static final String EMPTY_STRING = "";

	private static final String PERCENT_100 = "100%";

	private static final String PAIRED_REGEX = " \\| ";

	private static final String WORKSHEET_TYPE_COLON = "Worksheet Sample File:";

	private static final String WORKBOOK_TYPE_COLON = "Workbook Type:";

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";

	private static final String DOCK_PANEL_WIDTH = PERCENT_100;

	private static final String RESULTS_DATA_WIDTH = "500px";

	private static final String HORIZONTAL_RULE = "<hr/>";
	/**
	 * String constant for the name of the client upload service.
	 */
	private final String URL_SAMPLE_UPLOAD_SERVICE = "sampleUploadService";

	private static final String WAIT_MESSAGE = "Please wait...";

	private static final String WAIT_JPG = "hourglass1.jpg";

	/**
	 * Text for clear button
	 */
	private static final String CLEAR = "Clear";

	/**
	 * String constant for the name of the admin service.
	 */
	private final String URL_ADMIN_SERVICE = "adminService";

	/**
	 * Holder for the SubmissionService
	 */
	private AdminServiceAsync asa = null;

	/**
	 * Sub-title for panel
	 */
	private final String SUB_TITLE = "Create Configuration";
	
	/**
	 * Title of the button to submit the query to the server
	 */
	private final String SUBMIT = "Submit";
	private final String DELETE = "Delete";
	
	private String sheetName = EMPTY_STRING;
	
	/**
	 * Main container for the Search Panel
	 */
	private VerticalPanel mainPanel = new VerticalPanel();

	private final FilteredAlphaNumericMySQLTableNameTextBox workbookConfigBox = new FilteredAlphaNumericMySQLTableNameTextBox();

	private final FileUpload worksheetConfigBox = new FileUpload();
	
	private final FormPanel formPanel = new FormPanel();
	
	private final StringBuffer serverFileName = new StringBuffer();

	private final DecoratorPanel meDecoratorPanel = new DecoratorPanel();

	private final ListBox meListBox = new ListBox();

	private final FilteredAlphaNumericTextBox meName = new FilteredAlphaNumericTextBox();

	private final ListBox meTypeListBox = new ListBox();

	private final Button meAddButton = new Button("Add");

	private final Button meRemoveButton = new Button("Remove");

	private final Button meMoveUpButton = new Button("Move Up");

	private final Button meMoveDownButton = new Button("Move Down");

	private final FlexTable meTable = new FlexTable();

	private final ListBox scListBox = new ListBox();

	private final DecoratorPanel scDecoratorPanel = new DecoratorPanel();

	private final DecoratorPanel miDecoratorPanel = new DecoratorPanel();

	private final Button submitButton = new Button(SUBMIT);

	private final Button deleteButton = new Button(DELETE);

	private final Button meDeleteButton = new Button(DELETE);

	private final Button miDeleteButton = new Button(DELETE);

	private final ListBox s2pListBox = new ListBox();

	private final DecoratorPanel s2pDecoratorPanel = new DecoratorPanel();
	
	private final HashMap<String, SheetInfoDTO> sheetInfoMap = new HashMap<String, SheetInfoDTO>();

	private final FilteredNumberTextBox scHeaderRowBox = new FilteredNumberTextBox();

	private final FilteredAlphaNumericTextBox scHeaderColBox = new FilteredAlphaNumericTextBox();

	private final FilteredNumberTextBox scDataRowBox = new FilteredNumberTextBox();

	private final FilteredAlphaNumericTextBox scDataColBox = new FilteredAlphaNumericTextBox();

	private final CheckBox scMetaDataIntBox = new CheckBox("Metadata (Internal)?");

	private final FilteredNumberTextBox micStartRowBox = new FilteredNumberTextBox();

	private final FilteredNumberTextBox micEndRowBox = new FilteredNumberTextBox();

	private final FilteredAlphaNumericTextBox micStartColBox = new FilteredAlphaNumericTextBox();

	private final FilteredAlphaNumericTextBox micEndColBox = new FilteredAlphaNumericTextBox();

	private final Button clearButton = new Button(CLEAR);

	private final Button s2pGetHeadersButton = new Button("Get Headers");

	private final Button scAddButton = new Button("Add");

	private final CheckBox meCheckBox = new CheckBox("Metadata (External)?");

	private final Button s2pRemoveButton = new Button("Remove");

	private String wbConfig = null;
	
	private String shConfig = null;

	private final VerticalPanel s2pVPanel = new VerticalPanel();

	private final HorizontalPanel s2pHPanel = new HorizontalPanel();

	private final HorizontalPanel s3pHPanel = new HorizontalPanel();

	private Button chAddButton = new Button("Add");

	private final ListBox chPairedListBox = new ListBox();

	private final ListBox chHeaderListBox = new ListBox();

	private ListBox chTypeListBox = new ListBox();

	private Button chSaveButton = new Button("Save");

	private final Button chRemoveButton = new Button("Remove");

	private final Button chDeleteButton = new Button("Delete Sheet");

	private final ListBox miHeaderListBox = new ListBox();

	private final ListBox miTypeListBox = new ListBox();

	private final Button miRemoveButton = new Button("Remove");

	private final Button miSaveButton = new Button("Save");

	private final ListBox miPairedListBox = new ListBox();

	private final Button miAddButton = new Button("Add");

	private final Button meSaveButton = new Button("Save");

	private final ListBox chDataFormatListBox = new ListBox();

	private final ListBox miDataFormatListBox = new ListBox();

	private final ListBox meDataFormatListBox = new ListBox();
	
	private final List<NameValue> dataTypes = new ArrayList<NameValue>();
	
	private final List<NameValue> dateFormats = new ArrayList<NameValue>();
	
	private final List<NameValue> realFormats = new ArrayList<NameValue>();
	
	private DecoratorPanel dpint = new DecoratorPanel();
	
	private VerticalPanel vpint = new VerticalPanel();
	//private DecoratorPanel dp = new DecoratorPanel();
	private FlexTable fTable = new FlexTable();
	
	VerticalPanel vp = new VerticalPanel();
	
	DecoratorPanel dp = new DecoratorPanel();

	FlexTable psTable = new FlexTable();
	/**
	 * Constructor that sets up elements of the create configuration panel.
	 */
	public ConfigSpreadsheet() {

		initService();

		initWidget(mainPanel);
		
		getDataTypes();
		getDateFormats();
		getRealFormats();
		
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	
		mainPanel.setSpacing(10);
		
		DecoratorPanel dp = new DecoratorPanel();
		mainPanel.add(dp);
		mainPanel.setCellHorizontalAlignment(dp, HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.setWidth(DOCK_PANEL_WIDTH);

		VerticalPanel upperPanel = new VerticalPanel();
		upperPanel.setWidth(PERCENT_100);
		upperPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		upperPanel.setSpacing(10);

		// Sub-Title
		Label srchLabel = new Label(SUB_TITLE);
		srchLabel.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		upperPanel.add(srchLabel);
		upperPanel.setCellHorizontalAlignment(srchLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Sample Loading
		// Workbook Type
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth(RESULTS_DATA_WIDTH);
		hp.setSpacing(10);
		Label workbookConfigLabel = new Label(WORKBOOK_TYPE_COLON);
		hp.add(workbookConfigLabel);
		workbookConfigBox.setName(CONFIG);
		workbookConfigBox.addValueChangeHandler(new WorkbookConfigValueChangeHandler());
		hp.add(workbookConfigBox);
		hp.setCellHorizontalAlignment(workbookConfigBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperPanel.add(hp);
		
		// Worksheet Loading
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setWidth(RESULTS_DATA_WIDTH);
		hp1.setSpacing(10);
		Label worksheetConfigLabel = new Label(WORKSHEET_TYPE_COLON);
		hp1.add(worksheetConfigLabel);
		hp1.add(worksheetConfigBox);
		worksheetConfigBox.setName(EXCEL_FILE);
		hp1.setCellHorizontalAlignment(worksheetConfigBox, HasHorizontalAlignment.ALIGN_RIGHT);
		upperPanel.add(hp1);

		initSheetConfigurationPanel();

		// Meta Data (External)
		initMetadataExtPanel();
		
		// Buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);

		submitButton.addClickHandler(this);
		submitButton.setEnabled(false);
		buttonPanel.add(submitButton);

		clearButton.addClickHandler(this);
		buttonPanel.add(clearButton);

		deleteButton.addClickHandler(this);
		deleteButton.setEnabled(false);
		buttonPanel.add(deleteButton);

		upperPanel.add(buttonPanel);
		upperPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		formPanel.add(upperPanel);
		formPanel.setWidth(PERCENT_100);
		dp.add(formPanel);
		
		initFormPanel();
	}
	
	/**
	 * Private method to retrieve the list of data types.
	 */
	private void getDataTypes() {
		asa.getDataTypes(new AsyncCallback<List<NameValue>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting data types. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null) {
					Iterator<NameValue> it = result.iterator();
					while (it.hasNext()) {
						NameValue nv = it.next();
						dataTypes.add(nv);
					}
					meTypeListBox.clear();
					meTypeListBox.addItem("");
					addTypeNames2ListBox(meTypeListBox);
				}
			}
		});
	}

	/**
	 * Private method to initialize the external meta data panel.
	 */
	private void initMetadataExtPanel() {
		VerticalPanel metaDataPanel = new VerticalPanel();
		metaDataPanel.setWidth(DOCK_PANEL_WIDTH);
		metaDataPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		meTable.setCellSpacing(5);
		meTable.setVisible(false);
		meDecoratorPanel.setVisible(false);
		meDecoratorPanel.add(metaDataPanel);
		mainPanel.add(meDecoratorPanel);
		
		meCheckBox.addClickHandler(this);
		
		metaDataPanel.add(meCheckBox);

		int row = 0;
		int col = 0;
		
		metaDataPanel.add(meTable);
		
		meListBox.setVisibleItemCount(5);
		meListBox.addClickHandler(this);
		meTable.setWidget(row, col, meListBox);
		meTable.getFlexCellFormatter().setRowSpan(row, col, 3);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		col++;
		
		meRemoveButton.setEnabled(false);
		meRemoveButton.addClickHandler(this);
		meTable.setWidget(row, col, meRemoveButton);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		col--;
		
		meMoveUpButton.setEnabled(false);
		meMoveUpButton.addClickHandler(this);
		meTable.setWidget(row, col, meMoveUpButton);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
		meMoveDownButton.setEnabled(false);
		meMoveDownButton.addClickHandler(this);
		meTable.setWidget(row, col, meMoveDownButton);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		col = 0;
		
		meName.addValueChangeHandler(new MENameValueChangeHandler());
		meTable.setWidget(row, col, meName);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		col++;
		
		meTypeListBox.addItem(EMPTY_STRING);
		addTypeNames2ListBox(meTypeListBox);
		meTypeListBox.addChangeHandler(this);
		meTable.setWidget(row, col, meTypeListBox);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		col++;
		
		meDataFormatListBox.setEnabled(false);
		meDataFormatListBox.addChangeHandler(this);
		meTable.setWidget(row, col, meDataFormatListBox);
		
		col++;
		
		meAddButton.setEnabled(false);
		meAddButton.addClickHandler(this);
		meTable.setWidget(row, col, meAddButton);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		col = 0;
		
		meSaveButton.addClickHandler(this);
		meSaveButton.setEnabled(false);
		meTable.setWidget(row, col, meSaveButton);
		meTable.getFlexCellFormatter().setColSpan(row, col, 3);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col++, HasHorizontalAlignment.ALIGN_CENTER);

		meDeleteButton.addClickHandler(this);
		meDeleteButton.setEnabled(true);
		meTable.setWidget(row, col, meDeleteButton);
		meTable.getFlexCellFormatter().setColSpan(row, col, 3);
		meTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * Private method to add data type names to the given ListBox.
	 * 
	 * @param listBox ListBox to add data type names.
	 */
	private void addTypeNames2ListBox(ListBox listBox) {
		Iterator<NameValue> it = dataTypes.iterator();
		while (it.hasNext()) {
			NameValue nv = it.next();
			listBox.addItem(nv.getName());
		}
	}

	/**
	 * Private method to initialize the sheet configuration panel.
	 */
	private void initSheetConfigurationPanel() {
		scDecoratorPanel.setVisible(false);
		VerticalPanel sheetVPanel = new VerticalPanel();
		sheetVPanel.setWidth(PERCENT_100);
		sheetVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		FlexTable sheetTable = new FlexTable();
		sheetVPanel.add(sheetTable);
		scDecoratorPanel.add(sheetVPanel);
		mainPanel.add(scDecoratorPanel);

		int row = 0;
		int col = 0;
		
		Label sheetLabel = new Label(SHEET_PROCESSING_PANEL_TITLE);
		sheetTable.setWidget(row, col, sheetLabel);
		sheetTable.getFlexCellFormatter().setColSpan(row, col, 4);
		sheetTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
		sheetTable.setWidget(row, col, scListBox);
		sheetTable.getFlexCellFormatter().setRowSpan(row, col, 4);
		sheetTable.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		
		col++;
		
		// Row and Column fields
		// Header row
		Label headerRowLabel = new Label(HEADER_ROW_COLON);
		sheetTable.setWidget(row, col, headerRowLabel);
		
		col++;
		
		scHeaderRowBox.addValueChangeHandler(new SheetValueChangeHandler());
		sheetTable.setWidget(row, col, scHeaderRowBox);
		
		col++;
		
		scAddButton.addClickHandler(this);
		scAddButton.setEnabled(false);
		sheetTable.setWidget(row, col++, scAddButton);
		
		row++;
		col = 0;
		
		// Header Column
		Label headerColLabel = new Label(HEADER_COLUMN_COLON);
		sheetTable.setWidget(row, col, headerColLabel);
		
		col++;

		scHeaderColBox.addValueChangeHandler(new SheetValueChangeHandler());
		sheetTable.setWidget(row, col, scHeaderColBox);
		
		row++;
		col = 0;
		
		// Data Row
		Label dataRowLabel = new Label(DATA_ROW_COLON);
		sheetTable.setWidget(row, col, dataRowLabel);
		
		col++;

		scDataRowBox.addValueChangeHandler(new SheetValueChangeHandler());
		sheetTable.setWidget(row, col, scDataRowBox);
		
		row++;
		col = 0;
		
		// Data Row
		Label dataColLabel = new Label(DATA_COLUMN_ROW);
		sheetTable.setWidget(row, col, dataColLabel);
		
		col++;

		scDataColBox.addValueChangeHandler(new SheetValueChangeHandler());
		sheetTable.setWidget(row, col, scDataColBox);
		
		row++;
		col = 0;
		
		scMetaDataIntBox.addClickHandler(this);
		sheetTable.setWidget(row, col, scMetaDataIntBox);
		sheetTable.getFlexCellFormatter().setColSpan(row, col, 4);
		sheetTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		col = 0;
		row++;
		
		miDecoratorPanel.setVisible(false);
		miDecoratorPanel.setWidth(PERCENT_100);
		VerticalPanel mdIntVPanel = new VerticalPanel();
		mdIntVPanel.setWidth(PERCENT_100);
		mdIntVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		FlexTable mdIntTable = new FlexTable();
		mdIntVPanel.add(mdIntTable);
		miDecoratorPanel.add(mdIntVPanel);
		sheetTable.setWidget(row, col, miDecoratorPanel);
		sheetTable.getFlexCellFormatter().setColSpan(row, col, 4);
		sheetTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		initMetaDataInternalConfigurationPanel(mdIntTable);
		
		initSheets2ProcessPanel();
	}

	/**
	 * Private method to initialize the sheets to process panel
	 */
	private void initSheets2ProcessPanel() {
		s2pDecoratorPanel.setVisible(false);
		s2pHPanel.setWidth(PERCENT_100);
		s2pHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		s3pHPanel.setWidth(PERCENT_100);
		s3pHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		psTable = new FlexTable();
		s2pHPanel.add(psTable);
		s2pHPanel.setCellHorizontalAlignment(psTable,HasHorizontalAlignment.ALIGN_CENTER);
		s2pVPanel.setWidth(PERCENT_100);
		s2pDecoratorPanel.setWidth(PERCENT_100);
		s2pDecoratorPanel.add(s2pVPanel);
		s2pVPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		s2pVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		s2pVPanel.add(s3pHPanel);
		HTML rulerLabel = new HTML(HORIZONTAL_RULE, false);
		rulerLabel.setWidth(PERCENT_100);
		rulerLabel.setHeight("3px");
		s2pVPanel.add(rulerLabel);
		s2pVPanel.add(s2pHPanel);
		//s2pDecoratorPanel.add(s2pHPanel);
		
		mainPanel.add(s2pDecoratorPanel);
		
		int row = 0;
		int col = 0;
		
		Label sheetLabel = new Label(SHEETS_PROCESSED_TITLE);
		psTable.setWidget(row, col, sheetLabel);
		psTable.getFlexCellFormatter().setColSpan(row, col, 4);
		psTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
		s2pListBox.setVisibleItemCount(5);
		s2pListBox.addClickHandler(this);
		s2pListBox.addChangeHandler(this);
		psTable.setWidget(row, col, s2pListBox);
		psTable.getFlexCellFormatter().setRowSpan(row, col, 2);
		psTable.getFlexCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		
		col++;
		
		s2pRemoveButton.addClickHandler(this);
		s2pRemoveButton.setEnabled(false);
		psTable.setWidget(row, col, s2pRemoveButton);
		psTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		col = 0;
		
		s2pGetHeadersButton.addClickHandler(this);
		s2pGetHeadersButton.setEnabled(false);
		psTable.setWidget(row, col, s2pGetHeadersButton);
		psTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * Private method to initialize the meta data internal configuration panel.
	 * 
	 * @param mdIntTable FlexTable to add elements to.
	 */
	private void initMetaDataInternalConfigurationPanel(FlexTable mdIntTable) {
		int row1 = 0;
		int col1 = 0;
		
		Label startRowLabel = new Label(START_ROW_COLON);
		mdIntTable.setWidget(row1, col1, startRowLabel);
		
		col1++;

		micStartRowBox.addValueChangeHandler(new SheetValueChangeHandler());
		mdIntTable.setWidget(row1, col1, micStartRowBox);
		
		row1++;
		col1 = 0;
		
		Label endRowLabel = new Label(END_ROW_COLON);
		mdIntTable.setWidget(row1, col1, endRowLabel);
		
		col1++;

		micEndRowBox.addValueChangeHandler(new SheetValueChangeHandler());
		mdIntTable.setWidget(row1, col1, micEndRowBox);
		
		row1++;
		col1 = 0;
		
		Label startColLabel = new Label(START_COLUMN_ROW);
		mdIntTable.setWidget(row1, col1, startColLabel);
		
		col1++;

		micStartColBox.addValueChangeHandler(new SheetValueChangeHandler());
		mdIntTable.setWidget(row1, col1, micStartColBox);
		
		row1++;
		col1 = 0;
		
		Label endColLabel = new Label(END_COLUMN_ROW);
		mdIntTable.setWidget(row1, col1, endColLabel);
		
		col1++;

		micEndColBox.addValueChangeHandler(new SheetValueChangeHandler());
		mdIntTable.setWidget(row1, col1, micEndColBox);
	}

	/**
	 * Private method to initialize the FormPanel to upload spreadsheets.
	 */
	private void initFormPanel() {
		String urlTag = GWT.getModuleBaseURL() + URL_SAMPLE_UPLOAD_SERVICE;
		formPanel.setAction(urlTag);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);

		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				serverFileName.setLength(0);
				String result = event.getResults();
				result = cleanUpResults(result);
				// Parse file name and headers from string
				String[] tempStrings = result.split("\\|");
				
				serverFileName.append(tempStrings[0]);
				
				int count = 0;
				for (String item : tempStrings) {
					if (count == 0) {
						count++;
						continue;
					}
					int cnt = scListBox.getItemCount();
					boolean found = false;
					for (int i=0;i<cnt && !found;i++) {
						String s = scListBox.getItemText(i);
						if (s.equals(item)) {
							found = true;
							break;
						}
					}
					if (!found) 
						scListBox.addItem(item);
				}
				
				scDecoratorPanel.setVisible(true);
				s2pDecoratorPanel.setVisible(true);
				meDecoratorPanel.setVisible(true);
			}

			/**
			 * Private method to remove the HTML tags from around file name.
			 */
			private String cleanUpResults(String input) {
				if (input.toLowerCase().startsWith("<")) {
					int index = input.indexOf(">");
					input = input.substring(index + 1, input.length() - 1);
					index = input.indexOf("<");
					input = input.substring(0, index);
				}
				return input;
			}
		});
	}
	
	/**
	 * Private class to handle changes to the external metadata data type list.
	 *  This is used to enable or disable the meAddButton. 
	 * 
	 * @author James Albersheim
	 *
	 */
	private class MENameValueChangeHandler implements ValueChangeHandler<String> {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			meAddButton.setEnabled(false);
			int typeIndex = meTypeListBox.getSelectedIndex();
			if (typeIndex > 0) {
				int dateIndex = meDataFormatListBox.getSelectedIndex();
				String typeItem = meTypeListBox.getItemText(typeIndex);
				if (typeItem.equalsIgnoreCase(DATE_TYPE)) {
					meAddButton.setEnabled(dateIndex > 0);
				} else {
					meAddButton.setEnabled(true);
				}
			} else {
				meAddButton.setEnabled(false);
			}
		}
	}
	
	/**
	 * Private class to handle changes to the workbook configuration name box.
	 *  This is used to enable or disable the submitButton. 
	 * 
	 * @author James Albersheim
	 *
	 */
	private class WorkbookConfigValueChangeHandler implements ValueChangeHandler<String> {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
				submitButton.setEnabled(!workbookConfigBox.getValue().isEmpty());
				deleteButton.setEnabled(!workbookConfigBox.getValue().isEmpty());
		}
	}
	
	/**
	 * Private class to handle changes to the worksheet configuration fields.
	 *  This is used to enable or disable the scAddButton. 
	 * 
	 * @author James Albersheim
	 *
	 */
	private class SheetValueChangeHandler implements ValueChangeHandler<String> {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			// Check fields and enable sheetAddButton.
			boolean turnOffSheetAddButton = (scHeaderRowBox.getValue().isEmpty() || scHeaderColBox.getValue().isEmpty()
					|| scDataRowBox.getValue().isEmpty() || scDataColBox.getValue().isEmpty())
					|| (scMetaDataIntBox.getValue().booleanValue() == true
						&& (micStartRowBox.getValue().isEmpty()
							|| micStartColBox.getValue().isEmpty()
							|| micEndRowBox.getValue().isEmpty()
							|| micEndColBox.getValue().isEmpty()));
			scAddButton.setEnabled(!turnOffSheetAddButton);
		}
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

	/**
	 * Public method to convert an Excel column to an integer.
	 * 
	 * @param excelIndex String Excel column letter
	 * 
	 * @return int converted column index
	 */
	public int convertExcelColumnToIndex(String excelIndex) {
		int ALPHABET_LENGTH = 26;
		HashMap<String, Integer> alphaMap = new HashMap<String, Integer>();
		for (int i = 1; i <= ALPHABET_LENGTH; i++) {
			alphaMap.put(alpha[i-1], new Integer(i));
		}
		int excelIntIndex = -1;
		excelIndex = excelIndex.toUpperCase();
		if (excelIndex != null && !excelIndex.isEmpty()) {
			if (excelIndex.length() == 1) {
				String digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex = alphaMap.get(digit);
				}
			} else if (excelIndex.length() == 2) {
				String digit = excelIndex.substring(1, 2);
				excelIntIndex = alphaMap.get(digit);
				digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex += ALPHABET_LENGTH * alphaMap.get(digit);
				}
			} else if (excelIndex.length() == 3) {
				String digit = excelIndex.substring(2, 3);
				excelIntIndex = alphaMap.get(digit);
				digit = excelIndex.substring(1, 2);
				excelIntIndex += ALPHABET_LENGTH * alphaMap.get(digit);
				digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex += Math.pow(ALPHABET_LENGTH, 2) * alphaMap.get(digit);
				}
			}
		}
		return excelIntIndex;
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == clearButton) {
			workbookConfigBox.setValue(EMPTY_STRING);
			scListBox.clear();
			chPairedListBox.clear();
			chSaveButton.setEnabled(false);
			workbookConfigBox.setValue(EMPTY_STRING);
			chHeaderListBox.clear();
			submitButton.setEnabled(false);
			chAddButton = new Button("Add");
			sheetInfoMap.clear();
			s2pDecoratorPanel.setVisible(false);
			scDecoratorPanel.setVisible(false);
			meTable.setVisible(false);
			meCheckBox.setValue(false);
			vp.remove(fTable);
			dp.remove(vp);
			s2pHPanel.remove(dp);
			fTable = null;
			vp = null;
			dp = null;
		} else if (sender == submitButton) {
			scListBox.clear();
			handleSubmitButton();
		} else if (sender == deleteButton) {
			handleDeleteButton();
		} else if (sender == s2pGetHeadersButton) {
			handleGetHeadersButton();
		} else if (sender == scAddButton) {
			handleAddSheetButton();
		} else if (sender == scMetaDataIntBox) {
			handleInternalMetadataCheckBox(sender);
		} else if (sender == meMoveDownButton) {
			handleExternalMetadataMoveDownButton();
		} else if (sender == meMoveUpButton) {
			handleExternalMetadataMoveUpButton();
		} else if (sender == meRemoveButton) {
			handleExternalMetadataRemoveButton();
		} else if (sender == meListBox) {
			handleExternalMetadataListBox();
		} else if (sender == meAddButton) {
			handleExternalMetadataAddButton();
		} else if (sender == meCheckBox) {
			meTable.setVisible(((CheckBox)sender).getValue());
		} else if (sender == s2pRemoveButton) {
			handleSheets2ProcessRemoveButton();
		} else if (sender == chAddButton) {
			handleCellHeaderAddButton();
		} else if (sender == chRemoveButton) {
			handleCellHeaderRemoveButton();
		} else if (sender == chSaveButton) {
			handleCellHeaderSaveButton();
		} else if (sender == s2pListBox) {
			s2pGetHeadersButton.setEnabled(s2pListBox.getSelectedIndex() != -1);
		} else if (sender == chPairedListBox) {
			int index = chPairedListBox.getSelectedIndex();
			chRemoveButton.setEnabled(index != -1);
		} else if (sender == chHeaderListBox) {
			int index = chHeaderListBox.getSelectedIndex();
			chAddButton.setEnabled(index != -1);
		} else if (sender == miHeaderListBox) {
			int index = miHeaderListBox.getSelectedIndex();
			miAddButton.setEnabled(index != -1);
		} else if (sender == miAddButton) {
			handleInternalMetadataAddButton();
		} else if (sender == miPairedListBox) {
			int index = miPairedListBox.getSelectedIndex();
			miRemoveButton.setEnabled(index != -1);
		} else if (sender == miRemoveButton) {
			handleInternalMetadataRemoveButton();
		} else if (sender == miSaveButton) {
			handleInternalMetadataSaveButton();
		} else if (sender == meSaveButton) {
			handleExternalMetadataSaveButton();
		} else if (sender == meDeleteButton) {
			handleExternalMetadataDeleteButton();
		} else if (sender == miDeleteButton) {
			handleInternalMetadataDeleteButton();
		} else if (sender == chDeleteButton) {
			handleCellHeaderDeleteButton();
		}
	}

	/**
	 * Private method to handle saving external metadata to the
	 *  database.
	 */
	private void handleCellHeaderDeleteButton() {
		boolean decision = Window.confirm("Configuration: delete cell headers for "+wbConfig+"?");
		if (decision == true) { // User Confirmed
			// Delete old configuration
			asa.deleteCellDataHeaders(wbConfig, shConfig, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed to delete cell headers.");
				}
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						Window.alert("Succeeded deleting cell headers.");
					else
						Window.alert("Failed to delete cell headers.");
				}
			});
		} else {
			// do nothing
		}
	}

	/**
	/**
	 * Private method to handle saving external metadata to the
	 *  database.
	 */
	private void handleExternalMetadataDeleteButton() {
		boolean decision = Window.confirm("Configuration: delete external metadata for "+wbConfig+"?");
		if (decision == true) { // User Confirmed
			// Delete old configuration
			asa.deleteExternalMetadataHeaders(wbConfig, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed to delete external metadata.");
				}
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						Window.alert("Succeeded deleting external metadata.");
					else
						Window.alert("Failed to delete external metadata.");
				}
			});
		} else {
			// do nothing
		}
	}

	/**
	 * Private method to handle saving external metadata to the
	 *  database.
	 */
	@SuppressWarnings("deprecation")
	private void handleInternalMetadataDeleteButton() {
		boolean decision = Window.confirm("Configuration: delete internal metadata for "+wbConfig+"?");
		if (decision == true) { // User Confirmed
			// Delete old configuration
			asa.deleteInternalMetadataHeaders(wbConfig, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed to delete internal metadata.");
				}
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						Window.alert("Succeeded deleting internal metadata.");
					else
						Window.alert("Failed to delete internal metadata.");
				}
			});
			miDecoratorPanel.setVisible(false);
			scMetaDataIntBox.setChecked(false);
			//dp.setVisible(false);
		} else {
			// do nothing
		}
	}

	/**
	 * Private method to handle saving external metadata to the
	 *  database.
	 */
	private void handleExternalMetadataSaveButton() {
		int count = meListBox.getItemCount();
		List<NameValue> metaHeaders = new ArrayList<NameValue>();
		for (int i = 0; i < count; i++) {
			NameValue nv = new NameValue();
			String[] tempStrings = meListBox.getItemText(i).split(PAIRED_REGEX);
			nv.setName(tempStrings[0]);
			int dataType = 0;
			String type = tempStrings[1];
			dataType = getDataType(type);
			if (dataType == DATE_TYPE_ID || dataType == REAL_TYPE_ID || dataType == STRING_TYPE_ID) {
				nv.setDataFormat(tempStrings[2]);
			}
			nv.setDataType(dataType);
			nv.setOrder(i + 1);
			nv.setInternal(false);
			metaHeaders.add(nv);
		}
		meListBox.clear();
		meSaveButton.setEnabled(false);
		asa.setMetaDataHeaders(metaHeaders, wbConfig, new MDSaveHeadersCallback());
		asa.alterDataTableForMetaData(metaHeaders, wbConfig, shConfig, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to alter table for external metadata."); 
			}
			@Override
			public void onSuccess(Boolean result) {
				if (result)
					Window.alert("Succeeded altering table for external metadata.");
				else
					Window.alert("Failed to altering table for external metadata.");
			}
		});
	}

	/**
	 * Private method to handle saving internal metadata to the
	 *  database.
	 */
	private void handleInternalMetadataSaveButton() {
		int count = miPairedListBox.getItemCount();
		List<NameValue> metaHeaders = new ArrayList<NameValue>();
		for (int i = 0; i < count; i++) {
			NameValue nv = new NameValue();
			String[] tempStrings = miPairedListBox.getItemText(i).split(PAIRED_REGEX);
			nv.setName(tempStrings[0]);
			int dataType = 0;
			String type = tempStrings[1];
			dataType = getDataType(type);
			nv.setDataType(dataType);
			if (dataType == DATE_TYPE_ID || dataType == REAL_TYPE_ID || dataType == STRING_TYPE_ID) {
				nv.setDataFormat(tempStrings[2]);
			}
			nv.setInternal(true);
			nv.setOrder(i + 1);
			metaHeaders.add(nv);
		}
		miPairedListBox.clear();
		miSaveButton.setEnabled(false);
		asa.setMetaDataHeaders(metaHeaders, wbConfig, new MDSaveHeadersCallback());
		asa.alterDataTableForMetaData(metaHeaders, wbConfig, shConfig, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to alter table for internal metadata.");
			}
			@Override
			public void onSuccess(Boolean result) {
				if (result)
					Window.alert("Succeeded altering table for internal metadata.");
				else
					Window.alert("Failed to altering table for internal metadata.");
			}
		});
	}

	/**
	 * Private method to handle removing internal metadata items
	 *  from the processed list.
	 */
	private void handleInternalMetadataRemoveButton() {
		int index = miPairedListBox.getSelectedIndex();
		String paired = miPairedListBox.getItemText(index);
		miPairedListBox.removeItem(index);
		String[] tempStrings = paired.split(PAIRED_REGEX);
		miHeaderListBox.addItem(tempStrings[0]);
	}

	/**
	 * Private method to handle add internal metadata items to
	 *  the processed list. A type must have been selected. And if a date
	 *  or real type is selected then the format must also be selected.
	 */
	private void handleInternalMetadataAddButton() {
		int index = miHeaderListBox.getSelectedIndex();
		String header = miHeaderListBox.getItemText(index);
		miHeaderListBox.removeItem(index);
		if (miTypeListBox.getSelectedIndex() != 0) {
			String type = miTypeListBox.getItemText(miTypeListBox.getSelectedIndex());
			int dfIndex = miDataFormatListBox.getSelectedIndex();
			StringBuffer item = new StringBuffer(header + PAIRED_SEPARATOR + type);
			if ((type.equalsIgnoreCase(DATE_TYPE) || type.equalsIgnoreCase(REAL_TYPE) || type.equalsIgnoreCase(STRING_TYPE))
					&& dfIndex != 0) {
				String dataFormat = miDataFormatListBox.getItemText(dfIndex);
				item.append(PAIRED_SEPARATOR + dataFormat);
			}
			miPairedListBox.addItem(item.toString());
			if (miHeaderListBox.getItemCount() == 0)
				miSaveButton.setEnabled(true);
			miPairedListBox.setSelectedIndex(miPairedListBox.getItemCount() - 1);
			miHeaderListBox.setFocus(true);
		}
		miHeaderListBox.setSelectedIndex(index);
	}

	/**
	 * Private method to handle saving the processed cell headers to
	 *  the database.
	 */
	private void handleCellHeaderSaveButton() {
		int count = chPairedListBox.getItemCount();
		List<NameValue> cellHeaders = new ArrayList<NameValue>();
		for (int i = 0; i < count; i++) {
			NameValue nv = new NameValue();
			String[] tempStrings = chPairedListBox.getItemText(i).split(PAIRED_REGEX);
			nv.setName(tempStrings[0]);
			//Window.alert("name="+tempStrings[0]);
			int dataType = 0;
			String type = tempStrings[1];
			//Window.alert("type="+type);
			dataType = getDataType(type);
			nv.setDataType(dataType);
			if (dataType == DATE_TYPE_ID || dataType == REAL_TYPE_ID || dataType == STRING_TYPE_ID) {
				nv.setDataFormat(tempStrings[2]);
				//Window.alert("format="+tempStrings[2]);
			}
			nv.setOrder(i + 1);
			cellHeaders.add(nv);
		}
		chPairedListBox.clear();
		chSaveButton.setEnabled(false);
		asa.setCellDataHeaders(cellHeaders, wbConfig, sheetName, new CHSaveHeadersCallback());
		// TODO - clear up here between config creations
		workbookConfigBox.setValue(EMPTY_STRING);
		chHeaderListBox.clear();
		submitButton.setEnabled(false);
		chAddButton = new Button("Add");
		chSaveButton = new Button("Save");
		chTypeListBox = new ListBox();
		sheetInfoMap.clear();
		s2pDecoratorPanel.setVisible(false);
		scDecoratorPanel.setVisible(false);
		meTable.setVisible(false);
		meCheckBox.setValue(false);
		vp.remove(fTable);
		dp.remove(vp);
		s2pHPanel.remove(dp);
		fTable = null;
		vp = null;
		dp = null;
	}

	/**
	 * Private method to handle removing cell header items
	 *  from the processed list.
	 */
	private void handleCellHeaderRemoveButton() {
		int index = chPairedListBox.getSelectedIndex();
		String paired = chPairedListBox.getItemText(index);
		chPairedListBox.removeItem(index);
		String[] tempStrings = paired.split(PAIRED_REGEX);
		chHeaderListBox.addItem(tempStrings[0]);
	}

	/**
	 * Private method to handle adding a cell header to the processed
	 *  list. A type must have been selected. And if a date
	 *  or real type is selected then the format must also be selected.
	 */
	private void handleCellHeaderAddButton() {
		int index = chHeaderListBox.getSelectedIndex();
		String header = chHeaderListBox.getItemText(index);
		chHeaderListBox.removeItem(index);
		if (chTypeListBox.getSelectedIndex() != 0) {
			String type = chTypeListBox.getItemText(chTypeListBox.getSelectedIndex());
			StringBuffer pairedString = new StringBuffer(header + PAIRED_SEPARATOR + type);
			int dataIndex = chDataFormatListBox.getSelectedIndex();
			if ((type.equalsIgnoreCase(DATE_TYPE) || type.equalsIgnoreCase(REAL_TYPE) || type.equalsIgnoreCase(STRING_TYPE))
					&& dataIndex > 0) {
				String dataFormat = chDataFormatListBox.getItemText(dataIndex);
				pairedString.append(PAIRED_SEPARATOR + dataFormat);
			}
			chPairedListBox.addItem(pairedString.toString());
			
			chSaveButton.setEnabled(chHeaderListBox.getItemCount() == 0);
			chPairedListBox.setSelectedIndex(chPairedListBox.getItemCount() - 1);
			chHeaderListBox.setFocus(true);
		}
		chHeaderListBox.setSelectedIndex(index);
	}

	/**
	 * Private method to handle removing items from the sheets to 
	 *  process list.
	 */
	private void handleSheets2ProcessRemoveButton() {
		int index = s2pListBox.getSelectedIndex();
		String key = s2pListBox.getItemText(index);
		s2pListBox.removeItem(s2pListBox.getSelectedIndex());
		sheetInfoMap.remove(key);
		s2pRemoveButton.setEnabled(false);
		s2pGetHeadersButton.setEnabled(false);
		scListBox.addItem(key);
	}

	/**
	 * Private method to handle adding external metadata items to the
	 *  processed list. A type must have been selected. And if a date
	 *  or real type is selected then the format must also be selected.
	 */
	private void handleExternalMetadataAddButton() {
		String name = meName.getValue();
		String type = meTypeListBox.getItemText(meTypeListBox.getSelectedIndex());
		StringBuffer item = new StringBuffer(name + PAIRED_SEPARATOR + type);
		int dateIndex = meDataFormatListBox.getSelectedIndex();
		if ((type.equalsIgnoreCase(DATE_TYPE) || type.equalsIgnoreCase(REAL_TYPE) || type.equalsIgnoreCase(STRING_TYPE))
				&& dateIndex > 0) {
			String dateFormat = meDataFormatListBox.getItemText(dateIndex);
			item.append(PAIRED_SEPARATOR + dateFormat);
		}
		meListBox.addItem(item.toString());
		meName.setValue(EMPTY_STRING);
		meTypeListBox.setSelectedIndex(0);
		meSaveButton.setEnabled(true);
	}

	/**
	 * Private method to handle changes in the external metadata
	 *  list. This method enables/disables buttons.
	 */
	private void handleExternalMetadataListBox() {
		meRemoveButton.setEnabled(true);
		if (meListBox.getItemCount() > 0) {
			boolean turnOnMoveUpButton = meListBox.getSelectedIndex() != 0;
			meMoveUpButton.setEnabled(turnOnMoveUpButton);
			boolean turnOnMoveDownButton = meListBox.getSelectedIndex()
											< (meListBox.getItemCount() - 1);
			meMoveDownButton.setEnabled(turnOnMoveDownButton);
		}
	}

	/**
	 * Private method to handle removing an external metadata
	 *  item from the list.
	 */
	private void handleExternalMetadataRemoveButton() {
		meListBox.removeItem(meListBox.getSelectedIndex());
		boolean turnOnMdExtSaveButton = meListBox.getItemCount() != 0;
		meSaveButton.setEnabled(turnOnMdExtSaveButton);
	}

	/**
	 * Private method to handle moving an external metadata item up
	 *  in the list.
	 */
	private void handleExternalMetadataMoveUpButton() {
		int index = meListBox.getSelectedIndex();
		String selected = meListBox.getItemText(index);
		meListBox.removeItem(index);
		meListBox.insertItem(selected, index - 1);
	}

	/**
	 * Private method to handle moving an external metadata item down
	 *  in the list.
	 */
	private void handleExternalMetadataMoveDownButton() {
		int index = meListBox.getSelectedIndex();
		String selected = meListBox.getItemText(index);
		meListBox.removeItem(index);
		meListBox.insertItem(selected, index + 1);
	}

	/**
	 * Private method to show and hide the internal metadata panel.
	 * 
	 * @param sender Widget Internal metadata checkbox
	 */
	private void handleInternalMetadataCheckBox(Widget sender) {
		miDecoratorPanel.setVisible(((CheckBox)sender).getValue());
	}

	/**
	 * Private method to handle the add sheet button to the sheets to process
	 *  list.
	 */
	private void handleAddSheetButton() {
		String sheetName = scListBox.getItemText(scListBox.getSelectedIndex());
		shConfig = sheetName;
		if (!sheetName.isEmpty()) {
			s2pListBox.addItem(sheetName);
			SheetInfoDTO sheetInfo = new SheetInfoDTO();
			String headerRow = scHeaderRowBox.getValue();
			if (!headerRow.isEmpty()) {
				try {
					int headerRowInt = Integer.valueOf(headerRow);
					sheetInfo.setHeaderRow(headerRowInt);
				} catch (NumberFormatException e) {
					Window.alert("The header row is not a valid number.");
					return;
				}
			}
			String dataRow = scDataRowBox.getValue();
			if (!dataRow.isEmpty()) {
				try {
					int dataRowInt = Integer.valueOf(dataRow);
					sheetInfo.setDataRow(dataRowInt);
				} catch (NumberFormatException e) {
					Window.alert("The data row is not a valid number.");
					return;
				}
			}
			String headerCol = scHeaderColBox.getValue();
			if (!headerCol.isEmpty()) {
				try {
					int headerColInt = convertExcelColumnToIndex(headerCol);
					sheetInfo.setHeaderColumn(headerColInt);
				} catch (NumberFormatException e) {
					Window.alert("The header column is not a valid number.");
					return;
				}
			}
			String dataCol = scDataColBox.getValue();
			if (!dataCol.isEmpty()) {
				try {
					int dataColInt = convertExcelColumnToIndex(dataCol);
					sheetInfo.setDataColumn(dataColInt);
				} catch (NumberFormatException e) {
					Window.alert("The data column is not a valid number.");
					return;
				}
			}
			sheetInfo.setHasMetaData(scMetaDataIntBox.getValue().booleanValue());
			String startRow = micStartRowBox.getValue();
			if (!startRow.isEmpty()) {
				try {
					int startRowInt = Integer.valueOf(startRow);
					sheetInfo.setStartMetaRow(startRowInt);
				} catch (NumberFormatException e) {
					Window.alert("The start row is not a valid number.");
					return;
				}
			}
			String endRow = micEndRowBox.getValue();
			if (!endRow.isEmpty()) {
				try {
					int endRowInt = Integer.valueOf(endRow);
					sheetInfo.setEndMetaRow(endRowInt);
				} catch (NumberFormatException e) {
					Window.alert("The end row is not a valid number.");
					return;
				}
			}
			String startCol = micStartColBox.getValue();
			if (!startCol.isEmpty()) {
				try {
					int startColInt = convertExcelColumnToIndex(startCol);
					sheetInfo.setStartMetaColumn(startColInt);
				} catch (NumberFormatException e) {
					Window.alert("The start column is not a valid number.");
					return;
				}
			}
			String endCol = micEndColBox.getValue();
			if (!endCol.isEmpty()) {
				try {
					int endColInt = convertExcelColumnToIndex(endCol);
					sheetInfo.setEndMetaColumn(endColInt);
				} catch (NumberFormatException e) {
					Window.alert("The end column is not a valid number.");
					return;
				}
			}
			sheetInfoMap.put(sheetName, sheetInfo);
			scListBox.removeItem(scListBox.getSelectedIndex());
		}
	}

	/**
	 * Private method to handle retrieving the column headers and meta data headers
	 *  for the given worksheet. This method saves the worksheet configuration data, first.
	 */
	private void handleGetHeadersButton() {
		//Window.alert("Please wait...retrieving headers from worksheet");
		WaitDialog wait = new WaitDialog("");
		wait.show();
		int index = s2pListBox.getSelectedIndex();
		if (index != -1) {
			String sheetName = s2pListBox.getItemText(index);
			SheetInfoDTO sheetInfo = sheetInfoMap.get(sheetName);
			List<NameValue> specs = new ArrayList<NameValue>();
			NameValue nameValue = new NameValue();
			nameValue.setName(HDR_ROW);
			nameValue.setValue(String.valueOf(sheetInfo.getHeaderRow()));
			specs.add(nameValue);
			nameValue = new NameValue();
			nameValue.setName(HDR_COL);
			nameValue.setValue(String.valueOf(sheetInfo.getHeaderColumn()));
			specs.add(nameValue);
			nameValue = new NameValue();
			nameValue.setName(DATA_ROW);
			nameValue.setValue(String.valueOf(sheetInfo.getDataRow()));
			specs.add(nameValue);
			nameValue = new NameValue();
			nameValue.setName(DATA_COL);
			nameValue.setValue(String.valueOf(sheetInfo.getDataColumn()));
			specs.add(nameValue);
			if (sheetInfo.isHasMetaData()) {
				nameValue = new NameValue();
				nameValue.setName(META_START_ROW);
				nameValue.setValue(String.valueOf(sheetInfo.getStartMetaRow()));
				specs.add(nameValue);
				nameValue = new NameValue();
				nameValue.setName(META_START_COL);
				nameValue.setValue(String.valueOf(sheetInfo.getStartMetaColumn()));
				specs.add(nameValue);
				nameValue = new NameValue();
				nameValue.setName(META_END_ROW);
				nameValue.setValue(String.valueOf(sheetInfo.getEndMetaRow()));
				specs.add(nameValue);
				nameValue = new NameValue();
				nameValue.setName(META_END_COL);
				nameValue.setValue(String.valueOf(sheetInfo.getEndMetaColumn()));
				specs.add(nameValue);
			}
			s2pHPanel.remove(psTable);
			asa.setSheetConfig(specs, wbConfig, sheetName, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Problem saving configuration. Error: " + caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(String result) {
					asa.getInternalMetadataHeaders(wbConfig, serverFileName.toString(), result, new GetInternalMetadataHeadersCallback());
					asa.getCellHeaders(wbConfig, serverFileName.toString(), result, new GetCellHeadersCallback());
				}
			});
			s2pListBox.removeItem(index);
			s2pGetHeadersButton.setEnabled(false);
		}
		wait.hide();
	}
	
	/**
	 * Private method retrieve a data type ID given a data type name.
	 * 
	 * @param type String Data type name.
	 * 
	 * @return int Data type ID.
	 */
	private int getDataType(String type) {
		int dataType = 0;
		if (type.equalsIgnoreCase(LONG_TYPE))
			dataType = LONG_TYPE_ID;
		else if (type.equalsIgnoreCase(REAL_TYPE))
			dataType = REAL_TYPE_ID;
		else if (type.equalsIgnoreCase(DATE_TYPE))
			dataType = DATE_TYPE_ID;
		else if (type.equalsIgnoreCase(STRING_TYPE))
			dataType = STRING_TYPE_ID;
		else if (type.equalsIgnoreCase(BOOLEAN_TYPE))
			dataType = BOOLEAN_TYPE_ID;
		
		return dataType;
	}

	/**
	 * Private method that submits a workbook configuration name and sample file
	 *  to the server. If the name already exists then this method will make a call
	 *  delete the old version if the user requests.
	 */
	private void handleDeleteButton() {
		//if (!worksheetConfigBox.getFilename().isEmpty()) {
			// Check to see if config exists.
			wbConfig = workbookConfigBox.getValue();
			asa.workbookConfigExists(wbConfig, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Problem checking database. Error: " + caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result != null) {
						if (result.booleanValue() == true) { // Found in DB
							// Configuration exists in database, already.
							boolean decision = Window.confirm("Configuration, " + wbConfig + ", was found to exist in the database.\nDelete this configuration?");
							if (decision == true) { // User Confirmed
								// Delete old configuration
								asa.deleteWorkbookConfig(wbConfig, new AsyncCallback<Boolean>() {
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Failed to delete workbook config, "+wbConfig);
									}
									@Override
									public void onSuccess(Boolean result) {
										if (result)
											Window.alert("Successfully deleted workbook config, "+wbConfig);
										else
											Window.alert("Failed to delete workbook config, "+wbConfig);
									}
								});
								scDecoratorPanel.setVisible(false);
								s2pDecoratorPanel.setVisible(false);
								meDecoratorPanel.setVisible(false);
								workbookConfigBox.setValue(EMPTY_STRING);
								deleteButton.setEnabled(false);
								miDecoratorPanel.setVisible(false);
								//dp.setVisible(false);
							} else { // User Cancelled
								return;
							}
						} else { // Not found in DB
							// do nothing
						}
					}
				}
			});
		//} else {
		//	Window.alert("Please select a sample spreadsheet before submitting.");
		//}
	}
	
	/**
	 * Private method that submits a workbook configuration name and sample file
	 *  to the server. If the name already exists then this method will make a call
	 *  delete the old version if the user requests.
	 */
	private void handleSubmitButton() {
		if (!worksheetConfigBox.getFilename().isEmpty()) {
			// Check to see if config exists.
			wbConfig = workbookConfigBox.getValue();
			asa.workbookConfigExists(wbConfig, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Problem checking database. Error: " + caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result != null) {
						if (result.booleanValue() == true) { // Found in DB
							// Configuration exists in database, already.
							boolean decision = Window.confirm("Configuration, " + wbConfig + ", was found to exist in the database.\nReplace this configuration with the new data?");
							if (decision == true) { // User Confirmed
								// Delete old configuration
								// TODO: Add call to server to delete existing configuration
								// Submit new configuration
								formSubmission();
							} else { // User Cancelled
								return;
							}
						} else { // Not found in DB
							formSubmission();
						}
					}
				}

				/**
				 * Private method to submit the form to the server.
				 */
				private void formSubmission() {
					formPanel.submit();
					scListBox.clear();
				}
			});
		} else {
			Window.alert("Please select a sample spreadsheet before submitting.");
		}
	}
	
	/**
	 * Private class that handles the callback for the getCellHeaders call.
	 * @author James Albersheim
	 *
	 */
	private class GetCellHeadersCallback implements AsyncCallback<SheetCellHeaders> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("A problem occurred when we were getting headers. Error: " + caught.getLocalizedMessage());
		}
	

		@Override
		public void onSuccess(SheetCellHeaders result) {
			if (result != null && result.getSheet() != null && !result.getSheet().isEmpty()) {
				Window.alert("Successful. sheet: " + result.getSheet());
				setUpCellHeaderPanel(result);
			} else {
				Window.alert("There were no headers returned.");
			}
		}
	}
	
	/**
	 * Private class to handle the callback for the getInternalMetadataHeaders call.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class GetInternalMetadataHeadersCallback implements AsyncCallback<List<String>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("A problem occurred when we were getting metadata headers. Error: " + caught.getLocalizedMessage());
		}

		@Override
		public void onSuccess(List<String> result) {
			if (result != null) {
				//Window.alert("Successful.");
				setUpMetadataIntPanel(result);
			} else {
				Window.alert("There were no headers returned.");
			}
		}
	}
	
	/**
	 * Private method to set up the widgets for the internal meta data panel.
	 * 
	 * @param metadataNames List<String> Internal meta data names
	 */
	private void setUpMetadataIntPanel(List<String> metadataNames) {
		
		if (!(metadataNames != null && !metadataNames.isEmpty())) return;
		
		FlexTable fTable = new FlexTable();
		vpint.add(fTable);
		
		int row = 0;
		int col = 0;
		
		Label sheetLabel = new Label(INTERNAL_METADATA_CONFIGURATION_TITLE);
		fTable.setWidget(row, col, sheetLabel);
		fTable.getFlexCellFormatter().setColSpan(row, col, 5);
		fTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
		miHeaderListBox.setVisibleItemCount(5);
		miHeaderListBox.addChangeHandler(this);
		miHeaderListBox.addClickHandler(this);
		Iterator<String> it = metadataNames.iterator();
		while (it.hasNext()) {
			String header = it.next();
			miHeaderListBox.addItem(header);
		}
		fTable.setWidget(row, col, miHeaderListBox);
		fTable.getFlexCellFormatter().setRowSpan(row, col, 2);
		
		col++;
		
		miTypeListBox.addItem(EMPTY_STRING);
		addTypeNames2ListBox(miTypeListBox);
		miTypeListBox.addChangeHandler(this);
		fTable.setWidget(row, col, miTypeListBox);
		
		col++;		

		miDataFormatListBox.setEnabled(false);
		miDataFormatListBox.addChangeHandler(this);
		fTable.setWidget(row, col, miDataFormatListBox);
		
		col++;
		
		miAddButton .addClickHandler(this);
		miAddButton.setEnabled(false);
		fTable.setWidget(row, col, miAddButton);
		
		col++;
		
		miPairedListBox.setVisibleItemCount(5);
		miPairedListBox.addChangeHandler(this);
		miPairedListBox.addClickHandler(this);
		fTable.setWidget(row, col, miPairedListBox);
		fTable.getFlexCellFormatter().setRowSpan(row, col, 2);
		
		col++;
		
		miSaveButton.addClickHandler(this);
		miSaveButton.setEnabled(false);
		fTable.setWidget(row, col++, miSaveButton);
		
		miDeleteButton.addClickHandler(this);
		miDeleteButton.setEnabled(true);
		fTable.setWidget(row, col, miDeleteButton);

		row++;
		col = 0;
		
		miRemoveButton.addClickHandler(this);
		miRemoveButton.setEnabled(false);
		fTable.setWidget(row, col, miRemoveButton);
		fTable.getFlexCellFormatter().setColSpan(row, col, 3);
		fTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * Private method to retrieve the date formats from the database.
	 * 
	 * @param listBox ListBox to add date formats to.
	 */
	private void loadDateFormats(ListBox listBox) {
		listBox.clear();
		listBox.addItem("");
		if (dateFormats != null && !dateFormats.isEmpty()) {
			Iterator<NameValue> it = dateFormats.iterator();
			while (it.hasNext()) {
				NameValue nameValue = it.next();
				listBox.addItem(nameValue.getName());
			}
		}
	}

	/**
	 * Private method to retrieve the real formats from the database.
	 * 
	 * @param listBox ListBox to add real formats to.
	 */
	private void loadRealFormats(ListBox listBox) {
		listBox.clear();
		listBox.addItem("");
		if (realFormats != null && !realFormats.isEmpty()) {
			Iterator<NameValue> it = realFormats.iterator();
			while (it.hasNext()) {
				NameValue nameValue = it.next();
				listBox.addItem(nameValue.getName());
			}
		}
	}
	
	/**
	 * Private method to retrieve the real formats from the database.
	 * 
	 * @param listBox ListBox to add real formats to.
	 */
	private void loadStringFormats(ListBox listBox) {
		listBox.clear();
		listBox.addItem("");
		for (int i=20; i<251; i+=20) {
			listBox.addItem(String.valueOf(i));
		}
	}
	
	/**
	 * Private method to call server to retrieve date formats and
	 *  insert them into a list to be accessed later.
	 */
	private void getDateFormats() {
		asa.getDataFormats(DATE_TYPE, new AsyncCallback<List<NameValue>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting date formats. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null) {
					Iterator<NameValue> it = result.iterator();
					while (it.hasNext()) {
						NameValue nv = it.next();
						dateFormats.add(nv);
					}
				}
			}
		});
	}
	
	/**
	 * Private method to call server to retrieve real formats and
	 *  insert them into a list to be accessed later.
	 */
	private void getRealFormats() {
		asa.getDataFormats(REAL_TYPE, new AsyncCallback<List<NameValue>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting real formats. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null) {
					Iterator<NameValue> it = result.iterator();
					while (it.hasNext()) {
						NameValue nv = it.next();
						realFormats.add(nv);
					}
				}
			}
		});
	}

	/**
	 * Private method to set up the cell header widgets
	 * 
	 * @param cellHeaders SheetCellHeaders worksheet column header information
	 */
	private void setUpCellHeaderPanel(SheetCellHeaders cellHeaders) {
		// Sheet Processing
		s3pHPanel.add(dpint);
		vpint.setWidth(PERCENT_100);
		vpint.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dpint.add(vpint);
		dpint.setVisible(true);

		dp = new DecoratorPanel();
		s2pHPanel.add(dp);
				
		vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dp.add(vp);
		
		//FlexTable 
		fTable = new FlexTable();
		vp.add(fTable);
		
		int row = 0;
		int col = 0;
		
		Label sheetLabel = new Label(CELL_DATA_HEADERS_CONFIGURATION_TITLE);
		fTable.setWidget(row, col, sheetLabel);
		fTable.getFlexCellFormatter().setColSpan(row, col, 5);
		fTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
		chHeaderListBox.setVisibleItemCount(5);
		chHeaderListBox.addChangeHandler(this);
		chHeaderListBox.addClickHandler(this);
		sheetName = cellHeaders.getSheet();
		List<String> headers = cellHeaders.getHeaders();
		Iterator<String> it = headers.iterator();
		while (it.hasNext()) {
			String header = it.next();
			chHeaderListBox.addItem(header);
		}
		fTable.setWidget(row, col, chHeaderListBox);
		fTable.getFlexCellFormatter().setRowSpan(row, col, 2);
		
		col++;
		
		chTypeListBox.addItem(EMPTY_STRING);
		addTypeNames2ListBox(chTypeListBox);
		chTypeListBox.addChangeHandler(this);
		fTable.setWidget(row, col, chTypeListBox);
		
		col++;
		
		chDataFormatListBox.setEnabled(false);
		chDataFormatListBox.addChangeHandler(this);
		fTable.setWidget(row, col, chDataFormatListBox);
		
		col++;
		
		chAddButton.addClickHandler(this);
		chAddButton.setEnabled(false);
		fTable.setWidget(row, col, chAddButton);
		
		col++;
		
		chPairedListBox.setVisibleItemCount(5);
		chPairedListBox.addChangeHandler(this);
		chPairedListBox.addClickHandler(this);
		fTable.setWidget(row, col, chPairedListBox);
		fTable.getFlexCellFormatter().setRowSpan(row, col, 2);
		
		col++;
		
		chSaveButton.addClickHandler(this);
		chSaveButton.setEnabled(false);
		fTable.setWidget(row, col++, chSaveButton);
		
		//chDeleteButton.addClickHandler(this);
		//chDeleteButton.setEnabled(true);
		//fTable.setWidget(row, col, chDeleteButton);
		
		row++;
		col = 0;
		
		chRemoveButton.addClickHandler(this);
		chRemoveButton.setEnabled(false);
		fTable.setWidget(row, col, chRemoveButton);
		fTable.getFlexCellFormatter().setColSpan(row, col, 3);
		fTable.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * Private class to handle the callback for the setCellHeaders call.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class CHSaveHeadersCallback implements AsyncCallback<Boolean> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("There was an error saving the headers. Error: " + caught.getLocalizedMessage());
		}

		@Override
		public void onSuccess(Boolean result) {
			if (!result)
				Window.alert("There were problems saving the configuration.\nPlease see the logs for more information. ");
			else
				Window.alert("Saving of headers was successful.");
		}
	}

	/**
	 * Private class to handle the callback for the setMetadataHeaders call.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class MDSaveHeadersCallback implements AsyncCallback<Boolean> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("There was an error saving the headers. Error: " + caught.getLocalizedMessage());
		}

		@Override
		public void onSuccess(Boolean result) {
			Window.alert("Saving of headers was successful.");
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == meTypeListBox) {
			handleMETypeListBoxChanges();
		} else if (sender == miTypeListBox) {
			handleMITypeListBox();
		} else if (sender == chTypeListBox) {
			handleCHTypeListBox();
		} else if (sender == s2pListBox) {
			int index = s2pListBox.getSelectedIndex();
			s2pRemoveButton.setEnabled(index != -1);
			s2pGetHeadersButton.setEnabled(index != -1);
		} else if (sender == chPairedListBox) {
			int index = chPairedListBox.getSelectedIndex();
			chRemoveButton.setEnabled(index != -1);
		} else if (sender == miPairedListBox) {
			int index = miPairedListBox.getSelectedIndex();
			meRemoveButton.setEnabled(index != -1);
		} else if (sender == chHeaderListBox) {
			int index = chHeaderListBox.getSelectedIndex();
			chAddButton.setEnabled(index != -1);
		} else if (sender == miHeaderListBox) {
			int index = miHeaderListBox.getSelectedIndex();
			miAddButton.setEnabled(index != -1);
		} else if (sender == miDataFormatListBox) {
			miAddButton.setEnabled(miDataFormatListBox.getSelectedIndex() > 0);
		} else if (sender == meDataFormatListBox) {
			meAddButton.setEnabled(meDataFormatListBox.getSelectedIndex() > 0);
		} else if (sender == chDataFormatListBox) {
			chAddButton.setEnabled(chDataFormatListBox.getSelectedIndex() > 0);
		}
		//chDeleteButton.setEnabled(true);
	}

	/**
	 * Private method to handle changes to the external metadata type
	 *  list box. This method enables/disables various widgets based
	 *  on which type is selected.
	 */
	private void handleCHTypeListBox() {
		int typeIndex = chTypeListBox.getSelectedIndex();
		String typeItem = chTypeListBox.getItemText(typeIndex);
		if (typeItem.equalsIgnoreCase(DATE_TYPE)) {
			chDataFormatListBox.setEnabled(true);
			loadDateFormats(chDataFormatListBox);
			chAddButton.setEnabled(false);
		} else if (typeItem.equalsIgnoreCase(REAL_TYPE)) {
			chDataFormatListBox.setEnabled(true);
			loadRealFormats(chDataFormatListBox);
			chAddButton.setEnabled(false);
		} else if (typeItem.equalsIgnoreCase(STRING_TYPE)) {
			chDataFormatListBox.setEnabled(true);
			loadStringFormats(chDataFormatListBox);
			chAddButton.setEnabled(false);
		} else if (!typeItem.equalsIgnoreCase(DATE_TYPE) && typeIndex > 0) {
			chDataFormatListBox.setEnabled(false);
			chAddButton.setEnabled(true);
		} else {
			chDataFormatListBox.setEnabled(false);
			chAddButton.setEnabled(false);
		}
	}

	/**
	 * Private method to handle changes to the internal metadata type
	 *  list box. This method enables/disables various widgets based
	 *  on which type is selected.
	 */
	private void handleMITypeListBox() {
		int typeIndex = miTypeListBox.getSelectedIndex();
		String typeItem = miTypeListBox.getItemText(typeIndex);
		if (typeItem.equalsIgnoreCase(DATE_TYPE)) {
			miDataFormatListBox.setEnabled(true);
			loadDateFormats(miDataFormatListBox);
			miAddButton.setEnabled(false);
		} else if (typeItem.equalsIgnoreCase(REAL_TYPE)) {
			miDataFormatListBox.setEnabled(true);
			loadRealFormats(miDataFormatListBox);
			miAddButton.setEnabled(false);
		} else if (typeItem.equalsIgnoreCase(STRING_TYPE)) {
			miDataFormatListBox.setEnabled(true);
			loadStringFormats(miDataFormatListBox);
			miAddButton.setEnabled(false);
		} else if (!typeItem.equalsIgnoreCase(DATE_TYPE) && typeIndex > 0) {
			miDataFormatListBox.setEnabled(false);
			miAddButton.setEnabled(true);
		} else {
			miDataFormatListBox.setEnabled(false);
			miAddButton.setEnabled(false);
		}
	}

	/**
	 * Private method to handle changes to the external metadata type
	 *  list box. This method enables/disables various widgets based
	 *  on which type is selected.
	 */
	private void handleMETypeListBoxChanges() {
		int index = meTypeListBox.getSelectedIndex();
		boolean turnOn = index > 0 && !meName.getValue().isEmpty();
		if (turnOn) {
			String typeItem = meTypeListBox.getItemText(index);
			if (typeItem.equalsIgnoreCase(DATE_TYPE)) {
				meDataFormatListBox.setEnabled(true);
				loadDateFormats(meDataFormatListBox);
				meAddButton.setEnabled(false);						
			} else if (typeItem.equalsIgnoreCase(REAL_TYPE)) {
				meDataFormatListBox.setEnabled(true);
				loadRealFormats(meDataFormatListBox);
				meAddButton.setEnabled(false);						
			
			} else if (typeItem.equalsIgnoreCase(STRING_TYPE)) {
				meDataFormatListBox.setEnabled(true);
				loadStringFormats(meDataFormatListBox);
				meAddButton.setEnabled(false);						
			
			} else {
				meDataFormatListBox.setEnabled(false);
				meAddButton.setEnabled(true);
			}
		} else {
			meDataFormatListBox.setEnabled(false);
			meAddButton.setEnabled(false);
		}
	}
}
