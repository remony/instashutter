<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="me.stuartdouglas.stores.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Register</title>
		<link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
		<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
		<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.min.js"></script>
	</head>
	<body>
	<jsp:include page="header.jsp" /> 
	
	<div ng-app="">
		
		
		<div class="post">
			<div class="post_timestamp">
				<h3>Register</h3>
			</div>
			
			<form method="POST" id="register"  action="Register">
		    <ul>
		    	<li>User Name <input type="text" name="username" ng-model="username" required></li>
				<li>First Name <input type="text" name="fname" ng-model="fname" required></li>
				<li>Last Name <input type="text" name="lname" ng-model="lname" required></li> 
		        <li>Email <input type="text" name="email" ng-model="email" required></li>
		        <li>Password <input type="password" name="password" required></li>
		        <li>Enter Password again <input type="password" name="passwordAgain" required></li>
		        <li><br><h4>Profile [optional]</h4></li>
		        <li>Location <input type="text" name="location" ng-model="location"></li>
		        <li>Bio: <textarea rows="4" cols="50" name="bio" form="register" ng-model="bio"></textarea></li>
		    </ul>
		    <br/>
		    <input type="submit" value="register"> 
		</form>
		</div>
		
		<div class="post">
			<div class="post_timestamp">
				<h2>Profile preview</h2>
			</div>
			
		<table style="width:500px">
		<tr>
			<td>
				<p>@</p>
			</td>
			<td>	
				<p ng-bind="username">Username</p>
			</td>
		</tr>
		<tr>
			<td>
				<p>Name</p>
			</td>
			<td>
				<p ng-bind="fname">First name</p>
			</td> 									
			<td>		
				<p ng-bind="lname">Last name</p>
			</td>
		</tr>
		<tr>
		<td>
			Location
		</td>
		<td>
			<p ng-bind="location"></p>
		</td>
		</tr>
		<tr>
			<td>
				<p ng-bind="bio"></p>
			</td>
		
		</tr>
			</table>
	
			</div>
		</div>			
	</body>
</html>