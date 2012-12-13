<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>

<head>
<title>SABC New User Created</title>

<style TYPE="text/css">
.errormessage {
   color:red;
}

.successmessage {
}
</style>
</head>

<h1 align="center"><img src="sabc_logo.jpg" height="70"/>SABC New User Created<img src="nrel_logo_large.jpg" height="70"/></h1>

<body>
<center>
<form id="noteForm" name="noteForm" action="j_spring_security_check" method="post">
<c:if test="${user.j_message eq 'New user added. An email has been sent to the administrator for validation.'}">
    <span id="infomessage" class="infomessage" >
    <br> A new user has been added. </br>
    <br> An email has been sent to the administrator for validation. </br>
    <br> Your account will not be available for use until it has been validated. </br>
    </span>
</c:if> 
<c:if test="${not empty param.newuser}">
    <span id="infomessage" class="infomessage" >
    <br> A new user has been added. </br>
    <br> An email has been sent to the administrator for validation. </br>
    <br> Your account will not be available for use until it has been validated. </br>
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
   </span>
</c:if>
</form>
</center>
</body>

</html>