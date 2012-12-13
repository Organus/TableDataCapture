package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.dao.PrintersDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.model.Printers;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PrinterStatusThread extends Thread implements AppConstants {

	private static final int INTERVAL = 120; // seconds
//	private static final String PRINTCAP = "/etc/printcap";
	private static final String PRINTCAP = "/etc/print.cfg";
	private static final String WINDOWS = "windows";
	private static final int REMOTEPRINTER = 1;
	private static final int MACHINE = 2;
	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
        
	public PrinterStatusThread() {
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
	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(PrinterStatusThread.class);

	private static final long serialVersionUID = 632241583322844465L;

    /**
     * Public method to create file containing printer commands.
     * 
     * @return <String> file name
     * @throws <Exception>
     */
    private String createFile() throws Exception {
    	String filename = "";
    	try {
    		Date date = new Date();
	    	long time = date.getTime();
	    	ResourceBundle rBundle = new MyResources();//ResourceBundle.getBundle(TRACKER_PROPERTIES_FILE_NAME);
	    	String tmp = rBundle.getString(TEMP_DIR);
	    	//log.info("for temp_dir="+TEMP_DIR+", got tmp="+tmp);
	    	filename = tmp + "rlpq" + time + ".txt";
	    	//log.info("filename = "+filename);
    	} catch (MissingResourceException mre) {
    		
    	}
    	File f = new File(filename);
    	if (f.exists())
    		throw new Exception("file not unique");
    	return filename;
    }
    
	private boolean testPrinter(String remotePrinter, String machine) {
    	//log.info("in testPrinter with "+remotePrinter+" and machine="+machine);
    	boolean ok = true;
    	BufferedReader bis1 = null;
    	BufferedReader bis2 = null;
    	File outF = null;
    	try {
    		if (!machine.isEmpty() && !remotePrinter.isEmpty()) {
    			//log.info("machine="+machine+";remotePrinter="+remotePrinter);
    			Process process;
    			try {
    				Runtime runtime = Runtime.getRuntime();
    				String outfile = createFile();
    				String command = "rlpq -P"+remotePrinter+" -H"+machine+" --timeout=5 1>"+outfile+" 2>"+outfile;
    				//log.info("command="+command);
    				process = runtime.exec(new String[] {"/bin/bash", "-c",command});
    				process.waitFor();
    				Thread.sleep(1000);
    				outF = new File(outfile);
    				bis2 = new BufferedReader(new InputStreamReader(new FileInputStream(outF)));
    				boolean found = false;
    				if (!bis2.ready()) 
    					log.warning(outfile+" not ready");
    				else {
    					while (bis2.ready()) {
    						String line2 = bis2.readLine();
    						line2 = line2.toLowerCase();
    						//log.info("line2="+line2);
    						if (line2.indexOf("full_read_timed: timed out") != -1) {
    							found = true;
    							break;
    						} else if (line2.indexOf("specified host is unknown") != -1) {
    							found = true;
    							break;
    						} else if (line2.indexOf("specified printer does not exist") != -1) {
    							found = true;
    							break;
    						} else if (line2.indexOf("the request is valid but has no IP address") != -1) {
    							found = true;
    							break;    							
    						} else if (line2.indexOf("515: connection timed out") != -1) {
    							found = true;
    							break;    							
    						} else if (line2.indexOf("cannot connect to lpd") != -1) {
    							found = true;
    							break;    							
    						}
    					}
    				}
    				if (found) {
    					ok = false;
    					//log.info("REJECTING "+remotePrinter+" on "+machine);
    				}
    				//}
    			} catch (IOException ioe) {
    				log.severe("IOException caught executing smbclient: "+ioe);
    				return false;
    			} catch (InterruptedException e) {
    				log.severe("InterruptedException caught executing smbclient: "+e);
    				return false;
    			} catch (Exception e) {
    				log.warning("Exception caught: "+e);
    			} 
    		} else {
    			ok = false;
    		}
    	} finally {
    		try {
    	        if (bis1 != null)
    	        	bis1.close();
    	        if (bis2 != null)
    	        	bis2.close();
    	        if (outF != null && outF.exists())
    	        	FileUtils.deleteQuietly(outF);
    		} catch (IOException ioe1) {
        		log.warning("problem closing "+PRINTCAP+" file: "+ioe1);
    		}
    	}
    	return ok;
    }

    private String getInfo(String printer, int which) {
		//log.info("in getInfo with "+printer);
		BufferedReader bis = null;
		String machine = "";
		String remotePrinter = "";
		try {
	    	File file = new File(PRINTCAP);
	    	FileInputStream fis = new FileInputStream(file);
	        bis = new BufferedReader(new InputStreamReader(fis));
	        while (bis.ready()) {
	        	String line = bis.readLine();
	        	//log.info("line="+line);
	        	if (line.startsWith("lp|")) {
	        		int pipe = line.indexOf("|");
	        		if (pipe != -1) {
	        			String ptr = line.substring(pipe+1);
	        			int colon = ptr.indexOf(":");
	        			if (colon != -1) {
	        				ptr = ptr.substring(0,colon);
	    		        	//log.info("ptr="+ptr);
	        				if (ptr.equals(printer)) {
	        					//log.info("found "+printer);
	        				    int sd = -1;
	        					while (sd == -1) {
	        						line = bis.readLine();
	        			        	//log.info("line="+line);
	        			        	int rm = line.indexOf(":rm=");
	        			        	int rp = line.indexOf(":rp=");
	        						if (rm != -1) {
	        							machine = line.substring(rm+4);
	        							int mcolon = machine.indexOf(":");
	        							if (mcolon != -1)
	        								machine = machine.substring(0,mcolon);
	        							//log.info("machine="+machine);
	        						} else if (rp != -1) {
	        							remotePrinter = line.substring(rp+4);
	        							int rcolon = remotePrinter.indexOf(":");
	        							if (rcolon != -1)
	        								remotePrinter = remotePrinter.substring(0,rcolon);	        							
	        							//log.info("remotePrinter="+remotePrinter);
	        						}
	        						sd = line.indexOf(":sd=");
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
		} catch (IOException ioe) {
			log.warning("problem reading "+PRINTCAP+" file: "+ioe);
		} finally {
			try {
		        if (bis != null)
		        	bis.close(); 
			} catch (IOException ioe1) {
	    		log.warning("problem closing "+PRINTCAP+" file: "+ioe1);
			}
		}
		if (which==REMOTEPRINTER)
			return remotePrinter;
		else
			return machine;
	}

	private void updatePrinterStatus(String printerName, boolean status) {
		//log.info("updating status for "+printerName+" to "+status);
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			PrintersDAOHibernate sdh = new PrintersDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			Printers printer = sdh.findByName(printerName);
			if (printer == null && printerName != null && !printerName.isEmpty()) {
				printer = new Printers();
				//log.info("creating to db entry for "+printerName);
				String remotePrinter = getInfo(printerName,REMOTEPRINTER);
				String machine = getInfo(printerName,MACHINE);
				printer.setName(printerName);
				printer.setRemotePrinter(remotePrinter);
				printer.setMachine(machine);
			}
			printer.setStatus(status);
			session.saveOrUpdate(printer);
			
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
        	return;
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
    		return;
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	return;
    }

	/**
     * Public method to test to see if the printers are available.
     * 
     */
    private void testPrinters() {
    	//log.info("in testPrinters");
    	ArrayList<String> printers = new ArrayList<String>();
    	BufferedReader bis = null;
	    try {
	    	String os = System.getenv("os");
	    	//log.info("os="+os);
	    	if (os != null && os.toLowerCase().indexOf(WINDOWS) != -1) {
		        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
	            for (int i=0;i<services.length;i++) {
	                PrintServiceAttribute attr =
	                    services[i].getAttribute(PrinterName.class);
	                String name = ((PrinterName)attr).getValue();
	                PrintServiceAttribute psattrib = services[i].getAttribute(PrinterIsAcceptingJobs.class);
	                int accepting = ((PrinterIsAcceptingJobs)psattrib).getValue();
	                //log.info(name+" accepting="+accepting);
		            if (accepting == 1)
		            	printers.add(name);
	            }
            } else {
		    	File file = new File(PRINTCAP);
		    	FileInputStream fis = new FileInputStream(file);
		        bis = new BufferedReader(new InputStreamReader(fis));
		        while (bis.ready()) {
		        	String line = bis.readLine();
		        	//log.info("line="+line);
		        	if (line.startsWith("lp|")) {
		        		int pipe = line.indexOf("|");
		        		if (pipe != -1) {
		        			String ptr = line.substring(pipe+1);
		        			int colon = ptr.indexOf(":");
		        			if (colon != -1) {
		        				ptr = ptr.substring(0,colon);
		        				if (!printers.contains(ptr)) {
		        					//log.info("testing ptr="+ptr);
		        					String remotePrinter = getInfo(ptr,REMOTEPRINTER);
		        					//log.info("remotePrinter="+remotePrinter);
		        					String machine = getInfo(ptr,MACHINE);
		        					//log.info("machine="+machine);
		        					boolean status = testPrinter(remotePrinter,machine);
		        					//log.info("status="+status+" for printer="+remotePrinter+" on machine="+machine);
		        					updatePrinterStatus(ptr,status);
		        					printers.add(ptr);
		        				}
		        			}
		        		}
		        	}
		        }
		        bis.close();
            }
		} catch (IOException e) {
			log.severe("IOException caught reading "+PRINTCAP+" file: "+e);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	try {
	    		if (bis != null)
	    			bis.close();
	    	} catch (IOException ioe) {
	    		log.warning("problems closing "+PRINTCAP+": "+ioe);
	    	}
	    }
	    //log.info("found "+printers.size()+" printers");
    }
    
	@Override
	public void run() {
		log.info("in PrinterStatusThread.run");
		System.out.println("in PrinterStatusThread.run");
		try {
			while (true) {
				testPrinters();
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
