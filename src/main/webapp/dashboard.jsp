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
    <%LinkedList<PicStore> lsPics = (LinkedList<PicStore>) request.getAttribute("Pics");

	        	if (lsPics == null) {%>
	        		<div class="post">
	        			<div class="post_timestamp">
	        				<p>No results</p>
	        			</div>
	        			<div class="post_content">
	        			<form name="search" action="/instashutter/search" method="POST">
							<input type="text" class="form-control" name="keyword" placeholder="Search for posts" autofocus>
							<input type="submit" value="Search">
						</form>
	        			</div>
	        		</div>
	       		<%} else {%>
	       		<%
              String message = (String) request.getAttribute("message");
              if (message != null){
            	  %>
	           	  	<div class="message_banner">
	            	  	<%= message %>
	           	  	</div>
            	  <%
              }
            %>
	       		
	       		
	       		<%
	            Iterator<PicStore> iterator;
	            iterator = lsPics.iterator();
	            while (iterator.hasNext()) {
	            	PicStore p = iterator.next();
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
						<div class="post_share">
							
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