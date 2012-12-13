package gov.nrel.nbc.spreadsheet.client;

import com.google.gwt.user.client.ui.TextBox;

public class FilteredTextBox extends TextBox implements AppConstants {
	
	public FilteredTextBox() {
		super();
	}
	
	public boolean isValid() {
		return isValid(this.getValue());
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

	public boolean isValid(String input) {
		if (hasBadCharacters(input)) return false;
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
