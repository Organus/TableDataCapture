package gov.nrel.nbc.tracker.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
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
 * Composite panel to allow users to create a <Sample> in the system
 *  and save it to the database.
 * 
 * @author jalbersh
 *
 */
public class CreateSample extends Composite implements AppConstants {

//    private Timer sessionTimeoutResponseTimer;

	private static final String ATTACHMENT_ID = "ID";
	private static final int ATTACHMENT_ID_COL = 1;
	private static final int ATTACHMENT_NAME_COL = 2;
	private static final int ATTACHMENT_SELECT_COL = 0;
	private static final String DEFAULT_0 = "0";
	private static final String DESCRIPTOR_WIDTH = "430px";
	private static final String DESCRIPTOR_HEIGHT = "70px";
	private static final String DESCRIPTOR_HEIGHT_SABC = "46px";
	private static final String LABELPANEL_WIDTH = "440px";
	private static final String SPACE = " ";
	private static final String TRB_LABEL = "Trb:";
	private static final String COMMA = ",";
	private static final String BLANK = "";
	private static final String BLANK40 = "                                            ";
	private static final String BLANK25 = "                             ";
	private static final String LABEL_CONTENTS = "Label Contents";
	private static final String ENTRY_DATE = "Sample Create Date:";
	private static final String MATERIAL = "Material:";
	private static final String STRAIN = "Strain:";
	private static final String BIOMASS_LOT_LABEL = "Biomass Lot:";
	private static final String LABEL_LABEL = "(Bold fields are on printed label as shown on right)";
	private static final int MAX_OWNER_LENGTH = 25;
	private static final String FILE = "File";
	private static final String HORIZONTAL_RULE = "<HR>";
	private static final String PRINT_BUTTON_TEXT = "Print Label";
	private static final String EMAIL_BUTTON_TEXT = "Email Label";
	private static final String CLEAR_BUTTON_TEXT = "Clear";
	private static final String SUBMIT_BUTTON_TEXT = "Submit";
	private static final String PAGE_LABEL = "TRB Page:";
	private static final String NUMBER_LABEL = "TRB Number:";
	private static final String COMMENTS_LABEL = "Comments:";
	private static final String DESCRIPTOR_LABEL = "Label Descriptor:";
	private static final String STORAGE_NOTES_LABEL = "Storage Notes:";
	private static final String PACKAGING_LABEL = "Packaging:";
	private static final String HOLDER_LABEL = "Holder:";
	private static final String SHELF_LABEL = "Shelf:";
	private static final String SUBLOCATION_LABEL = "Sublocation:";
	private static final String ROOM_LABEL = "Room:";
	private static final String BUILDING_LABEL = "Building:";
	private static final String STATUS_LABEL = "Status:";
	private static final String FRACTION_LABEL = "Fraction:";
	private static final String FORM_LABEL = "Storage State:";
	private static final String FEEDSTOCK_LABEL = "Feedstock:";
	private static final String TREATMENT_LABEL = "Treatment:";
	private static final String DESTINATION_LABEL_ALGAE = "Project Task No:";
	private static final String DESTINATION_LABEL_SABC = "Destination:";
	private static final String STRAIN_LABEL = "Strain:";
	private static final String CUSTODIAN_NAME_LABEL = "Custodian Name:";
	private static final String COMPOSITION_LABEL = "Composition:"; 
	private static final String ORIGIN_LABEL = "Origin:";
	private static final String OWNER_NAME_LABEL = "Owner Name:";
	private static final String EXTERNAL_ID_LABEL = "External ID:";
	private static final String APPROXIMATE_AMOUNT_LABEL = "Approximate Amount:";
	private static final String SAMPLE_ID_LABEL = "Sample ID:";
	private static final String SAMPLE_CREATION = "Sample Entry";
	private static final String TITLE_HTML_PREFIX = "<center><h3>";
	private static final String TITLE_HTML_SUFFIX = "</h3></center>";
	private static final String TITLE_HTML = TITLE_HTML_PREFIX + "Tracking ID - {TBD}"
				+ TITLE_HTML_SUFFIX;
	private static final String URL_TRACKER_SERVICE = "trackerService";
	private final String URL_TRACKER_CLIENT_UPLOAD_SERVICE = "clientUploadService";
	private final String URL_ATTACHMENT_UPLOAD_SERVICE = "attachmentUploadService";
	private static final String SUBTITLE_STYLE = "SubTitle";
	private static final String BOLDLABEL_STYLE = "BoldLabel";
	private static final String BOLDFANCYLABEL_STYLE = "BoldFancyLabel";
	private static final String FANCYLABEL_STYLE = "FancyLabel";
	private static final String FANCYDESCLABEL_STYLE = "FancyDescLabel";
	private static final String FANCYPANEL_STYLE = "FancyPanel";
	private static final String PUNYLABEL_STYLE = "punyLabel";
	private static final String REMOVE_SAMPLE = "Remove";
	private static final String DOWNLOAD_SAMPLE = "Download";
	private static final String ADD_SAMPLE = "Add";
	private static final int START_POSITION = 40;
	private static final int MAX_PER_LINE = 51;
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

	private static final String SELECT_ROW = "Select";

	private static final String SECOND_COLUMN_WIDTH = "475px";

	//private static final String SAMPLE_HEIGHT = "270px";

	private List<FileInfo> attachmentList = new ArrayList<FileInfo> ();

	private HorizontalPanel sampleAttachmentsPanel = new HorizontalPanel();

	private VerticalPanel attachmentsTable = new VerticalPanel();

	private DecoratorPanel attachmentsDPanel = new DecoratorPanel();

	private FormPanel attachmentFormPanel = new FormPanel();
	/**
	 * A path to the tag file.
	 */
	protected String uploadedTagFile;

	/**
	 * Holder for TrackerService
	 */
	private TrackerServiceAsync tsa = null;

	private final DateBox entryDate = new DateBox();
	/**
	 * Units <ListBox>
	 */
	private final ListBox unitBox = new ListBox();
	
	/**
	 * Status <ListBox>
	 */
	private final ListBox statusBox = new ListBox();
	
	/**
	 * Oracle for feedstock <SuggestBox>
	 */
	private final MultiWordSuggestOracle feedstockOracle = new MultiWordSuggestOracle();
	
	/**
	 * Feedstock <SuggestBox>
	 */
	private final FilteredSuggestBox feedstockBox = new FilteredSuggestBox(feedstockOracle);
	private final Label labelMaterialBox1 = new Label(BLANK);
	private final Label labelComma1 = new Label(COMMA);
	private final Label labelMaterialBox2 = new Label(BLANK);
	private final Label labelComma2 = new Label(COMMA);
	private final Label labelMaterialBox3 = new Label(BLANK);
	private final Label labelStrainBox = new Label(BLANK);
	
	VerticalPanel labelDescriptorBox = new VerticalPanel();
	//FlexTable vimagePanel = new FlexTable();
	//Image image = new Image(DIAMOND_JPG);
	/**
	 * Fraction <ListBox>
	 */
	private final ListBox fractionBox = new ListBox();
	
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
	private final ListBox treatmentBox = new ListBox();
	/**
	 * form <ListBox>
	 */
	private final ListBox formBox = new ListBox();
	
	/**
	 * form <ListBox>
	 */
	private final ListBox compBox = new ListBox();

	/**
	 * Panel to pass data to server <FormPanel>
	 */
	private final FormPanel formPanel = new FormPanel();

	private final FormPanel hiddenFormPanel = new FormPanel();

	private final FileUpload attachFile = new FileUpload();
	
	private final Button removeButton = new Button(REMOVE_SAMPLE);

	private final Button addButton = new Button(ADD_SAMPLE);

	private final Button downloadButton = new Button(DOWNLOAD_SAMPLE);

	/**
	 * Feedstock <Hidden>
	 */
	private final Hidden feedstockHidden = new Hidden();
	
	/**
	 * Panel to hold everything.
	 */
	private final VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * Sample ID
	 */
	private String sampleId = "";
	
	/**
	 * attachment ID
	 */
	private String attachmentId = "";
	
	/**
	 * filename
	 */
	private String fileName = "";
	
	
	/**
	 * Sample ID Label template
	 */
	private HTML sampleIDLabel = new HTML(TITLE_HTML, false);
	
	/**
	 * Oracle for Customer Name <SuggestBox>
	 */
	private final MultiWordSuggestOracle sampleOracle = new MultiWordSuggestOracle();
	/**
	 * Sample name <TextBox>
	 */	
	private final FilteredSuggestBox sampleIDBox = new FilteredSuggestBox(sampleOracle);
	private final Label labelNameBox = new Label(BLANK40);
	
	/**
	 * Comment <TextBox>
	 */
	private final FilteredAlphaNumericTextBox commentBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * Sample amount <TextBox>
	 */
	private final FilteredNumberTextBox amountBox = new FilteredNumberTextBox();
	
	/**
	 * External ID <TextBox>
	 */
	private final FilteredAlphaNumericTextBox externalIdBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * Oracle for Customer Name <SuggestBox>
	 */
	private final MultiWordSuggestOracle biomassOracle = new MultiWordSuggestOracle();
	
	/**
	 * Employee Name <SuggestBox>
	 */
	private final FilteredSuggestBox biomassBox = new FilteredSuggestBox(biomassOracle);
	
	/**
	 * Oracle for Customer Name <SuggestBox>
	 */
	private final MultiWordSuggestOracle custNameOracle = new MultiWordSuggestOracle();
	
	/**
	 * Employee Name <SuggestBox>
	 */
	private final FilteredSuggestBox custNameBox = new FilteredSuggestBox(custNameOracle);
	
	/**
	 * Oracle for Owner Name <SuggestBox>
	 */
	private final MultiWordSuggestOracle ownNameOracle = new MultiWordSuggestOracle();
	
	private ListBox originListBox = new ListBox();
	
	private final MultiWordSuggestOracle destOracle = new MultiWordSuggestOracle();
	private FilteredSuggestBox destinationListBox = new FilteredSuggestBox(destOracle);
	
	//private ListBox strainsListBox = new ListBox();
	
	//private ListBox custodiansListBox = new ListBox();
	
	private final Label labelDestBox = new Label(BLANK25);
	
	/**
	 * Owner name <SuggestBox> 
	 */
	private final FilteredSuggestBox ownNameBox = new FilteredSuggestBox(ownNameOracle);
	private final Label labelOwnBox = new Label(BLANK25);
	private final Label labelCustBox = new Label(BLANK25);
	private final Label dateLabelLabel = new Label(ENTRY_DATE); 
	/**
	 * Oracle for building <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle buildingOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location building <SuggestBox>
	 */
	private final FilteredSuggestBox buildingBox = new FilteredSuggestBox(buildingOracle);
	
