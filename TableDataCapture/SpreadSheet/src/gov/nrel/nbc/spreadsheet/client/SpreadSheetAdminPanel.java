package gov.nrel.nbc.spreadsheet.client;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

public class SpreadSheetAdminPanel extends Composite implements AppConstants {
	
	private static final String PERCENT_100 = "100%";
	private static final int MENU_HEIGHT = 230;

	private static final String SUB_TITLE_LABEL_STYLE = "SubTitle";
	private FlexTable mainPanel = new FlexTable();

	public SpreadSheetAdminPanel() {
		super();
		
		mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
		
		initWidget(mainPanel);
		
		mainPanel.setWidth(PERCENT_100);
		
		int row = 0;
		int column = 0;
		
		Label titleLabel = new Label("Spreadsheet Administration");
		titleLabel.addStyleDependentName(SUB_TITLE_LABEL_STYLE);
		mainPanel.setWidget(row, column, titleLabel);
		mainPanel.getFlexCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER);

		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				mainPanel.setHeight(String.valueOf(Window.getClientHeight()-MENU_HEIGHT)+"px");
			}
			
		});
	}
}
