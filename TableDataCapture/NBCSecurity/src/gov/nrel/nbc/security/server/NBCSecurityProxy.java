package gov.nrel.nbc.security.server;
 
import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.utils.XLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.security.Principal;

import javax.ejb.EJBContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.ejb.EJBContext;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
//import org.jboss.security.*;
import org.jboss.security.SecurityProxy;

/**
 *
 * @author James W. Albersheim
 * The Security source module
 *
**/
public class NBCSecurityProxy extends NBCSecurityProxyServlet implements SecurityProxy {
	private static final long serialVersionUID = -4730874880186495483L;
    private static final XLogger log = new XLogger(XLogger.INFO);
    private SecurityServiceImpl service = null;

/* There is only one proxy created per container (i.e. not per service), so we use a ThreadLocal store the invocation context. **/

	@SuppressWarnings({ "unchecked", "unused" })
	private ThreadLocal _ctx = new ThreadLocal();

	/* we use another ThreadLocal to cache the service instance **/
	@SuppressWarnings({ "unchecked", "unused" })
	private ThreadLocal _service = new ThreadLocal();
	
	public NBCSecurityProxy() {
		super();
		log.info("in NBCSecurityProxy constructor");
	}

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    throws IOException, ServletException {
    	log.info("in NBCSecurityProxy.doGet");
    	String context = httpServletRequest.getContentType();
    	destinationUrl = getProxyURL(httpServletRequest);
        requestUrl = httpServletRequest.getRequestURL().toString();
        log.info("GET Request URL: " + requestUrl+"\n"+
              "Destination URL: " + destinationUrl);
        int reqcolon = requestUrl.lastIndexOf(":");
        String reqPort = requestUrl.substring(reqcolon+1,reqcolon+5);
        int destcolon = destinationUrl.lastIndexOf(":");
        String destPort = destinationUrl.substring(destcolon+1,destcolon+5);
        if (destPort.equals(reqPort)) {
        	log.info("same server");
        }
            GetMethod getMethodProxyRequest = new GetMethod(destinationUrl);
            String method = getMethodProxyRequest.getName();
            log.info("method="+method);
            HttpMethodParams params = getMethodProxyRequest.getParams();
            log.info("params="+params.toString());
            if (method.equals("GET")) {
            	log.info("its get");
            	httpServletResponse.setContentType(context);
            	PrintWriter pw = httpServletResponse.getWriter();
            	//String name = request.getParameter("username");
            	//String password = request.getParameter("password");
            	//if(name.equals("James")&& password.equals("abc"))
            	//{
            	log.info("going to tracker.html");
            	httpServletResponse.sendRedirect("/tracker/Tracker.html");
            	//}
            	//else
            	//{
            	//pw.println("u r not a valid user");
            	//}            
            }
            //invoke(getMethodProxyRequest, "SecurityServiceImpl", method, params);
            //httpServletResponse.sendRedirect(destinationUrl);
        //} else {
            //httpServletResponse.sendRedirect(requestUrl);
        	//super.doGet(httpServletRequest, httpServletResponse);
        //}
    }
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    throws IOException, ServletException {
    	log.info("in NBCSecurityProxy.doPost");
    	super.doPost(httpServletRequest, httpServletResponse);
    }
    @SuppressWarnings("unchecked")
	public void init(Class beanHome, Class beanRemote,
            Object securityMgr) throws InstantiationException {
    	log.info("init, beanHome="+beanHome
    			+ ", beanRemote="+beanRemote
    			+ ", securityMgr="+securityMgr);
    	// Get the echo method for equality testing in invoke
		try {
		   @SuppressWarnings("unused")
		   Class[] params = {String.class};
		   //echo = beanRemote.getDeclaredMethod("echo", params);
		} catch(Exception e) {
		   String msg = "Failed to finde an echo(String) method";
		   log.warning(msg);
		   SecurityServiceImpl.getStackTrace(e);
		   throw new InstantiationException(msg);
		}
    }

	public void setEJBContext(EJBContext ctx)
	{
		log.info("setEJBContext, ctx="+ctx);
	}

