package gov.nrel.nbc.labelprinting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Public class that contains the widgets to upload a TRB PDF.
 * 
 * @author bberry
 */
public class PrintAllDialog extends DialogBox implements AppConstants {
	
	// Constants

	private static final String DIALOG_BOX_HEIGHT = "200px";

	private static final String DIALOG_BOX_WIDTH = "600px";
	
	// Instance variables
	
	PrintAllDialog tud = null;
	
	FormPanel formPanel = new FormPanel();
	
	private boolean confirm = false;
	
	/**
	 * Public constructor
	 * 
	 * @param trbNum <String> TRB number
	 * @param trbPage <String> TRB page number
	 */
	public PrintAllDialog (final SearchSample ss,final int numberOfLabels) {
		
		this.tud = this;
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		formPanel.setSize(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
		
		this.add(formPanel);
		
		this.setText("Confirm Print");
		this.setSize(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
		this.setModal(true);
		this.setAnimationEnabled(true);
		
		this.setPopupPositionAndShow(new PositionCallback() {			

			public void setPosition(int offsetWidth, int offsetHeight) {

				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				if (top < 0)
					top = 0;
				if (left < 0)
					left = 0;
				tud.setPopupPosition(left, top);
			}			
		});
		
		Label fileLabel = new Label("Are you sure about printing labels for all "+numberOfLabels+" samples?");
		mainPanel.add(fileLabel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setSpacing(10);
		buttonPanel.setWidth("100%");
		
		Button uploadButton = new Button("Print All");
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (ss.getResultsCount() > 99) {
					Window.alert("Label count("+ss.getResultsCount()+" exceeds maximum permitted (100).");
				} else {
					setConfirm(true);
					Window.alert("Queuing all "+ss.getResultsCount()+" labels for printing");
					ss.printAllData(ss.getCritList(),ss.getResultsCount());
				}
				tud.hide();
			}
		});
		buttonPanel.add(uploadButton);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setConfirm(false);
				tud.hide();
			}
		});
		buttonPanel.add(cancelButton);
		
		mainPanel.add(buttonPanel);
		
		formPanel.add(mainPanel);
	}

	/**
	 * @param confirm the confirm to set
	 */
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the confirm
	 */
	public boolean isConfirm() {
		return confirm;
	}

}
