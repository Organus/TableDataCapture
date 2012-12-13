package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.utils.XLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class EmailContextListener implements ServletContextListener {

    private static EmailThread emailThread = null;

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(EmailContextListener.class);

	private static final PrinterContextListener INSTANCE = new PrinterContextListener();

	public static PrinterContextListener getInstance() { return INSTANCE; }
    
	public EmailContextListener() {
    }
    
    public void start() {
    	System.out.println("in EmailContextListener.start");
    	log.info("in EmailContextListener.start");
        if ((emailThread == null) || (!emailThread.isAlive())) {
        	emailThread = new EmailThread();
        	emailThread.setDaemon(true);
        	log.info("starting emailThread...");
        	emailThread.start();
        }    	
    }
    public void contextInitialized(ServletContextEvent sce) {
    	log.info("in EmailContextListener.contextInitialized");
    	System.out.println("in EmailContextListener.contextInitialized");
    	start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
        	emailThread.doShutdown();
        	log.info("emailThread destroyed");
        	System.out.println("emailThread destroyed");
        } catch (Exception ex) {
        }
    }
}

