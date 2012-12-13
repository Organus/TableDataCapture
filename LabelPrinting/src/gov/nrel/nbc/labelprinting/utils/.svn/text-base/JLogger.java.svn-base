package gov.nrel.nbc.labelprinting.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
* <p>An extension of the java.util.logging.Logger class with
* convenience methods. </p>
*
* <p>Usage:<code>
*  // XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
*  private static final XLogger l = new XLogger(XLogger.FINER);
* </code></p>
*
* <p>Run main() with<code>
* java -cp classes gov.nrel.nbc.tcuf.util.Logging
* </code></p>
*/
public class JLogger extends Logger {
  // Static initialization-------------------------------------------

  /** We delegate here so that users need not import Level. */
  public static final Level OFF     = Level.OFF;
  public static final Level SEVERE  = Level.SEVERE;
  public static final Level WARNING = Level.WARNING;
  public static final Level INFO    = Level.INFO;
  public static final Level CONFIG  = Level.CONFIG;
  public static final Level FINE    = Level.FINE;
  public static final Level FINER   = Level.FINER;
  public static final Level FINEST  = Level.FINEST;
  public static final Level ALL     = Level.ALL;

  /** A Formatter with no date information. */
  public static final Formatter DATELESS_FORMATTER = new Formatter() {
          public String format(LogRecord rec) {
              StringBuilder sb = new StringBuilder();
              java.util.Formatter fmt = new java.util.Formatter(sb);
              fmt.format("%-7s", rec.getLevel());
              sb.append(" [");
              sb.append(rec.getSourceClassName());
              sb.append(".");
              sb.append(rec.getSourceMethodName());
              sb.append("()");
              sb.append("] ");
              sb.append(rec.getMessage());
              return sb.append("\n").toString();
          }
      };

  /** A Formatter with date information. */
  public static final Formatter DATED_FORMATTER = new Formatter() {
          public String format(LogRecord rec) {
              StringBuilder sb = new StringBuilder();
              java.util.Formatter fmt = new java.util.Formatter(sb);
              fmt.format("%-7s", rec.getLevel());

              // Raw Date, e.g. "Fri Jan 11 10:41:11 MST 2008"
              //sb.append(new Date());
              // Formatted Date, e.g. "2008-01-11 09:49:03,795"
              SimpleDateFormat sdf
                  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
              sb.append(sdf.format(new Date()));                

              sb.append(" [");
              sb.append(rec.getSourceClassName());
              sb.append(".");
              sb.append(rec.getSourceMethodName());
              sb.append("()");
              sb.append("] ");
              sb.append(rec.getMessage());
              return sb.append("\n").toString();
          }
      };


  // Instance variables----------------------------------------------

  // Constructors----------------------------------------------------

  /**
   * Create a Logger whose name is given by the calling class, with
   * a default Level of INFO.
   */
  public JLogger() {
      super (new Throwable().getStackTrace()[1].getClassName(), null);
      setLevel(INFO);
      LogManager.getLogManager().addLogger (this);
  }


  /**
   * Create a Logger whose name is given explicitly.  This calls
   * XLogger(name, Level.INFO).  This may be slightly faster than XLogger()
   * with no argument because it does not determine the calling
   * class's name with
   * <pre>
   * (new Throwable()).getStackTrace()[1].getClassName()
   * </pre>
   *
   * @param name the name of the logger.
   */
  public JLogger(String name) {
      this(name, Level.INFO);
  }


  /**
   * Create a Logger whose name is given by the calling class and
   * with a given Level and formatting from getDefaultHandler().
   *
   * @param level A logging Level.
   */
  public JLogger(Level level) {
      super (new Throwable().getStackTrace()[1].getClassName(), null);
      setLevel(level);
      addHandler(getDefaultHandler(level));
      setUseParentHandlers(false);
      LogManager.getLogManager().addLogger(this);
  }


  /**
   * Create a Logger with a given name and a given Level and
   * formatting from getDefaultHandler().
   *
   * @param name the name of the logger.
   * @param level A logging Level.
   */
  public JLogger(String name, Level level) {
      super(name, null);
      setLevel(level);
      addHandler(getDefaultHandler(level));
      setUseParentHandlers(false);
      LogManager.getLogManager().addLogger (this);
  }


  protected JLogger(String s, String t) {
      super (s, t);
      LogManager.getLogManager().addLogger (this);
  }


  // Methods---------------------------------------------------------

  /**
   * Get a logger named after the class.
   */
  public static JLogger getXLogger() {
      String name = new Throwable().getStackTrace()[1].getClassName();
      JLogger l = new JLogger(name, INFO);
      return (l);
  }


  /**
   * Get a default Handler for a certain Level.  
   *
   * @param level The logging Level for for this Handler.
   */
  public Handler getDefaultHandler(Level level) {
      //  Handler handler = new StreamHandler(System.err, formatter);
      // @see <a href="http://tinyurl.com/26obk4">Javaranch thread</a>.
      Handler handler = new Handler() {
              @Override
              public void publish(LogRecord record){
                  try {
                      System.out.write(
                          DATELESS_FORMATTER.format(record).getBytes());
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }

              @Override
              public void close() throws SecurityException {}

              @Override
              public void flush() {}
          };
      handler.setLevel(level);

      return handler;
  }


  /**
   * Log entering a method without having to pass the class and method
   * names.
   */
  public void entering() {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      entering(ste.getClassName(), ste.getMethodName());
  }


  /**
   * Log entering a method without having to pass the class and method
   * names.
   *
   * @param o the Object logged along with the class and method.
   */
  public void entering(Object o) {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      entering(ste.getClassName(), ste.getMethodName(), o);
  }


  /**
   * Log entering a method without having to pass the class and method
   * names.
   *
   * @param o the array of Objects logged along with the class and method.
   */
  public void entering(Object[] o) {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      entering(ste.getClassName(), ste.getMethodName(), o);
  }


  /**
   * Log leaving a method without having to pass the class and method
   * names.
   */
  public void exiting() {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      exiting(ste.getClassName(), ste.getMethodName());
  }


  /**
   * Log leaving a method without having to pass the class and method
   * names.
   *
   * @param o the Object logged along with the class and method.
   */
  public void exiting(Object o) {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      exiting(ste.getClassName(), ste.getMethodName(), o);
  }


  /**
   * Log throwing without having to pass the class and method names.
   *
   * @param t the Throwable.
   */
  public void throwing(Throwable t) {
      StackTraceElement ste = new Throwable().getStackTrace()[1];
      throwing(ste.getClassName(), ste.getMethodName(), t);
  }


  /**
   * @return A String representation of this class.
   */
  public String toString() {
      String s = getName() + ": ";
      s += getLevel() + "'";
      if (getParent() != null) {
          s += getParent().getName() + "'";
      }
      return (s);
  }


  /**
   * Remove the Handlers from a Logger.
   */
  public static void removeHandlers(JLogger l) {
      Handler[] handlers = l.getHandlers();
      for (int i=0; i<handlers.length; i++) {
          l.removeHandler(handlers[i]);
      }
  }


  /**
   * A main() to try out logging.
   * Run with
   * <pre>java -cp classes nreldata.util.XLogger
   * </pre>
   */
  public static void main(String args[]) {
      System.out.println();

      final JLogger L = new JLogger();
      System.out.println(L);
      L.info("info");
      L.fine("fine");
      L.severe("severe");

      System.out.println();

  }
}
