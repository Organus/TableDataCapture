package gov.nrel.nbc.labelprinting.test;

import gov.nrel.nbc.labelprinting.server.PrintLabelService;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PrinterTest extends TestCase {

	ClassLoader thisLoader;

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}

	@Override
	protected void setUp() {
    	thisLoader = PrintLabelService.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(PrinterTest.class);
	}

	public void testComparePrinters() {
		PrintLabelService p = new PrintLabelService();
		boolean ret = p.comparePrinters("\\\\dcrocker-14415s\\Zebra  TLP3842", "\\\\dcrocker-14415s\\Zebra  TLP3842");
		assertTrue(ret);
		ret = p.comparePrinters("\\\\dcrocker-14415s\\Zebra_TLP3842", "\\\\dcrocker-14415s\\Zebra  TLP3842");
		assertFalse(ret);
	}
	
	public void testList() {
		PrintLabelService p = new PrintLabelService();
		ArrayList<String> printers = p.listPrinters();
		assertTrue(printers.size()>0);
		System.out.println("found "+printers.size()+" printers");
		for (int i=0;i<printers.size();i++)
			System.out.println("found printer="+printers.get(i));
	}

	public void testNoDupPrint() throws Exception {
		//assertTrue(false);
		PrintLabelService p = new PrintLabelService();
		p.initLabel();
		p.setTrbNum("3123");
		p.setTrbPage("12");
		p.setSampleId("sample123");
		p.setOwnerName("James Albersheim");
		p.setTrackingId("444444");
		p.setEntryDate("06/23/2999");
		p.setPrinted(false);
//		boolean ret = p.printLabel("\\\\bberry-14764s\\14764s_15747s");
//		boolean ret = p.printLabel("\\\\dcrocker-14415s\\14415s_15747s");
		boolean ret = true;
    	String os = System.getenv("os");
    	System.out.println("os="+os);
    	if (os != null && os.toLowerCase().indexOf("windows") != -1) {
       		//ret = p.printLabel("\\\\bberry-14764s\\14764s_15747s","NREL");
    		//ret = p.printLabel("\\\\dcrocker-14415s\\Zebra  TLP3842");
    		//ret = p.printLabel("\\\\ewolfrum-10389s\\Zebra  TLP3842");
    		ret = p.printLabel("\\\\LC16.nrel.gov\\Zebra  TLP3842","NREL");
    	} else {
    		ret = p.printLabel("LC16","NREL");
    		//ret = p.printLabel("14764s_15747s","NREL");
    		//ret = p.printLabel("14415s_15747s");
    		//ret = p.printLabel("10389s_15747s");
    	}
		assertTrue(ret);
	}
	public void testDupPrint() throws Exception {
		//assertTrue(false);
		PrintLabelService p = new PrintLabelService();
		p.initLabel();
		p.setTrbNum("3123");
		p.setTrbPage("12");
		p.setSampleId("sample123");
		p.setOwnerName("James Albersheim");
		p.setTrackingId("444444");
		p.setEntryDate("06/23/2999");
		p.setPrinted(true);
//		boolean ret = p.printLabel("\\\\bberry-14764s\\14764s_15747s");
//		boolean ret = p.printLabel("\\\\dcrocker-14415s\\14415s_15747s");
		boolean ret = true;
    	String os = System.getenv("os");
    	System.out.println("os="+os);
    	if (os != null && os.toLowerCase().indexOf("windows") != -1) {
       		ret = p.printLabel("\\\\bberry-14764s\\14764s_15747s","NREL");
    		//ret = p.printLabel("\\\\dcrocker-14415s\\Zebra  TLP3842");
    		//ret = p.printLabel("\\\\ewolfrum-10389s\\Zebra  TLP3842");
    	} else {
    		ret = p.printLabel("14764s_15747s","NREL");
    		//ret = p.printLabel("14415s_15747s");
    		//ret = p.printLabel("10389s_15747s");
    	}
		assertTrue(ret);
	}
	
}