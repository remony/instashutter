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

	
       	<div class="account">
       		<div class="post_timestamp">
       			<h2>Account details</h2>
       		</div>
       		<div class="account_caption">
       			<div class="account_options">
       				<a href="#editDetails" onclick="toggle_visibility('details')">Edit User Details</a><br>
       				<a href="#editPassword" onclick="toggle_visibility('password')">Change Password</a>
       				<a href="#editProfileImage" onclick="toggle_visibility('profileimage')">Change Profile image</a>
       				<a href="#editBio" onclick="toggle_visibility('bio')">Change Bio</a>
       			</div>
       			
       			<script type="text/javascript">

				    function toggle_visibility(id) {
				       var e = document.getElementById(id);
				       if (id == "details") {
				    	   var id = ["password", "profileimage", "bio", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   } 
				       } else if (id == "password") {
				    	   var id = ["details", "profileimage", "bio", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       } else if (id == "profileimage") {
				    	   var id = ["details", "password", "bio", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       }	else if (id == "bio")	{
				    	   var id = ["details", "profileimage", "profileimage", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       }	else	{
				    		console.log("Error unknown ID");   
				       }
				          e.style.display = 'block';
				    }
				
				</script>
       			
       			
       			<div class="account_view">
       				<div id="account_message">
       					<h4>Please select one of the options to your left</h4>
       				</div>
       				<div id="details">
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
       				<div id="password">
	       				<form name="input" action="/instashutter/account/editpassword" method="post">
		       				Current password: <input type="password" name="currentPassword" value = ""><br>
							Enter your new password:<input type="password" name="newPassword" value = ""><br>
							Enter your new password again: <input type="password" name="newPasswordVerify" value = ""><br>
							<input type="submit" value="Update password">
		       			</form>
       				</div>
       				<div id="profileimage">
	       				<form method="POST" id="uploadform" enctype="multipart/form-data" action="/instashutter/account/editProfileImage" >
							<div class="post_timestamp">
								<input type="file" name="file" value="/tmp"  onchange="readfile(this);"><br/>
							</div>
							<div class="post_comments">
								<input type="submit" value="Post">
							</div>
					   		
								
						</form>
       				</div>
       				<div id="bio">
       					<h4>Coming soon.</h4>
       				</div>
       			</div>
       		</div>
       	</div>
        	
        
   
        
</body>
</html>