package gov.nrel.nbc.tracker.utils;

//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Formatter;
import org.apache.log4j.Level;
//import java.util.logging.LogRecord;

import gov.nrel.nbc.tracker.client.AppConstants;
import gov.nrel.nbc.tracker.client.DevTestProdConstants;

import org.apache.log4j.Logger;

public class XLogger implements AppConstants, DevTestProdConstants {
	public XLogger(Class c) {
		log = Logger.getLogger(c);
	}
	private static final long serialVersionUID = 1304200572377062538L;

	private Logger log; 
		
		/** We delegate here so that users need not import Level. */
	  public static final Level OFF     = Level.OFF;
	  public static final Level SEVERE  = Level.FATAL;
	  public static final Level WARNING = Level.ERROR;
	  public static final Level INFO    = Level.INFO;
	  public static final Level DEBUG    = Level.DEBUG;
	  //public static final Level FINER   = Level.FINER;
	  //public static final Level FINEST  = Level.FINEST;
	  public static final Level ALL     = Level.ALL;

	  public void info(String s) {
		  log.info(s);
	  }
	
	  public void severe(String s) {
		  log.error(s);
	  }
	  
	  public void debug(String s) {
		  log.debug(s);
	  }
	  
	  public void warning(String s) {
		  log.warn(s);
	  }
	  
	  public void fine(String s) {
		  log.debug(s);
	  }
}
