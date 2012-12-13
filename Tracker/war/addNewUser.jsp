<%@ page session="true"%>
<%@ page import="com.temesoft.security.Base64Coder"%>
<%@ page import="com.temesoft.security.Encrypter"%>
<%@ page import="com.temesoft.security.Config"%>
<%@ page import="gov.nrel.nbc.security.client.StaticUserBean"%>
<%@ page import="java.awt.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>SABC Add New User</title>

<style TYPE="text/css">
.errormessage {
   color:red;
}

.successmessage {
}
</style>
</head>

<h1 align="center"><img src="sabc_logo.jpg" height="70"/>SABC Add New User<img src="nrel_logo_large.jpg" height="70"/></h1>

<jsp:useBean id="user" class="gov.nrel.nbc.security.client.UserBean" scope="session" />
<body onload='document.addForm.j_username.focus();'>

<center>

<form id="addForm" name="addForm" action="j_spring_security_check" method="post" >
<span id="infomessage" class="errormessage" >
<input id="j_message" type="hidden" name="j_message" />
<jsp:getProperty name="user" property="j_message" />
</span>
<c:if test="${user.j_message eq 'New user added. An email has been sent to the administrator for validation.'}">
  <c:redirect url="/notification.jsp"/>
</c:if> 
<c:if test="${user.j_message eq 'Login Failed.'}">
  <c:redirect url="/login.jsp?authfailed=true"/>
</c:if> 
<c:if test="${not empty param.weakpassword}">
    <span id="infomessage" class="errormessage" >
    User not created due to weak password.
    </span>
</c:if>
<c:if test="${not empty param.noemail}">
    <span id="infomessage" class="errormessage" >
    User not created due to no email provided.
    </span>
</c:if>
<c:if test="${not empty param.nomatch}">
    <span id="infomessage" class="errormessage" >
    User not created due to non-matching password.
    </span>
</c:if>
<c:if test="${not empty param.authfailed}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </span>
</c:if>
<c:if test="${not empty param.newpassword}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </span>
</c:if>
<c:if test="${not empty param.acclocked}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </span>
</c:if>
<c:if test="${not empty param.accdisabled}">
    <span id="infomessage" class="errormessage" >
    Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </span>
</c:if>
<c:if test="${not empty param.loggedout}">
    <span id="infomessage" class="successmessage">
    You have been successfully logged out.
    </span>
</c:if>
        <table>
          <tr></tr>
          <tr><td>New Username</td><td><input id="usernameField" type="text" name="j_username" value="<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/>"/></td></tr>
          <tr><td>First Name</td><td><input id="firstNameField" type="text" name="j_first" /></td></tr>
          <tr><td>Last Name</td><td><input id="lastNameField" type="text" name="j_last" /></td></tr>
          <tr><td>E-Mail</td><td><input id="emailField" type="text" name="j_email" /></td></tr>
		  <tr><td><input id="j_hidden" type="hidden" name="j_hidden" value="add"/></td></tr>
			<jsp:setProperty name="user" property="j_hidden" value="add"/>
<%
    String randomLetters = new String("");
    for (int i = 0; i < Config.getPropertyInt(Config.MAX_NUMBER); i++) {
        randomLetters += (char) (65 + (Math.random() * 24));

    }
    randomLetters = randomLetters.replaceAll("I","X");
    randomLetters = randomLetters.replaceAll("Q","Z");

    String passlineNormal = randomLetters + "." + request.getSession().getId();
    //- this one - String passlineValueEncoded = stringEncrypter.encrypt(passlineNormal);
    //passlineValueEncoded  = Base64Coder.encode(passlineValueEncoded );
    StaticUserBean.setRandom(passlineNormal);
%>
                          <tr>
                              <td colspan="2" align="center"><img
                                  src="PassImageServlet/<%=passlineNormal %>"
                                  border="0"></td>
                          </tr>
                          <tr>
                              <td align="right">Security Code</td><td><input type="text"
                                                     name="j_passline" size="9"></td>
                          </tr>
          <tr><td colspan="2" align="center"><input type="submit" value="Send Request" /></td></tr>
        </table>
</form>
</center>
</body>

</html>