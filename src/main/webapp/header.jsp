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
	<header>
	<h1>InstaShutter</h1>
	</header>
	<nav>
	<ul>
		<li><a href="/instashutter/">Home</a></li>
		<li><a href="/instashutter/upload">Upload</a></li>
		<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {
			%>
		<li><a
			href="/instashutter/Images/<%=currentSession.getUsername()%>">Your
				Images</a></li>
			<li>Welcome, <%= userName %> <a href="/instashutter/Logout">Logout?</a></li>	
		<%}
	} else {
			%>

		<li><a href="/instashutter/register">Register</a></li>
		<li><a href="/instashutter/login">Login</a></li>
		<%
		}
			%>
	</ul>
	</nav>
</body>
</html>