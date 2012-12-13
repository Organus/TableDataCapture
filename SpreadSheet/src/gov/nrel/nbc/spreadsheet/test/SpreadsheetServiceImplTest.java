package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.client.FileInfo;
import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SpreadsheetServiceImplTest extends TestCase implements AppConstants
{
	ClassLoader thisLoader;
	private SpreadSheetServiceImpl ssImpl=new SpreadSheetServiceImpl();
	
	//@Override
	protected void setUp() {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
    	//ssImpl = new SubmissionServiceImpl();
	}
	
	public static Test suite() {
		return new TestSuite(SpreadsheetServiceImplTest.class);
	}
	
	private NameValue getHeaders(boolean show) throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = (List<NameValue>)ssImpl.getHeaders("TestConfig","Digestion");
		assertNotNull(list);
		assertTrue(list.size()>0);
		if (show) System.out.println("got "+list.size()+" headers");
		NameValue header = list.get(26);
		if (show) System.out.println("got header="+header.getName());
		return header;
	}

	private List<NameValue> getAllHeaders() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = (List<NameValue>)ssImpl.getHeaders("TestConfig","Digestion");
		assertNotNull(list);
		assertTrue(list.size()>0);
		return list;
	}

	public void testGetType() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		NameValue header = getHeaders(true);
		System.out.println("using header="+header.getName());
		String type = ssImpl.getType(header.getName());
		assertTrue(type.length()>0);
		System.out.println("got type=<"+type+">");
	}

	public void testGetOperators() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		NameValue header = getHeaders(false);
		System.out.println("using header="+header.getName());
		List<String> list = (List<String>)ssImpl.getOperators("TestConfig",header.getName());
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" operators");
		String operator = list.get(0);
		System.out.println("got operator=<"+operator+">");
	}

	public void testGetMetadata() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		NameValue header = getHeaders(false);
		System.out.println("using header="+header.getName());
		List<NameValue> list = (List<NameValue>)ssImpl.getMetaData(74);
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" metadata");
	}

	public void testGetAttachmentExtensions() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<String> extensions = ssImpl.getAttachmentExtensions();
		assertNotNull(extensions);
		assertTrue(extensions.size()>0);
		System.out.println("got "+extensions.size()+" extensions");
	}
	
	public void testGetSheetConfigs() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<String> list = ssImpl.getSheetConfigs("TestConfig");
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" configs");
	}
	
	public void testGetMetadataHeaders() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = ssImpl.getMetaDataHeaders("TestConfig");
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" metadatas");
	}
	
	public void testGetMetadataHeadersType() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = ssImpl.getMetaDataHeaders("TestConfig",EXTERNAL);
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" metadatas");
	}
	
	public void testGetUrls() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> list = ssImpl.getUrls();
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" urls");
	}
	
	public void testGetValues() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		//NameValue header = getHeaders(true);
		//System.out.println("using header="+header);
		List<String> list = (List<String>)ssImpl.getStringValues("TestConfig","Submitter");
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" values");
	}
	
	public void testGetAttachments() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<FileInfo> list = (List<FileInfo>)ssImpl.getAttachments(74);
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" attachments");
	}
	
	public void testGetWorkbookConfigs() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<String> list = (List<String>)ssImpl.getWorkbookConfigs();
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.size()+" configs");
	}
	
	public void testGetWorkbookConfigName() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		String name = ssImpl.getWorkbookConfigName(5);
		assertNotNull(name);
		assertTrue(name.length()>0);
		System.out.println("got "+name);
	}
		
	public void testGetTitle() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		String name = ssImpl.getTitle();
		assertNotNull(name);
		assertTrue(name.length()>0);
		System.out.println("got "+name);
	}
		
	public void testGetFileNameAndPath() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<String> list = (List<String>)ssImpl.getFileNameAndPath("74");
		assertNotNull(list);
		assertTrue(list.size()>0);
		System.out.println("got "+list.get(0)+" and "+list.get(1));
	}
		
	public void testSearchSpreadsheet() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<NameValue> headers = getAllHeaders();
		Iterator<NameValue> ith = headers.iterator();
		while (ith.hasNext()) {
			NameValue header = ith.next();
			//NameValue header = new NameValue();
			//header.setName(WORKBOOK_ID);
			System.out.println("header="+header.getName());
			String type = ssImpl.getType(header.getName());
			if (type.equals("STRING")) {
				List<String> list = (List<String>)ssImpl.getStringValues("TestConfig",header.getName());
				assertNotNull(list);
				if (list.size()>0) {
					//assertTrue(list.size()>0);
					String value = "";
					long lvalue = 0;
					boolean isLong = false;
					//boolean isMetaData = true;
					//GenericData genData = null;
					Iterator<String> it = list.iterator();
					while (it.hasNext()) {
						//if (isMetaData) {
							if (header.getName().endsWith("ID") && !header.getName().startsWith("Feed")) {
								try {
									isLong = true;
									lvalue = Long.parseLong(it.next());
									//if (header.equals("Sample ID"))
									//	isMetaData = false;
								} catch (NumberFormatException nfe) {
									System.out.println("problem parsing value: "+nfe);
								}
							} else {
								value = it.next();
								isLong = false;
							}
						//}
						list.get(0);
						list = ssImpl.getOperators("TestConfig",header.getName());
						assertTrue(list.size()>0);
						String operator = list.get(0);
						CriteriaTrioDTO trio = new CriteriaTrioDTO();
						System.out.println("testing searching header="+header.getSynonym());
						trio.setHeader(header.getName());
						trio.setOperator(operator);
						if (isLong)
							trio.setValue(String.valueOf(lvalue));
						else
							trio.setValue(value);
						List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
						trios.add(trio);
						List<List<String>> rows = ssImpl.getWorkbookCounts("TestConfig",null,trios,0,5);
						System.out.println("trio has "+header.getSynonym()+","+operator+","+trio.getValue());
						//assertTrue(rows.size()>0);
						if (rows != null && rows.size()>0) {
						List<String> row = rows.get(0);
						System.out.println("Select got "+rows.size()+" rows of data, each having "+row.size()+" columns");
						} else {
							System.err.println("Had problem with trio "+header.getSynonym()+","+operator+","+trio.getValue());
						}
					}
				} else {
					System.out.println("problem with header="+header);
				}
			} else if (type.equals("LONG")&&header.getName().equals(WORKBOOK_ID)) {
				CriteriaTrioDTO trio = new CriteriaTrioDTO();
				System.out.println("testing searching header="+header.getName());
				trio.setHeader(header.getName());
				trio.setOperator("=");
				trio.setValue("74");
				List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
				trios.add(trio);
				List<List<String>> rows = ssImpl.getWorkbookCounts("TestConfig",null,trios,0,5);
				System.out.println("trio has "+header.getName()+"="+trio.getValue());
				assertTrue(rows.size()>0);
				List<String> row = rows.get(0);
				System.out.println("Select got "+rows.size()+" rows of data, each having "+row.size()+" columns");
			}
		}
	}

	public void testGetMetaDataValues() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		List<String> values = ssImpl.getMetaDataValues("TestConfig", "Charge Number");
		assertNotNull(values);
		assertTrue(values.size()>0);
	}
	
	public void testPerformSelect() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		//List<NameValue> headers = getAllHeaders();
		//Iterator<NameValue> ith = headers.iterator();
		//int ctr=0;
		//while (ith.hasNext()&& ctr < 10) {
			//NameValue header = ith.next();
		    NameValue header = new NameValue();
		    header.setName(WORKBOOK_ID);
			System.out.println("header="+header.getName());
			String type = ssImpl.getType(header.getName());
			if (type.equals("STRING")) {
				List<String> list = (List<String>)ssImpl.getStringValues("TestConfig",header.getName());
				assertNotNull(list);
				if (list.size()>0) {
					//assertTrue(list.size()>0);
					String value = "";
					long lvalue = 0;
					boolean isLong = false;
					//boolean isMetaData = true;
					//GenericData genData = null;
					Iterator<String> it = list.iterator();
					while (it.hasNext()) {
						//if (isMetaData) {
							if (header.getName().endsWith("ID") && !header.getName().startsWith("Feed")) {
								try {
									isLong = true;
									lvalue = Long.parseLong(it.next());
									//if (header.equals("Sample ID"))
									//	isMetaData = false;
								} catch (NumberFormatException nfe) {
									System.out.println("problem parsing value: "+nfe);
								}
							} else {
								value = it.next();
								isLong = false;
							}
						//}
						list.get(0);
						list = ssImpl.getOperators("TestConfig",header.getName());
						assertTrue(list.size()>0);
						String operator = list.get(0);
						CriteriaTrioDTO trio = new CriteriaTrioDTO();
						System.out.println("testing searching header="+header);
						trio.setHeader(header.getName());
						trio.setOperator(operator);
						if (isLong)
							trio.setValue(String.valueOf(lvalue));
						else
							trio.setValue(value);
						List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
						trios.add(trio);
						List<List<String>> rows = ssImpl.performSelect("TestConfig","Digestion",trios,0,5);
						System.out.println("trio has "+header+operator+trio.getValue());
						//assertTrue(rows.size()>0);
						if (rows != null && rows.size()>0) {
							List<String> row = rows.get(0);
							System.out.println("Select got "+rows.size()+" rows of data, each having "+row.size()+" columns");
						}
	
					}
				} else {
					System.out.println("problem with header="+header);
				}
			}
			//ctr++;
		//}
	}
	
	public void testPages() throws Exception {
		int num = 86;
		int page = 50;
		int inum = new Integer(num/page);
		double div = (double)num/(double)page;
		if (div > inum)
			num = inum+1;
		else 
			num = inum;
		System.out.println("pages="+num);
		num = 86;
		div = Math.ceil((double)num/(double)page);
		num = (int)div;
		System.out.println("pages="+num);
		num = 971;
		page = 50; 
		inum = new Integer(num/page);
		div = (double)num/(double)page;
		if (div > inum)
			num = inum+1;
		else 
			num = inum;
		System.out.println("pages="+num);
		div = Math.floor(971.0/50.0);
		System.out.println("floor = "+div);
		div = Math.ceil(971.0/50.0);
		System.out.println("ceil="+div);
		div = Math.ceil(971/50);
		System.out.println("ceil="+div);
	}

	public void testDownloadSelect() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		//NameValue header = getHeaders(false);
		List<String> list = (List<String>)ssImpl.getStringValues("TestConfig","Sample ID");//header.getName());
		assertNotNull(list);
		assertTrue(list.size()>0);
		//String value = list.get(0);
		list = ssImpl.getOperators("TestConfig", "Sample ID");//header.getName());
		assertTrue(list.size()>0);
		//String operator = list.get(0);
		CriteriaTrioDTO trio = new CriteriaTrioDTO();
		System.out.println("header="+"Sample ID");//header.getName());
		trio.setHeader("Sample ID");
		trio.setOperator("=");
		trio.setValue("James Albersheim");
		List<CriteriaTrioDTO> trios = new ArrayList<CriteriaTrioDTO>();
		trios.add(trio);
		List<String> namepath = ssImpl.downloadSelect("TestConfig","Digestion", trios);
		if (namepath != null) {
			assertTrue(namepath.get(0).length()>0);
			System.out.println("got file="+namepath.get(0));
			File f = new File(namepath.get(1));
			assertTrue(f.exists());
			System.out.println("file exists!");
			assertTrue(f.length()>0);
			System.out.println("file has data!");
		}
	}
	
	public void testConstructWherePart() {
		SpreadSheetServiceImpl sssi = new SpreadSheetServiceImpl();
		String sql = "";
		// test date =, !=, >, <
		CriteriaTrioDTO trio = new CriteriaTrioDTO();
		trio.setHeader("Submission Date");
		trio.setOperator("=");
		trio.setValue("08/08/2011");
		String wbConfig = "liquor";
		String shConfig = "digestion";
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			sql = sssi.constructWherePart(sql, trio, wbConfig, shConfig, session);
			System.out.println("sql="+sql);
			sql = "";
			trio.setOperator("!=");
			sql = sssi.constructWherePart(sql, trio, wbConfig, shConfig, session);
			System.out.println("sql="+sql);
			sql = "";
			trio.setOperator(">");
			sql = sssi.constructWherePart(sql, trio, wbConfig, shConfig, session);
			System.out.println("sql="+sql);
			sql = "";
			trio.setOperator("<");
			sql = sssi.constructWherePart(sql, trio, wbConfig, shConfig, session);
			System.out.println("sql="+sql);
		} catch (HibernateException he) {
			System.out.println("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			System.out.println(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				System.out.println("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			System.out.println("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			System.out.println(stack);
		} finally {
			try {
			if (session != null && session.isConnected())
				if (session.isOpen()) {
					session.flush();
					session.close();
				}
			} catch (HibernateException rbEx) {
				System.out.println("Couldn't roll back transaction! Error: " + rbEx);
			}
		}			
	}

	/* changed testGetfile to only check for null value on invalid spreadsheet id. 
	 * Can't get valid values for spreadsheet id through getValues call.	
	 *
	 */
	public void testGetFileName() throws Exception {
		ssImpl = new SpreadSheetServiceImpl();
		String path = ssImpl.getFileName("79");
		assertNotNull(path);
	}
	
}
