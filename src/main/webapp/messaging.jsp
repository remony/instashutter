<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<%@page import="java.util.*"%>
<%@ page import = "java.text.DateFormat" %>
<%@ page import = "java.text.SimpleDateFormat" %>

<html>
  <head>
    <title>Messages</title>
  </head>
  <body>
    <jsp:include page="header.jsp" />
    <%LinkedList<FollowingStore> lsPics = (LinkedList<FollowingStore>) request.getAttribute("userList");
	        	if (lsPics == null) {%>
	        		<p>No results</p>
	        		<form name="search" action="/instashutter/search" method="POST">
						<input type="text" class="form-control" name="keyword" placeholder="Search" autofocus>
						<input type="submit" value="comment">
					</form>
	        		 
	       		<%} else {%>
	       		<div class="post">
						<div class="post_timestamp">
							<h2>Select a follower to message</h2>
						</div>
	       		
	       		<%
	            Iterator<FollowingStore> iterator;
	            iterator = lsPics.iterator();
	            while (iterator.hasNext()) {
	            	FollowingStore p = iterator.next();
					String username = p.getFollowing();
					%>
					
					
						<div class="post_author">
							<a href="/instashutter/message/<%= username %>">@<%= username %></a>
							
						</div>
						
					
					

	            <%}} %>
	            </div>
  </body>
  <script src="<c:url value="/assets/js/jquery.timeago.js"/>"></script>
	<script src="<c:url value="/assets/js/app.js"/>"></script>
</html>