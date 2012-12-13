package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.LabelDTO;
import gov.nrel.nbc.labelprinting.dao.PrintersDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterName;

/**
 * Public class that handles printing labels.
 * 
 * @author jalbersh
 *
 */
public class PrintLabelService implements AppConstants {

	private static final int START_POSITION = 40;

	private static final int MAX_PER_LINE = 51;

	private static final String WINDOWS = "windows";

	private static final String UNIX_LPR = "/usr/bin/rlpr -P";

	//private static final String UNIX_LP = "/usr/bin/lp -d";
	
	private static final String WINDOWS_PRINT = "\\windows\\system32\\print.exe /D";

	//private static final String PRINTCAP = "/etc/printcap";
	private static final String PRINTCAP = "/etc/print.cfg";

	private static final String TRB_PAGE_NUMBER_FORMAT = "000";

	private static final String TRB_NUMBER_FORMAT = "00000";

	private static final String NEW_LINE = "\n";
	

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(PrintLabelService.class);

	private static final int REMOTEPRINTER = 1;
	private static final int MACHINE = 2;
        
    /**
     * <LabelDTO> the label to print
     */
    private LabelDTO label;

    /**
     * Initialize label
     */
    public void initLabel() {
    	label = new LabelDTO();
    }
    
    /**
     * Setter for label owner name
     * 
     * @param owner <String> owner name
     */
    public void setOwnerName(String owner) {
    	label.setOwnerName(owner);
    }
    
    /**
     * Setter for TRB page number
     * 
     * @param trbPage <String> TRB page number
     */
    public void setTrbPage(String trbPage) {
    	label.setTrbPage(trbPage);
    }
    
    /**
     * Setter for TRB number
     * 
     * @param trbNum <String> TRB number
     */
    public void setTrbNum(String trbNum) {
    	label.setTrbNum(trbNum);
    }
    
    /**
     * Setter for Tracking ID
     * 
     * @param id <String> Tracking ID
     */
    public void setTrackingId(String id) {
    	label.setTrackingId(id);
    }
    
    /**
     * Setter for sample id
     * 
     * @param id <String> sample id
     */
    public void setSampleId(String id) {
    	label.setSampleId(id);
    }
    
    /**
     * Setter for entry date
     * 
     * @param date <String> entry date
     */
    public void setEntryDate(String date) {
    	label.setEntryDate(date);
    }
    
    /**
     * Public method to set a label as having been printed.
     * 
     * @param printed - boolean whether label has been printed or not.
     */
    public void setPrinted(boolean printed) {
    	label.setPrinted(printed);
    }
    
