package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.utils.XLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PrinterContextListener implements ServletContextListener {

    private static PrinterStatusThread printerStatusThread = null;

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(PrinterContextListener.class);

	private static final PrinterContextListener INSTANCE = new PrinterContextListener();

	public static PrinterContextListener getInstance() { return INSTANCE; }
    
	public PrinterContextListener() {
    }
    
    public void start() {
    	System.out.println("in PrinterContextListener.start");
    	log.info("in PrinterContextListener.start");
        if ((printerStatusThread == null) || (!printerStatusThread.isAlive())) {
        	printerStatusThread = new PrinterStatusThread();
        	printerStatusThread.setDaemon(true);
        	log.info("starting printerStatusThread...");
        	printerStatusThread.start();
        }    	
    }
    public void contextInitialized(ServletContextEvent sce) {
    	log.info("in PrinterContextListener.contextInitialized");
    	System.out.println("in PrinterContextListener.contextInitialized");
    	start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
        	printerStatusThread.doShutdown();
        	log.info("printerStatusThread destroyed");
        	System.out.println("printerStatusThread destroyed");
        } catch (Exception ex) {
        }
    }
}

