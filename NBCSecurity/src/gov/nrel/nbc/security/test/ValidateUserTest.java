package gov.nrel.nbc.security.test;

import gov.nrel.nbc.security.server.SecurityServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ValidateUserTest extends TestCase {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = SecurityServiceImpl.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(ValidateUserTest.class);
	}
	
	public void testUnlockUser() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		String user = "jalbersh";
		ssi.validateUser(user);
		boolean ret = ssi.isValidated(user);
		assertTrue(ret);
	}

	private void validate(String user, String role, String group) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		ssi.validateUser(user);
		boolean ret = ssi.isValidated(user);
		assertTrue(ret);
		int num = ssi.addUserToRoleAndGroup(user, role, group);
		assertTrue(num>0);		
	}
	
	public void testValidateUser() throws Exception {
		// change these three parameters for the new user
		String user = "*";
		String role = "*";
		String group = "*";
		// don't change anything below this comment
		validate(user,role,group);
	}
	
}
