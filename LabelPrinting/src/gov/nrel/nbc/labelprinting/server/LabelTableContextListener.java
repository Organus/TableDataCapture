package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.utils.XLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LabelTableContextListener implements ServletContextListener {

    private static LabelTableThread labelTableThread = null;

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(LabelTableContextListener.class);

	private static final PrinterContextListener INSTANCE = new PrinterContextListener();

	public static PrinterContextListener getInstance() { return INSTANCE; }
    
	public LabelTableContextListener() {
    }
    
    public void start() {
    	System.out.println("in LabelTableContextListener.start");
    	log.info("in LabelTableContextListener.start");
        if ((labelTableThread == null) || (!labelTableThread.isAlive())) {
        	labelTableThread = new LabelTableThread();
        	labelTableThread.setDaemon(true);
        	log.info("starting labelTableThread...");
        	labelTableThread.start();
        }    	
    }
    public void contextInitialized(ServletContextEvent sce) {
    	log.info("in LabelTableContextListener.contextInitialized");
    	System.out.println("in LabelTableContextListener.contextInitialized");
    	start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
        	labelTableThread.doShutdown();
        	log.info("labelTableThread destroyed");
        	System.out.println("labelTableThread destroyed");
        } catch (Exception ex) {
        }
    }
}

