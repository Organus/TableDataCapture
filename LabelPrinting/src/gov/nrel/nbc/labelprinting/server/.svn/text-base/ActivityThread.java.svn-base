package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.utils.XLogger;
import gov.nrel.nbc.security.server.SecurityServiceImpl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ActivityThread extends Thread implements AppConstants {

	private static final long serialVersionUID = 7878740493440924695L;
	private static final int INTERVAL = 120; // seconds
	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
        
	public ActivityThread() {
		if (DEV_PROD_TEST.equals(LOCAL)) {
			tempDirKey = "tracker.local.tempDirectory";
			tempDirValue = "C:/workspace/tmp/";
			fileDirKey = "tracker.local.fileDirectory";
			fileDirValue = "C:/workspace/files/";
			logDirKey = "tracker.local.logDirectory";
			logDirValue = "C:/workspace/logfile.txt";
		} else if (DEV_PROD_TEST.equals(DEV)) {
			tempDirKey = "tracker.dev.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "tracker.dev.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SAMPLE/";
			logDirKey = "tracker.dev.logDirectory";
			logDirValue = "/var/log/tomcat5.5";
		} else if (DEV_PROD_TEST.equals(TEST)) {
			tempDirKey = "tracker.test.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "tracker.test.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SAMPLE/";
			logDirKey = "tracker.test.logDirectory";
			logDirValue = "/var/log/tomcat5.5";
		} else if (DEV_PROD_TEST.equals(PROD)) {
			tempDirKey = "tracker.prod.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "tracker.prod.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SAMPLE/";
			logDirKey = "tracker.prod.logDirectory";
			logDirValue = "/var/log/tomcat5.5";			
		} else if (DEV_PROD_TEST.equals(SABC)) {
			tempDirKey = "tracker.sabc.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "tracker.sabc.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/SABC/SAMPLE/";
			logDirKey = "tracker.sabc.logDirectory";
			logDirValue = "/var/log/tomcat5.5";			
		} else if (DEV_PROD_TEST.equals(ALGAE)) {
			tempDirKey = "tracker.algae.tempDirectory";
			tempDirValue = "/tmp/";
			fileDirKey = "tracker.algae.fileDirectory";
			fileDirValue = "/usr/local/NREL_ARCHIVE/NBC/SAMPLE/";
			logDirKey = "tracker.algae.logDirectory";
			logDirValue = "/var/log/tomcat5.5";			
		}
	}
	 public class MyResources extends ResourceBundle {
		 private HashMap<String,String> contents = new HashMap<String,String>();
	     public MyResources() {
	        	//	 # Temp directory for temporary files
    		 contents.put(tempDirKey,tempDirValue);
	        	//	 # Directory to store attachments
	         contents.put(fileDirKey,fileDirValue);
	        	//	 # Directory to create log file for files processed.
	         contents.put(logDirKey,logDirValue);	             
	     }
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
		@Override
		public Enumeration<String> getKeys() {
			return (Enumeration<String>) contents.keySet();
		}

		@Override
		protected Object handleGetObject(String key) {
			return contents.get(key);
		}
		
	 }
    private static final XLogger log = new XLogger(ActivityThread.class);

	/**
     * Public method to test to see if the printers are available.
     * 
     */
    private void testActivity() {
    	SecurityServiceImpl ssi = new SecurityServiceImpl();
    	List<String> logins = ssi.getLogins();
    	Iterator<String> lit = logins.iterator();
    	while (lit.hasNext()) {
    		String id = lit.next();
    		if (ssi.notActive(id,INITIAL_TIMEOUT_PAD/1000)) {
    			//ssi.logoff(id);
    		}
    	}
    }
    
	@Override
	public void run() {
		log.info("in ActivityThread.run");
		System.out.println("in ActivitysThread.run");
		try {
			while (true) {
				testActivity();
				Thread.sleep(INTERVAL*1000);
			}
		} catch (InterruptedException ie) {
			log.warning("Interruption occurred: "+ie);
		} catch (Exception e) {
			log.warning("Exception occurred: "+e);
		}
	}
    public void doShutdown() {
        Thread moribund = this;
        moribund.interrupt();
        moribund = null;
    }

}
