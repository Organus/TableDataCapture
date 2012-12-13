package gov.nrel.nbc.tracker.client;

import gov.nrel.nbc.tracker.client.DevTestProdConstants;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Composite panel to contain the title bar.
 * 
 * @author jalbersh
 *
 */
public class TitlePanel extends Composite {
	
	private static final int PANEL_SPACING = 10;
	private static final String SABC_LOGO_JPG = "sabc_logo.jpg";//"Algae.jpg";
	private static final String ALGAE_LOGO_JPG = "algae2.jpg";//"Algae.jpg";
	private static final String NREL_LOGO_JPG = "nrel_logo_large.jpg";//"nrel_logo_25.JPG";
	private static final String ECOPETROL_LOGO_JPG = "ecopetrolLogo.jpg";//"nrel_logo_25.JPG";
	private static final String TITLE_STYLE = "Title";
	private static final String SABC_TITLE_STRING = "SABC Sample Tracking System";
	private static final String ALGAE_TITLE_STRING = "Algae Sample Tracking System";
	private static final String BIOMASS_TITLE_STRING = "Biomass Sample Tracking System";
	private static final String ECOPETROL_TITLE_STRING = "Ecopetrol Sample Tracking System";
	private static final String VERSION_STYLE = "Small";
	
	/**
	 * Main panel <HorizontalPanel>
	 */
	private final HorizontalPanel titlePanel = new HorizontalPanel();

	/**
	 * Constructor
	 */
	public TitlePanel() {
		initWidget(titlePanel);
		Image img = null;
		Label version = null;
		Label title = null;
		titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.BIOMASS)) {
			version = new Label("V2.0 - Build @buildNum@ - Built on @buildDate@");
			if (DevTestProdConstants.CRADA.equals(DevTestProdConstants.ECOPETROL)){
				title = new Label(ECOPETROL_TITLE_STRING);
				img = new Image(ECOPETROL_LOGO_JPG);				
				img.setHeight("100px");
				img.setWidth("280px");
			} else {
				title = new Label(BIOMASS_TITLE_STRING);
				img = new Image(NREL_LOGO_JPG);
				img.setHeight("100px");
				img.setWidth("280px");
			}
		} else if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)){
			img = new Image(ALGAE_LOGO_JPG);
			img.setHeight("100px");
			img.setWidth("224px");
			version = new Label("Algae Customization - Build @buildNum@ - Built on @buildDate@");		
			title = new Label(ALGAE_TITLE_STRING);
		} //else if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)){
		//	img = new Image(SABC_LOGO_JPG);
		//	img.setHeight("70px");
		//	img.setWidth("184px");
		//	version = new Label("SABC Customization - Build @buildNum@ - Built on @buildDate@");		
		//	title = new Label(SABC_TITLE_STRING);
		//}
		titlePanel.add(img);
		titlePanel.setCellHorizontalAlignment(img,
				HasHorizontalAlignment.ALIGN_LEFT);
		
		VerticalPanel vPanel = new VerticalPanel();
		
		title.addStyleDependentName(TITLE_STYLE);
		vPanel.add(title);
		
		version.addStyleName(VERSION_STYLE);
		version.addStyleDependentName(VERSION_STYLE);
		vPanel.add(version);
		titlePanel.add(vPanel);

		//if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			Image img1 = new Image(NREL_LOGO_JPG);
			titlePanel.add(img1);
			img1.setHeight("100px");
			img1.setWidth("280px");
			titlePanel.setCellHorizontalAlignment(img1,
					HasHorizontalAlignment.ALIGN_RIGHT);
		//}

		titlePanel.setCellHorizontalAlignment(vPanel, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(vPanel,HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setSpacing(PANEL_SPACING);
	}
}