    /**
     * Public method to create "duplicate" label
     * 
     * @return <String> commands to print duplicate label
     */
    public String createDuplicateForm(int len, String group, LabelDTO label) {
    	int pos = 295 - (20*(int)Math.round(len/2));
    	//log.info("pos="+pos);
	    String form = "";
	    log.info("trb num="+label.getTrbNum());
	    if (!group.equals("SABC")&&!group.equals(JUNIT_GROUP)) {
	    	form = 
	    	"N\n" +
		    "FK\"FORM1\"\n" +
	        "FK\"FORM1\"\n" +
	        "FS\"FORM1\"\n" +        
	        "V00,80,N,\"\"\n" +
	        "V01,80,N,\"\"\n" +
	        "V02,80,N,\"\"\n" +
	        "V03,80,N,\"\"\n" +
	        "V04,80,N,\"\"\n" +
	        "V05,9,N,\"\"\n" +
	        "V06,10,N,\"\"\n" +
	        "V07,32,N,\"\"\n" +
	        "V08,20,N,\"\"\n" +
	        "V09,5,N,\"\"\n" +
	        "V10,5,N,\"\"\n" +
	        "V11,5,N,\"\"\n" +
	        "V12,5,N,\"\"\n" +
			"R0,0\n" +   // Set Reference Point                                                             
	        "ZB\n" + // Print direction (from Bottom of buffer)
	        "Q150,21\n" +  // Set label Length and gap
	         "A415,8,0,1,1,1,N,V00\n" +
	        "A415,33,0,1,1,1,N,V01\n" +
	        "A295,45,0,1,2,2,N,V09\n" +
	        "A415,58,0,1,1,1,N,V02\n" +
	        "A445,83,0,1,1,1,N,\"Material: \"V03\n" +
	        "A475,108,0,1,1,1,N,\"Sample ID: \"V04\n" +
	        "A505,133,0,1,1,1,N,\"Trb: \"V05\n" +
	        "A375,135,0,1,2,2,N,V10\n" +
	        "A220,135,0,1,2,2,N,V11\n" +
	        "A505,158,0,1,1,1,N,\"Create Date: \"V06\n" +
	        "A475,183,0,1,1,1,N,\"Owner: \"V07\n" +
	        "B860,138,0,1A,2,7,100,B,V08\n" +    
	        "A445,208,0,2,1,1,N,\"DUPLICATE LABEL\"\n" +
	        "A"+pos+",208,0,1,2,2,N,V12\n" +
	        "FE\n" + NEW_LINE;
	    } else {
	    	form = 
	    	"N\n" +
		    "FK\"FORM1\"\n" +
	        "FK\"FORM1\"\n" +
	        "FS\"FORM1\"\n" +        
	        "V00,80,N,\"\"\n" +
	        "V01,80,N,\"\"\n" +
	        "V02,80,N,\"\"\n" +
	        "V03,80,N,\"\"\n" +
	        "V04,80,N,\"\"\n" +
	        "V05,30,N,\"\"\n" +
	        "V06,30,N,\"\"\n" +
	        "V07,32,N,\"\"\n" +
	        "V08,20,N,\"\"\n" +
	        "V09,20,N,\"\"\n" +
	        "V10,5,N,\"\"\n" +
	        "V11,5,N,\"\"\n" +
	        "V12,5,N,\"\"\n" +
	        "V13,5,N,\"\"\n" +
			"R0,0\n" +   // Set Reference Point                                                             
	        "ZB\n" + // Print direction (from Bottom of buffer)
	        "Q150,21\n" +  // Set label Length and gap
	        "A365,13,0,1,1,1,N,V00\n" +
	        "A385,38,0,1,1,1,N,V01\n" +
	        "A295,45,0,1,2,2,N,V10\n" +
	        "A405,63,0,1,1,1,N,V02\n" +
	        "A435,88,0,1,1,1,N,\"Material: \"V03\n" +
	        "A450,113,0,1,1,1,N,\"Strain: \"V05\n" +
	        "A375,135,0,1,2,2,N,V11\n" +
	        "A220,135,0,1,2,2,N,V12\n" +
	        "A470,138,0,1,1,1,N,\"Sample ID: \"V04\n";
    	if (((label.getTrbNum() != null && !label.getTrbNum().isEmpty()) && (label.getTrbPage() != null && !label.getTrbPage().isEmpty())))
    		form += "A450,163,0,1,1,1,N,\"Trb: \"V06\n";
    	else
    		form += "A450,163,0,1,1,1,N,\"Trb: \"\n";
        form += "A435,188,0,1,1,1,N,\"Custodian: \"V07\n" +
        	"A"+pos+",208,0,1,2,2,N,V13\n" +
	        "A405,213,0,1,1,1,N,\"Create Date: \"V08\n" +
	        "B870,113,0,1A,2,7,100,B,V09\n" +    
	        "A385,238,0,2,1,1,N,\"DUPLICATE LABEL\"\n" +
	        "FE\n" + NEW_LINE;    	
	    }
	    return form;
    }
    
