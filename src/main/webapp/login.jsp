<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="me.stuartdouglas.stores.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
<jsp:include page="header.jsp" /> 
	<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {
			%>
	response.sendRedirect("/instashutter/dashboard"); 
	<%}
	} else {
			%>
	<h3>Login</h3>
	<div class="alert alert-warning" role="alert">${message}</div>
	<form method="POST" action="login">
		<ul>
			<li>User Name <input type="text" name="username" required="required" ></li>
			<li>Password <input type="password" name="password" required="required" ></li>
		</ul>
		<br /> <input type="submit" value="Login">
	</form>
	<%
		}
			%>



</body>
</html>