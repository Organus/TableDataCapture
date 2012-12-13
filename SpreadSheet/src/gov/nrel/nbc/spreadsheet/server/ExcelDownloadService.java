package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.spreadsheet.client.AppConstants;

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

/**
 * A servlet that implements the methods for downloading a file.
 * 
 * @author James Albersheim
 * 
 */
public class ExcelDownloadService extends HttpServlet implements AppConstants {
	private static final String EXCEL07 = ".xlsx";
	private static final String EXCEL97 = ".xls";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
	private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String XLSM_CONTENT_TYPE = "application/vnd.openxmlformats";
	private static final String SS_FILE_PATH = "excelfile";
	private static final String SS_FILE_NAME = "ssFileName";
	private static final long serialVersionUID = 217721541663385449L;


	/**
	 * Method to perform get for servlet. File is deleted
	 * upon completion of processing. Returns a list of techName strings.
	 * 
	 * @param req The servlet request object
	 * @param rsp The servlet response object
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
        String filePath = (String)req.getParameter(SS_FILE_PATH);
        String fileName = (String)req.getParameter(SS_FILE_NAME);
        int period = filePath.lastIndexOf(".");
        String ext = EXCEL_FILE_SUFFIX;
        if (period > -1) {
        	ext = filePath.substring(period);
        }
        if (ext.equals(EXCEL97)) {
        	rsp.setContentType(XLS_CONTENT_TYPE);
        } else if (ext.equals(EXCEL07)){
        	rsp.setContentType(XLSX_CONTENT_TYPE);
        } else {
        	rsp.setContentType(XLSM_CONTENT_TYPE);        	
        }
        rsp.addHeader(CONTENT_DISPOSITION,
                      "attachment; filename=" + fileName);
        
        OutputStream out = rsp.getOutputStream();
        returnFile(filePath, out);
    }

	/**
	 * A method that returns a file'S contents to standard output
	 * 
	 * @param fileName The name of the file to return
	 * @param out The output stream
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
