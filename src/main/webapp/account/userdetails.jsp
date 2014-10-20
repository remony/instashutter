<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:include page="../header.jsp" />

<div class="post">
	<div class="post_timestamp">
		<h1>Edit your details</h1>
	</div>
	<div class="post_content">
	<div class="account_caption">
       			<div class="account_options">
       				<a href="editdetails">Edit User Details</a><br>
       				<a href="editpassword">Change Password</a>
       			</div>
       			<div class="account_view">
       			
       			<%
       				LinkedList<UserSession> userInfo = (LinkedList<UserSession>) request.getAttribute("UserInfo");
       				if (userInfo == null) {
       			%>
       				<h4>No access: you are not logged in.</h4>
   				<%
       				}	else	{
       					Iterator<UserSession> iterator;
       					iterator = userInfo.iterator();
       					while (iterator.hasNext()) {
       						UserSession p = (UserSession) iterator.next();
       						
       			%>
       			<form name="input" action="/instashutter/account/editdetails" method="post">
					Your username: (cannot be changed)<br>
					First name: <input type="text" name="fname" value = "<%= p.getfname() %>"><br>
					<input type="hidden" name="previousFname" value = "<%= p.getfname() %>">			
					Last name: <input type="text" name="lname" value = "<%= p.getlname() %>"><br>
					<input type="hidden" name="previousLname" value = ""<%= p.getlname() %>>
					Password: <input type="password" name="password" ><br>
					<input type="submit" value="Submit">
				</form>
       			
       			<%       			
       					}
       				}
   				%>
       			
       			
       				
       			</div>
       		</div>
	
	
		
	</div>
</div>

</body>
</html>