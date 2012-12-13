package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.dao.AttachmentTypeDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.SampleDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.model.AttachmentType;
import gov.nrel.nbc.labelprinting.model.Attachments;
import gov.nrel.nbc.labelprinting.model.Sample;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AttachmentFileUploadServiceImpl extends HttpServlet implements
		AppConstants {

	private static final long serialVersionUID = 2312439429518358010L;
    /**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(AttachmentFileUploadServiceImpl.class);

	/**
	 * A public method that handles the doPost message
	 * 
	 * @param req <HttpServletRequest>
	 * @param rsp <HttpServletResponse>
	 */
    @SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
    	log.fine("doPost called!");
    	String fileName = "";
    	String clientPath = "";
    	String serverPath = "";
    	String trackingId = "";
    	long id = 0;
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart == true) {
			FileItemFactory itemFactory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(itemFactory);
			try {
				List<DiskFileItem> items = (List<DiskFileItem>)upload.parseRequest(req);
				Iterator<DiskFileItem> it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if (item.isFormField() == false) {
						serverPath = processFile(item);
					}
				}
				it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if (item.isFormField()==true){
						String name = item.getFieldName();
						String value = item.getString();
						log.fine(name + " = " + value);
						if (name.equals("name")) fileName = value;
						//else if (name.equals("path")) filePath = value;
						else if (name.equals("file")) clientPath = value;
						else if (name.equals("trackingId")) trackingId = value;
					}
				}
				
				if (!items.isEmpty()) {
					id = persistData(trackingId,fileName,serverPath);
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
    /**
     * Private method to save the trb, location and sample data
     * to the database.
     */
    private long persistData(String trackingId, String fileName, String path) {
    	log.fine("persistData called!");
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
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);
			long trackId = 0;
			if (trackingId != null && !trackingId.isEmpty()) {
				try {
					trackId = Long.parseLong(trackingId);
				} catch (NumberFormatException nfe) {
					log.severe("Problem parsing trackingId="+trackingId+": "+nfe);
				}
			}
			Sample sample = null;
			if (trackId != 0) {
				sample = sdh.findById(trackId, false);
			}
			if (sample != null) {
				attachment.setSample(sample);
			} else {
				//throw new Exception ("sample not found for tracking id="+trackingId);
			}
			session.saveOrUpdate(attachment);
			attach_id = attachment.getAttachment_id();
			
			tx.commit();
    	} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(getStackTrace(he));
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
        return attach_id;
	}
	/**
	 * Private method that saves the file with a unique name for use by the
	 * searching process.
	 * 
	 * @param item
	 *            Contains the path of the tag list file.
	 * @return Path of newly created local tag list file.
	 */
	private String processFile(DiskFileItem item) {
		ResourceBundle rBundle = ResourceBundle
				.getBundle(TRACKER_PROPERTIES_FILE_NAME);
		File tempDir = new File(rBundle.getString(FILE_DIR));
		tempDir.mkdirs();
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
			String fname = tempDir.getPath() + File.separator + newFileName;
			log.info("filename = " + fname);
			newFile = new File(fname);
			newFile.setExecutable(true);
			newFile.setReadable(true);
			newFile.setWritable(true);
			item.write(newFile);

			//saveFile(fileName, newFile.getPath());
		} catch (Exception e) {
			log.warning("Error writing calc file to disk! File: "
					+ newFile.getPath() + "; Error: " + e);
			String stack = getStackTrace(e);
			log.warning(stack);
		}
		return newFile.getPath();
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

}
