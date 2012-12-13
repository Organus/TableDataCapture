package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class WaitDialog extends DialogBox implements ClickHandler, AppConstants, DevTestProdConstants {

	private String msg;
	private static final String WAIT_MESSAGE = "Please wait...";
	private static final String WAIT_JPG = "hourglass1.jpg";
	private static final String DIALOG_BOX_HEIGHT = "100px";
	private static final String DIALOG_BOX_WIDTH = "100px";
	private WaitDialog wd = this;
	
	public WaitDialog(String msg) {
		//Window.alert("in WaitDialog constructor");
		this.msg = msg;
		display();
	}
	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
	public void display() {
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label waitLabel = new Label(WAIT_MESSAGE+msg);
		vp.add(waitLabel);
		Image hourGlass = new Image(WAIT_JPG);
		vp.add(hourGlass);
		this.setSize(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
		vp.setVisible(true);
		this.add(vp);
		this.setVisible(true);
		this.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 3;
				int top = (Window.getClientHeight() - offsetHeight) / 3;
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				wd.setPopupPosition(left, top);
			}			
		});
	}
}