    /**
     * Initializes the SecurityProxy
     * @param serviceHome the home interface of the service
	 * @param serviceRemote the remove interface of the service
	 * @param securityMgr The security Manager of the service
	 * @throws InstantiationException if unable to instantiate
     **/
/*
	public void init(Class serviceHome, Class serviceRemote, Object securityMgr) throws InstantiationException {
		log.info("Security Proxy Initializing for " + serviceRemote.getName());
		if (service == null)
		{
			try
			{
			  service = new SecurityServiceImpl();
			}
			catch (Exception e) {
				log.warning("exception caught in addUSer: " + e.getMessage());
			}
		}
        if( (securityMgr instanceof SubjectSecurityManager) == false )
        {
            String msg = "myinit-3-SubjectSecurityProxy requires a SubjectSecurityManager"
                 + " instance, securityMgr=" + securityMgr;
            log.info(msg);
        }
 	}
*.
    /**
     * Initializes the SecurityProxy
     * @param serviceHome the home interface of the service
	 * @param serviceRemote the remove interface of the service
	 * @param serviceLocalHome the localHome interface of the service
	 * @param serviceLocal the local interface of the service
	 * @param securityMgr The security Manager of the service
	 * @throws InstantiationException if unable to instantiate
     **/
    @SuppressWarnings("unchecked")
	public void init(Class serviceHome, Class serviceRemote, Class serviceLocalHome, Class serviceLocal, Object securityMgr) throws InstantiationException {
		log.info("Security Proxy Initializing for " + serviceRemote.getName());
		log.info("in init-5");
		if (service == null)
		{
			try
			{
			  service = new SecurityServiceImpl();
			}
			catch (Exception e) {
				log.warning("exception caught in addUSer: " + e.getMessage());
			}
		}
	}
    /**
     * Initializes the SecurityProxy
     * @param serviceHome the home interface of the service
	 * @param serviceRemote the remove interface of the service
	 * @param serviceLocal the local interface of the service
	 * @param securityMgr The security Manager of the service
	 * @throws InstantiationException if unable to instantiate
     **/
    @SuppressWarnings("unchecked")
	public void init(Class serviceHome, Class serviceRemote, Class serviceLocal, Object securityMgr) throws InstantiationException {
		log.info("myinit-4-Security Proxy Initializing for " + serviceRemote.getName());
		log.info("in init-4");
		if (service == null)
		{
			try
			{
			  service = new SecurityServiceImpl();
			}
			catch (Exception e) {
				log.warning("exception caught in addUSer: " + e.getMessage());
			}
		}
	}

    /**
     * finds the method of a service
     * @param service_name name of the service
	 * @param operation The name of the method
     **/
	@SuppressWarnings("unchecked")
	public int findMethod(String service_name, String operation) {
		int ret = 0;
        try {
            Class c = Class.forName(service_name);
            Method m[] = c.getDeclaredMethods();
            boolean found = false;
            for (int i = 0; i < m.length && !found; i++) {
            	if (m[i].getName().equals(operation)) {
            		found = true;
            		ret = 1;
            	}
            }
         }
         catch (Throwable e) {
            System.err.println(e);
         }
 		 return ret;
	}

    /**
     * finds the method of a service
     * @param service_name name of the service
	 * @param operation The name of the method
     **/
	@SuppressWarnings("unchecked")
	public Method getMethod(String service_name, String operation) {
		Method ret = null;
        try {
            Class c = Class.forName(service_name);
            Method m[] = c.getDeclaredMethods();
            boolean found = false;
            for (int i = 0; i < m.length && !found; i++) {
            	if (m[i].getName().equals(operation)) {
            		found = true;
            		ret = m[i];
            	}
            }
         }
         catch (Throwable e) {
            System.err.println(e);
         }
 		 return ret;
	}

    /**
     * finds the method of a service
     * @param obj the service object
     * @param service_name name of the service
	 * @param operation The name of the method
	 * @param args the list of arguments for the operation with the id of the session as the first arg.
	 * @return Object the values returned from the method 
     **/
	@SuppressWarnings("unchecked")
	public Object invoke(Object obj, String service_name, String operation, Object[] args) {
		Object o = null;
        try {
            Class c = Class.forName(service_name);
            Method m[] = c.getDeclaredMethods();
            boolean found = false;
            for (int i = 0; i < m.length && !found; i++) {
            	if (m[i].getName().equals(operation)) {
            		found = true;
            		Method meth = m[i];
            		o = meth.invoke(obj, args);
            	}
            }
         }
         catch (Throwable e) {
            log.warning(SecurityServiceImpl.getStackTrace(e));
         }
         return o;
	}

