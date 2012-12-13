package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.dao.AttachmentTypeDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.AttachmentType;
import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl.MyResources;
import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * A servlet that implements the methods for uploading an attachment file.
 * 
 * @author James Albersheim
 * 
 */
public class AttachmentFileUploadServiceImpl extends HttpServlet implements
		AppConstants {

	private static final long serialVersionUID = 2312439429518358010L;
    /**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(AttachmentFileUploadServiceImpl.class);

    /**
     * Cached contents of the file.
     */
	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
	/**
	 * A public method that handles the doPost message
	 * 
	 * @param req <HttpServletRequest>
	 * @param rsp <HttpServletResponse>
	 * 
	 * @throws <ServletException>
	 * @throws <IOException>
	 */
    @SuppressWarnings("unchecked")
	//@Override
	public void doPost(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
    	log.fine("doPost called!");
    	String fileName = "";
    	String clientPath = "";
    	String serverPath = "";
    	String tableId = "";
    	long id = 0;
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart == true) {
			File tempDir = null;
			ResourceBundle rBundle = null;
			
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
			upload.setFileSizeMax(1000*1024*1024);
			try {
				List<DiskFileItem> items = (List<DiskFileItem>)upload.parseRequest(req);
				Iterator<DiskFileItem> it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if (item.isFormField() == false) {
						serverPath = processFile(item, tdir);
					}
				}
				it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if (item.isFormField() == true){
						String name = item.getFieldName();
						String value = item.getString();
						log.fine(name + " = " + value);
						if (name.equals("name"))
							fileName = value;
						else if (name.equals("file"))
							clientPath = value;
						else if (name.equals("id"))
							tableId = value;
					}
				}
				
				if (!items.isEmpty()) {
					id = persistData(tableId,fileName,serverPath);
				}
				else
					log.warning("There were no parameters passed to doPost!");					
			} catch (FileUploadException fue) {
				log.warning("Error uploading file! Error: " + fue);
			}
			if (id != 0) {
				PrintWriter writer = rsp.getWriter();
				writer.write(Long.toString(id));
				writer.close();
			} else {
				log.warning("The attachment was null!");
			}
		}
		log.info("clientPath="+clientPath);
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
		//@Override
		public Enumeration<String> getKeys() {
			return (Enumeration<String>) contents.keySet();
		}
		
		//@Override
		protected Object handleGetObject(String key) {
			return contents.get(key);
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
    
    /**
     * Private method to save the file information to the database.
     * 
     * @param tableId <String> - <WorkDone> ID
     * @param fileName <String> - file name passed to servlet
     * @param path <String> - server path of file.
     * 
     * @return long <Attachments> ID
     */
    private long persistData(String tableId, String fileName, String path) {
    	if (tableId != null)
    		log.info("persistData called with tableId="+tableId);
    	else
    		log.info("persistData called with tableId set to NULL");    		
    	Session session = null;
    	long attach_id = 0;
    	try {
			session = HibernateSessionFactory.getSession();
			AttachmentTypeDAOHibernate atdh = new AttachmentTypeDAOHibernate();
			atdh.setSession(session);
			
			Transaction tx = session.beginTransaction();
			
			AttachmentType type = null;
			int index = fileName.lastIndexOf(".");
			if (index > -1) {
				String ext = fileName.substring(index+1).toUpperCase();
				type = atdh.findByExtension(ext);
				if (type == null) {
					type = new AttachmentType();
					type.setExt(ext);
					session.saveOrUpdate(type);
				}
			}
			
			Attachments attachment = new Attachments();
			attachment.setFilename(fileName);
			attachment.setPath(path);
			attachment.setType_id(type);
			
			WorkbookDAOHibernate wddh = new WorkbookDAOHibernate();
			wddh.setSession(session);
			
			long longTableId = 0;
			if (tableId != null && !tableId.isEmpty()) {
				try {
					longTableId = Long.parseLong(tableId);
				} catch (NumberFormatException nfe) {
					log.severe("Problem parsing tableId=" + tableId + ": " + nfe);
				}
				WorkbookData workbookData = null;
				if (longTableId != 0) {
					workbookData = wddh.findById(longTableId, false);
				}
				if (workbookData != null) {
					Set<Attachments> attachments = workbookData.getAttachments();
					if (attachments == null)
						attachments = new HashSet<Attachments>();
					attachments.add(attachment);
					workbookData.setAttachments(attachments);
				} else {
					//throw new Exception ("sample not found for tracking id="+trackingId);
				}
				session.saveOrUpdate(workbookData);
			}
			attach_id = ((Long)session.save(attachment)).longValue();
			
			tx.commit();
    	} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		//log.severe(getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
        }
        return attach_id;
	}
    
	/**
	 * Private method that saves the file with a unique name for use by the
	 * searching process.
	 * 
	 * @param item <DiskFileItem>
	 *            Contains the path of the tag list file.
	 * @return <String> - Path of newly created local tag list file.
	 */
	private String processFile(DiskFileItem item, File tdir) {
		String fileName = item.getName();
		String separator = "/";
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
		separator = "\\";
		if (fileName.indexOf(separator) != (-1))
			fileName = fileName.substring(fileName.lastIndexOf(separator) + 1);
 
		String newFileName = "upload" + new Date().getTime() + fileName;
		File newFile = null;
		try {
			String fname = tdir.getPath() + File.separator + newFileName;
			log.info("filename = " + fname);
			newFile = new File(fname);
			newFile.setExecutable(true);
			newFile.setReadable(true);
			newFile.setWritable(true);
			item.write(newFile);

			//saveFile(fileName, newFile.getPath());
		} catch (Exception e) {
			log.warning("Error writing workbook file to disk! File: "
					+ newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warning(stack);
		}
		return newFile.getPath();
	}
	
	/**
	 * Public static method to retrieve a stack track
	 *  as a <String>.
	 *  
	 * @param t <Throwable> - The Exception
	 * @return <String>
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
}
