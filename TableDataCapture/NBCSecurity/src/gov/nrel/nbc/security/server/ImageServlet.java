package gov.nrel.nbc.security.server;

import gov.nrel.nbc.tracker.utils.XLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1274665693924845025L;
    private static final XLogger log = new XLogger(XLogger.INFO);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	//@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.warning("in doGet");
		String filename = null;
		     	String os = System.getenv("os");
    	if (os != null && os.toLowerCase().indexOf("windows") != -1) {
    		filename = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/jboss-5.1.0.GA/jboss-5.1.0.GA/server/default/deploy/image.pdf";
    	} else {
    		filename = "/usr/local/jboss/server/default/deploy/image.pdf";
    	}
    	byte[] array=getAsByteArray(new URL("http://localhost:8880/image.pdf"));
    	resp.setContentType("application/pdf");
    	resp.setContentLength(array.length);				
    	resp.getOutputStream().write(array, 0, array.length);
		ServletOutputStream sos = resp.getOutputStream();
		resp.getOutputStream().flush();
	}

	public static byte[] getAsByteArray(URL url) throws IOException {
	    URLConnection connection = url.openConnection();
	    // Since you get a URLConnection, use it to get the InputStream
	    InputStream in = connection.getInputStream();
	    // Now that the InputStream is open, get the content length
	    int contentLength = connection.getContentLength();

	    // To avoid having to resize the array over and over and over as
	    // bytes are written to the array, provide an accurate estimate of
	    // the ultimate size of the byte array
	    ByteArrayOutputStream tmpOut;
	    if (contentLength != -1) {
	        tmpOut = new ByteArrayOutputStream(contentLength);
	    } else {
	        tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate size
	    }

	    byte[] buf = new byte[512];
	    while (true) {
	        int len = in.read(buf);
	        if (len == -1) {
	            break;
	        }
	        tmpOut.write(buf, 0, len);
	    }
	    in.close();
	    tmpOut.close(); // No effect, but good to do anyway to keep the metaphor alive

	    byte[] array = tmpOut.toByteArray();

	    //Lines below used to test if file is corrupt
	    //FileOutputStream fos = new FileOutputStream("C:\\abc.pdf");
	    //fos.write(array);
	    //fos.close();

	    return array;
	}
	public ByteArrayOutputStream getPdfFile(String filename) throws DocumentException, IOException{
		PdfReader pdfReader = new PdfReader(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfStamper stamp = new PdfStamper(pdfReader, baos);
        pdfReader.close();
        stamp.close();
        return baos;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	//@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
