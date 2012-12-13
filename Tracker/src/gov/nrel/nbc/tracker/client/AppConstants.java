package gov.nrel.nbc.tracker.client;

/**
 * This interface holds constants.
 *
 * @author jalbersh
 */
public interface AppConstants extends DevTestProdConstants {

    // Static initialization-------------------------------------------

	public static final String CRYPTOKEY = "IAtpmoammg10";
	public final String ALPHANUMERICPUNCT_PATTERN = "[a-zA-Z0-9!#$%&()\\*+,./:;<=>^_`{|}~@\\- ]*";
	public final String ALPHANUMERIC_PATTERN = "[a-zA-Z0-9 \\-]*";
	public final String NUMERIC_PATTERN = "[0-9.\\-]*";
    //public final String TRACKER_PROPERTIES_FILE_NAME = "tracker";
    public final String TRACKER_PROPERTIES_FILE_NAME = DISPLAY_MODE+"_tracker";
	public final String PROBLEM_PRINTING = "There was a problem printing the label.\nPlease make sure that the printer PC is reachable.";
	public final String PROBLEM_EMAILING = "There was a problem emailing the label.\nPlease make sure that the email address is correct for the user.";
    public final String SELECT_PRINTER = "A printer has not been selected.\nPlease select a printer before printing a label.";
	public final String SUCCESS_EMAILING = "The label has been emailed.";
	public final String SUCCESS_PRINTING = "The label has been queued for printing.";
	
	public final String DESCRIPTION = "Description";
    public final String FORM = "Form";
    public final String SAMPLEID = "SampleId";
    public final String STRAIN = "Strain";
    public final String DESTINATION = "Destination";
    public final String CUSTODIAN = "Custodian";
    public final String COMPOSITION = "Composition";
    public final String ENTRY_DATE = "EntryDate";
    public final String TRACKINGID = "TrackingId";
    public final String PRINTER = "Printer";

	public final String DB_FIRST = "tracker.db.first";
	public final String DB_LAST = "tracker.db.last";
	public final String DB_TRACKER = "tracker.db.tracker";
	public final String DB_TEST = "tracker.db.test";
	public final String DB_PROD = "tracker.db.prod";
	public final String DB_SABC = "tracker.db.sabc";
	public final String	DB_DEV = "tracker.db.dev";
	public final String DB_LOCAL = "tracker.db.local";
	public final String DB_BIOMASS = "tracker.db.biomass";
	public final String DB_ALGAE = "tracker.db.algae";
	public final String CRADA_DB_SERVER = "tracker.db.crada.server";
	
	public final String DB_USER = "tracker.db.user";
	public final String DB_PASS = "tracker.db.pass";
	
	public final String PROP_POP_USER = "tracker.pop.user";
	public final String PROP_POP_PASS = "tracker.pop.pass";
	public final String PROP_POP_HOST = "tracker.pop.host";
	
	public final String PRINT_METHOD = "tracker.print.method";
	public final String PRINT_VIA_EMAIL = "EMAIL";
	public final String PRINT_VIA_DB = "DB";

	public final String SABC_LABEL_PRINTING = "SABC Label For Printing";
	public final String ALGAE_LABEL_PRINTING = "ALGAE Label For Printing";
    public final String TAB = "\t";
    public final String NEWLINE = "\n";
	
    public final String TEST_USER = "*";//"*";
    public final String TEST_PASS = "*";//"*";

    // TODO - Sparky - replace the POP and EMAIL USER and PASS for SABC here
    // Use the security.test.CryptoTest.testEncryption unit test to get the encrypted password
    public final String POP_USER = "*";//"*";
    public final String EMAIL_USER = "*";//"*";
    public final String EMAIL_PASS = "*";//"*";
    public static final String POP_PASS = "*";//"*";
    
    public final String LABEL_TABLE = "labels_for_printing";
    
    public final int INACTIVE_INTERVAL = 60*15; //15 minutes

    public final String DATE_FORMAT_DISPLAY = "MM/dd/yyyy";
    
	public final String JUNIT_USER_ID = "-1";
	public final String JUNIT_SESSION_ID = "-99999";
	
	public final String SABC_GROUP = "SABC";
	public final String JUNIT_GROUP = "junit";
		
    /**
     * Added to the first session timeout check to allow for startup time
     */
    public final int INITIAL_TIMEOUT_PAD = 15000;//30000; 
    /**
     * Added to the session timeout check timer.
     */
    public final int TIMEOUT_PAD = 15000; 
    /**
     * Default Excel file name
     */
	public final String EXCEL_FILE_NAME = "SampleData";
	
	/**
	 * Default Excel suffix
	 */
	public final String EXCEL_FILE_SUFFIX = ".xls";

	/**
	 * Default Excel suffix standard
	 */
	public final String EXCEL_FILE_SUFFIX_2007 = ".xlsx";

	/**
	 * Property name for the path to store the tracker files locally.
	 */
	public final String TEMP_DIR = "tracker."+DEV_PROD_TEST+".tempDirectory";
    
	/**
	 * Property name for the path to store the tracker files locally.
	 */
	public final String FILE_DIR = "tracker."+DEV_PROD_TEST+".fileDirectory";
    
    /* *
     * Property name for the location of the processed file log
     *
    public final String PROCESSED_FILE_LOG = "tracker."+DEV_PROD_TEST+".logDirectory";
*/
    // Servlet parameter names
    public final String SAMPLE_TRB_PAGE = "sampleTrbPage";

