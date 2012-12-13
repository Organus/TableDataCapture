package gov.nrel.nbc.spreadsheet.test;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import junit.framework.Test;
import junit.framework.TestSuite;
///*
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.SeleniumException;

public class SeleniumSearchTest extends SeleneseTestCase implements AppConstants {
	ClassLoader thisLoader;
	public void setUp() throws Exception {
    	//thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
        //setUp("http://www.google.com/", "*firefox");
		//selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://*:8880/");
		//selenium.start();
		//selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://*:8880/");
		setUp("http://*:8880/", "*chrome");
	}

	public static Test suite() {
		return new TestSuite(SeleniumSearchTest.class);
	}
	
	public void testSeleniumSearch() throws Exception {
		selenium.setSpeed("2000");
		//selenium.open("file:///C:/Documents%20and%20Settings/James Albersheim.NREL_NT/My%20Documents/bookmark.htm");
		selenium.open("http://*:8880/spreadsheet/#initState");
		try {
			selenium.waitForPageToLoad("10000");
		} catch (SeleniumException se) {}
		try {
			//selenium.click("link=Spreadsheet Ingest");
			selenium.click("link=Search Spreadsheets");
			try {
				selenium.waitForPageToLoad("2000");
			} catch (SeleniumException se) {}
			//selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[2]/td/table/tbody/tr/td[2]/select", "label=TestConfig");
			//selenium.click("//option[@value='TestConfig']");
			SpreadSheetServiceImpl ssImpl = new SpreadSheetServiceImpl();
			List<NameValue> headers = ssImpl.getHeaders("TestConfig", "Digestion");
			Iterator<NameValue> hit = headers.iterator();
			while (hit.hasNext()) {
				NameValue nv = hit.next();
				String header = nv.getName();
				selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[1]/select", "label="+header);
				selenium.click("//option[@value='"+header+"']");
				List<String> operators = ssImpl.getOperators("TestConfig", header);
 				//Iterator<String> oit = operators.iterator();
				//while (oit.hasNext()) {
					String operator = operators.get(0);//oit.next();
					selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[2]/select", "label="+operator);
			    	List<String> values = ssImpl.getStringValues("TestConfig", header);
			    	String value = values.get(0);
				    long type = nv.getDataType();
				    if (type == LONG) {
						selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[3]/input", value);
				    }
				    else if (type == REAL) {
						selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[3]/input", value);
				    }
				    else if (type == STRING) {
						selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[3]/input", value);
				    }
				    else if (type == DATE) {
				    	Calendar cal = Calendar.getInstance();
				    	int day = cal.get(Calendar.DAY_OF_MONTH);
				    	day -= 7;
				    	cal.set(Calendar.DAY_OF_MONTH, day); // subtract week
				    	String date = String.valueOf(cal.get(Calendar.MONTH)+1)+"/"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(cal.get(Calendar.YEAR));
				    	value = date;
				    	System.out.println("date="+date);
						selenium.click("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[3]/input");
						selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td[3]/input", date);			    	
				    }
					//selenium.click("//tr[2]/td/table/tbody/tr[5]/td[2]");
					selenium.click("//button[@type='button']");
					try {
						selenium.waitForPageToLoad("30000");
					} catch (SeleniumException se) {}
					if (selenium.isAlertPresent()) {
						assertEquals("Your criteria returned no data.", selenium.getAlert());
						System.out.println("FAILED for "+header+";"+operator+";"+value);						
					} else {
						boolean ret = selenium.isTextPresent("Count");
				        //assertTrue(ret);
						if (!ret) {
							System.out.println("FAILED for "+header+";"+operator+";"+value);
						} else {
							if (selenium.isTextPresent("Select")) {
								try {
									try {
										selenium.click("gwt-uid-3");
										selenium.waitForPageToLoad("10000");
									} catch (SeleniumException se) {}
									ret = selenium.isTextPresent("Project");
									if (!ret)
										System.out.println("FAILED for "+header+";"+operator+";"+value);
									else {
										ret = selenium.isTextPresent(value);
										if (!ret)
											System.out.println("FAILED for "+header+";"+operator+";"+value);
									}
									try {
										selenium.click("gwt-uid-4");
										selenium.waitForPageToLoad("10000");
									} catch (SeleniumException se) {}
									ret = selenium.isTextPresent("Project");
									if (!ret)
										System.out.println("FAILED for "+header+";"+operator+";"+value);
									else {
										ret = selenium.isTextPresent(value);
										if (!ret)
											System.out.println("FAILED for "+header+";"+operator+";"+value);
									}
									try {
										selenium.click("gwt-uid-5");
										selenium.waitForPageToLoad("10000");
									} catch (SeleniumException se) {}
									ret = selenium.isTextPresent("Project");
									if (!ret)
										System.out.println("FAILED for "+header+";"+operator+";"+value);
									else {
										ret = selenium.isTextPresent(value);
										if (!ret)
											System.out.println("FAILED for "+header+";"+operator+";"+value);
									}
									try {
										selenium.click("gwt-uid-6");
										selenium.waitForPageToLoad("10000");
									} catch (SeleniumException se) {}
									ret = selenium.isTextPresent("Project");
									if (!ret)
										System.out.println("FAILED for "+header+";"+operator+";"+value);
									else {
										ret = selenium.isTextPresent(value);
										if (!ret)
											System.out.println("FAILED for "+header+";"+operator+";"+value);
									}
									try {
										selenium.click("gwt-uid-7");
										selenium.waitForPageToLoad("10000");
									} catch (SeleniumException se) {}
									ret = selenium.isTextPresent("Project");
									if (!ret)
										System.out.println("FAILED for "+header+";"+operator+";"+value);
									else {
										ret = selenium.isTextPresent(value);
										if (!ret)
											System.out.println("FAILED for "+header+";"+operator+";"+value);
									}
								} catch (SeleniumException se1) {
									System.out.println(se1.getMessage());
									se1.printStackTrace();
								}
							}
						}
					}
					selenium.click("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[7]/td/table/tbody/tr/td[2]/button");
				//}
			}
		} catch (SeleniumException se) {
			System.out.println(se.getMessage());
			se.printStackTrace();
		}
	}
}
//*/


