package gov.nrel.nbc.labelprinting.client;

import com.google.gwt.user.client.ui.TextBox;

public class FilteredTextBox extends TextBox implements AppConstants {
	
	public FilteredTextBox() {
		super();
	}
	
	public boolean isValid() {
		return isValid(this.getValue());
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
	
	public String replaceQuotes(String input) {
		String output = replaceDoubleQuotes(input);
		output = replaceSingleQuotes(output);
		
		return output;
	}
}
