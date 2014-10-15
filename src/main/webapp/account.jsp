<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ page import="me.stuartdouglas.stores.*" %>
    <%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:include page="header.jsp" />

	<%
            LinkedList<UserSession> lsUser = (LinkedList<UserSession>) request.getAttribute("UserInfo");
            if (lsUser == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<UserSession> iterator;
            iterator = lsUser.iterator();
            while (iterator.hasNext()) {
            	UserSession p = (UserSession) iterator.next();

        %>
        
        
        <div class = "post">
        	<h3>Your account details</h3>
        	<div class="post_desc">
        	<h2>Change user details</h2>
        	<form name="input" action="/instashutter/account/edit" method="post">
				Your username: <%= p.getUsername() %> (cannot be changed)<br>
				First name: <input type="text" name="fname" value = "<%= p.getfname() %>"><br>
				<input type="hidden" name="previousFname" value = "<%= p.getfname() %>">			
				Last name: <input type="text" name="lname" value = "<%= p.getlname() %>"><br>
				<input type="hidden" name="previousLname" value = "<%= p.getlname() %>">
				Password: <input type="password" name="password" ><br>
				<input type="submit" value="Submit">
			</form>
			
			<br><br>
			<h2>Change password</h2>
			<form name="input" action="/instashutter/account/editpassword" method="post">
				New Password: <input type="password" name="password" ><br>
				Old Password: <input type="password" name="password" ><br>
				<input type="submit" value="Submit">
			</form>
	       </div>
        </div> 
        <%

            }
            }
       
        %>
        
</body>
</html>