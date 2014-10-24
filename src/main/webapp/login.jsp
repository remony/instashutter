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
		if (currentSession.getUserSession()) {
			%>
	response.sendRedirect("/instashutter/"); 
	<%}
	} else {
			%>
	<div class="post">
		
			
		<div class="post_timestamp">
		<h3>Login</h3>
		</div>
		<div class="post_caption">
		<form name="login" action="login" method="POST" accept-charset="utf-8">
			    <ul>  
			        <li><label for="username">Username</label>  
			        <input type="text" name="username" placeholder="username" required></li>  
			        <li><label for="password">Password</label>  
			        <input type="password" name="password" placeholder="password" required></li>  
			        <li>  
			        <input type="submit" value="Login"></li>  
			    </ul>  
			</form> 
		</div>
	<%
		}
			%>



</body>
</html>