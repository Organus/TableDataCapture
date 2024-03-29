package gov.nrel.nbc.spreadsheet.client;

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
				if (isLetterOrDigit == false)
					Window.alert("Bad text. Please use numerals.");
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
		boolean isValid = true;
		if (input != null && !input.isEmpty()) {
			isValid = input.matches(NUMERIC_PATTERN);
		}
		return isValid;
	}
}