package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.dao.TrbDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.TrbSessionFactory;
import gov.nrel.nbc.labelprinting.model.Trb;
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
public class PDFDownloadService extends HttpServlet implements AppConstants {
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CONTENT_TYPE = "application/pdf";
	private static final long serialVersionUID = 217721541663385449L;
	
	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(PDFDownloadService.class);

	/**
	 * Method to perform get for servlet. File is deleted
	 * upon completion of processing. Returns a list of techName strings.
	 * 
	 * @param req <HttpServletRequest> The servlet request object
	 * @param rsp <HttpServletResponse> The servlet response object
	 * 
	 * @throws <ServletException>
	 * @throws <IOException>
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
        rsp.setContentType(CONTENT_TYPE);
        
        String trbPage = (String)req.getParameter(TRB_PAGE);
        String trbNum = (String)req.getParameter(TRB_NUM);
        
        rsp.addHeader(CONTENT_DISPOSITION,
                      "attachment; filename=" + trbNum + "-" + trbPage + ".pdf");
        
        int page = 0;
        int num = 0;
        try {
        	page = Integer.parseInt(trbPage);
        	num = Integer.parseInt(trbNum);
        } catch (NumberFormatException nfe) {
        	// stupid
        }
        String fileName = getFilename(num,page);
        if (fileName != null) {
	        OutputStream out = rsp.getOutputStream();
	        returnFile(fileName, out);
        }
    }
    
    /**
     * Private method to retrieve the file name for a TRB page.
     * 
     * @param num - int TRB number
     * @param page - int TRB page number
     * @return <String> TRB page file name
     */
    private String getFilename(int num, int page) {
		Session session = null;
		String filename = null;
		try {
			session = TrbSessionFactory.getSession();

			TrbDAOHibernate tdh = new TrbDAOHibernate();
			tdh.setSession(session);

			Transaction tx = session.beginTransaction();

			Trb trb = tdh.findByNumAndPage(num, page);
			
			if (trb.getTrbFile() != null)
				filename = trb.getTrbFile().getPath();

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
		return filename;
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