    /**
     * Public method to create default form for label
     * 
     * @return <String> commands to print form on label.
     */
    public String createForm(int len, String group, LabelDTO label) {
    	int pos = 295 - (20*(int)Math.round(len/2));
    	//log.info("pos="+pos);
	    String form = "";
	    log.info("trb num="+label.getTrbNum());
	    if (!group.equals(SABC_GROUP)&&!group.equals(JUNIT_GROUP)) {
		    form = 
		    	"N\n" +
			    "FK\"FORM1\"\n" +
		        "FK\"FORM1\"\n" +
		        "FS\"FORM1\"\n" +        
		        "V00,80,N,\"\"\n" +
		        "V01,80,N,\"\"\n" +
		        "V02,80,N,\"\"\n" +
		        "V03,80,N,\"\"\n" +
		        "V04,80,N,\"\"\n" +
		        "V05,9,N,\"\"\n" +
		        "V06,10,N,\"\"\n" +
		        "V07,32,N,\"\"\n" +
		        "V08,20,N,\"\"\n" +
		        "V09,5,N,\"\"\n" +
		        "V10,5,N,\"\"\n" +
		        "V11,5,N,\"\"\n" +
		        "V12,5,N,\"\"\n" +
				"R0,0\n" +   // Set Reference Point                                                             
		        "ZB\n" + // Print direction (from Bottom of buffer)
		        "Q150,21\n" +  // Set label Length and gap
		        "A415,8,0,1,1,1,N,V00\n" +
		        "A415,33,0,1,1,1,N,V01\n" +
		        "A295,45,0,1,2,2,N,V09\n" +
		        "A415,58,0,1,1,1,N,V02\n" +
		        "A445,83,0,1,1,1,N,\"Material: \"V03\n" +
		        "A475,108,0,1,1,1,N,\"Sample ID: \"V04\n" +
		        "A505,133,0,1,1,1,N,\"Trb: \"V05\n" +
		        "A375,135,0,1,2,2,N,V10\n" +
		        "A220,135,0,1,2,2,N,V11\n" +
		        "A505,158,0,1,1,1,N,\"Create Date: \"V06\n" +
		        "A475,183,0,1,1,1,N,\"Owner: \"V07\n" +
		        "B860,138,0,1A,2,7,100,B,V08\n" +    
		        "A"+pos+",208,0,1,2,2,N,V12\n" +
		        "FE\n" + NEW_LINE;
	    } else {
		    form = 
		    	"N\n" +
			    "FK\"FORM1\"\n" +
		        "FK\"FORM1\"\n" +
		        "FS\"FORM1\"\n" +        
		        "V00,80,N,\"\"\n" +
		        "V01,80,N,\"\"\n" +
		        "V02,80,N,\"\"\n" +
		        "V03,80,N,\"\"\n" +
		        "V04,80,N,\"\"\n" +
		        "V05,30,N,\"\"\n" +
		        "V06,30,N,\"\"\n" +
		        "V07,32,N,\"\"\n" +
		        "V08,20,N,\"\"\n" +
		        "V09,20,N,\"\"\n" +
		        "V10,5,N,\"\"\n" +
		        "V11,5,N,\"\"\n" +
		        "V12,5,N,\"\"\n" +
		        "V13,5,N,\"\"\n" +
				"R0,0\n" +   // Set Reference Point                                                             
		        "ZB\n" + // Print direction (from Bottom of buffer)
		        "Q150,21\n" +  // Set label Length and gap
		        "A365,13,0,1,1,1,N,V00\n" +
		        "A385,38,0,1,1,1,N,V01\n" +
		        "A295,45,0,1,2,2,N,V10\n" +
		        "A405,63,0,1,1,1,N,V02\n" +
		        "A435,88,0,1,1,1,N,\"Material: \"V03\n" +
		        "A450,113,0,1,1,1,N,\"Strain: \"V05\n" +
		        "A375,135,0,1,2,2,N,V11\n" +
		        "A220,135,0,1,2,2,N,V12\n" +
		        "A470,138,0,1,1,1,N,\"Sample ID: \"V04\n";
	    	if (((label.getTrbNum() != null && !label.getTrbNum().isEmpty()) && (label.getTrbPage() != null && !label.getTrbPage().isEmpty())))
	    		form += "A450,163,0,1,1,1,N,\"Trb: \"V06\n";
	    	else
	    		form += "A450,163,0,1,1,1,N,\"Trb: \"\n";
	        form += "A435,188,0,1,1,1,N,\"Custodian: \"V07\n" +
	        	"A"+pos+",208,0,1,2,2,N,V13\n" +
		        "A405,213,0,1,1,1,N,\"Create Date: \"V08\n" +
		        "B870,113,0,1A,2,7,100,B,V09\n" +    
		        "FE\n" + NEW_LINE;
	    }
	    return form;
    }

