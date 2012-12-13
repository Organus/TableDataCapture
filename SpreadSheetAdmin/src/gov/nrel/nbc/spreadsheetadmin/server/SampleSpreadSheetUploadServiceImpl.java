package gov.nrel.nbc.spreadsheetadmin.server;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheetadmin.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookFileDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookFileData;
import gov.nrel.nbc.spreadsheetadmin.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheetadmin.parse.DelimitedFileParser;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelParser;
import gov.nrel.nbc.spreadsheetadmin.parse.ExcelSXParser;
import gov.nrel.nbc.spreadsheetadmin.utilities.XLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * A servlet that implements the methods for uploading a sample workbook file.
 * 
 * @author James Albersheim
 * 
 */
public class SampleSpreadSheetUploadServiceImpl extends HttpServlet implements
		AppConstants {

	private static final String FIXED_QUOTE = "`";

	private static final String SINGLE_QUOTE = "'";

	private static final String SERVER_FILE_PREFIX = "calc";

	private String configName = "";
	
	/**
	 * Name of file
	 */
	private String fileName = null;

	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
	/**
	 * A serialization identifier.
	 */
	private static final long serialVersionUID = -4515434818206581128L;

	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(XLogger.INFO);

	/**
	 * filename and list of sheets for workbook
	 */
	private String sheetInfo = "";

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
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException {
		ResourceBundle rBundle = ResourceBundle.getBundle(SPREADSHEET_PROPERTIES_FILE_NAME);

        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.local.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.local.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.local.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.DEV)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.dev.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.dev.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.dev.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.prod.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.prod.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.prod.logDirectory"));
        }
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.TEST)){
        	System.setProperty("java.io.tmpdir",rBundle.getString("spreadsheetadmin.test.tempDirectory"));
	    	System.setProperty("java.io.filedir",rBundle.getString("spreadsheetadmin.test.fileDirectory"));
	    	System.setProperty("java.io.logdir",rBundle.getString("spreadsheetadmin.test.logDirectory"));
        }

        log.info("DEV_PROD_TEST="+DevTestProdConstants.DEV_PROD_TEST);
        log.info("tmp="+System.getProperty("java.io.tmpdir"));
        File f = new File(System.getProperty("java.io.tmpdir")+"/poifiles");
		if (!f.exists()) {
			f.mkdirs();
		}
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		String filename = "";
		if (isMultipart == true) {
			File tempDir = null;
			
			try {
				rBundle = ResourceBundle.getBundle(SPREADSHEET_PROPERTIES_FILE_NAME);
				tempDir = new File(rBundle.getString(FILE_DIR));
			} catch (MissingResourceException mre) {
				log.warning("in missing resource exception");
				rBundle = new MyResources();
				tempDir = new File(rBundle.getString(FILE_DIR));
			}
	        if (tempDir.exists() == false) {
	            tempDir.mkdirs();
	        }
	        File tdir = createTempDir(tempDir.getPath());
			FileItemFactory itemFactory = new DiskFileItemFactory(1000*1024*1024,tdir);
			ServletFileUpload upload = new ServletFileUpload(itemFactory);
			try {
				List<DiskFileItem> items = (List<DiskFileItem>) upload.parseRequest(req);
				
				// Get parameters and save
				Iterator<DiskFileItem> it = items.iterator();
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
				
				// Get file and save
				it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if ((item.isFormField() == false) && (item.getFieldName().equalsIgnoreCase(EXCEL_FILE))) {
						String name = item.getName();
						if (name.endsWith(EXCEL97) || name.endsWith(EXCEL07) || name.endsWith(EXCELMACRO) || name.endsWith(CSV) || name.endsWith(TAB) || name.endsWith(NUMBERS) || name.endsWith(NC)) {
							filename = processFile(item, tdir);
							//processFile(req);
						} else {
							log.warning(name + " is not a file we can upload.\nPlease choose an '" + EXCEL97 + "' file, " + "an '" + EXCEL07 + "' file, an '" + EXCELMACRO + "' file, n '" + CSV + "' file, n '" + TAB + "' file, or a '" + NUMBERS + "' file, or a '" + NC + "' file.");
						}
					}
				}
			} catch (FileUploadException fue) {
				log.warning("Error uploading spreadsheet file! Error: " + fue);
			}

			PrintWriter writer = rsp.getWriter();
			writer.write(filename + SHEET_SEPARATOR+String.valueOf(sheetInfo));
			writer.close();
			sheetInfo = "";
		}
	}

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
    
	private class MyResources extends ResourceBundle {
		 private HashMap<String,String> contents = new HashMap<String,String>();
		 
		 /**
		  * Default Constructor
		  */
		 public MyResources() {
		    	//	 # Temp directory for temporary files
			 contents.put(tempDirKey,tempDirValue);
		    	//	 # Directory to store attachments
		     contents.put(fileDirKey,fileDirValue);
		    	//	 # Directory to create log file for files processed.
		     contents.put(logDirKey,logDirValue);	             
		 }
		 
		 /**
		  * Get array contents
		  * 
		  * @return Object[][] - key/value pairs
		  */
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
		    		 {logDirKey,logDirValue}	             
		    		 // END OF MATERIAL TO LOCALIZE
		    };
		 }
		 
		
		@SuppressWarnings("unchecked")
		@Override
		public Enumeration<String> getKeys() {
			return (Enumeration<String>) contents.keySet();
		}
		
		@Override
		protected Object handleGetObject(String key) {
			return contents.get(key);
		}
	}
	
	/**
	 * Private method that saves the file with a unique name for use by the
	 * searching process.
	 * 
	 * @param item DiskFileItem - Contains the path of the tag list file.
	 * @return String Path of newly created local tag list file.
	 */
	private String processFile(DiskFileItem item, File tdir) {
	        
		fileName = item.getName();
		String separator = FORWARD_SLASH;
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		separator = DOUBLE_BACK_SLASH;
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);

		String newFileName = SERVER_FILE_PREFIX + new Date().getTime() + fileName;
		File newFile = null;
		try {
			String fname = tdir.getPath() + File.separator + newFileName;
			log.info("filename = " + fname);
			newFile = new File(fname);
			newFile.setExecutable(true);
			newFile.setReadable(true);
			newFile.setWritable(true);
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	FileUtils.copyFile(new File(item.getName()), newFile);
	        else
	        	item.write(newFile);

			parseFile(newFile);

		} catch (Exception e) {
			log.warning("Error writing spreadsheet file to disk! File: "
					+ newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warning(stack);
		}
		return newFile.getPath();
	}
	
	/**
	 * Public static method to get a Stack Trace.
	 * 
	 * @param t <Throwable> - Exception
	 * @return String - Stack trace
	 */
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
	 * Private method to save the given meta data name/value pair to
	 * global variables.
	 * 
	 * @param name String
	 *            The meta data name to save
	 * @param value String
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
		else { // Meta Data
			Transaction tx = null;
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();

				tx = session.beginTransaction();

				WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
				MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
				mhdh.setSession(session);
				wcdh.setSession(session);
					
				String synonym = configName;
				configName = AdminServiceImpl.removeBadCharacters(configName);
				WorkbookConfig config = wcdh.findBySynonym(configName);
				if (config == null) {
					config = new WorkbookConfig();
					config.setConfig_name(configName);
					config.setSynonym(synonym);
				}
				session.saveOrUpdate(config);
				tx.commit();
				
			} catch (NumberFormatException nfe) {
				// do nothing
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	    		log.severe(AdminServiceImpl.getStackTrace(he));
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	    		log.severe(getStackTrace(ex));
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
			
		}
	}

	/**
	 * Private method to parse an excel spreadsheet file
	 * 
	 * @param newFile File
	 *            Contains the path of the Excel spreadsheet
	 * @throws <Exception>
	 */
	private void parseFile(File newFile) throws Exception {
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			session = HibernateSessionFactory.getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookFileDAOHibernate wfdh = new WorkbookFileDAOHibernate();
			wfdh.setSession(session);
			wcdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			List<String> sheets = null;
			if (newFile.getName().endsWith(EXCEL97)) {
				ExcelParser parser = new ExcelParser(newFile, null, session);
				sheets = parser.getSheets();
			} else if (newFile.getName().endsWith(EXCEL07)) {
				ExcelSXParser parser = new ExcelSXParser(newFile, null, session);
				sheets = parser.getSheets();
			} else  if (newFile.getName().endsWith(EXCELMACRO)) {
				ExcelSXParser parser = new ExcelSXParser(newFile, null, session);
				sheets = parser.getSheets();
			} else  if (newFile.getName().endsWith(CSV)) {
				DelimitedFileParser parser = new DelimitedFileParser(newFile);
				parser.setDELIMITER(COMMA_DELIMITER);
				parser.setWbConfig(null);
				parser.setSession(session);
				sheets = parser.getSheets();
			} else  if (newFile.getName().endsWith(TAB)) {
				DelimitedFileParser parser = new DelimitedFileParser(newFile);
				parser.setDELIMITER(TAB_DELIMITER);
				parser.setWbConfig(null);
				parser.setSession(session);
				sheets = parser.getSheets();
			} 
	
			Iterator<String> sit = sheets.iterator();
			while (sit.hasNext()) {
				String sheet = sit.next();
				if (!sheetInfo.isEmpty()) 
					sheetInfo += SHEET_SEPARATOR;
				sheetInfo += sheet;
			}
			String synonym = configName;
			configName = AdminServiceImpl.removeBadCharacters(configName);
			WorkbookConfig config = wcdh.findByName(configName);
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
	        stmt = conn.createStatement();
			if (config == null) {
				long id = 0;
				log.info("next config id="+wcdh.getNextId());
				config = new WorkbookConfig();
				config.setConfig_name(configName);
				config.setSynonym(synonym);
				long id1 = (Long)session.save(config);
				String path = newFile.getPath();
				WorkbookFileData wbFile = wcdh.findFile(path);
				if (wbFile == null) {
					log.info("new workbookFileData");
					wbFile = new WorkbookFileData();
					String name = path;
					String separator = FORWARD_SLASH;
					if (name.indexOf(separator) != (-1))
						name = name.substring(name.lastIndexOf(separator) + 1);
					separator = DOUBLE_BACK_SLASH;
					if (name.indexOf(separator) != (-1))
						name = name.substring(name.lastIndexOf(separator) + 1);
					log.info("name="+name);
					log.info("path="+path);
					id = wfdh.getNextId();
					wbFile.setWorkbook_file_id(id);
					wbFile.setFilename(name);
					wbFile.setPath(path);
					//session.createQuery("insert")
					//session.saveOrUpdate(wbFile);
					// Get a connection to the database
					path = path.replace(DOUBLE_BACK_SLASH, FORWARD_SLASH);
			        String sql = "insert into workbook_file_data "
			        	       + "(workbook_file_id, filename, path) values "
			        	       + "("+id+",'"+name+"','"+path+"')";
			        log.info("insert sql="+sql);
			        int ret = stmt.executeUpdate(sql);
			        log.info("inserted "+ret+" rows");
				}
				//config.getWorkbook_file().add(wbFile);
				if (id != 0) {
					String sql = "insert into workbook_file_join (workbook_config_id, workbook_file_id) "
						+ " values ("+id1+","+id+")";
			        log.info("insert sql="+sql);
			        int ret = stmt.executeUpdate(sql);
			        log.info("inserted "+ret+" rows");
				}
			}
			//session.saveOrUpdate(config);
			tx.commit();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (ConstraintViolationException cve) {
			log.warning("A constraint violation exception was thrown. Exception: "
							+ cve);
			log.warning(cve.getSQL());
			log.warning(getStackTrace(cve));
    	} catch (HibernateException he) {
    		log.severe("Hibernate exception on spreadsheet upload. error: " + he);
    		log.severe(getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } finally {
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warning(getStackTrace(se));
			} catch (Exception e) {
				log.severe("Exception on getting type. error: " + e);
				log.warning(getStackTrace(e));
			}
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
	}

	/**
	 * Inner class that implements a renaming of a file if a duplicate file is
	 * detected.
	 * 
	 * @author James Albersheim
	 * 
	 */
	public class CalcFileRenamePolicy implements FileRenamePolicy {

		/**
		 * Method to rename a file if a duplicate file exists
		 * 
		 * @param arg0 File
		 *            The file to rename
		 * 
		 */
		public File rename(File arg0) {
			String fileName = arg0.getName();
			StringBuffer fileNameBuf = new StringBuffer(fileName);
			fileNameBuf.insert(0, new Date().getTime());
			File renamedFile = new File(fileNameBuf.toString());
			if (arg0.renameTo(renamedFile))
				return arg0;
			else
				return renamedFile;
		}
	}
}
