package gov.nrel.nbc.spreadsheet.hibernate;

//import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.sql.DataSource;

import gov.nrel.nbc.spreadsheet.client.DevTestProdConstants;
import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
//import com.mchange.v2.c3p0.*;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern.
 */
public class HibernateSessionFactory implements DevTestProdConstants {

    /** 
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses  
     * #resourceAsStream style lookup for its configuration file. 
     * The default classpath location of the hibernate config file is 
     * in the default package. Use #setConfigFile() to update 
     * the location of the configuration file for the current session.   
     */
    private static String CONFIG_FILE_LOCATION = "/"+DEV_PROD_TEST+"hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static Configuration configuration = new Configuration();    
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = CONFIG_FILE_LOCATION;
	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final XLogger log = new XLogger(HibernateSessionFactory.class);

	static {
    	try {
	        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
	        	configFile = "/"+DevTestProdConstants.DEV+"hibernate.cfg.xml";
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
    }
    private HibernateSessionFactory() {
    }
	
	/**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *  @return Session
     *  @throws HibernateException
     */
	//@SuppressWarnings("deprecation")
	public static Session getSession() throws HibernateException {
        Session session = (Session) threadLocal.get();

		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}

		/*
		try {
			//Connection connection = (Connection) session.connection();
			ComboPooledDataSource cpds = new ComboPooledDataSource(); 
			cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver 
			cpds.setJdbcUrl( "jdbc:mysql://*/spread_sheet" ); 
			cpds.setUser("user"); 
			cpds.setPassword("password"); 	
			// cpds has direct accessors for all config properties, check out
			// the api docs for ComboPooledDataSource
			int num = cpds.getNumConnections();
			log.info("num connections = "+num);
			num = cpds.getNumBusyConnections();
			log.info("num busy connections = "+num);
			num = cpds.getInitialPoolSize();
			log.info("initial pool size = "+num);
			num = cpds.getMaxPoolSize();
			log.info("max pool size = "+num);
			num = cpds.getNumIdleConnections();
			log.info("num idle connections = "+num);
		} catch (PropertyVetoException pve) {
			log.warning("Property exception caught: "+pve);
		} catch (SQLException se) {
			log.warning("SQL exception caught: "+se);
		} catch (Exception e) {
			log.warning("Exception caught: "+e);
		}
		*/
		return session;
    }

	/**
     *  Rebuild hibernate session factory
     *
     */
	public static void rebuildSessionFactory() {
		try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
     *  Close the single hibernate session instance.
     *
     *  @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null && session.isConnected()) {
			if (session.isOpen()) {
				session.flush();
				session.close();
			}
        }
    }

	/**
     *  return session factory
     *
     */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
     *  return session factory
     *
     *	session factory will be rebuilded in the next call
     */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
     *  return hibernate configuration
     *
     */
	public static Configuration getConfiguration() {
		return configuration;
	}

	static void destroy() throws SQLException {
		log.info("in destroy");
		/*
		ComboPooledDataSource ds_pooled = null; 
		try { 
			DataSource ds_unpooled = DataSources.unpooledDataSource("jdbc:mysql://*/spread_sheet", "user", "password"); 
			ds_pooled = (ComboPooledDataSource)DataSources.pooledDataSource( ds_unpooled ); 
			// do all kinds of stuff with that sweet pooled DataSource... 
			int num = ds_pooled.getNumConnections();
			log.info("num connections = "+num);
			num = ds_pooled.getNumBusyConnections();
			log.info("num busy connections = "+num);
			num = ds_pooled.getInitialPoolSize();
			log.info("initial pool size = "+num);
			num = ds_pooled.getMaxPoolSize();
			log.info("max pool size = "+num);
			num = ds_pooled.getNumIdleConnections();
			log.info("num idle connections = "+num);
		} finally { 
			DataSources.destroy( ds_pooled ); 
		} 	
		*/
	}
	
	//@SuppressWarnings("deprecation")
	static void cleanup(DataSource ds) throws SQLException 
	{ 
		log.info("in cleanup");
		/*
		// make sure it's a c3p0 PooledDataSource 
		if ( ds instanceof PooledDataSource) 
		{ 
			PooledDataSource pds = (PooledDataSource) ds; 
			int num = pds.getNumConnections();
			log.info("num connections = "+num);
			num = pds.getNumBusyConnections();
			log.info("num busy connections = "+num);
			num = pds.getNumIdleConnections();
			log.info("num idle connections = "+num);
			pds.close(); 
		} else {
			log.warning("Not a c3p0 PooledDataSource!");
			if (ds instanceof ComboPooledDataSource) {
				ComboPooledDataSource ds_pooled = (ComboPooledDataSource)ds; 
				// do all kinds of stuff with that sweet pooled DataSource... 
				int num = ds_pooled.getNumConnections();
				log.info("num connections = "+num);
				num = ds_pooled.getNumBusyConnections();
				log.info("num busy connections = "+num);
				num = ds_pooled.getInitialPoolSize();
				log.info("initial pool size = "+num);
				num = ds_pooled.getMaxPoolSize();
				log.info("max pool size = "+num);
				num = ds_pooled.getNumIdleConnections();
				log.info("num idle connections = "+num);
			}
		}
		*/
	} 
}