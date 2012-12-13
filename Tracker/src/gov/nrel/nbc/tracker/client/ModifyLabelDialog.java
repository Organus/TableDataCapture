package gov.nrel.nbc.tracker.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * A <DialogBox> that allows a user to modify the sample data.
 * 
 * @author jalbersh
 *
 */
public class ModifyLabelDialog extends DialogBox implements ClickHandler, AppConstants, DevTestProdConstants {
	private static final String HORIZONTAL_RULE = "<hr/>";
	private static final String DIALOG_BOX_HEIGHT = "500px";
	private static final String DIALOG_BOX_WIDTH = "100%";
	private static final String CANCEL_BUTTON_LABEL = "Cancel";
	private static final String SAVE_BUTTON_LABEL = "Save";
	private static final String OWNER_NAME_LABEL = "Owner Name:";
	private static final String CUSTODIAN_LABEL = "Custodian:";
	private static final String STRAIN_LABEL = "Strain:";
	private static final String DESTINATION_LABEL_ALGAE = "Project Task No:";
	private static final String DESTINATION_LABEL_SABC = "Destination:";
	private static final String SAMPLE_ID_LABEL = "Sample ID:";
	private static final String PAGE_LABEL = "TRB Page:";
	private static final String NUMBER_LABEL = "TRB Number:";
	private static final String PANEL_TITLE_LABEL = "Modify Label Data";
	private static final String MAIN_PANEL_HEIGHT = DIALOG_BOX_HEIGHT;
	private static final String MAIN_PANEL_WIDTH = DIALOG_BOX_WIDTH;
	private static final String URL_TRACKER_SERVICE = "trackerService";
	private static final String ENTRY_DATE = "Entry Date:";
	private static final String DATE_ENTERED_LABEL = "Date Created:";
	private static final String MATERIAL = "Material:";
	private static final String TRB_LABEL = "Trb:";
	private static final String FANCYLABEL_STYLE = "FancyLabel";
	private static final String BOLDFANCYLABEL_STYLE = "BoldFancyLabel";
	private static final String FANCYDESCLABEL_STYLE = "FancyDescLabel";
	private static final String FANCYPANEL_STYLE = "FancyPanel";
	private static final String LABEL_CONTENTS = "Label Contents";
	private static final String BLANK = "";
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String DASH = "-";
	private static final String DESCRIPTOR_WIDTH = "430px";
	private static final String DESCRIPTOR_HEIGHT = "70px";
	private static final int MAX_PER_LINE = 51;
	private static final int START_POSITION = 40;
	private static final String FRACTION_LABEL = "Fraction:";
	private static final String FORM_LABEL = "Storage State:";
	private static final String COMP_LABEL = "Composition:";
	private static final String FEEDSTOCK_LABEL = "Feedstock:";
	private static final String STATE_LABEL = "Treatment:";
	private static final String DESCRIPTOR_LABEL = "Label Descriptor:";
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
	 * TRB number <TextBox>
	 */
	private FilteredNumberTextBox trbNum = new FilteredNumberTextBox();
	
	/**
	 * TRB page number <TextBox>
	 */
	private FilteredNumberTextBox trbPage = new FilteredNumberTextBox();
	
	/**
	 * Sample ID
	 */
	private long sampleId = 0;
	
	private final Label labelMaterialBox1 = new Label(BLANK);
	private final Label labelComma1 = new Label(COMMA);
	private final Label labelMaterialBox2 = new Label(BLANK);
	private final Label labelComma2 = new Label(COMMA);
	private final Label labelMaterialBox3 = new Label(BLANK);
	/**
	 * Global reference to the <DialogBox>
	 */
	private final ModifyLabelDialog msd = this;

	/**
	 * Entered Date <Label>
	 */
	private final DateBox enteredDate = new DateBox();
	/**
	 * Tracking ID <Label>
	 */
	private final Label trackingIdLabel = new Label();

	/**
	 * Main panel <FlexTable>
	 */
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
	private final FilteredAlphaNumericTextBox descriptorBox = new FilteredAlphaNumericTextBox();
	private final Label descriptorLabel1 = new Label(SPACE);
	private final Label descriptorLabel2 = new Label(SPACE);
	private final Label descriptorLabel3 = new Label(SPACE);
	
	/**
	 * Oracle for the owner name
	 */
	private MultiWordSuggestOracle ownerOracle = new MultiWordSuggestOracle();
	
	/**
	 * Custodian Name <SuggestBox>
	 */
	private FilteredSuggestBox ownerBox = new FilteredSuggestBox(ownerOracle);
	
