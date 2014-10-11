<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:include page="header.jsp" /> 
<form method="POST" enctype="multipart/form-data" action="Image">
	<input type="file" name="file" value="/tmp"><br/>
	<h5>Title:</h5><input type="text" name="description"><br/>
	<input type="submit" value="Press">
</form>


</body>
</html>