/*
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[3]/td[1]/select", "label=Long");
selenium.click("//option[@value='Long']");
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[3]/td[2]/select", "label==");
selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[3]/td[3]/input", "1");
selenium.click("//button[@type='button']");
try {
	selenium.waitForPageToLoad("10000");
} catch (SeleniumException se) {}
ret = selenium.isTextPresent("Count");
assertTrue(ret);
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[4]/td[1]/select", "label=Material");
selenium.click("//option[@value='Material']");
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[4]/td[2]/select", "label==");
selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[4]/td[3]/input", "A*");
selenium.click("//button[@type='button']");
try {
	selenium.waitForPageToLoad("10000");
} catch (SeleniumException se) {}
ret = selenium.isTextPresent("Count");
assertTrue(ret);
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[5]/td[1]/select", "label=S % Non-structural Inorganics");
selenium.click("//option[@value='S % Non-structural Inorganics']");
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[5]/td[2]/select", "label=>");
selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[5]/td[3]/input", ".3");
selenium.click("//button[@type='button']");
try {
	selenium.waitForPageToLoad("10000");
} catch (SeleniumException se) {}
ret = selenium.isTextPresent("Count");
assertTrue(ret);
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[6]/td[1]/select", "label=S % Total Extractives");
selenium.click("//option[@value='S % Total Extractives']");
selenium.select("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[6]/td[2]/select", "label=>");
selenium.type("//td[@id='sheetContainer']/div/div/div[3]/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr[6]/td/table/tbody/tr[6]/td[3]/input", "5");
selenium.click("//button[@type='button']");
try {
	selenium.waitForPageToLoad("10000");
} catch (SeleniumException se) {}
ret = selenium.isTextPresent("Count");
assertTrue(ret);
 */