    /**
     * Public method to create a label.
     * 
     * @return <String> commands to print label
     */
    public String createLabel(String group) {
    	return createLabel(label, group);
    }
    
    private int getSplitIndex(String text) {
    	int div=0;
    	String punct = ",.!?; \" ') ";
    	if (text.length()<MAX_PER_LINE) return -1;
    	int start=START_POSITION;
    	int which = 0;
    	while (div < 1 && which+1 < punct.length()) {
    		String ch = punct.substring(which,++which);
    		div = text.indexOf(ch, start);
    		if (div > 0) {
    			//log.info("found punct=<"+ch+"> at "+div);
    			if (div > MAX_PER_LINE) 
    				div = -1;
    		}
    	}
    	if (div < 1)
    		div = MAX_PER_LINE;
    	return div;
    }
    /**
     * Public method to create a label based on the input.
     * 
     * @param label - <LabelDTO> label to print
     * @return <String> commands to print label.
     */
    public String createLabel(LabelDTO label, String group) {
	    int trbNumInt = -1;
	    int trbPageInt = -1;
	    log.info("group="+group);
    	//if (!group.equals("SABC")&&!group.equals(JUNIT_GROUP)) {
		    try {
		    	trbNumInt = Integer.parseInt(label.getTrbNum());
		    } catch (NumberFormatException nfe) {
		    	log.warning("Problem parsing TRB number.");
		    }
		    
		    try {
		    	trbPageInt = Integer.parseInt(label.getTrbPage());
		    } catch (NumberFormatException nfe) {
		    	log.warning("Problem parsing TRB page.");
		    }
    	//}
	    DecimalFormat numFormat = new DecimalFormat();
	    numFormat.applyPattern(TRB_NUMBER_FORMAT);
	    
	    DecimalFormat pageFormat = new DecimalFormat();
	    pageFormat.applyPattern(TRB_PAGE_NUMBER_FORMAT);
	    
	    // Prepare string to send to the printer
	    //String printed = label.isPrinted() ? "Duplicate" : "Original";
	    String descriptor1 = "";
	    String descriptor2 = "";
	    String descriptor3 = "";
	    String actual = label.getDescription();
	    if (actual != null && !actual.isEmpty()) {
	    	int index = getSplitIndex(actual);
	    	if (index < 1 || actual.length() < index+1) {
	    		descriptor1 = actual;
	    	} else {
	    		descriptor1 = actual.substring(0,index+1);
		    	descriptor2 = actual.substring(index+1);
		    	if (descriptor2.length()>MAX_PER_LINE) {
		    		int index1 = getSplitIndex(descriptor2);
			    	if (index1 > 1) {
				    	descriptor3 = descriptor2.substring(index1+1);
			    		descriptor2 = descriptor2.substring(0,index1+1);
			    	}
		    	}
		    	if (descriptor2 != null && !descriptor2.isEmpty() && !descriptor2.substring(0,1).equals(" "))
		    		descriptor2 = descriptor2.substring(1);
		    	if (descriptor3 != null && !descriptor3.isEmpty() && !descriptor3.substring(0,1).equals(" "))
		    		descriptor3 = descriptor3.substring(1);
	    	}
	    }
	    String material = "";
	    if (!group.equals("SABC")&&!group.equals(JUNIT_GROUP)) {
	        if (label.getFeedstock() != null && !label.getFeedstock().isEmpty() ) {
	        	material += label.getFeedstock()+",";
	        }
	        if (label.getTreatment() != null && !label.getTreatment().isEmpty()) {
	        	material += label.getTreatment()+",";
	        }
	        if (label.getFraction() != null && !label.getFraction().isEmpty()) {
	        	material += label.getFraction();
	        }
	    } else {
	    	material += "biomass";
	        if (label.getForm() != null && !label.getForm().isEmpty() && !label.getForm().equals(" ") && !label.getForm().equals("null")) {
	        	material += ","+label.getForm();
	        } else {
	        	material += ","+"---";
	        }
	        if (label.getFraction() != null && !label.getFraction().isEmpty() && !label.getFraction().equals(" ") && !label.getFraction().equals("null")) {
	        	material += ","+label.getFraction();
	        } else {
	        	material += ","+"---";
	        }
	        if (label.getComposition() != null && !label.getComposition().isEmpty() && !label.getComposition().equals(" ") && !label.getComposition().equals("null")) {
	        	material += ","+label.getComposition();
	        } else {
	        	material += ","+"---";
	        }
	    }
	    
	    String fire = label.getFire() != null && !label.getFire().equals("null") ? label.getFire() : "";
	    String react = label.getReactivity() != null && !label.getReactivity().equals("null") ? label.getReactivity() : "";
	    String health = label.getHealth() != null && !label.getHealth().equals("null") ? label.getHealth() : "";
	    String specific = label.getSpecific() != null && !label.getSpecific().equals("null") ? label.getSpecific() : "";
	    String s =      
	    	    //"N\n" +         // Clear Image Buffer  
                "FR\"FORM1\"\n" +
                "?\n" +
                descriptor1 + NEW_LINE +
                descriptor2 + NEW_LINE +
                descriptor3 + NEW_LINE +
                material + NEW_LINE +
                label.getSampleId() + NEW_LINE;
        if (!group.equals("SABC")&&!group.equals(JUNIT_GROUP)) {
        		s += numFormat.format(trbNumInt) + "-" + pageFormat.format(trbPageInt)+NEW_LINE+
                label.getEntryDate() + NEW_LINE +
                label.getOwnerName() + NEW_LINE +
                label.getTrackingId() + NEW_LINE;                          
        } else {
        	String strain = "";
        	if (label.getStrain() != null && !label.getStrain().isEmpty() && !label.getStrain().equals("null") && !label.getStrain().equals(" ")) 
        		strain = label.getStrain();
        	else
        		strain = "  ";
        	String custodian = "";
        	if (label.getCustodian() != null && !label.getCustodian().isEmpty() && !label.getCustodian().equals("null") && !label.getCustodian().equals(" ")) 
        		custodian = label.getCustodian();
        	else
        		custodian = "  ";
        	String destination = "";
        	if (trbNumInt > 0 && trbPageInt > 0)
                destination = numFormat.format(trbNumInt) + "-" + pageFormat.format(trbPageInt);
        	String date = "";
        	if (label.getEntryDate() != null) date = label.getEntryDate();
        	s += strain + NEW_LINE +
        		 destination + NEW_LINE +
        		 custodian + NEW_LINE +
                 date + NEW_LINE +    
                 label.getTrackingId() + NEW_LINE;
        }
        s += fire + NEW_LINE +                            
        react + NEW_LINE +                            
        health + NEW_LINE +                            
        specific + NEW_LINE +                            
        "P1\n" +   // Print content of buffer, 1 label
		"N\n";         // Clear Image Buffer 
	    //log.info("label=\n"+s);
	    return s;
    }
    
