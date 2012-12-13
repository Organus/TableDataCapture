package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Public class that is a SuggestBox that only allows alpha-numeric text
 *  that does not wreck the mySql database.
 * 
 * @author James Albersheim
 *
 */
public class FilteredSuggestBox extends SuggestBox implements AppConstants {
	
	/**
	 * A constructor that takes an oracle as a parameter.
	 * 
	 * @param oracle MultiWordSuggestOracle supplies text for suggestions
	 */
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
	
    /** This methods validates a data entry
    *
    * @param code The data to be validated
    *
    * @return Returns true if in valid format, else returns false
    */
	   private boolean hasBadCharacters(String data) {
			  for (int i=0;i<data.length();i++)
			  {
				  CharSequence cs = String.valueOf(data.charAt(i));
				  if (BADINPUTCHARS.contains(cs)) {
					  return true;
				  }
			  }

	      return false;
	   }

	/**
	 * Validates the text in the SuggestBox.
	 * 
	 * @return boolean valid?
	 */
	public boolean isValid() {
		return isValid(this.getValue());
	}
	
	/**
	 * Validates the input string.
	 * 
	 * @param input String to be validated.
	 * 
	 * @return boolean valid?
	 */
	public boolean isValid(String input) {
		if (hasBadCharacters(input)) return false;
		boolean isLetterOrDigit = true;
		if (input != null && !input.isEmpty()) {
			isLetterOrDigit = input.matches(ALPHANUMERICPUNCT_PATTERN);
		}
		return isLetterOrDigit;
	}
	
	/**
	 * Replaces single quotes with slash quote for java.
	 * 
	 * @param input String to be changed.
	 * 
	 * @return String modified
	 */
	public String replaceSingleQuotes(String input) {
		String output = null;
		
		if (input != null && !input.isEmpty())
			output = input.replace('\'', '`');
		
		return output;
	}
	
	/**
	 * Replaces double quotes with slash double quote for java.
	 * 
	 * @param input String to be changed.
	 * 
	 * @return String modified
	 */
	public String replaceDoubleQuotes(String input) {
		String output = null;
		
		if (input != null && !input.isEmpty())
			output = input.replace('\"', ' ');
		
		return output;
	}
	
	/**
	 * Public method to clean a string of quotes.
	 * 
	 * @param input String to be changed.
	 * 
	 * @return String modified
	 */
	public String replaceQuotes(String input) {
		String output = replaceDoubleQuotes(input);
		output = replaceSingleQuotes(output);
		
		return output;
	}
}
