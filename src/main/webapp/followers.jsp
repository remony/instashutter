<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<%@page import="java.util.*"%>
<%@ page import = "java.text.DateFormat" %>
<%@ page import = "java.text.SimpleDateFormat" %>

<html>
  <head>
    <title>Followers</title>
  </head>
  <body>
    <jsp:include page="header.jsp" />
    <h1>Your following</h1>
    <%LinkedList<FollowingStore> lsFollowing = (LinkedList<FollowingStore>) request.getAttribute("followers");

	        	if (lsFollowing == null) {%>
	        		<p>No results</p>
	        		<form name="search" action="/instashutter/search" method="POST">
						<input type="text" class="form-control" name="keyword" placeholder="Search" autofocus>
						<input type="submit" value="comment">
					</form>
	        		 
	       		<%} else {
	            Iterator<FollowingStore> iterator;
	            iterator = lsFollowing.iterator();
	            while (iterator.hasNext()) {
	            	FollowingStore p = iterator.next();
					String username = p.getFollowing();

					%>
					
					<div class="post">
						<div class="post_timestamp">
							<h2>Your Followers</h2>
						</div>
						<div class="post_caption">
							<%= username %>
						</div>
					</div>
					

	            <%}} %>
  </body>
  <script src="<c:url value="/assets/js/jquery.timeago.js"/>"></script>
	<script src="<c:url value="/assets/js/app.js"/>"></script>
</html>