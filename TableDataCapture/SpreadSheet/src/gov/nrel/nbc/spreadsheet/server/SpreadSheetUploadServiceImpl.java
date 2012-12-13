package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.dao.AttachmentsDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.RowDataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookFileDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.CellData;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataFormat;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.GenericValue;
import gov.nrel.nbc.spreadsheet.dto.Metadata;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.RowData;
import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.SheetData;
import gov.nrel.nbc.spreadsheet.dto.ValueData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookFileData;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.parse.CellDataDTO;
import gov.nrel.nbc.spreadsheet.parse.DelimitedFileParser;
import gov.nrel.nbc.spreadsheet.parse.ExcelSParser;
import gov.nrel.nbc.spreadsheet.parse.ExcelSXParser;
import gov.nrel.nbc.spreadsheet.parse.NetCDFFileParser;
import gov.nrel.nbc.spreadsheet.parse.NumbersParser;
import gov.nrel.nbc.spreadsheet.parse.TikaSXParser;
//import gov.nrel.nbc.spreadsheet.parse.NumbersParser;
import gov.nrel.nbc.spreadsheet.parse.RowDTO;
//import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
//import org.mortbay.log.Log;

/**
 * A servlet that implements the methods for uploading a worksheet file.
 * 
 * @author James Albersheim
 * 
 */