    /**
     * Invokes the Proxy
     * @param m The method being invoked
	 * @param args The arguments being passed into the method
	 * @param service The service object begin invoked
	 * @throws SecurityException if the service instance is invalid or cannot be invoked by this user
     **/
	public void invoke(Method m, Object[] args, Object service) throws SecurityException {
		log.info("in my invoke"+" and # args="+args.length);
		log.info("method-"+m.toString());
		String operation = m.getName();
		int i=0;
		String id="";
		String service_name=service.toString();
		for (i=0;i<args.length ;i++ )
		{
			log.info("invoke args["+i+"]="+args[i]);
			//args[i] = "changed "+args[i];
		}
		if (args.length > 0)
		{
			log.info("First Argument for "+operation+" of "+service.toString()+" is "+args[0]);
			log.info("Last Argument for "+operation+" of "+service.toString()+" is "+args[args.length-1]);
			try
			{
			    id = ((String)args[args.length-1]);
			}
			catch (Exception nfe)
			{
				id = "";
			}
		}
		else
		{
			log.info(operation+" of "+service.toString()+" has no arguments");
		}
		//String sservice = service.toString();
		if (m.toString().indexOf("javax.ejb.EJBObject.remove()") != -1) {
			log.info("got wierd remove call...returning");return;
		}
		log.info("id="+id);
		//EJBContext ctx = (EJBContext)_ctx.get();
		String [] splits = service_name.split("[@]");
		if (splits.length > 1)
		{
			service_name = splits[splits.length-2];
		}
		else
		{
			service_name = service.toString();
		}
		Principal principal = null;
		try
		{
			//principal = ctx.getCallerPrincipal();
		}
		catch (Exception pe)
		{
			//log.warning("Exception while trying to get CallerPrincipal: "+pe.getMessage());
		}
		if (principal != null)
		{
			//log.info("principal NOT null");
		}
		else
		{
			//log.info("principal IS null");
		}
		
		log.info("service="+service.toString());
		log.info("service_name="+service_name);
		if (id != null && !id.isEmpty() && !operation.equals("invalidateUsers"))
		{
			int found=0;
			try
			{
				found = findMethod(service_name,operation);
			}
			catch (Exception ef)
			{
				log.info("exception caught when calling findMethod: "+ef.getMessage());
				found = 0;
			}
			if (found == 1)
			{
				if (operation.equals("changePassword"))
				{
					try
					{
						if (SecurityServiceImpl.getInstance().isLoggedIn(id))
						{
							int pk = new Integer((String)args[0]).intValue();
							if (SecurityServiceImpl.getInstance().getUserPk(id)==pk)
							{
								String currPassword = SecurityServiceImpl.getInstance().getJDBCUser(pk).getPassword();
								String oldPassword = (String)args[1];
								String encryptedOP = DataEncryption.getInstance().encrypt(oldPassword);
								if (currPassword.equals(encryptedOP))
								{
									String encryptedNP = DataEncryption.getInstance().encrypt((String)args[2]);
									SecurityServiceImpl.getInstance().changePassword(pk,encryptedNP);
								}
								else
								{
									throw new SecurityException("Incorrect old password");
								}
							}
							else
							{
								throw new SecurityException("This user not privileged to change another user's password");
							}
						}
						else
						{
							throw new SecurityException("User Not Logged In");
						}
					}
					catch (Exception all)
					{
						throw new SecurityException(all.getMessage());
					}
				}
				else {
					if (SecurityServiceImpl.getInstance().isLoggedIn(id)) {
						Object newArgs[] = new Object[args.length-1];
						for (int o=1;o<args.length;o++)
							newArgs[o-1] = args[o];
						invoke(service,service_name,operation,newArgs);
					}
					else
						throw new SecurityException("User Not Logged In");	
				}
			}
			else
			{
				log.warning("************WARNING*********");
				log.warning("SECURITY VIOLATION...service("+service_name+") and method ("+operation+") not registered");
				log.warning("************WARNING*********");
            }
		}
		if (service == null)
		{
			try
			{
			  service = new SecurityServiceImpl();
			}
			catch (Exception e) {
				log.warning("exception caught in invoke: " + e.getMessage());
			}
		}
		log.info("invoking method=" + operation + " from " + service_name);
	}

