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
	<jsp:include page="header.jsp" />
	<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession() == false) {
			%>
	<div class="alert alert-info" role="alert">
	  <h3>You have not even logged in yet...</h3>
	</div>
	<%}
	} else {
			%>
	<div class="alert alert-info" role="alert">
	  <h3>You have logged out.</h3>
	</div>
	
	<a href="login">Come back!</a>
	<%
		}
			%>
	
</body>
</html>