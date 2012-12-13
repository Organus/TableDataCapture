package gov.nrel.nbc.spreadsheet.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

/** 
 * A <DialogBox> that allows a user to modify the Spreadsheet meta data.
 * 
 * @author James Albersheim
 *
 */
public class ModifySpreadSheetDialog extends DialogBox implements AppConstants {

	private static final String DATE_FORMAT = "MM/dd/yy";

	private static final String DIALOG_BOX_HEIGHT = "800px";

	private static final String DIALOG_BOX_WIDTH = "100%";

	/**
	 * String constant for the name of the client upload service.
	 */
	private static final String URL_CALC_SUBMISSION_SERVICE = "spreadSheetService";

	/**
	 * Holder for the SubmissionService
	 */
	private SpreadSheetServiceAsync ssa = null;
	
	/**
	 * Request ID
	 */
	private long spreadsheet_id = 0;
	
	/**
	 * Global reference to the <DialogBox>
	 */
	final private ModifySpreadSheetDialog mrd = this;
	
	private static final String SUBTITLE_STYLE = "SubTitle";

	private static final String PANEL_TITLE_LABEL = "Modify Calcsheet MetaData";

	private static final String MAIN_PANEL_HEIGHT = DIALOG_BOX_HEIGHT;

	private static final String MAIN_PANEL_WIDTH = DIALOG_BOX_WIDTH;

	public static final String URL_CLIENT_PRINT_SERVICE = "printService";

	private final FlexTable mainTable = new FlexTable();
	
	private final FileSelectionPanel filePanel = new FileSelectionPanel();

	private final Label ssType = new Label();

	private final Grid dataGrid = new Grid(0, 2);
	
	private final List<NameValue> metaData = new ArrayList<NameValue>();

