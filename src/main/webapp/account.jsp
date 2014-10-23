<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ page import="me.stuartdouglas.stores.*" %>
    <%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Accounts page</title>
<script src="<c:url value="/assets/js/Accounts.js"/>"></script>

</head>
<body>
<jsp:include page="header.jsp" />


       	<div class="account">
       		<div class="post_timestamp">
       			<h2>Account details</h2>
       		</div>
       		<div class="account_caption">
       			<div class="account_options">
       				<a href="#editDetails" onclick="toggle_hidden('details')">Edit User Details</a><br>
       				<a href="#editPassword" onclick="toggle_hidden('password')">Change Password</a><br>
       				<a href="#editProfileImage" onclick="toggle_hidden('profileimage')">Change Profile image</a><br>
       				<a href="#editBio" onclick="toggle_hidden('bio')">Change Bio</a><br>
       				<a href="#changeProfileBackground" onclick="toggle_hidden('profileBackground')">Change profile background</a>
       				<a href="#changeprofileEmail" onclick="toggle_hidden('profileEmail')">Change email</a>
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
       						UserSession p = iterator.next();
       						//background = p.getBackground();
       						//bio = p.getBio();
       			%>
       				<div id="account_message">
       					<h4>Please select one of the options to your left</h4><br>
       					<a href="<%=request.getContextPath()%>/picture/<%= p.getUsername() %>"><img src="<%=request.getContextPath()%>/picture/<%= p.getUsername() %>" alt="Profile image" /></a>
       					Username: <%= p.getUsername() %><br>
       					Full name: <%= p.getfname() %> <%= p.getlname() %><br>
       					Location: <%= p.getLocation() %><br>
       					Bio: <%= p.getBio() %><br>
       					Profile background: <%= p.getBackground() %><br>
       					Profile email: <%= p.getEmail() %><br>
       				</div>
       				<div id="details">
       				
       			<form name="input" action="<c:url value="/account/editdetails"/>" method="post">
					Your username: (cannot be changed)<br>
					First name: <input type="text" name="fname" value = "<%= p.getfname() %>"><br>
					<input type="hidden" name="previousFname" value = "<%= p.getfname() %>">			
					Last name: <input type="text" name="lname" value = "<%= p.getlname() %>"><br>
					<input type="hidden" name="previousLname" value = "<%= p.getlname() %>">
					Location: <input type="text" name="location" value = "<%= p.getLocation() %>"><br>
					Password: <input type="password" name="password" ><br>
					<input type="submit" value="Submit">
				</form>
       			
       			
       				</div>
       				<div id="password">
	       				<form name="input" action="<c:url value="/account/editpassword"/>" method="post">
		       				Current password: <input type="password" name="currentPassword" value = ""><br>
							Enter your new password:<input type="password" name="newPassword" value = ""><br>
							Enter your new password again: <input type="password" name="newPasswordVerify" value = ""><br>
							<input type="submit" value="Update password">
		       			</form>
       				</div>
       				<div id="profileimage">
	       				<form method="POST" id="uploadform" enctype="multipart/form-data" action="<c:url value="/account/editProfileImage"/>" >
							<div class="post_timestamp">
								<input type="file" name="file" value="/tmp"  onchange="readfile(this);"><br/>
							</div>
							<div class="post_comments">
								<input type="submit" value="Post">
							</div>
					   		
								
						</form>
       				</div>
       				<div id="bio">
       				
       					<form name="input" id="updatebio" action="<c:url value="/account/editbio"/>" method="post">
		       				Bio: <textarea rows="4" cols="50" name="bio" form="updatebio"><%= p.getBio() %></textarea><br>
							<input type="submit" value="Update background">
		       			</form>
       				</div>
       				<div id="profileBackground">
       					<form name="input" action="<c:url value="/account/profilePersonalization"/>" method="post">
		       				Background URL: <input type="text" name="url" value = "<%= p.getBackground() %>"><br>
							<input type="submit" value="Update background">
		       			</form>
       				</div>
       				<div id="profileEmail">
       					<form name="emailInput" action="<c:url value="/account/editEmail"/>" method="post">
		       				Email: <input type="text" name="email" value = "<%= p.getEmail() %>"><br>
							Enter your password:<input type="password" name="password" value = ""><br>
							<input type="submit" value="Update email">
		       			</form>
       				</div>
       			</div>
       			
       			<%       			
       					}
       				}
   				%>
       		</div>
       	</div>
        	
        
   
        
</body>
</html>