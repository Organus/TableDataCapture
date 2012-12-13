package gov.nrel.nbc.labelprinting.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;

public class FilteredAlphaNumericTextBox extends FilteredTextBox {
	
	public FilteredAlphaNumericTextBox () {
		super();
		
		this.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String value = ((TextBox)event.getSource()).getValue();
				String name = ((TextBox)event.getSource()).getName();
				boolean isLetterOrDigit = isValid(value);
				if (!isLetterOrDigit)
					Window.alert(name+" Bad text. Please use alpha-numeric text.");
			}
		});
	}
	
	public boolean isValid(String input) {
		boolean isLetterOrDigit = true;
		if (input != null && !input.isEmpty()) {
			isLetterOrDigit = input.matches(ALPHANUMERICPUNCT_PATTERN);
		}
		return isLetterOrDigit;
	}
	
	public String replaceSingleQuotes(String input) {
		String output = null;
		
		if (input != null && !input.isEmpty())
			output = input.replace('\'', '`');
		
		return output;
	}
	
	public String replaceDoubleQuotes(String input) {
		String output = null;
		
		if (input != null && !input.isEmpty())
			output = input.replace('\"', ' ');
		
		return output;
	}
}