	/**
	 * <DialogBox> constructor
	 * 
	 * @param spreadsheetIdString <String> Request ID
	 */
	public ModifySpreadSheetDialog(String spreadsheetIdString) {		
		initServices();
		
		spreadsheet_id = Long.parseLong(spreadsheetIdString);
		loadSpreadsheetInfo(spreadsheet_id);
		
		mainTable.setSize(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
		mainTable.setCellSpacing(10);
		
		this.setText(PANEL_TITLE_LABEL);
		
		int row = 0;
		
	// Title
		Label formTitle = new Label("Modify Workbook MetaData");
		formTitle.addStyleDependentName(SUBTITLE_STYLE);
		
		mainTable.setWidget(row, 0, formTitle);
		mainTable.getFlexCellFormatter().setColSpan(row, 0, 2);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		row++;
		
	// Spreadsheet Type
		Label ssTypeLabel = new Label("Workbook Type:");
		mainTable.setWidget(row, 0, ssTypeLabel);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		mainTable.setWidget(row, 1, ssType);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		
		row++;
		
	// Meta Data
		mainTable.setWidget(row, 0, dataGrid);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		mainTable.getFlexCellFormatter().setColSpan(row, 0, 2);
		
		row++;
		
	// File Selection Panel
		mainTable.setWidget(row, 0, filePanel);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		mainTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	
		row++;
		
		mainTable.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
	// Buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setWidth("100%");
		
		Button saveButton = new Button("Save");
		saveButton.addClickHandler(new SaveClickHandler());
		buttonPanel.add(saveButton);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				mrd.hide();
			}			
		});
		buttonPanel.add(cancelButton);
		mainTable.setWidget(row, 0, buttonPanel);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		mainTable.getFlexCellFormatter().setColSpan(row, 0, 2);
					
		this.add(mainTable);
		this.setSize(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
		
		this.setPopupPositionAndShow(new PositionCallback() {

			public void setPosition(int offsetWidth, int offsetHeight) {

				int left = (Window.getClientWidth() - offsetWidth) / 3;
				int top = (Window.getClientHeight() - offsetHeight) / 3;
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				mrd.setPopupPosition(left, top);
			}			
		});
	}

	/**
	 * Private method to populate the dialog with Spreadsheet data
	 * 
	 * @param spreadsheetId long - to load
	 */
	private void loadSpreadsheetInfo(long spreadsheetId) {
		final long workbookId = spreadsheetId;
		filePanel.setWorkbookId(String.valueOf(workbookId));
		ssa.getAttachments(spreadsheetId, new AsyncCallback<Collection<FileInfo>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Attachments failed to load.");
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
		
		List<CriteriaTrioDTO> trioList = new ArrayList<CriteriaTrioDTO>();
		CriteriaTrioDTO dto = new CriteriaTrioDTO();
		dto.setHeader("Spreadsheet ID");
		dto.setOperator("=");
		dto.setValue(String.valueOf(spreadsheetId));
		trioList.add(dto);
		ssa.getWorkbookConfigName(spreadsheetId,
				new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						Window.alert("Spreadsheet data failed to load.");
					}

					public void onSuccess(String result) {
						if (result != null && !result.isEmpty()) {
							ssType.setText(result);
						}
					}
		});
		ssa.getMetaData(spreadsheetId, 
			new AsyncCallback<Collection<NameValue>>() {

				public void onFailure(Throwable caught) {
					Window.alert("Spreadsheet data failed to load.");
				}

				public void onSuccess(Collection<NameValue> result) {
					if (result != null && !result.isEmpty()) {
						Iterator<NameValue> it = result.iterator();
						while (it.hasNext()) {
							NameValue nv = it.next();
							String name = nv.getName();
							String value = nv.getValue();
							long dataTypeCode = nv.getDataType();
							int row = dataGrid.getRowCount();
							dataGrid.resizeRows(dataGrid.getRowCount()+1);
							Label label = new Label(name);
							dataGrid.setWidget(row, 0, label);
							if (dataTypeCode == 1) { // Long
								FilteredNumberTextBox box = new FilteredNumberTextBox();
								box.setValue(value);
								dataGrid.setWidget(row, 1, box);
							} else if (dataTypeCode == 2) { // Real
								FilteredNumberTextBox box = new FilteredNumberTextBox();
								box.setValue(value);
								dataGrid.setWidget(row, 1, box);
							} else if (dataTypeCode == 3) { // Date
								try {
									DateBox box = new DateBox();
									DefaultFormat format = new DefaultFormat(DateTimeFormat.getFormat(DATE_FORMAT));
									box.setFormat(format);
									// 2010-02-04 07:00:00.0
									box.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse(value));
									dataGrid.setWidget(row, 1, box);
								} catch (Exception e1) {
									//Window.alert(e1.getMessage());
								}
							} else if (dataTypeCode == 4) { // String
								final MultiWordSuggestOracle suggestOracle = new MultiWordSuggestOracle();
								FilteredAlphaNumericTextBox box = new FilteredAlphaNumericTextBox(suggestOracle);
								box.setValue(value);
								dataGrid.setWidget(row, 1, box);
							} else if (dataTypeCode == 5) { // Boolean
								CheckBox box = new CheckBox();
								box.setValue(value.equalsIgnoreCase("true"));
								dataGrid.setWidget(row, 1, box);
							}
						}
					}
				}
			}
		);
	}


	/**
	 * Protected method to set the selected item in a list box
	 *  by value.
	 */
	protected void setListBox(String value, ListBox listBox) {
		
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (value.equalsIgnoreCase(listBox.getItemText(i)))
				listBox.setSelectedIndex(i);
		}
	}

	/**
	 * Private method to initialize and configure the services needed for this
	 * class.
	 */
	private void initServices() {
		ssa = (SpreadSheetServiceAsync) GWT.create(SpreadSheetService.class);

		ServiceDefTarget endpoint = (ServiceDefTarget) ssa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ URL_CALC_SUBMISSION_SERVICE);
	}
	
	/**
	 * Private <ClickHandler> to save a request.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class SaveClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			// Loading attachments
			List<Long> ids = filePanel.getAttachmentIds();
			if (ids != null && !ids.isEmpty()) {
				Iterator<Long> it = ids.iterator();
				while (it.hasNext()) {
					Long id = it.next();
					NameValue nv = new NameValue();
					nv.setName("attachment");
					nv.setValue(String.valueOf(id.longValue()));
					metaData.add(nv);
				}
			}
			
			List<Long> rids = filePanel.getRemoveList();
			Iterator<Long> it = rids.iterator();
			while (it.hasNext()) {
				Long id = it.next();
				ssa.removeAttachment(id.longValue(), spreadsheet_id, new AsyncCallback<Boolean>() {

					//@Override
					public void onFailure(Throwable caught) {
						Window.alert("The attachment was not removed.\nAn error was thrown: " + caught.getMessage());
					}

					//@Override
					public void onSuccess(Boolean result) {
						if (result)
							Window.alert("Attachment removed");
						else
							Window.alert("Attachment was not removed.\nCheck the logs for errors.");
					}
				});
			}
			
			// Loading metaData changes
			getMetaDataFromGrid();

			ssa.setMetaData(metaData, spreadsheet_id, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					Window.alert("The workbook was not saved.\nAn error was thrown: " + caught.getMessage());
				}

				public void onSuccess(Boolean result) {
					if (result) {
						Window.alert("The workbook was saved.");
						mrd.hide();
					} else
						Window.alert("The workbook was not saved.");
				}			
			});
		}		
	}

	/**
	 * Public method to get the meta data from the date grid.
	 */
	public void getMetaDataFromGrid() {
		for (int i = 0; i < dataGrid.getRowCount(); i++) {
			NameValue nv = new NameValue();
			String name = ((Label)dataGrid.getWidget(i, 0)).getText();
			nv.setName(name);
			
			Widget widget = dataGrid.getWidget(i, 1);
			if (widget.getClass().getName().endsWith("FilteredNumberTextBox")) {
				nv.setValue(((FilteredNumberTextBox)widget).getValue());
			} else if (widget.getClass().getName().endsWith("DateBox")) {
				String dateString = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.S").format(((DateBox)widget).getValue());
				nv.setValue(dateString);
			} else if (widget.getClass().getName().endsWith("FilteredAlphaNumericTextBox")) {
				try {
					nv.setValue(((FilteredAlphaNumericTextBox)widget).getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (widget.getClass().getName().endsWith("CheckBox")) {
				Boolean value = ((CheckBox)widget).getValue();
				nv.setValue(value ? "true" : "false");
			}
			metaData.add(nv);
		}
	}
}
