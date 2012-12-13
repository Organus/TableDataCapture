package gov.nrel.nbc.tracker.client;

import gov.nrel.nbc.tracker.client.CreateSample.MySelectionHandler;
import gov.nrel.nbc.tracker.client.CreateSample.MyValueChangeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * A <DialogBox> that allows a user to modify the sample data.
 * 
 * @author jalbersh
 *
 */
public class ModifySampleDialog extends DialogBox implements ClickHandler, AppConstants {
	private static final String DOWNLOAD_SAMPLE = "Download";
	private static final String HORIZONTAL_RULE = "<hr/>";
	private static final String DIALOG_BOX_HEIGHT = "500px";
	private static final String DIALOG_BOX_WIDTH = "100%";
	private static final String CANCEL_BUTTON_LABEL = "Cancel";
	private static final String SAVE_BUTTON_LABEL = "Modify";
	private static final String NEW_BUTTON_LABEL = "Create New Sample";
	private static final String PAGE_LABEL = "TRB Page:";
	private static final String NUMBER_LABEL = "TRB Number:";
	private static final String SUBLOCATION_LABEL = "Sublocation:";
	private static final String PACKAGING_LABEL = "Packaging:";
	private static final String STORAGE_NOTES_LABEL = "Storage Notes:";
	private static final String SHELF_LABEL = "Shelf:";
	private static final String HOLDER_LABEL = "Holder:";
	private static final String ROOM_LABEL = "Room:";
	private static final String BUILDING_LABEL = "Building:";
	private static final String STATUS_LABEL = "Status:";
	private static final String FRACTION_LABEL = "Fraction:";
	private static final String BIOMASS_LOT_LABEL = "Biomass Lot ID:";
	private static final String FORM_LABEL = "Storage State:";
	private static final String COMP_LABEL = "Composition:";
	private static final String STRAIN_LABEL = "Strain:";
	private static final String DEST_LABEL = "Project Task No:";
	private static final String FEEDSTOCK_LABEL = "Feedstock:";
	private static final String TREATMENT_LABEL = "Treatment:";
	private static final String ORIGIN_LABEL = "Origin:";
	private static final String CUSTODIAN_NAME_LABEL = "Custodian Name:";
	private static final String CUSTODIAN_LABEL = "Custodian:";
	private static final String OWNER_NAME_LABEL = "Owner Name:";
	private static final String EXTERNAL_ID_LABEL = "External ID:";
	private static final String DATE_ENTERED_LABEL = "Date Entered:";
	private static final String AMOUNT_LABEL = "Approximate Amount:";
	private static final String COMMENTS_LABEL = "Comments:";
	private static final String DESCRIPTOR_LABEL = "Label Descriptor:";
	private static final String SAMPLE_ID_LABEL = "Sample ID:";
	private static final String TRACKING_ID_LABEL = "Tracking ID:";
	private static final String PANEL_TITLE_LABEL = "Modify Sample Data";
	private static final String MAIN_PANEL_HEIGHT = DIALOG_BOX_HEIGHT;
	private static final String MAIN_PANEL_WIDTH = DIALOG_BOX_WIDTH;
	private static final String URL_TRACKER_SERVICE = "trackerService";
	private static final String SELECT_ROW = "Select";
	private static final String ATTACHMENT_ID = "ID";
	private static final String ATTACHMENT = "File Name";
	private static final int ATTACHMENT_SELECT_COL = 0;
	private static final int ATTACHMENT_ID_COL = 1;
	private static final int ATTACHMENT_NAME_COL = 2;
	private final String URL_ATTACHMENT_UPLOAD_SERVICE = "attachmentUploadService";
	private static final String LABEL_LABEL = "Bold fields are on printed label as shown on right";
	private static final String REMOVE_SAMPLE = "Remove";
	private static final String ADD_SAMPLE = "Add";
	private static final String ENTRY_DATE = "Create Date:";
	private static final String MATERIAL = "Material:";
	private static final String TRB_LABEL = "Trb:";
	private static final String FANCYLABEL_STYLE = "FancyLabel";
	private static final String BOLDFANCYLABEL_STYLE = "BoldFancyLabel";
	private static final String FANCYDESCLABEL_STYLE = "FancyDescLabel";
	private static final String FANCYPANEL_STYLE = "FancyPanel";
	private static final String LABEL_CONTENTS = "Label Contents";
	private static final String BOLDLABEL_STYLE = "BoldLabel";
	private static final String PUNYLABEL_STYLE = "punyLabel";
	private static final String BLANK = "";
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String DASH = "-";
	private static final String DESCRIPTOR_WIDTH = "430px";
	private static final String DESCRIPTOR_HEIGHT = "70px";
	private static final int MAX_PER_LINE = 51;
	private static final int START_POSITION = 40;
	private static final String MAY_DETONATE = "4 - May detonate";
	private static final String SHOCK_OR_HEAT = "3 - Shock or heat may detonate";
	private static final String VIOLENT_CHEMICAL_REATION = "2 - Violent chemical reation; water reactive";
	private static final String UNSTABLE_IF_HEATED = "1 - Unstable if heated";
	private static final String STABLE = "0 - Stable";
	private static final String BELOW_73 = "4 - Below 73F";
	private static final String BELOW_100 = "3 - Below 100F";
	private static final String BELOW_200 = "2 - Below 200F";
	private static final String ABOVE_200 = "1 - Above 200F";
	private static final String WILL_NOT_BURN = "0 - Will not burn";
	private static final String DEADLY = "4 - Deadly";
	private static final String EXTREME_DANGER = "3 - Extreme Danger";
	private static final String HAZARDOUS = "2 - Hazardous";
	private static final String SLIGHTLY_HAZARDOUS = "1 - Slightly Hazardous";
	private static final String NO_HEALTH_THREAT = "0 - No Health Threat";
	private static final String CORROSIVE = "CORR - corrosive";
	private static final String ACID = "ACID - acid";
	private static final String OXIDIZER = "OX - oxidizer";
	private static final String WATER_REACTIVITY = "W - water reactivity";
	private final String URL_ATTACHMENT_DOWNLOAD_SERVICE = "fileDownloadService";

	private static final String BLANKS = "---";

	/**
	 * Holder for TrackerService
	 */
	private TrackerServiceAsync tsa = null;
	
	/**
	 * A reference to the sample DTO
	 */
	private SampleCriteria theSample = null;
	
	/**
	 * <String> version of the sample ID
	 */
	private String sampleIdString = null;
	
	/**
	 * Sample ID
	 */
	private long sampleId = 0;
	
	/**
	 * Global reference to the <DialogBox>
	 */
	private final ModifySampleDialog msd = this;

	/**
	 * Tracking ID <Label>
	 */
	private final Label trackingIdLabel = new Label();
	
	private final Label change = new Label("false");

	/**
	 * Main panel <FlexTable>
	 */
	private final FlexTable samplePanel = new FlexTable();
	private final FlexTable labelPanel = new FlexTable();
	private final HorizontalPanel hpanel = new HorizontalPanel();
	private final FlexTable h1panel = new FlexTable();
	private final VerticalPanel mainPanel = new VerticalPanel();
	/**
	 * Oracle for the sample name
	 */
	private MultiWordSuggestOracle sampleIdOracle = new MultiWordSuggestOracle();
	
	/**
	 * Sample Name <SuggestBox>
	 */
	private final FilteredSuggestBox sampleIdBox = new FilteredSuggestBox(sampleIdOracle);
	
	/**
	 * Comment <TextBox>
	 */
	private final FilteredAlphaNumericTextBox commentBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * Comment <TextBox>
	 */
	private final FilteredAlphaNumericTextBox descriptorBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * Amount <TextBox>
	 */
	private final FilteredNumberTextBox amountBox = new FilteredNumberTextBox();
	
	/**
	 * Units <ListBox>
	 */
	private final ListBox unitsBox = new ListBox();
	
	/**
	 * Entered Date <Label>
	 */
	private final DateBox enteredDate = new DateBox();

	private ListBox originListBox = new ListBox();
	
	/**
	 * Oracle for the custodian name
	 */
	private MultiWordSuggestOracle custodianOracle = new MultiWordSuggestOracle();
	
	/**
	 * Custodian Name <SuggestBox>
	 */
	private FilteredSuggestBox custodianBox = new FilteredSuggestBox(custodianOracle);

	/**
	 * Oracle for the owner name
	 */
	private MultiWordSuggestOracle ownerOracle = new MultiWordSuggestOracle();
	
	/**
	 * Custodian Name <SuggestBox>
	 */
	private FilteredSuggestBox ownerBox = new FilteredSuggestBox(ownerOracle);
	
	private final Label labelMaterialBox1 = new Label(BLANK);
	private final Label labelComma1 = new Label(COMMA);
	private final Label labelMaterialBox2 = new Label(BLANK);
	private final Label labelComma2 = new Label(COMMA);
	private final Label labelMaterialBox3 = new Label(BLANK);
	private final Label descriptorLabel1 = new Label(SPACE);
	private final Label descriptorLabel2 = new Label(SPACE);
	private final Label descriptorLabel3 = new Label(SPACE);
	Label dateValue = new Label();

	private Label labelNumBox = new Label();
	
	private Label labelPageBox = new Label();
	
	private Label labelNameBox = new Label();
	
	private Label labelOwnBox = new Label();
	
	private Label labelDestBox = new Label();
	
	private Label labelStrainBox = new Label();
	
	private Label labelCustBox = new Label();
	/**
	 * Fire <Label>
	 */
	private final Label fireBoxLabel = new Label();
	/**
	 * reactivity <Label>
	 */
	private final Label reactivityBoxLabel = new Label();
	/**
	 * specific <Label>
	 */
	private final Label specificBoxLabel = new Label();
	/**
	 * Fraction <Label>
	 */
	private final Label healthBoxLabel = new Label();
	/**
	 * Treatment <ListBox>
	 */
	private ListBox treatmentBox = new ListBox();

	/**
	 * Oracle for the feedstock
	 */
	private MultiWordSuggestOracle feedstockOracle = new MultiWordSuggestOracle();
	
	/**
	 * Feedstock <SuggestBox>
	 */
	private FilteredSuggestBox feedstockBox = new FilteredSuggestBox(feedstockOracle);
	
	/**
	 * Fraction <ListBox>
	 */
	private ListBox fractionBox = new ListBox();
	
	/**
	 * Form <ListBox>
	 */
	private MultiWordSuggestOracle bioOracle = new MultiWordSuggestOracle();
	private FilteredSuggestBox bioBox = new FilteredSuggestBox(bioOracle);
	/**
	 * Form <ListBox>
	 */
	private ListBox formBox = new ListBox();
	
	/**
	 * Composition <ListBox>
	 */
	private ListBox compBox = new ListBox();
	
	/**
	 * Destination <ListBox>
	 */
	private MultiWordSuggestOracle destOracle = new MultiWordSuggestOracle();
	private FilteredSuggestBox destBox = new FilteredSuggestBox(destOracle);
	