    public final String SAMPLE_TRB_NUM = "sampleTrbNum";

    public final String SAMPLE_STORAGENOTES = "sampleStorageNotes";
    
    public final String SAMPLE_PACKAGING = "samplePackaging";

    public final String SAMPLE_CASE = "sampleCase";

    public final String SAMPLE_SHELF = "sampleShelf";

    public final String SAMPLE_SUBLOCATION = "sampleSublocation";
    
    public final String SAMPLE_ROOM = "sampleRoom";
    
    public final String SAMPLE_HOLDER = "sampleContainer";

    public final String SAMPLE_BUILDING = "sampleBuilding";

    public final String SAMPLE_STATUS = "sampleStatus";

    public final String SAMPLE_FRACTION = "sampleFraction";

    public final String SAMPLE_FIRE = "sampleFire";

    public final String SAMPLE_REACTIVITY = "sampleReactivity";

    public final String SAMPLE_SPECIFIC = "sampleSpecific";

    public final String SAMPLE_HEALTH = "sampleHealth";

    public final String SAMPLE_FEEDSTOCK = "sampleFeedstock";

    public final String SAMPLE_TREATMENT = "sampleTreatment";

    public final String SAMPLE_FORM = "sampleForm";

    public final String SAMPLE_CUSTODIAN_NAME = "sampleEmpName";
    
    public final String SAMPLE_COMPOSITION = "sampleComposition";
    
	public final String SAMPLE_ORIGIN = "sampleOrigin";

    public final String SAMPLE_OWN_NAME = "sampleOwnName";

    public final String SAMPLE_EXTERNAL_ID = "sampleExternalId";

    public final String SAMPLE_UNIT = "sampleUnits";

    public final String SAMPLE_AMOUNT = "sampleAmount";

    public final String SAMPLE_COMMENTS = "sampleComments";

    public final String SAMPLE_DESCRIPTOR = "sampleDescriptor";

    public final String SAMPLE_NAME = "sampleName";
    
    // Navigation link tags
	public final String INIT_STATE = "initState";
	public final String SEARCH_STATE = "searchState";
	
	public final String FIRE_LABEL = "Fire:";
	public final String REACTIVITY_LABEL = "Reactivity:";
	public final String SPECIFIC_LABEL = "Specific:";
	public final String HEALTH_LABEL = "Health:";

	// Database java names
	public final String PAGE = "page";
	public final String NUMBER = "number";
	public final String TRB = "trb";
	public final String TRB_NUM = "trbNum";
	public final String TRB_PAGE = "trbPage";
	public final String PACKAGING = "packaging";
	public final String STORAGE_NOTES = "storageNotes";
	public final String SHELF = "shelf";
	public final String SUBLOCATION = "subLocation";
	public final String HOLDER = "holder";
	public final String ROOM = "room";
	public final String BUILDING = "building";
	public final String LOCATION = "location";
	public final String STATUS = "status";
	public final String FRACTION = "fraction";
	public final String FEEDSTOCK = "feedstock";
	public final String TREATMENT = "treatment";
	public final String CUSTODIAN_NAME = "custodianName";
	public final String FORM_NAME = "form";
	public final String COMPOSITION_NAME = "composition";
	public final String STRAIN_NAME = "strain";
	public final String OWNER_NAME = "ownerName";
	public final String BIOMASS_LOT = "biomassLot";
	public final String EXTERNAL_ID = "externalId";
	public final String CREATE_DATE = "createDate";
	public final String UNITS = "units";
	public final String AMOUNT = "amount";
	public final String COMMENT = "comment";
	public final String SAMPLE_ID = "sampleId";
	public final String ID = "id";
	public final String NAME = "name";
	public final String ORIGIN = "origin";
	public final String DESTINATION_NAME = "destination";
	public final String PROJECT = "project";
	public final String UNIT = "unit";
	public final String LABEL_DESCRIPTION = "label_description";

	public final int FIRE = 1;
	public final int REACTIVITY = 2;
	public final int SPECIFIC = 3;
	public final int HEALTH = 4;
	
	public final int SAMPLEID_FLD = -1;
	public final int TRBNUMBER_FLD = -2;
	public final int TRBPAGE_FLD = -3;
	public final int OWNER_FLD = -4;
	public final int LABELDESC_FLD = -5;
	public final int EXTERNALID_FLD = -6;
	public final int CUSTODIAN_FLD = -7;
	public final int ORIGIN_FLD = -8;
	public final int STATUS_FLD = -9;
	public final int FEEDSTOCK_FLD = -10;
	public final int TREATMENT_FLD = -11;
	public final int FRACTION_FLD = -12;
	public final int FIRE_FLD = -13;
	public final int REACT_FLD = -14;
	public final int SPEC_FLD = -15;
	public final int HEALTH_FLD = -16;
	public final int AMT_FLD = -17;
	public final int BLDG_FLD = -18;
	public final int ROOM_FLD = -19;
	public final int SUBLOC_FLD = -20;
	public final int SHELF_FLD = -21;
	public final int HOLDER_FLD = -22;
	public final int PKG_FLD = -23;
	public final int STORE_NOTES_FLD = -24;
	public final int COMMENT_FLD = -25;
	public final int ATTACHMENTS_FLD = -26;
	public final int UNIT_FLD = -27;
	public final int FORM_FLD = -28;
	public final int COMPOSITION_FLD = -29;
}
