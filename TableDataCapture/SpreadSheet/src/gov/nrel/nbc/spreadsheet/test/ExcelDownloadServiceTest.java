package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import gov.nrel.nbc.spreadsheet.server.ExcelDownloadService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExcelDownloadServiceTest extends TestCase {

	ClassLoader thisLoader;
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testSomething() {
			String ret = "";
			//ExcelDownloadService eds = new ExcelDownloadService();
			System.out.println(ret);
	  }

	//@Override
	protected void setUp() {
    	thisLoader = ExcelDownloadService.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(ExcelDownloadServiceTest.class);
	}

	public void testMethod() throws Exception {
		//ExcelDownloadService eds = new ExcelDownloadService();
        String fileName = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls";
        String file2 = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Copy of Demo.xls";
        File f = new File(file2);
        OutputStream out = new FileOutputStream(f);
        assertNotNull(out);
        ExcelDownloadService.returnFile(fileName, out);
	}
	public void testDoGet () throws Exception
	{
	    String url = "http://localhost/spreadsheet/clientDownloadService";
	    String webXML = "C:\\projects\\james\\SpreadSheet\\war\\WEB-INF\\web.xml";
	    String inFile = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls";
	    String copyFile = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls.bak";
	    File fXML = new File(webXML);
	    ServletRunner sr = new ServletRunner(fXML);
	    sr.registerServlet("/spreadsheet/clientDownloadService",ExcelDownloadService.class.getName());
	    ServletUnitClient sc = sr.newClient();
	    WebRequest request = new GetMethodWebRequest(url);
	    File srcFile = new File(inFile);
	    File destFile = new File(copyFile);
	    FileUtils.copyFile(srcFile,destFile);
	    request.setParameter("excelfile", inFile);
	      try
	      {
	         // Use the InvocationContext to create an instance
	         // of the servlet
	         InvocationContext ic = sc.newInvocation(request);
	         ExcelDownloadService cdServlet = null;
	    	 try {
		         cdServlet = (ExcelDownloadService)ic.getServlet();
		         assertNull("A session already exists",
		                    ic.getRequest().getSession(false));
	    	 }
	    	 catch (Exception e2) {System.out.println("Error initializing cdServlet. Exception is " + e2); }
	         HttpServletRequest cdServletRequest = ic.getRequest();
	         System.out.println("parm="+cdServletRequest.getParameter("excelfile"));
	         HttpServletResponse cdServletResponse = ic.getResponse();
	         if (cdServlet != null) {
	        	 try {
	        		 cdServlet.doGet(cdServletRequest,cdServletResponse);
	        	 } catch (Exception e5) {System.out.println("Error calling servlet.doGet. Exception is " + e5);e5.printStackTrace();}
	         }
	      }
	      catch (Exception e) {System.out.println("Error testing clientDownloadService. Exception is " + e); e.printStackTrace(); }
	    srcFile = new File(copyFile);
	    destFile = new File(inFile);
	    try { FileUtils.moveFile(srcFile, destFile); } catch (IOException ioe) {FileUtils.deleteQuietly(srcFile);}
	}
}
