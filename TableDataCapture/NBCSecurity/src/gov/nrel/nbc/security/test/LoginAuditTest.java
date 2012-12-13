package gov.nrel.nbc.security.test;

import java.util.Date;

import com.ibm.icu.util.Calendar;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.server.LoginAudit;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LoginAuditTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = LoginAudit.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(LoginAuditTest.class);
	}
	/*
	public void testAudit() throws Exception {
		System.out.println("in audit test");
		LoginAudit la = LoginAudit.getInstance();
		Users user = new Users();
		user.setUserId("jalbersh");
		String status = "";
		while (!status.equals("locked")) {
			if (la.notLocked(user)) {
				status = "not locked";
				System.out.println(status);
				la.incrementCount(user);
				System.out.println("incremented");
			} else {
				status = "locked";
				System.out.println(status);
			}
		}
		System.out.println("audit done");
	}
	*/
	public void testAuditReport() throws Exception {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		String path = INITIAL_PART_DB+ssi.dbPathValue+"/security";
		String user = ssi.dbUserValue;
		String pass = ssi.dbPassValue;
		DataEncryption de = new DataEncryption();
		String depass = de.decrypt(pass);
		DBUtils db = new DBUtils(path,user,depass);
		String sql = "select `userId`,`activity`,`activityDate` from sec_audit ";
		sql += " where userId != '*' and userId != '*' and userId != 'junit'";
		sql += " order by userId,activityDate desc";
		db.performQuery(sql, false);
		while (db.getNextRow()) {
			String userId = db.getStringColumn(1);
			String activity = db.getStringColumn(2);
			Date date = db.getDateColumn(3);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int mon = cal.get(Calendar.MONTH)+1;
			String smon = "";
			if (mon < 10)
				smon = "0"+mon;
			else
				smon = String.valueOf(mon);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String sday = "";
			if (day < 10)
				sday = "0"+day;
			else
				sday = String.valueOf(day);
			int year = cal.get(Calendar.YEAR);
			if (userId != null && !userId.isEmpty())
				System.out.println(userId+": "+activity+" on "+smon+"/"+sday+"/"+year);
		}
		db.close();
	}
}