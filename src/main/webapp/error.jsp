<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!-- 

		Description: Error page takes in an error JSON and displays it on the page.

 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
</head>
<body>

<jsp:include page="header.jsp" />

	<% Object message = request.getAttribute("message");%>
	
	

<div class="post">

	<div class="post_timestamp">
		<div id="title">Error title</div>
	</div>
	<div class="post_caption">
		<div id="message">Error Message</div>
		
		<div id="date">Error date</div>
	</div>
</div>

<script>
	var json = '<%= message %>';
	obj = JSON.parse(json);
	document.getElementById("title").innerHTML = "<h1>" + obj.title + "</h1>";
	document.getElementById("message").innerHTML = "<h3>" + obj.message + "</h3>";
	document.getElementById("date").innerHTML = "Error received " + obj.time_issued;
	</script>
	
</body>
</html>