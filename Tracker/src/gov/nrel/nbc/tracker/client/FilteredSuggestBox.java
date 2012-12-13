package gov.nrel.nbc.tracker.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

public class FilteredSuggestBox extends SuggestBox implements AppConstants {
	
	public FilteredSuggestBox (MultiWordSuggestOracle oracle) {
		super(oracle);
		
		this.getTextBox().addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				String value = ((TextBox)event.getSource()).getValue();
				boolean isLetterOrDigit = isValid(value);
				if (isLetterOrDigit == false)
					Window.alert("Bad text. Please use normal text.");
			}
		});
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