    /**
     * Public method to actually print a label.
     * 
     * @param printer - <String> printer name
     * @return boolean - whether label printed or not
     */
    public boolean printLabel(String printer, String group) {
    	return printLabel(label,printer, group);
    }
 
    /**
     * Public method to send printing information to workstation where printer
     *  is connected.
     *  
     * @param filename - <String> file to print
     * @param printer - <String> printer name
     * @return boolean - whether function was successful
     * @throws <Exception>
     */
    public boolean printFile(String filename, String printer) throws Exception {
    	log.info("in printFile to printer="+printer);
    	boolean worked = true;
    	String command = "";
    	String os = System.getenv("os");
    	if (os != null && os.toLowerCase().indexOf(WINDOWS) != -1)
    		command = WINDOWS_PRINT+printer+" "+filename;
    	else {
    		//  old command: lp -dprinter filename
    		//command = UNIX_LP+printer+" "+filename;
    		//  new commend: lpr -Pprinter -l filename
    		String remotePrinter = getInfo(printer,REMOTEPRINTER);
    		String machine = getInfo(printer,MACHINE);
    		command = UNIX_LPR+remotePrinter+" -H"+machine+" -l "+filename;
    	}
    	log.info("command = "+command);
    	Process p = null;
    	try {
    		p = Runtime.getRuntime().exec(command);
    	} catch (Exception e) {}
    	//p.waitFor();
    	int ret = -1;
    	//while (ret == -1) {
    		try {
	        	ret = p.exitValue();
	        	//log.info("command returned "+ret);
	    		File f = new File(filename);
	    		if (f.exists()) {
	    			//boolean b = 
	    			FileUtils.deleteQuietly(f);
	    			//log.info("delete returned "+b);
	    		}
    		} catch (Exception e1) {
    			//log.warning("Exception caught deleting file: "+e1);
    		}
    	//}
    	return worked;
    }
    
