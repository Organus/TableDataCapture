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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifySpreadsheet extends Composite implements AppConstants, ClickHandler, ChangeHandler {

	private static final String SHEETS_COLON = "Sheets:";

	private static final String PERCENT_100 = "100%";

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";

	private static final String DOCK_PANEL_WIDTH = PERCENT_100;

	private static final String RESULTS_DATA_WIDTH = "500px";

	private static final String WORKBOOK_TYPE_COLON = "Workbook Type:";
	
	private VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * String constant for the name of the client upload service.
	 */
	private final String URL_ADMIN_SERVICE = "adminService";

	/**
	 * Holder for the SubmissionService
	 */
	private AdminServiceAsync asa = null;
	
	private final ListBox workbookConfigListBox = new ListBox();

	/**
	 * Title for the Search box
	 */
	private final String SUB_TITLE = "Modify Configuration";

	private final DecoratorPanel configDecPanel = new DecoratorPanel();

	private final VerticalPanel metadataVerticalPanel = new VerticalPanel();

	private final VerticalPanel emdVPanel = new VerticalPanel();

	private final Button saveMetaDataChangesButton = new Button("Save");
	
	private final HashMap<String, NameValue> metaMap = new HashMap<String, NameValue>();
	
	private final HashMap<String, NameValue> cellMap = new HashMap<String, NameValue>();
	
	private final List<NameValue> dataTypes = new ArrayList<NameValue>();
	
	private final List<NameValue> dateFormats = new ArrayList<NameValue>();
	
	private final List<NameValue> realFormats = new ArrayList<NameValue>();

	private final ListBox sheetListBox = new ListBox();

	private final VerticalPanel upperPanel = new VerticalPanel();

	private final VerticalPanel cellVPanel = new VerticalPanel();

	private final Button saveCellDataChangesButton = new Button("Save");

	/**
	 * Default constructor
	 */
	public ModifySpreadsheet() {
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
		Label srchLabel = new Label(SUB_TITLE);
		srchLabel.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		upperPanel.add(srchLabel);
		upperPanel.setCellHorizontalAlignment(srchLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Workbook Type
		LabeledWidget workbookTypeBox = new LabeledWidget(workbookConfigListBox);
		workbookTypeBox.setLabelText(WORKBOOK_TYPE_COLON);
		workbookTypeBox.setPanelWidth(RESULTS_DATA_WIDTH);
		workbookConfigListBox.addChangeHandler(this);
		getConfigs();
		upperPanel.add(workbookTypeBox);
		dp.add(upperPanel);
		
		// Sheets
		LabeledWidget sheetBox = new LabeledWidget(sheetListBox);
		sheetBox.setLabelText(SHEETS_COLON);
		sheetBox.setPanelWidth(RESULTS_DATA_WIDTH);
		sheetListBox.addChangeHandler(this);
		upperPanel.add(sheetBox);
		
		setUpConfigPanel();
		
		getDataTypes();
		getDateFormats();
		getRealFormats();
	}
	
	/**
	 * Private method to call server to retrieve data types and
	 *  insert them into a list to be accessed later.
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
				}
			}
		});
	}
	
	/**
	 * Private method to call server to retrieve date formats and
	 *  insert them into a list to be accessed later.
	 */
	private void getDateFormats() {
		asa.getDataFormats(DATE_TYPE, new AsyncCallback<List<NameValue>>() {
			
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
				Window.alert("Problem getting data types. Error: " + caught.getLocalizedMessage());
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
	 * Set up Modify configuration panel
	 */
	private void setUpConfigPanel() {
		configDecPanel.setVisible(false);
		mainPanel.add(configDecPanel);
		mainPanel.setCellHorizontalAlignment(configDecPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		metadataVerticalPanel.setWidth(PERCENT_100);
		metadataVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		configDecPanel.add(metadataVerticalPanel);
		
		metadataVerticalPanel.add(emdVPanel);
		
		saveMetaDataChangesButton.addClickHandler(this);
		metadataVerticalPanel.add(saveMetaDataChangesButton);
	}

	/**
	 * Private method to get all meta data based on the chosen workbook configuration.
	 */
	private void getMetaData() {
		emdVPanel.clear();
		metaMap.clear();
		
		final String wbConfig = workbookConfigListBox.getItemText(workbookConfigListBox.getSelectedIndex());
		
		asa.getMetaDataHeaders(wbConfig, EXTERNAL, new AsyncCallback<List<NameValue>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting meta data. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty()) {
					setupMetadataFields(result, "External Metadata");
				}
				
				asa.getMetaDataHeaders(wbConfig, INTERNAL, new AsyncCallback<List<NameValue>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Problem getting meta data. Error: " + caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(List<NameValue> result) {
						if (result != null && !result.isEmpty()) {
							setupMetadataFields(result, "Internal Metadata");
						}
					}
				});
			}
		});
	}

	/**
	 * Private method to get column headers based on the workbook and sheet configurations.
	 */
	private void getCellHeaders() {
		DecoratorPanel cellDPanel = new DecoratorPanel();
		cellVPanel.setWidth(PERCENT_100);
		cellVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		cellDPanel.add(cellVPanel);
		cellVPanel.clear();
		cellMap.clear();
		
		String wbConfig = workbookConfigListBox.getItemText(workbookConfigListBox.getSelectedIndex());
		String shConfig = sheetListBox.getItemText(sheetListBox.getSelectedIndex());
		
		asa.getCellDataHeaders(wbConfig, shConfig, new AsyncCallback<List<NameValue>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting cell data. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NameValue> result) {
				if (result != null && !result.isEmpty()) {
					setupCellDataFields(result, "Cell Headers");
				}
			}
		});
		
		mainPanel.add(cellDPanel);
	}

	/**
	 * Protected method to take a list of meta data elements and
	 *  create GUI elements that can be modified by the user.
	 *  
	 * @param metadataElements List<NameValue>
	 * @param subTitle String
	 */
	protected void setupCellDataFields(List<NameValue> metadataElements, String subTitle) {		
		if (metadataElements != null && !metadataElements.isEmpty()) {
			Label subTitleLabel = new Label(subTitle);
			cellVPanel.add(subTitleLabel);
			cellVPanel.setCellHorizontalAlignment(subTitleLabel, HasHorizontalAlignment.ALIGN_CENTER);
			
			Iterator<NameValue> it = metadataElements.iterator();
			while (it.hasNext()) {
				NameValue nv = it.next();
				cellMap.put(nv.getName(), nv);
				ListBox listBox = new ListBox();
				listBox.addChangeHandler(this);
				long dataTypeId = nv.getDataType();
				Iterator<NameValue> it2 = dataTypes.iterator();
				while (it2.hasNext()) {
					NameValue dataType = it2.next();
					listBox.addItem(dataType.getName());
				}
				listBox.setSelectedIndex((int)dataTypeId - 1);
				ListBox dataFormatBox = new ListBox();
				dataFormatBox.setVisible(false);
				if (dataTypeId == DATE_TYPE_ID) {
					dataFormatBox.setVisible(true);
					Iterator<NameValue> it3 = dateFormats.iterator();
					int index = 0;
					while (it3.hasNext()) {
						NameValue dataFormat = it3.next();
						dataFormatBox.addItem(dataFormat.getValue());
						if (nv.getDataFormat() != null && nv.getDataFormat().equalsIgnoreCase(dataFormat.getName())) {
							dataFormatBox.setSelectedIndex(index);
						}
						index++;
					}
				} else if (dataTypeId == REAL_TYPE_ID) {
					dataFormatBox.setVisible(true);
					Iterator<NameValue> it3 = realFormats.iterator();
					int index = 0;
					while (it3.hasNext()) {
						NameValue dataFormat = it3.next();
						dataFormatBox.addItem(dataFormat.getValue());
						if (nv.getDataFormat() != null && nv.getDataFormat().equalsIgnoreCase(dataFormat.getName())) {
							dataFormatBox.setSelectedIndex(index);
						}
						index++;
					}
				
				}
				LabeledWidgetAndWidget lWidget = new LabeledWidgetAndWidget(nv.getName() + ":", listBox, dataFormatBox);
				lWidget.setPanelWidth(RESULTS_DATA_WIDTH);
				cellVPanel.add(lWidget);			
			}
			
			saveCellDataChangesButton.addClickHandler(this);
			cellVPanel.add(saveCellDataChangesButton);
		}
	}

	/**
	 * Protected method to take a list of meta data elements and
	 *  create GUI elements that can be modified by the user.
	 *  
	 * @param metadataElements List<NameValue>
	 * @param subTitle String
	 */
	protected void setupMetadataFields(List<NameValue> metadataElements, String subTitle) {		
		if (metadataElements != null && !metadataElements.isEmpty()) {
			Label subTitleLabel = new Label(subTitle);
			emdVPanel.add(subTitleLabel);
			emdVPanel.setCellHorizontalAlignment(subTitleLabel, HasHorizontalAlignment.ALIGN_CENTER);
			
			Iterator<NameValue> it = metadataElements.iterator();
			while (it.hasNext()) {
				NameValue nv = it.next();
				metaMap.put(nv.getName(), nv);
				ListBox listBox = new ListBox();
				listBox.addChangeHandler(this);
				long dataTypeId = nv.getDataType();
				Iterator<NameValue> it2 = dataTypes.iterator();
				while (it2.hasNext()) {
					NameValue dataType = it2.next();
					listBox.addItem(dataType.getName());
				}
				listBox.setSelectedIndex((int)dataTypeId - 1);
				ListBox dataFormatBox = new ListBox();
				dataFormatBox.setVisible(false);
				if (dataTypeId == DATE_TYPE_ID) {
					dataFormatBox.setVisible(true);
					Iterator<NameValue> it3 = dateFormats.iterator();
					int index = 0;
					while (it3.hasNext()) {
						NameValue dateFormat = it3.next();
						dataFormatBox.addItem(dateFormat.getValue());
						if (nv.getDataFormat() != null && nv.getDataFormat().equalsIgnoreCase(dateFormat.getName())) {
							dataFormatBox.setSelectedIndex(index);
						}
						index++;
					}
				} else if (dataTypeId == REAL_TYPE_ID) {
					dataFormatBox.setVisible(true);
					Iterator<NameValue> it3 = realFormats.iterator();
					int index = 0;
					while (it3.hasNext()) {
						NameValue dataFormat = it3.next();
						dataFormatBox.addItem(dataFormat.getValue());
						if (nv.getDataFormat() != null && nv.getDataFormat().equalsIgnoreCase(dataFormat.getName())) {
							dataFormatBox.setSelectedIndex(index);
						}
						index++;
					}
				
				}
				LabeledWidgetAndWidget lWidget = new LabeledWidgetAndWidget(nv.getName() + ":", listBox, dataFormatBox);
				lWidget.setPanelWidth(RESULTS_DATA_WIDTH);
				emdVPanel.add(lWidget);			
			}
		}
	}

	/**
	 * Private method to get the workbook configurations and populate the list box.
	 */
	private void getConfigs() {
		asa.getWorkbookConfigs(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting configurations. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				if (result != null && !result.isEmpty()) {
					workbookConfigListBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String config = it.next();
						workbookConfigListBox.addItem(config);
					}
				}
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == saveMetaDataChangesButton)
			handleSaveMetaDataChangesButton();
		else if (sender == saveCellDataChangesButton)
			handleSaveCellDataChangesButton();
	}

	/**
	 * Private method to handle saving cell data changes.
	 *  Logic: for each cell data item compare to cellMap to see if there were changes.
	 *   If so then call server to set changes.
	 */
	private void handleSaveCellDataChangesButton() {
		final String wbConfig = workbookConfigListBox.getItemText(workbookConfigListBox.getSelectedIndex());
		final String shConfig = sheetListBox.getItemText(sheetListBox.getSelectedIndex());
		int widgetCount = cellVPanel.getWidgetCount();
		setWorked(true);
		for (int i = 0; i < widgetCount; i++) {
			Widget widget = cellVPanel.getWidget(i);
			if (widget instanceof LabeledWidgetAndWidget) {
				LabeledWidgetAndWidget lwidget = (LabeledWidgetAndWidget)widget;
				String name = lwidget.getLabelText();
				
				// Remove colon
				name = name.substring(0, name.length() - 1);
				
				final NameValue cellHeader = cellMap.get(name);
				int newValue = ((ListBox)lwidget.getWidget()).getSelectedIndex() + 1;
				ListBox listBox = (ListBox)lwidget.getWidget2();
				if (listBox != null) {
					int selIndex = listBox.getSelectedIndex();
					if (selIndex >= 0) {
						String newDataFormat = (listBox.getItemText(selIndex));
						if (newValue != cellHeader.getDataType() || !newDataFormat.equalsIgnoreCase(cellHeader.getDataFormat())) {
							if (newValue != cellHeader.getDataType())
								cellHeader.setDataType(newValue);
							if (!newDataFormat.equalsIgnoreCase(cellHeader.getDataFormat()))
								cellHeader.setDataFormat(newDataFormat);
							asa.setCellDataHeader(cellHeader, wbConfig, shConfig, new AsyncCallback<Boolean>() {
		
								@Override
								public void onFailure(Throwable caught) {
									//Window.alert("Problem updating cell data, " + cellHeader.getName() + ". Error: "
									//		+ caught.getLocalizedMessage());
									setWorked(false);
								}
		
								@Override
								public void onSuccess(Boolean result) {
									if (!result) {
										//Window.alert("Modification failed. See logs.");
										setWorked(false);
									}
								}
							});
						}
					}
				}
			}
		}
		if (getWorked())
			Window.alert("Cell Header changes saved!");
		else
			Window.alert("Cell header changes failed. See logs.");
	}

	boolean worked = true;
 	protected void setWorked(boolean b) {
		worked = b;
	}
 	protected boolean getWorked() {
		return worked;
	}

	/**
	 * Private method to handle saving meta data changes.
	 *  Logic: for each meta data item compare to metaMap to see if there were changes.
	 *   If so then call server to set changes.
	 */
	private void handleSaveMetaDataChangesButton() {
		final String wbConfig = workbookConfigListBox.getItemText(workbookConfigListBox.getSelectedIndex());
		int widgetCount = emdVPanel.getWidgetCount();
		setWorked(true);
		for (int i = 0; i < widgetCount; i++) {
			Widget widget = emdVPanel.getWidget(i);
			if (widget instanceof LabeledWidgetAndWidget) {
				LabeledWidgetAndWidget lwidget = (LabeledWidgetAndWidget)widget;
				String name = lwidget.getLabelText();
				
				// Remove colon
				name = name.substring(0, name.length() - 1);
				
				final NameValue metaHeader = metaMap.get(name);
				int newValue = ((ListBox)lwidget.getWidget()).getSelectedIndex() + 1;
				ListBox listBox = (ListBox)lwidget.getWidget2();
				if (listBox != null) {
					int selIndex = listBox.getSelectedIndex();
					if (selIndex >= 0) {
						String newDateFormat = (listBox.getItemText(selIndex));
						if (newValue != metaHeader.getDataType() || !newDateFormat.equalsIgnoreCase(metaHeader.getDataFormat())) {
							if (newValue != metaHeader.getDataType())
								metaHeader.setDataType(newValue);
							if (!newDateFormat.equalsIgnoreCase(metaHeader.getDataFormat())) {
								if (newValue == DATE_TYPE_ID || newValue == REAL_TYPE_ID) {
									Iterator<NameValue> it = null;
									if (newValue == DATE_TYPE_ID) {
										it = dateFormats.iterator();
									} else if (newValue == REAL_TYPE_ID) {
										it = realFormats.iterator();
									}
									while (it.hasNext()) {
										NameValue nv = it.next();
										if (nv.getName().equalsIgnoreCase(newDateFormat)) {
											newDateFormat = nv.getName();
										}
									}
									metaHeader.setDataFormat(newDateFormat);
								}
							}
							asa.setMetaDataHeader(metaHeader, wbConfig, new AsyncCallback<Boolean>() {
		
								@Override
								public void onFailure(Throwable caught) {
									//Window.alert("Problem updating meta data, " + metaHeader.getName() + ". Error: "
									//		+ caught.getLocalizedMessage());
									setWorked(false);
								}
		
								@Override
								public void onSuccess(Boolean result) {
									if (!result)
										setWorked(false);
								}
							});
						}
					}
				}
			}
		}
		if (getWorked())
			Window.alert("Meta Header changes saved!");
		else
			Window.alert("Meta header changes failed. See logs.");
	}

	@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget)event.getSource();
		
		if (sender == workbookConfigListBox) {
			handleWorkbookConfigListChanges();
		} else if (sender == sheetListBox) {
			handleWorksheetListChanges();
		} else if(sender instanceof ListBox) { // Must be last; List boxes from meta and cell headers.
			handleTypeAndFormatListBoxes(sender);
		}
	}

	/**
	 * Private method to handle changes to the data type and format
	 *  lists. If Real or Date types are selected show the format
	 *  ListBox.
	 * 
	 * @param sender Widget that is the ListBox.
	 */
	private void handleTypeAndFormatListBoxes(Widget sender) {
		ListBox listBox = (ListBox)sender;
		Widget subParent = listBox.getParent();
		LabeledWidgetAndWidget parent = (LabeledWidgetAndWidget)subParent.getParent();
		int index = listBox.getSelectedIndex();
		ListBox dataFormatBox = (ListBox)parent.getWidget2();
		if (index == (DATE_TYPE_ID - 1)) {
			dataFormatBox.setVisible(true);
			if (dataFormatBox == null) {
				dataFormatBox = new ListBox();
				parent.setWidget2(dataFormatBox);
			} else {
				dataFormatBox.clear();
			}
			Iterator<NameValue> it3 = dateFormats.iterator();
			while (it3.hasNext()) {
				NameValue dateFormat = it3.next();
				dataFormatBox.addItem(dateFormat.getValue());
			}
			dataFormatBox.setSelectedIndex(0);
		} else if (index == (REAL_TYPE_ID - 1)) {
			dataFormatBox.setVisible(true);
			if (dataFormatBox == null) {
				dataFormatBox = new ListBox();
				parent.setWidget2(dataFormatBox);
			} else {
				dataFormatBox.clear();
			}
			Iterator<NameValue> it3 = realFormats.iterator();
			while (it3.hasNext()) {
				NameValue dateFormat = it3.next();
				dataFormatBox.addItem(dateFormat.getValue());
			}
			dataFormatBox.setSelectedIndex(0);
		} else {
			dataFormatBox.setVisible(false);
		}
	}

	/**
	 * Private method to get cell data when the user selects a
	 *  different worksheet configuration.
	 */
	private void handleWorksheetListChanges() {
		if (sheetListBox.getSelectedIndex() > 0) {
			getCellHeaders();
		}
	}

	/**
	 * Private method to get data when the user selects a different
	 *  workbook configuration.
	 */
	private void handleWorkbookConfigListChanges() {
		int index = workbookConfigListBox.getSelectedIndex();
		configDecPanel.setVisible(index > 0);
		if (index > 0) {
			getMetaData();
			getSheets();
		}
	}

	/**
	 * Private method to get a list of worksheets for the given workbook.
	 */
	private void getSheets() {
		int index = workbookConfigListBox.getSelectedIndex();
		String wbConfig = workbookConfigListBox.getItemText(index);
		asa.getSheetConfigs(wbConfig, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem getting sheets. Error: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				if (result != null) {
					sheetListBox.clear();
					sheetListBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String sheet = it.next();
						sheetListBox.addItem(sheet);
					}
				}
			}
		});
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
}
