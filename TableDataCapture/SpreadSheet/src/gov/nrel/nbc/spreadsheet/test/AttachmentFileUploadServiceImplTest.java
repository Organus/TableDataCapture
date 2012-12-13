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

import gov.nrel.nbc.spreadsheet.server.AttachmentFileUploadServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;

public class AttachmentFileUploadServiceImplTest extends TestCase {

	ClassLoader thisLoader;
	private AttachmentFileUploadServiceImpl cfusImpl;
	
	//@Override
	protected void setUp() {
    	thisLoader = AttachmentFileUploadServiceImpl.class.getClassLoader();
    	//cfusImpl = new CalcFileUploadServiceImpl();
	}
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testParseFile() {
			String ret = "";
			try {
			    cfusImpl = new AttachmentFileUploadServiceImpl();
			    File f = new File("C:\\Demo.xls");
				PrivateAccessor.invoke(cfusImpl, "parseFile", new Class[]{File.class}, new Object[]{f});
			} catch (Throwable t) {
				System.out.println("problem invoking parseFile: "+t);
				//String stack = CalcFileUploadServiceImpl.getStackTrace(t);
				//System.out.println(stack);
			}
			System.out.println(ret);
	  }

	  public void testProcessFile() {
			String ret = "";
			String filename="";
			String newfilename="";
			try {
			    cfusImpl = new AttachmentFileUploadServiceImpl();
			    filename = "C:\\Demo.xls";
			    String reponame = "C:\\Demo1.xls";
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
			    cfusImpl = new AttachmentFileUploadServiceImpl();
				try {
					//techName
					name = "techName";
					value = "techName1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//subtaskNumber
					name = "subtaskNumber";
					value = "subtaskNumber1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//feedstockId
					name = "feedstockId";
					value = "feedstockId1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//sampleMatrix
					name = "sampleMatrix";
					value = "sampleMatrix1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//comment
					name = "comment";
					value = "comment1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//fileName
					name = "fileName";
					value = "fileName1";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//important
					name = "important";
					value = "true";
					PrivateAccessor.invoke(cfusImpl, "saveMetadata", new Class[]{String.class,String.class}, new Object[]{name,value});
					//real_data
					name = "real_data";
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
			    String filename = "C:\\Demo.xls";
			    String newfilename = "C:\\Demo1.xls";
				PrivateAccessor.invoke(cfusImpl, "saveFile", new Class[]{String.class,String.class}, new Object[]{filename,newfilename});				
			} catch (Throwable t2) {
				System.out.println("problem invoking saveFile: "+t2);
				String stack = SpreadSheetUploadServiceImpl.getStackTrace(t2);
				System.out.println(stack);
			}
			System.out.println(ret);
	  }
	  
	  public void testDoPost() {

		  try {
		    String url = "http://localhost/spreadsheet/attachmentUploadService";
		    String webXML = "C:\\projects\\james\\SpreadSheet\\war\\WEB-INF\\web.xml";
		    String inFile = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls";
		    //String copyFile = "C:\\Documents and Settings\\James Albersheim\\Desktop\\Copy of Demo.xls.bak";
		    File fXML = new File(webXML);
		    ServletRunner sr = new ServletRunner(fXML);
		    sr.registerServlet("/spreadsheet/attachmentUploadService",AttachmentFileUploadServiceImpl.class.getName());
		    ServletUnitClient sc = sr.newClient();
		    WebRequest request = new PostMethodWebRequest(url, true);
		    //File srcFile = new File(inFile);
		    //File destFile = new File(copyFile);
		    //FileUtils.copyFile(srcFile,destFile);
			request.setParameter("techName", "James");
		    request.setParameter("subtaskNumber", "bb");
		    request.setParameter("feedstockId", "corn stover");
		    request.setParameter("sampleMatrix", "123");
		    request.setParameter("real_data", "3.0");
		    request.setParameter("important", "true");
		    request.setParameter("comment", "junit test");
		    request.setParameter("fileName",inFile);
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
		      catch (Exception e) {System.out.println("Error testing AttachmentFileUploadServiceImpl. Exception is " + e); e.printStackTrace(); }
		    //srcFile = new File(copyFile);
		    //destFile = new File(inFile);
		    //if (!destFile.exists())
		    //	FileUtils.moveFile(srcFile, destFile);
		    //else
		    //	FileUtils.deleteQuietly(srcFile);
		  } catch (Exception e) {}
	}

	public static Test suite() {
		return new TestSuite(AttachmentFileUploadServiceImplTest.class);
	}
}
