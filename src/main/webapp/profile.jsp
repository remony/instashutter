<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<%@page import="java.util.*"%>
<%@ page import = "java.text.DateFormat" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<% String user = request.getAttribute("viewingUser").toString();
	String background = null;
%>
<html>
  <head>
    <title>Profile</title>
  </head>
  <body>
  <jsp:include page="header.jsp" />
	<div class="profile_header">
		<div class="profile_content">
			<div class="profile_image">
				<a href="/instashutter/picture/<%= user %>">
					<img src="/instashutter/picture/<%= user  %>" alt="profile image"/>
				</a>
			</div>
			<%
				LinkedList<UserSession> userInfo = (LinkedList<UserSession>) request.getAttribute("UserInfo");
				if (userInfo == null)	{%>
					<p>User has no profile information</p>
				<%}	else	{
					Iterator<UserSession> iterator;
					iterator = userInfo.iterator();
						UserSession profile = iterator.next();
						background = profile.getBackground();
						String fname = profile.getfname();
						String lname = profile.getlname();
						String location = profile.getLocation();
						String bio = profile.getBio();
						%>
						<div class="profile_info">
							<table>
								<tr>
									<td>Name: </td>
									<td><%= fname %> </td>
									<td><%= lname %></td>
								</tr>
								<tr>
									<td>location: </td>
									<td><%= location %></td>
								</tr>
								<tr>
									<td>Bio: </td>
									<td><%= bio %></td>
								</tr>
								<tr>
									<td>
										<form name="follow" action="/instashutter/profile/<%= user %>/follow" method="POST">
											<input type="hidden" name="method" value="follow">
											<input type="hidden" name="followingUser" value="<%= user%>">
											<input type="submit" value="Follow"><br>
										</form>
									</td>
									<td>
										<form name="unfollow" action="/instashutter/profile/<%= user %>/unfollow" method="POST">
											<input type="hidden" name="method" value="unfollow">
											<input type="hidden" name="followingUser" value="<%= user%>">
											<input type="submit" value="Unfollow"><br>
										</form>
									</td>
								</tr>
							</table>
						</div>	
					<%
				}%>
		</div>
	</div>
	


    <%LinkedList<Pic> lsPics = (LinkedList<Pic>) request.getAttribute("Pics");

	        	if (lsPics == null) {%>
	        		<p>No Pictures found</p>
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
							<a href="/instashutter/post/<%= uuid %>"><img src="/instashutter/Thumb/<%= uuid %>"></a>
						</div>
						<div class="post_timestamp">
							<div class="timeConvertsince" title="<%= timeAdded %>"><%= timeAdded %></div> 
						</div>
						<div class="post_author">
						
							<a href="/instashutter/profile/<%= username %>">@<%= username %></a>
							<a href="<%=request.getContextPath()%>/picture/<%= username %>"><img src="<%=request.getContextPath()%>/picture/<%= username %>" alt="Profile image" /></a>
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
								<form name="comment_input" action="/instashutter/" method="POST">
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