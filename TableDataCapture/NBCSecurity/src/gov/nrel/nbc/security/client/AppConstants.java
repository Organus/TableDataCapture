package gov.nrel.nbc.security.client;

/**
 * This interface holds constants.
 *
 * @author jalbersh
 */
public interface AppConstants extends DevTestProdConstants {

    // Static initialization-------------------------------------------

	public static final String CRYPTOKEY = "*";
	public static final String GENKEY = "*";

	public final String ALPHANUMERICPUNCT_PATTERN = "[a-zA-Z0-9!#$%&()\\*+,./:;<=>^_`{|}~@\\- ]*";
	public final String ALPHANUMERIC_PATTERN = "[a-zA-Z0-9 \\-]*";
	public final String NUMERIC_PATTERN = "[0-9.\\-]*";
    public final String SECURITY_PROPERTIES_FILE_NAME = "NBCSecurity";
	public final String PROBLEM_PRINTING = "There was a problem printing the label.\nPlease make sure that the printer PC is reachable.";
	public final String SELECT_PRINTER = "A printer has not been selected.\nPlease select a printer before printing a label.";

	public final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public final String INITIAL_PART_DB = "jdbc:mysql://";
	public final String FIRST_PART_DB = "*";
	public final String NREL = "*";
	public final String LAST_PART_DB = "/security";
	public final String LAST_PROD_PART_DB = "/security";
	public final String LAST_PART_TRACKERDB = "*/tracker"; 
	public final String TEST_DB = "jdbc:mysql://*/security";
	public final String PROD_DB = "jdbc:mysql://*/security";
	public final String DEV_DB = "jdbc:mysql://*/security";
	public final String LOCAL_DB = "jdbc:mysql://localhost/security";
	public final String USERTAG="connection.username";
    public final String PASSTAG="connection.password";

    public final String dbUserKey = "security.db.user";
    public final String dbPassKey = "security.db.password";
    public final String dbPathKey = "security.db.path";
    
    public final String emailHostKey = "security.email.host";
    public final String emailUserKey = "security.email.user";
    public final String emailPassKey = "security.email.password";

    public final String adminUser1Key = "security.admin.user1";
    public final String adminUser2Key = "security.admin.user2";
    public final String adminUser3Key = "security.admin.user3";

    public final String domain = "security.domain";
    public final String mode = "security.mode";
    public final String ldap_provider = "security.ldap.provider";
    
    public final String ACTIVE_DIRECTORY_MODE = "AD";
    public final String NO_MODE = "None";
    public final String DB_MODE = "DB";
    
    public final int FTLB_140_PRINTER_ID = 1;
    public final int AFUF_205_PRINTER_ID = 2;
    public final int FTLB_OTHER_ID = 3;
    public final int AFUF_OTHER_ID = 4;
    public final int NO_PRINTER_ID = 0;
    public final String FTLB_140_PRINTER = "*";
    public final String AFUF_205_PRINTER = "*";
    public final String FTLB_OTHER = "";
    public final String AFUF_OTHER = "";
    public final String NO_PRINTER = "";

    public final String TEST_USER = "user";//"user";
    public final String TEST_PASS = "*";//"*";
    
    public final String DEFAULT_dbUserValue="user";
    public final String DEFAULT_dbPassValue="*";
    public final String DEFAULT_dbPathValue="*";
    public final String DEFAULT_emailHostValue="*";
    public final String DEFAULT_emailUserValue="*";//"*";
    public final String DEFAULT_emailPassValue="*";//"*";
    public final String DEFAULT_adminUser1Value="*";//"*";
    public final String DEFAULT_adminUser2Value="*";
    public final String DEFAULT_adminUser3Value="*";
    public final String DEFAULT_security_domain = "*";
    public final String DEFAULT_security_mode = "AD";
    
    public final String DEFAULT_ldap_provider = "ldap://*:389";
    public final String LDAPFACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	// TODO - Sparky - replace the POP and EMAIL USER and PASS for SABC here
    // Use the security.test.CryptoTest.testEncryption unit test to get the encrypted password
    public final String POP_USER = "user";//"password";
    public final String EMAIL_USER="*";//"*";
    public final String EMAIL_PASS="*";//"*";
    public final String ADMIN_USER1 = "*";//"*";
    public final String ADMIN_USER2 = "*";
    public final String ADMIN_USER3 = "*";
    public final String MAIL_HOST = "*";
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
    
	public final String DOUBLE_BACK_SLASH = "\\";

	public final String FORWARD_SLASH = "/";

    /**
     * Property name for the location of the processed file log
     */
    public final String PROCESSED_FILE_LOG = "tracker."+DEV_PROD_TEST+".logDirectory";

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
	public final String OWNER_NAME = "ownerName";
	public final String EXTERNAL_ID = "externalId";
	public final String CREATE_DATE = "createDate";
	public final String UNITS = "units";
	public final String AMOUNT = "amount";
	public final String COMMENT = "comment";
	public final String SAMPLE_ID = "sampleId";
	public final String ID = "id";
	public final String NAME = "name";
	public final String ORIGIN = "origin";
	public final String COMPOSITION = "composition";

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
	public final int COMPOSITION_FLG = -28;
}
