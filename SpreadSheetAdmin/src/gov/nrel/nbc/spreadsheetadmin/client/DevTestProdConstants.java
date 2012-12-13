package gov.nrel.nbc.spreadsheetadmin.client;

public interface DevTestProdConstants {
    /**
     * directive for local, dev, test, or prod
     */
	public final String LOCAL = "local";
	public final String DEV = "dev";
	public final String PROD = "prod";
	public final String TEST = "test";

	public final String DEV_PROD_TEST = PROD; // set this one to LOCAL, DEV, TEST, or PROD

	/*
	
//	public final String TEST_WAR_PATH = "C:\\projects\\james\\SpreadSheet\\war\\";
	public final String TEST_WAR_PATH = "C:\\Projects\\MyEclipse\\SpreadSheet\\war\\";

*/
}
