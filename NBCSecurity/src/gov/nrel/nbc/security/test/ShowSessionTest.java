package gov.nrel.nbc.security.test;

//import com.google.gwt.user.client.Window;

import gov.nrel.nbc.security.server.ShowSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShowSessionTest extends TestCase {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = ShowSession.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(ShowSessionTest.class);
	}
	
	public void testShowSession() throws Exception {
		//Window.open("http//localhost:8880/security/showSession", "sessionTest", null);
	}
}
