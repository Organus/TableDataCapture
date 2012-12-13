package gov.nrel.nbc.tracker.client;

public interface DevTestProdConstants {
    /**
     * directive for local, dev, test, or prod
     */
	public final String LOCAL = "local";
	public final String DEV = "dev";
	public final String PROD = "prod";
	public final String TEST = "test";
	public final String SABC = "sabc";
	public final String ALGAE = "algae";
	public final String BIOMASS = "biomass";
	public final String NO_CRADA = "";
	
	public final Boolean OFF = false;
	public final Boolean ON = true;

	public final String DEV_PROD_TEST = DEV; // set this one to LOCAL, DEV, TEST, or PROD - this is the hibernate db
	public final String DISPLAY_MODE = BIOMASS; // this is the display - either BIOMASS or ALGAE
	
	public final String CRADA = NO_CRADA; // Set to an empty String (NO_CRADA) if not a CRADA project.
	//public final String CRADA_HIBERNATE_PREFIX = "";
	
	// for algae-tracker: 
	// modify log4j.properties and uncomment algae-tracker.log if ALGAE
	// don't forget to use the deploy-algae ant target

	
	
	// don't modify these
	public final Boolean SECURITY_MODE = OFF;
	public final String PRINT_MODE = BIOMASS;
	public final String CRADA_HIBERNATE_PREFIX = CRADA;
}
