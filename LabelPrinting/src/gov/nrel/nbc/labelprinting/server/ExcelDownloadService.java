package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * <HttpServlet> to handle the downloading of an Excel spreadsheet 
 *  that contains the results of the users query.
 * 
 * @author jalbersh
 *
 */
public class ExcelDownloadService extends HttpServlet implements AppConstants {
	private static final String EXCELFILE = "excelfile";
	private static final long serialVersionUID = 217721541663385449L;
	private static final String EXCEL07 = ".xlsx";
	private static final String EXCEL97 = ".xls";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
	private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String XLSM_CONTENT_TYPE = "application/vnd.openxmlformats";

    /**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(ExcelDownloadService.class);
	/**
	 * <HttpServlet> <code>doGet()</code> method returns the file
	 *  to the client and then deletes the file from the server.
	 *  
	 *  @param req <HttpServletRequest> The servlet request.
	 *  @param rsp <HttpServletResponse> The servlet response.
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
        String fileName = (String)req.getParameter(EXCELFILE);
        int period = fileName.lastIndexOf(".");
        String ext = EXCEL_FILE_SUFFIX;
        if (period > -1) {
        	ext = fileName.substring(period);
        }
        if (ext.equals(EXCEL97)) {
        	rsp.setContentType(XLS_CONTENT_TYPE);
        } else if (ext.equals(EXCEL07)){
        	rsp.setContentType(XLSX_CONTENT_TYPE);
        } else {
        	rsp.setContentType(XLSM_CONTENT_TYPE);        	
        }
        rsp.addHeader(CONTENT_DISPOSITION,
                      "attachment; filename=" + EXCEL_FILE_NAME + ext);
        
        System.out.println("doGet");
        OutputStream out = rsp.getOutputStream();
        returnFile(fileName, out);
		
		FileUtils.deleteQuietly(new File(fileName));
    }

    /**
     * public static method to put the file into the <OutputStream>.
     * 
     * @param fileName <String> File name.
     * @param out <OutputStream>
     * @throws <FileNotFoundException>
     * @throws <IOException>
     */
	public void returnFile(String fileName, OutputStream out)
			throws FileNotFoundException, IOException {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buf = new byte[4 * 1024];
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}			
		} catch (FileNotFoundException fnfe) {
			log.warning("file not found exception : "+fileName);
		} finally {
			if (in != null)
				in.close();
		}		
	}
}
