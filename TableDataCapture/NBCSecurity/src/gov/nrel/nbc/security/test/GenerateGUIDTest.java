package gov.nrel.nbc.security.test;

import org.junit.After;
import org.junit.Before;

import gov.nrel.nbc.security.dbUtils.GenerateGUID;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GenerateGUIDTest extends TestCase {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = GenerateGUID.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(GenerateGUIDTest.class);
	}
	
	public void testGenerateGUID() throws Exception {
		SecurityServiceImpl susi = new SecurityServiceImpl();
		GenerateGUID genGUID = null;
		for (int i=0;i<10;i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {}
			String company = "Company#"+i;
			genGUID = new GenerateGUID(company);
			String guid = genGUID.generateGUID();
			System.out.println(company + " has guid "+guid);
			susi.addClient(company, guid);
		}
	}

	public void testAddCompany() throws Exception {
		SecurityServiceImpl susi = new SecurityServiceImpl();
		for (int i=10;i<20;i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {}
			String company = "Company#"+i;
			susi.addClient(company);
		}
	}

	public void testGetCompany() throws Exception {
		SecurityServiceImpl susi = new SecurityServiceImpl();
		String guid = susi.getGuidForClient("Company#1");
		String client = susi.getClientByGuid(guid);
		assertEquals(client,"Company#1");
	}
}
