package gov.nrel.nbc.tracker.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;

public class FilteredNumberTextBox extends FilteredTextBox {
		
	public FilteredNumberTextBox () {
		super();
		
		this.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String value = ((TextBox)event.getSource()).getValue();
				boolean isLetterOrDigit = isValid(value);
				if (isLetterOrDigit == false) {
					Window.alert("Bad text. Please use numerals.");
					((TextBox)event.getSource()).setValue("");
				}
			}
		});
	
	}
	
	public boolean isValid(String input) {
		boolean isValid = true;
		if (input != null && !input.isEmpty()) {
			isValid = input.matches(NUMERIC_PATTERN);
		}
		return isValid;
	}
}