    /**
     * Public method to create file containing printer commands.
     * 
     * @param label - <String> printer commands
     * @return <String> file name
     * @throws <Exception>
     */
    public String createFile(String label) throws Exception {
    	Date date = new Date();
    	long time = date.getTime();
    	String filename = File.separator + "tmp" + File.separator + "label" + time + ".txt";
    	//log.info("filename = "+filename);
    	File f = new File(filename);
    	if (!f.exists()) {
    		PrintWriter out
    		   = new PrintWriter(new BufferedWriter(new FileWriter(f)));
    		//log.info("label="+label);
    		out.write(label);
    		out.close();
    		f.setWritable(true);
    		f.setReadable(true);
    		f.setExecutable(true);
    	}
    	else throw new Exception("file not unique");
    	return filename;
    }
    
    /**
     * Public method to create file containing printer commands.
     * 
     * @return <String> file name
     * @throws <Exception>
     */
    public String createFile() throws Exception {
    	Date date = new Date();
    	long time = date.getTime();
    	ResourceBundle rBundle = ResourceBundle.getBundle(TRACKER_PROPERTIES_FILE_NAME);
    	String tmp = rBundle.getString(TEMP_DIR);
    	String filename = tmp + "rlpq" + time + ".txt";
    	//log.info("filename = "+filename);
    	File f = new File(filename);
    	if (f.exists())
    		throw new Exception("file not unique");
    	return filename;
    }
    
