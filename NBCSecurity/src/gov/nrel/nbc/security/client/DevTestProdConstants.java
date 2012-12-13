package gov.nrel.nbc.security.client;

public interface DevTestProdConstants {
    /**
     * directive for local, dev, test, or prod
     */
	public final String LOCAL = "local";
	public final String DEV = "dev";
	public final String PROD = "prod";
	public final String TEST = "test";
	public final String SABC = "sabc";

	public final String DEV_PROD_TEST = PROD; // set this one to LOCAL, DEV, TEST, PROD or SABC

	public final String MODE = SABC;
	public final String TESTID = "-99999";
	public final String JUNIT_SESSION_ID = "-99999";
}
