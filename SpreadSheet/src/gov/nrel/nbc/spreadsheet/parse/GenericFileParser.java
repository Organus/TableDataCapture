package gov.nrel.nbc.spreadsheet.parse;

import java.util.List;

/**
 * An abstract class from which all Parsers derive.
 * @author James Albersheim
 *
 */
public abstract class GenericFileParser {

	protected boolean handle_as_blanks=false;
	/**
	 * return value.
	 */
	protected long retVal=0;
	/**
	 * @return the retVal
	 */
	public long getRetVal() {
		return retVal;
	}

	/**
	 * @param retVal the retVal to set
	 */
	public void setRetVal(long retVal) {
		this.retVal = retVal;
	}

	/**
	 * @return the handle_as_blanks
	 */
	public boolean isHandle_as_blanks() {
		return handle_as_blanks;
	}

	/**
	 * @param handle_as_blanks the handle_as_blanks to set
	 */
	public void setHandle_as_blanks(boolean handle_as_blanks) {
		this.handle_as_blanks = handle_as_blanks;
	}

	/**
	 * Abstract method to retrieve data.
	 * 
	 * @param <T> A template type that represents the data returned.
	 * @return List of data objects.
	 * @throws Exception
	 */
	abstract public <T> List<T> getData(String worksheet) throws Exception;
}
