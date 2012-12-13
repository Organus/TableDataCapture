package gov.nrel.nbc.spreadsheet.test;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;

public class SpreadSheetUploadServiceImplTest extends TestCase implements AppConstants {

	ClassLoader thisLoader;
	private SpreadSheetUploadServiceImpl cfusImpl;
	
	//@Override
	protected void setUp() {
    	thisLoader = SpreadSheetUploadServiceImpl.class.getClassLoader();
    	//cfusImpl = new CalcFileUploadServiceImpl();
	}
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testParseFile() {
			String ret = "";
			try {
			    cfusImpl = new SpreadSheetUploadServiceImpl();
			    File f = new File("C:/projects/james/SpreadSheet/resources/Test Spreadsheet.xlsx");
				PrivateAccessor.invoke(cfusImpl, "parseFile", new Class[]{File.class}, new Object[]{f});
			} catch (Throwable t) {
				System.out.println("problem invoking parseFile: "+t);
				//String stack = CalcFileUploadServiceImpl.getStackTrace(t);
				//System.out.println(stack);
			}
			System.out.println(ret);
	  }

	  public void testGetConfig() {
		    cfusImpl = new SpreadSheetUploadServiceImpl();
		    File f = new File("C:/projects/MAIN/SpreadSheet/Roman_Aug_1-10_liquor-1.xlsx");	
		    String config = cfusImpl.getConfig(f);
		    System.out.println("config="+config);
	  }
	  
	  public void testProcessFile() {
			String ret = "";
			String filename="";
			String newfilename="";
			try {
			    cfusImpl = new SpreadSheetUploadServiceImpl();
			    filename = "C:/projects/james/SpreadSheet/resources/Test Spreadsheet.xlsx";
			    String reponame = "C:/projects/james/SpreadSheet/resources/Test Spreadsheet1.xlsx";
			    File repo = new File(reponame);
			    DiskFileItem item = new DiskFileItem(null, "", false, filename, 100000, repo);
				newfilename = "";
				try {
					newfilename = (String)PrivateAccessor.invoke(cfusImpl, "processFile", new Class[]{DiskFileItem.class}, new Object[]{item});
				}
				catch (Exception e1) {
					System.err.println("Exception caught: "+e1);
				}
				if (newfilename != null && !newfilename.isEmpty()) {
					System.out.println("creating new file="+newfilename);
					File f = new File(newfilename);
					boolean b = f.createNewFile();
					if (!b)
						System.out.println("failed to create new file: "+newfilename);
					newfilename = (String)PrivateAccessor.invoke(cfusImpl, "processFile", new Class[]{DiskFileItem.class}, new Object[]{item});
				}
			} catch (Throwable t) {
				System.out.println("problem invoking parseFile: "+t);
				String stack = SpreadSheetUploadServiceImpl.getStackTrace(t);
				System.out.println(stack);
			}
			System.out.println(ret);
	  }

	  public void testSaveMetadata() {
			String ret = "";
			String name = "";
			String value = "";
			try {
			    cfusImpl = new SpreadSheetUploadServiceImpl();
				try {
					//techName
					name = "submitter";
					value = "submitter1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//subtaskNumber
					name = "charge number";
					value = "charge number1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//feedstockId
					name = "project";
					value = "project1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//sampleMatrix
					name = "customer";
					value = "customer1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//comment
					name = "comments";
					value = "comments1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//fileName
					name = "fileName";
					value = "fileName1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//important
					name = "long";
					value = "1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//real_data
					name = "real";
					value = "1.0";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});

					//persistMetadata
					//PrivateAccessor.invoke(cfusImpl, "persistMetadata", null, null);
				}
				catch (Exception e1) {System.err.println("Exception caught: "+e1);	}
			} catch (Throwable t) {
				System.out.println("problem invoking saveMetadata: "+t);
				String stack = SpreadSheetUploadServiceImpl.getStackTrace(t);
				System.out.println(stack);
			}
			try {
			    String filename = "C:/projects/james/SpreadSheet/resources/Test Spreadsheet.xlsx";
			    String newfilename = "C:/projects/james/SpreadSheet/resources/Test Spreadsheet1.xlsx";
				PrivateAccessor.invoke(cfusImpl, "saveFile", new Class[]{String.class,String.class}, new Object[]{filename,newfilename});				
			} catch (Throwable t2) {
				System.out.println("problem invoking saveFile: "+t2);
				String stack = SpreadSheetUploadServiceImpl.getStackTrace(t2);
				System.out.println(stack);
			}
			System.out.println(ret);
	  }
	  
	  public void testDoPost() throws Exception {

		    String url = "http://localhost/spreadsheet/clientUploadService";
		    String webXML = "C:/projects/james/SpreadSheet/war/WEB-INF/web.xml";
		    String inFile = "C:/projects/james/SpreadSheet/resources/Test Spreadsheet.xlsx";
		    //String copyFile = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls.bak";
		    File fXML = new File(webXML);
		    ServletRunner sr = new ServletRunner(fXML);
		    sr.registerServlet("/spreadsheet/clientUploadService",SpreadSheetUploadServiceImpl.class.getName());
		    ServletUnitClient sc = sr.newClient();
		    WebRequest request = new PostMethodWebRequest(url, true);
		    //File srcFile = new File(inFile);
		    //File destFile = new File(copyFile);
		    //FileUtils.copyFile(srcFile,destFile);
			request.setParameter(CONFIG, "TestConfig");
			request.setParameter("submitter ", "James");
		    request.setParameter("charge number", "11425");
		    request.setParameter("project", "biomass");
		    request.setParameter("customer", "Sparky");
		    request.setParameter("comments", "Unit Testing");
		    request.setParameter("long", "1");
		    request.setParameter("real", "2.0");
		    request.setParameter("excelfile",inFile);
		    request.setParameter("fileName",inFile);
		    request.setParameter("ssFileName","Test Spreadsheet.xlsx");
		    request.selectFile("fileName", new File(inFile));
		      try
		      {
		         // Use the InvocationContext to create an instance
		         // of the servlet
		         InvocationContext ic = sc.newInvocation(request);
		         SpreadSheetUploadServiceImpl cfuServlet = null;
		    	 try {
			         cfuServlet = (SpreadSheetUploadServiceImpl)ic.getServlet();
			         assertNull("A session already exists",
			                    ic.getRequest().getSession(false));
		    	 }
		    	 catch (Exception e2) {System.out.println("Error initializing cdServlet. Exception is " + e2); }
		         HttpServletRequest cdServletRequest = ic.getRequest();
		         System.out.println("parm="+cdServletRequest.getParameter("output"));
		         HttpServletResponse cdServletResponse = ic.getResponse();
		         if (cfuServlet != null) {
		        	 try {
		        		 cfuServlet.doPost(cdServletRequest,cdServletResponse);
		        	 } catch (Exception e5) {System.out.println("Error calling servlet.doPost. Exception is " + e5);e5.printStackTrace();}
		         }
		      }
		      catch (Exception e) {System.out.println("Error testing CalcFileUploadServiceImpl. Exception is " + e); e.printStackTrace(); }
		    //srcFile = new File(copyFile);
		    //destFile = new File(inFile);
		    //if (!destFile.exists())
		    //	FileUtils.moveFile(srcFile, destFile);
		    //else
		    //	FileUtils.deleteQuietly(srcFile);
	}

	public static Test suite() {
		return new TestSuite(SpreadSheetUploadServiceImplTest.class);
	}
}
