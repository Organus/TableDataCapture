package gov.nrel.nbc.security.server;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.client.DevTestProdConstants;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.security.dbUtils.GenerateGUID;
import gov.nrel.nbc.security.model.GroupTypes;
import gov.nrel.nbc.security.model.Groups;
import gov.nrel.nbc.security.model.Permissions;
import gov.nrel.nbc.security.model.RoleGroupPair;
import gov.nrel.nbc.security.model.Roles;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.utils.XLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SecurityServiceImpl implements DevTestProdConstants, AppConstants {

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(XLogger.INFO);
	private static final long serialVersionUID = -7744278154082500277L;

    public String dbUserValue = "";
    public String dbPassValue = "";
    public String dbPathValue = "";
    
    public String emailHostValue = "";
    public String emailUserValue = "";
    public String emailPassValue = "";

    public String adminUser1Value = "";
    public String adminUser2Value = "";
    public String adminUser3Value = "";		
    
    public String security_domain = "";
	public String security_mode = "";
	public String security_ldap_provider="";
    
    public SecurityServiceImpl() {
    	getProperties();
    }
	public void getProperties() {
		ResourceBundle rBundle = null;
		
		try {
			rBundle = ResourceBundle.getBundle(SECURITY_PROPERTIES_FILE_NAME);
		    dbUserValue = rBundle.getString(dbUserKey);
		    dbPassValue = rBundle.getString(dbPassKey);
		    dbPathValue = rBundle.getString(dbPathKey);
		    
		    emailHostValue = rBundle.getString(emailHostKey);
		    emailUserValue = rBundle.getString(emailUserKey);
		    emailPassValue = rBundle.getString(emailPassKey);

		    adminUser1Value = rBundle.getString(adminUser1Key);
		    adminUser2Value = rBundle.getString(adminUser2Key);
		    adminUser3Value = rBundle.getString(adminUser3Key);	
		    
		    security_domain = rBundle.getString(domain);
			security_mode = rBundle.getString(mode);
			security_ldap_provider = rBundle.getString(ldap_provider);
		} catch (MissingResourceException mre) {
			log.warning("Problem reading resource file: "+mre.getMessage());
			//log.warning(SecurityServiceImpl.getStackTrace(mre));
			dbUserValue=DEFAULT_dbUserValue;
			dbPassValue=DEFAULT_dbPassValue;
			dbPathValue=DEFAULT_dbPathValue;
			emailHostValue=DEFAULT_emailHostValue;
			emailUserValue=DEFAULT_emailUserValue;
			emailPassValue=DEFAULT_emailPassValue;
			adminUser1Value=DEFAULT_adminUser1Value;
			adminUser2Value=DEFAULT_adminUser2Value;
			adminUser3Value=DEFAULT_adminUser3Value;
			security_domain = DEFAULT_security_domain;
			security_mode = DEFAULT_security_mode;
			security_ldap_provider = DEFAULT_ldap_provider;
		}	
	}
	/**
	 * Method to add users
	 * 
	 * @param UsersDTO the <Users> object to add
	 * @return Long the pk of the user
	 */
	//@Override
	public Boolean addUser(Users user, Boolean email) {
		log.info("in addUser with new user="+user.getUserId());
		System.out.println("in addUser with new user="+user.getUserId());
		boolean worked = false;
		Calendar cal = Calendar.getInstance();
		DBUtils db = new DBUtils();
		Users u = getJDBCUser(user.getUserId());
		String sql = "";
		if (u != null) {
			sql = "update sec_users set last_used="+cal.getTime();
			int ret = db.performUpdate(sql);
			if (ret > 0) worked = true;
		} else {
			String into = "";
			String values = "(";
			sql = "insert into sec_users(";
			if (user.getUserId() != null && !user.getUserId().isEmpty()) {
				into += "user_id";
				values += "'"+user.getUserId()+"'";
			}
			if (user.getFirst() != null && !user.getFirst().isEmpty()) {
				into += ", user_first_name";
				values += ",'"+user.getFirst()+"'";
			}
			if (user.getLast() != null && !user.getLast().isEmpty()) {
				into += ", user_last_name";
				values += ",'"+user.getLast()+"'";
			}
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				into += ", user_email";
				values += ",'"+user.getEmail()+"'";
			}
			values += ",CURRENT_TIMESTAMP)";
			sql += into + ", last_used)" + " values " + values;
			log.info("add user sql="+sql);
			int ret = db.performInsert(sql);
			if (ret > 0) worked = true;
		}
		if (email) 
			sendmail("Added new user "+user.getUserId()+". Please validate.", null, false);
		else {
			sql = "update sec_users set enabled=true where user_id='"+user.getUserId()+"'";
			db.performUpdate(sql);
		}
		db.close();
		return worked;
 	}

	public Boolean addUser(Users user) {
		log.info("in addUser with new user="+user.getUserId());
		System.out.println("in addUser with new user="+user.getUserId());
		boolean worked = false;
		Calendar cal = Calendar.getInstance();
		DBUtils db = new DBUtils();
		Users u = getJDBCUser(user.getUserId());
		String sql = "";
		if (u != null) {
			sql = "update sec_users set last_used="+cal.getTime();
			int ret = db.performUpdate(sql);
			if (ret > 0) worked = true;
		} else {
			String into = "";
			String values = "(";
			sql = "insert into sec_users(";
			if (user.getUserId() != null && !user.getUserId().isEmpty()) {
				into += "user_id";
				values += "'"+user.getUserId()+"'";
			}
			if (user.getFirst() != null && !user.getFirst().isEmpty()) {
				into += ", user_first_name";
				values += ",'"+user.getFirst()+"'";
			}
			if (user.getLast() != null && !user.getLast().isEmpty()) {
				into += ", user_last_name";
				values += ",'"+user.getLast()+"'";
			}
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				into += ", user_email";
				values += ",'"+user.getEmail()+"'";
			}
			values += ",CURRENT_TIMESTAMP)";
			sql += into + ", last_used)" + " values " + values;
			log.info("add user sql="+sql);
			int ret = db.performInsert(sql);
			if (ret > 0) worked = true;
		}
		sql = "update sec_users set enabled=true where user_id='"+user.getUserId()+"'";
		db.performUpdate(sql);
		db.close();
		return worked;
 	}

	public Boolean isAuthorizedThruAD(String username) {
		if (!checkUser(username)) return false;
		String password = getPassword(username);
		String domain = getUserDomain(username);
		if (domain == null || domain.isEmpty()) 
			domain = security_domain;
		String userDomain = "";
		if (username.indexOf(domain) != -1)
			userDomain = username;
		else
			userDomain = username + "@" + security_domain;
		return isAuthorizedThruAD(userDomain,password);
	}
	
	/**
	 * Method to check authorization
	 * 
	 * @param String userId the userOd of the user
	 * @param String password the plain text password of the user
	 * @return Boolean true if the user is authorized, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public Boolean isAuthorizedThruAD(String username, String password) {
		//Server: ds.nrel.gov:389
		//Base dn: dc=nrel,dc=gov
		String provider = "";
		getProperties();
		log.info("user="+username);
		String domain = getUserDomain(username);
		if (domain == null || domain.isEmpty()) 
			domain = security_domain;
		log.info("domain="+domain);
		if (domain.equals("nrel.gov"))
			provider = security_ldap_provider;
		else
			provider = "ldap://cdmzdc1.nrelhub.gov:389";
		log.info("ldap_provider="+provider);
		if (username.indexOf(domain) == -1)
			username +=  "@" + domain;
		Boolean auth = false;
		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,LDAPFACTORY);
			env.put(Context.PROVIDER_URL, provider);//LDAP server location
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put(Context.SECURITY_PRINCIPAL,username); // specify the username
			env.put(Context.SECURITY_CREDENTIALS,password); // specify the password
			DirContext ctx = new InitialDirContext(env);
			System.out.println("CTX:"+ctx);		
			auth = true;
		} catch (Exception e) {
			//log.info(getStackTrace(e));
			log.warning(e.getMessage());
			auth = false;
		}
    	return auth;
	}
	
	public Boolean checkUser(String userId) {
		Boolean ret = false;
		DBUtils db = new DBUtils();
		String sql = "select user_id from sec_users where user_id = '"+userId+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String user = db.getStringColumn(1);
			log.info("user="+user);
			ret = true;
		}
		db.close();
    	return ret;
	}
	
	public String getPassword(String userId) {
		String password = "";
		DBUtils db = new DBUtils();
		String sql = "select user_password from sec_users where user_id = '"+userId+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String encryptedPWD = db.getStringColumn(1);
			DataEncryption de = new DataEncryption(CRYPTOKEY);
			password = de.decrypt(encryptedPWD);
		}
		db.close();
    	return password;
	}
	
	/**
	 * Method to check authorization
	 * 
	 * @param String userId the userOd of the user
	 * @param String password the plain text password of the user
	 * @return Boolean true if the user is authorized, false otherwise
	 */
	public Boolean isAuthorized(String userId, String password) {
		Boolean auth = false;
		DBUtils db = new DBUtils();
		String sql = "select user_password from sec_users where user_id = '"+userId+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String encryptedPWD = db.getStringColumn(1);
			DataEncryption de = new DataEncryption(CRYPTOKEY);
			String decryptedPWD = de.decrypt(encryptedPWD);
			if (decryptedPWD != null && decryptedPWD.equals(password)) auth = true;
		}
		db.close();
    	return auth;
	}
	/*
	private long getPermissionLevel(String permission) {
		long ret = 0;
		String sql = "select permission_pk from sec_permissions where permission_name = '"+permission+"'";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			ret = db.getLongColumn(1);
		}
		db.close();
		return ret;
	}

	private long getGroupLevel(String group) {
		long ret = 0;
		String sql = "select group_pk from sec_groups where group_name = '"+group+"'";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			ret = db.getLongColumn(1);
		}
		db.close();
		return ret;
	}
    */
	public boolean setGroups(String id, String type, List<String> groups) {
		boolean ret = false;
		if (id != null && !id.isEmpty() && type != null && !type.isEmpty() && 
				groups != null && groups.size()>0) {
			String sql = "insert into sec_object_groups (object_id,"
				       + "object_type,group_pk) values ("+id+",'"+type+"',";
			DBUtils db = new DBUtils();
			Iterator<String> git = groups.iterator();
			while (git.hasNext()) {
				String group = git.next();
				Groups g = getJDBCGroup(group);
				if (g != null) {
					int num = db.performInsert(sql+g.getPk()+")");
					if (num>0) ret = true;
				}
			}
			db.close();
		}
		return ret;
	}

	public List<Long> getObjectsInGroup(String group) {
		List<Long> ids = new ArrayList<Long> ();
		if (group != null && !group.isEmpty()) {
			DBUtils db = new DBUtils();
			Groups g = getJDBCGroup(group);
			String sql = "select object_id from sec_object_groups"
					   + " where group_pk="+g.getPk();
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long id = db.getLongColumn(1);
				ids.add(id);
			}
			db.close();
		}
		return ids;
	}

	/**
	 * Method to add object to group
	 * 
	 * @param long id the id of the object
	 * @param type the type of the object
	 * @param String group the group name
	 * @return Integer the number of rows inserted ( should be 1)
	 */
	public Integer addObjectToUserGroups(long id, String type, String sessionId) {
		int ret = 0;
		List<String> groups = getGroupsForUser(sessionId);
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			if (group != null && !group.isEmpty()) {
				DBUtils db = new DBUtils();
				Groups g = getJDBCGroup(group);
				if (g == null)
					return -1;
				
				String sql = "insert into sec_object_groups"
					       + " (object_id,object_type,group_pk) values ("
						   + id + ",'" + type + "',"+ g.getPk() + ")";
				ret = db.performInsert(sql);
				db.close();
			}
		}
		return ret;
	}

	/**
	 * Method to add object to group
	 * 
	 * @param long id the id of the object
	 * @param type the type of the object
	 * @param String group the group name
	 * @return Integer the number of rows inserted ( should be 1)
	 */
	public Integer addObjectToGroupsForUser(long id, String type, String userId) {
		int ret = 0;
		List<String> groups = getGroupsForUserId(userId);
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			if (group != null && !group.isEmpty()) {
				DBUtils db = new DBUtils();
				Groups g = getJDBCGroup(group);
				String sql = "insert into sec_object_groups"
					       + " (object_id,object_type,group_pk) values ("
						   + id + ",'" + type + "',"+ g.getPk() + ")";
				ret = db.performInsert(sql);
				db.close();
			}
		}
		return ret;
	}

	/**
	 * Method to add object to group
	 * 
	 * @param long id the id of the object
	 * @param type the type of the object
	 * @param String group the group name
	 * @return Integer the number of rows inserted ( should be 1)
	 */
	public Integer addObjectToGroup(long id, String type, String group) {
		int ret = 0;
		if (group != null && !group.isEmpty()) {
			DBUtils db = new DBUtils();
			Groups g = getJDBCGroup(group);
			String sql = "insert into sec_object_groups"
				       + " (object_id,object_type,group_pk) values ("
					   + id + ",'" + type + "',"+ g.getPk() + ")";
			ret = db.performInsert(sql);
			db.close();
		}
		return ret;
	}

	/**
	 * Method to add object to group
	 * 
	 * @param long id the id of the object
	 * @param type the type of the object
	 * @param List<String> groups the group names
	 * @return Integer the number of rows inserted ( should be 1)
	 */
	public Integer addObjectToGroups(long id, String type, List<String> groups) {
		int ret = 0;
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			ret = addObjectToGroup(id,type,group);
		}
		return ret;
	}

	private boolean isGroupValid(String group) {
		boolean ret = false;
		if (group != null && !group.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select enabled from `sec_groups`"
					   + " where group_name='"+group+"'";
			db.performQuery(sql, false);
			if (db.getNextRow()) {
				ret = db.getBooleanColumn(1);
			}
			db.close();
		}
		return ret;
	}
	
	public boolean isGroupValidForUser(String id) {
		boolean ret = false;
		String userId = getUserIdFromId(id);
		Users user = getJDBCUser(userId);
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select group_pk from `sec_user_role_group_mapping`"
					   + " where user_pk="+user.getPk();
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				Groups group = getJDBCGroup(gid);	
				if (group != null) {
					boolean enabled = isGroupValid(group.getName());
					if (enabled) ret = true;
				}
			}
			db.close();
		}
		return ret;
	}

	public List<String> getGroupsForUser(String id) {
		List<String> groups = new ArrayList<String> ();
		String userId = getUserIdFromId(id);
		Users user = getJDBCUser(userId);
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select group_pk from `sec_user_role_group_mapping`"
					   + " where user_pk="+user.getPk();
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				Groups group = getJDBCGroup(gid);				
				groups.add(group.getName());
			}
			db.close();
		}
		if (id.equals(JUNIT_SESSION_ID))
			groups.add("SABC");
		return groups;
	}

	public List<String> getGroupsForUserId(String userId) {
		List<String> groups = new ArrayList<String> ();
		Users user = getJDBCUser(userId);
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select group_pk from `sec_user_role_group_mapping`"
					   + " where user_pk="+user.getPk();
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				Groups group = getJDBCGroup(gid);				
				groups.add(group.getName());
			}
			db.close();
		}
		return groups;
	}

	//@Override
	public String getGroupForUser(String id) {
		String group = "";
		String userId = getUserIdFromId(id);
		Users user = getJDBCUser(userId);
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select group_pk from `sec_logins`"
					   + " where user_pk="+user.getPk();
			db.performQuery(sql, false);
			if (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				Groups grp = getJDBCGroup(gid);				
				group = grp.getName();
			}
			db.close();
		}
		return group;
	}

	public List<Long> getObjectIdsForUser(String id, String type) {
		List<Long> objects = new ArrayList<Long>();
		List<String> groups = null;
		if (!id.equals(JUNIT_SESSION_ID))
			groups = getGroupsForUser(id);
		else {
			groups = new ArrayList<String>();
			groups.add("SABC");
		}
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			
			DBUtils db = new DBUtils();
			Groups g = getJDBCGroup(group);
			String sql = "select object_id from sec_object_groups"
					   + " where group_pk="+g.getPk()+" and object_type = '"+type+"'";
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				objects.add(gid);
			}
			db.close();
		}
		return objects;
	}

	public List<Long> getObjectIdsForUser(List<String> groups, String type) {
		List<Long> objects = new ArrayList<Long>();
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			
			DBUtils db = new DBUtils();
			Groups g = getJDBCGroup(group);
			String sql = "select object_id from sec_object_groups"
					   + " where group_pk="+g.getPk()+" and object_type = '"+type+"'";
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long gid = db.getLongColumn(1);
				objects.add(gid);
			}
			db.close();
		}
		return objects;
	}

	private long getUserPkFromUserId(String userId) {
		long pk = 0;
		DBUtils db = new DBUtils();
		String sql = "select user_pk from sec_users where user_id = '"+userId+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			pk = db.getLongColumn(1);
		}
		db.close();
		return pk;
	}
	
	private long getGroupPkFromGroupName(String group) {
		long grp_pk = 0;
		String sql = "select group_pk from sec_groups where group_name = '"+group+"'";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			grp_pk = db.getLongColumn(1);
		}
		db.close();
		return grp_pk;
	}
	
	private long getRolePkFromRoleName(String role) {
		long grp_pk = 0;
		String sql = "select role_pk from sec_roles where role_name = '"+role+"'";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			grp_pk = db.getLongColumn(1);
		}
		db.close();
		return grp_pk;
	}
	
	public Boolean hasPermission(String id, String task) {
		//log.info("in SecurityServiceImpl.hasPermission with id="+id);
		boolean ret = false;
		
		if (task.equals("Email")) {
			String email = getUserEmail(getSessionId());
			if (email != null && !email.isEmpty()) ret = true;
			return ret;
		}
		else if (task.equals("Print")) {
			String pp = "";
			if (id != null && !id.isEmpty()) pp = this.getUserPrinterPermission(id);
			if (pp != null && !pp.isEmpty() && !pp.equals("0")) ret = true;
			return ret;
		}
		DBUtils db = new DBUtils();
			
		List<RoleGroupPair> rgps = new ArrayList<RoleGroupPair>();
		String userId = getUserIdFromId(id);
		//log.info("userId="+userId);
		long user_pk = getUserPkFromUserId(userId);
		List<String> groups = getGroupsForUser(id);
		Iterator<String> git = groups.iterator();
		while (git.hasNext()) {
			String group = git.next();
			long grp_pk = getGroupPkFromGroupName(group);
			String sql = "select role_pk, group_pk from sec_user_role_group_mapping";
			sql += " where user_pk = "+user_pk;
			sql += " and group_pk = "+grp_pk;
			db.performQuery(sql, false);
			while (db.getNextRow()) {
				long rolePk = db.getLongColumn(1);
				long groupPk = db.getLongColumn(2);
				RoleGroupPair rgp = new RoleGroupPair();
				rgp.setGroup_pk(groupPk);
				rgp.setRole_pk(rolePk);
				rgp.setPk(user_pk);
				rgps.add(rgp);
			}
			//db.close();
			Iterator<RoleGroupPair> rit = rgps.iterator();
			sql = "select p.permission_name from sec_permissions p,"
				+ " sec_role_group_permission_mapping rp"
			    + " where rp.permission_pk = p.permission_pk and";
			//long task_level = getPermissionLevel(task);
			//long group_level = getGroupLevel(group);
			while (rit.hasNext()&&!ret) {
				RoleGroupPair rgp = rit.next();
				sql += " rp.role_pk = "+rgp.getRole_pk();
				sql += " and";
				sql += " rp.group_pk = "+rgp.getGroup_pk();	
				sql += " and p.permission_name = '"+task+"'";
				String permission = null;
				try {
					if (db.performQuery(sql, false).first())
						permission = db.getStringColumn(1);
				} catch (Exception e3){}
				if (permission != null) {
					ret = true;
				}
				// TODO - check this
				//long plevel = getPermissionLevel(permission);
				//long glevel = rgp.getGroup_pk();
				//if (plevel >= task_level && glevel <= group_level) {
				//	ret = true;
				//	db.close();
				//	break;
				//}
				//db.close();
			}
		}
		db.close();
		return ret;
	}

	/**
	 * Method to check authorization
	 * 
	 * @param String userId the userOd of the user
	 * @param String group the group
	 */
	public Integer addUserToRoleAndGroup(String userId, String role, String group) {
		int ret = 0;
		DBUtils db = new DBUtils();
		String sql = "insert into sec_user_role_group_mapping"
				   + " (user_pk, role_pk, group_pk) values (";
		
		long user_pk = getUserPkFromUserId(userId);
		long grp_pk = getGroupPkFromGroupName(group);
		long role_pk = getRolePkFromRoleName(role);
		if (user_pk != 0 && grp_pk != 0 && role_pk != 0) {
			sql += user_pk + "," + role_pk + "," + grp_pk + ")";
			ret = db.performInsert(sql);
		} else {
			if (user_pk == 0)
				log.warning("user "+userId+" not found");
			else if (role_pk == 0)
				log.warning("role "+role+" not found");
			else
				log.warning("group "+group+" not found");				
		}
		db.close();
        return ret;
  	}
	
	public List<String> getLogins() {
    	List<String> logins = new ArrayList<String>();
    	String sql = "select login_id from sec_logins";
    	DBUtils db = new DBUtils();
    	db.performQuery(sql, false);
    	while (db.getNextRow()) {
    		String id = db.getStringColumn(1);
    		logins.add(id);
    	}
    	db.close();
	   	return logins;		
	}
    public Boolean isLoggedIn(String id) {
    	boolean ret = false;
    	String sql = "select user_pk from sec_logins where login_id = '"+id+"'";
    	DBUtils db = new DBUtils();
    	db.performQuery(sql, false);
    	if (db.getNextRow()) {
    		long pk = db.getLongColumn(1);
    		if (pk > 0)
    			ret = true;
    	}
    	db.close();
	   	return ret;
    }

    public String sayHello(String msg) {
    	log.info("The server says: "+msg);
    	return "Server says hello, "+msg;
    }
    
    public boolean isValidated(String userId) {
    	boolean ret = false;
		String sql1 = "select enabled from sec_users where user_id='"+userId+"'";
		DBUtils db = new DBUtils();
		db.performQuery(sql1, false);
		if (db.getNextRow()) {
			ret = db.getBooleanColumn(1);
		}
		db.close();
		return ret;
    }

    public void invalidateUser(String userId) {
		String sql1 = "update sec_users set enabled=false where user_id='"+userId+"'";
		DBUtils db = new DBUtils();
		int ret = db.performUpdate(sql1);
		log.info("updated "+ret+" rows");
		db.close();
    }

    public void validateUser(String userId) {
		String sql1 = "update sec_users set enabled=true where user_id='"+userId+"'";
		DBUtils db = new DBUtils();
		int ret = db.performUpdate(sql1);
		log.info("updated "+ret+" rows");
		db.close();
    }

    public void invalidateGroup(String group) {
		String sql1 = "update sec_groups set enabled=false where group_name='"+group+"'";
		DBUtils db = new DBUtils();
		int ret = db.performUpdate(sql1);
		log.info("updated "+ret+" rows");
		db.close();
    }

    public void validateGroup(String group) {
		String sql1 = "update sec_groups set enabled=true where group_name='"+group+"'";
		DBUtils db = new DBUtils();
		int ret = db.performUpdate(sql1);
		log.info("updated "+ret+" rows");
		db.close();
    }

    public String getUserIdFromId(String id) {
    	String ret = "";
		DBUtils db = new DBUtils();
		String sql = "select user_pk from sec_logins where login_id = '"+id+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			long pk = db.getLongColumn(1);
			if (pk != 0) {
				DBUtils db1 = new DBUtils();
				String sql1 = "select user_id from sec_users where user_pk="+pk;
				db1.performQuery(sql1, false);
				if (db1.getNextRow()) {
					ret = db1.getStringColumn(1);
				}
				db1.close();
			}
		}
		db.close();
		return ret;
    }

	public synchronized void setModifiedTime(String session_id) {
    	String userId = getUserIdFromId(session_id);
    	if (userId != null && !userId.isEmpty()) {
	    	Users user = getJDBCUser(userId);		
	        String sql = "update sec_users set last_used = CURRENT_TIMESTAMP"
	         		   + " where user_id = '"+user.getUserId()+"'";
	        DBUtils db = new DBUtils();
	        db.performUpdate(sql);
	        db.close();
	        //log.info("update ret="+ret);
    	}
	}
	
	public synchronized void setModifiedTimeForUser(String userId) {
    	if (userId != null && !userId.isEmpty()) {
    		log.info("userId = " + userId);
	    	Users user = getJDBCUser(userId);		
	        String sql = "update sec_users set last_used = CURRENT_TIMESTAMP"
	         		   + " where user_id = '"+user.getUserId()+"'";
	        log.info("sql= " + sql);
	        DBUtils db = new DBUtils();
	        db.performUpdate(sql);
	        db.close();
	        //log.info("update ret="+ret);
    	}
	}
	
	private synchronized Timestamp getLastUsed(String session_id) {
		//log.info("in getLastUSed");
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        Timestamp time = currentTimestamp;

    	String userId = getUserIdFromId(session_id);
    	if (userId != null && !userId.isEmpty()) {
	    	Users user = getJDBCUser(userId);
	        //log.info("user="+user.getUserId());
	        String sql = "select last_used from sec_users where user_id = '"+user.getUserId()+"'";
	        DBUtils db = new DBUtils();
	        db.performQuery(sql, false);
	        if (db.getNextRow()) {
	            time = db.getTimestampColumn(1);
	            //log.info("got time="+time);
	        }
	        db.close();
    	} else {
    		time = null;
    	}
        return time;
	}
    
	private synchronized boolean pastActive(Timestamp time, int pastActive) {
		boolean past = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int mins = cal.get(Calendar.MINUTE);
        //log.info("minutes="+mins);
        int hours = cal.get(Calendar.HOUR);
        //log.info("hours="+hours);
        int days = cal.get(Calendar.DAY_OF_MONTH);
        //log.info("days="+days);
        if (mins+pastActive > 60)
        {
        	mins = mins+pastActive-60;
        	//log.info("mins = "+mins);
        	if (hours+1 > 12) {
        		hours=hours+1-12;
        		//log.info("hours="+hours);
        		days = days+1;
        		if (days > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        			days = 1;
        			month = month+1;
        			if (month>11) {
        				month = 0;
        				year = year + 1;
        			}
        		}
        		//log.info("days="+days);
        	} else
        		hours = hours+1;
        } else {
        	mins = mins+pastActive;
        }
        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        Calendar ctimePlusPastActive = Calendar.getInstance();
        ctimePlusPastActive.set(Calendar.MINUTE,mins);
        ctimePlusPastActive.set(Calendar.HOUR,hours);
        ctimePlusPastActive.set(Calendar.DAY_OF_MONTH,days);
        ctimePlusPastActive.set(Calendar.MONTH,month);
        ctimePlusPastActive.set(Calendar.YEAR,year);
        
        java.util.Date timePlusPastActive = ctimePlusPastActive.getTime();

        // create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();
        //log.info("now="+now.toString());
        //log.info("timePlusPastActive="+timePlusPastActive.toString());
        if (now.after(timePlusPastActive)) {
        	//log.info("past "+pastActive+" minutes of activity");
        	past = true;
        }
        return past;
	}

	public Boolean notActive(String session_id, int time) {
		Timestamp ts = getLastUsed(session_id);
		if (ts != null) 
			return pastActive(ts,time);
		return false;
    }
	
	public Boolean isCurrent(String userId) {
		//log.info("in isCurrent");
		Boolean past = false;
		String sql = "select c.expires from sec_clients c, sec_users u where ";
		sql += "c.guid_id = u.guid_id and user_id = '"+userId+"'";
		//log.info("sql="+sql);
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String sdt = db.getStringColumn(1);
			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt = null;
			try {
				dt = dFormat.parse(sdt);
			} catch (ParseException pe) {}
			past = !pastExpired(new Timestamp(dt.getTime()));
		}
		if (past) {
			//log.info(userId + " is current");
		}
		else {
			//log.info(userId + " is past due");
		}
    	return past;
	}
	
	public Boolean isCurrent(long id) {
		//log.info("in isCurrent");
		Boolean past = false;
		String sql = "select client,expires from sec_clients where guid_id = "+id;
		//log.info("sql="+sql);
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String client = db.getStringColumn(1);
			String sdt = db.getStringColumn(2);
			//log.info("got date="+sdt);
			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt = null;
			try {
				dt = dFormat.parse(sdt);
			} catch (ParseException pe) {}
			//log.info("dt="+dt.toString());
			past = !pastExpired(new Timestamp(dt.getTime()));
			if (past) {
				//log.info(client + " is current");
			}
			else {
				//log.info(client + " is past due");
			}
		}
    	return past;
	}

	private synchronized boolean pastExpired(Timestamp time) {
		if (time == null) return false;
		//log.info("checking pastExpired with "+time.toString());
		boolean past = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int mins = cal.get(Calendar.MINUTE);
        //log.info("minutes="+mins);
        int hours = cal.get(Calendar.HOUR);
        //log.info("hours="+hours);
        int days = cal.get(Calendar.DAY_OF_MONTH);
        //log.info("days="+days);
        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        Calendar ctimePlusPastActive = Calendar.getInstance();
        ctimePlusPastActive.set(Calendar.MINUTE,mins);
        ctimePlusPastActive.set(Calendar.HOUR,hours);
        ctimePlusPastActive.set(Calendar.DAY_OF_MONTH,days);
        ctimePlusPastActive.set(Calendar.MONTH,month);
        ctimePlusPastActive.set(Calendar.YEAR,year);
        
        java.util.Date timePlusPastActive = ctimePlusPastActive.getTime();

        // create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();
        //log.info("now="+now.toString());
        //log.info("timePlusPastActive="+timePlusPastActive.toString());
        if (now.after(timePlusPastActive)) {
        	//log.info("account expired");
        	past = true;
        } else {
        	//log.info("account is current");
        }
        return past;
	}

	public void sendmail(String msg, String filename, boolean print) {
		//log.info("in sendmail");
		//System.out.println("in sendmail");
	    try {
	    	String emailUserValue = null;
	    	String emailPassValue	= null;
	    	String adminUser1Value = null;
	    	String adminUser2Value = null;
	    	String adminUser3Value = null;
			getProperties();
		    String to1 = adminUser1Value;//ADMIN_USER1;
		    String to2 = adminUser2Value;//ADMIN_USER2;
		    String to3 = adminUser3Value;//ADMIN_USER3;
		    String from = adminUser1Value;//ADMIN_USER1;
		    String host = MAIL_HOST;
		
		    // Create properties, get Session
		    Properties props = new Properties();
		
		    props.put("mail.smtp.host", host);
		    props.put("mail.debug", "true");
		    Session session = Session.getInstance(props);
	
	        Message msg1 = new MimeMessage(session);
	        msg1.setFrom(new InternetAddress(from));
	        InternetAddress[] address = {new InternetAddress(to1)};
	        InternetAddress[] addresses = {new InternetAddress(to1), new InternetAddress(to2), new InternetAddress(to3)};
	        if (print)
	        	msg1.setRecipients(Message.RecipientType.TO, address);
	        else
	        	msg1.setRecipients(Message.RecipientType.TO, addresses);
	        //if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.ALGAE)) 
	        //	msg1.setSubject("SABC - "+msg);
	        //else
	        	msg1.setSubject("ALGAE - "+msg);
	        msg1.setSentDate(new Date());
	        
	        // create and fill the first message part
	        MimeBodyPart mbp1 = new MimeBodyPart();
	        //if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.ALGAE)) 
	        //	mbp1.setText("This is a notification sent from SABC.\n" + msg);
	        //else
	        	mbp1.setText("This is a notification sent from ALGAE.\n" + msg);

	        // create the second message part
	        MimeBodyPart mbp2 = null;

	        // attach the file to the message
	        if (filename != null && !filename.isEmpty()) {
	        	mbp2 = new MimeBodyPart();
		        FileDataSource fds = new FileDataSource(filename);
		        mbp2.setDataHandler(new DataHandler(fds));
		        mbp2.setFileName(fds.getName());
	        }

	        // create the Multipart and add its parts to it
	        Multipart mp = new MimeMultipart();
	        mp.addBodyPart(mbp1);
	        if (mbp2 != null)
	        	mp.addBodyPart(mbp2);

	        // add the Multipart to the message
	        msg1.setContent(mp);

	        //Send the message
	        Transport t = session.getTransport("smtp");
	        try {
	        	DataEncryption de = new DataEncryption();
	        	String unc = de.decrypt(emailPassValue);
	            t.connect(emailUserValue, unc);
	            t.sendMessage(msg1, msg1.getAllRecipients());
	        } catch (Exception e1) {
	        	log.warning("caught: "+e1.getMessage());
	        	log.warning("address1="+to1+" ;address2="+to2+"; address3="+to3);
	        	log.warning(SecurityServiceImpl.getStackTrace(e1));
	        } finally {
	            t.close();
	        }
	    } catch (MessagingException mex) {
			log.warning(mex.getMessage());
		} catch (Exception e) {
			log.warning(e.getMessage());
		}		
	}
    
	public boolean sendmail(String msg, String filename, String to1) {
		boolean ret = false;
		//log.info("in sendmail");
		//System.out.println("in sendmail");
	    try {
	    	getProperties();
		    String to2 = adminUser1Value;
		    String from = adminUser1Value;
		    String host = emailHostValue;//"mailgate1.nrel.gov";
		
		    // Create properties, get Session
		    Properties props = new Properties();
		
		    props.put("mail.smtp.host", host);
		    //props.put("mail.debug", "true");
		    Session session = Session.getInstance(props);
		    session.setDebug(false);
	
	        Message msg1 = new MimeMessage(session);
	        msg1.setFrom(new InternetAddress(from));
	        InternetAddress[] address = {new InternetAddress(to1), new InternetAddress(to2)};
	        msg1.setRecipients(Message.RecipientType.TO, address);
	        //if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.ALGAE)) 
	        //	msg1.setSubject("SABC - "+msg);
	        //else
	        	msg1.setSubject("ALGAE - "+msg);
	        msg1.setSentDate(new Date());
	        
	        // create and fill the first message part
	        MimeBodyPart mbp1 = new MimeBodyPart();
	        //if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.ALGAE)) 
	        //	mbp1.setText("This is a notification sent from SABC.\n" + msg);
	        //else
	        	mbp1.setText("This is a notification sent from ALGAE.\n" + msg);

	        // create the second message part
	        MimeBodyPart mbp2 = null;

	        // attach the file to the message
	        if (filename != null && !filename.isEmpty()) {
	        	mbp2 = new MimeBodyPart();
		        FileDataSource fds = new FileDataSource(filename);
		        mbp2.setDataHandler(new DataHandler(fds));
		        mbp2.setFileName(fds.getName());
	        }

	        // create the Multipart and add its parts to it
	        Multipart mp = new MimeMultipart();
	        mp.addBodyPart(mbp1);
	        if (mbp2 != null)
	        	mp.addBodyPart(mbp2);

	        // add the Multipart to the message
	        msg1.setContent(mp);

	        //Send the message
	        Transport t = session.getTransport("smtp");
	        try {
	        	DataEncryption de = new DataEncryption();
	        	String unc = de.decrypt(emailPassValue);
	            t.connect(emailUserValue, unc);
	            t.sendMessage(msg1, msg1.getAllRecipients());
	            ret = true;
	        } catch (Exception e1) {
	        	ret = false;
	        	log.warning("caught: "+e1.getMessage());
	        	log.warning("address1="+to1+" ;address2="+to2+"; from="+from);
	        	log.warning(SecurityServiceImpl.getStackTrace(e1));
	        } finally {
	            t.close();
	        }
	    } catch (MessagingException mex) {
	    	ret = false;
			log.warning(mex.getMessage());
		} catch (Exception e) {
			log.warning(e.getMessage());
			ret = false;
		}		
		return ret;
	}
    
    public void audit(String sessionId, String activity) {
		//log.info("in audit");
    	String userId = getUserIdFromId(sessionId);
		DBUtils db = new DBUtils();
    	String sql = "insert into sec_audit (userId,activity,activityDate)";
     	sql += " values ('"+userId+"','"+activity+"',CURRENT_TIMESTAMP)";
     	//log.info("audit sql="+sql);
		db.performInsert(sql);
		db.close();
    }

    public void auditByUser(String userId, String activity) {
		//log.info("in audit");
		DBUtils db = new DBUtils();
    	String sql = "insert into sec_audit (userId,activity,activityDate)";
     	sql += " values ('"+userId+"','"+activity+"',CURRENT_TIMESTAMP)";
     	//log.info("audit sql="+sql);
		db.performInsert(sql);
		db.close();
    }

    public String logon(String userId, String password) {//, String id) {
		//log.info("in 2-arg logon");
    	String ret = "";
		boolean auth = false;
		log.info("security_mode="+security_mode);
        if (security_mode.equals(ACTIVE_DIRECTORY_MODE))
        	auth = isAuthorizedThruAD(userId, password);
        else if (security_mode.equals(DB_MODE)) {
        	auth = isAuthorized(userId, password);
            if (auth) {
            	List<gov.nrel.nbc.security.model.Users> users = getUsers();
                Users user = get(users,userId);
                String storedPass = user.getPassword();
                String decodedPass = DataEncryption.getInstance().decrypt(storedPass);
            	auth = decodedPass.equals(password);
            }
        } else if (security_mode.equals(NO_MODE))
        	auth = true;
        if (auth) {
			String id = getSessionId(userId);
			if (id != null && !id.isEmpty()) {
				//log.info("session id="+id);
				ret = id;//String.valueOf(new GenerateId().getRandomNumber());
				Users user = getJDBCUser(userId);
				DBUtils db = new DBUtils();
				String sql = "insert into sec_logins(login_id,user_pk) values ('"
						   + id+"',"+user.getPk()+")";
				int num = db.performInsert(sql);
				if (num == 0) ret = null;
				db.close();
				audit(id,"logon");
			}
		}
		//log.info("sending email");
		//System.out.println("sending mail");
		//sendmail(userId+" logged on",null);//"/tmp/MVS-001.pdf");
		return ret;
    }

    private Users get(List<Users> users, String username) {
    	if (username == null) return null;
    	if (username.isEmpty()) return null;
    	Users user = null;
        Iterator<Users> uit = users.iterator();
        while (uit.hasNext() && user == null) {
        	Users u = uit.next();
        	if (u != null && u.getUserId() != null && !u.getUserId().isEmpty()) {
	        	if (u.getUserId().equals(username)) {
	        		user = u;
	        	}
        	}
        }
    	return user;
    }
    
	public String logon(String username, String password, String id) {
		//log.info("in 3-arg logon");
		boolean auth = false;
		log.info("security_mode="+security_mode);
        if (security_mode.equals(ACTIVE_DIRECTORY_MODE))
        	auth = isAuthorizedThruAD(username, password);
        else if (security_mode.equals(DB_MODE)) {
        	auth = isAuthorized(username, password);
            if (auth) {
            	List<gov.nrel.nbc.security.model.Users> users = getUsers();
                Users user = get(users,username);
                String storedPass = user.getPassword();
                String decodedPass = DataEncryption.getInstance().decrypt(storedPass);
            	auth = decodedPass.equals(password);
            }
        } else if (security_mode.equals(NO_MODE))
        	auth = true;
        if (auth) {
			if (id != null && !id.isEmpty()) {
				//log.info("session id="+id);
				Users user = getJDBCUser(username);
				DBUtils db = new DBUtils();
				List<String> groups = getGroupsForUserId(username);
				String group = null;
				long grpPk = 2;// initialized to SABC
				if (groups != null && !groups.isEmpty()) {
					group = groups.get(0);// TODO - group in login
					if (group != null && !group.isEmpty()) {
						grpPk = getGroupPkFromGroupName(group);
					}
				}
				String sql = "delete from sec_logins where user_pk="+user.getPk();
				db.performUpdate(sql);
				//log.info("got group="+grpPk+" for user="+username);
				sql = "insert into sec_logins(login_id,user_pk,group_pk) values ('"
						   + id+"',"+user.getPk()+","+grpPk+")";
				int num = db.performInsert(sql);
				if (num == 0) id = null;
				db.close();
				setModifiedTime(id);
				audit(id,"login");
			}
		}
		//log.info("sending email");
		//System.out.println("sending email");
		//sendmail(username+" logged on",null);//"/tmp/MVS-001.pdf");
		
		return id;
	}

    public void logoff(String id) {
    	//log.info("in logoff");
    	//System.out.println("in logoff");
  	 	DBUtils db = new DBUtils();
		if (isLoggedIn(id)&&!id.equals(JUNIT_SESSION_ID)) {
			audit(id,"logoff");
	 	 	String sql = "delete from sec_logins where login_id = '"+id+"'";
	 	 	//int ret = 
	 	 	db.performDelete(sql);
	 	 	//log.info("returned "+ret+" from delete");
		}	
		db.close();
    }

    public void logoffByUserId(String userId) {
    	DBUtils db = new DBUtils();
 		Users user = getJDBCUser(userId);
 		long userPk = user.getPk();	
		String sql = "select login_id from sec_logins where user_pk = "+userPk;
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			String id = db.getStringColumn(1);
			if (isLoggedIn(id)) {
				logoff(id);
			}
		}
		db.close();	
    }

	/**
	 * Method to check authorization
	 * 
	 * @param String userId the userId of the user
	 * @param String role the old role
	 * @param String group the old group
	 */
	public Integer removeUserFromRoleAndGroup(String id, String role, String group) {
		int ret = 0;
		DBUtils db = new DBUtils();
		String sql = "delete from sec_user_role_group_mapping where ";
		
		String userId = getUserIdFromId(id);
		if (userId != null && !userId.isEmpty()) {
			Users u = getJDBCUser(userId);
			Groups g = getJDBCGroup(group);
			Roles r = getJDBCRole(role);
			if (u != null && r != null && g != null) {
				sql += " user_pk="+u.getPk() + " and role_pk=" + r.getPk() + " and group_pk=" + g.getPk() + ")";
				ret = db.performDelete(sql);
				db.close();
			} else {
				if (u == null)
					log.warning("user "+userId+" not found");
				else if (r == null)
					log.warning("role "+role+" not found");
				else
					log.warning("group "+group+" not found");				
			}
		}
        return ret;
 	}
	
	/**
	 * Method to get all permissions
	 * 
	 * @return List<Permissions> the list of permissions
	 */
	public List<Permissions> getPermissions() {
		List<Permissions> permissions = new ArrayList<Permissions>();
		String sql = "select permission_pk, permission_name, permission_description from sec_permissions";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		while (db.getNextRow()) {
			long pk = db.getLongColumn(1);
			String name = db.getStringColumn(2);
			String desc = db.getStringColumn(3);
			Permissions p = new Permissions();
			p.setPk(pk);
			p.setName(name);
			p.setDescription(desc);
			permissions.add(p);
		}
		db.close();
    	return permissions;
		
	}

	/**
	 * Method to get all groups
	 * 
	 * @return List<Groups> the list of groups
	 */
	public List<Groups> getGroups() {
		List<Groups> groups = new ArrayList<Groups>();
		String sql = "select group_pk, group_name, group_description, group_type from sec_groups";
		DBUtils db = new DBUtils();
		db.performQuery(sql, false);
		while (db.getNextRow()) {
			long pk = db.getLongColumn(1);
			String name = db.getStringColumn(2);
			String desc = db.getStringColumn(3);
			long type = db.getLongColumn(4);
			Groups grp = new Groups();
			grp.setPk(pk);
			grp.setName(name);
			grp.setDescription(desc);
			grp.setGroup_type(type);
			groups.add(grp);
		}
		db.close();
    	return groups;
		
	}

	/**
	 * Method to get all users
	 * 
	 * @return List<Users> the list of users
	 */
	public List<Users> getUsers() {
		List<Users> users = new ArrayList<Users>();
		DBUtils db = new DBUtils();
		String sql = "select user_id from sec_users";
		db.performQuery(sql, false);
		while (db.getNextRow()) {
			String userId = db.getStringColumn(1);
			Users user = getJDBCUser(userId);
			users.add(user);
		}
		db.close();
    	return users;
		
	}
	public List<Users> getJDBCUsers() {
		List<Users> users = new ArrayList<Users>();
		DBUtils db = new DBUtils();
		db.performQuery("select user_id from sec_users", false);
		while (db.getNextRow()) {
			String val = db.getStringColumn(1);
			Users user = new Users();
			user.setUserId(val);
			users.add(user);
		}
		db.close();
    	return users;
	}
	
	public Users getJDBCUser(String userId) {
		Users user = null;
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select `user_pk`,`user_last_name`,"
				       + "`user_first_name`,`domain`,"
				       + "`user_password`,`user_email`,`enabled`,"
				       + "`last_used` from sec_users where user_id='"+userId+"'";
			db.performQuery(sql, false);
			if (db.getNextRow()) {
				long pk = db.getLongColumn(1);
				String lname = db.getStringColumn(2);
				String fname = db.getStringColumn(3);
				String domain = db.getStringColumn(4);
				String pass = db.getStringColumn(5);
				String email = db.getStringColumn(6);
				boolean enabled = db.getBooleanColumn(7);
				Date last = db.getDateColumn(8);
				user = new Users();
				user.setLast(lname);
				user.setFirst(fname);
				user.setDomain(domain);
				user.setPassword(pass);
				user.setEmail(email);
				user.setPk(pk);
				user.setEnabled(enabled);
				user.setLastUsed(last);
				user.setUserId(userId);
			}
			db.close();
		}
    	return user;
	}
	
	public String getUserDomain(String userId) {
		String domain = "";
		if (userId != null && !userId.isEmpty()) {
			DBUtils db = new DBUtils();
			String sql = "select `domain` from sec_users where user_id='"+userId+"'";
			db.performQuery(sql, false);
			if (db.getNextRow()) {
				domain = db.getStringColumn(1);
			}
			db.close();
		}
    	return domain;
	}
	
	public Users getJDBCUser(long pk) {
		Users user = null;
		if (pk != 0) {
			DBUtils db = new DBUtils();
			String sql = "select `user_id`,`user_last_name`,"
				       + "`user_first_name`,`domain`,"
				       + "`user_password`,`user_email`,`user_printer`,"
				       + "`last_used`, `enabled` from sec_users where user_pk="+pk;
			db.performQuery(sql, false);
			if (db.getNextRow()) {
				String user_id = db.getStringColumn(1);
				String lname = db.getStringColumn(2);
				String fname = db.getStringColumn(3);
				String domain = db.getStringColumn(4);
				String pass = db.getStringColumn(5);
				String email = db.getStringColumn(6);
				int printer_id = db.getIntColumn(7);
				Date last = db.getDateColumn(8);
				boolean enabled = db.getBooleanColumn(9);
				user = new Users();
				user.setUserId(user_id);
				user.setLast(lname);
				user.setFirst(fname);
				user.setDomain(domain);
				user.setPassword(pass);
				user.setEmail(email);
				user.setPk(pk);
				user.setEnabled(enabled);
				user.setLastUsed(last);
				switch (printer_id) {
					case NO_PRINTER_ID: user.setPrinter(NO_PRINTER);break;
					case FTLB_140_PRINTER_ID: user.setPrinter(FTLB_140_PRINTER);break;
					case AFUF_205_PRINTER_ID: user.setPrinter(AFUF_205_PRINTER);break;
					case FTLB_OTHER_ID: user.setPrinter(FTLB_OTHER);break;
					case AFUF_OTHER_ID: user.setPrinter(AFUF_OTHER);break;
				}
			}
			db.close();
		}
    	return user;
	}
	
	public Groups getJDBCGroup(String group) {
		Groups g = null;
		if (group != null && !group.isEmpty()) {
			DBUtils db = new DBUtils();
			db.performQuery("select group_pk,group_type from sec_groups where group_name='"+group+"'", false);
			if (db.getNextRow()) {
				long pk = db.getLongColumn(1);
				long type = db.getLongColumn(2);
				g = new Groups();
				g.setName(group);
				g.setPk(pk);
				g.setGroup_type(type);
			}
			db.close();
		}
    	return g;
	}
	
	public Roles getJDBCRole(String role) {
		Roles r = null;
		if (role != null && !role.isEmpty()) {
			DBUtils db = new DBUtils();
			db.performQuery("select role_pk,role_desciption from sec_roles where role_name='"+role+"'", false);
			if (db.getNextRow()) {
				long pk = db.getLongColumn(1);
				String desc = db.getStringColumn(2);
				r = new Roles();
				r.setName(role);
				r.setPk(pk);
				r.setDescription(desc);
			}
			db.close();
		}
    	return r;
	}
	
	public Groups getJDBCGroup(long pk) {
		Groups g = null;
		if (pk != 0) {
			DBUtils db = new DBUtils();
			db.performQuery("select group_name,group_type from sec_groups where group_pk="+pk, false);
			if (db.getNextRow()) {
				String name = db.getStringColumn(1);
				long type = db.getLongColumn(2);
				g = new Groups();
				g.setName(name);
				g.setPk(pk);
				g.setGroup_type(type);
			}
			db.close();
		}
    	return g;
	}
	
	public GroupTypes getJDBCGroupType(String group) {
		GroupTypes gt = null;
		if (group != null && !group.isEmpty()) {
			Groups g = getJDBCGroup(group);
			if (g != null) {
				DBUtils db = new DBUtils();
				db.performQuery("select type_pk,type_name from sec_group_types where type_pk="+g.getGroup_type(), false);
				if (db.getNextRow()) {
					long pk = db.getLongColumn(1);
					String name = db.getStringColumn(2);
					gt = new GroupTypes();
					gt.setName(name);
					gt.setPk(pk);
				}
				db.close();
			}
		}
    	return gt;
	}
	
	public List<Groups> getJDBCGroups() {
		List<Groups> groups = new ArrayList<Groups>();
		DBUtils db = new DBUtils();
		db.performQuery("select group_name from sec_groups", false);
		while (db.getNextRow()) {
			String val = db.getStringColumn(1);
			Groups g = new Groups();
			g.setName(val);
			groups.add(g);
		}
		db.close();
    	return groups;
	}
	
	public List<Permissions> getJDBCPermissions() {
		List<Permissions> permissions = new ArrayList<Permissions>();
		DBUtils db = new DBUtils();
		db.performQuery("select permissions_name from sec_permissions", false);
		while (db.getNextRow()) {
			String val = db.getStringColumn(1);
			Permissions p = new Permissions();
			p.setName(val);
			permissions.add(p);
		}
		db.close();
    	return permissions;
	}
	
	/**
	 * Public static method to return a stack trace
	 * 
	 * @param t
	 * @return <String> stack track
	 */
	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

	public void changePassword(long pk, String newPass) {
		Users user = null;
		user = getJDBCUser(pk);
		if (user != null) {
			String newPWD = DataEncryption.getInstance().encrypt(newPass);
			user.setPassword(newPWD);
			DBUtils db = new DBUtils();
			String sql = "update sec_users set user_password='"+newPWD+"'";
			sql += " where user_pk="+user.getPk();
			int ret = db.performUpdate(sql);
			if (ret == 0)
				log.warning("update failed");
			db.close();
		}
	}

	public static SecurityServiceImpl getInstance() {
		return new SecurityServiceImpl();
	}

	public long getUserPk(String id) {
		long ret = 0;
		DBUtils db = new DBUtils();
		String sql = "select user_pk from sec_logins where login_id='"+id+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			ret = db.getLongColumn(1);
		}
		db.close();
		return ret;
	}
	
	public String getUserEmail(String id) {
		String email = null;
		long pk = getUserPk(id);
		Users user = getJDBCUser(pk);
		email = user.getEmail();
		return email;
	}

	public String getUserPrinter(String id) {
		if (id == null || id.isEmpty()) return null;
		String printer = null;
		long pk = getUserPk(id);
		Users user = getJDBCUser(pk);
		if (user != null)
			printer = user.getPrinter();
		else
			printer = "15747s_18200s";
		log.info("got printer = "+printer);
		return printer;
	}

	public String getUserPrinterPermission(String id) {
		if (id == null || id.isEmpty()) return null;
		String printer = null;
		long pk = getUserPk(id);
		Users user = getJDBCUser(pk);
		if (user != null)
			printer = user.getPrinter();
		else
			printer = "0";
		log.info("got printer = "+printer);
		return printer;
	}

	public String getSessionId(String userId) {
		String id = "";
		DBUtils db = new DBUtils();
		long pk = getUserPkFromUserId(userId);
		String sql = "select login_id from sec_logins where user_pk = "+pk;
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			id = db.getStringColumn(1);
		}
		db.close();
		//log.info("session id="+id);
		return id;
	}

	public String getSessionId() {
		String id = "";
		DBUtils db = new DBUtils();
		String sql = "select login_id from sec_logins where login_pk in (select MAX(login_pk) from sec_logins)";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			id = db.getStringColumn(1);
		}
		db.close();
		//log.info("session id="+id);
		return id;
	}
	
	//@Override
	public void checkPermutationStrongName() throws SecurityException {
		
		return;
	}

	public void addClient(String client, String guid) {
		String sql = "insert into sec_guids (guid,client) values ('"+guid+"','"+client+"')";
		DBUtils db = new DBUtils();
		int rows = db.performInsert(sql);
		log.info("rows inserted="+rows);
		db.close();
	}

	public void addClient(String client) {
		GenerateGUID gg = new GenerateGUID(client);
		String guid = gg.generateGUID();
		String sql = "insert into sec_guids (guid,client) values ('"+guid+"','"+client+"')";
		DBUtils db = new DBUtils();
		int rows = db.performInsert(sql);
		log.info("rows inserted="+rows);
		db.close();
	}

	public String getClientByGuid(String guid) {
		String id = "";
		DBUtils db = new DBUtils();
		String sql = "select client from sec_guids where guid='"+guid+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			id = db.getStringColumn(1);
		}
		db.close();
		//log.info("session id="+id);
		return id;
	}
	
	public String getGuidForClient(String client) {
		String id = "";
		DBUtils db = new DBUtils();
		String sql = "select guid from sec_guids where client='"+client+"'";
		db.performQuery(sql, false);
		if (db.getNextRow()) {
			id = db.getStringColumn(1);
		}
		db.close();
		//log.info("session id="+id);
		return id;
	}
}
