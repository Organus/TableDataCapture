package gov.nrel.nbc.spreadsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
//import com.google.gwt.user.client.ui.TextBox;

public class FilteredAlphaNumericTextBox extends FilteredSuggestBox implements AppConstants {
	
	@SuppressWarnings("unchecked")
	public FilteredAlphaNumericTextBox (MultiWordSuggestOracle oracle) {
		super(oracle);
		
		this.addValueChangeHandler(new ValueChangeHandler() {
			public void onValueChange(ValueChangeEvent event) {
				String value = ((SuggestBox)event.getSource()).getValue();
				boolean isLetterOrDigit = isValid(value);
				if (isLetterOrDigit == false)
					Window.alert("Bad text. Please use alpha-numeric text.");
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
