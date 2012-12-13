package gov.nrel.nbc.spreadsheet.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Public class to that is used to hold the data returned
 * from a query, including the counts, divided into workbook configs,
 * listed by the workbook config meta headers and cell headers. This class
 * is used to recall the results by workbook config, without the necessity 
 * to re-submit the query. It uses the inner class <Results> and a enum <State>.
 * 
 * @author James Albersheim
 *
 */
public class QueryStorage {
	public enum State {INIT,FIRST,SECOND};
	private List<Results> results;
	private State used;
	public QueryStorage() {
		used = State.INIT;
		results = new ArrayList<Results>();
	}
	public class Results {
		private List<List<String>> results;
		private String wbConfig;
		private String shConfig;
		/**
		 * @param results the results to set
		 */
		public synchronized void setResults(List<List<String>> results) {
			this.results = results;
		}
		/**
		 * @return the results
		 */
		public synchronized List<List<String>> getResults() {
			return results;
		}
		/**
		 * @param wbConfig the wbConfig to set
		 */
		public synchronized void setWbConfig(String wbConfig) {
			this.wbConfig = wbConfig;
		}
		/**
		 * @return the wbConfig
		 */
		public synchronized String getWbConfig() {
			return wbConfig;
		}
		/**
		 * @param shConfig the shConfig to set
		 */
		public synchronized void setShConfig(String shConfig) {
			this.shConfig = shConfig;
		}
		/**
		 * @return the shConfig
		 */
		public synchronized String getShConfig() {
			return shConfig;
		}
	}
	/**
	 * @return the used
	 */
	public synchronized State getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public synchronized void setUsed(State used) {
		this.used = used;
	}
	/**
	 * @param results the results to set
	 */
	public synchronized void setResults(List<Results> results) {
		this.results = results;
	}
	/**
	 * @return the results
	 */
	public synchronized List<Results> getResults() {
		return results;
	}
}
