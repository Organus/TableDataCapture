package gov.nrel.nbc.security.dbUtils;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.client.DevTestProdConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import gov.nrel.nbc.security.utils.XLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class DBUtils implements AppConstants, DevTestProdConstants {
	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(XLogger.INFO);
	//private DBUtils _instance = null;
	private String url = null;
	private Statement stmt = null;
	private Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public DBUtils() {
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
		} catch (ClassNotFoundException cnf) {
			log.severe("Class for "+MYSQL_DRIVER+" not found. Cannot proceed. Exiting...");
			return;
		}
		SecurityServiceImpl susi = new SecurityServiceImpl();
		
		// Get a connection to the database
        url = INITIAL_PART_DB;//FIRST_PART_DB;
        // If it's a real production environment then build the path from NBCSecurity.properties
        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.SABC)) {
        	url += susi.dbPathValue + LAST_PROD_PART_DB;
        } else if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.PROD))
        	url += susi.dbPathValue + LAST_PROD_PART_DB;
        else {
	        if (!DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.SABC))
	        	url += FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else 
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += NREL;
	        url += LAST_PART_DB;
        }
        //log.info("url="+url);
        //log.info("user="+dbUserValue);
		//String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
		//String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
        conn = getConn();
        stmt = getStmt();
	}  
	public DBUtils(String dbPath, String dbUser, String dbPass) {
		try {
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
		} catch (ClassNotFoundException cnf) {
			log.severe("Class for "+MYSQL_DRIVER+" not found. Cannot proceed. Exiting...");
			return;
		}
        //log.info("url="+url);
        //log.info("user="+dbUserValue);
		url = dbPath;
        conn = getConn();
        stmt = getStmt();
	}  
	public boolean getNextRow() {
		boolean ret = true;
		try {
			if (rs != null && !rs.isClosed())// && rs.isBeforeFirst() && !rs.isAfterLast())
				ret = rs.next();
			else
				ret = false;
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public String getStringColumn(int column) {
		String ret = null;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getString(column);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public long getLongColumn(int column) {
		long ret = 0;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getLong(column);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public int getIntColumn(int column) {
		int ret = 0;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getInt(column);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public boolean getBooleanColumn(int column) {
		short ret = 0;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getShort(column);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		if (ret == 0) return false;
		return true;
	}
	public Date getDateColumn(int column) {
		Date ret = null;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getDate(column);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public void addToBatch(String sql, boolean info) {
		conn = getConn();
		stmt = getStmt();
		try {
			if (info)
				log.info("sql="+sql);
			stmt.addBatch(sql);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public int[] performBatchInsert() {
		int[] ret = null;
		conn = getConn();
		stmt = getStmt();
		try {
			ret = stmt.executeBatch();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public ResultSet performQuery(String sql, boolean info) {
		conn = getConn();
		stmt = getStmt();
		try {
			if (info)
				log.info("sql="+sql);
			rs = stmt.executeQuery(sql);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return rs;
	}
	public void prepareStatement(String sql) {
		conn = getConn();
		try {
			pstmt = conn.prepareStatement(sql);
			//pstmt.setInt(2, 100);
			//pstmt.setString(1, "Bob");
			pstmt.executeUpdate();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		//close();
	}
	public void setParam(int pos,int value) {
		try {
			pstmt.setInt(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public void setParam(int pos,long value) {
		try {
			pstmt.setLong(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public void setParam(int pos,double value) {
		try {
			pstmt.setDouble(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public void setParam(int pos,String value) {
		try {
			pstmt.setString(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public void setParam(int pos,java.sql.Date value) {
		try {
			pstmt.setDate(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public void setParam(int pos,boolean value) {
		try {
			pstmt.setBoolean(pos,value);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
	}
	public int executeUpdate() {
		int ret = 0;
		try {
			ret = pstmt.executeUpdate();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
	public int performInsert(String sql) {
		conn = getConn();
		stmt = getStmt();
		int ret = 0;
		try {
			//log.info("Sql="+sql);
			ret = stmt.executeUpdate(sql);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		//close();
		return ret;
	}
	public int performDelete(String sql) {
		conn = getConn();
		stmt = getStmt();
		int ret = 0;
		try {
			//log.info("Sql="+sql);
			ret = stmt.executeUpdate(sql);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		//close();
		return ret;
	}
	public void commit() {
		try {
			if (conn != null)
				conn.commit();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		}		
	}
	public void rollback() {
		try {
			if (conn != null)
				conn.rollback();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		}		
	}
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
				//tx.commit();
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warning(SecurityServiceImpl.getStackTrace(se));
			} catch (Exception e) {
				log.severe("Exception on getting type. error: " + e);
				log.warning(SecurityServiceImpl.getStackTrace(e));
			}
			stmt = null;
			conn = null;
		}		
	}
	/**
	 * @return the stmt
	 */
	public Statement getStmt() {
		try {
			if (stmt != null && !stmt.isClosed()) return stmt;
			stmt = conn.createStatement();
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return stmt;
	}
	/**
	 * @param stmt the stmt to set
	 */
	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
	/**
	 * @return the conn
	 */
	public Connection getConn() {
		try {
			SecurityServiceImpl susi = new SecurityServiceImpl();
			if (conn != null && !conn.isClosed()) return conn;
			//log.info("url="+url);
			//log.info("user="+susi.dbUserValue);
			//log.info("pass="+getDbPass());
			conn = DriverManager.getConnection(url,susi.dbUserValue,getDbPass());
		} catch( SQLException se ) {
		      log.warning( "SQL Exception encountered connecting to "+url+":" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          log.warning( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return conn;
	}
	/**
	 * @param conn the conn to set
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	/**
	 * @return the dbPass
	 */
	public String getDbPass() {
		SecurityServiceImpl susi = new SecurityServiceImpl();
		DataEncryption de = new DataEncryption(CRYPTOKEY);
		String dbPassDecrypted = de.decrypt(susi.dbPassValue);
		return dbPassDecrypted;
	}
	/**
	 * @param dbPass the dbPass to set
	 */
	public void setDbPass(String dbPass) {
		SecurityServiceImpl susi = new SecurityServiceImpl();
		DataEncryption de = new DataEncryption(CRYPTOKEY);
		dbPass = de.decrypt(susi.dbPassValue);
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	public int performUpdate(String sql) {
		return performInsert(sql);
	}
	public Timestamp getTimestampColumn(int i) {
		Timestamp ret = null;
		try {
			if (rs.isBeforeFirst()) rs.next();
			ret = rs.getTimestamp(i);
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(SecurityServiceImpl.getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(SecurityServiceImpl.getStackTrace(e));
		} 
		return ret;
	}
}
