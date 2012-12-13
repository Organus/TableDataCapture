package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.dao.AttachmentsDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.model.Attachments;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * A servlet that implements the methods for downloading a file.
 * 
 * @author jalbersh
 * 
 */
public class FileDownloadService extends HttpServlet implements AppConstants {
	private static final String ATTACHMENT_ID = "attachmentId";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final long serialVersionUID = 217721541663385449L;
	
	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(FileDownloadService.class);
	
	private String fileName;

	/**
	 * Method to perform get for servlet. File is deleted
	 * upon completion of processing.
	 * 
	 * @param req <HttpServletRequest> The servlet request object
	 * @param rsp <HttpServletResponse> The servlet response object
	 * 
	 * @throws <ServletException>
	 * @throws <IOException>
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
        
        String attachmentIdString = (String)req.getParameter(ATTACHMENT_ID);
        
        String filePath = getFilePath(attachmentIdString);
        if (filePath != null) {
        	String contentType = getServletContext().getMimeType(filePath);
            rsp.setContentType(contentType);
            rsp.addHeader(CONTENT_DISPOSITION,
                          "attachment; filename=\"" + fileName + "\"");
            
	        OutputStream out = rsp.getOutputStream();
	        returnFile(filePath, out);
        }
    }
    
    /**
     * Private method to retrieve the file path for the file.
     * 
     * @param idString - <String> <Attachments> ID
     * @return <String> file path
     */
    private String getFilePath(String idString) {
		Session session = null;
		String path = null;
		try {
			session = HibernateSessionFactory.getSession();

			AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
			adh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			long attachmentId = -1;
			try {
				attachmentId = Long.parseLong(idString);
			} catch (NumberFormatException e) {
				// do nothing
			}

			if (attachmentId != -1) {
				Attachments attachment = adh.findById(attachmentId);
				
				if (attachment != null) {
					path = attachment.getPath();
					fileName = attachment.getFilename();
				}
			}
			
			tx.commit();
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		return path;
    }

	/**
	 * A method that returns a file'S contents to standard output
	 * 
	 * @param fileName <String> The name of the file to return
	 * @param out <OutputStream> The output stream
	 * 
	 * @throws <FileNotFoundException>
	 * @throws <IOException>
	 */
	public static void returnFile(String fileName, OutputStream out)
			throws FileNotFoundException, IOException {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buf = new byte[4 * 1024];
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}			
		} finally {
			if (in != null)
				in.close();
		}		
	}
}