    private String getInfo(String printer, int which) {
		//log.info("in getInfo with "+printer);
		BufferedReader bis = null;
		String machine = "";
		String remotePrinter = "";
		try {
	    	File file = new File(PRINTCAP);
	    	FileInputStream fis = new FileInputStream(file);
	        bis = new BufferedReader(new InputStreamReader(fis));
	        while (bis.ready()) {
	        	String line = bis.readLine();
	        	//log.info("line="+line);
	        	if (line.startsWith("lp|")) {
	        		int pipe = line.indexOf("|");
	        		if (pipe != -1) {
	        			String ptr = line.substring(pipe+1);
	        			int colon = ptr.indexOf(":");
	        			if (colon != -1) {
	        				ptr = ptr.substring(0,colon);
	    		        	//log.info("ptr="+ptr);
	        				if (ptr.equals(printer)) {
	        					//log.info("found "+printer);
	        				    int sd = -1;
	        					while (sd == -1) {
	        						line = bis.readLine();
	        			        	//log.info("line="+line);
	        			        	int rm = line.indexOf(":rm=");
	        			        	int rp = line.indexOf(":rp=");
	        						if (rm != -1) {
	        							machine = line.substring(rm+4);
	        							int mcolon = machine.indexOf(":");
	        							if (mcolon != -1)
	        								machine = machine.substring(0,mcolon);
	        							//log.info("machine="+machine);
	        						} else if (rp != -1) {
	        							remotePrinter = line.substring(rp+4);
	        							int rcolon = remotePrinter.indexOf(":");
	        							if (rcolon != -1)
	        								remotePrinter = remotePrinter.substring(0,rcolon);	        							
	        							//log.info("remotePrinter="+remotePrinter);
	        						}
	        						sd = line.indexOf(":sd=");
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
		} catch (IOException ioe) {
			log.warning("problem reading "+PRINTCAP+" file: "+ioe);
		} finally {
			try {
		        if (bis != null)
		        	bis.close(); 
			} catch (IOException ioe1) {
	    		log.warning("problem closing "+PRINTCAP+" file: "+ioe1);
			}
		}
		if (which==REMOTEPRINTER)
			return remotePrinter;
		return machine;
	}

	private boolean testPrinter(String printer) {
		//String remotePrinter = getInfo(printer,REMOTEPRINTER);
		//String machine = getInfo(printer,MACHINE);
    	//log.info("in testPrinter with "+remotePrinter+" and machine="+machine);
    	boolean ok = false;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			PrintersDAOHibernate sdh = new PrintersDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			ok = sdh.getStatusByName(printer);
			
			tx.commit();
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        	return ok;
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
    		return ok;
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	return ok;
    }
    /**
     * Public method to get a list of available label printers.
     * 
     * @return <ArrayList<String>> - list of printer names
     */
    public ArrayList<String> listPrinters() {
    	ArrayList<String> printers = new ArrayList<String>();
    	BufferedReader bis = null;
	    try {
	    	String os = System.getenv("os");
	    	//log.info("os="+os);
	    	if (os != null && os.toLowerCase().indexOf(WINDOWS) != -1) {
		        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
	            for (int i=0;i<services.length;i++) {
	                PrintServiceAttribute attr =
	                    services[i].getAttribute(PrinterName.class);
	                String name = ((PrinterName)attr).getValue();
	                PrintServiceAttribute psattrib = services[i].getAttribute(PrinterIsAcceptingJobs.class);
	                int accepting = ((PrinterIsAcceptingJobs)psattrib).getValue();
		            if (accepting == 1)
		            	printers.add(name);
	            }
            } else {
		    	File file = new File(PRINTCAP);
		    	FileInputStream fis = new FileInputStream(file);
		        bis = new BufferedReader(new InputStreamReader(fis));
		        //log.info("opened "+PRINTCAP);
		        while (bis.ready()) {
		        	String line = bis.readLine();
		        	//log.info("line="+line);
		        	if (line.startsWith("lp|")) {
		        		int pipe = line.indexOf("|");
		        		if (pipe != -1) {
		        			String ptr = line.substring(pipe+1);
		        			int colon = ptr.indexOf(":");
		        			if (colon != -1) {
		        				ptr = ptr.substring(0,colon);
		        				if (!printers.contains(ptr)) {
		        					//log.info("got printer="+ptr);
		        					if (ptr.startsWith("157") || testPrinter(ptr)) {
		        						//log.info("adding "+ptr);
		        						printers.add(ptr);
		        					} //else
		        						//log.info(ptr + " failed test");
		        				}
		        			}
		        		}
		        	}
		        }
		        bis.close();
            }
		} catch (IOException e) {
			log.severe("IOException caught reading "+PRINTCAP+" file: "+e);
	    } catch (Exception e) {
	        log.warning(TrackerServiceImpl.getStackTrace(e));
	    } finally {
	    	try {
	    		if (bis != null)
	    			bis.close();
	    	} catch (IOException ioe) {
	    		log.warning("problems closing "+PRINTCAP+": "+ioe);
	    	}
	    }
	    return printers;
    }
    
    /**
     * Public method to compare two printer names for equality
     * 
     * @param p1 <String> printer 1 name
     * @param p2 <String> printer 2 name
     * @return boolean - where printer names are equal or not.
     */
    public boolean comparePrinters(String p1, String p2) {
    	if (p1.equals(p2)) return true;
    	String p1Node = "";
    	String p2Node = "";
    	String p1Printer = "";
    	String p2Printer = "";
    	int slash = 0;
    	while (p1.charAt(slash) == '\\') {
    		slash++;
    	}
    	p1Node = p1.substring(slash);
    	slash = p1Node.indexOf("\\");
    	if (slash > -1)
    		p1Printer = p1Node.substring(slash+1);
    	else {
    		slash = p1Node.indexOf("_");
    		p1Printer = p1Node.substring(slash+1);
    	}
    	if (slash < p1Node.length() && slash != -1)
    		p1Node = p1Node.substring(0,slash);
    	
    	slash = 0;
    	while (p2.charAt(slash) == '\\') {
    		slash++;
    	}
    	p2Node = p2.substring(slash);
    	slash = p2Node.indexOf("\\");
    	if (slash > -1)
    		p2Printer = p2Node.substring(slash+1);
    	else {
    		slash = p2Node.indexOf("_");
    		p2Printer = p2Node.substring(slash+1);
    	}
    	p2Node = p2Node.substring(0,slash);
    	
    	return (p1Node.equals(p2Node) && p1Printer.equals(p2Printer)) ? true : false;
    	
    }
    
    /**
     * Public method to print label to Zebra printer
     * 
     * @param label - <LabelDTO> label information to print
     * @param printer - <String> name of printer
     * @return boolean - whether label printed or not.
     */
    public boolean printLabel(LabelDTO label, String printer, String group) {
    	boolean worked = false;
	
	    // Search for an installed zebra printer...
	    // is a printer with "zebra" in its name
        PrintService psZebra = null;
        String sPrinterName = printer;
	    try {
	    	String s = "";
	    	int len = 0;
	    	if (label != null) {
	    		if (label.getSpecific() != null && !label.getSpecific().isEmpty())
	    			len = label.getSpecific().length();
	    	}
	    	if (!label.isPrinted())
	    		s += createForm(len, group, label);
	    	else
	    		s += createDuplicateForm(len, group, label);
	    	s+=createLabel(label, group);
	    	//s = null;
	    	if (s != null) {
		    	String os = System.getenv("os");
		    	if (os != null && os.toLowerCase().indexOf(WINDOWS) != -1) {
			    	if (printer != null && !printer.isEmpty()) {
				        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
				        for (int i = 0; i < services.length; i++) {
				        	//log.info("printer: " + i + " = " + services[i].getName());
				        	if (comparePrinters(services[i].getName(),printer)) {
					        	sPrinterName = services[i].getName();
					            psZebra = services[i];
					            break;
				        	}
				        }
			    	}
		    		if (psZebra == null) {
		    			log.info("SimpleDoc Zebra printer is not found.");
		    		} else {
		    			//log.info("Found printer: " + sPrinterName);
		    			DocPrintJob job = psZebra.createPrintJob();
		    			byte[] by = s.getBytes();
		    			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		    			SimpleDoc doc = new SimpleDoc(by, flavor, null);
		    			//log.info("printing label");
		    			job.print(doc, null);
		    			//log.info("done printing label");
		    			worked = true;
		    		}
		    	} else {
		    		String filename = createFile(s);
		    		//log.info("trying to print file="+filename);
	    			log.info("trying to print to "+sPrinterName);
		    		if (sPrinterName != null && !sPrinterName.isEmpty())
		    			worked = testPrinter(sPrinterName);
		    		if (worked)
		    			worked = printFile(filename,sPrinterName);
		    		else {
		    			log.info("printer "+sPrinterName+" failed test. Queuing to default (15747s_18200s)");
		    			if (sPrinterName == null || sPrinterName.isEmpty()) 
		    				sPrinterName = "15747s_18200s";
		    			worked = printFile(filename,sPrinterName);
		    		}
		    		//log.info("printFile returned "+worked);
		    	}
	    	} else {
	    		log.info("label was null");
	    	}
	    } catch (Exception e) {
	    	worked = false;
	    	log.warning("exception caught: "+e);
	    	log.warning(getStackTrace(e));
	        //e.printStackTrace();
	    }
	    //log.info("printLabel returning "+worked);
	    return worked;
	}
    
    /**
     * Public static method to get a stack trace.
     * 
     * @param t
     * @return <String> stack trace
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
