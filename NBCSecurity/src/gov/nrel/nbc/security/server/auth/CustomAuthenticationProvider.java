package gov.nrel.nbc.security.server.auth;

import gov.nrel.nbc.security.client.AppConstants;
import gov.nrel.nbc.security.client.StaticUserBean;
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.server.LoginAudit;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import gov.nrel.nbc.security.utils.XLogger;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
 
public class CustomAuthenticationProvider extends CustomAuthenticationProcessingFilter implements AuthenticationProvider, AppConstants {
     
	private static final long serialVersionUID = -8932540422420509883L;
    private static final XLogger log = new XLogger(XLogger.INFO);
	private static final String BADCHARS = "<>/?`;";
	private static final String SYMBOLS = "~!@#$%^&*()_+=-,./*-|}{[];:";
	private static final String NUMBERS = "0123456789";
	private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int PASSWORD_LENGTH_MIN = 6;
	private static final int PASSWORD_LENGTH_MAX = 15;
	private String password2 = null;
	private String firstName = null;
	private String lastName = null;
	private String email = null;
	private String hidden = null;
	private String passline = null;
	private String random = null;
	private HttpServletResponse res = null;
     
    public CustomAuthenticationProvider() {    	
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
    
    //@Override
    public Authentication authenticate(Authentication authentication) //throws AuthenticationException 
    {         
    	SecurityServiceImpl ssi = new SecurityServiceImpl();
    	List<gov.nrel.nbc.security.model.Users> users = ssi.getUsers();
    	
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        
        password2 = StaticUserBean.getPassword2();
        firstName = StaticUserBean.getFirst();
        lastName = StaticUserBean.getLast();
        email = StaticUserBean.getEmail();
        hidden = StaticUserBean.getHidden();
        passline = StaticUserBean.getPassline();
        random = StaticUserBean.getRandom();
        
        Authentication customAuthentication =
            new CustomUserAuthentication("ROLE_USER", authentication);
        
		ssi.auditByUser(username,"login attempt");
        Users user = get(users,username);
        if (user == null) {
             if (hidden != null && hidden.equals("add")) {
	             if (ssi.security_mode.equals(DB_MODE)) {	
	    	       	if (!isPasswordStrong(password)) {
	    		    	try {
	    		    		hidden = "failed";
	    		            StaticUserBean.setMessage("Password is weak.");
	    		            if (res != null) res.sendRedirect("/addNewUser.jsp?weakpassword=true");
	    		    		else throw new BadCredentialsException("Weak password.");
	    		    	} catch (Exception e1) {
	    		    		log.warning(e1.getMessage());
	    		           	throw new BadCredentialsException("Weak password.");
	    		    	}
	    		    }
	    		    if (!password.equals(password2)) {
	    		    	try {
	    		    		hidden = "failed";
	    		    		StaticUserBean.setMessage("Passwords don't match");
	    		    		if (res != null)res.sendRedirect("/addNewUser.jsp?nomatch=true");
	    		    		else throw new BadCredentialsException("Passwords don't match.");
	    		    	} catch (Exception e1) {
	    		    		log.warning(e1.getMessage());
	    		           	throw new BadCredentialsException("Passwords don't match.");
	    		    	}
	    		    }
	            }
	        	Users u = new Users();
	        	if (!isValid(username)) {
	        		StaticUserBean.setMessage("Bad characters found in username");
	        		throw new BadCredentialsException("Bad characters in username");
	        	}
	        	u.setUserId(username);
	        	u.setPassword(DataEncryption.getInstance().encrypt(password));
	        	if (!isValid(email)) {
	        		StaticUserBean.setMessage("Bad characters found in email");
	        		throw new BadCredentialsException("Bad characters in email");
	        	}
	        	if (!isEmailValid(email)) {
	        		StaticUserBean.setMessage("Invalid email address given.");
	        		throw new BadCredentialsException("Invalid email address given");
	        	}
	        	u.setEmail(email);
	        	if (!isValid(firstName)) {
	        		StaticUserBean.setMessage("Bad characters found in first name");
	        		throw new BadCredentialsException("Bad characters in first name");
	        	}
	        	u.setFirst(firstName);
	        	if (!isValid(lastName)) {
	        		StaticUserBean.setMessage("Bad characters found in last name");
	        		throw new BadCredentialsException("Bad characters in last name");
	        	}
	        	u.setLast(lastName);
	        	//log.info("passline="+passline);
	        	//log.info("random="+random);
	        	if (!isValid(passline)) {
	        		StaticUserBean.setMessage("Bad characters found in field.");
	        		throw new BadCredentialsException("Bad characters in field.");	        		
	        	}
	        	if (!random.startsWith(passline)) {
	        		StaticUserBean.setMessage("Security characters do not match");
	        		throw new BadCredentialsException("Security characters do not match");	        		
	        	}
	        	ssi.addUser(u, true);
	        	StaticUserBean.setMessage("New user added. An email has been sent to the administrator for validation.");
        		try {
        			if (res != null) res.sendRedirect("/notification.jsp?newuser=true");
        			else throw new BadCredentialsException("New user added. An email has been sent to the administrator for validation.");
        		} catch (Exception e1) {
        			log.warning(e1.getMessage());
        		}
            } else if (hidden != null && hidden.equals("login")) {
            		try {
            			StaticUserBean.setMessage("");
            			if (res != null) res.sendRedirect("/addNewUser.jsp");
            		} catch (Exception e1) {
            			log.warning(e1.getMessage());
            		}
            } else {
        		try {
        			if (res != null) res.sendRedirect("/addNewUser.jsp");
        		} catch (Exception e1) {
        			log.warning(e1.getMessage());
        			log.warning(SecurityServiceImpl.getStackTrace(e1));
        		}            	
            }
        	throw new BadCredentialsException("User not found");//UsernameNotFoundException("User not found");
        }
        
        if (user == null) {
        	log.info("user is NULL");
    		try {
    			if (res != null) res.sendRedirect("/addNewUser.jsp");
    		} catch (Exception e1) {
    			log.info("in exception");
    			log.warning(e1.getMessage());
    			log.warning(SecurityServiceImpl.getStackTrace(e1));
    		}            	
        }
         
    	if (!ssi.isValidated(username)) {
    		StaticUserBean.setMessage("Login Failed.");
    		throw new BadCredentialsException("Account has not been validated.");
    	}
    	
        LoginAudit la = LoginAudit.getInstance();
    	if (la.notLocked(user)) {    
    		boolean auth = false;
    		log.info("security_mode="+ssi.security_mode);
	        if (ssi.security_mode.equals(ACTIVE_DIRECTORY_MODE))
	        	auth = ssi.isAuthorizedThruAD(username, password);
	        else if (ssi.security_mode.equals(DB_MODE)) {
	        	auth = ssi.isAuthorized(username, password);
	            if (auth) { 
	                //log.info("past user test");
	                String storedPass = user.getPassword();
	                //log.info("pass="+storedPass);
	                String decodedPass = DataEncryption.getInstance().decrypt(storedPass);
	                 
	            	auth = decodedPass.equals(password);
	            }
	        } else if (ssi.security_mode.equals(NO_MODE))
	        	auth = true;
	        if (!auth) {
        		la.incrementCount(user);
        		if (la.multipleOf3(user)) {
	           		if (!la.isMajor(user)) {
	            		StaticUserBean.setMessage("Login Failed.");
	           			throw new BadCredentialsException("Account is locked. It will be unlocked in 15 minutes.");
	           		}
	           		else {
	            		StaticUserBean.setMessage("Login Failed.");
	           			throw new BadCredentialsException("Account is locked. Please see the administrator \nto unlock your account.");
	           		}
	        	} else if (la.isMajor(user)) {
	        		StaticUserBean.setMessage("Login Failed.");
           			throw new BadCredentialsException("Account is locked. Please see the administrator \nto unlock your account.");	        		
	        	}
        		StaticUserBean.setMessage("Login Failed.");
	            throw new BadCredentialsException("Invalid password");
	        } else {
	        	la.resetCount(user);
	        }
    	} else {
       		if (!la.isMajor(user)) {
        		StaticUserBean.setMessage("Login Failed.");
       			throw new BadCredentialsException("Account is locked. It will be unlocked in 15 minutes.");
       		}
       		else {
        		StaticUserBean.setMessage("Login Failed.");
       			throw new BadCredentialsException("Account is locked. Please see the administrator \nto unlock your account.");
       		}
    	}
        
        customAuthentication.setAuthenticated(true);
        
        String id = "";
        StringBuilder sb = new StringBuilder();
        sb.append(customAuthentication.getDetails());
        String sbstr = sb.toString();
        int index = sbstr.lastIndexOf("SessionId: ");
        if (index != -1) {
        	index += "SessionId: ".length();
        	id = sbstr.substring(index);
        }
		//log.info("session id="+id);
        log.info("after authentication, id="+id);
        String ret = ssi.logon(username, password, id);
        log.info("after logon, id="+ret);
         
        return customAuthentication;
         
    }
 
    //@Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
 
    private boolean isValid(String data) {
    	  if (data == null) return false;
    	  if (data.isEmpty()) return false;
  		  for (int i=0;i<data.length();i++)
  		  {
  			  CharSequence cs = String.valueOf(data.charAt(i));
  			  if (BADCHARS.contains(cs)) {
  				  System.out.println(data+" contains invalid characters");
  				  return false;
  			  }
  		  }

        return true;
     }
    
    private boolean isEmailValid(String emailAddress) {
    	if (emailAddress == null) return false;
    	if (emailAddress.isEmpty()) return false;
        // a null string is invalid
        if ( emailAddress == null )
          return false;

        // a string without a "@" is an invalid email address
        if ( emailAddress.indexOf("@") < 0 )
          return false;

        // a string without a "."  is an invalid email address
        if ( emailAddress.indexOf(".") < 0 )
          return false;

        if ( lastEmailFieldTwoCharsOrMore(emailAddress) == false )
          return false;

        try
        {
          InternetAddress internetAddress = new InternetAddress(emailAddress);
          //log.info("email="+
          internetAddress.getAddress();
          return true;
        }
        catch (AddressException ae)
        {
    	  // log exception
          return false;
        }
    }
    
    /**
     * Returns true if the last email field (i.e., the country code, or something
     * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
     * must be to be legal.
     */
    private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress)
    {
  	  if (emailAddress == null) return false;
  	  if (emailAddress.isEmpty()) return false;
      StringTokenizer st = new StringTokenizer(emailAddress,".");
      String lastToken = null;
      while ( st.hasMoreTokens() )
      {
        lastToken = st.nextToken();
      }

      if ( lastToken.length() >= 2 )
      {
        return true;
      }
      else
      {
        return false;
      }
    }

