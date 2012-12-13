package gov.nrel.nbc.spreadsheet.client;

import java.util.Collection;
import java.util.Date;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

/**
 * Public class to hold the upload compositional analysis panel.
 * 
 * @author James Albersheim
 * 
 */
public class UploadSpreadSheet extends Composite implements AppConstants, ClickHandler, ChangeHandler {
	
	private static final String ATTACHMENTS = "Attachments";

	private static final String SPREADSHEET_LAYOUT = "Spreadsheet Layout: ";
	
	private static final String CREATE = "Create";
	
	private static final String MODIFY = "Modify";
	
	private static final String EMPTY = " empty.\n";
	
	private static final String SPREADSHEET = "Spreadsheet:";

	private static final String UPLOAD = "Upload";

	private static final String WORKBOOK_CONFIG_NAME = "workbookConfigName";

	private static final String EMPTY_STRING = "";

	private static final String HORIZONTAL_RULE = "<HR>";

	private static final String UPLOAD_IN_PROGRESS = "Upload in progress. Please wait...";

	private static final String WAIT_JPG = "hourglass1.jpg";

	private static final String UPLOAD_BUTTON_TEXT = UPLOAD;
	
	private static final String LOAD = "Load Meta Data";
	
	private static final String CREATE_OR_MODIFY_NAME = "c_or_m";
	
	private static final String WORKBOOK_TO_MODIFY_NAME = "workbook2modify";

	private static final String FILE_NAME_NAME = "fileName";
	
	private static final String PATH_NAME_NAME = "pathName";

	private static final String FILE_UPLOAD_NAME = "excelFile";

	private static final String ATTACH_FILE = "attachFile";
		
	private static final String SAMPLE_FILE = "Name of Sample file: ";
	
	private static final String FILE_UPLOAD_LABEL = "Select Excel file to upload:";
	
	private final Label sampleFileLabel = new Label(SAMPLE_FILE);
	
	private static final String WORKBOOK_ID_DEFAULT = "{Automatically set after upload.}";

	private static final String WORKBOOK_ID = "Spreadsheet ID:";

	private static final int METADATA_GRID_COLUMNS = 2;

	//private static final String MAIN_PANEL_HEIGHT = "80%";
	private static final String MAIN_PANEL_HEIGHT = "100%";

	private static final String MAIN_PANEL_WIDTH = "100%";

	//private static final String FORM_PANEL_HEIGHT = "80%";
	private static final String FORM_PANEL_HEIGHT = "100%";

	private static final String FORM_PANEL_WIDTH = "100%";

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";

	/**
	 * String constant for the name of the submission service.
	 */
	private static final String URL_SPREADSHEET_SERVICE = "spreadSheetService";

	/**
	 * String constant for the name of the client upload service.
	 */
	private static final String URL_CALC_CLIENT_UPLOAD_SERVICE = "clientUploadService";

	/**
	 * Holder for SubmissionService
	 */
	private SpreadSheetServiceAsync ssa = null;

	/**
	 * Panel used to send message to servlet to upload spreadsheet and meta
	 * data.
	 */
	private final FormPanel formPanel = new FormPanel();

	private Label workBookID = null;
	
	private final Hidden hiddenWorkbookToModify = new Hidden();
	
	private final Hidden hiddenCreateOrModify = new Hidden();
	
	private final Hidden hiddenFileName = new Hidden();
	
	private final Hidden hiddenPathName = new Hidden();
	
	private final Hidden hiddenHandle = new Hidden();

	private final PopupPanel uploadDialog = new PopupPanel();
	
	/**
	 * GWT FileUpload to handle select of file to upload.
	 */
	private final FileUpload excelFile = new FileUpload();

	private final FileSelectionPanel filePanel = new FileSelectionPanel();

	private final Label workbookConfigLabel = new Label(SPREADSHEET_LAYOUT);

	private final Grid dataGrid = new Grid(0, METADATA_GRID_COLUMNS);

	private final ListBox workbookConfigBox = new ListBox();
	
