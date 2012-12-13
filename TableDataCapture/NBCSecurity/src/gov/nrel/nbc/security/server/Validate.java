package gov.nrel.nbc.security.server;

public class Validate {

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		if (args.length>2) {
			String user = args[0];
			String role = args[1];
			String group = args[2];
			ssi.validateUser(user);
			ssi.isValidated(user);
			ssi.addUserToRoleAndGroup(user, role, group);
		} else {
			System.out.println("Usage: SecurityServiceImpl user role group");
		}
	}
}
