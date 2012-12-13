package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Public class holding the title panel elements.
 * 
 * @author James Albersheim
 *
 */
public class TitlePanel extends Composite {

	private static final int PANEL_SPACING = 10;
	private static final String NREL_LOGO_JPG = "nrel_logo_25.JPG";
	private static final String TITLE_STYLE = "Title";
	private static final String TITLE_STRING = "Spreadsheet Admin\nConfiguration System";
	private static final String VERSION_STYLE = "Small";

	private final HorizontalPanel titlePanel = new HorizontalPanel();

	public TitlePanel() {
		initWidget(titlePanel);

		titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Image img = new Image(NREL_LOGO_JPG);
		titlePanel.add(img);
		titlePanel.setCellHorizontalAlignment(img,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		VerticalPanel vPanel = new VerticalPanel();
		
		Label title = new Label(TITLE_STRING);
		title.addStyleDependentName(TITLE_STYLE);
		vPanel.add(title);
		
		Label version = new Label("V4.1 (Spreadsheet Modify - Build @buildNum@ - Built on @buildDate@");		
		version.addStyleName(VERSION_STYLE);
		version.addStyleDependentName(VERSION_STYLE);
		vPanel.add(version);

		titlePanel.add(vPanel);
		titlePanel.setCellHorizontalAlignment(vPanel, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(vPanel,HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setSpacing(PANEL_SPACING);
	}
}
