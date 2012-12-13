package gov.nrel.nbc.spreadsheet.test;
 

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class SQLTest extends TestCase implements AppConstants{

	ClassLoader thisLoader;
	private SpreadSheetServiceImpl ssImpl=new SpreadSheetServiceImpl();
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(SQLTest.class);
	}
	/*
	private List<NameValue> getAllHeaders() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = (List<NameValue>)ssImpl.getHeaders("TestConfig","Digestion");
		assertNotNull(list);
		assertTrue(list.size()>0);
		return list;
	}
	*/
	public void testCombos() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
		CriteriaTrioDTO trio = new CriteriaTrioDTO();
		trio.setHeader("Material");
		trio.setOperator("=");
		trio.setValue("Lodge*");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("S % Ash");
		trio.setOperator("<");
		trio.setValue("0.5");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("S % Total Extractives");
		trio.setOperator("<");
		trio.setValue("5.0");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("S % Total Extractives");
		trio.setOperator(">");
		trio.setValue("3.0");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader(WORKBOOK_ID);
		trio.setOperator("=");
		trio.setValue("131");
		trio = new CriteriaTrioDTO();
		trio.setHeader(ATTACHMENT_EXT);
		trio.setOperator("=");
		trio.setValue("XLSX");		
		trios.add(trio);
		Iterator<CriteriaTrioDTO> trit = trios.iterator();
		while (trit.hasNext()) {
			trio = trit.next();
			System.out.println("trio has "+trio.getHeader()+" "+trio.getOperator()+" "+trio.getValue());
		}
		List<List<String>> rows = ssImpl.performSelect("TestConfig","Digestion",trios,0,5);
		System.out.println("returned "+rows.size()+" rows");
		assertTrue(rows.size()>0);
	}
	public void testDateRanges() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
		CriteriaTrioDTO trio = new CriteriaTrioDTO();
		trio.setHeader("Submission Date");
		trio.setOperator("=");
		trio.setValue("02/02/2011");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("Date Extracted");
		trio.setOperator("<");
		trio.setValue("11/17/2010");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("Date Extracted");
		trio.setOperator(">");
		trio.setValue("11/10/2010");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("Auto Clave");
		trio.setOperator("=");
		trio.setValue("Dishwash");		
		trios.add(trio);
		trio = new CriteriaTrioDTO();
		trio.setHeader("Date Hydrolyzed");
		trio.setOperator("=");
		trio.setValue("11/16/2010");		
		trios.add(trio);
		Iterator<CriteriaTrioDTO> trit = trios.iterator();
		while (trit.hasNext()) {
			trio = trit.next();
			System.out.println("trio has "+trio.getHeader()+" "+trio.getOperator()+" "+trio.getValue());
		}
		List<List<String>> rows = ssImpl.performSelect("TestConfig","Digestion",trios,0,5);
		System.out.println("returned "+rows.size()+" rows");
		assertTrue(rows.size()>0);
	}
	/*
	private void testPerformSelect() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> headers = getAllHeaders();
		System.out.println("there are "+headers.size()+" headers");
		Iterator<NameValue> ith = headers.iterator();
		while (ith.hasNext()) {
			NameValue header = ith.next();
			System.out.println("header="+header.getName());
			String type = ssImpl.getType(header.getName());
			List<String> list = (List<String>)ssImpl.getStringValues("TestConfig",header.getName(),type);
			assertNotNull(list);
			assertTrue(list.size()>0);
			if (list.size()>0) {
				String value = "";
				long lvalue = 0;
				boolean isLong = false;
				int ctr=0;
				String svalue = "";
				while (ctr < list.size() && svalue.length()==0) {
					svalue = list.get(ctr++);//it.next();
				}
				if (type.equals("DATE")) {
					SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date dvalue = null;
					try {
						dvalue = dFormat.parse(svalue);
					} catch (ParseException pe) {
						System.out.println("Invalid date format: "+svalue);
						dFormat = new SimpleDateFormat("MM/dd/yyyy");
						dvalue = null;
						try {
							dvalue = dFormat.parse(svalue);
						} catch (ParseException pe1) {
							System.out.println("Invalid date format: "+svalue);
						}
					}
					if (dvalue != null) {
						Date collectionTime = dvalue;
						GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
						cal.setTime(collectionTime);
						value = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
						System.out.println("date = "+value);
					} 
				} else {
					value = svalue;
				}
				if (value != null && value.length() > 0) {
					System.out.println("found value="+value);
					isLong = false;
					//list.get(0);
					list = ssImpl.getOperators("TestConfig",header.getName());
					assertTrue(list.size()>0);
					String operator = "=";//list.get(0);
					CriteriaTrioDTO trio = new CriteriaTrioDTO();
					trio.setHeader(header.getName());
					trio.setOperator(operator);
					if (isLong)
						trio.setValue(String.valueOf(lvalue));
					else
						trio.setValue(value);
					System.out.println("test searching header="+header.getName()+ " with value="+trio.getValue());
					List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
					trios.add(trio);
					List<List<String>> rows = ssImpl.performSelect("TestConfig","Digestion",trios);
					System.out.println("trio has "+header.getName()+operator+trio.getValue());
					System.out.println("returned "+rows.size()+" rows");
					assertTrue(rows.size()>0);
					if (rows != null && rows.size()>0) {
						List<String> row = rows.get(0);
						System.out.println("Select got "+rows.size()+" rows of data, each having "+row.size()+" columns");
					}
				}
			} else {
				System.out.println("problem with header="+header.getName());
			}
		}
	}
	*/
}
