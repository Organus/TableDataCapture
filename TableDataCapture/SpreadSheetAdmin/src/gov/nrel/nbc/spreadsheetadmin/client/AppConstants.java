package gov.nrel.nbc.spreadsheetadmin.client;

/**
 * This interface holds constants.
 *
 * @author James Albersheim
 */
public interface AppConstants extends DevTestProdConstants {

    // Static initialization-------------------------------------------

	public final String DOUBLE_BACK_SLASH = "\\";
	public final String FORWARD_SLASH = "/";

	public final String ALPHANUMERICPUNCT_PATTERN = "[a-zA-Z0-9!#$%&()\\*+,\\./:;<=>^_`{|}~@\\- ]*";
	// none of the following according to MYSQL and Linux rules 
	// /, \, ., >, <, |, :, (, ), &, ;, ?, *
	public final String ALPHANUMERICPUNCT_PATTERN_MYSQL_TABLENAME = "[a-zA-Z0-9!#$%+,=^_{}~@\\- ]*";
	public final String ALPHANUMERIC_PATTERN = "[a-zA-Z0-9 \\-]*";
	public final String NUMERIC_PATTERN = "[0-9.\\-]*";
	public final String BADINPUTCHARS = "\\<>?`';";
	public final String BADTABLECHARS = "[`/\\,\\.><|:()&;?\\*]*";
    /** The Hibernate properties file name. */
    public final String SPREADSHEET_PROPERTIES_FILE_NAME = "spreadsheetadmin";
	public final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public final String FIRST_PART_DB = "jdbc:mysql://*";
	//public final String LAST_PART_DB = ".nrel.gov/ssic_spread_sheet"; 
	public final String LAST_PART_DB = "*/spread_sheet"; 
	public final String TEST_DB = "jdbc:mysql://nbcdbtest.nrel.gov/spread_sheet";
	public final String PROD_DB = "jdbc:mysql://nbcdbprod.nrel.gov/spread_sheet";
//	public final String DEV_DB = "jdbc:mysql://nbcdbdev.nrel.gov/ssic_spread_sheet";
	public final String DEV_DB = "jdbc:mysql://nbcdbdev.nrel.gov/spread_sheet";
	public final String LOCAL_DB = "jdbc:mysql://localhost/spread_sheet";

	public final String DB_FIRST = "spreadsheetadmin.db.first";
	public final String DB_LAST = "spreadsheetadmin.db.last";
	public final String DB_TRACKER = "spreadsheetadmin.db.tracker";
	public final String DB_TEST = "spreadsheetadmin.db.test";
	public final String DB_PROD = "spreadsheetadmin.db.prod";
	public final String	DB_DEV = "spreadsheetadmin.db.dev";
	public final String DB_LOCAL = "spreadsheetadmin.db.local";
	
	public final String USERTAG="connection.username";
    public final String PASSTAG="connection.password";

	// DB Types
	public final int LONG=1;
	public final int REAL=2;
	public final int DATE=3;
	public final int STRING=4;
	public final int BOOLEAN=5;

	public final long DEFAULT_LONG = -9999;
	public final double DEFAULT_REAL = -9999.0;
	/**
	 * Default Excel suffix standard
	 */
	public final String EXCEL_FILE_SUFFIX_2007 = ".xlsx";

	public static final String EXCEL97 = ".xls";

	public static final String EXCEL07 = ".xlsx";

	public static final String EXCELMACRO = ".xlsm";
	
	public static final String CSV = ".csv";
	
	public static final String TAB = ".tab";
	
	public static final String NUMBERS = ".numbers";

	public static final String NC = ".nc";
	
	public static final String TAB_DELIMITER = "\t";

	public static final String COMMA_DELIMITER = ",";

	/**
	 * Property name for the path to store the spreadsheet files locally.
	 */
	public final String FILE_DIR = "spreadsheetadmin."+DEV_PROD_TEST+".fileDirectory";
    
    /**
     * Property name for the location of the processed file log
     */
    public final String PROCESSED_FILE_LOG = "spreadsheetadmin."+DEV_PROD_TEST+".logDirectory";
    
    /**
     * Label for navigation panel - Create
     */
	public final String INIT_STATE = "initState";
    
    /**
     * Label for navigation panel - Modify
     */
	public final String MODIFY_STATE = "modifyState";

    /**
     * Label for navigation panel - Delete
     */
	public final String DELETE_STATE = "deleteState";
	
    /**
     * Label for navigation panel - Delete
     */
	public final String DELETE_CONFIG_STATE = "deleteConfigState";
	
	public final String WORKBOOK_ID_COLUMN = "workbook_id";
	
	public final String WORKBOOK_ID = "Workbook ID";
	
	public final String SHEET_SEPARATOR = "|";
	
	public final String HDR_ROW = "hdr_row";
	public final String HDR_COL = "hdr_col";
	public final String DATA_ROW = "data_row";
	public final String DATA_COL = "data_col";
	public final String META_START_ROW = "meta_start_row";
	public final String META_START_COL = "meta_start_col";
	public final String META_END_ROW = "meta_end_row";
	public final String META_END_COL = "meta_end_col";

	public final String FILE_NAME = "fileName";

	public final String CONFIG = "workbookConfigName";
	
	public final String SHEET_NAME = "sheetName";
	
	public final String EXCEL_FILE = "excelFile";
	
	public final String DATA_TABLE_ENDING = "_data";
	
	public final int NUM_INFO_COLUMNS = 2;

	// Meta data constants
	public final int ALL = 0;
	public final int INTERNAL = 1;
	public final int EXTERNAL = 2;
	
	public final int META = 1;
	public final int CELL = 2;

	// Data type ids
	public static final int BOOLEAN_TYPE_ID = 5;
	public static final int STRING_TYPE_ID = 4;
	public static final int REAL_TYPE_ID = 2;
	public static final int LONG_TYPE_ID = 1;
	public static final int DATE_TYPE_ID = 3;

	// Data Type Names
	public static final String STRING_TYPE = "String";
	public static final String BOOLEAN_TYPE = "Boolean";
	public static final String REAL_TYPE = "Real";
	public static final String LONG_TYPE = "Long";
	public static final String DATE_TYPE = "Date";
	
	public static final int SPREADSHEET_ERRORS = -1;
	public static final int MISSING_WORKSHEET = -2;
	public static final int MISMATCHED_COLUMN_NUMBERS = -3;
	public static final int MISSING_COLUMN = -4;
	public static final int MISMATCHED_TYPE = -5;
}