    /**
     * Invokes the Proxy
     * @param m The method being invoked
	 * @param args The arguments being passed into the method
	 * @param service The service object begin invoked
	 * @returns Object the return type of the method being called
	 * @throws SecurityException if the service instance is invalid or cannot be invoked by this user
     **/
	public Object invokeIt(Method m, Object[] args, Object service) throws SecurityException {
		log.info("in my invoke"+" and # args="+args.length);
		log.info("method-"+m.toString());
		String operation = m.getName();
		int i=0;
		String id="";
		String service_name=service.toString();
		for (i=0;i<args.length ;i++ )
		{
			log.info("invoke args["+i+"]="+args[i]);
			//args[i] = "changed "+args[i];
		}
		if (args.length > 0)
		{
			log.info("First Argument for "+operation+" of "+service.toString()+" is "+args[0]);
			log.info("Last Argument for "+operation+" of "+service.toString()+" is "+args[args.length-1]);
			try
			{
			    id = ((String)args[0]);
			}
			catch (Exception nfe)
			{
				id = "";
			}
		}
		else
		{
			log.info(operation+" of "+service.toString()+" has no arguments");
		}
		//String sservice = service.toString();
		if (m.toString().indexOf("javax.ejb.EJBObject.remove()") != -1) {
			log.info("got wierd remove call...returning");return null;
		}
		log.info("id="+id);
		//EJBContext ctx = (EJBContext)_ctx.get();
		String [] splits = service_name.split("[@]");
		if (splits.length > 1)
		{
			service_name = splits[splits.length-2];
		}
		else
		{
			service_name = service.toString();
		}
		Principal principal = null;
		try
		{
			//principal = ctx.getCallerPrincipal();
		}
		catch (Exception pe)
		{
			//log.warning("Exception while trying to get CallerPrincipal: "+pe.getMessage());
		}
		if (principal != null)
		{
			//log.info("principal NOT null");
		}
		else
		{
			//log.info("principal IS null");
		}
		
		log.info("service="+service.toString());
		log.info("service_name="+service_name);
		if (id != null && !id.isEmpty() && !operation.equals("invalidateUsers"))
		{
			int found=0;
			try
			{
				found = findMethod(service_name,operation);
			}
			catch (Exception ef)
			{
				log.info("exception caught when calling findMethod: "+ef.getMessage());
				found = 0;
			}
			if (found == 1)
			{
				if (operation.equals("changePassword"))
				{
					try
					{
						if (SecurityServiceImpl.getInstance().isLoggedIn(id))
						{
							int pk = new Integer((String)args[0]).intValue();
							if (SecurityServiceImpl.getInstance().getUserPk(id)==pk)
							{
								String currPassword = SecurityServiceImpl.getInstance().getJDBCUser(pk).getPassword();
								String oldPassword = (String)args[1];
								String encryptedOP = DataEncryption.getInstance().encrypt(oldPassword);
								if (currPassword.equals(encryptedOP))
								{
									String encryptedNP = DataEncryption.getInstance().encrypt((String)args[2]);
									SecurityServiceImpl.getInstance().changePassword(pk,encryptedNP);
								}
								else
								{
									throw new SecurityException("Incorrect old password");
								}
							}
							else
							{
								throw new SecurityException("This user not privileged to change another user's password");
							}
						}
						else
						{
							throw new SecurityException("User Not Logged In");
						}
					}
					catch (Exception all)
					{
						throw new SecurityException(all.getMessage());
					}
				}
				else {
					if (SecurityServiceImpl.getInstance().isLoggedIn(id)) {
						Object newArgs[] = new Object[args.length-1];
						for (int o=1;o<args.length;o++)
							newArgs[o-1] = args[o];
						return invoke(service,service_name,operation,newArgs);
					}
					else
						throw new SecurityException("User Not Logged In");	
				}
			}
			else
			{
				log.warning("************WARNING*********");
				log.warning("SECURITY VIOLATION...service("+service_name+") and method ("+operation+") not registered");
				log.warning("************WARNING*********");
            }
		}
		if (service == null)
		{
			try
			{
			  service = new SecurityServiceImpl();
			}
			catch (Exception e) {
				log.warning("exception caught in invoke: " + e.getMessage());
			}
		}
		log.info("invoking method=" + operation + " from " + service_name);
		return null;
	}

    /**
     * invokes the home interface of the service
     * @param method The method of the service to invoke
	 * @param args The parameters being passed into the method
	 * @throws SecurityException if the service instance is invalid or or cannot be invoked by this user 
     **/
    public void invokeHome(Method method, Object[] args) throws SecurityException
	{
		int i=0;
		for (i=0;i<args.length ;i++ )
		{
			log.info("invokeHome args["+i+"]="+args[i]);
		}
		//log.info("out of invokeHome");
  	}

    /**
     * validates the SOAP message being called
	 * @param method The method being validated
	 * @param service   The service being validated
	 * @param userId The user invoking the method
	 * @param password The password of the user
	 * @throws SecurityException if the user provides an incorrect password or has no rights to invoke the method
     **/
	public void validateSOAPMessage(String method, String service, String userId, String password) throws SecurityException {
		log.info("in validateSOAPMessage");
		// JWA - todo
	}

	protected void invokeHomeOnDelegate(Method arg0, Object[] arg1, Object arg2)
			throws Exception {
		invoke(arg0, arg1, arg2);
	}

	protected void invokeOnDelegate(Method arg0, Object[] arg1, Object arg2)
			throws Exception {
		invoke(arg0, arg1, arg2);
	}
}
