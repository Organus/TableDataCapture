package gov.nrel.nbc.security.utils;

//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Formatter;
import org.apache.log4j.Level;
//import java.util.logging.LogRecord;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.client.DevTestProdConstants;

import org.apache.log4j.Logger;

public class XLogger extends Level implements AppConstants, DevTestProdConstants {
	public XLogger(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
		logger.setLevel(this);
	}

	public XLogger(Level level) {
		super(level.toInt(),level.toString(),level.getSyslogEquivalent());
		logger.setLevel(level);
	}
	private static final long serialVersionUID = 1304200572377062538L;

	  /** We delegate here so that users need not import Level. */
	  public static final Level OFF     = Level.OFF;
	  public static final Level SEVERE  = Level.FATAL;
	  public static final Level WARNING = Level.ERROR;
	  public static final Level INFO    = Level.INFO;
	  public static final Level DEBUG    = Level.DEBUG;
	  //public static final Level FINER   = Level.FINER;
	  //public static final Level FINEST  = Level.FINEST;
	  public static final Level ALL     = Level.ALL;

	  private static Logger logger = Logger.getLogger("workmgmt.log");
	  
	  public void info(String s) {
		  logger.info(s);
	  }
	
	  public void severe(String s) {
		  logger.error(s);
	  }
	  
	  public void debug(String s) {
		  logger.debug(s);
	  }
	  
	  public void warning(String s) {
		  logger.warn(s);
	  }
	  
	  public void fine(String s) {
		  logger.debug(s);
	  }
}
