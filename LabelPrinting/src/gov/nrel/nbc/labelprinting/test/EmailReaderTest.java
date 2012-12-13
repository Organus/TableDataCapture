package gov.nrel.nbc.labelprinting.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.LabelDTO;
import gov.nrel.nbc.labelprinting.server.EmailReader;
import gov.nrel.nbc.labelprinting.server.TrackerFileUploadServiceImpl;
import gov.nrel.nbc.labelprinting.server.TrackerServiceImpl;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EmailReaderTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = EmailReader.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(EmailReaderTest.class);
	}
	///*
	public void testEmailReader() throws Exception {
		TrackerFileUploadServiceImpl susi = new TrackerFileUploadServiceImpl();
        String popServer=susi.pop_host;//"mail.nrel.gov";//"mailgate1.nrel.gov";//"MAILBOX2.nrel.gov";//"pop3.nrel.gov";//
        String popUser=susi.pop_user;//TEST_USER;
    	DataEncryption de = new DataEncryption(CRYPTOKEY);
    	String unc = de.decrypt(susi.pop_pass);//TEST_PASS);
        String popPassword=unc;
             
        EmailReader er = new EmailReader();
        er.receive(popServer, popUser, popPassword);
	}
//*/
	public void testLabelPrinting() throws Exception {
		EmailReader er = new EmailReader();
	    // Create properties, get Session
	    Properties props = new Properties();
	
	    javax.mail.Session session = javax.mail.Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject(SABC_LABEL_PRINTING);
        LabelDTO label = new LabelDTO();
        label.setDescription("Now is the time for all good men to come to the aid of their country.");
        label.setForm("frozen");
        label.setFraction("liquid");
        label.setComposition("High Protein");
        label.setSampleId("98765");
        label.setStrain("Nannochloropsis");
        label.setDestination("ASU");
        label.setCustodian("NREL:Lieve Laurens");
        label.setEntryDate("04/20/2011");
        label.setTrackingId("56789");
        TrackerServiceImpl tsi = new TrackerServiceImpl();
        String body = tsi.createLabelMessage(label, "15747s_18200s");
        MimeBodyPart mbp1 = new MimeBodyPart();
        msg.setFrom(new InternetAddress("James.Albersheim@nrel.gov"));
        mbp1.setText(body);
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        msg.setContent(mp);
        er.processMessage(msg);
	}
	public void testEmailLabel() throws Exception {
	    // Create properties, get Session
		Transport t = null;
		try {
		    Properties props = new Properties();
			
    		TrackerFileUploadServiceImpl susi = new TrackerFileUploadServiceImpl();
            String host=susi.pop_host;//"mail.nrel.gov";//"mailgate1.nrel.gov";//"MAILBOX2.nrel.gov";//"pop3.nrel.gov";//
            System.out.println("host="+host);
		    props.put("mail.smtp.host", host);
		    props.put("mail.debug", "true");
			
		    javax.mail.Session session = javax.mail.Session.getInstance(props);
	
	        Message msg = new MimeMessage(session);
	        msg.setSubject(SABC_LABEL_PRINTING);
	        LabelDTO label = new LabelDTO();
	        label.setDescription("Now is the time for all good men to come to the aid of their country.");
	        label.setForm("frozen");
	        label.setFraction("liquid");
	        label.setComposition("High Protein");
	        label.setSampleId("98765");
	        label.setStrain("Nannochloropsis");
	        label.setDestination("ASU");
	        label.setCustodian("NREL:Lieve Laurens");
	        label.setEntryDate("04/20/2011");
	        label.setTrackingId("56789");
	        TrackerServiceImpl tsi = new TrackerServiceImpl();
	        String body = tsi.createLabelMessage(label, "15747s_18200s");
	        MimeBodyPart mbp1 = new MimeBodyPart();
	        msg.setFrom(new InternetAddress("James.Albersheim@nrel.gov"));
	        mbp1.setText(body);
	        Multipart mp = new MimeMultipart();
	        mp.addBodyPart(mbp1);
	        msg.setContent(mp);
	        t = session.getTransport("smtp");
            String popUser=susi.pop_user;//TEST_USER;
            System.out.println("user="+popUser);
        	DataEncryption de = new DataEncryption(CRYPTOKEY);
        	String unc = de.decrypt(susi.pop_pass);//TEST_PASS);
            t.connect(popUser, unc);
            t.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e1) {
        	System.out.println("caught: "+e1.getMessage());
        	//System.out.println("caught: "+e1.getMessage());
        } finally {
            t.close();
        }
	}
}