	private final Label workbookToModifyLabel = new Label(WORKBOOK_ID);

	private final RadioButton createBtn = new RadioButton(CREATE);
	
	private final RadioButton modifyBtn = new RadioButton(MODIFY);
	
	private final RadioButton blankBtn = new RadioButton(AS_BLANKS);
	
	private final RadioButton failedBtn = new RadioButton(FAILED);
	
	private Label sampleFileName = new Label("");
	
	private final TextBox workbookBox = new TextBox();
	
	private final Button loadButton = new Button(LOAD);

	private final VerticalPanel metaDataPanel = new VerticalPanel();

	private final Button importButton = new Button(UPLOAD_BUTTON_TEXT);

	/**
	 * Public constructor
	 */
	public UploadSpreadSheet() {
		initService();

		initWidget(formPanel);
		
		formPanel.setStyleName("FancyPanel");
		
		initFormPanel();

		metaDataPanel.setSpacing(10);
		metaDataPanel.setSize(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
		metaDataPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		metaDataPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		// SubTitle
		Label subTitle = new Label(UPLOAD);
		subTitle.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		metaDataPanel.add(subTitle);
		
		// Workbook ID
		HorizontalPanel hp = new HorizontalPanel();
		Label workBookIDLabel = new Label(WORKBOOK_ID);
		hp.add(workBookIDLabel);
		workBookID = new Label(WORKBOOK_ID_DEFAULT);
		hp.add(workBookID);
		metaDataPanel.add(hp);
		
		// Workbook Configuration Names
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(workbookConfigLabel);
		hp1.add(workbookConfigBox);
		workbookConfigBox.setName(WORKBOOK_CONFIG_NAME);
		loadWorkbookConfigNames();
		metaDataPanel.add(hp1);

		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSpacing(20);
		Label createLabel = new Label(CREATE);
		hp2.add(createLabel);
		hp2.add(createBtn);
		createBtn.setValue(true,false);
		createBtn.addClickHandler(this);
		Label modifyLabel = new Label(MODIFY);
		hp2.add(modifyLabel);
		hp2.add(modifyBtn);
		modifyBtn.setValue(false,false);
		modifyBtn.addClickHandler(this);
		workbookToModifyLabel.setVisible(false);
		workbookBox.addChangeHandler(this);
		loadButton.setVisible(false);
		loadButton.addClickHandler(this);
		hp2.add(workbookToModifyLabel);
		hp2.add(workbookBox);
		workbookBox.setVisible(false);
		hp2.add(loadButton);
		metaDataPanel.add(hp2);

		HTML oneHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		metaDataPanel.add(oneHeadingLabel);
		
		// Meta Data Grid
		metaDataPanel.add(dataGrid);

		HTML twoHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		metaDataPanel.add(twoHeadingLabel);
		
		metaDataPanel.add(sampleFileLabel);
		sampleFileLabel.setVisible(false);
		metaDataPanel.add(sampleFileName);
		
		VerticalPanel vp3 = new VerticalPanel();
		vp3.add(new Label(HANDLE_ERRORS));
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.setSpacing(20);
		blankBtn.setValue(false,false);
		blankBtn.addClickHandler(this);
		failedBtn.setValue(true,false);
		failedBtn.addClickHandler(this);
		hp3.add(new Label(AS_BLANKS));
		hp3.add(blankBtn);
		hp3.add(new Label(FAILED));
		hp3.add(failedBtn);
		vp3.add(hp3);
		metaDataPanel.add(vp3);
		
		// Spreadsheet upload
		Label uploadLabel = new Label(FILE_UPLOAD_LABEL, false);
		metaDataPanel.add(uploadLabel);

		excelFile.setName(FILE_UPLOAD_NAME);
		metaDataPanel.add(excelFile);
		
		// hidden data
		hiddenFileName.setName(FILE_NAME_NAME);
		metaDataPanel.add(hiddenFileName);
		hiddenPathName.setName(PATH_NAME_NAME);
		metaDataPanel.add(hiddenPathName);
		hiddenCreateOrModify.setName(CREATE_OR_MODIFY_NAME);
		metaDataPanel.add(hiddenCreateOrModify);
		hiddenWorkbookToModify.setName(WORKBOOK_TO_MODIFY_NAME);
		metaDataPanel.add(hiddenWorkbookToModify);
		hiddenHandle.setName(HANDLE_ERRORS);
		metaDataPanel.add(hiddenHandle);
		
		//importButton.setEnabled(false);
		importButton.addClickHandler(this);
		metaDataPanel.add(importButton);

		formPanel.add(metaDataPanel);
		
		HTML horizontalRule = new HTML(HORIZONTAL_RULE);
		metaDataPanel.add(horizontalRule);

		// SubTitle
		Label attachTitle = new Label(ATTACHMENTS);
		attachTitle.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		metaDataPanel.add(attachTitle);
		
		// Attachments
		metaDataPanel.add(filePanel);
		//filePanel.setVisible(false); // For testing
		
		HTML horizontalRule2 = new HTML(HORIZONTAL_RULE);
		metaDataPanel.add(horizontalRule2);

		initUploadDialog();
	}

	/**
	 * Private method that initializes the upload dialog.
	 */
	private void initUploadDialog() {
		VerticalPanel vpanel1 = new VerticalPanel();
		vpanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label waitLabel1 = new Label(UPLOAD_IN_PROGRESS);
		vpanel1.add(waitLabel1);
		Image hourGlass = new Image(WAIT_JPG);
		vpanel1.add(hourGlass);
		
		//final int hourHeight = hourGlass.getHeight();
		//final int hourWidth = hourGlass.getWidth();
		uploadDialog.add(vpanel1);
		uploadDialog.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int top = ((Window.getClientHeight() - offsetHeight) / 2);
				int left = ((Window.getClientWidth() - offsetWidth) / 2);
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				uploadDialog.setPopupPosition(left, top);
				uploadDialog.hide();
			}			
		});
	}

	/**
	 * Private method to call the server to get workbook configuration names.
	 */
	private void loadWorkbookConfigNames() {
		workbookConfigBox.addItem(EMPTY_STRING);
		workbookConfigBox.addChangeHandler(this);
		ssa.getWorkbookConfigs(new MetaDataCallback());
	}
	
	/*
	 * Private method to get attachments
	 */
	private void getAttachments(long workbookId) {
		filePanel.setWorkbookId(String.valueOf(workbookId));
		ssa.getAttachments(workbookId, new AsyncCallback<Collection<FileInfo>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Table Data failed to load.");
			}

			public void onSuccess(Collection<FileInfo> result) {
				// Files
				if (result != null) {
					Iterator<FileInfo> it3 = result.iterator();
					while (it3.hasNext()) {
						FileInfo attachment = it3.next();
						filePanel.getAttachmentList().add(attachment);
						int gridRow = filePanel.getListGrid().getRowCount();
						filePanel.getListGrid().resizeRows(gridRow + 1);
						int col = 0;
						filePanel.getListGrid().setWidget(gridRow, col, new CheckBox());
						filePanel.getListGrid().getCellFormatter().setHorizontalAlignment(gridRow, col, HasHorizontalAlignment.ALIGN_CENTER);
						col++;
						filePanel.getListGrid().setText(gridRow, col, attachment.filename);
						filePanel.getListGrid().getCellFormatter().setWordWrap(gridRow, col, false);
						filePanel.getListGrid().setVisible(true);
					}
				}
			}
		});		
	}
	/**
	 * Private method to get meta data values.
	 */
	private void getMetaData(long workbookId) {
		ssa.getMetaData(workbookId, new AsyncCallback<Collection<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				// Do nothing
			}

			@SuppressWarnings("deprecation")
			//@Override
			public void onSuccess(Collection<NameValue> result) {
				if (result != null && !result.isEmpty()) {
						int row = 0;
						dataGrid.clear();
						dataGrid.resizeRows(0);
						Iterator<NameValue> it = result.iterator();
						while (it.hasNext()) {
							NameValue nameValue = it.next();
							if (!nameValue.isInternal()) {
								String metaDataName = nameValue.getName();
								long metaDataType = nameValue.getDataType();
								row = dataGrid.getRowCount();
								dataGrid.resizeRows(dataGrid.getRowCount()+1);
								Label label = new Label(metaDataName);
								dataGrid.setWidget(row, 0, label);
								if (metaDataType == 1) { // Long
									FilteredNumberTextBox box = new FilteredNumberTextBox();
									box.setText(nameValue.getValue());
									dataGrid.setWidget(row, 1, box);
								} else if (metaDataType == 2) { // Real
									FilteredNumberTextBox box = new FilteredNumberTextBox();
									box.setText(nameValue.getValue());
									dataGrid.setWidget(row, 1, box);
								} else if (metaDataType == 3) { // Date
									DateBox box = new DateBox();
									DefaultFormat format = new DefaultFormat(DateTimeFormat.getFormat("MM/dd/yy"));
									box.setFormat(format);
									Date date = null;
									try {
										date = new Date(nameValue.getValue());
									} catch (Exception pe) {
										date = new Date();
									}
									box.setValue(date);
									dataGrid.setWidget(row, 1, box);
								} else if (metaDataType == 4) { // String
									final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
									ssa.getMetaDataValues(workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex()),
											metaDataName, new AsyncCallback<List<String>>() {
												public void onFailure(Throwable caught) {
													// Do not report
													Window.alert("problem retrieving metaData values: "+caught);
												}
				
												public void onSuccess(List<String> result) {
													if ((result != null) && (!result.isEmpty()))
														oracle.addAll(result);
												}
											});
									FilteredAlphaNumericTextBox box = new FilteredAlphaNumericTextBox(oracle);
									box.setText(nameValue.getValue());
									dataGrid.setWidget(row, 1, box);
								} else if (metaDataType == 5) { // Boolean
									CheckBox box = new CheckBox();
									box.setText(nameValue.getValue());
									dataGrid.setWidget(row, 1, box);
								}
							}
						}
						metaDataPanel.setSize(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
						formPanel.setSize(FORM_PANEL_WIDTH, FORM_PANEL_HEIGHT);
					}
				}
		});
	}

	/**
	 * Private inner callback class that populates the workbook
	 *  configuration list box.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class MetaDataCallback implements AsyncCallback<List<String>> {

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

	private String translateReturnCode(long code,String msg) {
		String message="Failed to ingest spreadsheet. \n";
		switch((int)code) {
			case MISSING_WORKSHEET: message += "Configured worksheet name missing from ingested workbook.";break;
			case MISMATCHED_COLUMN_NUMBERS: message += "Number of ingested worksheet columns exceeds number of columns in configuration.";break;
			case MISSING_COLUMN: message += "Column name from configuration not found in ingested worksheet.";break;
			case MISMATCHED_TYPE: message += "Column type from configuration does not match column in ingested worksheet."; break;
			case MISMATCHED_CONFIG: message += "The workbook ingested did not match the form of the configuration."; break;
			case MISSING_INTERNAL: message += "The ingested worksheet is missing internal metadata defined in the configuration chosen."; break;
			case PARSING_PROBLEMS: message += "File could be corrupt. Please check spreadsheet for errors.";break;
			case PARTIAL_INGESTION: message += "Data could not be uploaded. Parsing errors occurred. "; break;
			case INVALID_PAIR: message += msg;
			default: message += "Check the ingested worksheet for errors.  CODE: " + code; break;
		}
		
		return message;		
	}
	/**
	 * 
	 */
	private void initFormPanel() {
		String urlTag = GWT.getModuleBaseURL() + URL_CALC_CLIENT_UPLOAD_SERVICE;
		formPanel.setAction(urlTag);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setSize(FORM_PANEL_WIDTH, FORM_PANEL_HEIGHT);

		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = event.getResults();
				result = cleanUpResults(result);
				workBookID.setText(result);
				long id=0;
				try {
					id = Long.parseLong(result);
				} catch (NumberFormatException nfe) {
					id = -2000;
				}
				if (id < 0) {
					if (id > -2000 && id < -1000)  {
						String errorMsg = "Spreadsheet contains an invalid tracking id/work id pair at row "+(-(id+1000));
						Window.alert(translateReturnCode(-10,errorMsg));
					}
					else if (id == -2000)
						Window.alert(result);
					else
						Window.alert(translateReturnCode(id,""));
				} else {
					Window.alert("Successfully ingested spreadsheet. Assigned ID #"+result);
				}
				uploadDialog.hide();
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
	 * Private method to apply validation rules to the input fields.
	 * 
	 * @return boolean - Valid?
	 */
	private boolean validForm() {
		boolean valid = true;
		String errorMsg = "All fields must be filled.\n";
		if (excelFile.getFilename().isEmpty()) {
			errorMsg += SPREADSHEET + EMPTY;
			valid = false;
		}
		if (valid == false) {
			//Window.alert(errorMsg);
		}
		return valid;
	}

	/**
	 * Private method to initialize and configure the services needed for this
	 * class.
	 */
	public void initService() {
		ssa = (SpreadSheetServiceAsync) GWT.create(SpreadSheetService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) ssa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + URL_SPREADSHEET_SERVICE);
	}

	private void unselectWorkbookConfig() {
		int count = workbookConfigBox.getItemCount();
		for (int ctr=0;ctr<count;ctr++)
			workbookConfigBox.setItemSelected(ctr, false);
	}
	//@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == importButton) {
