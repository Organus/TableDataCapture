package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.utils.XLogger;
import gov.nrel.nbc.security.crypto.DataEncryption;

public class LabelTableThread extends Thread implements AppConstants {

	private static final long serialVersionUID = 7878740493440924695L;
	private static final int INTERVAL = 120; // seconds
        
	public LabelTableThread() {
	}
    private static final XLogger log = new XLogger(LabelTableThread.class);

    private void checkTable() {
    	TrackerFileUploadServiceImpl susi = new TrackerFileUploadServiceImpl();
    	
        String dbPath=susi.db_dev;
        String dbUser=susi.db_user;
        
        DataEncryption de = new DataEncryption(CRYPTOKEY);
        String dbPass=de.decrypt(susi.db_pass);
             
        LabelTableReader ltr = new LabelTableReader();
        ltr.receive(dbPath, dbUser, dbPass);    	
    }
    
	@Override
	public void run() {
		log.info("in LabelTableThread.run");
		System.out.println("in LabelTableThread.run");
		try {
			while (true) {
				checkTable();
				Thread.sleep(INTERVAL*500);
			}
		} catch (InterruptedException ie) {
			log.warning("Interruption occurred: "+ie);
		} catch (Exception e) {
			log.warning("Exception occurred: "+e);
		}
	}
    public void doShutdown() {
        Thread moribund = this;
        moribund.interrupt();
        moribund = null;
    }

}