	/**
	 * Oracle for room <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle roomOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location room <SuggestBox>
	 */
	private final FilteredSuggestBox roomBox = new FilteredSuggestBox(roomOracle);
	
	/**
	 * Oracle for sub-location <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle sublocationOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location sub-location <SuggestBox>
	 */
	private final FilteredSuggestBox sublocationBox = new FilteredSuggestBox(sublocationOracle);
	
	/**
	 * Oracle for building <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle shelfOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location shelf <SuggestBox>
	 */
	private final FilteredSuggestBox shelfBox = new FilteredSuggestBox(shelfOracle);
	
	/**
	 * Oracle for holder <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle holderOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location case <SuggestBox>
	 */
	private final FilteredSuggestBox holderBox = new FilteredSuggestBox(holderOracle);
	
	/**
	 * Oracle for packaging <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle packagingOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location packaging <SuggestBox>
	 */
	private final FilteredSuggestBox packagingBox = new FilteredSuggestBox(packagingOracle);
	
	/**
	 * Oracle for packaging <MultiWordSuggestOracle>
	 */
	//private final MultiWordSuggestOracle destinationOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location packaging <SuggestBox>
	 */
	//private final FilteredSuggestBox destinationBox = new FilteredSuggestBox(destinationOracle);
	/**
	 * Oracle for packaging <MultiWordSuggestOracle>
	 */
	private final MultiWordSuggestOracle strainOracle = new MultiWordSuggestOracle();
	
	/**
	 * Location packaging <SuggestBox>
	 */
	private final FilteredSuggestBox strainBox = new FilteredSuggestBox(strainOracle);
	/**
	 * Storage notes <TextBox>
	 */
	private final FilteredAlphaNumericTextBox descriptorBox = new FilteredAlphaNumericTextBox();
	private final Label descriptorLabel1 = new Label(SPACE);
	private final Label descriptorLabel2 = new Label(SPACE);
	private final Label descriptorLabel3 = new Label(SPACE);

	/**
	 * Storage notes <TextBox>
	 */
	private final FilteredAlphaNumericTextBox storageNotesBox = new FilteredAlphaNumericTextBox();
	
	/**
	 * TRB number <TextBox>
	 */
	private final FilteredNumberTextBox numBox = new FilteredNumberTextBox();
	
	private final ListBox groupBox = new ListBox();
	/**
	 * TRB page number <TextBox>
	 */
	private final FilteredNumberTextBox pageBox = new FilteredNumberTextBox();
	private final Label labelPageBox = new Label(BLANK25);
	private final Label labelNumBox = new Label(BLANK25);
	
	/**
	 * Owner name <Hidden>.
	 */
	private Hidden ownNameHidden = new Hidden();
	
	/**
	 * Custodian name <Hidden>.
	 */
	private Hidden custNameHidden = new Hidden();
	
	/**
	 * Composition name <Hidden>.
	 */
	private Hidden compositionHidden = new Hidden();

	private Hidden originHidden = new Hidden();
	
	/**
	 * Building <Hidden>.
	 */
	private Hidden buildingHidden = new Hidden();
	
	/**
	 * Room <Hidden>.
	 */
	private Hidden roomHidden = new Hidden();
	
	/**
	 * Sublocation <Hidden>.
	 */
	private Hidden sublocationHidden = new Hidden();
	
	/**
	 * Shelf <Hidden>.
	 */
	private Hidden shelfHidden = new Hidden();
	
	/**
	 * Holder <Hidden>.
	 */
	private Hidden holderHidden = new Hidden();
	
	/**
	 * Packaging <Hidden>.
	 */
	private Hidden packagingHidden = new Hidden();
	
	/**
	 * Button to print labels.
	 */
	private Button emailButton = new Button(EMAIL_BUTTON_TEXT);
	/**
	 * Button to print labels.
	 */
	private Button printButton = new Button(PRINT_BUTTON_TEXT);
	
	private final ListBox printerBox = new ListBox();
	private final Grid sampleGrid = new Grid(0,3);
	
	private boolean saved = false;

	final HorizontalPanel printerPanel = new HorizontalPanel();
	
