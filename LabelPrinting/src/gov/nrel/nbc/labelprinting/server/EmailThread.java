package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.utils.XLogger;
import gov.nrel.nbc.security.crypto.DataEncryption;

public class EmailThread extends Thread implements AppConstants {

	private static final long serialVersionUID = 7878740493440924695L;
	private static final int INTERVAL = 120; // seconds
        
	public EmailThread() {
	}
    private static final XLogger log = new XLogger(EmailThread.class);

    private void checkEmail() {
    	TrackerFileUploadServiceImpl susi = new TrackerFileUploadServiceImpl();
    	
        String popServer=susi.pop_host;//"mail.nrel.gov";//"mailgate1.nrel.gov";//"MAILBOX2.nrel.gov";//"pop3.nrel.gov";//
        String popUser=susi.pop_user;//POP_USER;
        
        DataEncryption de = new DataEncryption(CRYPTOKEY);
        String popPassword=de.decrypt(susi.pop_pass);//POP_PASS);
             
        EmailReader er = new EmailReader();
        er.receive(popServer, popUser, popPassword);    	
    }
    
	@Override
	public void run() {
		log.info("in EmailThread.run");
		System.out.println("in EmailThread.run");
		try {
			while (true) {
				checkEmail();
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