	/**
	 * Strain <ListBox>
	 */
	//private ListBox strainBox = new ListBox();
	private MultiWordSuggestOracle strainOracle = new MultiWordSuggestOracle();
	private FilteredSuggestBox strainBox = new FilteredSuggestBox(strainOracle);
	
	/**
	 * Customer <ListBox>
	 */
	private ListBox custBox = new ListBox();
	
	/**
	 * Fire <ListBox>
	 */
	private final ListBox fireBox = new ListBox();
	/**
	 * reactivity <ListBox>
	 */
	private final ListBox reactivityBox = new ListBox();
	/**
	 * specific <ListBox>
	 */
	private final ListBox specificBox = new ListBox();
	/**
	 * Fraction <ListBox>
	 */
	private final ListBox healthBox = new ListBox();
	/**
	 * Status <ListBox>
	 */
	private ListBox statusBox = new ListBox();

	/**
	 * Oracle for the location building
	 */
	private MultiWordSuggestOracle buildingOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location building <SuggestBox>
	 */
	private FilteredSuggestBox buildingBox = new FilteredSuggestBox(buildingOracle);

	/**
	 * Oracle for the location room
	 */
	private MultiWordSuggestOracle roomOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location room <SuggestBox>
	 */
	private FilteredSuggestBox roomBox = new FilteredSuggestBox(roomOracle);

	/**
	 * Oracle for the location container
	 */
	private MultiWordSuggestOracle holderOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location container <SuggestBox>
	 */
	private FilteredSuggestBox holderBox = new FilteredSuggestBox(holderOracle);

	/**
	 * Oracle for the location shelf
	 */
	private MultiWordSuggestOracle shelfOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location shelf <SuggestBox>
	 */
	private FilteredSuggestBox shelfBox = new FilteredSuggestBox(shelfOracle);

	/**
	 * Oracle for the location packaging
	 */
	private MultiWordSuggestOracle packagingOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location packaging <SuggestBox>
	 */
	private FilteredSuggestBox packagingBox = new FilteredSuggestBox(packagingOracle);

	/**
	 * Oracle for the location sublocation
	 */
	private MultiWordSuggestOracle subLocationOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location subLocation <SuggestBox>
	 */
	private FilteredSuggestBox subLocationBox = new FilteredSuggestBox(subLocationOracle);
	
	/**
	 * TRB number <TextBox>
	 */
	private FilteredNumberTextBox trbNum = new FilteredNumberTextBox();
	
	/**
	 * TRB page number <TextBox>
	 */
	private FilteredNumberTextBox trbPage = new FilteredNumberTextBox();
	
	/**
	 * External ID <TextBox>
	 */
	private FilteredAlphaNumericTextBox externalBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * Storage Notes <TextBox>
	 */
	private FilteredAlphaNumericTextBox storageNotesBox = new FilteredAlphaNumericTextBox();
	
	private List<FileInfo> attachmentList = new ArrayList<FileInfo> ();
	
	private List<FileInfo> attachmentPendingList = new ArrayList<FileInfo> ();
	
	private List<FileInfo> attachmentDeleteList = new ArrayList<FileInfo> ();
	
	private final FileUpload attachFile = new FileUpload();
	
	private final Grid attachmentGrid = new Grid(0, 3);

	private final Button removeButton = new Button(REMOVE_SAMPLE);

	private final Button addButton = new Button(ADD_SAMPLE);

	private final Button downloadButton = new Button(DOWNLOAD_SAMPLE);
	
	/**
	 * attachment ID
	 */
	private String attachmentId = "";
	
	/**
	 * filename
	 */
	private String fileName = "";
	
	private String sessionId = "";

	private HorizontalPanel sampleAttachmentsPanel = new HorizontalPanel();

	private VerticalPanel attachmentsTable = new VerticalPanel();

	private DecoratorPanel attachmentsDPanel = new DecoratorPanel();

	private FormPanel attachmentFormPanel = new FormPanel();

