package gov.nrel.nbc.spreadsheetadmin.dao;

import gov.nrel.nbc.spreadsheetadmin.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheetadmin.client.NameValue;
import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.server.AdminServiceImpl;
import gov.nrel.nbc.spreadsheetadmin.utilities.XLogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SheetDataDAOHibernate extends GenericHibernateDAO<SheetConfig, Long>
		implements SheetDataDAO, AppConstants {

	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(XLogger.INFO);

	private static final String SHEET = "sheet";

	/**
	 * Public method to return a CellDataHeader given the sheet name
	 *  and WorkbookConfig.
	 * 
	 * @param config WorkbookConfig
	 * @param name String
	 * @return CellDataHeader
	 */
	public CellDataHeader findByNameAndConfig(WorkbookConfig config, String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(SHEET, name));
				
		return (CellDataHeader)crit.uniqueResult();
	}
	
	/**
	 * Public method to return a list of CellDataHeaders given the 
	 *  sheet name.
	 * 
	 * @param name String
	 * @return List<CellDataHeader>
	 */
	@SuppressWarnings("unchecked")
	public List<CellDataHeader> findBySheetName(String name) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(CellDataHeader.class)
			.add(Restrictions.eq(SHEET, name));
				
		return (List<CellDataHeader>)crit.list();
	}
	
	/**
	 * Public method to return a list of CellDataHeader given the sheet.
	 * Could return null.
	 * 
	 * @param sheet <SheetConfig>
	 * @return <List<CellDataHeader>>
	 */
	public List<NameValue> findBySheet(String wbConfig, String shConfig) {

		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		List<NameValue> headers = new ArrayList<NameValue>();
		
		try {
			session = getSession();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			MetadataHeaderDAOHibernate mddh = new MetadataHeaderDAOHibernate();
			mddh.setSession(session);
			wcdh.setSession(session);
			
			session.beginTransaction();
			
			WorkbookConfig wbc = wcdh.findByName(wbConfig);
			List<MetadataHeader> metas = mddh.findByConfig(wbc);
			int numMetas = 0;
			if (metas != null && !metas.isEmpty())
				numMetas = metas.size();
			
			// Load the database driver
			Class.forName( MYSQL_DRIVER ) ;
			// Get a connection to the database
	        String url = FIRST_PART_DB;
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	url += DevTestProdConstants.DEV;
	        else
	        	url += DevTestProdConstants.DEV_PROD_TEST;
	        url += LAST_PART_DB;
			String user = HibernateSessionFactory.getConfiguration().getProperty(USERTAG);
			String pass = HibernateSessionFactory.getConfiguration().getProperty(PASSTAG);
	        conn = DriverManager.getConnection(url,user,pass);
			// Get a statement from the connection
			stmt = conn.createStatement() ;
			String sql = "describe `"+AdminServiceImpl.getTableName(wbConfig,shConfig)+"`";
			int rowCtr=0;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rowCtr>NUM_INFO_COLUMNS+numMetas) {
					String name = rs.getString(1);
					String type = rs.getString(2);
					long dtype = 0;
					NameValue nv = new NameValue();
					if (type.startsWith("bigint")) {
						dtype = LONG;
					}
					else if (type.startsWith("varchar")) {
						dtype = STRING;
					}
					else if (type.equals("datetime")) {
						dtype = DATE;
						nv.setDataFormat("4");
					}
					else if (type.equals("double")) {
						dtype = REAL;
						nv.setDataFormat("10");
					}
					nv.setName(name);
					nv.setDataType(dtype);
					nv.setOrder(rowCtr-2);
					headers.add(nv);
				}
				rowCtr++;
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting headers. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch( SQLException se ) {
		      System.out.println( "SQL Exception:" ) ;
			  log.warning(getStackTrace(se));
		      // Loop through the SQL Exceptions
		      while( se != null )
		      {
		          System.out.println( "Message: " + se.getMessage()   ) ;
		          se = se.getNextException() ;
		      }
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			log.warning(getStackTrace(e));
		} finally {
			// Close the result set, statement and the connection
			try {
				if (stmt != null)
					stmt.close() ;
				if (conn != null)
					conn.close() ;
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
				  log.warning(getStackTrace(se));
			} catch (Exception e) {
				log.severe("Exception on getting type. error: " + e);
				log.warning(getStackTrace(e));
			}
		}
		return headers;
	}
	/**
	 * Public static method to return a stack trace
	 * 
	 * @param t
	 * @return <String> stack track
	 */
	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
