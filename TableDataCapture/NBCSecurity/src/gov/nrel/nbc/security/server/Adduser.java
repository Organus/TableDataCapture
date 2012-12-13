package gov.nrel.nbc.security.server;

import gov.nrel.nbc.security.model.Users;

public class Adduser {
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		if (args.length>2) {
			String user = args[0];
			String first = args[1];
			String last = args[2];
			String email = args[3];
			Users users = new Users();
			users.setUserId(user);
			users.setEmail(email);
			users.setFirst(first);
			users.setLast(last);
			ssi.addUser(users, false);
			ssi.addUserToRoleAndGroup(user, "*", "*");
		} else {
			System.out.println("Usage: Adduser userId first last email ");
		}
	}
}
