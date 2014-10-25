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
	<div class="post">
		<div class="post_timestamp">
			<h2>You have logged out!</h2>
		</div>
		<div class="post_caption">
			<h4>We miss you!</h4>
			<a href="/instashutter/login">Click here to log back in</a>
		</div>
	</div>
	
</body>
</html>