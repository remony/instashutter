<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<%@page import="java.util.*"%>
<%@ page import = "java.text.DateFormat" %>
<%@ page import = "java.text.SimpleDateFormat" %>

<html>
  <head>
    <title>Dashboard</title>
  </head>
  <body>
    <jsp:include page="header.jsp" />
    <%LinkedList<Pic> lsPics = (LinkedList<Pic>) request.getAttribute("Pics");

	        	if (lsPics == null) {%>
	        		<p>No results</p>
	        		<form name="search" action="/instashutter/search" method="POST">
						<input type="text" class="form-control" name="keyword" placeholder="Search" autofocus>
						<input type="submit" value="comment">
					</form>
	        		 
	       		<%} else {
	            Iterator<Pic> iterator;
	            iterator = lsPics.iterator();
	            while (iterator.hasNext()) {
	            	Pic p = iterator.next();
					String username = p.getPostedUsername();
					String uuid = p.getSUUID();
					String caption = p.getCaption();
					//Convert date to iso 8601 format for use with jquery.timeago.js using code from http://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format
					TimeZone timeZone = TimeZone.getTimeZone("UTC");
	    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		    		df.setTimeZone(timeZone);
					String timeAdded = df.format(p.getPicAdded());
					%>
					
					<div class="post">
						<div class="post_image">
							<a href="/instashutter/profile/<%= username %>"><img src="/instashutter/Thumb/<%= uuid %>"></a>
						</div>
						<div class="post_timestamp">
							<%= timeAdded %> 
						</div>
						<div class="post_author">
							<%= username %>
						</div>
						<div class="post_caption">
							<%= caption %>
						</div>
						<div class="post_comments">
							<%
								LinkedList<CommentStore> lsComments = p.getCommentlist();
								int count = 0;
								if (lsComments == null) {
									%>
									
									<p> No comments </p>
									
									
									<%
								} else { %>
								
								<div class="comment_container">
								<table>
								
								<%
							Iterator<CommentStore> commentIterator;
							commentIterator = lsComments.iterator();
							while(commentIterator.hasNext()){
								CommentStore c = commentIterator.next();
								String cUsername = c.getUsername();
								String comment = c.getCommentMessage();
								count++;
								%>
									<tr>
									<td>
										<div class="comment_user">
											<a href="/instashutter/profile/<%= cUsername%>"><%= cUsername %></a>
										</div>
									</td>
									<td>
										<div class="comment_message">
											<%= comment %>
										</div>

									</td>
								</tr>
								<%}} %>
								</table>
								<% if (count == 5)	{ %>
									<div class="comment_readmore">
										<a href="/instashutter/post/<%= uuid %>">More comments</a>
									</div>
								<% } %>
							</div>
							<div class="post_comment_form">
								<form name="comment_input" action="/instashutter/dashboard" method="POST">
									<input type="hidden" name="uuid" value="<%=p.getSUUID() %>">
									Comment: <input type="text" name="comment">
									<input type="submit" value="comment">
								</form>
							</div>
						</div>
					</div>
					

	            <%}} %>
  </body>
  <script src="<c:url value="/assets/js/jquery.timeago.js"/>"></script>
	<script src="<c:url value="/assets/js/app.js"/>"></script>
</html>