	Label dateValue = new Label();

	/**
	 * Custodian <ListBox>
	 */
	private final ListBox custBox = new ListBox();
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

	private Label labelNumBox = new Label();
	
	private Label labelPageBox = new Label();
	
	private Label labelNameBox = new Label();

	private Label labelDestBox = new Label();

	private Label labelOwnBox = new Label();
	
	private Label labelCustBox = new Label();
	
	private Label labelStrainBox = new Label();
	
	/**
	 * State <ListBox>
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
	 * Fraction <ListBox>
	 */
	private ListBox formBox = new ListBox();
	
	/**
	 * Composition <ListBox>
	 */
	private ListBox compBox = new ListBox();
	
	/**
	 * Destination <ListBox>
	 */
	private ListBox destBox = new ListBox();
	
	
	/**
	 * Strain <ListBox>
	 */
	private ListBox strainBox = new ListBox();
	
	
	/**
	 * <DialogBox> constructor
	 * 
	 * @param sampleIdString <String> Sample ID
	 */
	@SuppressWarnings("unchecked")
	public ModifyLabelDialog(String sampleIdString) {
		int row = 0;
		
		initServices();
		
		this.sampleIdString = sampleIdString;
		sampleId = Long.parseLong(sampleIdString);
		loadSampleData(sampleId);
		
		mainPanel.setSize(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
		//mainPanel.setCellSpacing(10);
		
		this.setText(PANEL_TITLE_LABEL);

		//HTML ruler1Label = new HTML(HORIZONTAL_RULE, false);
		//mainPanel.setWidget(row, 0, ruler1Label);
		//mainPanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		row += 3;;

		HorizontalPanel hpanel = new HorizontalPanel();
		
		FlexTable samplePanel = new FlexTable();
		FlexTable labelPanel = new FlexTable();
		
		samplePanel.setCellPadding(1);
		
		Label nameLabel = new Label(SAMPLE_ID_LABEL);
		samplePanel.setWidget(row, 0, nameLabel);

		samplePanel.setWidget(row++, 1, sampleIdBox);
		sampleIdBox.addKeyDownHandler(new MyValueChangeHandler());
		sampleIdBox.addSelectionHandler(new MySelectionHandler());
		loadSampleIds();
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label ownerLabel = new Label(OWNER_NAME_LABEL);
			ownerLabel.setWordWrap(false);
			samplePanel.setWidget(row, 0, ownerLabel);
			samplePanel.setWidget(row++, 1, ownerBox);
			ownerBox.addKeyDownHandler(new MyValueChangeHandler());
			ownerBox.addSelectionHandler(new MySelectionHandler());
			loadOwnerNames();
		} else {
			Label ownerLabel = new Label(CUSTODIAN_LABEL);
			ownerLabel.setWordWrap(false);
			samplePanel.setWidget(row, 0, ownerLabel);			
			samplePanel.setWidget(row++, 1, custBox);
			custBox.addKeyDownHandler(new MyValueChangeHandler());
			custBox.addChangeHandler(new MyChangeHandler());
			loadCustodians();
		}
		
		Label descriptorLabel = new Label(DESCRIPTOR_LABEL);
		samplePanel.setWidget(row, 0, descriptorLabel);

		descriptorBox.addChangeHandler(new MyChangeHandler());
		samplePanel.setWidget(row++, 1, descriptorBox);

		Label trbNumLabel = new Label(NUMBER_LABEL);
		samplePanel.setWidget(row, 0, trbNumLabel);

		samplePanel.setWidget(row++, 1, trbNum);
		trbNum.addChangeHandler(new MyChangeHandler());
		
		Label trbPageLabel = new Label(PAGE_LABEL);
		samplePanel.setWidget(row, 0, trbPageLabel);

		samplePanel.setWidget(row++, 1, trbPage);
		trbPage.addChangeHandler(new MyChangeHandler());

		HTML ruler2Label = new HTML(HORIZONTAL_RULE, false);
		samplePanel.setWidget(row, 0, ruler2Label);
		samplePanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label feedstockLabel = new Label(FEEDSTOCK_LABEL);
			samplePanel.setWidget(row, 0, feedstockLabel);
	
			samplePanel.setWidget(row++, 1, feedstockBox);
			feedstockBox.addSelectionHandler(new MySelectionHandler());
			loadFeedstocks();
		
			Label stateLabel = new Label(STATE_LABEL);
			samplePanel.setWidget(row, 0, stateLabel);
	
			samplePanel.setWidget(row++, 1, treatmentBox);
			treatmentBox.addChangeHandler(new MyChangeHandler());
			loadStates();
		}
		