	private final FormPanel fileDownloadFormPanel = new FormPanel();
	/**
	 * <DialogBox> constructor
	 * 
	 * @param sampleIdString <String> Sample ID
	 */
	@SuppressWarnings("unchecked")
	public ModifySampleDialog(String sampleIdString) {
		int row = 0;
		
		initServices();
		
		// The file download form
		
		String fileDownloadUrlTag = GWT.getModuleBaseURL() + URL_ATTACHMENT_DOWNLOAD_SERVICE;
		fileDownloadFormPanel.setAction(fileDownloadUrlTag);
		fileDownloadFormPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
		fileDownloadFormPanel.setMethod(FormPanel.METHOD_GET);
		
		this.sampleIdString = sampleIdString;
		sampleId = Long.parseLong(sampleIdString);
		loadSampleData(sampleId);
		
		loadAttachments(sampleId);
		
		mainPanel.setWidth("100%");
		
		hpanel.add(samplePanel);
		hpanel.setCellHorizontalAlignment(samplePanel, HasHorizontalAlignment.ALIGN_LEFT);
		hpanel.setCellVerticalAlignment(samplePanel, HasVerticalAlignment.ALIGN_MIDDLE);
				
		hpanel.add(labelPanel);
		hpanel.setCellHorizontalAlignment(labelPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hpanel.setCellVerticalAlignment(labelPanel, HasVerticalAlignment.ALIGN_TOP);
				
		mainPanel.add(hpanel);
		hpanel.setCellHorizontalAlignment(hpanel, HasHorizontalAlignment.ALIGN_CENTER);
		hpanel.setCellVerticalAlignment(hpanel, HasVerticalAlignment.ALIGN_TOP);

		h1panel.setCellPadding(10);
		mainPanel.add(h1panel);
		hpanel.setCellHorizontalAlignment(h1panel, HasHorizontalAlignment.ALIGN_CENTER);
		hpanel.setCellVerticalAlignment(h1panel, HasVerticalAlignment.ALIGN_BOTTOM);

		samplePanel.setSize(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
		samplePanel.setCellSpacing(10);
		
		this.setText(PANEL_TITLE_LABEL);

		Label labelLabel = new Label(LABEL_LABEL);
		labelLabel.addStyleDependentName(PUNYLABEL_STYLE);
		labelLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		samplePanel.setWidget(row, 0, labelLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		samplePanel.getCellFormatter().setHorizontalAlignment(row++, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		HTML ruler1Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler1Label);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);

		Label idLabel = new Label(TRACKING_ID_LABEL);
		idLabel.setWordWrap(false);
		samplePanel.setWidget(row, 0, idLabel);

		samplePanel.setWidget(row++, 1, trackingIdLabel);
		
		Label nameLabel = new Label(SAMPLE_ID_LABEL);
		samplePanel.setWidget(row, 0, nameLabel);
		nameLabel.addStyleDependentName(BOLDLABEL_STYLE);

		sampleIdBox.addKeyDownHandler(new MyValueChangeHandler());
		sampleIdBox.addSelectionHandler(new MySelectionHandler());
		samplePanel.setWidget(row++, 1, sampleIdBox);
		loadSampleIds();
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label externalLabel = new Label(EXTERNAL_ID_LABEL);
			samplePanel.setWidget(row, 0, externalLabel);
	
			samplePanel.setWidget(row++, 1, externalBox);
		
			Label ownerLabel = new Label(OWNER_NAME_LABEL);
			ownerLabel.setWordWrap(false);
			samplePanel.setWidget(row, 0, ownerLabel);
			ownerLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, ownerBox);
			ownerBox.addKeyDownHandler(new MyValueChangeHandler());
			ownerBox.addSelectionHandler(new MySelectionHandler());
			loadOwnerNames();
		}
		
		Label custodianLabel = new Label(CUSTODIAN_NAME_LABEL);
		custodianLabel.setWordWrap(false);
		samplePanel.setWidget(row, 0, custodianLabel);

		samplePanel.setWidget(row++, 1, custodianBox);
		loadCustodianNames();
		
		Label descriptorLabel = new Label(DESCRIPTOR_LABEL);
		samplePanel.setWidget(row, 0, descriptorLabel);
		descriptorLabel.addStyleDependentName(BOLDLABEL_STYLE);

		descriptorBox.addChangeHandler(new MyChangeHandler());
		samplePanel.setWidget(row++, 1, descriptorBox);

		HorizontalPanel namePanel = new HorizontalPanel();
		Label sampleLabel = new Label(SAMPLE_ID_LABEL);
		sampleLabel.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(sampleLabel);

		labelNameBox.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(labelNameBox);

		HorizontalPanel labelDatePanel = new HorizontalPanel();
		final Label dateLabel = new Label(ENTRY_DATE);
		dateLabel.addStyleDependentName(FANCYLABEL_STYLE);
		dateValue.addStyleDependentName(FANCYLABEL_STYLE);
		labelDatePanel.setStyleName(FANCYPANEL_STYLE);
		labelDatePanel.add(dateLabel);
		if (theSample != null) {
			if (theSample.getStartCreateDate() != null) {
				Date date = theSample.getStartCreateDate();
				String sdate = date.toString();
				if (sdate != null && !sdate.isEmpty()) dateValue.setText(sdate);
				else {
					dateValue.setText("");
					Window.alert("setting dateValue to empty");
				}
			}
		}
		labelDatePanel.add(dateValue);
		enteredDate.setFormat(new DateBox.DefaultFormat(
				DateTimeFormat.getFormat(DATE_FORMAT_DISPLAY)));

		enteredDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date dt = enteredDate.getValue();
				String dtString = String.valueOf(dt.getMonth()+1)+"/"+String.valueOf(dt.getDate())+"/"+String.valueOf(dt.getYear()+1900);
				dateValue.setText(dtString);
			}							
		});

		HorizontalPanel trbPanel = new HorizontalPanel();
		Label trbLabel = new Label(TRB_LABEL);
		trbLabel.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(trbLabel);
		labelNumBox.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(labelNumBox);
		trbPanel.add(new Label(DASH));
		labelPageBox.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(labelPageBox);

		int labelRow = 60;
		Label contentsLabel = new Label(LABEL_CONTENTS);
		contentsLabel.addStyleDependentName(BOLDFANCYLABEL_STYLE);
		labelPanel.getCellFormatter().setAlignment(labelRow, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		labelPanel.setWidget(labelRow, 0, contentsLabel);
		labelRow++;
		
		HorizontalPanel materialPanel = new HorizontalPanel();
		Label materialLabel = new Label(MATERIAL);
		materialLabel.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(materialLabel);
		labelMaterialBox1.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox1);
		materialPanel.add(labelComma1);
		labelMaterialBox2.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox2);
		materialPanel.add(labelComma2);
		labelMaterialBox3.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox3);
		
		HorizontalPanel labelDestPanel = new HorizontalPanel();
		Label labelDestLabel = new Label(DEST_LABEL);
		labelDestLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelDestPanel.add(labelDestLabel);
		labelDestPanel.setStyleName(FANCYPANEL_STYLE);
		labelDestBox.addStyleDependentName(FANCYLABEL_STYLE);
		labelDestPanel.add(labelDestBox);

		HorizontalPanel labelStrainPanel = new HorizontalPanel();
		Label labelStrainLabel = new Label(STRAIN_LABEL);
		labelStrainLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelStrainPanel.add(labelStrainLabel);
		labelStrainPanel.setStyleName(FANCYPANEL_STYLE);
		labelStrainBox.addStyleDependentName(FANCYLABEL_STYLE);
		labelStrainPanel.add(labelStrainBox);

		HorizontalPanel labelCustPanel = new HorizontalPanel();
		Label labelCustLabel = new Label(CUSTODIAN_LABEL);
		labelCustLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelCustPanel.add(labelCustLabel);
		labelCustPanel.setStyleName(FANCYPANEL_STYLE);
		labelCustBox.addStyleDependentName(FANCYLABEL_STYLE);
		labelCustPanel.add(labelCustBox);

		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
		HorizontalPanel labelOwnPanel = new HorizontalPanel();
		Label ownLabel = new Label(OWNER_NAME_LABEL);
		ownLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelOwnPanel.add(ownLabel);
		labelOwnPanel.setStyleName(FANCYPANEL_STYLE);
		labelOwnBox.addStyleDependentName(FANCYLABEL_STYLE);
		labelOwnPanel.add(labelOwnBox);

		HorizontalPanel labelFirePanel = new HorizontalPanel();
		Label fireLabel = new Label(FIRE_LABEL);
		fireLabel.addStyleDependentName(FANCYLABEL_STYLE);
		fireBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelFirePanel.setStyleName(FANCYPANEL_STYLE);
		labelFirePanel.add(fireLabel);
		labelFirePanel.add(fireBoxLabel);

		HorizontalPanel labelReactivityPanel = new HorizontalPanel();
		Label reactivityLabel = new Label(REACTIVITY_LABEL);
		reactivityLabel.addStyleDependentName(FANCYLABEL_STYLE);
		reactivityBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelReactivityPanel.setStyleName(FANCYPANEL_STYLE);
		labelReactivityPanel.add(reactivityLabel);
		labelReactivityPanel.add(reactivityBoxLabel);

		HorizontalPanel labelSpecificPanel = new HorizontalPanel();
		Label specificLabel = new Label(SPECIFIC_LABEL);
		specificLabel.addStyleDependentName(FANCYLABEL_STYLE);
		specificBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelSpecificPanel.setStyleName(FANCYPANEL_STYLE);
		labelSpecificPanel.add(specificLabel);
		labelSpecificPanel.add(specificBoxLabel);

		HorizontalPanel labelHealthPanel = new HorizontalPanel();
		Label healthLabel = new Label(HEALTH_LABEL);
		healthLabel.addStyleDependentName(FANCYLABEL_STYLE);
		healthBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelHealthPanel.setStyleName(FANCYPANEL_STYLE);
		labelHealthPanel.add(healthLabel);
		labelHealthPanel.add(healthBoxLabel);

		VerticalPanel labelDescriptorBox = new VerticalPanel();
		descriptorLabel1.addStyleDependentName(FANCYDESCLABEL_STYLE);
		descriptorLabel2.addStyleDependentName(FANCYDESCLABEL_STYLE);
		descriptorLabel3.addStyleDependentName(FANCYDESCLABEL_STYLE);
		labelDescriptorBox.addStyleName(FANCYPANEL_STYLE);
		labelDescriptorBox.setHeight(DESCRIPTOR_HEIGHT);
		labelDescriptorBox.setWidth(DESCRIPTOR_WIDTH);
		//this.ownerBox.addStyleName(FANCYPANEL_STYLE);
		labelDatePanel.addStyleName(FANCYPANEL_STYLE);
		trbPanel.addStyleName(FANCYPANEL_STYLE);
		namePanel.addStyleName(FANCYPANEL_STYLE);
		materialPanel.addStyleName(FANCYPANEL_STYLE);
		labelDescriptorBox.add(descriptorLabel1);
		labelDescriptorBox.add(descriptorLabel2);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE))
			labelDescriptorBox.add(descriptorLabel3);
		labelPanel.setWidget(labelRow++, 0, labelDescriptorBox);
		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelPanel.setWidget(labelRow++, 0, trbPanel);
		//}
		labelPanel.setWidget(labelRow++, 0, materialPanel);
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelPanel.setWidget(labelRow++, 0, labelStrainPanel);			
		}
		labelPanel.setWidget(labelRow++, 0, namePanel);
		labelPanel.setWidget(labelRow++, 0, labelDatePanel);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelPanel.setWidget(labelRow++, 0, labelOwnPanel);
			labelPanel.setWidget(labelRow++, 0, labelFirePanel);
			labelPanel.setWidget(labelRow++, 0, labelReactivityPanel);
			labelPanel.setWidget(labelRow++, 0, labelSpecificPanel);
			labelPanel.setWidget(labelRow++, 0, labelHealthPanel);
		} else {
			//labelPanel.setWidget(labelRow++, 0, labelDestPanel);			
			labelPanel.setWidget(labelRow++, 0, labelCustPanel);			
		}
		labelPanel.addStyleName(FANCYPANEL_STYLE);

		// Sample Origin
		Label originLabel = new Label(ORIGIN_LABEL);
		samplePanel.setWidget(row, 0, originLabel);
		loadOrigins();
		samplePanel.setWidget(row++, 1, originListBox);
		
		Label statusLabel = new Label(STATUS_LABEL);
		samplePanel.setWidget(row, 0, statusLabel);

		samplePanel.setWidget(row++, 1, statusBox);
		loadStatus();
		
		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label trbNumLabel = new Label(NUMBER_LABEL);
			samplePanel.setWidget(row, 0, trbNumLabel);
			trbNumLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, trbNum);
			trbNum.addChangeHandler(new MyChangeHandler());
			
			Label trbPageLabel = new Label(PAGE_LABEL);
			samplePanel.setWidget(row, 0, trbPageLabel);
			trbPageLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, trbPage);
			trbPage.addChangeHandler(new MyChangeHandler());
		//}
		HTML ruler2Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler2Label);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label feedstockLabel = new Label(FEEDSTOCK_LABEL);
			samplePanel.setWidget(row, 0, feedstockLabel);
			feedstockLabel.addStyleDependentName(BOLDLABEL_STYLE);

			samplePanel.setWidget(row++, 1, feedstockBox);
			feedstockBox.addSelectionHandler(new MySelectionHandler());
			loadFeedstocks();
			
			Label treatmentLabel = new Label(TREATMENT_LABEL);
			samplePanel.setWidget(row, 0, treatmentLabel);
			treatmentLabel.addStyleDependentName(BOLDLABEL_STYLE);
			samplePanel.setWidget(row++, 1, treatmentBox);
			treatmentBox.addChangeHandler(new MyChangeHandler());
			loadTreatments();
		}
		
		Label fractionLabel = new Label(FRACTION_LABEL);
		samplePanel.setWidget(row, 0, fractionLabel);
		fractionLabel.addStyleDependentName(BOLDLABEL_STYLE);

		samplePanel.setWidget(row++, 1, fractionBox);
		fractionBox.addChangeHandler(new MyChangeHandler());
		loadFraction();
		
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label bioLabel = new Label(BIOMASS_LOT_LABEL);
			samplePanel.setWidget(row, 0, bioLabel);
			//bioLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1,bioBox);
			//bioBox.addChangeHandler(new MyChangeHandler());
			bioBox.addKeyDownHandler(new MyValueChangeHandler());
			bioBox.addSelectionHandler(new MySelectionHandler());
			loadBiomasses();
			
			Label formLabel = new Label(FORM_LABEL);
			samplePanel.setWidget(row, 0, formLabel);
			formLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1,formBox);
			formBox.addChangeHandler(new MyChangeHandler());
			loadForms();
			
			Label compLabel = new Label(COMP_LABEL);
			samplePanel.setWidget(row, 0, compLabel);
			compLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, compBox);
			compBox.addChangeHandler(new MyChangeHandler());
			loadCompositions();

			Label strainLabel = new Label(STRAIN_LABEL);
			samplePanel.setWidget(row, 0, strainLabel);
			strainLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, strainBox);
			strainBox.addSelectionHandler(new MySelectionHandler());
			loadStrains();

			Label destLabel = new Label(DEST_LABEL);
			samplePanel.setWidget(row, 0, destLabel);
			//destLabel.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, destBox);
			//destBox.addChangeHandler(new MyChangeHandler());
			destBox.addKeyDownHandler(new MyValueChangeHandler());
			destBox.addSelectionHandler(new MySelectionHandler());
			loadDestinations();

		}
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label fireLabel1 = new Label(FIRE_LABEL);
			samplePanel.setWidget(row, 0, fireLabel1);
			fireLabel1.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, fireBox);
			fireBox.addKeyDownHandler(new MyValueChangeHandler());
			loadFire();
			
			Label reactivityLabel1 = new Label(REACTIVITY_LABEL);
			samplePanel.setWidget(row, 0, reactivityLabel1);
			reactivityLabel1.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, reactivityBox);
			reactivityBox.addKeyDownHandler(new MyValueChangeHandler());
			loadReactivity();
			
			Label specificLabel1 = new Label(SPECIFIC_LABEL);
			samplePanel.setWidget(row, 0, specificLabel1);
			specificLabel1.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, specificBox);
			specificBox.addKeyDownHandler(new MyValueChangeHandler());
			loadSpecific();
			
			Label healthLabel1 = new Label(HEALTH_LABEL);
			samplePanel.setWidget(row, 0, healthLabel1);
			healthLabel1.addStyleDependentName(BOLDLABEL_STYLE);
	
			samplePanel.setWidget(row++, 1, healthBox);
			healthBox.addKeyDownHandler(new MyValueChangeHandler());
			loadHealth();
		}
		
		Label amountLabel = new Label(AMOUNT_LABEL);
		samplePanel.setWidget(row, 0, amountLabel);

		samplePanel.setWidget(row, 1, amountBox);

		samplePanel.setWidget(row++, 2, unitsBox);
		loadUnits();

		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {

			HTML ruler3Label = new HTML(HORIZONTAL_RULE, false);
			samplePanel.setWidget(row, 0, ruler3Label);
			samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
			
			Label buildingLabel = new Label(BUILDING_LABEL);
			samplePanel.setWidget(row, 0, buildingLabel);
	
			samplePanel.setWidget(row++, 1, buildingBox);
			loadBuildings();
			
			Label roomLabel = new Label(ROOM_LABEL);
			samplePanel.setWidget(row, 0, roomLabel);
	
			samplePanel.setWidget(row++, 1, roomBox);
			loadRooms();
			
			Label subLocationLabel = new Label(SUBLOCATION_LABEL);
			samplePanel.setWidget(row, 0, subLocationLabel);
	
			samplePanel.setWidget(row++, 1, subLocationBox);
			loadSubLocation();
			
			Label shelfLabel = new Label(SHELF_LABEL);
			samplePanel.setWidget(row, 0, shelfLabel);
	
			samplePanel.setWidget(row++, 1, shelfBox);
			loadShelves();
			
			Label holderLabel = new Label(HOLDER_LABEL);
			samplePanel.setWidget(row, 0, holderLabel);
	
			samplePanel.setWidget(row++, 1, holderBox);
			loadHolders();
			
			Label packagingLabel = new Label(PACKAGING_LABEL);
			samplePanel.setWidget(row, 0, packagingLabel);
	
			samplePanel.setWidget(row++, 1, packagingBox);
			loadPackaging();
			
		} else { 
			if (theSample != null) {
				setListBox(theSample.getForm(), formBox);
				changeLabel(formBox);
			} 
			if (theSample != null) {
				setListBox(theSample.getFraction(), fractionBox);
				changeLabel(fractionBox);
			} 
			if (theSample != null) {
				setListBox(theSample.getComposition(), compBox);
				changeLabel(compBox);
			} 
			/*
			tsa.findSampleById(sampleId, new AsyncCallback<SampleCriteria>() {

				public void onFailure(Throwable caught) {
					Window.alert("The sample failed to load.\nDatabase may not be available.");				
				}

				public void onSuccess(SampleCriteria result) {
					if (result != null) {
						theSample = result;
						if (theSample.getFraction() == null || theSample.getFraction().isEmpty())
							for (int i=0;i<fractionBox.getItemCount();i++) {
								String item = fractionBox.getItemText(i);
								if (item.equals(theSample.getFraction())) {
									fractionBox.setSelectedIndex(i);
									break;
								}
							}
						if (theSample.getForm() == null || theSample.getForm().isEmpty())
							for (int i=0;i<formBox.getItemCount();i++) {
								String item = formBox.getItemText(i);
								if (item.equals(theSample.getForm())) {
									formBox.setSelectedIndex(i);
									break;
								}
							}
						if (theSample.getComposition() == null || theSample.getComposition().isEmpty())
							for (int i=0;i<compBox.getItemCount();i++) {
								String item = compBox.getItemText(i);
								if (item.equals(theSample.getComposition())) {
									compBox.setSelectedIndex(i);
									break;
								}
							}	
					}
				}
			});
			*/
		}
		Label storageNotesLabel = new Label(STORAGE_NOTES_LABEL);
		samplePanel.setWidget(row, 0, storageNotesLabel);

		samplePanel.setWidget(row++, 1, storageNotesBox);

		HTML ruler4Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler4Label);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		Label enteredLabel = new Label(DATE_ENTERED_LABEL);
		enteredLabel.setWordWrap(false);
		samplePanel.setWidget(row, 0, enteredLabel);

		samplePanel.setWidget(row++, 1, enteredDate);
		
		Label commentLabel = new Label(COMMENTS_LABEL);
		samplePanel.setWidget(row, 0, commentLabel);

		samplePanel.setWidget(row++, 1, commentBox);

		HTML fiveHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, fiveHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		
		row++;
		
		final int rowf1 = row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result2) {
				//Window.alert("From server:session="+result);
				sessionId = result2;
				tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here");
					}
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							initAttachmentForm(samplePanel, rowf1);
						}
					}
				});
			}
		});
		
		//loadSampleData(sampleId);
		row+=2;
		
		HTML ruler5Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler5Label);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		Button saveButton = new Button(SAVE_BUTTON_LABEL);
		h1panel.setWidget(0, 0, saveButton);
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveSample(true);
			}			
		});
		
		Button newButton = new Button(NEW_BUTTON_LABEL);
		h1panel.setWidget(0, 15, newButton);
		newButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveSample(false);
			}			
		});
		
		Button cancelButton = new Button(CANCEL_BUTTON_LABEL);
		h1panel.setWidget(0, 30, cancelButton);
		cancelButton.addClickHandler(new CancelClickHandler());
		
		mainPanel.add(fileDownloadFormPanel);
		
		this.add(mainPanel);
		this.setSize(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
		
		this.setPopupPositionAndShow(new PositionCallback() {

			public void setPosition(int offsetWidth, int offsetHeight) {

				int left = (Window.getClientWidth() - offsetWidth) / 3;
				int top = (Window.getClientHeight() - offsetHeight) / 3;
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				msd.setPopupPosition(left, top);
			}			
		});
	}
	
	private void initAttachmentForm(FlexTable mainPanel, int row) {		
		// Attachment List
		String attachUrlTag = GWT.getModuleBaseURL() + URL_ATTACHMENT_UPLOAD_SERVICE;
		attachmentFormPanel.setAction(attachUrlTag);
		attachmentFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		attachmentFormPanel.setMethod(FormPanel.METHOD_POST);
		attachmentFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				attachmentId = event.getResults();
				//Code work-around for FireFox anomaly; 
				cleanUp();
				// End of work-around
				ListIterator<FileInfo> it = attachmentPendingList.listIterator();
				while (it.hasNext()) {
					FileInfo info = it.next();
					if (info.getFilename().equals(fileName)) {
						//it.remove();
						info.setAttachment_id(attachmentId);
						//attachmentPendingList.add(info);
						// update grid
                	    int gridRow = attachmentGrid.getRowCount();
                	    for (int i = 1; i < gridRow; i++) {
                	    	Label tempLabel = ((Label)attachmentGrid.getWidget(i, ATTACHMENT_ID_COL));
                	    	if (tempLabel != null) {
	                	    	if (tempLabel.getText() != null && tempLabel.getText().equals("")) {
			                	    attachmentGrid.setWidget(i, ATTACHMENT_ID_COL, new Label(info.getAttachment_id()));
			                	    break;
	                	    	}
                	    	}
                	    }
					}
				}
			}
			
			/**
			 * Private method to remove the HTML tags from around sampleId.
			 */
			private void cleanUp() {
				if (attachmentId.toLowerCase().startsWith("<")) {
					int index = attachmentId.indexOf(">");
					attachmentId = attachmentId.substring(index + 1, attachmentId.length() - 1);
					index = attachmentId.indexOf("<");
					attachmentId = attachmentId.substring(0, index);
				}
			}			
		});
		attachmentsDPanel.setWidth("400px");
		attachmentsTable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		attachmentsTable.setWidth("400px");
		attachmentsTable.setHeight("400px");
		attachmentsDPanel.add(attachmentsTable);
		attachmentFormPanel.add(attachmentsDPanel);
		mainPanel.setWidget(row, 0, attachmentFormPanel);
		mainPanel.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		mainPanel.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		mainPanel.getFlexCellFormatter().setRowSpan(row, 0, 2);
		mainPanel.getFlexCellFormatter().setColSpan(row, 0, 3);

		Label attachmentLabel = new Label("Select a file to attach:");
		sampleAttachmentsPanel.add(attachmentLabel);
		
		sampleAttachmentsPanel.add(attachFile);
		
		attachmentsTable.add(sampleAttachmentsPanel);
		
		Label smAttachmentsLabel = new Label("List of Attachments");
		smAttachmentsLabel.addStyleDependentName("SubSubTitle");
		attachmentsTable.add(smAttachmentsLabel);
		
		attachmentGrid.setWidth("100%");

		attachmentGrid.addClickHandler(new ItemSelectClickHandler());
		attachmentGrid.setCellSpacing(3);
		attachmentGrid.setBorderWidth(1);
		attachmentGrid.insertRow(0);
		
		attachmentGrid.setWidget(0, ATTACHMENT_SELECT_COL, new Label(SELECT_ROW));
		attachmentGrid.getCellFormatter().setWordWrap(0, ATTACHMENT_SELECT_COL, false);
		attachmentGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);
		attachmentGrid.setWidget(0, ATTACHMENT_ID_COL, new Label(ATTACHMENT_ID));
		attachmentGrid.getCellFormatter().setWordWrap(0, ATTACHMENT_ID_COL, false);
		attachmentGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_ID_COL, HasHorizontalAlignment.ALIGN_CENTER);
		attachmentGrid.setWidget(0, ATTACHMENT_NAME_COL, new Label(ATTACHMENT));
		attachmentGrid.getCellFormatter().setWordWrap(0, ATTACHMENT_NAME_COL, false);
		attachmentGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_NAME_COL, HasHorizontalAlignment.ALIGN_CENTER);
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidth("100%");
		VerticalPanel scrollVert = new VerticalPanel();
		attachmentGrid.setVisible(false);
		scrollVert.add(attachmentGrid);
		scrollVert.setHeight("100px");
		scrollPanel.add(scrollVert);
		attachmentsTable.add(scrollPanel);

		sampleAttachmentsPanel.setWidth("400px");
		sampleAttachmentsPanel.setHeight("30px");
		attachmentsDPanel.setWidth("400px");
		attachmentsDPanel.setHeight("100px");
		attachmentsTable.setWidth("400px");
		attachmentsTable.setHeight("100px");

		tsa.findAttachmentsInfoByTrackingId(sampleId, new AsyncCallback<Collection<FileInfo>>() {
 
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problems retrieving attachment list");
			}

			@Override
			public void onSuccess(Collection<FileInfo> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<FileInfo> it = result.iterator();
					int row = 0;
					while (it.hasNext()) {
						FileInfo attachment = it.next();
						row++;
						attachmentGrid.insertRow(row);
						attachmentGrid.setWidget(row, ATTACHMENT_SELECT_COL, new CheckBox());
						attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_SELECT_COL, false);
						attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);
						attachmentGrid.setWidget(row, ATTACHMENT_ID_COL, new Label((attachment != null) ? attachment.getAttachment_id() : "---"));
						attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_ID_COL, false);
						attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_ID_COL, HasHorizontalAlignment.ALIGN_CENTER);
						attachmentGrid.setWidget(row, ATTACHMENT_NAME_COL, new Label((attachment != null) ? attachment.getName() : "---"));
						attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_NAME_COL, false);
						attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_NAME_COL, HasHorizontalAlignment.ALIGN_CENTER);
					}
				}
				attachmentGrid.setVisible(attachmentGrid.getRowCount()>1);
			}
		});

		HorizontalPanel attachmentButtonPanel = new HorizontalPanel();
		attachmentButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		attachmentButtonPanel.setSpacing(20);
		addButton.addClickHandler(new AddAttachmentClickHandler());
		attachmentButtonPanel.add(addButton);
		removeButton.addClickHandler(new RemoveAttachmentClickHandler());
		attachmentButtonPanel.add(removeButton);
		removeButton.setEnabled(false);
		downloadButton.addClickHandler(new DownloadAttachmentClickHandler());
		attachmentButtonPanel.add(downloadButton);
		downloadButton.setEnabled(false);
		attachmentsTable.add(attachmentButtonPanel);
	}

	/**
	 * Private method to save the sample data to the database
	 *  through calls to the server.
	 */
	private void saveSample(boolean modify) {
		try {
		change.setText(String.valueOf(modify));
		if (!modify)
			theSample.setTrackingId(0);
		
		if (sampleIdBox.isValid())
			theSample.setSampleId(sampleIdBox.replaceQuotes(sampleIdBox.getValue()));
		else {
			Window.alert("The Sample ID is invalid.");
			return;
		}
		
		//if (bioBox.getValue() != null) {
		//	theSample.setBiomass_lots(bioBox.getValue());
		//}
		
		if (destBox != null && destBox.getText() != null) {
			theSample.setDestination(destBox.getText());
		}
		
		if (strainBox != null && strainBox.getText() != null) {
			theSample.setStrain(strainBox.getText());
		}
		
		if (enteredDate.getValue() != null)
			theSample.setStartCreateDate(enteredDate.getValue());
		
		if (descriptorBox.isValid())
			theSample.setLabelDescription(descriptorBox.replaceQuotes(descriptorBox.getValue()));
		else {
			Window.alert("The Label Descriptor is invalid.");
			return;
		}
		
		if (commentBox.isValid())
			theSample.setComments(commentBox.replaceQuotes(commentBox.getValue()));
		else {
			Window.alert("The Comment is invalid.");
			return;
		}
		
		if (amountBox.isValid())
			theSample.setAmount(amountBox.replaceQuotes(amountBox.getValue()));
		else {
			Window.alert("The Amount is invalid.");
			return;
		}
		
		theSample.setUnits(unitsBox.getItemText(unitsBox.getSelectedIndex()));
		
		if (externalBox.isValid())
			theSample.setExternalId(externalBox.replaceQuotes(externalBox.getValue()));
		else {
			Window.alert("The External ID is invalid.");
			return;
		}
		
		if (custodianBox != null && custodianBox.isValid())
			theSample.setCustodianName(custodianBox.replaceQuotes(custodianBox.getValue()));
		else {
			Window.alert("The Custodian Name is invalid.");
			return;
		}
		
		if (ownerBox != null && ownerBox.isValid())
			theSample.setOwnerName(ownerBox.replaceQuotes(ownerBox.getValue()));
		else {
			Window.alert("The Owner Name is invalid.");
			return;
		}
		
		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			if (!trbNum.isValid()) {
				Window.alert("The TRB Number is invalid.");
				return;
			}
			// Parse TRB num text into an int.
			int num = -1;
			try {
				num = Integer.parseInt(trbNum.getValue());
			} catch (NumberFormatException nfe) {
				//Window.alert("failed to parse num = "+trbNum.getValue());
			}
			if (num != -1)
				theSample.setTrbNum(num);
	
			if (!trbPage.isValid()) {
				Window.alert("The TRB Number is invalid.");
				return;
			}
			// Parse TRB page text into an int.
			int page = -1;
			try {
				page = Integer.parseInt(trbPage.getValue());
			} catch (NumberFormatException nfe) {
				//Window.alert("failed to parse page = "+trbPage.getValue());
			}
			if (page != -1)
				theSample.setTrbPage(page);
		//}
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			theSample.setTreatment(treatmentBox.getItemText(treatmentBox.getSelectedIndex()));
			
			if (feedstockBox.isValid())
				theSample.setFeedstock(feedstockBox.replaceQuotes(feedstockBox.getValue()));
			else {
				Window.alert("The Feedstock is invalid.");
				return;
			}
		}
		if (fractionBox != null && fractionBox.getSelectedIndex() > -1)
			theSample.setFraction(fractionBox.getItemText(fractionBox.getSelectedIndex()));
		if (formBox != null && formBox.getSelectedIndex() > -1)
			theSample.setForm(formBox.getItemText(formBox.getSelectedIndex()));
		if (bioBox != null)
			theSample.setBiomass_lots(bioBox.getText());
		if (compBox != null && compBox.getSelectedIndex() > -1)
			theSample.setComposition(compBox.getItemText(compBox.getSelectedIndex()));
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			if (fireBox.getSelectedIndex()>-1)
				theSample.setFire(fireBox.getItemText(fireBox.getSelectedIndex()).substring(0,1));
			if (reactivityBox.getSelectedIndex()>-1)
				theSample.setReactivity(reactivityBox.getItemText(reactivityBox.getSelectedIndex()).substring(0,1));
			if (specificBox.getSelectedIndex()>-1)
				theSample.setSpecific(specificBox.getItemText(specificBox.getSelectedIndex()).substring(0,1));
			if (healthBox.getSelectedIndex()>-1)
				theSample.setHealth(healthBox.getItemText(healthBox.getSelectedIndex()).substring(0,1));
		}
		if (originListBox != null && originListBox.getSelectedIndex() > -1)
			theSample.setOrigin(originListBox.getItemText(originListBox.getSelectedIndex()));
		if (statusBox != null && statusBox.getSelectedIndex() > -1)
			theSample.setStatus(statusBox.getItemText(statusBox.getSelectedIndex()));
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			if (buildingBox.isValid())
				theSample.setBuilding(buildingBox.replaceQuotes(buildingBox.getValue()));
			else {
				Window.alert("The Building location is invalid.");
				return;
			}
			
			if (roomBox.isValid())
				theSample.setRoom(roomBox.replaceQuotes(roomBox.getValue()));
			else {
				Window.alert("The Room location is invalid.");
				return;
			}
			
			if (holderBox.isValid())
				theSample.setHolder(holderBox.replaceQuotes(holderBox.getValue()));
			else {
				Window.alert("The Holder location is invalid.");
				return;
			}
			
			if (shelfBox.isValid())
				theSample.setShelf(shelfBox.replaceQuotes(shelfBox.getValue()));
			else {
				Window.alert("The Shelf location is invalid.");
				return;
			}
			
			if (packagingBox.isValid())
				theSample.setPackaging(packagingBox.replaceQuotes(packagingBox.getValue()));
			else {
				Window.alert("The Packaging location is invalid.");
				return;
			}
			
			if (subLocationBox.isValid())
				theSample.setSubLocation(subLocationBox.replaceQuotes(subLocationBox.getValue()));
			else {
				Window.alert("The Sub-location location is invalid.");
				return;
			}
			
		}
		if (storageNotesBox.isValid())
			theSample.setStorageNotes(storageNotesBox.replaceQuotes(storageNotesBox.getValue()));
		else {
			Window.alert("The Storage Notes is invalid.");
			return;
		}
		if (attachmentPendingList != null && !attachmentPendingList.isEmpty()) {
			Iterator<FileInfo> it = attachmentPendingList.iterator();
			while (it.hasNext()) {
				FileInfo info = it.next();
				if (info.getAttachment_id() != null && !info.getAttachment_id().isEmpty()) {
					long attachId = 0;
					try {
						attachId = Long.parseLong(info.getAttachment_id());
					} catch (NumberFormatException nfe) {
						Window.alert("Problems parsing attachment id="+info.getAttachment_id());
					}
					if (attachId != 0)
					{
						theSample.getAttachmendIds().add(new Long(attachId));
					}		
				}
			}
		}
		
		if (attachmentDeleteList != null && !attachmentDeleteList.isEmpty()) {
			ListIterator<FileInfo> dit = attachmentDeleteList.listIterator();
			while (dit.hasNext()) {
				FileInfo fileInfo = dit.next();
				tsa.removeAttachment(Long.parseLong(fileInfo.getAttachment_id()), theSample, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("The attachment was not deleted. Error: " + caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						// do nothing
					}
				});
				dit.remove();
			}
		}
		
		labelMaterialBox3.setText(constructMaterial());
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
		tsa.saveSample(sessionId, theSample, new AsyncCallback<Long>() {

			public void onFailure(Throwable caught) {
				Window.alert("The sample was not saved.\nAn error was thrown: " + caught.getMessage());
			}

			public void onSuccess(Long result) {
				if (result > 0) {
					Window.alert("The sample was saved.");
					if (!change.getText().equals("false"))
						msd.hide();
				} else
					Window.alert("The sample was not saved.");
			}			
		});
		} catch (Exception e1) {
			Window.alert(e1.getMessage());
		}
	}
	
	/**
	 * Private method to load packaging.
	 */
	private void loadPackaging() {
		tsa.getPackaging(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Packaging failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							packagingOracle.add(suggestion);
					}
				}
			}			
		});		
	}
	
	/**
	 * Private method to load sublocations.
	 */
	private void loadSubLocation() {
		tsa.getSubLocations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Sublocations failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							subLocationOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the location shelf data
	 */
	private void loadShelves() {
		tsa.getShelves(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Shelves failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							shelfOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the location container data
	 */
	private void loadHolders() {
		tsa.getHolders(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Containers failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							holderOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the location room data
	 */
	private void loadRooms() {
		tsa.getRooms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Rooms failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							roomOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the location building data
	 */
	private void loadBuildings() {
		tsa.getBuildings(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Buildings failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							buildingOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the status data
	 */
	private void loadStatus() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				String sessionId = result;
				tsa.getStatusNames(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						Window.alert("Status failed to load.\nDatabase may not be available.");				
					}
		
					public void onSuccess(Collection<String> result) {
						if ((result != null) && (!result.isEmpty())) {
							statusBox.clear();
							statusBox.addItem("");
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String item = it.next();
								if (item != null && !item.isEmpty())
									statusBox.addItem(item);
							}
						}
						if (theSample != null)
							setListBox(theSample.getStatus(), statusBox);
					}			
				});		
			}
		});
	}

	/**
	 * Protected method to set the selected item in a list box
	 *  by value.
	 */
	protected void setListBox(String value, ListBox listBox) {
		if (value != null) {
			//Window.alert("value="+value+";listBox.getItemCount()="+listBox.getItemCount());
			for (int i = 0; i < listBox.getItemCount(); i++) {
				if (value.equalsIgnoreCase(listBox.getItemText(i))) {
					listBox.setSelectedIndex(i);
					//Window.alert("setSelected="+i);
					break;
				}
			}
		}
	}

	/**
	 * Protected method to set the selected item in a list box
	 *  by value.
	 */
	protected void setShortListBox(String value, ListBox listBox) {
		if (value != null) {
			for (int i = 0; i < listBox.getItemCount(); i++) {
				if (value.length() <= listBox.getItemText(i).length()) {
					if (value.equalsIgnoreCase(listBox.getItemText(i).substring(0,value.length()))) {
						listBox.setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	/**
	 * Private method to populate the fraction data
	 */
	private void loadFraction() {
		tsa.getFractionNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Fractions failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					fractionBox.clear();
					fractionBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							fractionBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getFraction(), fractionBox);
					changeLabel(fractionBox);
				}
			}			
		});		
	}

	/**
	 * Private method to load the biomass list box.
	 */
	private void loadBiomasses() {
		tsa.getBiomasses(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Biomasses failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					bioOracle.clear();
					int ctr=0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							bioOracle.add(name);
						}
						ctr++;
					}
					if (theSample != null) {
						bioBox.setText(theSample.getBiomass_lots());
						//changeLabel(bioBox);
					} else {
						//Window.alert("theSample was null");
					}
				} else {
					//Window.alert("There are no biomasses loaded.\nCheck with administrator.");
				}
			}			
		});		
	}

	/**
	 * Private method to populate the form data
	 */
	private void loadForms() {
		tsa.getForms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Forms failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				//Window.alert("in loadForms.success");
				if (result != null && !result.isEmpty()) {
					formBox.clear();
					formBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							formBox.addItem(item);
					}
				} else {
					//Window.alert("result was empty");
				}
				if (theSample != null) {
					//Window.alert("in loadForm.sample loaded");
					setListBox(theSample.getForm(), formBox);
					changeLabel(formBox);
				} else {
					//Window.alert("theSample was null");
				}
			}			
		});		
	}

	/**
	 * Private method to populate the composition data
	 */
	private void loadCompositions() {
		tsa.getCompositions(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Compositions failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					compBox.clear();
					compBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							compBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getComposition(), compBox);
					changeLabel(compBox);
				//	//labelMaterialBox3.setText(constructMaterial());
				}
			}			
		});		
	}

	/**
	 * Private method to populate the composition data
	 */
	private void loadDestinations() {
		tsa.getDestinations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Destinations failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (!result.isEmpty()) {
					destOracle.clear();
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							destOracle.add(item);
					}
				}
				if (theSample != null) {
					destBox.setText(theSample.getDestination());
					changeLabel(destBox);
				}
			}			
		});		
	}

	/**
	 * Private method to populate the strain data
	 */
	private void loadStrains() {
		tsa.getStrains(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Strains failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				strainOracle.clear();
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							strainOracle.add(item);
					}
				}
				if (theSample != null) {
					strainBox.setText(theSample.getStrain());
					changeLabel(strainBox);
				}
			}			
		});		
	}

	/**
	 * Private method to populate the feedstock data
	 */
	private void loadFeedstocks() {
		tsa.getFeedstockNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Feedstocks failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							feedstockOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the treatment data
	 */
	private void loadTreatments() {
		tsa.getTreatmentNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (!result.isEmpty()) {
					treatmentBox.clear();
					treatmentBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							treatmentBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getTreatment(), treatmentBox);
					changeLabel(treatmentBox);
				}
			}			
		});		
	}

	/**
	 * Private method to populate the owner name data
	 */
	private void loadOwnerNames() {
		tsa.getOwnerNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Owner names failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							ownerOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to populate the custodian names data
	 */
	private void loadCustodianNames() {
		tsa.getCustodianNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Custodian names failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							custodianOracle.add(suggestion);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getCustodianName(), custBox);
					changeLabel(custBox);
				}
			}			
		});		
	}

	/**
	 * Private method to populate the units data
	 */
	private void loadUnits() {
		tsa.getUnits(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Units failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					unitsBox.clear();
					unitsBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							unitsBox.addItem(item);
					}
				}
				if (theSample != null)
					setListBox(theSample.getUnits(), unitsBox);
			}			
		});		
	}

	/**
	 * Private method to populate the sample names data
	 */
	private void loadSampleIds() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-2");
			}

			@Override
			public void onSuccess(String sessionId) {
				tsa.getSampleIds(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						Window.alert("Names failed to load.\nDatabase may not be available.");				
					}
		
					public void onSuccess(Collection<String> result) {
						if (result != null && !result.isEmpty()) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String suggestion = it.next();
								if (suggestion != null)
									sampleIdOracle.add(suggestion);
							}
						}
						if (theSample != null) {
							if (theSample.getSampleId() != null && !theSample.getSampleId().isEmpty()) {
								sampleIdBox.setValue(theSample.getSampleId());
								changeLabel(sampleIdBox);
							}
						}
					}			
				});		
			}
		});
	}

    private int getSplitIndex(String text) {
    	int div=0;
    	String punct = ",.!?; \" ') ";
    	if (text.length()<MAX_PER_LINE) return -1;
    	int start=START_POSITION;
    	int which = 0;
    	while (div < 1 && which+1 < punct.length()) {
    		String ch = punct.substring(which,++which);
    		div = text.indexOf(ch, start);
    		if (div > 0) {
    			if (div > MAX_PER_LINE) 
    				div = -1;
    		}
    	}
    	if (div < 1)
    		div = MAX_PER_LINE;
    	return div;
    }

    private void changeLabel(Object obj) {
		String val = "";
		if (obj instanceof TextBox) {
			TextBox tb = (TextBox)obj;
			val = tb.getValue();
			if (tb == descriptorBox) {
			    String descriptor1 = "";
			    String descriptor2 = "";
			    String descriptor3 = "";
			    String actual = val;
			    if (actual != null && !actual.isEmpty()) {
			    	int index = getSplitIndex(actual);
			    	if (index < 1) {
			    		descriptor1 = actual;
			    	} else {
			    		descriptor1 = actual.substring(0,index+1);
				    	descriptor2 = actual.substring(index+1);
				    	if (descriptor2.length()>MAX_PER_LINE) {
				    		int index1 = getSplitIndex(descriptor2);
					    	if (index1 > 1) {
						    	descriptor3 = descriptor2.substring(index1+1);
					    		descriptor2 = descriptor2.substring(0,index1+1);
					    	}
				    	}
				    	if (descriptor2 != null && !descriptor2.isEmpty() && descriptor2.substring(0,1).equals(SPACE))
				    		descriptor2 = descriptor2.substring(1);
				    	if (descriptor3 != null && !descriptor3.isEmpty() && descriptor3.substring(0,1).equals(SPACE))
				    		descriptor3 = descriptor3.substring(1);
			    	}
			    }
			    descriptorLabel1.setText(descriptor1);
			    descriptorLabel2.setText(descriptor2);
			    descriptorLabel3.setText(descriptor3);
			} else if (tb == trbNum) {
				String zeroes = "00000";
				String nval = zeroes.substring(0,5-val.length())+val;
				labelNumBox.setText(nval);
			} else if (tb == trbPage) {
				String zeroes = "000";
				String nval = zeroes.substring(0,3-val.length())+val;
				labelPageBox.setText(nval);
			}
		} else if (obj instanceof SuggestBox) {
			SuggestBox sb = (SuggestBox)obj;
			if (sb == feedstockBox) {
				val = feedstockBox.getValue();
				if (val != null && !val.isEmpty()) { 
					labelMaterialBox1.setText(val);
					labelComma1.setText(COMMA);
				}
				else
					labelComma1.setText("");
			} else if (sb == ownerBox) {
				val = ownerBox.getValue();
				labelOwnBox.setText(val);
			} else if (sb == sampleIdBox) {
				val = sampleIdBox.getValue();
				labelNameBox.setText(val);				
			} else if (sb == strainBox) {
				val = strainBox.getValue();
				labelStrainBox.setText(val);				
			}
		} else if (obj instanceof ListBox) {
			ListBox lb = (ListBox)obj;
			int index = lb.getSelectedIndex();
			if (index == -1) {
				index = 0;
				//Window.alert("index was -1");
			}
			val = lb.getItemText(index);
			if (lb == formBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == fractionBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == compBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == treatmentBox) {
				if (val != null && !val.isEmpty()) { 
					labelMaterialBox2.setText(val);
					if (val != null && !val.isEmpty()) 
						labelComma2.setText(COMMA);
					else
						labelComma2.setText("");
				}
			} else if (lb == fireBox) {
				if (val != null && !val.isEmpty())
					fireBoxLabel.setText(val.substring(0,1));
				else
					fireBoxLabel.setText(BLANK);
			} else if (lb == reactivityBox) {
				if (val != null && !val.isEmpty())
					reactivityBoxLabel.setText(val.substring(0,1));
				else
					reactivityBoxLabel.setText(BLANK);
			} else if (lb == specificBox) {
				if (val != null && !val.isEmpty())
					specificBoxLabel.setText(transformSpecific(val));
				else
					specificBoxLabel.setText(BLANK);
			} else if (lb == healthBox) {
				if (val != null && !val.isEmpty())
					healthBoxLabel.setText(val.substring(0,1));
				else
					healthBoxLabel.setText(BLANK);
			}
		} else if (obj instanceof Label) {
			Label lb = (Label)obj;
			//Window.alert("in label type");
			if (lb == dateValue) {
				
			}
		}
    }
    private String constructMaterial(SampleCriteria sample) {
    	String val = "";
		String form = "";
		if (sample.getForm() != null && !sample.getForm().isEmpty())
			form = sample.getForm();
		String fraction = "";
		if (sample.getFraction() != null && !sample.getFraction().isEmpty())
			fraction = sample.getFraction();
		String comp = "";
		if (sample.getComposition() != null && !sample.getComposition().isEmpty())
			comp = sample.getComposition();
		if (!form.isEmpty() && form != null)
			val += form;
		if (!fraction.isEmpty() && fraction != null) {
			if (!val.isEmpty()) val += ",";
			val += fraction;
		}
		if (!comp.isEmpty() && comp != null) {
			if (!val.isEmpty()) val += ",";
			val += comp;
		}
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelMaterialBox1.setText("Biomass");
			labelComma1.setText(COMMA);
			labelMaterialBox2.setText("");
			labelComma2.setText("");
		}
		
    	return val;
    }
    private String constructMaterial() {
    	String val = "";
		String form = "";
		if (formBox.getSelectedIndex() != -1)
			form = formBox.getItemText(formBox.getSelectedIndex());
		String fraction = "";
		if (fractionBox.getSelectedIndex() != -1)
			fraction = fractionBox.getItemText(fractionBox.getSelectedIndex());
		String comp = "";
		if (compBox.getSelectedIndex() != -1)
			comp = compBox.getItemText(compBox.getSelectedIndex());
		if (form != null && !form.isEmpty())
			val += form;
		if (fraction != null && !fraction.isEmpty()) {
			if (!val.isEmpty()) val += ",";
			val += fraction;
		}
		if (comp != null && !comp.isEmpty()) {
			if (!val.isEmpty()) val += ",";
			val += comp;
		}
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelMaterialBox1.setText("Biomass");
			labelComma1.setText(COMMA);
			labelMaterialBox2.setText("");
			labelComma2.setText("");
		}
		
    	return val;
    }
	private String transformSpecific(String input) {
		int dash = 0;
		String value = "";
		if (input == null || input.isEmpty()) return "";
		dash = input.indexOf(" - ");
		if (dash != -1)
			value = input.substring(0,dash);
		return value;
	}
	
	private class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			ListIterator<FileInfo> it = attachmentPendingList.listIterator();
			while (it.hasNext()) {
				FileInfo fileInfo = it.next();
				tsa.removeAttachment(Long.parseLong(fileInfo.getAttachment_id()), theSample, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// do nothing
					}

					@Override
					public void onSuccess(Boolean result) {
						// do nothing
					}
				});
			}
			msd.hide();
		}
	}
	
	public class MyChangeHandler implements ChangeHandler {

	    @Override
		public void onChange(ChangeEvent event) {
			Object obj = event.getSource();
			changeLabel(obj);
		}
	}
	@SuppressWarnings("unchecked")
	public class MyValueChangeHandler implements ValueChangeHandler, KeyDownHandler {

		@Override
		public void onValueChange(ValueChangeEvent event) {
			Object obj = event.getSource();
			changeLabel(obj);
		}

		@Override
		public void onKeyDown(KeyDownEvent event) {
			Object obj = event.getSource();
			changeLabel(obj);			
		}
	}
	@SuppressWarnings("unchecked")
	public class MySelectionHandler implements SelectionHandler {

		@Override
		public void onSelection(SelectionEvent event) {
			Object obj = event.getSource();
			changeLabel(obj);			
		}

	}
	/**
	 * Private method to find a sample by its ID and load it into
	 *  the dialog screen.
	 * 
	 * @param sampleId2 <long> Sample ID
	 */
	private void loadSampleData(long sampleId2) {
		tsa.findSampleById(sampleId2, new AsyncCallback<SampleCriteria>() {

			public void onFailure(Throwable caught) {
				Window.alert("The sample failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(SampleCriteria result) {
				try {
					if (result != null) {
						theSample = result;
						trackingIdLabel.setText(String.valueOf(theSample.getTrackingId()));
						if (theSample != null && theSample.getSampleId() != null && !theSample.getSampleId().isEmpty()) {
							sampleIdBox.setValue(theSample.getSampleId());
						}
						if (theSample != null && theSample.getComments() != null && !theSample.getComments().isEmpty()) {
							commentBox.setValue(theSample.getComments());
						}
						if (theSample != null && theSample.getLabelDescription() != null && !theSample.getLabelDescription().isEmpty()) {
							descriptorBox.setValue(theSample.getLabelDescription());
							changeLabel(descriptorBox);
						}
						if (theSample != null && theSample.getAmount() != null && !theSample.getAmount().isEmpty()) {
							amountBox.setValue(theSample.getAmount());
						}
						if (theSample != null && theSample.getUnits() != null && !theSample.getUnits().isEmpty()) {
							setListBox(theSample.getUnits(), unitsBox);
						}
						if (theSample != null && theSample.getStartCreateDate() != null) {
							enteredDate.setValue(theSample.getStartCreateDate());
						}
						if (theSample.getCustodianName() != null && !theSample.getCustodianName().isEmpty())
							custodianBox.setValue(theSample.getCustodianName());
						//Window.alert(theSample.getCustodianName());
						//Window.alert(theSample.getStrain());
						if (theSample.getStrain() != null && !theSample.getStrain().isEmpty()) {
							strainBox.setText(theSample.getStrain());
						}
						if (theSample.getDestination() != null && !theSample.getDestination().isEmpty()) {
							destBox.setText(theSample.getDestination());
						}
						if (theSample.getOrigin() != null && !theSample.getOrigin().isEmpty()) {
							if (theSample != null)
								setListBox(theSample.getOrigin(), originListBox);
						}
						if (theSample.getOwnerName() != null && !theSample.getOwnerName().isEmpty()) {
							ownerBox.setValue(theSample.getOwnerName());
						}
						if (theSample.getFeedstock() != null && !theSample.getFeedstock().isEmpty()) {
							feedstockBox.setValue(theSample.getFeedstock());
						}
						if (theSample != null && theSample.getTreatment() != null && !theSample.getTreatment().isEmpty()) {
							setListBox(theSample.getTreatment(), treatmentBox);
						}
						if (theSample.getFraction() != null && !theSample.getFraction().isEmpty()) {
							for (int i=0;i<fractionBox.getItemCount();i++) {
								String item = fractionBox.getItemText(i);
								if (item.equals(theSample.getFraction())) {
									fractionBox.setSelectedIndex(i);
									changeLabel(fractionBox);
									break;
								}
							}
						}
						if (theSample.getForm() != null && !theSample.getForm().isEmpty()) {
							for (int i=0;i<formBox.getItemCount();i++) {
								String item = formBox.getItemText(i);
								if (item.equals(theSample.getForm())) {
									formBox.setSelectedIndex(i);
									changeLabel(formBox);
									break;
								}
							}
						}
						if (theSample.getComposition() != null && !theSample.getComposition().isEmpty()) {
							for (int i=0;i<compBox.getItemCount();i++) {
								String item = compBox.getItemText(i);
								if (item.equals(theSample.getComposition())) {
									compBox.setSelectedIndex(i);
									changeLabel(compBox);
									break;
								}
							}
						}
						if (theSample.getFeedstock() != null && !theSample.getFeedstock().isEmpty()) {
							labelMaterialBox1.setText(theSample.getFeedstock());
						} else {
							labelMaterialBox1.setText("");
							labelComma1.setText("");
						}
						labelMaterialBox3.setText(constructMaterial(theSample));
						if (theSample.getFire() != null && !theSample.getFire().isEmpty()) {
							setShortListBox(theSample.getFire(), fireBox);
							fireBoxLabel.setText(theSample.getFire());
						}
						if (theSample.getReactivity() != null && !theSample.getReactivity().isEmpty()) {
							setShortListBox(theSample.getReactivity(), reactivityBox);
							reactivityBoxLabel.setText(theSample.getReactivity());
						}
						if (theSample.getSpecific() != null && !theSample.getSpecific().isEmpty()) {
							setShortListBox(theSample.getSpecific(), specificBox);
							specificBoxLabel.setText(theSample.getSpecific());
						}
						if (theSample.getHealth() != null && !theSample.getHealth().isEmpty()) {
							setShortListBox(theSample.getHealth(), healthBox);
							healthBoxLabel.setText(theSample.getHealth());
						}
						if (theSample.getBuilding() != null && !theSample.getBuilding().isEmpty()) {
							buildingBox.setValue(theSample.getBuilding());
						}
						if (theSample.getRoom() != null && !theSample.getRoom().isEmpty()) {
							roomBox.setValue(theSample.getRoom());
						}
						if (theSample.getHolder() != null && !theSample.getHolder().isEmpty()) {
							holderBox.setValue(theSample.getHolder());
						}
						if (theSample.getShelf() != null && !theSample.getShelf().isEmpty()) {
							shelfBox.setValue(theSample.getShelf());
						}
						if (theSample.getSubLocation() != null && !theSample.getSubLocation().isEmpty()) {
							subLocationBox.setValue(theSample.getSubLocation());
						}
						if (theSample.getPackaging() != null && !theSample.getPackaging().isEmpty()) {
							packagingBox.setValue(theSample.getPackaging());
						}
						if (theSample.getStorageNotes() != null && !theSample.getStorageNotes().isEmpty()) {
							storageNotesBox.setValue(theSample.getStorageNotes());
						}
						if (theSample.getTrbNum() != 0) {
							trbNum.setText(String.valueOf(theSample.getTrbNum()));
						}
						if (theSample.getTrbPage() != 0) {
							trbPage.setText(String.valueOf(theSample.getTrbPage()));
						}
						if (theSample.getExternalId() != null && !theSample.getExternalId().isEmpty()) {
							externalBox.setValue(theSample.getExternalId());
						}
						if (theSample.getSampleId() != null && !theSample.getSampleId().isEmpty()) {
							labelNameBox.setText(theSample.getSampleId());
						}
						if (theSample.getCustodianName() != null && !theSample.getCustodianName().isEmpty()) {
							labelCustBox.setText(theSample.getCustodianName());
						}
						if (theSample.getDestination() != null && !theSample.getDestination().isEmpty()) {
							labelDestBox.setText(theSample.getDestination());
						}
						if (theSample.getStrain() != null && !theSample.getStrain().isEmpty()) {
							labelStrainBox.setText(theSample.getStrain());
						}
						//labelMaterialBox3.setText(theSample.getFraction());
						if (theSample.getOwnerName() != null && !theSample.getOwnerName().isEmpty()) {
							labelOwnBox.setText(theSample.getOwnerName());
						}
						if (theSample.getFire() != null && !theSample.getFire().isEmpty()) {
							fireBoxLabel.setText(theSample.getFire());
						}
						if (theSample.getReactivity() != null && !theSample.getReactivity().isEmpty()) {
							reactivityBoxLabel.setText(theSample.getReactivity());
						}
						if (theSample.getSpecific() != null && !theSample.getSpecific().isEmpty()) {
							specificBoxLabel.setText(theSample.getSpecific());
						}
						if (theSample.getHealth() != null && !theSample.getHealth().isEmpty()) {
							healthBoxLabel.setText(theSample.getHealth());
						}
						if (theSample.getTrbNum() != 0) {
							labelNumBox.setText(String.valueOf(theSample.getTrbNum()));
						}
						if (theSample.getTrbPage() != 0) {
							labelPageBox.setText(String.valueOf(theSample.getTrbPage()));
						}
						if (theSample.getStartCreateDate() != null) {
							Date date = theSample.getStartCreateDate();
							String sdate = date.toString();
							if (sdate != null && !sdate.isEmpty()) dateValue.setText(sdate);
							else {
								dateValue.setText("");
								//Window.alert("setting dateValue to empty");
							}
						}
					} else {
						Window.alert("The sample could not be found.\nCheck with administrator.");
					}
				} catch (Exception e) {
			        StackTraceElement ste[] = e.getStackTrace();
			        StringBuilder sb = new StringBuilder();
			        sb.append(e.getMessage());
			        for (int i=0;i<ste.length;i++)
			        {
			        	String s = "problem at "+ste[i].getLineNumber()+" in "+ste[i].getMethodName()+" of "+ste[i].getFileName()+";\n";
			        	sb.append(s);
			        }
					Window.alert(sb.toString());					
				}
			}
		});
	}

	private void loadFire() {
		fireBox.clear();
		fireBox.addItem(BLANK);
		fireBox.addItem(WILL_NOT_BURN);
		fireBox.addItem(ABOVE_200);
		fireBox.addItem(BELOW_200);
		fireBox.addItem(BELOW_100);
		fireBox.addItem(BELOW_73);
		fireBoxLabel.setText(BLANK);
	}

	private void loadReactivity() {
		reactivityBox.clear();
		reactivityBox.addItem(BLANK);
		reactivityBox.addItem(STABLE);
		reactivityBox.addItem(UNSTABLE_IF_HEATED);
		reactivityBox.addItem(VIOLENT_CHEMICAL_REATION);
		reactivityBox.addItem(SHOCK_OR_HEAT);
		reactivityBox.addItem(MAY_DETONATE);
		reactivityBoxLabel.setText(BLANK);
	}

	private void loadSpecific() {
		specificBox.clear();
		specificBox.addItem(BLANK);
		specificBox.addItem(WATER_REACTIVITY);
		specificBox.addItem(OXIDIZER);
		specificBox.addItem(ACID);
		specificBox.addItem(CORROSIVE);
		specificBoxLabel.setText(BLANK);
	}

	private void loadHealth() {
		healthBox.clear();
		healthBox.addItem(BLANK);
		healthBox.addItem(NO_HEALTH_THREAT);
		healthBox.addItem(SLIGHTLY_HAZARDOUS);
		healthBox.addItem(HAZARDOUS);
		healthBox.addItem(EXTREME_DANGER);
		healthBox.addItem(DEADLY);
		healthBoxLabel.setText(BLANK);
	}

	private void loadOrigins() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				String sessionId = result;
				tsa.getOrigins(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						Window.alert("Origins failed to load. Error: " + caught.getMessage());
					}
		
					public void onSuccess(Collection<String> result) {
						originListBox.clear();
						originListBox.addItem("");
						if (result != null) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String item = it.next();
								if (item != null && !item.isEmpty())
									originListBox.addItem(item);
							}
						}
						if (theSample != null)
							setListBox(theSample.getOrigin(), originListBox);
					}			
				});
			}
		});
	}

	/**
	 * Private method to find a sample by its ID and load it into
	 *  the dialog screen.
	 * 
	 * @param sampleId2 <long> Sample ID
	 */
	private void loadAttachments(long sampleId2) {
		tsa.findAttachmentsInfoByTrackingId(sampleId2, new AsyncCallback<Collection<FileInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("something strange happened retrieving attachments");
			}

			@Override
			public void onSuccess(Collection<FileInfo> result) {
				if (result != null && !result.isEmpty()) 
					attachmentList.addAll(result);
			}
		});
	}

	/**
	 * Private <ClickHandler> to handle adding attachments to the sample
	 * 
	 * @author jalbersh
	 *
	 */
	private class DownloadAttachmentClickHandler implements ClickHandler {

		private static final String ATTACHMENT_ID_NAME = "attachmentId";

		@Override
		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row = 1; row < attachmentGrid.getRowCount() && index == -1; row++) {
				CheckBox cb = (CheckBox)attachmentGrid.getWidget(row, ATTACHMENT_SELECT_COL);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			
			if (index == -1) 
				Window.alert("Please select a file to remove.");
			else {
				// Download the file from the server list
				long attachmentId = 0;
				Iterator<FileInfo> it = attachmentList.iterator();
				while (it.hasNext()) {
					FileInfo fi = it.next();
					String attachmentIdString = fi.getAttachment_id();
					String gridId = ((Label)attachmentGrid.getWidget(index, ATTACHMENT_ID_COL)).getText();
					if (attachmentIdString.equalsIgnoreCase(gridId)) {
						try {
							attachmentId = Long.parseLong(fi.getAttachment_id());
						} catch (NumberFormatException e) {
							attachmentId = -1;
						}
						if (attachmentId != -1) {
							fileDownloadFormPanel.clear();
							Hidden hiddenId = new Hidden(ATTACHMENT_ID_NAME, String.valueOf(attachmentId));							
							VerticalPanel vp = new VerticalPanel();
							vp.add(hiddenId);
							fileDownloadFormPanel.add(vp);
							fileDownloadFormPanel.submit();
						}
					}
				}
				// Download the file from the pending list
				attachmentId = 0;
				Iterator<FileInfo> it2 = attachmentPendingList.iterator();
				while (it2.hasNext()) {
					FileInfo fi = it2.next();
					String attachmentIdString = fi.getAttachment_id();
					String gridId = ((Label)attachmentGrid.getWidget(index, ATTACHMENT_ID_COL)).getText();
					if (attachmentIdString.equalsIgnoreCase(gridId)) {
						try {
							attachmentId = Long.parseLong(fi.getAttachment_id());
						} catch (NumberFormatException e) {
							attachmentId = -1;
						}
						if (attachmentId != -1) {
							fileDownloadFormPanel.clear();
							Hidden hiddenId = new Hidden(ATTACHMENT_ID_NAME, String.valueOf(attachmentId));							
							VerticalPanel vp = new VerticalPanel();
							vp.add(hiddenId);
							fileDownloadFormPanel.add(vp);
							fileDownloadFormPanel.submit();
						}
					}
				}
			}		
		}
	}

	/**
	 * Private <ClickHandler> to handle removing samples from the request.
	 * 
	 * @author jalbersh
	 *
	 */
	private class RemoveAttachmentClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			int index = -1;
			String attachmentIDString = "";
			for (int row=1;row<attachmentGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)attachmentGrid.getWidget(row, ATTACHMENT_SELECT_COL);
				if (cb != null) {
					if (cb.getValue().booleanValue()) { 
						index = row;
						Label idLabel = (Label)attachmentGrid.getWidget(row, ATTACHMENT_ID_COL);
						Label fileNameLabel = (Label)attachmentGrid.getWidget(row, ATTACHMENT_NAME_COL);
						if (fileNameLabel != null) {
							attachmentIDString = idLabel.getText();
						}
					}
				}
			}
			if (index == -1) 
				Window.alert("Please select a sample to remove.");
			else {
				boolean removed = false;
				ListIterator<FileInfo> it = attachmentList.listIterator();
				while (it.hasNext()) {
					FileInfo fileInfo = it.next();
					if (fileInfo.getAttachment_id().equalsIgnoreCase(attachmentIDString)) {
						removed = attachmentList.remove(fileInfo);
						attachmentDeleteList.add(fileInfo);
						break;
					}
				}
				if (!removed) {
					ListIterator<FileInfo> it2 = attachmentPendingList.listIterator();
					while (it2.hasNext()) {
						FileInfo fileInfo = it2.next();
						if (fileInfo.getAttachment_id().equalsIgnoreCase(attachmentIDString)) {
							removed = attachmentPendingList.remove(fileInfo);
							attachmentDeleteList.add(fileInfo);
							break;
						}
					}
				}
				
				attachmentGrid.removeRow(index);
				removeButton.setEnabled(false);
				downloadButton.setEnabled(false);
			}
			attachmentGrid.setVisible(attachmentGrid.getRowCount()>1);
		}		
	}

	/**
	 * Private <ClickHandler> to handle the enabling of the Remove button
	 *  and the visibility of the sample list.
	 * 
	 * @author jalbersh
	 *
	 */
	private class ItemSelectClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row=1;row<attachmentGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)attachmentGrid.getWidget(row, ATTACHMENT_SELECT_COL);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			
			removeButton.setEnabled(index != -1);
			downloadButton.setEnabled(index != -1);
			attachmentGrid.setVisible(attachmentGrid.getRowCount() > 1);
		}		
	}

	/**
	 * Private <ClickHandler> to handle adding attachments to the sample
	 * 
	 * @author jalbersh
	 *
	 */
	private class AddAttachmentClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			fileName = attachFile.getFilename();
			if (fileName != null && !fileName.isEmpty()) {
				String separator = "\\";//File.separator;
				String path = "";
				String name = "";
				if (fileName.indexOf(separator) != (-1)) {
					name = fileName.substring(fileName.lastIndexOf(separator) + 1);
					path = fileName.substring(0,fileName.lastIndexOf(separator));
				} else {
					name = fileName;
					path = fileName;
				}
				
				// Add to pending list
				FileInfo info = new FileInfo();
				info.setName(name);
				info.setPath(path);
				info.setFilename(fileName);				
				attachmentPendingList.add(info);
				
				// Submit file to server
				attachFile.setName(name);
				Hidden w1 = new Hidden("name", name);
				Hidden w2 = new Hidden("path", path);
				Hidden w3 = new Hidden("file", fileName);
				sampleAttachmentsPanel.add(w1);
				sampleAttachmentsPanel.add(w2);
				sampleAttachmentsPanel.add(w3);
				attachmentFormPanel.submit();
				
				// Update GUI
				/*int index = -1;
				if (attachmentGrid.getRowCount() > 0) {
					for (int row=1;row<attachmentGrid.getRowCount() && index == -1;row++) {
						CheckBox cb = (CheckBox)attachmentGrid.getWidget(row, ATTACHMENT_SELECT_COL);
						if (cb.getValue().booleanValue()) 
							index = row;
					}
				}
				removeButton.setEnabled(index != -1);
				downloadButton.setEnabled(index != -1);*/

				int row = attachmentGrid.getRowCount();
				
				if (row == 0) { // Add header row
					attachmentGrid.resizeRows(row + 1);
					
					attachmentGrid.setWidget(row, ATTACHMENT_SELECT_COL, new Label(SELECT_ROW));
					attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_SELECT_COL, false);
					attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);
					
					attachmentGrid.setWidget(row, ATTACHMENT_ID_COL, new Label(ATTACHMENT_ID));
					attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_ID_COL, false);
					attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_ID_COL, HasHorizontalAlignment.ALIGN_CENTER);
					
					attachmentGrid.setWidget(row, ATTACHMENT_NAME_COL, new Label(ATTACHMENT));
					attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_NAME_COL, false);
					attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_NAME_COL, HasHorizontalAlignment.ALIGN_CENTER);
					
					row++;
				}

				attachmentGrid.resizeRows(row + 1);
				
				attachmentGrid.setWidget(row, ATTACHMENT_SELECT_COL, new CheckBox());
				attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_SELECT_COL, false);
				attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);					
				
				attachmentGrid.setWidget(row, ATTACHMENT_ID_COL, new Label((info.getAttachment_id() != null) ? info.getAttachment_id() : BLANKS));
				attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_ID_COL, false);
				attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_ID_COL, HasHorizontalAlignment.ALIGN_CENTER);
				
				attachmentGrid.setWidget(row, ATTACHMENT_NAME_COL, new Label((info.getName() != null) ? info.getName() : BLANKS));
				attachmentGrid.getCellFormatter().setWordWrap(row, ATTACHMENT_NAME_COL, false);
				attachmentGrid.getCellFormatter().setHorizontalAlignment(row, ATTACHMENT_NAME_COL, HasHorizontalAlignment.ALIGN_CENTER);
			}
			else {
				Window.alert("Please select a file to add");
			}
			attachmentGrid.setVisible(attachmentGrid.getRowCount() > 1);
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
	 * Getter for theSample
	 * 
	 * @return <SampleCritera>
	 */
	public SampleCriteria getTheSample() {
		return theSample;
	}

	/**
	 * Setter for theSample
	 * 
	 * @param theSample <SampleCriteria>
	 */
	public void setTheSample(SampleCriteria theSample) {
		this.theSample = theSample;
	}

	/**
	 * Getter for sampleIdString
	 * 
	 * @return <String>
	 */
	public String getSampleIdString() {
		return sampleIdString;
	}

	/**
	 * Setter for sampleIdString
	 * 
	 * @param sampleIdString
	 */
	public void setSampleIdString(String sampleIdString) {
		this.sampleIdString = sampleIdString;
	}

	/**
	 * Handle the <ClickEvent> and hide the dialog
	 */
	public void onClick(ClickEvent event) {
		this.hide();
	}

	/**
	 * Getter for sampleId
	 * 
	 * @return long
	 */
	public long getSampleId() {
		return sampleId;
	}

	/**
	 * Setter for sampleId
	 * 
	 * @param sampleId long
	 */
	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}
}
