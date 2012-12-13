package gov.nrel.nbc.security.test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List; 

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.server.NBCSecurityProxy;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import gov.nrel.nbc.tracker.client.SampleCriteria;
import gov.nrel.nbc.tracker.server.TrackerServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NBCSecurityProxyTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = NBCSecurityProxy.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(NBCSecurityProxyTest.class);
	}
	
	public void testFindMethod() throws Exception {
		NBCSecurityProxy proxy = new NBCSecurityProxy();
		int ret = proxy.findMethod("gov.nrel.nbc.tracker.server.TrackerServiceImpl", "getOrigins");
		assertTrue(ret > 0);
	}
	///*
	@SuppressWarnings("unchecked")
	public void testInvokeNoId() throws Exception {
		NBCSecurityProxy proxy = new NBCSecurityProxy();
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Object o = proxy.invoke(tsi,"gov.nrel.nbc.tracker.server.TrackerServiceImpl","getOrigins",null);
		assertNotNull(o);
		List<String> origins = (List<String>)o;
		Iterator<String> oit = origins.iterator();
		while (oit.hasNext()) {
			String origin = oit.next();
			System.out.println("got "+origin);
		}
	}
	@SuppressWarnings("unchecked")
	public void testInvokeWithId() throws Exception {
		NBCSecurityProxy proxy = new NBCSecurityProxy();
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Method m = proxy.getMethod("gov.nrel.nbc.tracker.server.TrackerServiceImpl","getOrigins");
		Object args[] = new Object[1];
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		args[0] = new String(id);
		Object o = proxy.invokeIt(m, args, tsi);
		assertNotNull(o);
		List<String> origins = (List<String>)o;
		Iterator<String> oit = origins.iterator();
		while (oit.hasNext()) {
			String origin = oit.next();
			System.out.println("got "+origin);
		}
	}

	@SuppressWarnings("unchecked")
	public void testInvokeWithIdWithArgs() throws Exception {
		NBCSecurityProxy proxy = new NBCSecurityProxy();
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria crit = new SampleCriteria();
		crit.setTrbPage(70);		
		Method m = proxy.getMethod("gov.nrel.nbc.tracker.server.TrackerServiceImpl","searchSamples");
		Object args[] = new Object[2];
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		System.setProperty("sessionId", String.valueOf(id));
		args[0] = new String(id);
		args[1] = crit;
		Object o = proxy.invokeIt(m, args, tsi);
		assertNotNull(o);
		Collection<SampleCriteria> samples = (Collection<SampleCriteria>)o;
		System.out.println("got "+samples.size()+" samples");
		Iterator<SampleCriteria> oit = samples.iterator();
		while (oit.hasNext()) {
			SampleCriteria sample = oit.next();
			System.out.println("got "+sample.getSampleId());
		}
		ssi.logoff(id);
	}
    //*/
}
