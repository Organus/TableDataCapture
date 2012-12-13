<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>

<head>
<title>SABC Login</title>

<style TYPE="text/css">
.errormessage {
   color:red;
}

.successmessage {
}
</style>
<script type="text/javascript">
function initForm()
{
	//self.document.loginForm.submit();
}
</script>
 
</head>

<h1 align="center"><img src="sabc_logo.jpg" height="70"/>SABC Login<img src="nrel_logo_large.jpg" height="70"/></h1>

<body onLoad="initForm()">
<center>
<form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
<c:if test="${not empty param.authfailed}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
    </span>
</c:if>
<c:if test="${not empty param.newpassword}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
    </span>
</c:if>
<c:if test="${not empty param.acclocked}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
    </span>
</c:if>
<c:if test="${not empty param.accdisabled}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
    </span>
</c:if>
<c:if test="${not empty param.loggedout}">
    <span id="infomessage" class="successmessage">
    You have been successfully logged out.
    </span>
</c:if>
        <table>
          <tr><td>Username</td><td><input id="usernameField" type="text" name="j_username" value="<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/>"/></td></tr>
          <tr><td>Password</td><td><input id="passwordField" type="password" name="j_password" /></td></tr>
          <tr><td><input id="j_hidden" type="hidden" name="j_hidden" value="login"/></td></tr>
			<jsp:useBean id="user" class="gov.nrel.nbc.security.client.UserBean" />
			<jsp:setProperty name="user" property="j_hidden" value="login"/>
			<%session.setAttribute("j_hidden", "login");%> 
          <tr><td colspan="2" align="center"><input type="submit" value="Login" /></td></tr>
        </table>
</form>
</center>
</body>

</html>