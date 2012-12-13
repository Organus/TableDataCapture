package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Public class that is a TextBox that only allows alpha-numeric text.
 * 
 * @author James Albersheim
 *
 */
public class FilteredAlphaNumericMySQLTableNameTextBox extends FilteredTextBox implements AppConstants {
	
	/**
	 * Default constructor.
	 */
	public FilteredAlphaNumericMySQLTableNameTextBox () {
		super();
		
		this.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String value = ((TextBox)event.getSource()).getValue();
				boolean isLetterOrDigit = isValid(value);
				if (isLetterOrDigit == false) {
					Window.alert("Bad text. Please use alpha-numeric characters or any of !#$%+,=^_{}~@\\-\nThe invalid entry has been removed.");
					((TextBox)event.getSource()).setValue("");
				}
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
	 * Checks to see if the text is valid.
	 * 
	 * @param input String to be validated
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
}
