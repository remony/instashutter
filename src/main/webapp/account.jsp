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
        Username: <%= p.getUsername() %><br>
	      First name:  <%= p.getfname() %><br>
	       Last name: <%= p.getlname() %>
	       </div>
        </div> 
        <%

            }
            }
       
        %>
        
</body>
</html>