	private String sessionId = "";
	/**
	 * Constructor.
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public CreateSample() {
		
		initWidget(mainPanel);
		
		initServices();
		
		// The hidden form
		
		String hiddenUrlTag = GWT.getModuleBaseURL() + URL_ATTACHMENT_UPLOAD_SERVICE;
		hiddenFormPanel.setAction(hiddenUrlTag);
		hiddenFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		hiddenFormPanel.setMethod(FormPanel.METHOD_POST);

		// The form
		String urlTag = GWT.getModuleBaseURL() + URL_TRACKER_CLIENT_UPLOAD_SERVICE;
		formPanel.setAction(urlTag);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				sampleId = event.getResults();
				//Code work-around for FireFox anomaly; 
				cleanUp();
				String labelString = TITLE_HTML;
				labelString = labelString.replace("{TBD}", sampleId);
				sampleIDLabel.setHTML(labelString);
				printButton.setVisible(true);
				emailButton.setVisible(true);
			}			
			/**
			 * Private method to remove the HTML tags from around sampleId.
			 */
			private void cleanUp() {
				if (sampleId.toLowerCase().startsWith("<")) {
					int index = sampleId.indexOf(">");
					sampleId = sampleId.substring(index + 1, sampleId.length() - 1);
					index = sampleId.indexOf("<");
					sampleId = sampleId.substring(0, index);
				}
			}			
		});
		HorizontalPanel hpanel = new HorizontalPanel();
		
		final FlexTable samplePanel = new FlexTable();
		
		samplePanel.setCellPadding(1);
		
		hpanel.add(samplePanel);
		hpanel.setCellHorizontalAlignment(samplePanel, HasHorizontalAlignment.ALIGN_LEFT);
		hpanel.setCellVerticalAlignment(samplePanel, HasVerticalAlignment.ALIGN_MIDDLE);
				
		formPanel.add(hpanel);
		
		final FlexTable labelPanel = new FlexTable();
		labelPanel.setCellPadding(1);
		labelPanel.setWidth(LABELPANEL_WIDTH);
		
		FlexTable vrpanel = new FlexTable();
		
		vrpanel.setWidget(60,0,labelPanel);
		hpanel.add(vrpanel);
		hpanel.setCellHorizontalAlignment(vrpanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hpanel.setCellVerticalAlignment(vrpanel, HasVerticalAlignment.ALIGN_TOP);

		int row = 0;
		
		Label titleLabel = new Label(SAMPLE_CREATION);
		titleLabel.addStyleDependentName(SUBTITLE_STYLE);
		titleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		samplePanel.setWidget(row, 0, titleLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		samplePanel.setWidget(row, 0, sampleIDLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		samplePanel.getCellFormatter().setHorizontalAlignment(row++, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		Label labelLabel = new Label(LABEL_LABEL);
		labelLabel.addStyleDependentName(PUNYLABEL_STYLE);
		labelLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		samplePanel.setWidget(row, 0, labelLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		samplePanel.getCellFormatter().setHorizontalAlignment(row++, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		HTML oneHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, oneHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		 
		/* TODO - add when security and groups added
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

		Label nameLabel = new Label(SAMPLE_ID_LABEL);
		nameLabel.addStyleDependentName(BOLDLABEL_STYLE);
		samplePanel.setWidget(row, 0, nameLabel);
		
		final HorizontalPanel namePanel = new HorizontalPanel();
		Label sampleLabel = new Label(SAMPLE_ID_LABEL);
		sampleLabel.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(sampleLabel);

		//sampleIDBox.setName(SAMPLE_NAME);
		//sampleIDBox.setMaxLength(MAX_SAMPLE_ID_LENGTH);
		sampleIDBox.setTitle(String.valueOf(SAMPLEID_FLD));
		samplePanel.setWidget(row++, 1, sampleIDBox);
		sampleIDBox.addValueChangeHandler(new MyValueChangeHandler());
		sampleIDBox.addSelectionHandler(new MySelectionHandler());
		labelNameBox.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(labelNameBox);
		loadSampleIds();

		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label numLabel = new Label(NUMBER_LABEL);
			numLabel.addStyleDependentName(BOLDLABEL_STYLE);
			samplePanel.setWidget(row, 0, numLabel);
	
			numBox.setName(SAMPLE_TRB_NUM);
			numBox.setTitle(String.valueOf(TRBNUMBER_FLD));
			samplePanel.setWidget(row++, 1, numBox);
			numBox.addChangeHandler(new MyChangeHandler());
	
			Label pageLabel = new Label(PAGE_LABEL);
			pageLabel.addStyleDependentName(BOLDLABEL_STYLE);
			samplePanel.setWidget(row, 0, pageLabel);
	
			pageBox.setName(SAMPLE_TRB_PAGE);
			pageBox.setTitle(String.valueOf(TRBPAGE_FLD));
			samplePanel.setWidget(row++, 1, pageBox);
			pageBox.addChangeHandler(new MyChangeHandler());
		//}
		
		final HorizontalPanel labelOwnPanel = new HorizontalPanel();
		final HorizontalPanel labelCustPanel = new HorizontalPanel();
		final HorizontalPanel labelDestPanel = new HorizontalPanel();
		final HorizontalPanel trbPanel = new HorizontalPanel();
		final int subrow1=row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
				Cookies.setCookie("SessionID", sessionId);
			}
		});
	    //final long DURATION = 1000 * 60 * 3;//60 * 24 * 14; //duration remembering login. 2 weeks in this example.
	    //Date expires = new Date(System.currentTimeMillis() + DURATION);
	    //Cookies.setCookie("sid", sessionId, expires, null, "/tracker", false);		
						
		Label trbLabel = new Label(TRB_LABEL);
		trbLabel.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(trbLabel);
		labelNumBox.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(labelNumBox);
		trbPanel.add(new Label("-"));
		labelPageBox.addStyleDependentName(FANCYLABEL_STYLE);
		trbPanel.add(labelPageBox);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label ownNameLabel = new Label(OWNER_NAME_LABEL);
			samplePanel.setWidget(subrow1+2, 0, ownNameLabel);
			ownNameLabel.addStyleDependentName(BOLDLABEL_STYLE);
			loadOwnName();
			ownNameHidden.setName(SAMPLE_OWN_NAME);
			ownNameBox.setTitle(String.valueOf(OWNER_FLD));
			((TextBox)(ownNameBox.getTextBox())).setMaxLength(MAX_OWNER_LENGTH);
			samplePanel.setWidget(subrow1+2, 1, ownNameBox);
			ownNameBox.addKeyDownHandler(new MyValueChangeHandler());
			ownNameBox.addSelectionHandler(new MySelectionHandler());
			Label ownLabel = new Label(OWNER_NAME_LABEL);
			ownLabel.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(ownLabel);
			labelOwnPanel.setStyleName(FANCYPANEL_STYLE);
			labelOwnBox.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(labelOwnBox);
		} else {
			Label destLabel = new Label();
			destLabel.setText(DESTINATION_LABEL_ALGAE);				
			destLabel.addStyleDependentName(BOLDLABEL_STYLE);
			samplePanel.setWidget(subrow1, 0, destLabel);
			//destinationBox.setName(SAMPLE_TRB_NUM);
			//destinationBox.setTitle(String.valueOf(TRBNUMBER_FLD));
			destinationListBox.addKeyDownHandler(new MyValueChangeHandler());
			destinationListBox.addSelectionHandler(new MySelectionHandler());
			//destinationListBox.addChangeHandler(new MyChangeHandler());
			samplePanel.setWidget(subrow1, 1, destinationListBox);
			loadDestinations();
			Label strainLabel = new Label(STRAIN_LABEL);
			strainLabel.addStyleDependentName(BOLDLABEL_STYLE);
			samplePanel.setWidget(subrow1+1, 0, strainLabel);
			//strainBox.setName(SAMPLE_TRB_PAGE);
			//strainBox.setTitle(String.valueOf(TRBPAGE_FLD));
			samplePanel.setWidget(subrow1+1, 1, strainBox);
			strainBox.addSelectionHandler(new MySelectionHandler());
			strainBox.addValueChangeHandler(new MyValueChangeHandler());
			loadStrains();
			//Label trbLabel = new Label(TRB_LABEL);
			//trbLabel.addStyleDependentName(FANCYLABEL_STYLE);
			//trbPanel.add(trbLabel);
			//labelNumBox.addStyleDependentName(FANCYLABEL_STYLE);
			//trbPanel.add(labelNumBox);
			//trbPanel.add(new Label("-"));
			//labelPageBox.addStyleDependentName(FANCYLABEL_STYLE);
			//trbPanel.add(labelPageBox);
			//Label ownNameLabel = new Label(OWNER_NAME_LABEL);
			//samplePanel.setWidget(subrow1+2, 0, ownNameLabel);
			//ownNameLabel.addStyleDependentName(BOLDLABEL_STYLE);
			//loadOwnName();
			//ownNameHidden.setName(SAMPLE_OWN_NAME);
			//ownNameBox.setTitle(String.valueOf(OWNER_FLD));
			//((TextBox)(ownNameBox.getTextBox())).setMaxLength(MAX_OWNER_LENGTH);
			//samplePanel.setWidget(subrow1+2, 1, ownNameBox);
			//ownNameBox.addKeyDownHandler(new MyValueChangeHandler());
			//ownNameBox.addSelectionHandler(new MySelectionHandler());
			Label custLabel = new Label(CUSTODIAN_NAME_LABEL);
			custLabel.addStyleDependentName(FANCYLABEL_STYLE);
			labelCustPanel.add(custLabel);
			labelCustPanel.setStyleName(FANCYPANEL_STYLE);
			labelCustBox.addStyleDependentName(FANCYLABEL_STYLE);
			labelCustPanel.add(labelCustBox);
//			Label destLabel1 = new Label(DESTINATION_LABEL);
//			destLabel1.addStyleDependentName(FANCYLABEL_STYLE);
			labelDestPanel.setStyleName(FANCYPANEL_STYLE);
			labelDestPanel.addStyleDependentName(FANCYLABEL_STYLE);
			labelDestBox.addStyleDependentName(BOLDLABEL_STYLE);
			labelDestPanel.add(labelDestBox);							
		}
		
		
		row+=3;
		Label descriptorLabel = new Label(DESCRIPTOR_LABEL);
		descriptorLabel.addStyleDependentName(BOLDLABEL_STYLE);
		samplePanel.setWidget(row, 0, descriptorLabel);

		descriptorBox.setName(SAMPLE_DESCRIPTOR);
		descriptorBox.setTitle(String.valueOf(LABELDESC_FLD));
		samplePanel.setWidget(row++, 1, descriptorBox);
		descriptorBox.addChangeHandler(new MyChangeHandler());

		final int subrow=row;
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label externalIdLabel = new Label(EXTERNAL_ID_LABEL);
			samplePanel.setWidget(subrow, 0, externalIdLabel);

			externalIdBox.setName(SAMPLE_EXTERNAL_ID);
			externalIdBox.setTitle(String.valueOf(EXTERNALID_FLD));
			samplePanel.setWidget(subrow, 1, externalIdBox);

			loadCustName();
		}  else {
			loadCustodians();
		}
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-7");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
			}
		});
		row++;
		
		Label custNameLabel = new Label(CUSTODIAN_NAME_LABEL);
		samplePanel.setWidget(row, 0, custNameLabel);

		custNameHidden.setName(SAMPLE_CUSTODIAN_NAME);
		custNameBox.setTitle(String.valueOf(CUSTODIAN_FLD));
		((TextBox)(custNameBox.getTextBox())).setMaxLength(MAX_OWNER_LENGTH);
		samplePanel.setWidget(row++, 1, custNameBox);
		custNameBox.addValueChangeHandler(new ValueChangeHandler() {
			@Override
			public void onValueChange(ValueChangeEvent event) {
				changeLabel(custNameBox);
			}
		});

		// Sample Origin
		Label originLabel = new Label(ORIGIN_LABEL);
		originHidden.setName(SAMPLE_ORIGIN);
		samplePanel.setWidget(row, 0, originLabel);
		loadOrigins();
		originListBox.setTitle(String.valueOf(ORIGIN_FLD));
		samplePanel.setWidget(row++, 1, originListBox);
		
		Label statusLabel = new Label(STATUS_LABEL);
		samplePanel.setWidget(row, 0, statusLabel);

		loadStatuses();
		statusBox.setName(SAMPLE_STATUS);
		statusBox.setTitle(String.valueOf(STATUS_FLD));
		samplePanel.setWidget(row++, 1, statusBox);

		HTML twoHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, twoHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);

		final int subrow2=row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
						if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							Label feedstockLabel = new Label(FEEDSTOCK_LABEL);
							feedstockLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2, 0, feedstockLabel);

							loadFeedstocks();
							feedstockHidden.setName(SAMPLE_FEEDSTOCK);
							feedstockBox.setTitle(String.valueOf(FEEDSTOCK_FLD));
							samplePanel.setWidget(subrow2, 1, feedstockBox);
							//feedstockBox.addKeyDownHandler(new MyValueChangeHandler());
							feedstockBox.addSelectionHandler(new MySelectionHandler());
							
							Label treatmentLabel = new Label(TREATMENT_LABEL);
							treatmentLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+1, 0, treatmentLabel);

							loadTreatments();
							treatmentBox.setName(SAMPLE_TREATMENT);
							treatmentBox.setTitle(String.valueOf(TREATMENT_FLD));
							samplePanel.setWidget(subrow2+1, 1, treatmentBox);
							treatmentBox.addChangeHandler(new MyChangeHandler());
							
							Label fractionLabel = new Label(FRACTION_LABEL);
							fractionLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+2, 0, fractionLabel);

							loadFractions();
							fractionBox.setName(SAMPLE_FRACTION);
							fractionBox.setTitle(String.valueOf(FRACTION_FLD));
							samplePanel.setWidget(subrow2+2, 1, fractionBox);
							fractionBox.addChangeHandler(new MyChangeHandler());

							Label fireLabel = new Label(FIRE_LABEL);
							fireLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+3, 0, fireLabel);

							loadFire();
							fireBox.setName(SAMPLE_FIRE);
							fireBox.setTitle(String.valueOf(FIRE_FLD));
							samplePanel.setWidget(subrow2+3, 1, fireBox);
							fireBox.addChangeHandler(new MyChangeHandler());

							Label reactivityLabel = new Label(REACTIVITY_LABEL);
							reactivityLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+4, 0, reactivityLabel);

							loadReactivity();
							reactivityBox.setName(SAMPLE_REACTIVITY);
							reactivityBox.setTitle(String.valueOf(REACT_FLD));
							samplePanel.setWidget(subrow2+4, 1, reactivityBox);
							reactivityBox.addChangeHandler(new MyChangeHandler());

							Label specificLabel = new Label(SPECIFIC_LABEL);
							specificLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+5, 0, specificLabel);

							loadSpecific();
							specificBox.setName(SAMPLE_SPECIFIC);
							specificBox.setTitle(String.valueOf(SPEC_FLD));
							samplePanel.setWidget(subrow2+5, 1, specificBox);
							specificBox.addChangeHandler(new MyChangeHandler());

							Label healthLabel = new Label(HEALTH_LABEL);
							healthLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+6, 0, healthLabel);

							loadHealth();
							healthBox.setName(SAMPLE_HEALTH);
							healthBox.setTitle(String.valueOf(HEALTH_FLD));
							samplePanel.setWidget(subrow2+6, 1, healthBox);
							healthBox.addChangeHandler(new MyChangeHandler());

						} else {
							Label feedstockLabel = new Label(BIOMASS_LOT_LABEL);
							//feedstockLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2, 0, feedstockLabel);

							//loadFeedstocks();
							feedstockHidden.setName(SAMPLE_FEEDSTOCK);
							biomassBox.setTitle(String.valueOf(FEEDSTOCK_FLD));
							samplePanel.setWidget(subrow2, 1, biomassBox);
							//feedstockBox.addKeyDownHandler(new MyValueChangeHandler());
							feedstockBox.addSelectionHandler(new MySelectionHandler());
							
							Label formLabel = new Label(FORM_LABEL);
							formLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+1, 0, formLabel);

							loadForms();
							formBox.setName(SAMPLE_FORM);
							formBox.setTitle(String.valueOf(FORM_FLD));
							samplePanel.setWidget(subrow2+1, 1, formBox);
							formBox.addChangeHandler(new MyChangeHandler());
							
							Label fractionLabel = new Label(FRACTION_LABEL);
							fractionLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+2, 0, fractionLabel);

							loadFractions();
							fractionBox.setName(SAMPLE_FRACTION);
							fractionBox.setTitle(String.valueOf(FRACTION_FLD));
							samplePanel.setWidget(subrow2+2, 1, fractionBox);
							fractionBox.addChangeHandler(new MyChangeHandler());

							///*
							Label compLabel = new Label(COMPOSITION_LABEL);
							compLabel.addStyleDependentName(BOLDLABEL_STYLE);
							samplePanel.setWidget(subrow2+3, 0, compLabel);

							loadCompositions();
							compositionHidden.setName(SAMPLE_COMPOSITION);
							compBox.setName(SAMPLE_COMPOSITION);
							compBox.setTitle(String.valueOf(COMPOSITION_FLD));
							samplePanel.setWidget(subrow2+3, 1, compBox);
							compBox.addChangeHandler(new MyChangeHandler());
							//*/
						}
			}
		});
		row += 8;
		
		Date date = new Date();
		// TODO - fix date format
		String dateString = String.valueOf(date.getMonth()+1)+"/"+String.valueOf(date.getDate())+"/"+String.valueOf(date.getYear()+1900);

		final HorizontalPanel labelDatePanel = new HorizontalPanel();
		dateLabelLabel.addStyleDependentName(FANCYLABEL_STYLE);
		final Label dateValue = new Label(dateString);
		dateValue.addStyleDependentName(FANCYLABEL_STYLE);
		labelDatePanel.setStyleName(FANCYPANEL_STYLE);
		labelDatePanel.add(dateLabelLabel);
		labelDatePanel.add(dateValue);

		Label dateLabel = new Label(ENTRY_DATE);
		dateLabel.addStyleDependentName(FANCYLABEL_STYLE);
		entryDate.setValue(date);
		entryDate.setFormat(new DateBox.DefaultFormat(
				DateTimeFormat.getFormat(DATE_FORMAT_DISPLAY)));
		entryDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date dt = entryDate.getValue();
				String dtString = String.valueOf(dt.getMonth()+1)+"/"+String.valueOf(dt.getDate())+"/"+String.valueOf(dt.getYear()+1900);
				dateValue.setText(dtString);
			}							
		});
		final HorizontalPanel datePanel = new HorizontalPanel();
		//datePanel.setStyleName(FANCYPANEL_STYLE);
		datePanel.add(dateLabel);
		datePanel.add(entryDate);
		samplePanel.setWidget(row, 0, datePanel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);

		final HorizontalPanel materialPanel = new HorizontalPanel();
		Label materialLabel = new Label(MATERIAL);
		materialLabel.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(materialLabel);
		labelMaterialBox1.addStyleDependentName(FANCYLABEL_STYLE);
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
				tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-6");
					}

					@Override
					public void onSuccess(String group) {
						//Window.alert("got group="+group);
			*/
						if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							labelMaterialBox1.setText("Biomass");
						}
		//			}
		//		});
		//	}
		//});
		materialPanel.add(labelMaterialBox1);
		materialPanel.add(labelComma1);
		labelComma1.setText("");
		labelMaterialBox2.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox2);
		materialPanel.add(labelComma2);
		labelMaterialBox3.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox3);
		
		final HorizontalPanel strainPanel = new HorizontalPanel();
		strainPanel.addStyleDependentName(FANCYPANEL_STYLE);
		strainPanel.setStyleName(FANCYPANEL_STYLE);
		Label labelStrainLabel = new Label(STRAIN);
		labelStrainLabel.addStyleDependentName(BOLDLABEL_STYLE);
		strainPanel.add(labelStrainLabel);
		labelStrainBox.addStyleDependentName(FANCYLABEL_STYLE);
		strainPanel.add(labelStrainBox);

		//final int subrow4=row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
			}
		});
						if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							HorizontalPanel labelFirePanel = new HorizontalPanel();
							Label fireLabel1 = new Label(FIRE_LABEL);
							fireLabel1.addStyleDependentName(FANCYLABEL_STYLE);
							labelFirePanel.add(fireLabel1);
							labelFirePanel.setStyleName(FANCYPANEL_STYLE);
							//labelFirePanel.addStyleDependentName(FANCYLABEL_STYLE);
							labelFirePanel.add(fireBoxLabel);

							HorizontalPanel labelReactivityPanel = new HorizontalPanel();
							Label reactivityLabel1 = new Label(REACTIVITY_LABEL);
							reactivityLabel1.addStyleDependentName(FANCYLABEL_STYLE);
							labelReactivityPanel.add(reactivityLabel1);
							labelReactivityPanel.setStyleName(FANCYPANEL_STYLE);
							//labelReactivityPanel.addStyleDependentName(FANCYLABEL_STYLE);
							labelReactivityPanel.add(reactivityBoxLabel);

							HorizontalPanel labelSpecificPanel = new HorizontalPanel();
							Label specificLabel1 = new Label(SPECIFIC_LABEL);
							specificLabel1.addStyleDependentName(FANCYLABEL_STYLE);
							labelSpecificPanel.add(specificLabel1);
							labelSpecificPanel.setStyleName(FANCYPANEL_STYLE);
							//labelSpecificPanel.addStyleDependentName(FANCYLABEL_STYLE);
							labelSpecificPanel.add(specificBoxLabel);

							HorizontalPanel labelHealthPanel = new HorizontalPanel();
							Label healthLabel1 = new Label(HEALTH_LABEL);
							healthLabel1.addStyleDependentName(FANCYLABEL_STYLE);
							labelHealthPanel.add(healthLabel1);
							labelHealthPanel.setStyleName(FANCYPANEL_STYLE);
							//labelHealthPanel.addStyleDependentName(FANCYLABEL_STYLE);
							labelHealthPanel.add(healthBoxLabel);

							int labelRow = 0;
							Label contentsLabel = new Label(LABEL_CONTENTS);
							contentsLabel.addStyleDependentName(BOLDFANCYLABEL_STYLE);
							labelPanel.getCellFormatter().setAlignment(labelRow, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							labelPanel.setWidget(labelRow, 0, contentsLabel);
							labelRow++;

							descriptorLabel1.addStyleDependentName(FANCYDESCLABEL_STYLE);
							descriptorLabel2.addStyleDependentName(FANCYDESCLABEL_STYLE);
							descriptorLabel3.addStyleDependentName(FANCYDESCLABEL_STYLE);
							labelDescriptorBox.addStyleName(FANCYPANEL_STYLE);
							labelDescriptorBox.setHeight(DESCRIPTOR_HEIGHT);
							labelDescriptorBox.setWidth(DESCRIPTOR_WIDTH);
							labelOwnPanel.addStyleName(FANCYPANEL_STYLE);
							labelDatePanel.addStyleName(FANCYPANEL_STYLE);
							trbPanel.addStyleName(FANCYPANEL_STYLE);
							namePanel.addStyleName(FANCYPANEL_STYLE);
							materialPanel.addStyleName(FANCYPANEL_STYLE);
							labelPanel.addStyleName(FANCYPANEL_STYLE);
							labelDescriptorBox.add(descriptorLabel1);
							labelDescriptorBox.add(descriptorLabel2);
							labelDescriptorBox.add(descriptorLabel3);
							fireBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							reactivityBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							specificBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							healthBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							labelPanel.setWidget(labelRow++, 0, labelDescriptorBox);
							labelPanel.setWidget(labelRow++, 0, materialPanel);
							labelPanel.setWidget(labelRow++, 0, namePanel);
							labelPanel.setWidget(labelRow++, 0, trbPanel);
							labelPanel.setWidget(labelRow++, 0, labelDatePanel);
							labelPanel.setWidget(labelRow++, 0, labelOwnPanel);
							labelPanel.setWidget(labelRow++, 0, labelFirePanel);
							labelPanel.setWidget(labelRow++, 0, labelReactivityPanel);
							labelPanel.setWidget(labelRow++, 0, labelSpecificPanel);
							labelPanel.setWidget(labelRow++, 0, labelHealthPanel);
						} else {
							int labelRow = 0;
							Label contentsLabel = new Label(LABEL_CONTENTS);
							contentsLabel.addStyleDependentName(BOLDFANCYLABEL_STYLE);
							labelPanel.getCellFormatter().setAlignment(labelRow, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							labelPanel.setWidget(labelRow, 0, contentsLabel);
							labelRow++;

							/*
							Label destLabel = new Label(DESTINATION_LABEL);
							destLabel.addStyleDependentName(FANCYLABEL_STYLE);
							labelDestPanel.add(destLabel);
							labelDestPanel.setStyleName(FANCYPANEL_STYLE);
							labelDestBox.addStyleDependentName(FANCYLABEL_STYLE);
							labelDestPanel.add(labelDestBox);							
							*/

							descriptorLabel1.addStyleDependentName(FANCYDESCLABEL_STYLE);
							descriptorLabel2.addStyleDependentName(FANCYDESCLABEL_STYLE);
							labelDescriptorBox.addStyleName(FANCYPANEL_STYLE);
							labelDescriptorBox.setHeight(DESCRIPTOR_HEIGHT_SABC);
							labelDescriptorBox.setWidth(DESCRIPTOR_WIDTH);
							labelOwnPanel.addStyleName(FANCYPANEL_STYLE);
							labelDatePanel.addStyleName(FANCYPANEL_STYLE);
							trbPanel.addStyleName(FANCYPANEL_STYLE);
							namePanel.addStyleName(FANCYPANEL_STYLE);
							materialPanel.addStyleName(FANCYPANEL_STYLE);
							labelPanel.addStyleName(FANCYPANEL_STYLE);
							labelDescriptorBox.add(descriptorLabel1);
							labelDescriptorBox.add(descriptorLabel2);
							//fireBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							//reactivityBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							//specificBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							//healthBoxLabel.addStyleDependentName(FANCYDESCLABEL_STYLE);
							labelPanel.setWidget(labelRow++, 0, labelDescriptorBox);
							//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE))
								labelPanel.setWidget(labelRow++, 0, trbPanel);
							labelPanel.setWidget(labelRow++, 0, materialPanel);
							labelPanel.setWidget(labelRow++, 0, strainPanel);
							labelPanel.setWidget(labelRow++, 0, namePanel);
							//labelPanel.setWidget(labelRow++, 0, labelDestPanel);
							labelPanel.setWidget(labelRow++, 0, labelCustPanel);
							labelPanel.setWidget(labelRow++, 0, labelDatePanel);
							//labelPanel.setWidget(labelRow++, 0, labelFirePanel);
							//labelPanel.setWidget(labelRow++, 0, labelReactivityPanel);
							//labelPanel.setWidget(labelRow++, 0, labelSpecificPanel);
							//labelPanel.setWidget(labelRow++, 0, labelHealthPanel);							
						}
		row += 7;
		
		Label amountLabel = new Label(APPROXIMATE_AMOUNT_LABEL);
		samplePanel.setWidget(row, 0, amountLabel);

		amountBox.setName(SAMPLE_AMOUNT);
		amountBox.setTitle(String.valueOf(AMT_FLD));
		samplePanel.setWidget(row, 1, amountBox);

		loadUnits();
		unitBox.setName(SAMPLE_UNIT);
		unitBox.setTitle(String.valueOf(UNIT_FLD));
		samplePanel.setWidget(row++, 2, unitBox);

		HTML threeHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, threeHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		final int subrow3=row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
						if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							Label buildingLabel = new Label(BUILDING_LABEL);
							samplePanel.setWidget(subrow3, 0, buildingLabel);

							loadBuildings();
							buildingHidden.setName(SAMPLE_BUILDING);
							buildingBox.setTitle(String.valueOf(BLDG_FLD));
							samplePanel.setWidget(subrow3, 1, buildingBox);

							Label roomLabel = new Label(ROOM_LABEL);
							samplePanel.setWidget(subrow3+1, 0, roomLabel);

							loadRooms();
							roomHidden.setName(SAMPLE_ROOM);
							roomBox.setTitle(String.valueOf(ROOM_FLD));
							samplePanel.setWidget(subrow3+1, 1, roomBox);

							Label sublocationLabel = new Label(SUBLOCATION_LABEL);
							samplePanel.setWidget(subrow3+2, 0, sublocationLabel);

							loadSublocations();
							sublocationHidden.setName(SAMPLE_SUBLOCATION);
							sublocationBox.setTitle(String.valueOf(SUBLOC_FLD));
							samplePanel.setWidget(subrow3+2, 1, sublocationBox);

							Label shelfLabel = new Label(SHELF_LABEL);
							samplePanel.setWidget(subrow3+3, 0, shelfLabel);

							loadShelves();
							shelfHidden.setName(SAMPLE_SHELF);
							shelfBox.setTitle(String.valueOf(SHELF_FLD));
							samplePanel.setWidget(subrow3+3, 1, shelfBox);

							Label holderLabel = new Label(HOLDER_LABEL);
							samplePanel.setWidget(subrow3+4, 0, holderLabel);

							loadHolders();
							holderHidden.setName(SAMPLE_HOLDER);
							holderBox.setTitle(String.valueOf(HOLDER_FLD));
							samplePanel.setWidget(subrow3+4, 1, holderBox);

							Label packagingLabel = new Label(PACKAGING_LABEL);
							samplePanel.setWidget(subrow3+5, 0, packagingLabel);

							loadPackagings();
							packagingHidden.setName(SAMPLE_PACKAGING);
							packagingBox.setTitle(String.valueOf(PKG_FLD));
							samplePanel.setWidget(subrow3+5, 1, packagingBox);
						} else {
							
						}
			}
		});
		row += 6;

		Label storageNotesLabel = new Label(STORAGE_NOTES_LABEL);
		samplePanel.setWidget(row, 0, storageNotesLabel);

		storageNotesBox.setName(SAMPLE_STORAGENOTES);
		storageNotesBox.setTitle(String.valueOf(STORE_NOTES_FLD));
		samplePanel.setWidget(row++, 1, storageNotesBox);

		HTML fourHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, fourHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		Label commentLabel = new Label(COMMENTS_LABEL);
		samplePanel.setWidget(row, 0, commentLabel);

		commentBox.setName(SAMPLE_COMMENTS);
		commentBox.setTitle(String.valueOf(COMMENT_FLD));
		samplePanel.setWidget(row++, 1, commentBox);

		HTML fiveHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, fiveHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		
		row++;

		final int subrow4=row;
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-2");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
				tsa.hasPermission(sessionId, "Attach", new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("Shouldn't get to here-3");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							initAttachmentForm(samplePanel, subrow4);
						}
					}
				});
			}
		});
		
		row+=2;

		HTML sixHeadingLabel = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, sixHeadingLabel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);

		row++;

		// Printer listbox		
		Label printerLabel = new Label("Select a label printer: ");
		printerPanel.add(printerLabel);
		printerPanel.add(printerBox);
		Label noteLabel = new Label("Note: Printers missing from list are unavailable for printing.");
		printerPanel.add(noteLabel);
		samplePanel.setWidget(row, 0, printerPanel);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);		
		row++;
		
		HTML ruler7Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler7Label);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);
		
		row++;
		
		int col = 0;
		samplePanel.setWidget(row, col, feedstockHidden);
		col++;
		samplePanel.setWidget(row, col, ownNameHidden);
		col++;
		samplePanel.setWidget(row, col, custNameHidden);
		col++;
		samplePanel.setWidget(row, col, originHidden);
		col++;
		samplePanel.setWidget(row, col, buildingHidden);
		col++;
		samplePanel.setWidget(row, col, roomHidden);
		col++;
		samplePanel.setWidget(row, col, sublocationHidden);
		col++;
		samplePanel.setWidget(row, col, shelfHidden);
		col++;
		samplePanel.setWidget(row, col, holderHidden);
		col++;
		samplePanel.setWidget(row, col, packagingHidden);
		
		row++;

		Button submitButton = new Button(SUBMIT_BUTTON_TEXT);
		submitButton.addClickHandler(new TrackClickHander());
		samplePanel.setWidget(row, 0, submitButton);
		samplePanel.getCellFormatter().setHorizontalAlignment(row, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		Button clearButton = new Button(CLEAR_BUTTON_TEXT);
		clearButton.addClickHandler(new ClearClickHander());
		samplePanel.setWidget(row, 1, clearButton);
		samplePanel.getCellFormatter().setHorizontalAlignment(row, 1,
				HasHorizontalAlignment.ALIGN_CENTER);

		printButton.addClickHandler(new PrintClickHandler());
		printButton.setVisible(false);
		samplePanel.setWidget(row, 2, printButton);
		samplePanel.getCellFormatter().setHorizontalAlignment(row, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		emailButton.addClickHandler(new EmailClickHander());
		emailButton.setVisible(false);
		samplePanel.setWidget(row, 3, emailButton);
		samplePanel.getCellFormatter().setHorizontalAlignment(row, 3,
				HasHorizontalAlignment.ALIGN_CENTER);

		mainPanel.add(formPanel);
		mainPanel.setCellHorizontalAlignment(formPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.add(hiddenFormPanel);
		
		printerPanel.setVisible(false);
		//printerPanel.clear();
		if (DevTestProdConstants.SECURITY_MODE==DevTestProdConstants.ON) {
			tsa.getSessionId(new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("Shouldn't get to here-2");
				}
	
				@Override
				public void onSuccess(String result) {
					//Window.alert("From server:session="+result);
					sessionId = result;
					tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
	
						@Override
						public void onFailure(Throwable caught) {
							//Window.alert("Shouldn't get to here-3");
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
							}
						}
						
					});
				}
			});
		} else {
			loadPrinters();
			printerPanel.setVisible(true);
		}
		//String jSessionId=Cookies.getCookie("JSESSIONID");
		//Window.alert("session="+jSessionId);
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
			if (tb == numBox) {
				int len = val.length();
				String padded = "";
				for (int i=len;i<5;i++)
					padded += DEFAULT_0;
				padded += val;
				labelNumBox.setText(padded);
			} else if (tb == pageBox) {
				int len = val.length();
				String padded = "";
				for (int i=len;i<3;i++)
					padded += DEFAULT_0;
				padded += val;
				labelPageBox.setText(padded);
			} else if (tb == descriptorBox) {
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
			    final String desc3 = descriptor3;
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
							descriptorLabel3.setText(desc3);
						}
					//}
				//});
			}
		} else if (obj instanceof SuggestBox) {
			SuggestBox sb = (SuggestBox)obj;
			if (sb == sampleIDBox) {
				val = sampleIDBox.getValue();
				labelNameBox.setText(val);
			} else if (sb == feedstockBox) {
						if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
							val = feedstockBox.getValue();
							labelMaterialBox1.setText(val);
							if (val != null && !val.isEmpty()) 
								labelComma1.setText(COMMA);
							else
								labelComma1.setText("");
						} else {
							labelMaterialBox1.setText("Biomass");
						}
			} else if (sb == strainBox) {
				val = strainBox.getValue();
				labelStrainBox.setText(val);
			} else if (sb == ownNameBox) {
				val = ownNameBox.getValue();
				labelOwnBox.setText(val);
			} else if (sb == custNameBox) {
				val = custNameBox.getValue();
				labelCustBox.setText(val);
			}
		} else if (obj instanceof ListBox) {
			ListBox lb = (ListBox)obj;
			int index = lb.getSelectedIndex();
			if (index == -1) 
				index = 0;
			val = lb.getItemText(index);
			if (lb == formBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == fractionBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == compBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == treatmentBox) {
				labelMaterialBox2.setText(val);
				if (val != null && !val.isEmpty() && (formBox.getSelectedIndex() != -1 || fractionBox.getSelectedIndex() != -1 || compBox.getSelectedIndex() != -1)) 
					labelComma2.setText(COMMA);
				else
					labelComma2.setText("");
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
		}    	
    }
    private String constructMaterial() {
    	String val = "";
		String form = "";
		String comma = "";
		if (labelComma2.getText().isEmpty() && (formBox.getSelectedIndex() != -1 || fractionBox.getSelectedIndex() != -1 || compBox.getSelectedIndex() != -1))
			comma = ",";
		if (formBox.getSelectedIndex() != -1)
			form = comma+formBox.getItemText(formBox.getSelectedIndex());
		String fraction = "";
		if (fractionBox.getSelectedIndex() != -1)
			fraction = comma+fractionBox.getItemText(fractionBox.getSelectedIndex());
		String comp = "";
		if (compBox.getSelectedIndex() != -1)
			comp = comma+compBox.getItemText(compBox.getSelectedIndex());
		if (!form.isEmpty())
			val += form;
		if (!fraction.isEmpty()) {
			if (!val.isEmpty()) val += ",";
			val += fraction;
		}
		if (!comp.isEmpty()) {
			if (!val.isEmpty()) val += ",";
			val += comp;
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
	public class MyMouseUpHandler implements MouseUpHandler {

		@Override
		public void onMouseUp(MouseUpEvent event) {
			Object obj = event.getSource();
			changeLabel(obj);
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
	 * @param samplePanel
	 * @param row
	 */
	private void initAttachmentForm(FlexTable samplePanel, int row) {
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
				Iterator<FileInfo> it = attachmentList.iterator();
				while (it.hasNext()) {
					FileInfo info = it.next();
					if (info.getFilename().equals(fileName)) {
						boolean ret = attachmentList.remove(info);
						info.setAttachment_id(attachmentId);
						if (ret) {
							attachmentList.add(info);
							// Add file to list
	                	    int gridRow = sampleGrid.getRowCount();
	                	    sampleGrid.insertRow(gridRow);
			        	    sampleGrid.setWidget(gridRow, 0, new CheckBox());
			        		sampleGrid.getCellFormatter().setHorizontalAlignment(gridRow, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);
			        		sampleGrid.setText(gridRow, ATTACHMENT_ID_COL, info.getAttachment_id());
			        		sampleGrid.setText(gridRow, ATTACHMENT_NAME_COL, info.getName());
			    			sampleGrid.getCellFormatter().setWordWrap(gridRow, 1, false);
			    			sampleGrid.setVisible(true);
							break;
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
		
		attachmentsTable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		attachmentsDPanel.add(attachmentsTable);
		attachmentFormPanel.add(attachmentsDPanel);
		samplePanel.setWidget(row, 0, attachmentFormPanel);
		samplePanel.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		samplePanel.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		samplePanel.getFlexCellFormatter().setRowSpan(row, 0, 2);
		samplePanel.getFlexCellFormatter().setColSpan(row, 0, 3);

		Label attachmentLabel = new Label("Select a file to attach:");
		sampleAttachmentsPanel.add(attachmentLabel);
		
		sampleAttachmentsPanel.add(attachFile);
		
		attachmentsTable.add(sampleAttachmentsPanel);
		
		Label smAttachmentsLabel = new Label("List of Attachments");
		smAttachmentsLabel.addStyleDependentName("SubSubTitle");
		attachmentsTable.add(smAttachmentsLabel);
		
		sampleGrid.setWidth("100%");

		sampleGrid.addClickHandler(new ItemSelectClickHandler());
		sampleGrid.setCellSpacing(3);
		sampleGrid.setBorderWidth(1);
		sampleGrid.insertRow(0);
		
		sampleGrid.setWidget(0, ATTACHMENT_SELECT_COL, new Label(SELECT_ROW));
		sampleGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_SELECT_COL, HasHorizontalAlignment.ALIGN_CENTER);
		sampleGrid.setWidget(0, ATTACHMENT_ID_COL, new Label(ATTACHMENT_ID));
		sampleGrid.getCellFormatter().setWordWrap(0, ATTACHMENT_ID_COL, false);
		sampleGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_ID_COL, HasHorizontalAlignment.ALIGN_CENTER);
		sampleGrid.setWidget(0, ATTACHMENT_NAME_COL, new Label(FILE));
		sampleGrid.getCellFormatter().setWordWrap(0, ATTACHMENT_NAME_COL, false);
		sampleGrid.getCellFormatter().setHorizontalAlignment(0, ATTACHMENT_NAME_COL, HasHorizontalAlignment.ALIGN_CENTER);
		sampleGrid.setVisible(false);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidth(SECOND_COLUMN_WIDTH);
		scrollPanel.setHeight("100px");
		VerticalPanel scrollVert = new VerticalPanel();
		scrollVert.add(sampleGrid);
		scrollVert.setHeight("100px");
		scrollPanel.add(scrollVert);
		attachmentsTable.add(scrollPanel);

		HorizontalPanel attachmentButtonPanel = new HorizontalPanel();
		attachmentButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		attachmentButtonPanel.setSpacing(20);
		addButton.addClickHandler(new AddAttachmentClickHandler());
		attachmentButtonPanel.add(addButton);
		removeButton.addClickHandler(new RemoveAttachmentClickHandler());
		attachmentButtonPanel.add(removeButton);
		removeButton.setEnabled(false);
		attachmentsTable.add(attachmentButtonPanel);
		
		sampleAttachmentsPanel.setWidth("400px");
		sampleAttachmentsPanel.setHeight("30px");
		attachmentsDPanel.setWidth("400px");
		attachmentsDPanel.setHeight("100px");
		attachmentsTable.setWidth("400px");
		attachmentsTable.setHeight("100px");
	}

	/**
	 * Private <ClickHandler> to handle the submit button and 
	 *  submit the form to the server.
	 * 
	 * @author jalbersh
	 *
	 */
	private class TrackClickHander implements ClickHandler {

		public void onClick(ClickEvent event) {
			printButton.setVisible(false);
			emailButton.setVisible(false);
			if (validForm()) {
				final SampleCriteria sample = new SampleCriteria();
				
				if (sampleIDBox.isValid())
					sample.setSampleId(sampleIDBox.replaceQuotes(sampleIDBox.getValue()));
				else {
					Window.alert("The Sample ID is invalid.");
					return;
				}
				if (externalIdBox.isValid())
					sample.setExternalId(externalIdBox.replaceQuotes(externalIdBox.getValue()));
				else {
					Window.alert("The External ID is invalid.");
					return;
				}
				
				if (amountBox.isValid())
					sample.setAmount(amountBox.getValue());
				else {
					Window.alert("The Amount is invalid.");
					return;
				}
				
				if (commentBox.isValid())
					sample.setComments(commentBox.replaceQuotes(commentBox.getValue()));
				else {
					Window.alert("The Comment is invalid.");
					return;
				}
				
				if (custNameBox.isValid())
					sample.setCustodianName(custNameBox.replaceQuotes(custNameBox.getValue()));
				else {
					Window.alert("The Custodian Name is invalid.");
					return;
				}
				
				//int index =	destinationListBox.getSelectedIndex();
				//if (index > -1)
					sample.setDestination(destinationListBox.getText());
				
				int index =	formBox.getSelectedIndex();
				if (index > -1)
					sample.setForm(formBox.getItemText(index));
				
				index =	compBox.getSelectedIndex();
				if (index > -1)
					sample.setComposition(compBox.getItemText(index));
				
				if (custNameBox.isValid()) {
					sample.setCustodianName(custNameBox.replaceQuotes(custNameBox.getValue()));
				}
				
				if (strainBox.isValid()) {
					sample.setStrain(strainBox.replaceQuotes(strainBox.getValue()));
				}
				
				if (biomassBox.isValid()) {
					sample.setBiomass_lots(biomassBox.replaceQuotes(biomassBox.getValue()));
				}
				
				if (ownNameBox.isValid())
					sample.setOwnerName(ownNameBox.replaceQuotes(ownNameBox.getValue()));
				else {
					Window.alert("The Owner Name is invalid.");
					return;
				}
				
				sample.setStartCreateDate(new Date());
				
				if (feedstockBox.isValid())
					sample.setFeedstock(feedstockBox.replaceQuotes(feedstockBox.getValue()));
				else {
					Window.alert("The Feedstock is invalid.");
					return;
				}
				
				/* TODO - only for security
					if (groupBox.getItemCount()>0) {
						int count=0;
						for (int i=0;i<groupBox.getItemCount();i++) {
							if (groupBox.isItemSelected(i)) {
								count++;
								sample.getGroups().add(groupBox.getItemText(i));
							}
						}
						if (count == 0) {
							if (groupBox.getItemCount()>1) { 
								Window.alert("A group must be selected");
								return;
							} else {
								String group = groupBox.getItemText(0);
								sample.getGroups().add(group);
							}
						}
					} else {
						tsa.getGroupsForUser(sessionId, new AsyncCallback<List<String>>() {
							@Override
							public void onFailure(Throwable caught) {
								//Window.alert("Shouldn't get to here-2");
							}
							@Override
							public void onSuccess(List<String> result) {
								if (result != null && result.size()>0) {
									if (result.size()>1) {
										Window.alert("A group must be selected");
										return;
									} else
										sample.getGroups().add(result.get(0));
								}
							}
						});
					}
				 */
				
				index =	originListBox.getSelectedIndex();
				if (index > -1)
					sample.setOrigin(originListBox.getItemText(index));
				sample.setStatus(statusBox.getItemText(statusBox.getSelectedIndex()));
				sample.setFraction(fractionBox.getItemText(fractionBox.getSelectedIndex()));

				sample.setUnits(unitBox.getItemText(unitBox.getSelectedIndex()));
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
				if (!numBox.isValid()) {
					Window.alert("The TRB Number is invalid");
					return;
				}
				
				if (!pageBox.isValid()) {
					Window.alert("The TRB Page is invalid");
					return;
				}
				int trbNum = 0;
				int trbPage = 0;
				try {
					trbNum = Integer.parseInt(numBox.getValue());
				} catch (NumberFormatException nfe) {
				   //Window.alert("A valid TRB Number was not entered.");
				}
				try {
					trbPage = Integer.parseInt(pageBox.getValue());
				} catch (NumberFormatException nfe) {
				   //Window.alert("A valid TRB Page was not entered.");
				}
				sample.setTrbNum(trbNum);
				sample.setTrbPage(trbPage);
				
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
							sample.setFire(fireBox.getItemText(fireBox.getSelectedIndex()));
							sample.setReactivity(reactivityBox.getItemText(reactivityBox.getSelectedIndex()));
							sample.setSpecific(specificBox.getItemText(specificBox.getSelectedIndex()));
							sample.setHealth(healthBox.getItemText(healthBox.getSelectedIndex()));
							sample.setTreatment(treatmentBox.getItemText(treatmentBox.getSelectedIndex()));
							
							if (buildingBox.isValid()){
								buildingBox.replaceQuotes(buildingBox.getValue());
								if(buildingBox.getValue() == null)
									sample.setBuilding("");
								else
									sample.setBuilding(buildingBox.getValue());
							}
							else {
								Window.alert("The Building location is invalid.");
								return;
							}
							
							if (roomBox.isValid()){
								roomBox.replaceQuotes(roomBox.getValue());
								if(roomBox.getValue() == null)
									sample.setRoom("");
								else
									sample.setRoom(roomBox.getValue());
							}
							else {
								Window.alert("The Room location is invalid.");
								return;
							}
							
							if (holderBox.isValid()){
								holderBox.replaceQuotes(holderBox.getValue());
								if(holderBox.getValue() == null)
									sample.setHolder("");
								else
									sample.setHolder(holderBox.getValue());
							}
							else {
								Window.alert("The Holder location is invalid.");
								return;
							}
							
							if (shelfBox.isValid()){
								shelfBox.replaceQuotes(shelfBox.getValue());
								if(shelfBox.getValue() == null)
									sample.setShelf("");
								else
									sample.setShelf(shelfBox.getValue());
							}
							else {
								Window.alert("The Shelf location is invalid.");
								return;
							}
							
							if (packagingBox.isValid()){
								packagingBox.replaceQuotes(packagingBox.getValue());
								if(packagingBox.getValue() == null)
									sample.setPackaging("");
								else
									sample.setPackaging(packagingBox.getValue());
							}
							else {
								Window.alert("The Packaging location is invalid.");
								return;
							}
							
							if (sublocationBox.isValid()){
								sublocationBox.replaceQuotes(sublocationBox.getValue());
								if(sublocationBox.getValue() == null)
									sample.setSubLocation("");
								else
									sample.setSubLocation(sublocationBox.getValue());
							}
							else {
								Window.alert("The Sub-location location is invalid.");
								return;
							}
						}
				//	}
				//});
				
				if (storageNotesBox.isValid())
					sample.setStorageNotes(storageNotesBox.replaceQuotes(storageNotesBox.getValue()));
				else {
					Window.alert("The Storage Notes is invalid.");
					return;
				}
				
				if (descriptorBox.isValid())
					sample.setLabelDescription(descriptorBox.getValue());
				else {
					Window.alert("The Label Descriptor is invalid.");
					return;
				}
				
				if (attachmentList != null && !attachmentList.isEmpty()) {
					Iterator<FileInfo> it = attachmentList.iterator();
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
								sample.getAttachmendIds().add(new Long(attachId));
							}		
						}
					}
				}
				
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
				tsa.saveSample(sessionId, sample, new AsyncCallback<Long>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Sample save failed.");
						printButton.setVisible(false);
						emailButton.setVisible(false);
						saved = false;
					}

					@Override
					public void onSuccess(Long result) {
						if (result.longValue() > 0) {
							Window.alert("Sample saved successfully.");
							/*
							tsa.hasPermission(sessionId, "Print", new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									// do nothing
								}
								@Override
								public void onSuccess(Boolean result) {
									if (result)
							*/
										printButton.setVisible(true);
							//	}
							//});
							//emailButton.setVisible(true);
							sampleId = String.valueOf(result.longValue());
							String labelString = TITLE_HTML;
							labelString = labelString.replace("{TBD}", sampleId);
							sampleIDLabel.setHTML(labelString);
							saved = true;
						}
						else {
							Window.alert("Sample failed to save.");
							printButton.setVisible(false);
							emailButton.setVisible(false);
							saved = false;
						}
					}					
				});
			}
			else
				Window.alert("You must enter data to create a sample.");
		}

		private boolean validForm() {
			//if (sampleIDBox.getValue().isEmpty())
			//	return false;
			//if (treatmentBox.getItemText(treatmentBox.getSelectedIndex()).isEmpty())
			//	return false;
			return true;
		}		
	}
	
	/**
	 * Private <ClickHandler> that handles the clear button and
	 *  clears the fields on the screen.
	 * 
	 * @author jalbersh
	 *
	 */
	private class ClearClickHander implements ClickHandler {

		public void onClick(ClickEvent event) {
			unitBox.setSelectedIndex(0);
			statusBox.setSelectedIndex(0);
			feedstockBox.setValue("");
			fractionBox.setSelectedIndex(0);
			treatmentBox.setSelectedIndex(0);
			sampleIDBox.setValue("");
			fireBox.setSelectedIndex(0);
			reactivityBox.setSelectedIndex(0);
			specificBox.setSelectedIndex(0);
			healthBox.setSelectedIndex(0);
			commentBox.setValue("");
			descriptorBox.setValue("");
			amountBox.setValue("");
			externalIdBox.setValue("");
			custNameBox.setValue("");
			ownNameBox.setValue("");
			buildingBox.setValue("");
			roomBox.setValue("");
			sublocationBox.setValue("");
			shelfBox.setValue("");
			holderBox.setValue("");
			packagingBox.setValue("");
			shelfBox.setValue("");
			storageNotesBox.setValue("");
			numBox.setValue("");
			pageBox.setValue("");
			originListBox.setSelectedIndex(0);	
			destinationListBox.setValue("");
			strainBox.setValue("");
			biomassBox.setValue("");
			formBox.setSelectedIndex(0);
			compBox.setSelectedIndex(0);
			//entryDate.setValue(null);
			labelCustBox.setText("");
			labelDestBox.setText("");
			labelMaterialBox1.setText("");
			labelMaterialBox2.setText("");
			labelMaterialBox3.setText("");
			labelNameBox.setText("");
			labelNumBox.setText("");
			labelOwnBox.setText("");
			labelPageBox.setText("");
			labelStrainBox.setText("");
			fireBoxLabel.setText("");
			reactivityBoxLabel.setText("");
			specificBoxLabel.setText("");
			healthBoxLabel.setText("");
			descriptorBox.setText("");
			descriptorLabel1.setText("");
			descriptorLabel2.setText("");
			descriptorLabel3.setText("");
			labelDescriptorBox.clear();
			if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
				labelDescriptorBox.add(descriptorLabel1);
				labelDescriptorBox.add(descriptorLabel2);
				labelDescriptorBox.add(descriptorLabel3);				
			} else {
				labelDescriptorBox.add(descriptorLabel1);
				labelDescriptorBox.add(descriptorLabel2);
			}
			//dateLabelLabel.setText("");
			//attachFile.set
			//attachmentList.clear();
			
			int index = -1;
			for (int row=1;row<sampleGrid.getRowCount() && index == -1;row++) {
				//CheckBox cb = (CheckBox)sampleGrid.getWidget(row, 0);
				index = row;

				final Integer iIndex = new Integer(index);
				if (index == -1) {
					//Window.alert("Please select a file to remove.");
				}
				else {
					// Delete the file on the server
					long attachmentId = 0;
					Iterator<FileInfo> it = attachmentList.iterator();
					while (it.hasNext()) {
						FileInfo fi = it.next();
						String attachmentIdString = fi.getAttachment_id();
						String gridId = sampleGrid.getText(index, ATTACHMENT_ID_COL);
						if (attachmentIdString.equalsIgnoreCase(gridId)) {
							try {
								attachmentId = Long.parseLong(fi.getAttachment_id());
							} catch (NumberFormatException e) {
								attachmentId = -1;
							}
							if (attachmentId != -1) {
								tsa.removeAttachment(attachmentId, null, new AsyncCallback<Boolean>() {
		
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Error removing file. Error: " + caught.getLocalizedMessage());
									}
		
									@Override
									public void onSuccess(Boolean result) {
										sampleGrid.removeRow(iIndex);
										if (sampleGrid.getRowCount() == 1)
											sampleGrid.setVisible(false);
										removeButton.setEnabled(false);
										downloadButton.setEnabled(false);
									}
									
								});
							}
						}
					}
				}
			}
			attachmentList.clear();
			sampleGrid.setVisible(sampleGrid.getRowCount()>1);
		}		
	}
	
	/**
	 * Private <ClickHandler> that handles the clear button and
	 *  clears the fields on the screen.
	 * 
	 * @author jalbersh
	 *
	 */
	private class PrintClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			//Window.alert("Requested label print");
			final LabelDTO label = new LabelDTO();
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
					label.setOwnerName(ownNameBox.getValue());
					label.setTrbNum(numBox.getValue());
					label.setTrbPage(pageBox.getValue());
					label.setTrackingId(sampleId);
					label.setSampleId(sampleIDBox.getValue());			
				
					String printerName = printerBox.getItemText(printerBox.getSelectedIndex());
					if (printerName != null && !printerName.trim().isEmpty()) {
					tsa.printLabel(sessionId, label, printerName, new AsyncCallback<Boolean>() {
					
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(PROBLEM_PRINTING);
						}
					
						@Override
						public void onSuccess(Boolean result) {
							// Not sure if we should do anything or not.	
							if (!result.booleanValue()) {
								Window.alert(PROBLEM_PRINTING);							
							} else
								Window.alert(SUCCESS_PRINTING);
						}				
					});
					} else {
						Window.alert("No printer selected. Please select a printer.");
					}
				} else {
					label.setCustodian(custNameBox.getValue());
					label.setStrain(strainBox.getValue());
					int index =	formBox.getSelectedIndex();
					if (index > -1)
						label.setForm(formBox.getItemText(index));
					index =	compBox.getSelectedIndex();
					if (index > -1)
						label.setComposition(compBox.getItemText(index));
					index =	fractionBox.getSelectedIndex();
					if (index > -1)
						label.setFraction(fractionBox.getItemText(index));
					label.setTrackingId(sampleId);
					label.setSampleId(sampleIDBox.getValue());	
					//index =	destinationListBox.getSelectedIndex();
					//if (index > -1)
						label.setDestination(destinationListBox.getText());
		
					String printerName = printerBox.getItemText(printerBox.getSelectedIndex());
					if (printerName != null && !printerName.trim().isEmpty()) {
					tsa.printLabel(sessionId, label, printerName, new AsyncCallback<Boolean>() {
					
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(PROBLEM_PRINTING);
						}
				
						@Override
						public void onSuccess(Boolean result) {
							// Not sure if we should do anything or not.	
							if (!result.booleanValue()) {
								Window.alert(PROBLEM_PRINTING);							
							} else
								Window.alert(SUCCESS_PRINTING);
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
	 * Private <ClickHandler> that handles the clear button and
	 *  clears the fields on the screen.
	 * 
	 * @author jalbersh
	 *
	 */
	private class EmailClickHander implements ClickHandler {

		public void onClick(ClickEvent event) {
			final LabelDTO label = new LabelDTO();
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
						label.setOwnerName(ownNameBox.getValue());
						label.setTrbNum(numBox.getValue());
						label.setTrbPage(pageBox.getValue());
						label.setTrackingId(sampleId);
						label.setSampleId(sampleIDBox.getValue());			
			
							tsa.emailLabel(sessionId, label, new AsyncCallback<Boolean>() {
				
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(PROBLEM_EMAILING);
								}
				
								@Override
								public void onSuccess(Boolean result) {
									// Not sure if we should do anything or not.	
									if (!result.booleanValue()) {
										Window.alert(PROBLEM_EMAILING);							
									}
								}				
							});
					} else {
						label.setCustodian(custNameBox.getValue());
						label.setStrain(strainBox.getValue());
						int index =	formBox.getSelectedIndex();
						if (index > -1)
							label.setForm(formBox.getItemText(index));
						index =	fractionBox.getSelectedIndex();
						if (index > -1)
							label.setFraction(fractionBox.getItemText(index));
						index =	compBox.getSelectedIndex();
						if (index > -1)
							label.setComposition(compBox.getItemText(index));
						label.setTrackingId(sampleId);
						label.setSampleId(sampleIDBox.getValue());	
						//index =	destinationListBox.getSelectedIndex();
						//if (index > -1)
							label.setDestination(destinationListBox.getText());
			
							tsa.emailLabel(sessionId, label, new AsyncCallback<Boolean>() {
				
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(PROBLEM_EMAILING);
								}
				
								@Override
								public void onSuccess(Boolean result) {
									// Not sure if we should do anything or not.	
									if (!result.booleanValue()) {
										Window.alert(PROBLEM_EMAILING);							
									}
								}				
							});
					}
				//}
			//});
		}		
	}

	private void loadDestinations() {
		tsa.getDestinations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					destOracle.clear();
					//destinationListBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							destOracle.add(suggestion);
					}
				}
			}			
		});		
	}
	
	private void loadStrains() {
		tsa.getStrains(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							strainOracle.add(suggestion);
							//strainsListBox.addItem(suggestion);
					}
				}
			}			
		});		
	}
	
	/**
	 * Private method to get owner names
	 */
	private void loadOwnName() {
		tsa.getOwnerNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							ownNameOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to get owner names
	 */
	private void loadCustodians() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-6");
			}

			@Override
			public void onSuccess(final String sessionId) {
				tsa.getCustodians(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						//Window.alert("Owner Names failed to load.\nError: " + caught.getMessage());				
					}
		
					public void onSuccess(Collection<String> result) {
						if (result != null && !result.isEmpty()) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String suggestion = it.next();
								if (suggestion != null)
									custNameOracle.add(suggestion);
							}
						}
					}			
				});		
			}
		});
	}

	/**
	 * Private method to get customer names
	 */
	private void loadSampleIds() {
		tsa.getGroupForUser(sessionId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-6");
			}

			@Override
			public void onSuccess(final String group) {
				tsa.getSampleIds(group, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						//Window.alert("Custodian Names failed to load.\nError: " + caught.getMessage());				
					}
		
					public void onSuccess(Collection<String> result) {
						if (result != null && !result.isEmpty()) {
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String suggestion = it.next();
								if (suggestion != null)
									sampleOracle.add(suggestion);
							}
						}
					}			
				});	
			}
		});
	}

	/**
	 * Private method to get customer names
	 */
	private void loadCustName() {
		tsa.getCustodianNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Custodian Names failed to load.\nError: " + caught.getMessage());				
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String suggestion = it.next();
						if (suggestion != null)
							custNameOracle.add(suggestion);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the treatment list box.
	 */
	private void loadTreatments() {
		tsa.getTreatmentNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					int ctr=0;
					treatmentBox.clear();
					treatmentBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							treatmentBox.addItem(name);
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
	 * Private method to load the form list box.
	 */
	///*
	private void loadCompositions() {
		tsa.getCompositions(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					compBox.clear();
					compBox.addItem("");
					int ctr=0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							compBox.addItem(name);
						}
						ctr++;
					}
				} else {
					//Window.alert("There are no treatments loaded.\nCheck with administrator.");
				}
			}			
		});		
	}
	//*/

	/**
	 * Private method to load the form list box.
	 */
	private void loadForms() {
		tsa.getForms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Treatments failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					formBox.clear();
					formBox.addItem("");
					int ctr=0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							formBox.addItem(name);
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
	 * Private method to load the fractions list box.
	 */
	private void loadFractions() {
		tsa.getFractionNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Fractions failed to load.\nError: " + caught.getMessage());					
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					fractionBox.clear();
					fractionBox.addItem("");
					int ctr = 0;
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							fractionBox.addItem(name);
						}
						ctr++;
					}
				} else {
					//Window.alert("There are no fractions loaded.\nCheck with administrator.");
				}
			}			
		});
		
	}

	/**
	 * Private method to load the feedstock suggest box.
	 */
	private void loadFeedstocks() {
		tsa.getFeedstockNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Feedstocks failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					Iterator<String> it = result.iterator();
					int ctr=0;
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty()) {
							if (ctr==0) {
								
							}
							feedstockOracle.add(name);
						}
						ctr++;
					}
				} else {
					//Window.alert("There are no feedstocks loaded.\nCheck with administrator.");
				}
			}			
		});		
	}

	/**
	 * Private method to load the status list box.
	 */
	private void loadStatuses() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-5");
			}

			@Override
			public void onSuccess(String result) {
				//Window.alert("From server:session="+result);
				sessionId = result;
				tsa.getStatusNames(sessionId, new AsyncCallback<Collection<String>>() {
		
					public void onFailure(Throwable caught) {
						//Window.alert("Statuses failed to load.\nError: " + caught.getMessage());	
					}
		
					public void onSuccess(Collection<String> result) {
						if (result != null && !result.isEmpty()) {
							statusBox.clear();
							statusBox.addItem("");
							Iterator<String> it = result.iterator();
							while (it.hasNext()) {
								String name = it.next();
								statusBox.addItem(name);
							}
						} else {
							//Window.alert("There are no statuses loaded.\nCheck with administrator.");
						}
					}			
				});
			}
		});
	}

	/**
	 * Private method to load the packagings list box.
	 */
	private void loadPackagings() {
		tsa.getPackaging(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Packagings failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							packagingOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the holders list box.
	 */
	private void loadHolders() {
		tsa.getHolders(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Holders failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							holderOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the shelves list box.
	 */
	private void loadShelves() {
		tsa.getShelves(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Shelves failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							shelfOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the sublocations list box.
	 */
	private void loadSublocations() {
		tsa.getSubLocations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Sublocations failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							sublocationOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the rooms list box.
	 */
	private void loadRooms() {
		tsa.getRooms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Rooms failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							roomOracle.add(name);
					}
				}
			}			
		});		
	}

	/**
	 * Private method to load the buildings list box.
	 */
	private void loadBuildings() {
		tsa.getBuildings(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Buildings failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if ((result != null) && (!result.isEmpty())) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						if (name != null && !name.isEmpty())
							buildingOracle.add(name);
					}
				}
			}			
		});		
	}

	/*
    private void checkUserSessionAlive()
    {
        tsa.getUserSessionTimeout(new AsyncCallback<Integer>()
        {
            public void onSuccess(Integer timeout)
            {
                sessionTimeoutResponseTimer.cancel();
                sessionTimeoutResponseTimer.schedule((int) (timeout+TIMEOUT_PAD));
            }

            public void onFailure(Throwable caught)
            {
                displaySessionTimedOut();
            }
        });
    }
    private void displaySessionTimedOut()
    {
        Window.alert(
                "Session Timeout.\nYour session has timed out.");
        String sessionId = Cookies.getCookie("SessionID");
		tsa.logoff(sessionId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Void result) {
			}			
		});
		Window.Location.replace("/tracker/login.jsp");
    }
          */
 
    /**
	 * Private method to initialize the services
	 */
	private void initServices() {
		tsa = (TrackerServiceAsync) GWT.create(TrackerService.class);		

		ServiceDefTarget endpoint = (ServiceDefTarget) tsa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + URL_TRACKER_SERVICE);
		/*
        tsa.getUserSessionTimeout(new AsyncCallback<Integer>()
                {
                    public void onSuccess(Integer timeout)
                    {
                    	//Window.alert("timeout="+timeout/(1000*60)+" minutes");
                        sessionTimeoutResponseTimer = new Timer()
                        {
                            @Override
                            public void run()
                            {
                                checkUserSessionAlive();
                            }
                        };
                        sessionTimeoutResponseTimer.schedule((int) (timeout+INITIAL_TIMEOUT_PAD));
                    }

                    public void onFailure(Throwable caught)
                    {
                        displaySessionTimedOut();
                    }
                });
        */
	}

	/**
	 * Private method to load the units list box.
	 */
	private void loadUnits() {
		tsa.getUnits(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert("Units failed to load.\nError: " + caught.getMessage());	
			}

			public void onSuccess(Collection<String> result) {
				if (result != null && !result.isEmpty()) {
					unitBox.clear();
					unitBox.addItem("");
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String name = it.next();
						unitBox.addItem(name);
					}
				} else {
					//Window.alert("There are no units loaded.\nCheck with administrator.");
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
				sessionId = result;
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
								originListBox.addItem(item);
							}
						}
					}			
				});
			}
		});
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
				attachFile.setName(name);
				
				// Add to attachmentList
				FileInfo info = new FileInfo();
				info.setName(name);
				info.setPath(path);
				info.setFilename(fileName);
				attachmentList.add(info);
				
				// Submit to server
				attachFile.setName("uploadFile");
				Hidden w1 = new Hidden();
				w1.setName("name");
				w1.setValue(name);
				Hidden w2 = new Hidden();
				w2.setName("path");
				w2.setValue(path);
				Hidden w3 = new Hidden();
				w3.setName("file");
				w3.setValue(fileName);
				sampleAttachmentsPanel.add(w1);
				sampleAttachmentsPanel.add(w2);
				sampleAttachmentsPanel.add(w3);
				attachmentFormPanel.submit();
			} else {
				Window.alert("Please select a file before adding.");
			}
		}		
	}
	
	/**
	 * Private <ClickHandler> to handle removing attachments from the sample
	 * 
	 * @author jalbersh
	 *
	 */
	private class RemoveAttachmentClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row=1;row<sampleGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)sampleGrid.getWidget(row, 0);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			final Integer iIndex = new Integer(index);
			if (index == -1) 
				Window.alert("Please select a file to remove.");
			else {
				// Delete the file on the server
				long attachmentId = 0;
				Iterator<FileInfo> it = attachmentList.iterator();
				while (it.hasNext()) {
					FileInfo fi = it.next();
					String attachmentIdString = fi.getAttachment_id();
					String gridId = sampleGrid.getText(index, ATTACHMENT_ID_COL);
					if (attachmentIdString.equalsIgnoreCase(gridId)) {
						attachmentList.remove(fi);
						try {
							attachmentId = Long.parseLong(fi.getAttachment_id());
						} catch (NumberFormatException e) {
							attachmentId = -1;
						}
						if (attachmentId != -1) {
							tsa.removeAttachment(attachmentId, null, new AsyncCallback<Boolean>() {
	
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Error removing file.");
								}
	
								@Override
								public void onSuccess(Boolean result) {
									sampleGrid.removeRow(iIndex);
									if (sampleGrid.getRowCount() == 1)
										sampleGrid.setVisible(false);
									removeButton.setEnabled(false);
									downloadButton.setEnabled(false);
								}
							});
						}
					}
				}
			}
			sampleGrid.setVisible(sampleGrid.getRowCount()>1);
		
		}		
	}
	
	/**
	 * Private method to load the available printers for printing labels.
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
					}
				} else {
					if (!DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
						Window.alert("No printers were found.\nYou will not be able to print labels.");
				}
			}			
		});
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
			for (int row=1;row<sampleGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)sampleGrid.getWidget(row, 0);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			removeButton.setEnabled(index != -1);
			downloadButton.setEnabled(index != -1);
			sampleGrid.setVisible(sampleGrid.getRowCount()>1);
		}		
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (!saved) {
			if (attachmentList != null && !attachmentList.isEmpty()) {
				Iterator<FileInfo> it = attachmentList.iterator();
				while (it.hasNext()) {
					FileInfo fileInfo = it.next();
					tsa.removeAttachment(Long.parseLong(fileInfo.getAttachment_id()), null, new AsyncCallback<Boolean>() {

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
			}
		}
	}
}
