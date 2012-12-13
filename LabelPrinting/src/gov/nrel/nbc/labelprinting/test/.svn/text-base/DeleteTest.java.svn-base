package gov.nrel.nbc.labelprinting.test;

import gov.nrel.nbc.labelprinting.server.TrackerServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DeleteTest extends TestCase {

	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = TrackerServiceImpl.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(DeleteTest.class);
	}
	
	public void testDeleteSampleWithoutArg() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		String tracking_id = System.getProperty("id");
		System.out.println("test = "+tracking_id);
		System.out.println("trying to delete "+tracking_id);
		int id = 0;
		try {
			id = Integer.parseInt(tracking_id);
		} catch (NumberFormatException nfe) {
		}
		if (id != 0) {
			Boolean ret = tsi.deleteSample(id);
			System.out.println("after delete with ret="+ret);
			assertTrue(ret);
		} else {
			assertTrue(false);
		}
	}

}
