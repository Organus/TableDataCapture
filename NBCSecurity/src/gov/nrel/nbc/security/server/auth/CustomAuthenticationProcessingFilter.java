package gov.nrel.nbc.security.server.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import gov.nrel.nbc.security.client.StaticUserBean;
import gov.nrel.nbc.security.utils.XLogger;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Filter class in which a custom Authentication token is built with an extra domain parameter retrieved from the incoming request.
 * @author guy
 */
public class CustomAuthenticationProcessingFilter extends AbstractPreAuthenticatedProcessingFilter
{
   private static final XLogger log = new XLogger(XLogger.INFO);
   protected static final String USERNAME_PARAM_KEY = "j_username";
   protected static final String PASSWORD_PARAM_KEY = "j_password";
   protected static final String PASSWORD2_PARAM_KEY   = "j_password2";
   protected static final String FIRSTNAME_PARAM_KEY   = "j_first";
   protected static final String LASTNAME_PARAM_KEY   = "j_last";
   protected static final String EMAIL_PARAM_KEY   = "j_email";
   protected static final String HIDDEN_PARAM_KEY   = "j_hidden";
   protected static final String RANDOM_PARAM_KEY   = "j_random";
   protected static final String PASSLINE_PARAM_KEY   = "j_passline";

//@Override
protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    StaticUserBean.setUsername(request.getParameter (USERNAME_PARAM_KEY));
    StaticUserBean.setPassword(request.getParameter (PASSWORD_PARAM_KEY));
    StaticUserBean.setPassword2(request.getParameter (PASSWORD2_PARAM_KEY));
    StaticUserBean.setFirst(request.getParameter (FIRSTNAME_PARAM_KEY));
    StaticUserBean.setLast(request.getParameter (LASTNAME_PARAM_KEY));
    StaticUserBean.setEmail(request.getParameter (EMAIL_PARAM_KEY));
    StaticUserBean.setHidden(request.getParameter(HIDDEN_PARAM_KEY));
    StaticUserBean.setPassline(request.getParameter(PASSLINE_PARAM_KEY));
    //log.info ("username : " + StaticUserBean.getUsername());
    //log.info ("username : " + StaticUserBean.getUsername());
    //log.info ("password : " + StaticUserBean.getPassword());
    //log.info ("password2 : " + StaticUserBean.getPassword2());
    //log.info ("first : " + StaticUserBean.getFirst());
    //log.info ("last : " + StaticUserBean.getLast());
    //log.info ("email : " + StaticUserBean.getEmail());
    //log.info("hidden=" + StaticUserBean.getHidden());
	return request.getParameter (USERNAME_PARAM_KEY);
}
/* (non-Javadoc)
 * @see org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
 */
//@Override
public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
	super.doFilter(request, response, chain);
}
//@Override
protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    StaticUserBean.setUsername(request.getParameter (USERNAME_PARAM_KEY));
    StaticUserBean.setPassword(request.getParameter (PASSWORD_PARAM_KEY));
    StaticUserBean.setPassword2(request.getParameter (PASSWORD2_PARAM_KEY));
    StaticUserBean.setFirst(request.getParameter (FIRSTNAME_PARAM_KEY));
    StaticUserBean.setLast(request.getParameter (LASTNAME_PARAM_KEY));
    StaticUserBean.setEmail(request.getParameter (EMAIL_PARAM_KEY));
    StaticUserBean.setHidden(request.getParameter(HIDDEN_PARAM_KEY));
    StaticUserBean.setPassline(request.getParameter(PASSLINE_PARAM_KEY));
    //log.info ("username : " + StaticUserBean.getUsername());
    //log.info ("password : " + StaticUserBean.getPassword());
    //log.info ("password2 : " + StaticUserBean.getPassword2());
    //log.info ("first : " + StaticUserBean.getFirst());
    //log.info ("last : " + StaticUserBean.getLast());
    //log.info ("email : " + StaticUserBean.getEmail());
    //log.info("hidden=" + StaticUserBean.getHidden());
	return request.getParameter (USERNAME_PARAM_KEY);
}

}