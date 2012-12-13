package gov.nrel.nbc.spreadsheet.client;

public interface DevTestProdConstants {
    /**
     * directive for local, dev, test, or prod
     */
	public final String LOCAL = "local";
	public final String DEV = "dev";
	public final String PROD = "prod";
	public final String TEST = "test";
 
	public final String DEV_PROD_TEST = PROD; // set this one to LOCAL, DEV, TEST, or PROD

}
