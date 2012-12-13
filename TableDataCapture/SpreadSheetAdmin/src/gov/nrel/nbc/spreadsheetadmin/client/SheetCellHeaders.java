package gov.nrel.nbc.spreadsheetadmin.client;

import java.io.Serializable;
import java.util.List;

public class SheetCellHeaders  implements Serializable {

	private static final long serialVersionUID = -5789857102404276832L;
	private String sheet;
	private List<String> headers;
	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}
	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
}
