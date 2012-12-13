package gov.nrel.nbc.security.test;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.model.GroupTypes;
import gov.nrel.nbc.security.model.Groups;
import gov.nrel.nbc.security.model.Permissions;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.server.SecurityServiceImpl;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SecurityServiceImplTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = SecurityServiceImpl.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(SecurityServiceImplTest.class);
	}
	
	public void testAddUser() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		Users user = new Users();
		user.setEmail("boo.ger@nrel.gov");
		user.setFirst("boo");
		user.setLast("ger");
		user.setUserId("booger");
		List<Users> users = ssi.getUsers();
		int num = users.size();
		boolean ret = ssi.addUser(user,false);//,"*","*");
		assertTrue(ret);
		users = ssi.getUsers();
		int num1 = users.size();
		assertTrue(num1 == num+1);
		int ret1 = ssi.addUserToRoleAndGroup("*", "*", "*");
		if (ret1 != 0)
			System.out.println("added");
		else
			System.err.println("Failed");
	}

	public void testGetUsers() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		List<Users> users = ssi.getUsers();
		Iterator<Users> uit = users.iterator();
		while (uit.hasNext()) {
			Users u = uit.next();
			System.out.println("got user="+u.getUserId());
		}
	}

	public void testGetGroups() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		List<Groups> users = ssi.getGroups();
		Iterator<Groups> uit = users.iterator();
		while (uit.hasNext()) {
			Groups u = uit.next();
			GroupTypes t = ssi.getJDBCGroupType(u.getName());
			System.out.println("got group="+u.getName()+" of type="+t.getName());
		}
	}

	public void testGetGroupForUser() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		List<String> users = ssi.getGroupsForUser(id);
		Iterator<String> uit = users.iterator();
		while (uit.hasNext()) {
			String u = uit.next();
			GroupTypes t = ssi.getJDBCGroupType(u);
			System.out.println("got group="+u+" of type="+t.getName());
		}
	}

	public void testGetPermissions() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		List<Permissions> users = ssi.getPermissions();
		Iterator<Permissions> uit = users.iterator();
		while (uit.hasNext()) {
			Permissions u = uit.next();
			System.out.println("got Permissions="+u.getName());
		}
	}

	public void testHasPermission() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	//DataEncryption de = new DataEncryption();
    	//String unc = de.decrypt(TEST_PASS);
		//String id = ssi.logon(TEST_USER,unc);
		Boolean b = false;
		//b = ssi.hasPermission(id,"*","*");
		//assertTrue(b);
		//b = ssi.hasPermission(id,"*","*");
		b = ssi.hasPermission("*","*");
		assertFalse(b);
	}

	public void testAddUserToRoleAndGroup() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		int ret = ssi.addUserToRoleAndGroup(id, "*", "*");
		if (ret != 0)
			System.out.println("added");
		else
			System.err.println("Failed");
		
	}
	
	public void testLogonLogoff() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		assertNotNull(id);
		boolean ret = ssi.isLoggedIn(id);
		assertTrue(ret);
		ssi.logoff(id);
		ret = ssi.isLoggedIn(id);
		assertFalse(ret);
	}

	public void testIsLoggedIn() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		boolean ret = ssi.isLoggedIn(id);
		assertTrue(ret);
	}

	public void testIsCurrent() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		boolean ret = ssi.isCurrent("*");
		assertTrue(ret);
		ssi.isCurrent(59);
		ssi.isCurrent(58);
	}

	public void testLogon() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		assertNotNull(id);
	}

	public void testLogoff() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		boolean ret = ssi.isLoggedIn(id);
		assertTrue(ret);
		ssi.logoff(TEST_USER);
		ret = ssi.isLoggedIn(id);
		assertFalse(ret);
	}

	public void testRemoveUserFromRoleAndGroup() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
    	DataEncryption de = new DataEncryption();
    	String unc = de.decrypt(TEST_PASS);
		String id = ssi.logon(TEST_USER,unc);
		ssi.removeUserFromRoleAndGroup(id, "*", "*");
	}

	public void testGetJDBCUsers() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		List<Users> users = ssi.getJDBCUsers();
		Iterator<Users> uit = users.iterator();
		while (uit.hasNext()) {
			Users u = uit.next();
			System.out.println("got user="+u.getUserId());
		}
	}
}
