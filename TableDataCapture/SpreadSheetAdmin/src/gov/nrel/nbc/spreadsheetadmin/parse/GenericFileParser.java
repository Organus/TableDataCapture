package gov.nrel.nbc.spreadsheetadmin.parse;

import java.util.List;

/**
 * An abstract class from which all Parsers derive.
 * @author James Albersheim
 *
 */
public abstract class GenericFileParser {

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
	 * Abstract method to retrieve data.
	 * 
	 * @param <T> A template type that represents the data returned.
	 * @return List of data objects.
	 * @throws Exception
	 */
	abstract public <T> List<T> getData(String worksheet) throws Exception;
}
