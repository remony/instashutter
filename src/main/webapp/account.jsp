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
        	<form name="input" action="/instashutter/account/edit" method="post">
				Username: <input type="text" name="username" value = "<%= p.getUsername() %>">
				<input type="hidden" name="previousUsername" value = "<%= p.getUsername() %>">
				First name: <input type="text" name="fname" value = "<%= p.getfname() %>">
				<input type="hidden" name="previousFname" value = "<%= p.getfname() %>">			
				Last name: <input type="text" name="lname" value = "<%= p.getlname() %>">
				<input type="hidden" name="previousLname" value = "<%= p.getlname() %>">
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