public class SpreadSheetUploadServiceImpl extends HttpServlet implements
		AppConstants {

	private static final int MISMATCHED_CONFIG = -6;

	private static final int MISSING_COLUMN = -4;

	private static final String EXCEL97 = ".xls";

	private static final String EXCEL07 = ".xlsx";

	private static final String EXCELMACRO = ".xlsm";

	private static final String FIXED_QUOTE = "`";

	private static final String SINGLE_QUOTE = "'";

	private static final String SERVER_FILE_PREFIX = "spreadsheet";

	private static final String DOUBLE_BACK_SLASH = "\\";

	private static final String FORWARD_SLASH = "/";

	private static final String FILE_NAME = "fileName";

	private static final String ATTACH_FILE = "attachFile";
	
	private static final String STRING = "STRING";

	private static final String DATE = "DATE";

	private static final String REAL = "REAL";

	private static final String LONG = "LONG";

	private static final String BOOLEAN = "BOOLEAN";

	private static final String DB_DATE_FORMAT = "MM/dd/yy";
	
    private transient File tempFile;
    
    private String errorMsg = "";
    //private File repository;
    /**
     * Cached contents of the file.
     */
	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
	private File tempDir = null;
	public String db_first = "";
	public String db_last = ""; 
	public String db_tracker = ""; 
	public String db_test = "";
	public String db_prod = "";
	public String db_dev = "";
	public String db_local = "";
	/**
	 * A serialization identifier.
	 */
	private static final long serialVersionUID = -4515434818206581128L;

	/**
	 * XLogger.<OFF.error.warn|INFO|CONFIG.debug.debugR.debugST|ALL>
	 */
	//private static final XLogger log = new XLogger(XLogger.INFO);
	private static Logger log = Logger.getLogger(SpreadSheetUploadServiceImpl.class);

	String _tempQcFileName;
	/**
	 * workbook information reference
	 */
	private WorkbookData workbook = null;
	
	private String configName = "";
	
	private List<Attachments> attachments = new ArrayList<Attachments>();

	private List<Metadata> metadatas = null;

	/**
	 * Reference to spreadsheet file
	 */
	private WorkbookFileData theFile = null;

	/**
	 * Unique id for spreadsheet (workbookId)
	 */
	private long workbookId = -1;

	/**
	 * Create_or_modify - true is create, false is modify
	 */
	private boolean c_or_m = true;

	/**
	 * Name of file
	 */
	private String fileName = null;
	
	private boolean handle_errors_as_blanks = false;

	/**
	 * Method to perform post for servlet. Returns the sheet ID.
	 * 
	 * @param req
	 *            The servlet request object
	 * @param rsp
	 *            The servlet response object
	 * 
	 * @throws <ServletException>
	 * @throws <IOException>
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException {

//watchWindow("doPost","175");
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
        	System.setProperty("java.io.tmpdir","C:/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/");
        else
        	System.setProperty("java.io.tmpdir","/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/tmp");
        log.info("DEV_PROD_TEST="+DevTestProdConstants.DEV_PROD_TEST);
        log.info("tmp="+System.getProperty("java.io.tmpdir"));
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart == true) {
			getProperties();
	        File tdir = createTempDir(tempDir.getPath());
			FileItemFactory itemFactory = new DiskFileItemFactory(1000*1024*1024,tdir);
			ServletFileUpload upload = new ServletFileUpload(itemFactory);
			upload.setFileSizeMax(1000*1024*1024);
			try {
				List<DiskFileItem> items = (List<DiskFileItem>) upload.parseRequest(req);
				
				metadatas = new ArrayList<Metadata>();
				
				setFileSystem();
				// First check to see if the file matches the configuration type.
				setNames(items);
				boolean qcId = qcFile(items);
				if (qcId == false) {
					//workbookId = -1;
					log.warn("Failed QC.");
//watchWindow("doPost","193");
				}
				else {
					log.info("Passed QC.");
					// Get parameters and save
					Iterator<DiskFileItem> it = items.iterator();
//watchWindow("doPost","199");
					while (it.hasNext()) {
						DiskFileItem item = it.next();
						if (item.isFormField() == true) {
							String name = item.getFieldName();
							String value = item.getString();
							if (value != null)
								value = value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE);
							saveMetadata(name, value);
						}
					}
					
					// Save Workbook
					if (!items.isEmpty()) {
						if (c_or_m)
							persistWorkbook();
					}
					else
						log.warn("There were no parameters passed to doPost!");
	
					// Get file and save
					it = items.iterator();
					while (it.hasNext()) {
						DiskFileItem item = it.next();
						if ((item.isFormField() == false) && (item.getFieldName().equalsIgnoreCase("excelFile"))) {
							String name = item.getName();
							if (name.endsWith(EXCEL97) || name.endsWith(EXCEL07) || name.endsWith(EXCELMACRO) || name.endsWith(CSV) || name.endsWith(TAB) || name.endsWith(NUMBERS) || name.endsWith(NC)) {
								processFile(item, tdir);
								//processFile(req);
							} else {
								log.warn(name + " is not a file we can upload.\nPlease choose an '" + EXCEL97 + "' file, " + "an '" + EXCEL07 + "' file, an '" + EXCELMACRO + "' file, n '" + CSV + "' file, n '" + TAB + "' file, or a '" + NUMBERS + "' file, or a '" + NC + "' file.");
							}
						}
					}
				}
			} catch (FileUploadException fue) {
				log.warn("Error uploading spreadsheet file! Error: " + fue);
				log.warn(SpreadSheetUploadServiceImpl.getStackTrace(fue));
			}
			finally{
				metadatas = null;
			}

			PrintWriter writer = rsp.getWriter();
			if (workbookId != -1 && workbookId != -9)
				writer.write(String.valueOf(workbookId));
			else 
				writer.write(errorMsg);				
			writer.close();
//watchWindow("doPost","238");
		}
	}
	
	/** Sets the Object's configName and fileName variables
	 * 
	 * @param items
	 */
	private void setNames(List<DiskFileItem> items) {
		Iterator<DiskFileItem> it = items.iterator();
		while (it.hasNext()) {
			DiskFileItem item = it.next();
			if (item.isFormField() == true) {
				String name = item.getFieldName();
				String value = item.getString();
				if (name.equalsIgnoreCase(CONFIG)){
					setConfigName(name, value);
				}
				else if (name.equalsIgnoreCase(FILE_NAME)){
					setFileName(name, value);
				}
				else if (name.equalsIgnoreCase(CREATE_OR_MODIFY_NAME)) {
					int num = 0;
					try {
						num = Integer.parseInt(value);
					} catch (NumberFormatException nfe) {
						//log.warn("unable to parse "+value);
					}
					if (num == 1) c_or_m = true;
					else c_or_m = false;
				}
				else if (name.equalsIgnoreCase(WORKBOOK_TO_MODIFY_NAME)) {
					long num = 0;
					try {
						num =Long.parseLong(value);
					} catch (NumberFormatException nfe) {
						//log.warn("unable to parse "+value);
					}
					if (num > 0)
						workbookId = num;					
				} else if (name.equalsIgnoreCase(AS_BLANKS)) {
						handle_errors_as_blanks = true;
				} else if (name.equalsIgnoreCase(FAILED)) {
						handle_errors_as_blanks = false;
				}
			}
		}
	}

	private void setConfigName(String name, String value){
		if (value != null){
			value = value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE);
			configName = value;
			return;
		}
	}
	
	private void setFileName(String name, String value){
		if (value != null){
			value = value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE);
			fileName = value;
			return;
		}
	}
	
	/**
	 * Private method to save the spreadsheet file and meta data to the database
	 * 
	 * @throws <ServletException>
	 */
	private void persistWorkbook() throws ServletException {
		workbook = new WorkbookData();
		
		if (theFile != null)
			workbook.setWorkbook_file_id(theFile);
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			Transaction tx = session.beginTransaction();

			session.save(workbook);

			workbookId = workbook.getWorkbook_id();
			
//watchWindow("persistWorkbook","299");

			tx.commit();
		} catch (HibernateException he) {
    		log.error("Hibernate exception on ingest. error: " + he);
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.error("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } finally {
        	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
        }
	}

	private void setMetadata(Session session, MetadataHeader header, String name, String value) {
		ValueData gd = new ValueData();
		Metadata metadata = new Metadata();
		
		DataType dt = header.getTypeId();
		String stype = "";
		if (dt != null)
			stype = dt.getDescription();
		log.info("metaData header="+header.getName()+";stype="+stype+";name="+name+";value="+value);
		if (stype.equals(STRING)) {
			gd.setSvalue(value);
			gd.setData_type(4);
		} else if (stype.equals(LONG)) {
			long data = AppConstants.DEFAULT_LONG;
			try {
				// TODO: Check for null or bad values and treat correctly.
				data = Long.parseLong(value);
			} catch (NumberFormatException nfe) {
				log.warn("problems parsing long data = "+value);
			}
			gd.setLvalue(data);		
			gd.setData_type(1);
		} else if (stype.equals(REAL)) {
			double data = AppConstants.DEFAULT_REAL;
			try {
				data = Double.parseDouble(value);
			} catch (NumberFormatException nfe) {
				log.warn("problems parsing real data = "+value);
			}
			gd.setRvalue(data);			
			gd.setData_type(2);
		} else if (stype.equals(BOOLEAN)) {
			boolean data = false;
			try {
				data = Boolean.parseBoolean(value);
			} catch (NumberFormatException nfe) {
				log.warn("problems parsing boolean data = "+value);
			}
			gd.setBvalue(data);
			gd.setData_type(5);
		} else if (stype.equals(DATE)) {
			DataFormat format = header.getData_format();
			String sformat = DB_DATE_FORMAT;
			if (format != null) {
					sformat = format.getFormat();
			}
			SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
			Date dvalue = null;
			try {
				dvalue = dFormat.parse(value);
			} catch (ParseException pe) {
				log.warn("Invalid date format: "+value);
			}
			if (dvalue != null) {
				Date collectionTime = dvalue;
				GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
				cal.setTime(collectionTime);
				GenericValue gValue = new GenericValue();
				gValue.setDateValue(cal.getTime());
				gd.setDvalue(gValue.getDateValue());	
			}
			gd.setData_type(3);
		}

		metadata.setMetadata_hdr_id(header);
		if (gd != null) {
			long id = getMaxValueId("value_data","data_id");
			gd.setData_id(id+1);
			//insertNewValue(gd);
			//session.save(gd);
			metadata.setValue(gd);
			log.info("saving new metadata");
			//session.save(metadata);
			metadatas.add(metadata);
		}
	}
	
	/**
	 * Private method to save the given meta data name/value pair to
	 * global variables.
	 * 
	 * @param name
	 *            The meta data name to save
	 * @param value
	 *            The value of the meta data name to save
	 * @throws <ServletException>
	 */
	private void saveMetadata(String name, String value)
			throws ServletException {

		log.info("saving metadata with name="+name+" and value="+value);
		
		if (name.equalsIgnoreCase(FILE_NAME))
			fileName = value;
		else if (name.equalsIgnoreCase(CONFIG))
			configName = value;
		else if (name.startsWith(ATTACH_FILE)) {
			long attachment_id = 0;
			Session session = null;
			try {
				attachment_id = Long.parseLong(value);
				if (attachment_id != 0) {
					session = HibernateSessionFactory.getSession();

					Transaction tx = session.beginTransaction();
					AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
					adh.setSession(session);

					Attachments attachment = adh.findById(attachment_id);

					attachments.add(attachment);
					
					tx.commit();
				}
			} catch (NumberFormatException nfe) {
				// Do nothing
			} catch (HibernateException he) {
	    		log.error("Hibernate exception: " + he);
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.error("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.error("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
	        }
		} else { // Meta Data
			Transaction tx = null;
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();

				tx = session.beginTransaction();

				MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
				mhdh.setSession(session);

				WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
				wcdh.setSession(session);
					
				// these saveOrUpdate are supposed to resolve any possible NonUniqueObjectException 
				//when multiple posts are made with identical metadata on the web page.
//				session.saveOrUpdate(mhdh);
//				session.saveOrUpdate(wcdh);
				
				WorkbookConfig config = wcdh.findBySynonym(configName);
				MetadataHeader header = mhdh.findBySynonymAndConfig(config, name);
				if (header == null) {
					//log.warn("header for name = " + name + " NOT FOUND");
					return;
				}

				setMetadata(session, header, name, value);
				tx.commit();
				
			} catch (NumberFormatException nfe) {
				// do nothing
			} catch (HibernateException he) {
	    		log.error("Hibernate exception: " + he);
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.error("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.error("Exception caught: " + ex);
	    		log.error(getStackTrace(ex));
	        } finally {
	        	if (session != null && session.isConnected())
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
	        }
			
		}
	}

	 public class MyResources extends ResourceBundle {
		 private HashMap<String,String> contents = new HashMap<String,String>();
	     public MyResources() {
	        	//	 # Temp directory for temporary files
    		 contents.put(tempDirKey,tempDirValue);
	        	//	 # Directory to store attachments
	         contents.put(fileDirKey,fileDirValue);
	        	//	 # Directory to create log file for files processed.
	         contents.put(logDirKey,logDirValue);	 
	         contents.put(FILE_DIR, "/usr/local/NREL_ARCHIVE/SPREADSHEET/");
	     }
	     protected Object[][] getContents() {
	     
	         return new Object[][] {
	             // LOCALIZE THE SECOND STRING OF EACH ARRAY (e.g., "OK")
	             //{"OkKey", "OK"},
	             //{"CancelKey", "Cancel"},
	        	//	 # Temp directory for temporary files
	        		 {tempDirKey,tempDirValue},
	        	//	 # Directory to store attachments
	        		 {fileDirKey,fileDirValue},
	        	//	 # Directory to create log file for files processed.
	        		 {logDirKey,logDirValue},
	        		 {FILE_DIR,"/usr/local/NREL_ARCHIVE/SPREADSHEET/"}
	        		 // END OF MATERIAL TO LOCALIZE
	        };
	     }
	     

		//@Override
		@SuppressWarnings("unchecked")
		public Enumeration<String> getKeys() {
			return (Enumeration<String>) contents.keySet();
		}

		//@Override
		protected Object handleGetObject(String key) {
			return contents.get(key);
		}
		
	 }

	 /**
	  * Adjust directories for server type.
	  * 
	  * @return void
	  * 
	  */
	 private void setFileSystem(){
		if (DEV_PROD_TEST.equals(LOCAL)) {
			tempDirKey = "spreadsheet.local.tempDirectory";
			tempDirValue = "C:/workspace/tmp/";
			fileDirKey = "spreadsheet.local.fileDirectory";
			fileDirValue = "C:/workspace/files/";
			logDirKey = "spreadsheet.local.logDirectory";
			logDirValue = "C:/workspace/logfile.txt";
		} else if (DEV_PROD_TEST.equals(DEV)) {
			tempDirKey = "spreadsheet.dev.tempDirectory";
			tempDirValue = "/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/";
			fileDirKey = "spreadsheet.dev.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/";
			logDirKey = "spreadsheet.dev.logDirectory";
			logDirValue = "/var/log/tomcat5.5";
		} else if (DEV_PROD_TEST.equals(TEST)) {
			tempDirKey = "spreadsheet.test.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "spreadsheet.test.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/";
			logDirKey = "spreadsheet.test.logDirectory";
			logDirValue = "/var/log/tomcat5.5";
		} else if (DEV_PROD_TEST.equals(PROD)) {
			tempDirKey = "spreadsheet.prod.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "spreadsheet.prod.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SPREADSHEET/";
			logDirKey = "spreadsheet.prod.logDirectory";
			logDirValue = "/var/log/tomcat5.5";			
		}
	        
		return;
	 }
	 
	    /**
	     * Creates and returns a {@link java.io.File File} representing a uniquely
	     * named temporary file in the configured repository path. The lifetime of
	     * the file is tied to the lifetime of the <code>FileItem</code> instance;
	     * the file will be deleted when the instance is garbage collected.
	     *
	     * @return The {@link java.io.File File} to be used for temporary storage.
	     */
	    protected File getTempFile(File tempDir) {
            	String tempFileName = "upload.tmp";//+new Date().getTime() + ".tmp";
	            tempFile = new File(tempDir, tempFileName);
	            tempFile.setExecutable(true,false);
	            tempFile.setReadable(true,false);
	            tempFile.setWritable(true,false);
	            //IOUtils.
	        //}
	        return tempFile;
	    }

	    /**
	     * Returns the contents of the file as an array of bytes.  If the
	     * contents of the file were not yet cached in memory, they will be
	     * loaded from the disk storage and cached.
	     *
	     * @return The contents of the file as an array of bytes.
	     */
	    private static File createTempDir(String baseTempPath) {
	        //final String baseTempPath = System.getProperty("java.io.tmpdir");

	        Random rand = new Random();
	        int randomInt = 1 + rand.nextInt();

	        File tempDir = new File(baseTempPath + File.separator + "tempDir" + randomInt);
	        if (tempDir.exists() == false) {
	            tempDir.mkdirs();
	        }

	        //tempDir.deleteOnExit();

	        return tempDir;
	    }
	    
     /**
	 * Private method that saves the file with a unique name for use by the
	 * searching process and ingests the data into the database.
	 * 
	 * @param item
	 *            Contains the path of the tag list file.
	 * @return Path of newly created local tag list file.
	 */
	private String processFile(DiskFileItem item, File tdir) {

		fileName = item.getName().toLowerCase();
		log.info("fileName from item (client) = "+fileName);
		String separator = FORWARD_SLASH;
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		separator = DOUBLE_BACK_SLASH;
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);

		String newFileName = SERVER_FILE_PREFIX + new Date().getTime() + fileName;
		File newFile = null;
		try {
			//newFile = createTempDir(newFileName);
			///*
 			String fname = tdir.getPath() + File.separator + newFileName;
			log.info("upload filename = " + fname);
			newFile = new File(fname);
			newFile.setExecutable(true,false);
			newFile.setReadable(true,false);
			newFile.setWritable(true,false);
			//*/
			
			
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	FileUtils.copyFile(new File(item.getName()), newFile);
	        else {
	        	//newFile.createNewFile();
	        	item.write(newFile);
	        	//write(item,newFile,tdir);
	        	/*
                BufferedInputStream in = new BufferedInputStream(
                        item.getInputStream());
                BufferedOutputStream    out = new BufferedOutputStream(
                            new FileOutputStream(newFile));
                IOUtils.copy(in, out);
                in.close();
                out.close();
	        	newFile.setReadable(true, false);
	        	log.info("is readable="+newFile.canRead());
	        	log.info(newFile.getPath() + (newFile.exists() ? " does " : " does NOT ") + " exist");
	        	Thread.sleep(2000);
	        	*/
	        }

			if (c_or_m)
				saveFile(fileName, newFile.getPath());
			else
				updateFile(fileName, newFile.getPath());

			long id = -1;
			//log.error("b4 insert");
			id = parseFile(newFile);
			//log.error("after insert");
			if (id < 0) {
				workbookId = id;
			}
//watchWindow("processFile","627");

		} catch (Exception e) {
			log.warn("Error ingesting spreadsheet file! File: "
					+ newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warn(stack);
			workbookId = -1;
		}
		return newFile.getPath();
	}
	
	/**
	 * Private method to Quality Control check an Excel spreadsheet file.
	 * Internal metadata is NOT currently checked.
	 * 
	 * @param newFile
	 *            Contains the path of the Excel spreadsheet
	 *            
	 * @throws <Exception>
	 * 
	 * @return boolean
	 *            
	 */
	private boolean qcFile(List<DiskFileItem> items) {
		Session session = null;
		
		File _tempQcFile = null;
		String _qcFailLogMsg = "QC CHECK FAILED. The workbook ingested did not match " +
											"the form of the configuration:" + '\n' ;
		boolean retVal = false;
		
		try {
			session = HibernateSessionFactory.getSession();

			// Session bookkeeping
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			mhdh.setSession(session);				
			ctdh.setSession(session);
			dtdh.setSession(session);
			rddh.setSession(session);
			wcdh.setSession(session);

			// Get fileName and configName and save a temporary file copy
			Iterator<DiskFileItem> it = items.iterator();
			while (it.hasNext()) {
				DiskFileItem item = it.next();
				if(item.getFieldName().equalsIgnoreCase("excelFile")){
					String name = item.getName();
					if (name == null)
						name = item.getString();
					if (name.endsWith(EXCEL97) || name.endsWith(EXCEL07) || name.endsWith(EXCELMACRO) || name.endsWith(CSV) || name.endsWith(TAB) || name.endsWith(NUMBERS) || name.endsWith(NC)) {
						_tempQcFile = saveTempQcFile(item);
					}
					if (_tempQcFile == null){
						log.warn("The _tempQcFile file returned a null value.  Something went wrong.  You might want to look into it.");
						return false;
					}
				}
			}
			
			
			//get worksheets from configuration
			log.info("wbConfigName="+configName);
			WorkbookConfig config = wcdh.findBySynonym(configName);
			Set<SheetConfig> sheets = config.getSheetConfigs();
			Iterator<SheetConfig> sit = sheets.iterator();
			
			//loop through worksheets from config
			while (sit.hasNext()) {
				SheetConfig sheetConfig = sit.next();
				if (sheetConfig != null)
					log.info("sheetConfig="+sheetConfig.getSheet_name()+" with id="+sheetConfig.getSheet_config_id());
				if (_tempQcFile.getName().endsWith(EXCEL97)) {
					ExcelSParser parser = new ExcelSParser(handle_errors_as_blanks,_tempQcFile, config, session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} else if (_tempQcFile.getName().endsWith(EXCEL07) || _tempQcFile.getName().endsWith(EXCELMACRO)) {
					ExcelSXParser parser = new ExcelSXParser(handle_errors_as_blanks, _tempQcFile, config, session);
					//TikaSXParser parser = new TikaSXParser(_tempQcFile);
					//parser.setWbConfig(config);
					//parser.setSession(session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} else  if (_tempQcFile.getName().endsWith(CSV)) {
					DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, _tempQcFile);
					parser.setDELIMITER(COMMA_DELIMITER);
					parser.setWbConfig(config);
					parser.setSession(session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} else  if (_tempQcFile.getName().endsWith(TAB)) {
					DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, _tempQcFile);
					parser.setDELIMITER(TAB_DELIMITER);
					parser.setWbConfig(config);
					parser.setSession(session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} else  if (_tempQcFile.getName().endsWith(NUMBERS)) {
					NumbersParser parser = new NumbersParser(handle_errors_as_blanks, _tempQcFile);
					parser.setWbConfig(config);
					parser.setSession(session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} else  if (_tempQcFile.getName().endsWith(NC)) {
					NetCDFFileParser parser = new NetCDFFileParser(handle_errors_as_blanks, _tempQcFile);
					parser.setWbConfig(config);
					parser.setSession(session);
					retVal = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
				} 
				//log.warn("retVal = " + retVal);
				if (retVal == true){
					log.warn("Configuration name is " + configName + ".\n");
					log.warn("File name is " + fileName + ".\n");
					if (c_or_m)
						workbookId = 0;
				}
				else {
					//workbookId = -1;
//					if (session.isOpen())
//						session.close();
				}
				log.warn("workbookId = " + workbookId);
			}

			boolean ret = _tempQcFile.delete();
			if (!ret)
				log.warn("failed to delete temp file: "+_tempQcFile.getPath());
			
		} catch (ConstraintViolationException cve) {
			log.warn("A constraint violation exception was thrown. Exception: "
							+ cve);
			log.warn(cve.getSQL());
			log.warn(getStackTrace(cve));
			workbookId = -1;
			retVal = false;
		} catch (HibernateException he) {
			workbookId = -1;
			retVal = false;
			log.error("Hibernate exception on spreadsheet upload. error: " + he);
			log.error(getStackTrace(he));
	    	try {
	    		if (session != null && session.isConnected())
	    			session.getTransaction().rollback();
	    	} catch (HibernateException rbEx) {
	        	log.error("Couldn't roll back transaction! Error: " + rbEx);
	    	}
	    } catch (Exception e) {
			log.warn("An exception was thrown in qcFile(). Exception: "
					+ e);
			log.warn(getStackTrace(e));
			retVal = false;
			workbookId = -1;
	    } finally {
	    	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
	    }
	    log.warn("qcFile retVal = " + retVal);
	    return retVal;
	}

	public String getConfig(File item) {
		Session session = null;
		
		File _tempQcFile = null;
		String _qcFailLogMsg = "QC CHECK FAILED. The workbook ingested did not match " +
											"the form of the configuration:" + '\n' ;
		String retVal = "";
		
		try {
			session = HibernateSessionFactory.getSession();

			// Session bookkeeping
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			mhdh.setSession(session);				
			ctdh.setSession(session);
			dtdh.setSession(session);
			rddh.setSession(session);
			wcdh.setSession(session);

			// Get fileName and configName and save a temporary file copy
			String name = item.getName();
			if (name.endsWith(EXCEL97) || name.endsWith(EXCEL07) || name.endsWith(EXCELMACRO) || name.endsWith(CSV) || name.endsWith(TAB) || name.endsWith(NUMBERS) || name.endsWith(NC)) {
				_tempQcFile = saveTempQcFile(item);
			}
			if (_tempQcFile == null){
				log.warn("The _tempQcFile file returned a null value.  Something went wrong.  You might want to look into it.");
				return "";
			}
			
			List<String> configs = wcdh.findAllNames();
			Iterator<String> cit = configs.iterator();
			while (retVal.isEmpty()&&cit.hasNext()) {
				configName = cit.next();
				//get worksheets from configuration
				log.info("wbConfigName="+configName);
				WorkbookConfig config = wcdh.findBySynonym(configName);
				Set<SheetConfig> sheets = config.getSheetConfigs();
				Iterator<SheetConfig> sit = sheets.iterator();
				
				//loop through worksheets from config
				while (sit.hasNext()) {
					SheetConfig sheetConfig = sit.next();
					if (sheetConfig != null)
						log.info("sheetConfig="+sheetConfig.getSheet_name()+" with id="+sheetConfig.getSheet_config_id());
					if (_tempQcFile.getName().endsWith(EXCEL97)) {
						ExcelSParser parser = new ExcelSParser(handle_errors_as_blanks,_tempQcFile, config, session);
						boolean ret = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
						if (ret) 
							retVal = configName;//sheetConfig.getSheet_name();
					} else if (_tempQcFile.getName().endsWith(EXCEL07) || _tempQcFile.getName().endsWith(EXCELMACRO)) {
						ExcelSXParser parser = new ExcelSXParser(handle_errors_as_blanks, _tempQcFile, config, session);
						//TikaSXParser parser = new TikaSXParser(_tempQcFile);
						//parser.setWbConfig(config);
						//parser.setSession(session);
						boolean ret = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
						if (ret) 
							retVal = configName;//sheetConfig.getSheet_name();
					} else  if (_tempQcFile.getName().endsWith(CSV)) {
						DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, _tempQcFile);
						parser.setDELIMITER(COMMA_DELIMITER);
						parser.setWbConfig(config);
						parser.setSession(session);
						boolean ret = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
						if (ret) 
							retVal = configName;//sheetConfig.getSheet_name();
					} else  if (_tempQcFile.getName().endsWith(TAB)) {
						DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, _tempQcFile);
						parser.setDELIMITER(TAB_DELIMITER);
						parser.setWbConfig(config);
						parser.setSession(session);
						boolean ret = qcWorkbook(parser, sheetConfig, _qcFailLogMsg);
						if (ret) 
							retVal = configName;//sheetConfig.getSheet_name();
					} 
					//log.warn("retVal = " + retVal);
					if (!retVal.isEmpty()){
						log.warn("Configuration name is " + configName + ".\n");
						log.warn("File name is " + fileName + ".\n");
						if (c_or_m)
							workbookId = 0;
					}
					else {
						//workbookId = -1;
	//					if (session.isOpen())
	//						session.close();
					}
					log.warn("workbookId = " + workbookId);
				}
	
				//boolean ret = _tempQcFile.delete();
				//if (!ret)
				//	log.warn("failed to delete temp file: "+_tempQcFile.getPath());
			}
		} catch (ConstraintViolationException cve) {
			log.warn("A constraint violation exception was thrown. Exception: "
							+ cve);
			log.warn(cve.getSQL());
			log.warn(getStackTrace(cve));
			workbookId = -1;
			retVal = "";
		} catch (HibernateException he) {
			workbookId = -1;
			retVal = "";
			log.error("Hibernate exception on spreadsheet upload. error: " + he);
			log.error(getStackTrace(he));
	    	try {
	    		if (session != null && session.isConnected())
	    			session.getTransaction().rollback();
	    	} catch (HibernateException rbEx) {
	        	log.error("Couldn't roll back transaction! Error: " + rbEx);
	    	}
	    } catch (Exception e) {
			log.warn("An exception was thrown in qcFile(). Exception: "
					+ e);
			log.warn(getStackTrace(e));
			retVal = "";
			workbookId = -1;
	    } finally {
	    	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
	    }
	    log.warn("qcFile retVal = " + retVal);
	    return retVal;
	}

	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An ExcelParser for Excel 97 files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(ExcelSParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty()|| !_sheetNames.contains(_worksheet)){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled " +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				} */

				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					_configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						_columnName = _columnNames.get(_configHdrIndex - 1);
						if (!_columnName.equals(_configHeader.getSynonym())){
							errorMsg = " The parser expected to find the header " +
							_columnName +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_configHeader.getSynonym() +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHdrIndex +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An ExcelParser for Excel 2007 files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(ExcelSXParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		//CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		//String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty() || !_sheetNames.contains(_worksheet)){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled" +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				workbookId = MISSING_COLUMN;
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				}
				*/
				
				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					CellDataHeader _configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						String _columnName = _columnNames.get(_configHdrIndex - 1);
						String configHeaderName = _configHeader.getSynonym();
						if (!_columnName.equals(configHeaderName)){
							errorMsg = " The parser expected to find the header " +
							_configHeader.getSynonym() +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_columnName +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHeader.getHdr_index() +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		log.warn("qcWorkbook returns true");
		return true;
	}
	
	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An TikaSXParser for Excel XLSX files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(TikaSXParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty()){// || (!_sheetNames.contains(_worksheet))){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled " +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				workbookId = MISSING_COLUMN;
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				} */

				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					_configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						_columnName = _columnNames.get(_configHdrIndex - 1);
						if (!_columnName.equals(_configHeader.getSynonym())){
							errorMsg = " The parser expected to find the header " +
							_columnName +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_configHeader.getSynonym() +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHdrIndex +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An NumbersParser for iWork Numbers files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(NumbersParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty() || (!_sheetNames.contains(_worksheet))){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled " +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				workbookId = MISSING_COLUMN;
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				} */

				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					_configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						_columnName = _columnNames.get(_configHdrIndex - 1);
						if (!_columnName.equals(_configHeader.getSynonym())){
							errorMsg = " The parser expected to find the header " +
							_columnName +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_configHeader.getSynonym() +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHdrIndex +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An NumbersParser for iWork Numbers files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(DelimitedFileParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty()|| !_sheetNames.contains(_worksheet)){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled " +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				workbookId = MISSING_COLUMN;
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				} */

				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					_configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						_columnName = _columnNames.get(_configHdrIndex - 1);
						if (!_columnName.equals(_configHeader.getSynonym())){
							errorMsg = " The parser expected to find the header " +
							_columnName +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_configHeader.getSynonym() +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHdrIndex +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Compares the workbook configuration to the workbook attempting to be ingested.
	 * Sends.warns to the log to help identify the problem.
	 * 
	 * @param parser An NumbersParser for iWork Numbers files.
	 * @param sheetConfig SheetConfig DTO
	 * @param qcFailLogMsg Verbose.warn message header
	 * 
	 * @return A boolean that is true for success, false for fail
	 * 
	 */
	private boolean qcWorkbook(NetCDFFileParser parser, SheetConfig sheetConfig, String qcFailLogMsg){
		String _worksheet = sheetConfig.getSynonym();
		Set <CellDataHeader> _configHeaders = sheetConfig.getCell_hdrs();
		CellDataHeader _configHeader;
		List<String> _sheetNames;
		List<String> _columnNames;
		Iterator<CellDataHeader> cit;
		String _columnName = "";
		int _configHdrIndex=0;
		
		_sheetNames = parser.getSheets();
		if (_sheetNames.isEmpty()|| !_sheetNames.contains(_worksheet)){
			errorMsg = " Either the file was empty or the parser didn't find the worksheet titled " +
			_worksheet + ".";
			log.warn(qcFailLogMsg + 
					'\t' + 
					errorMsg + '\n');
			workbookId = MISMATCHED_CONFIG;
			return false;
		}
		else {
			_columnNames = parser.getHeaders(_worksheet);
			if (_columnNames.isEmpty()){
				errorMsg = "No headers were found in the spreadsheet!";
				log.warn(qcFailLogMsg +
						'\t' +
						errorMsg);
				workbookId = MISSING_COLUMN;
				return false;
			} else {
				
				/* for(int loop = 0;  loop < _columnNames.size(); loop++){
					log.warn(loop + ": " + _columnNames.get(loop)+'\n');					
				} */

				cit = _configHeaders.iterator();
				
				while (cit.hasNext()){
					_configHeader = cit.next();
					_configHdrIndex = _configHeader.getHdr_index();
					if(_configHdrIndex >= 0 && _configHdrIndex <= _columnNames.size()){
						_columnName = _columnNames.get(_configHdrIndex - 1);
						if (!_columnName.equals(_configHeader.getSynonym())){
							errorMsg = " The parser expected to find the header " +
							_columnName +
							" at position " +
							_configHdrIndex +
							" in worksheet " +
							_worksheet + 
							", but found " + 
							_configHeader.getSynonym() +
							" instead.";
							log.warn(qcFailLogMsg + 
									'\t' + 
									errorMsg +
									'\n');
							workbookId = MISSING_COLUMN;
							return false;
						}else // for debugging 
						{
							log.debug(" The parser found the header " +
									_columnName +
									" at position " +
									_configHdrIndex +
									" in worksheet " +
									_worksheet + 
									"." + 
									'\n');
						}
					} else {
						log.warn("Index Out of Bounds.");
						errorMsg = " The parser expected to find the header " +
						_configHeader.getSynonym() +
						" at position " +
						_configHdrIndex +
						" in worksheet " +
						_worksheet + 
						", but the array was out of bounds at "+
						_configHdrIndex +
						".";
						log.warn(qcFailLogMsg + 
								'\t' + 
								errorMsg +
								'\n');
						workbookId = MISSING_COLUMN;
						return false;					
					}
				}
			}
		}

		return true;
	}
	
	public void getProperties() {
		ResourceBundle rBundle = null;
		
		try {
			rBundle = ResourceBundle.getBundle(SPREADSHEET_PROPERTIES_FILE_NAME);
			tempDir = new File(rBundle.getString(FILE_DIR));
			db_first = rBundle.getString(DB_FIRST);
			db_last = rBundle.getString(DB_LAST);
			db_tracker = rBundle.getString(DB_TRACKER); 
			db_test = rBundle.getString(DB_TEST);
			db_prod = rBundle.getString(DB_PROD);
			db_dev = rBundle.getString(DB_DEV);
			db_local = rBundle.getString(DB_LOCAL);
		} catch (MissingResourceException mre) {
			rBundle = new MyResources();
			tempDir = new File(rBundle.getString(FILE_DIR));
		}
		
	}
	private File saveTempQcFile(File item) {

		getProperties();
		if (!tempDir.exists()) {
			boolean ret = tempDir.mkdirs();
			if (!ret) {
				log.warn("failed to create directories for "+tempDir.getPath());
			}
		}
		_tempQcFileName = item.getName().toLowerCase();
		String separator = FORWARD_SLASH;
		if (_tempQcFileName.indexOf(separator) != (-1))
			_tempQcFileName = _tempQcFileName.substring(_tempQcFileName.lastIndexOf(separator) + 1);
		separator = DOUBLE_BACK_SLASH;
		if (_tempQcFileName.indexOf(separator) != (-1))
			_tempQcFileName = _tempQcFileName.substring(_tempQcFileName.lastIndexOf(separator) + 1);

		String newFileName = SERVER_FILE_PREFIX + "qcTemp" + new Date().getTime() + _tempQcFileName;
		File _newFile = null;
		try {
			String fname = tempDir.getPath() + File.separator + newFileName;
			log.info("filename = " + fname);
			_newFile = new File(fname);
			_newFile.setExecutable(true);
			_newFile.setReadable(true);
			_newFile.setWritable(true);
			//item.write(_newFile);
			FileUtils.copyFile(item, _newFile);

		} catch (Exception e) {
			log.warn("Error saving temporary QC spreadsheet file! File: "
					+ _newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warn(stack);
			workbookId = -1;
			_newFile = null;
		}
		
		return _newFile;
	}

	/**
	 * File to save the file temporarily so that it can be checked against the configuration.
	 * 
	 * @param item
	 * 		The DiskFileItem passed from the client.
	 * 
	 * @return The file object pointing to the saved location on disk.
	 */
	private File saveTempQcFile(DiskFileItem item) {

		getProperties();
		if (!tempDir.exists()) {
			boolean ret = tempDir.mkdirs();
			if (!ret) {
				log.warn("failed to create directories for "+tempDir.getPath());
			}
		}
		_tempQcFileName = item.getName().toLowerCase();
		if (_tempQcFileName == null)
			_tempQcFileName = item.getString();
		String separator = FORWARD_SLASH;
		if (_tempQcFileName.indexOf(separator) != (-1))
			_tempQcFileName = _tempQcFileName.substring(_tempQcFileName.lastIndexOf(separator) + 1);
		separator = DOUBLE_BACK_SLASH;
		if (_tempQcFileName.indexOf(separator) != (-1))
			_tempQcFileName = _tempQcFileName.substring(_tempQcFileName.lastIndexOf(separator) + 1);

		String newFileName = SERVER_FILE_PREFIX + "qcTemp" + new Date().getTime() + _tempQcFileName;
		File _newFile = null;
		try {
			String fname = tempDir.getPath() + File.separator + newFileName;
			log.info("filename = " + fname);
			_newFile = new File(fname);
			_newFile.setExecutable(true);
			_newFile.setReadable(true);
			_newFile.setWritable(true);
			item.write(_newFile);

		} catch (Exception e) {
			log.warn("Error saving temporary QC spreadsheet file! File: "
					+ _newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warn(stack);
			workbookId = -1;
			_newFile = null;
		}
		
		return _newFile;
	}

	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

	/**
	 * Private method to parse an excel spreadsheet file
	 * 
	 * @param newFile
	 *            Contains the path of the Excel spreadsheet
	 * @throws <Exception>
	 * 
	 */
	private long parseFile(File newFile) throws Exception {
		Session session = null;
		Statement stmt = null;
		Connection conn = null;
		long ret = 0;
		try {
			session = HibernateSessionFactory.getSession();

			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			RowDataDAOHibernate rddh = new RowDataDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			WorkbookFileDAOHibernate wbfdh = new WorkbookFileDAOHibernate();
			WorkbookDAOHibernate wbdh = new WorkbookDAOHibernate();
			wbdh.setSession(session);
			wbfdh.setSession(session);
			mhdh.setSession(session);				
			ctdh.setSession(session);
			dtdh.setSession(session);
			rddh.setSession(session);
			wcdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			WorkbookConfig config = wcdh.findBySynonym(configName);
			Set<SheetConfig> sheets = config.getSheetConfigs();
			Iterator<SheetConfig> sit = sheets.iterator();
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			while (sit.hasNext()) {
				SheetConfig sheetConfig = sit.next();
				String worksheet = sheetConfig.getSynonym();
				SheetData sd = null;
				if (c_or_m) {
					sd = new SheetData();
					sd.setWorkbook_id(workbook);
					sd.setSheet_config_id(sheetConfig);
					session.saveOrUpdate(sd);
					workbook.getSheets().add(sd);
					session.saveOrUpdate(workbook);
				} else {
					workbook = wbdh.findById(workbookId, false);
					Set<SheetData> sheetSet = workbook.getSheets();
					Iterator<SheetData> sit1 = sheetSet.iterator();
					if (sit1.hasNext())
						sd = sit1.next();
					stmt = conn.createStatement();
					long high = 0;
					long low = 9999;
					List<RowData> rows = rddh.findBySheet(sd);
					Iterator<RowData> rit = rows.iterator();
					while (rit.hasNext()) {
						RowData row = rit.next();
						long rn = row.getId();
						if (rn < low) low = rn;
						if (rn > high) high = rn;
					}
					String table = SpreadSheetServiceImpl.getTableName(config.getConfig_name(),sheetConfig.getSheet_name());
					String sql = "delete from `"+table+"` where row_data_id > "+(low-1)+
					             " and row_data_id < "+(high+1);
					//log.info("sql="+sql);
					ret = stmt.executeUpdate(sql);
					log.info(ret+" rows deleted");
					stmt.close();
				}
				
				log.info("Atempting to parse "+ config.getConfig_name() + ":" + newFile.getName() + ":" + worksheet + ", with path "+newFile.getPath());
				
				List<NameValue> nvHeaders = new ArrayList<NameValue>();
				List<RowDTO> rowDtos = null;
				if (newFile.getName().endsWith(EXCEL97)) {
					ExcelSParser parser = new ExcelSParser(handle_errors_as_blanks, newFile, config, session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						errorMsg = parser.getErrorStr();
					} else if (parser.getRetVal() != 0) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else if (newFile.getName().endsWith(EXCEL07)) {
					ExcelSXParser parser = new ExcelSXParser(handle_errors_as_blanks, newFile, config, session);
					//TikaSXParser parser = new TikaSXParser(newFile);
					//parser.setWbConfig(config);
					//parser.setSession(session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						errorMsg = parser.getErrorStr();
					} else if (parser.getRetVal() != 0) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else  if (newFile.getName().endsWith(EXCELMACRO)) {
					ExcelSXParser parser = new ExcelSXParser(handle_errors_as_blanks, newFile, config, session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
					} else if (parser.getRetVal() != 0) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else  if (newFile.getName().endsWith(CSV)) {
					DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, newFile);
					parser.setDELIMITER(COMMA_DELIMITER);
					parser.setWbConfig(config);
					parser.setSession(session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else  if (newFile.getName().endsWith(TAB)) {
					DelimitedFileParser parser = new DelimitedFileParser(handle_errors_as_blanks, newFile);
					parser.setDELIMITER(TAB_DELIMITER);
					parser.setWbConfig(config);
					parser.setSession(session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else  if (newFile.getName().endsWith(NUMBERS)) {
					NumbersParser parser = new NumbersParser(handle_errors_as_blanks, newFile);
					parser.setWbConfig(config);
					parser.setSession(session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else  if (newFile.getName().endsWith(NC)) {
					NetCDFFileParser parser = new NetCDFFileParser(handle_errors_as_blanks, newFile);
					parser.setWbConfig(config);
					parser.setSession(session);
					if (sheetConfig.getMeta_start_row()>0 && sheetConfig.getMeta_end_row()>0)
						nvHeaders = parser.getMetadata(worksheet);
					rowDtos = parser.getData(worksheet);
					if (rowDtos == null) {
						workbookId = parser.getRetVal();
						return workbookId;
					}
				} else {
					log.warn("Unknown file type = "+newFile.getName());
					return -1;
				}
	
				if (nvHeaders == null) return MISSING_INTERNAL;
				//if (c_or_m) {
					if (!nvHeaders.isEmpty()) {
						Iterator<NameValue> nit = nvHeaders.iterator();
						while (nit.hasNext()) {
							NameValue nv = nit.next();
							MetadataHeader header = mhdh.findByNameAndConfig(config, nv.getName());
							if (header == null) {
								log.warn("header for name = " + nv.getName() + " NOT FOUND");
							} else {
								setMetadata(session, header, nv.getName(), nv.getValue());
							}
						}
					}

					Iterator<Metadata> mdit = metadatas.iterator();
					while (mdit.hasNext()) {
						Metadata md = mdit.next();
						md.setWorkbook(workbook);
						try {
							//session.saveOrUpdate(md);
						} catch (HibernateException he) {
							log.warn("Hibernate exception on spreadsheet upload in workbooks. error: " + he);
						}
					}
				//}
				
				if (rowDtos != null) {
					// Save data
					Statement stmt1 = conn.createStatement() ;
					Iterator<RowDTO> it = rowDtos.iterator();
					while (it.hasNext()) {
						RowDTO rowDto = it.next();
						int sheetRow = rowDto.getSheetRow();
						log.warn("sheetRow="+sheetRow);
						if (workbook != null) {
							RowData rd = null;
							try {
								rd = rddh.findByRowNumAndSheet(sheetRow, sd);
							} catch (ConstraintViolationException cve) {
								log.warn("A constraint violation exception was thrown. Exception: "
										+ cve);
								log.warn(cve.getSQL());
								rd = null;
					    	} catch (HibernateException he) {
					    		log.error("Hibernate exception on spreadsheet upload. error: " + he);
								rd = null;
					        } catch (Exception e) {
								log.warn("An exception was thrown. Exception: " + e);
								rd = null;
					        }
							if (rd == null) {
								//log.info("rowData was null");
								//if (sheetRow>0) {
									rd = new RowData();
									rd.setSheet_data_id(sd);
									rd.setRowNum(sheetRow);
									session.save(rd);
									//long id = getMaxValueId("row_data","row_data_id");
									//rd.setId(id+1);
								//}
							}
							boolean first = true;
							boolean modify = false;
							
							List<CellDataHeaderData> cells = new ArrayList<CellDataHeaderData>();

							Iterator<CellDataDTO> it2 = rowDto.getRow().iterator();
							while (it2.hasNext()) {
								CellDataDTO cellData = it2.next();
								CellData cellDatum = new CellData();
								cellDatum.setRowId(rd);
								CellDataHeader ct = null;
								try {
									ct = ctdh.findByNameAndConfig(sheetConfig, cellData.getTag());
								} catch (ConstraintViolationException cve) {
									log.warn("A constraint violation exception was thrown. Exception: "
											+ cve);
									log.warn(cve.getSQL());
									ct = null;
							   	} catch (HibernateException he) {
							   		log.error("Hibernate exception on spreadsheet upload. error: " + he);
									ct = null;
							    } catch (Exception e) {
							    	log.warn("An exception was thrown. Exception: " + e);
									ct = null;
							    }
							    if (ct != null) {
									cellDatum.setCell_hdr_id(ct);
								} else {
									log.warn("cellDataHeader was null");
									ct = new CellDataHeader(cellData.getTag());
									long index = 0;
									index = getCDHIndex(sheetConfig.getSheet_config_id(),cellData.getTag());
									ct.setHdr_index((int)index);
									long cdid = getMaxValueId(SpreadSheetServiceImpl.getTableName(config.getConfig_name(), sheetConfig.getSheet_name()),"cell_data_id");
									cellDatum.setCell_data_id(cdid+1);
									cellDatum.setCell_hdr_id(ct);
									session.save(ct);
								}
						        DataType dt = ct.getTypeId();
								if (dt != null) {
									//ct.setTypeId(dt);
								}
								else {
									log.info("DataType = " + cellData.getType());
									if ((cellData.getType() != null) && (!cellData.getType().isEmpty())) {
										ct.setTypeId(new DataType(cellData.getType()));
										session.save(ct.getTypeId());
									}
								}
								
								if (!session.isOpen())
									session.beginTransaction();
								ValueData gd = cellData.getValue();
								long max = getMaxValueId("value_data","data_id");
								//log.info("max = "+max);
								gd.setData_id(max+1);
								long id = 0;
								if (cellData != null && cellData.getType() != null) {
									if (cellData.getType().equals("LONG")) id = 1;
									else if (cellData.getType().equals("REAL")) id = 2;
									else if (cellData.getType().equals("DATE")) id = 3;
									else if (cellData.getType().equals("STRING")) id = 4;
									else if (cellData.getType().equals("BOOLEAN")) id = 5;
									if (id == 0) {
										id = getIdByDesc(cellData.getType());
									}
								}
								gd.setData_type(id);
								cellDatum.setValueId(gd);
								cellDatum.setRowId(rd);
								String header = ct.getName();
						        //String table = SpreadSheetServiceImpl.getTableName(config.getConfig_name(),sheetConfig.getSheet_name());
								try {
									CellDataHeaderData cdhd = new CellDataHeaderData();
									cdhd.setCellData(cellDatum);
									cdhd.setHeader(header);
									cells.add(cdhd);
									//if (ret < 0) return -1;
									first = false;
								} catch (Exception e) {
									log.error("Exception on getting type. error: " + e);
									log.warn(getStackTrace(e));
								} 
							}
							ret = insertNewCells(SpreadSheetServiceImpl.getTableName(config.getConfig_name(),sheetConfig.getSheet_name()), sheetConfig.getSheet_config_id(), metadatas, cells, first, false, stmt1);
							if (ret < -1000) {
								workbookId = ret;
								errorMsg = "Spreadsheet contains an invalid tracking id/work id pair at row "+(-(ret+1000));
								break;
							//	workbookId = -8;
							//	errorMsg = "Problems parsing spreadsheet. Please check your file for errors.";
							//	break;
							}
						}
					}
					if (stmt1 != null)
						stmt1.close();
				} 
			}
			tx.commit();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (ConstraintViolationException cve) {
			log.warn("A constraint violation exception was thrown. Exception: "
							+ cve);
			log.warn(cve.getSQL());
			log.warn(getStackTrace(cve));
			workbookId = -1;
    	} catch (HibernateException he) {
			workbookId = -1;
    		log.error("Hibernate exception on spreadsheet upload. error: " + he);
    		log.error(getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.error("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception e) {
			log.warn("An exception was thrown. Exception: "
					+ e);
			//System.out.println(getStackTrace(e));
			log.warn(getStackTrace(e));
			workbookId = -1;
        } finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			if (session != null && session.isConnected()) {
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
			}
        }
        if (ret < 0)
        	workbookId = -1;
        return workbookId;
	}

	private long getIdByDesc(String desc) {
		Connection conn = null;
		Statement stmt = null;
		long ret = 0;
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        getProperties();
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "select data_type_id from data_type where description = '"+desc+"'";
			ResultSet rs = stmt.executeQuery( sql ) ;
			if( rs.next() ) {
				ret = rs.getLong(1);
			}
		}catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		      //    se = se.getNextException() ;
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
				{
					conn.close() ;
				}
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return ret;
	}

	private long getMaxValueId(String table, String field) {
		Connection conn = null;
		Statement stmt = null;
		long ret = 0;
        String url = "";
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
			//log.info("url="+url);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "select max("+field+") from `"+table+"`";
			ResultSet rs = stmt.executeQuery( sql ) ;
			if( rs.next() ) {
				ret = rs.getLong(1);
			}
		}catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		     //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		      //    se = se.getNextException() ;
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return ret;
	}

	private long getCDHIndex(long id, String header) {
		Connection conn = null;
		Statement stmt = null;
		long ret = 0;
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = "";
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url = db_local;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV))
	        	url = db_dev;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST))
	        	url += db_test;
	        else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
		        url += db_prod;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "select cell_hdr_id from cell_data_hdr where sheet_config_id = "+id+" and name = '"+header+"'";
			ResultSet rs = stmt.executeQuery( sql ) ;
			if( rs.next() ) {
				ret = rs.getLong(1);
			}
		}catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		      //    se = se.getNextException() ;
		      //}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			log.warn(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warn(getStackTrace(se));
			} catch (Exception e) {
				log.error("Exception on getting type. error: " + e);
				log.warn(getStackTrace(e));
			}
		}
		return ret;
	}

	private long insertNewCells(String table, long sheet_id,
			List<Metadata> metadatas2, List<CellDataHeaderData> cells,
			boolean first, boolean modify, Statement stmt) {
		//Statement stmt = null;
		int ret = 0;
		long rowId = 0;
		String sql = "";
		try {
			sql = "insert into `"+table+"` (sheet_config_id, row_data_id ";
			if (!metadatas.isEmpty()) {
				Iterator<Metadata> mit = metadatas.iterator();
				while (mit.hasNext()) {
					Metadata meta = mit.next();
					sql += ", `"+ meta.getMetadata_hdr_id().getName() +"`";
				}
			}
			// Get a statement from the connection
			//stmt = conn.createStatement() ;
			long trackingIdCol = 0;
			long workIdCol = 0;
			long colCtr = 0;
			Iterator<CellDataHeaderData> cit = cells.iterator();
			while (cit.hasNext()) {
				CellDataHeaderData cdhd = cit.next();
				CellData cell = cdhd.getCellData();
				String header = cdhd.getHeader();
				if (header.toLowerCase().equals("tracking id"))
					trackingIdCol = colCtr;
				if (header.toLowerCase().equals("work id"))
					workIdCol = colCtr;
				if (cell.getValueId() == null)
					log.info("value_id was null");
				else if (cell.getValueId().getData_id() == 0)
					log.info("value data_id was null");
				else if (cell.getCell_hdr_id()==null)
					log.info("cell_hdr_id was null");
				else if (cell.getCell_hdr_id().getCell_hdr_id()==0)
					log.info("cell_hdr_id.cell_hdr_id is null");
				else if (cell.getRowId() == null)
					log.info("row_id was null");
				sql += ",`"+header +"`";
				rowId = cell.getRowId().getId();
				colCtr++;
			}
			sql += ") values (";
			sql += sheet_id+","+rowId;			
			if (!metadatas.isEmpty()) {
				Iterator<Metadata> mit = metadatas.iterator();
				while (mit.hasNext()) {
					Metadata meta = mit.next();
					long mtype = meta.getValue().getData_type();
					String mval = "";
					if (mtype==AppConstants.LONG) {
						long lval = meta.getValue().getLvalue();
						if (lval != AppConstants.DEFAULT_LONG)
							mval = String.valueOf(lval);
						else
							mval = null;
					} else if (mtype==AppConstants.REAL) {
						double rval = meta.getValue().getRvalue();
						if (rval != AppConstants.DEFAULT_REAL)
							mval = String.valueOf(rval);
						else
							mval = null;
					} else if (mtype==AppConstants.DATE) {
						Date dval = meta.getValue().getDvalue();
						if (dval != null) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(dval);
							int year = cal.get(Calendar.YEAR);
							//log.info("year="+year);
							int mon = cal.get(Calendar.MONTH)+1;
							//log.info("mon="+mon);
							String smon = "";
							if (mon < 10) 
								smon = "0";
							smon += String.valueOf(mon);
							int day = cal.get(Calendar.DAY_OF_MONTH);
							//log.info("day = "+day);
							String sday = "";
							if (day < 10) 
								sday = "0";
							sday += String.valueOf(day);
							mval = String.valueOf(year) + "-" + smon + "-" + sday;
						} else mval = null;
					} else if (mtype==AppConstants.STRING) {
						mval = meta.getValue().getSvalue();
					} else if (mtype==AppConstants.BOOLEAN) {
						//mval = String.valueOf(meta.getValue().getBvalue());
						if (meta.getValue().getBvalue()==true)
							mval = "1";
						else 
							mval = "0";
					} else {
						log.error("TYPE NOT SET");
					}
					if (mtype == AppConstants.BOOLEAN) {
						if (mval != null)
							sql += ", "+ mval +" ";
						else
							sql += ", null ";							
					} else {
						if (mval != null)
							sql += ", '"+ mval +"' ";
						else
							sql += ", null ";
					}
				}
			}
			colCtr = 0;
			cit = cells.iterator();
			while (cit.hasNext()) {
				String trackingId = "";
				String workId = "";
				CellDataHeaderData cdhd = cit.next();
				CellData cell = cdhd.getCellData();
				//String header = cdhd.getHeader();
			
				long type = cell.getValueId().getData_type();
				String val = "";
				if (type==AppConstants.LONG) {
					long lval = cell.getValueId().getLvalue();
					if (lval != AppConstants.DEFAULT_LONG)
						val = String.valueOf(lval);
					else
						val = null;
					//val = String.valueOf(cell.getValueId().getLvalue());
				} else if (type==AppConstants.REAL) {
					double rval = cell.getValueId().getRvalue();
					if (rval != AppConstants.DEFAULT_REAL)
						val = String.valueOf(rval);
					else
						val = null;
					//val = String.valueOf(cell.getValueId().getRvalue());
				} else if (type==AppConstants.DATE) {
					Date dval = cell.getValueId().getDvalue();
					if (dval != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(dval);
						int year = cal.get(Calendar.YEAR);
						//log.info("year="+year);
						int mon = cal.get(Calendar.MONTH)+1;
						//log.info("mon="+mon);
						String smon = "";
						if (mon < 10) 
							smon = "0";
						smon += String.valueOf(mon);
						int day = cal.get(Calendar.DAY_OF_MONTH);
						//log.info("day = "+day);
						String sday = "";
						if (day < 10) 
							sday = "0";
						sday += String.valueOf(day);
						val = String.valueOf(year) + "-" + smon + "-" + sday;
					} else { 
						val = null;
					}
				} else if (type==AppConstants.STRING) {
					val = cell.getValueId().getSvalue();
				} else if (type==AppConstants.BOOLEAN) {
					//val = String.valueOf(cell.getValueId().getBvalue());
					if (cell.getValueId().getBvalue()==true)
						val = "1";
					else 
						val = "0";
				} else {
					//log.error("TYPE NOT SET");
				}
				//log.warn("in insertNewCells-modify="+modify);
				if (type != AppConstants.STRING && type != AppConstants.DATE) {
					if (val == null || val.isEmpty()) 
						val = "null";
					sql += ", "+val;//+")";
				}
				else if (val != null)
					sql += ", '"+val+"'";//)";
				else 
					sql += ", null";
				if (trackingIdCol == colCtr) {
					trackingId = val;
				} else if (workIdCol == colCtr) {
					workId = val;
				}
				if (!trackingId.isEmpty()&&!workId.isEmpty()) {
					if (!validateIds(trackingId,workId)) {
						long retval = -1000-rowId;
						System.out.println("returning "+retval);
						return -retval;
					}
				}
				colCtr++;
			}
			if (!modify) sql += ")";
			//log.warn("sql = "+sql);
			ret = stmt.executeUpdate( sql ) ;
			//stmt.addBatch(sql);
		}catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
		      log.warn( "SQL Exception:" ) ;
			log.warn("Problem at row ="+rowId+" SQL: "+sql);
			System.out.println("Problem at row ="+rowId+" SQL: "+sql);
			  log.warn(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      //while( se != null )
		      //{
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          log.warn( "Message: " + se.getMessage()   ) ;
		      //    se = se.getNextException() ;
		      //}
		} catch (Exception e) {
			log.warn("Problem at row ="+rowId+" SQL: "+sql);
			System.out.println("Problem at row ="+rowId+" SQL: "+sql);
			log.error("Exception performing insert. error: " + e);
			log.warn(getStackTrace(e));
		} 
		return ret;
	}


	private boolean validateIds(String trackingId, String workId) {
		boolean ret = true;
		String sql = "select r.work_done_id "+ 
		 "from relation r, sample s, sample_relation sr "+ 
		 "where r.relation_id = sr.relation_id and "+
		 "sr.id = s.id and s.id = "+trackingId+" and r.work_done_id="+workId;
		String dbPath = "";
		dbPath = gov.nrel.nbc.security.client.AppConstants.INITIAL_PART_DB;
		dbPath += gov.nrel.nbc.security.client.AppConstants.FIRST_PART_DB;
		dbPath += DevTestProdConstants.DEV_PROD_TEST;
		dbPath += "/" + "tracker";//rBundle.getString(DB_BIOMASS);
		log.warn("db="+dbPath);
		String dbUser = "trackeruser";
		String dbPass = "trackeruserpw";
		DBUtils db = new DBUtils(dbPath,dbUser,dbPass);
		db.performQuery(sql, true);
		if (db.getNextRow()) {
			long wid = db.getLongColumn(1);
			log.warn("wid="+wid);
			ret = true;
		} else {
			log.warn("no work id found");
			ret = false;
		}
		db.close();
		return ret;
	}

	/**
	 * Private method to save a file reference to the db
	 * 
	 * @param fileName
	 *            Contains the name of the Excel spreadsheet file
	 * @param path
	 *            Contains the path of the new file
	 * @throws ServletException 
	 * 
	 */
	private void saveFile(String fileName, String path) throws ServletException {
		theFile = new WorkbookFileData();
		theFile.setFilename(fileName);
		theFile.setPath(path);

		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();

			Transaction tx = session.beginTransaction();

			session.save(theFile);

			if (workbook == null)
				workbook = new WorkbookData();
			workbook.setWorkbook_file_id(theFile);
			 
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			WorkbookConfig config = null;
			if (configName != null && !configName.isEmpty())
				config = wcdh.findBySynonym(configName);
			else {
				log.error("Configuration was null.\nSpreadsheet could not be saved.");
				throw new ServletException("Configuration was null. Spreadsheet could not be saved.");
			}
			workbook.setWorkbook_config_id(config);
			
			if (attachments != null && !attachments.isEmpty())
				workbook.getAttachments().addAll(attachments);

			session.saveOrUpdate(workbook);
			
			tx.commit();
		} catch (HibernateException he) {
    		log.error("Hibernate exception on Opto ingest. error: " + he);
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.error("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } finally {
        	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
        }
	}

	/**
	 * Private method to save a file reference to the db
	 * 
	 * @param fileName
	 *            Contains the name of the Excel spreadsheet file
	 * @param path
	 *            Contains the path of the new file
	 * @throws ServletException 
	 * 
	 */
	private void updateFile(String fileName, String path) throws ServletException {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			
			WorkbookFileDAOHibernate wfdh = new WorkbookFileDAOHibernate();
			WorkbookDAOHibernate wddh = new WorkbookDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wddh.setSession(session);
			wcdh.setSession(session);
			wfdh.setSession(session);
			
			Transaction tx = session.beginTransaction();

			workbook = wddh.findById(workbookId, true);
			WorkbookFileData wfd = wfdh.findById(workbook.getWorkbook_file_id().getWorkbook_file_id(), true);
			wfd.setFilename(fileName);
			wfd.setPath(path);
			session.saveOrUpdate(wfd);

			//theFile = new WorkbookFileData();
			//theFile.setFilename(fileName);
			//theFile.setPath(path);
			//if (!session.isOpen()) {
			//	session = HibernateSessionFactory.getSession();
			//	wcdh.setSession(session);
			//	wfdh.setSession(session);				
			//}
			//if (!tx.isActive()) {
			//	tx = session.beginTransaction();
			//}

			workbook.setWorkbook_file_id(wfd);
			 
			WorkbookConfig config = null;
			if (configName != null && !configName.isEmpty())
				config = wcdh.findBySynonym(configName);
			else {
				log.error("Configuration was null.\nSpreadsheet could not be saved.");
				throw new ServletException("Configuration was null. Spreadsheet could not be saved.");
			}
			workbook.setWorkbook_config_id(config);
			
			//if (attachments != null && !attachments.isEmpty())
			//	workbook.getAttachments().addAll(attachments);

			session.saveOrUpdate(workbook);
			
			tx.commit();
		} catch (HibernateException he) {
    		log.error("Hibernate exception on spreadsheet ingest. error: " + he);
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.error("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } finally {
        	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
        }
	}

}