//Window.alert("Click!");			
			handleImportButtonClick();
		} else if (sender == createBtn) {
			if (createBtn.getValue()) {
				workbookToModifyLabel.setVisible(false);
				workbookBox.setVisible(false);
				loadButton.setVisible(false);
				modifyBtn.setValue(false,false);
			} else {
				workbookToModifyLabel.setVisible(true);
				workbookBox.setVisible(true);
				loadButton.setVisible(true);
				modifyBtn.setValue(true,false);
			}
		} else if (sender == modifyBtn) {
			if (modifyBtn.getValue()) {
				workbookConfigBox.setVisible(false);
				workbookConfigLabel.setVisible(false);
				workbookToModifyLabel.setVisible(true);
				workbookBox.setVisible(true);
				unselectWorkbookConfig();
				loadButton.setVisible(true);
				createBtn.setValue(false,false);
			} else {
				workbookConfigBox.setVisible(true);
				workbookConfigLabel.setVisible(true);
				unselectWorkbookConfig();
				workbookToModifyLabel.setVisible(false);
				workbookBox.setVisible(false);
				loadButton.setVisible(false);
				createBtn.setValue(true,false);
			}
		} else if (sender == blankBtn) {
			if (blankBtn.getValue()) {
				failedBtn.setValue(false,false);
			} else {
				failedBtn.setValue(true,false);
			}
		} else if (sender == failedBtn) {
			if (failedBtn.getValue()) {
				blankBtn.setValue(false,false);
			} else {
				blankBtn.setValue(true,false);
			}
		} else if (sender == loadButton) {
			long l = 0;
			try {
				l = Long.parseLong(workbookBox.getText());
			} catch (NumberFormatException nfe) {}
			getMetaData(l);
			getAttachments(l);
			ssa.getWorkbookConfigFromWorkbook(l, new AsyncCallback<String>() {
				//@Override
				public void onFailure(Throwable caught) {
					//Window.alert("The workbook id ("+workbookId+") specified does not exist");
				}
				public void onSuccess(String configName) {
					if (configName != null && !configName.isEmpty())
						workbookConfigBox.addItem(configName);
					int count = workbookConfigBox.getItemCount();
					String name = "";
					for (int ctr=0;ctr<count && !name.equals(configName);ctr++) {
						name = workbookConfigBox.getItemText(ctr);
						if (name.equals(configName))
							workbookConfigBox.setItemSelected(ctr,true);
						else
							workbookConfigBox.setItemSelected(ctr, false);
					}
				}
			});
		}
	}

	private void getWorkbookConfig(long l) {
		final long workbookId = l;
		ssa.getWorkbookConfigFromWorkbook(l, new AsyncCallback<String>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("The workbook id ("+workbookId+") specified does not exist");
			}

			public void onSuccess(String result) {
				//Window.alert("returned "+result);
				if (result != null && !result.isEmpty())
					handleWorkbookConfig(result);
				else {
					Window.alert("Unknown workbook id "+workbookId);
					workbookBox.setText("");
				}
			}
		});
	}

	/**
	 * Private method to handle the import button.
	 *  This method calls the import servlet and uploads
	 *  the spreadsheet.
	 */
	private void handleImportButtonClick() {
//Window.alert("handleImportButtonClick");		
		clearHiddenMetaData();
		
		String fileName = excelFile.getFilename();
		String separator = "/";
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		separator = "\\";
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		hiddenFileName.setValue(fileName);

		hiddenWorkbookToModify.setValue(workbookBox.getText());
		
		if (!createBtn.getValue())
			hiddenCreateOrModify.setValue("0");
		else 
			hiddenCreateOrModify.setValue("1");
		
		// Get Attachment IDs
		List<Long> ids = filePanel.getAttachmentIds();
		int count = 0;
		Iterator<Long> it = ids.iterator();
		while (it.hasNext()) {
			Long id = it.next();
			Hidden attachFile = new Hidden(ATTACH_FILE + count++, id.toString());
			metaDataPanel.add(attachFile);
		}
		Hidden fileCount = new Hidden(String.valueOf(ids.size()));
		metaDataPanel.add(fileCount);
		
		// Meta Data
		int gridRows = dataGrid.getRowCount();
		for (int i = 0; i < gridRows; i++) {
			Label metaLabel = (Label)dataGrid.getWidget(i, 0);
			Widget metaWidget = dataGrid.getWidget(i, 1);
			String metaValue = "";
		    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
		    // prints 12/17/2007 in the default locale
		    //GWT.log(fmt.format(today), null);
		    if (metaWidget instanceof TextBox) {
				metaValue = ((TextBox)metaWidget).getValue();
				//Window.alert("label="+metaLabel.getText()+";got value="+metaValue);
		    }
			else if (metaWidget instanceof DateBox) {
				metaValue = (fmt.format(((DateBox)metaWidget).getValue()));
				//metaValue = (DateTimeFormat.getShortDateFormat().format(((DateBox)metaWidget).getValue()));
			} else if (metaWidget instanceof CheckBox) {
				metaValue = ((CheckBox)metaWidget).getValue().toString();
			} else if (metaWidget instanceof FilteredAlphaNumericTextBox) {
				metaValue = ((FilteredAlphaNumericTextBox)metaWidget).getValue();
				//Window.alert("label="+metaLabel.getText()+";got value="+metaValue);
			}
			
			Hidden metaHidden = new Hidden(metaLabel.getText(), metaValue);
			metaDataPanel.add(metaHidden);
		}
		
		if (blankBtn.getValue()) 
			hiddenHandle.setValue(AS_BLANKS);
		else
			hiddenHandle.setValue(FAILED);
		
		if (validForm()) {
			uploadDialog.show();
			formPanel.submit();
		}
	}

	/**
	 * Removes Hidden objects so that repeated posts do not add duplicate metadata.
	 * 
	 * @return void
	 */
	private void clearHiddenMetaData(){
//Window.alert("clearHiddenMetaData");
		for (int i = metaDataPanel.getWidgetCount() - 1; i >= 0; i--) {
			Widget w = metaDataPanel.getWidget(i);
//Window.alert(w.toString());			
			if( w instanceof Hidden){
				if (!w.equals(hiddenFileName) && !w.equals(hiddenPathName)  && !w.equals(uploadDialog) && !w.equals(this.hiddenCreateOrModify) && !w.equals(this.hiddenWorkbookToModify) && !w.equals(this.hiddenHandle)){
//Window.alert("Removing " + w.toString());					
					metaDataPanel.remove(w);
				}
			}
		}
		
	}
	
	//@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == workbookConfigBox) {
			handleWorkbookConfigListBoxChanges();
		} else if (sender == workbookBox) {
			long l = 0;
			try {
				l = Long.parseLong(workbookBox.getText());
			} catch (NumberFormatException nfe) {}
			getWorkbookConfig(l);
		}
	}

	private void handleWorkbookConfig(String configName) {
		ssa.getSampleFileName(configName, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(String result) {
				if (!result.isEmpty()) {
					sampleFileLabel.setVisible(true);
					sampleFileName.setText(result);
				}
			}
			
		});
		ssa.getMetaDataHeaders(configName, EXTERNAL, new AsyncCallback<List<NameValue>>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("There was a problem loading the configuration.\nError: " + caught.getLocalizedMessage());
			}

			//@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty()) {
					constructMetaDataTable(result);
				} else {
					dataGrid.clear();
					dataGrid.resizeRows(0);
				}
			}
			
			private void constructMetaDataTable (List<NameValue> metaDataHdrs) {
				int row = 0;
				dataGrid.clear();
				dataGrid.resizeRows(0);
				Iterator<NameValue> it = metaDataHdrs.iterator();
				while (it.hasNext()) {
					NameValue nameValue = it.next();
					if (!nameValue.isInternal()) {
						String metaDataName = nameValue.getName();
						long metaDataType = nameValue.getDataType();
						row = dataGrid.getRowCount();
						dataGrid.resizeRows(dataGrid.getRowCount()+1);
						Label label = new Label(metaDataName);
						dataGrid.setWidget(row, 0, label);
						if (metaDataType == 1) { // Long
							FilteredNumberTextBox box = new FilteredNumberTextBox();
							dataGrid.setWidget(row, 1, box);
						} else if (metaDataType == 2) { // Real
							FilteredNumberTextBox box = new FilteredNumberTextBox();
							dataGrid.setWidget(row, 1, box);
						} else if (metaDataType == 3) { // Date
							DateBox box = new DateBox();
							DefaultFormat format = new DefaultFormat(DateTimeFormat.getFormat("MM/dd/yy"));
							box.setFormat(format);
							Date date = new Date();
							box.setValue(date);
							dataGrid.setWidget(row, 1, box);
						} else if (metaDataType == 4) { // String
							final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
							ssa.getMetaDataValues(workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex()),
									metaDataName, new AsyncCallback<List<String>>() {
										public void onFailure(Throwable caught) {
											// Do not report
											Window.alert("problem retrieving metaData values: "+caught);
										}
		
										public void onSuccess(List<String> result) {
											if ((result != null) && (!result.isEmpty()))
												oracle.addAll(result);
										}
									});
							FilteredAlphaNumericTextBox box = new FilteredAlphaNumericTextBox(oracle);
							dataGrid.setWidget(row, 1, box);
						} else if (metaDataType == 5) { // Boolean
							CheckBox box = new CheckBox();
							dataGrid.setWidget(row, 1, box);
						}
					}
				}
			}
		});		
	}
	/**
	 * Private method to handle the changes to the workbook configuration list box.
	 *  This method gets the meta data headers for the selected workbook configuration.
	 */
	private void handleWorkbookConfigListBoxChanges() {
		String configName = workbookConfigBox.getItemText(workbookConfigBox.getSelectedIndex());
		if (configName.isEmpty()) {
			dataGrid.clear();
			dataGrid.resizeRows(0);
			//importButton.setEnabled(false);
		} else {
			//importButton.setEnabled(true);
		}
		handleWorkbookConfig(configName);
	}
}
