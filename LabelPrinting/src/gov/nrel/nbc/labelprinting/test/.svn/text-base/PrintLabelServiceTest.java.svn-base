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
import gov.nrel.nbc.labelprinting.server.PrintLabelService;
import gov.nrel.nbc.labelprinting.server.TrackerFileUploadServiceImpl;
import gov.nrel.nbc.labelprinting.server.TrackerServiceImpl;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PrintLabelServiceTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = EmailReader.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(PrintLabelServiceTest.class);
	}
	public void testEmailLabel() throws Exception {
	    // Create properties, get Session
		try {
	        LabelDTO label = new LabelDTO();
	        label.setDescription("Now is the time for all good men to come to the aid of their country.");
	        //label.setForm("frozen");
	        label.setFraction("liquid");
	        //label.setComposition("High Protein");
	        label.setSampleId("98765");
	        //label.setStrain("Nannochloropsis");
	        //label.setDestination("ASU");
	        //label.setCustodian("NREL:Lieve Laurens");
	        label.setEntryDate("04/20/2011");
	        label.setTrackingId("56789");
	        PrintLabelService pls = new PrintLabelService();
	        String s = pls.createForm(0, "SABC",label);
	        s += pls.createLabel(label,"SABC");
	        System.out.println("label=<"+s+">");
        } catch (Exception e1) {
        	System.out.println("caught: "+e1.getMessage());
        	//System.out.println("caught: "+e1.getMessage());
        } 
	}
	public void testPrintLabel() throws Exception {
	    // Create properties, get Session
		try {
	        LabelDTO label = new LabelDTO();
	        label.setDescription("Now is the time for all good men to come to the aid of their country.");
	        //label.setForm("frozen");
	        label.setFraction("liquid");
	        //label.setComposition("High Protein");
	        label.setSampleId("98765");
	        //label.setStrain("Nannochloropsis");
	        //label.setDestination("ASU");
	        //label.setCustodian("NREL:Lieve Laurens");
	        label.setEntryDate("04/20/2011");
	        label.setTrackingId("56789");
	        PrintLabelService pls = new PrintLabelService();
	        pls.printLabel(label, "", "SABC");
        } catch (Exception e1) {
        	System.out.println("caught: "+e1.getMessage());
        	//System.out.println("caught: "+e1.getMessage());
        } 
	}
}
