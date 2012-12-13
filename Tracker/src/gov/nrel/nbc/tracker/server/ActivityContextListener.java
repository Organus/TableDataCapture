package gov.nrel.nbc.tracker.server;

import gov.nrel.nbc.tracker.utils.XLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ActivityContextListener implements ServletContextListener {

    private static ActivityThread activityThread = null;

    private static final XLogger log = new XLogger(ActivityContextListener.class);

	private static final ActivityContextListener INSTANCE = new ActivityContextListener();

	public static ActivityContextListener getInstance() { return INSTANCE; }
    
	public ActivityContextListener() {
    }
    
    public void start() {
    	System.out.println("in ActivityContextListener.start");
    	log.info("in ActivityContextListener.start");
        if ((activityThread == null) || (!activityThread.isAlive())) {
        	activityThread = new ActivityThread();
        	activityThread.setDaemon(true);
        	log.info("starting activityThread...");
        	activityThread.start();
        }    	
    }
    public void contextInitialized(ServletContextEvent sce) {
    	log.info("in ActivityContextListener.contextInitialized");
    	System.out.println("in ActivityContextListener.contextInitialized");
    	start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
        	activityThread.doShutdown();
        	log.info("activityThread destroyed");
        	System.out.println("activityThread destroyed");
        } catch (Exception ex) {
        }
    }
}

