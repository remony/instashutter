<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account Setting - Password</title>
</head>
<body>
<jsp:include page="../header.jsp" />


<div class="post">
	<div class="post_timestamp">
		<h1>Editing user password</h1>
	</div>
	
	<div class="account_caption">
       			<div class="account_options">
       				<a href="editdetails">Edit User Details</a><br>
       				<a href="editpassword">Change Password</a>
       			</div>
       			<div class="account_view">
	       			<form name="input" action="/instashutter/account/editpassword" method="post">
	       				Current password: <input type="text" name="currentPassword" value = ""><br>
						Enter your new password:<input type="text" name="newPassword" value = ""><br>
						Enter your new password again: <input type="text" name="newPasswordVerify" value = ""><br>
						<input type="submit" value="Update password">
	       			</form>
       			</div>
       		</div>
</div>





</body>
</html>