    protected boolean isPasswordStrong(String password) {
    	if (password == null) return false;
    	if (password.isEmpty()) return false;
       	boolean ret = true;
       	if (password.length()<PASSWORD_LENGTH_MIN) return false;
       	if (password.length()>PASSWORD_LENGTH_MAX) return false;
       	boolean found = false;
       	for (int i=0;i<password.length();i++) {
       		CharSequence cs = String.valueOf(password.charAt(i));
    		    if (SYMBOLS.contains(cs)) {
    			    found = true;
    			    break;
    			}
       	}
       	if (!found) {
    		    log.warning(password+" does not contain symbols");
    		    return false;
       	}
       	found = false;
       	for (int i=0;i<password.length();i++) {
       		CharSequence cs = String.valueOf(password.charAt(i));
    		    if (NUMBERS.contains(cs)) {
    			    found = true;
    			    break;
    			}
       	}
    		if (!found) {
    		    log.warning(password+" does not contain numbers");
    		    return false;
    		}
    		found = false;
       	for (int i=0;i<password.length();i++) {
       		CharSequence cs = String.valueOf(password.charAt(i));
    		    if (LETTERS.contains(cs)) {
    			    found = true;
    			    break;
    			}
       	}
    		if (!found) {
    			log.warning(password+" does not contain letters");
    			return false;
    		}
    		found = false;
       	for (int i=0;i<password.length();i++) {
       		CharSequence cs = String.valueOf(password.charAt(i));
    		    if (CAPS.contains(cs)) {
    			    found = true;
    			    break;
    			}
       	}
    		if (!found) {
    			log.warning(password+" does not contain capital letters");
    			return false;
    		}
       	return ret;
       }
}