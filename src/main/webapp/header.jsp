<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="me.stuartdouglas.stores.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Instashutter</title>
<link rel="stylesheet" type="text/css" href="/instashutter/assets/css/styles.css">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>


<style>
body {
	width:600px;
	margin:0 auto;
	text-align:center;
}
</style>


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
		<li><a href="/instashutter/images/<%=currentSession.getUsername()%>">Your
				Images</a></li>
		<li><a href="/instashutter/profile/<%=currentSession.getUsername()%>">Your
				Profile</a></li>
			<li>Welcome, <%= userName %> <a href="/instashutter/logout">Logout?</a></li>	
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