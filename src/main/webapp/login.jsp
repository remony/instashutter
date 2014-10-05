<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="me.stuartdouglas.stores.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {
			%>
	<jsp:forward page="/dashboard" />
	<%}
	} else {
			%>
	<h3>Login</h3>
	<form method="POST" action="login">
		<ul>
			<li>User Name <input type="text" name="username"></li>
			<li>Password <input type="password" name="password"></li>
		</ul>
		<br /> <input type="submit" value="Login">
	</form>
	<%
		}
			%>



</body>
</html>