		Label fractionLabel = new Label(FRACTION_LABEL);
		samplePanel.setWidget(row, 0, fractionLabel);

		samplePanel.setWidget(row++, 1, fractionBox);
		fractionBox.addChangeHandler(new MyChangeHandler());
		loadFraction();

		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)){
			Label formLabel = new Label(FORM_LABEL);
			samplePanel.setWidget(row, 0, formLabel);

			samplePanel.setWidget(row++, 1, formBox);
			formBox.addChangeHandler(new MyChangeHandler());
			loadForm();

			Label compLabel = new Label(COMP_LABEL);
			samplePanel.setWidget(row, 0, compLabel);

			samplePanel.setWidget(row++, 1, compBox);
			compBox.addChangeHandler(new MyChangeHandler());
			loadComposition();

			Label destLabel = new Label();
			//destLabel.setText(DESTINATION_LABEL_SABC);
		//} else {
			destLabel.setText(DESTINATION_LABEL_ALGAE);				
		//}
			samplePanel.setWidget(row, 0, destLabel);

			samplePanel.setWidget(row++, 1, destBox);
			destBox.addChangeHandler(new MyChangeHandler());
			loadDestinations();

			Label strainLabel = new Label(STRAIN_LABEL);
			samplePanel.setWidget(row, 0, strainLabel);
	
			samplePanel.setWidget(row++, 1, strainBox);
			strainBox.addChangeHandler(new MyChangeHandler());
			loadStrains();

		}

		Label enteredLabel = new Label(DATE_ENTERED_LABEL);
		enteredLabel.setWordWrap(false);
		samplePanel.setWidget(row, 0, enteredLabel);

		samplePanel.setWidget(row++, 1, enteredDate);
		
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
	
			Label fireLabel = new Label(FIRE_LABEL);
			samplePanel.setWidget(row, 0, fireLabel);
	
			loadFire();
			samplePanel.setWidget(row++, 1, fireBox);
			fireBox.addChangeHandler(new MyChangeHandler());
	
			Label reactivityLabel = new Label(REACTIVITY_LABEL);
			samplePanel.setWidget(row, 0, reactivityLabel);
	
			loadReactivity();
			samplePanel.setWidget(row++, 1, reactivityBox);
			reactivityBox.addChangeHandler(new MyChangeHandler());
	
			Label specificLabel = new Label(SPECIFIC_LABEL);
			samplePanel.setWidget(row, 0, specificLabel);
	
			loadSpecific();
			samplePanel.setWidget(row++, 1, specificBox);
			specificBox.addChangeHandler(new MyChangeHandler());
	
			Label healthLabel = new Label(HEALTH_LABEL);
			samplePanel.setWidget(row, 0, healthLabel);
	
			loadHealth();
			samplePanel.setWidget(row++, 1, healthBox);
			healthBox.addChangeHandler(new MyChangeHandler());
		}

		hpanel.add(samplePanel);
		hpanel.setCellHorizontalAlignment(samplePanel, HasHorizontalAlignment.ALIGN_LEFT);
		hpanel.setCellVerticalAlignment(samplePanel, HasVerticalAlignment.ALIGN_MIDDLE);
				
		hpanel.add(labelPanel);
		hpanel.setCellHorizontalAlignment(labelPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hpanel.setCellVerticalAlignment(labelPanel, HasVerticalAlignment.ALIGN_MIDDLE);
				
		mainPanel.add(hpanel);
		hpanel.setCellHorizontalAlignment(hpanel, HasHorizontalAlignment.ALIGN_CENTER);
		hpanel.setCellVerticalAlignment(hpanel, HasVerticalAlignment.ALIGN_TOP);

		FlexTable h1panel = new FlexTable();
		h1panel.setCellPadding(10);
		mainPanel.add(h1panel);
		hpanel.setCellHorizontalAlignment(h1panel, HasHorizontalAlignment.ALIGN_CENTER);
		hpanel.setCellVerticalAlignment(h1panel, HasVerticalAlignment.ALIGN_BOTTOM);
		
		HorizontalPanel namePanel = new HorizontalPanel();
		Label sampleLabel = new Label(SAMPLE_ID_LABEL);
		sampleLabel.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(sampleLabel);

		labelNameBox.addStyleDependentName(FANCYLABEL_STYLE);
		namePanel.add(labelNameBox);

		HorizontalPanel destPanel = new HorizontalPanel();
		HorizontalPanel strainPanel = new HorizontalPanel();
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)){
			Label labelDestLabel = new Label();
			//labelDestLabel.setText(DESTINATION_LABEL_SABC);
		//} else {
			labelDestLabel.setText(DESTINATION_LABEL_ALGAE);				
		//}
			labelDestLabel.addStyleDependentName(FANCYLABEL_STYLE);
			destPanel.add(labelDestLabel);

			labelDestBox.addStyleDependentName(FANCYLABEL_STYLE);
			destPanel.add(labelDestBox);
			destPanel.setStyleName(FANCYPANEL_STYLE);

			Label labelStrainLabel = new Label(STRAIN_LABEL);
			labelStrainLabel.addStyleDependentName(FANCYLABEL_STYLE);
			strainPanel.add(labelStrainLabel);
			strainPanel.setStyleName(FANCYPANEL_STYLE);
	
			labelStrainBox.addStyleDependentName(FANCYLABEL_STYLE);
			strainPanel.add(labelStrainBox);
		}
		HorizontalPanel labelDatePanel = new HorizontalPanel();
		Label dateLabel = new Label(ENTRY_DATE);
		dateLabel.addStyleDependentName(FANCYLABEL_STYLE);
		dateValue.addStyleDependentName(FANCYLABEL_STYLE);
		labelDatePanel.setStyleName(FANCYPANEL_STYLE);
		labelDatePanel.add(dateLabel);
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

		Label contentsLabel = new Label(LABEL_CONTENTS);
		contentsLabel.addStyleDependentName(BOLDFANCYLABEL_STYLE);
		labelPanel.getCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		labelPanel.setWidget(row, 0, contentsLabel);
		row++;
		
		HorizontalPanel materialPanel = new HorizontalPanel();
		Label materialLabel = new Label(MATERIAL);
		materialLabel.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(materialLabel);
		labelMaterialBox1.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox1);
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			materialPanel.add(labelComma1);
		}
		labelMaterialBox2.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox2);
		//materialPanel.add(labelComma2);
		labelMaterialBox3.addStyleDependentName(FANCYLABEL_STYLE);
		materialPanel.add(labelMaterialBox3);
		
		HorizontalPanel labelFirePanel = new HorizontalPanel();
		Label fireLabel1 = new Label(FIRE_LABEL);
		fireLabel1.addStyleDependentName(FANCYLABEL_STYLE);
		labelFirePanel.add(fireLabel1);
		labelFirePanel.setStyleName(FANCYPANEL_STYLE);
		labelFirePanel.addStyleDependentName(FANCYLABEL_STYLE);
		fireBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelFirePanel.add(fireBoxLabel);

		HorizontalPanel labelReactivityPanel = new HorizontalPanel();
		Label reactivityLabel1 = new Label(REACTIVITY_LABEL);
		reactivityLabel1.addStyleDependentName(FANCYLABEL_STYLE);
		labelReactivityPanel.add(reactivityLabel1);
		labelReactivityPanel.setStyleName(FANCYPANEL_STYLE);
		labelReactivityPanel.addStyleDependentName(FANCYLABEL_STYLE);
		reactivityBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelReactivityPanel.add(reactivityBoxLabel);

		HorizontalPanel labelSpecificPanel = new HorizontalPanel();
		Label specificLabel1 = new Label(SPECIFIC_LABEL);
		specificLabel1.addStyleDependentName(FANCYLABEL_STYLE);
		labelSpecificPanel.add(specificLabel1);
		labelSpecificPanel.setStyleName(FANCYPANEL_STYLE);
		labelSpecificPanel.addStyleDependentName(FANCYLABEL_STYLE);
		specificBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelSpecificPanel.add(specificBoxLabel);

		HorizontalPanel labelHealthPanel = new HorizontalPanel();
		Label healthLabel1 = new Label(HEALTH_LABEL);
		healthLabel1.addStyleDependentName(FANCYLABEL_STYLE);
		labelHealthPanel.add(healthLabel1);
		labelHealthPanel.setStyleName(FANCYPANEL_STYLE);
		labelHealthPanel.addStyleDependentName(FANCYLABEL_STYLE);
		healthBoxLabel.addStyleDependentName(FANCYLABEL_STYLE);
		labelHealthPanel.add(healthBoxLabel);

		HorizontalPanel labelOwnPanel = new HorizontalPanel();
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Label ownLabel = new Label(OWNER_NAME_LABEL);
			ownLabel.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(ownLabel);
			labelOwnPanel.setStyleName(FANCYPANEL_STYLE);
			labelOwnBox.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(labelOwnBox);
		} else {
			Label ownLabel = new Label(CUSTODIAN_LABEL);
			ownLabel.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(ownLabel);
			labelOwnPanel.setStyleName(FANCYPANEL_STYLE);
			labelCustBox.addStyleDependentName(FANCYLABEL_STYLE);
			labelOwnPanel.add(labelCustBox);
		}

		int labelRow = 0;
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
		mainPanel.addStyleName(FANCYPANEL_STYLE);
		labelDescriptorBox.add(descriptorLabel1);
		labelDescriptorBox.add(descriptorLabel2);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE))
			labelDescriptorBox.add(descriptorLabel3);
		labelPanel.addStyleName(FANCYPANEL_STYLE);
		labelPanel.setWidget(labelRow++, 0, labelDescriptorBox);
		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE))
			labelPanel.setWidget(labelRow++, 0, trbPanel);
		labelPanel.setWidget(labelRow++, 0, materialPanel);
		labelPanel.setWidget(labelRow++, 0, strainPanel);
		labelPanel.setWidget(labelRow++, 0, namePanel);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelPanel.setWidget(labelRow++, 0, trbPanel);
		} else {
			labelPanel.setWidget(labelRow++, 0, destPanel);
		}
		labelPanel.setWidget(labelRow++, 0, labelDatePanel);
		labelPanel.setWidget(labelRow++, 0, labelOwnPanel);
		if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			labelPanel.setWidget(labelRow++, 0, labelFirePanel);
			labelPanel.setWidget(labelRow++, 0, labelReactivityPanel);
			labelPanel.setWidget(labelRow++, 0, labelSpecificPanel);
			labelPanel.setWidget(labelRow++, 0, labelHealthPanel);
		}
		
		//HTML ruler5Label = new HTML(HORIZONTAL_RULE, false);
		//labelPanel.setWidget(row, 0, ruler5Label);
		//labelPanel.getFlexCellFormatter().setColSpan(row++, 0, 3);
		
		Button saveButton = new Button(SAVE_BUTTON_LABEL);
		h1panel.setWidget(0, 0, saveButton);
		//h1panel.add(saveButton);
		//h1panel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_LEFT);
		//h1panel.setCellVerticalAlignment(saveButton, HasVerticalAlignment.ALIGN_MIDDLE);
		//labelPanel.setWidget(row, 0, saveButton);
		//labelPanel.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saveSample();
			}			
		});
		
		Button cancelButton = new Button(CANCEL_BUTTON_LABEL);
		h1panel.setWidget(0, 25, cancelButton);
		//h1panel.add(cancelButton);
		//h1panel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_RIGHT);
		//h1panel.setCellVerticalAlignment(cancelButton, HasVerticalAlignment.ALIGN_MIDDLE);
		//labelPanel.setWidget(row, 2, cancelButton);
		//labelPanel.getFlexCellFormatter().setHorizontalAlignment(row++, 0, HasHorizontalAlignment.ALIGN_CENTER);
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				msd.hide();
			}			
		});
		
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
	
	/**
	 * Private method to save the sample data to the database
	 *  through calls to the server.
	 */
	public String getStackTrace(Throwable t)
    {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] elems = t.getStackTrace();
        for (int i=0;i<elems.length;i++) {
        	StackTraceElement elem = elems[i];
        	String line = elem.getMethodName()+":"+elem.getLineNumber();
        	if (i>0) sb.append("\n");
        	sb.append(line);
        }
        return sb.toString();
    }

	private void saveSample() {
		try {
		//Window.alert("in saveSample");
		if (sampleIdBox.isValid()) {
			//Window.alert("1");
			theSample.setSampleId(sampleIdBox.replaceQuotes(sampleIdBox.getValue()));
		}
		else {
			Window.alert("The Sample ID is not valid.");
			return;
		}
		
		if (descriptorBox.isValid()) {
			//Window.alert("2");
			theSample.setLabelDescription(descriptorBox.replaceQuotes(descriptorBox.getValue()));
		}
		else {
			Window.alert("The Label Descriptor is not valid.");
			return;
		}
		
		if (ownerBox.isValid()) {
			//Window.alert("3");
			theSample.setOwnerName(ownerBox.replaceQuotes(ownerBox.getValue()));
		}
		else {
			Window.alert("The Owner Name is not valid.");
			return;
		}
		
		if (treatmentBox != null && treatmentBox.getSelectedIndex() != -1) {
			//Window.alert("4");
			theSample.setTreatment(treatmentBox.getItemText(treatmentBox.getSelectedIndex()));
		}
		
		if (feedstockBox != null && feedstockBox.isValid()) {
			//Window.alert("5");
			theSample.setFeedstock(feedstockBox.replaceQuotes(feedstockBox.getValue()));
		}
		else {
			Window.alert("The Feedstock is not valid.");
			return;
		}
		
		if (fractionBox != null && fractionBox.getSelectedIndex() != -1) {
			//Window.alert("6");
			theSample.setFraction(fractionBox.getItemText(fractionBox.getSelectedIndex()));
		}
		if (formBox != null && formBox.getSelectedIndex() != -1) {
			//Window.alert("7");
			theSample.setForm(formBox.getItemText(formBox.getSelectedIndex()));
		}
		if (compBox != null && compBox.getSelectedIndex() != -1) {
			//Window.alert("8");
			theSample.setComposition(compBox.getItemText(compBox.getSelectedIndex()));
		}
		if (destBox != null && destBox.getSelectedIndex() != -1) {
			//Window.alert("9");
			theSample.setDestination(destBox.getItemText(destBox.getSelectedIndex()));
		}
		if (strainBox != null && strainBox.getSelectedIndex() != -1) {
			//Window.alert("10");
			theSample.setStrain(strainBox.getItemText(strainBox.getSelectedIndex()));
		}
		if (fireBox != null && fireBox.getSelectedIndex() != -1) {
			//Window.alert("11");
			theSample.setFire(fireBox.getItemText(fireBox.getSelectedIndex()));
		}
		if (reactivityBox != null && reactivityBox.getSelectedIndex() != -1) {
			//Window.alert("12");
			theSample.setReactivity(reactivityBox.getItemText(reactivityBox.getSelectedIndex()));
		}
		if (specificBox != null && specificBox.getSelectedIndex() != -1) {
			//Window.alert("13");
			theSample.setSpecific(specificBox.getItemText(specificBox.getSelectedIndex()));
		}
		if (healthBox != null && healthBox.getSelectedIndex() != -1) {
			//Window.alert("14");
			theSample.setHealth(healthBox.getItemText(healthBox.getSelectedIndex()));
		}
		
		tsa.saveLabel(theSample, new AsyncCallback<Long>() {

			public void onFailure(Throwable caught) {
				Window.alert("The label was not saved.\nAn error was thrown: " + caught.getMessage());
			}

			public void onSuccess(Long result) {
				if (result > 0) {
					Window.alert("The label was saved.");
					msd.hide();
				} else
					Window.alert("The label was not saved.");
			}			
		});
		} catch (Exception e1) {
			Window.alert("Exception caught: "+e1.getMessage());
		}
	}
	
	/**
	 * Protected method to set the selected item in a list box
	 *  by value.
	 */
	protected void setListBox(String value, ListBox listBox) {
		if (value != null) {
			for (int i = 0; i < listBox.getItemCount(); i++) {
				if (value.equalsIgnoreCase(listBox.getItemText(i))) {
					listBox.setSelectedIndex(i);
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
				fractionBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							fractionBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getFraction(), fractionBox);
					labelMaterialBox3.setText(constructMaterial());
				}
			}			
		});		
	}

	/**
	 * Private method to populate the fraction data
	 */
	private void loadForm() {
		tsa.getForms(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Forms failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				formBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							formBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getForm(), formBox);
					labelMaterialBox3.setText(constructMaterial());
				}
			}			
		});		
	}

	/**
	 * Private method to populate the Composition data
	 */
	private void loadComposition() {
		tsa.getCompositions(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Compositions failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				compBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							compBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getComposition(), compBox);
					labelMaterialBox3.setText(constructMaterial());
				}
			}			
		});		
	}

	/**
	 * Private method to populate the Destination data
	 */
	private void loadDestinations() {
		tsa.getDestinations(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Destinations failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				destBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							destBox.addItem(item);
					}
				}
				if (theSample != null)
					setListBox(theSample.getDestination(), destBox);
			}			
		});		
	}

	/**
	 * Private method to populate the Strain data
	 */
	private void loadStrains() {
		tsa.getStrains(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("Strains failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				strainBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							strainBox.addItem(item);
					}
				}
				if (theSample != null)
					setListBox(theSample.getStrain(), strainBox);
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
	 * Private method to populate the state data
	 */
	private void loadStates() {
		tsa.getTreatmentNames(new AsyncCallback<Collection<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert("States failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(Collection<String> result) {
				treatmentBox.addItem("");
				if (!result.isEmpty()) {
					Iterator<String> it = result.iterator();
					while (it.hasNext()) {
						String item = it.next();
						if (item != null && !item.isEmpty())
							treatmentBox.addItem(item);
					}
				}
				if (theSample != null) {
					setListBox(theSample.getTreatment(), treatmentBox);
					labelMaterialBox3.setText(constructMaterial());
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
	 * Private method to populate the custodian data
	 */
	private void loadCustodians() {
		tsa.getSessionId(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Shouldn't get to here-2");
			}

			@Override
			public void onSuccess(final String sessionId) {
						tsa.getCustodians(sessionId, new AsyncCallback<Collection<String>>() {
				
							public void onFailure(Throwable caught) {
								Window.alert("Custodians failed to load.\nDatabase may not be available.");				
							}
				
							public void onSuccess(Collection<String> result) {
								if (result != null && !result.isEmpty()) {
									custBox.clear();
									custBox.addItem("");
									Iterator<String> it = result.iterator();
									while (it.hasNext()) {
										String suggestion = it.next();
										if (suggestion != null)
											custBox.addItem(suggestion);
									}
									if (theSample != null)
										setListBox(theSample.getCustodianName(), custBox);
								}
							}			
						});		
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
	private void loadSampleData(long sampleId2) {
		tsa.findSampleById(sampleId2, new AsyncCallback<SampleCriteria>() {

			public void onFailure(Throwable caught) {
				Window.alert("The sample failed to load.\nDatabase may not be available.");				
			}

			public void onSuccess(SampleCriteria result) {
				if (result != null) {
					theSample = result;
					trackingIdLabel.setText(String.valueOf(theSample.getTrackingId()));
					sampleIdBox.setValue(theSample.getSampleId());
					feedstockBox.setValue(theSample.getFeedstock());
					if (theSample != null)
						setListBox(theSample.getFraction(), fractionBox);
					if (theSample != null)
						setListBox(theSample.getTreatment(), treatmentBox);
					if (theSample != null && theSample.getLabelDescription() != null) {
						descriptorBox.setValue(theSample.getLabelDescription());
						changeLabel(descriptorBox);
					}
					if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
						ownerBox.setValue(theSample.getOwnerName());
					} else {
						String name = theSample.getCustodianName();
						int num = custBox.getItemCount();
						for (int ctr=0;ctr<num;ctr++) {
							String val = custBox.getItemText(ctr);
							if (val.equals(name)) {
								custBox.setItemSelected(ctr, true);
								break;
							}
						}
					}
					if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
						/*
						labelMaterialBox1.setText(theSample.getFeedstock());
						labelMaterialBox2.setText(theSample.getTreatment());
						if (theSample.getFeedstock() == null || theSample.getFeedstock().isEmpty())
							labelComma1.setText("");
						if (theSample.getTreatment() == null || theSample.getTreatment().isEmpty())
							labelComma2.setText("");
						labelMaterialBox3.setText(theSample.getFraction());
						*/
						labelMaterialBox3.setText(constructMaterial());
					} else {
						if (theSample != null)
							setListBox(theSample.getForm(), formBox);
						if (theSample != null)
							setListBox(theSample.getComposition(), compBox);
						if (theSample != null)
							setListBox(theSample.getDestination(), destBox);
						if (theSample != null)
							setListBox(theSample.getStrain(), strainBox);
						labelMaterialBox1.setText("biomass");
						/*
						//labelMaterialBox2.setText(theSample.getForm());
						if (theSample.getForm() == null || theSample.getForm().isEmpty())
							labelComma1.setText("");
						if (theSample.getFraction() == null || theSample.getFraction().isEmpty())
							labelComma2.setText("");
						String fr = theSample.getFraction();
						if (theSample.getComposition() != null && !theSample.getComposition().isEmpty())
							fr += "," + theSample.getComposition();
						labelMaterialBox3.setText(fr);
						*/
						labelMaterialBox3.setText(constructMaterial());
					}
					labelNameBox.setText(theSample.getSampleId());
					labelOwnBox.setText(theSample.getOwnerName());
					labelCustBox.setText(theSample.getCustodianName());
					labelNumBox.setText(String.valueOf(theSample.getTrbNum()));
					labelPageBox.setText(String.valueOf(theSample.getTrbPage()));
					labelDestBox.setText(String.valueOf(theSample.getDestination()));
					labelStrainBox.setText(String.valueOf(theSample.getStrain()));
					if (theSample != null) {
						setShortListBox(theSample.getFire(), fireBox);
						fireBoxLabel.setText(theSample.getFire());
					}
					if (theSample != null) {
						setShortListBox(theSample.getReactivity(), reactivityBox);
						reactivityBoxLabel.setText(theSample.getReactivity());
					}
					if (theSample != null) {
						setShortListBox(theSample.getSpecific(), specificBox);
						specificBoxLabel.setText(theSample.getSpecific());
					}
					if (theSample != null) {
						setShortListBox(theSample.getHealth(), healthBox);
						healthBoxLabel.setText(theSample.getHealth());
					}					
					if (theSample.getTrbNum() != 0) {
						trbNum.setText(String.valueOf(theSample.getTrbNum()));
					}
					if (theSample.getTrbPage() != 0) {
						trbPage.setText(String.valueOf(theSample.getTrbPage()));
					}
					
					Date date = theSample.getStartCreateDate();
					String sdate = date.toString();
					if (sdate != null && !sdate.isEmpty()) dateValue.setText(sdate);
					else dateValue.setText("");
					if (sdate != null && !sdate.isEmpty()) enteredDate.setValue(date);
				} else {
					Window.alert("The sample could not be found.\nCheck with administrator.");
				}
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
				labelMaterialBox3.setText(constructMaterial());
				//if (val != null && !val.isEmpty()) 
				//	labelComma1.setText(COMMA);
				//else
				//	labelComma1.setText("");
			} else if (sb == ownerBox) {
				val = ownerBox.getValue();
				labelOwnBox.setText(val);
			} else if (sb == sampleIdBox) {
				val = sampleIdBox.getValue();
				labelNameBox.setText(val);				
			}
		} else if (obj instanceof ListBox) {
			ListBox lb = (ListBox)obj;
			int index = lb.getSelectedIndex();
			if (index == -1) 
				index = 0;
			val = lb.getItemText(index);
			if (lb == formBox || lb == fractionBox || lb == compBox || lb == treatmentBox) {
				labelMaterialBox3.setText(constructMaterial());
/*			} else if (lb == fractionBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == compBox) {
				labelMaterialBox3.setText(constructMaterial());
			} else if (lb == treatmentBox) {
				labelMaterialBox3.setText(constructMaterial());
				//if (val != null && !val.isEmpty()) 
				//	labelComma2.setText(COMMA);
				//else
				//	labelComma2.setText("");
*/			} else if (lb == fireBox) {
				if (val != null && !val.isEmpty())
					fireBoxLabel.setText(val);
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
			} else if (lb == destBox) {
				if (val != null && !val.isEmpty())
					labelDestBox.setText(val);
				else
					labelDestBox.setText(BLANK);
			} else if (lb == strainBox) {
				if (val != null && !val.isEmpty())
					labelStrainBox.setText(val);
				else
					labelStrainBox.setText(BLANK);
			} else if (lb == custBox) {
				val = custBox.getItemText(custBox.getSelectedIndex());
				labelCustBox.setText(val);
			}
		}    	
    }
     private String constructMaterial() {
    	String val = "";
		String comma = "";
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			String form = "";
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
		} else {
			val = "";
			String feedstock = "";
			String treatment = "";
			String fraction = "";
			//Window.alert("labelComma2="+labelComma2);
			//if (labelComma2.getText().isEmpty() && (!feedstockBox.getValue().equals("") || treatmentBox.getSelectedIndex() != -1 || fractionBox.getSelectedIndex() != -1))
				comma = ",";
			//Window.alert("feedstock="+feedstockBox.getValue());
			if (!feedstockBox.getText().equals(""))
				feedstock = feedstockBox.getValue();
			//Window.alert("treatment="+treatmentBox.getItemText(treatmentBox.getSelectedIndex()));
			if (treatmentBox.getSelectedIndex() != -1)
				treatment = treatmentBox.getItemText(treatmentBox.getSelectedIndex());
			//Window.alert("fraction="+fractionBox.getItemText(fractionBox.getSelectedIndex()));
			if (fractionBox.getSelectedIndex() != -1)
				fraction = fractionBox.getItemText(fractionBox.getSelectedIndex());
			if (!feedstock.isEmpty())
				val += feedstock;
			if (!treatment.isEmpty()) {
				if (!val.isEmpty()) val += comma;
				val += treatment;
			}
			if (!fraction.isEmpty()) {
				if (!val.isEmpty()) val += comma;
				val += fraction;
			}
			//Window.alert("val="+val);
		}
    	return val;
    }
    /*
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
		
    	return val;
    }
    */
	private String transformSpecific(String input) {
		int dash = 0;
		String value = "";
		if (input == null || input.isEmpty()) return "";
		dash = input.indexOf(" - ");
		if (dash != -1)
			value = input.substring(0,dash);
		return value;
	}
	@SuppressWarnings("unchecked")
	public class MySelectionHandler implements SelectionHandler {

		@Override
		public void onSelection(SelectionEvent event) {
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
