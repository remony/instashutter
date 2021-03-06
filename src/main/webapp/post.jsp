<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="me.stuartdouglas.stores.*" %>
    
	<%@page import="java.util.*"%>
	<%@ page import = "java.text.DateFormat" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>user post</title>

</head>
<body>

<div class="profile_background"></div>
<jsp:include page="header.jsp" />
<div class="container">
			<div class="row">
				<%LinkedList<PicStore> lsPics = (LinkedList<PicStore>) request.getAttribute("Pics");
	        	if (lsPics == null) {%>
	        		<p>No Pictures found</p>
	       		<%} else {
	       		 
	           	  	String message = (String) request.getAttribute("message");
              if (message != null){
            	  %>
	           	  	<div class="message_banner">
	            	  	<%= message %>
	           	  	</div>
            	  <%
              }
            	  
	       		
	            Iterator<PicStore> iterator;
	            iterator = lsPics.iterator();
	            while (iterator.hasNext()) {
	                PicStore p = iterator.next();
					String username = p.getPostedUsername();

					%>
					
					
	       			<div class = "post">
						<div class = "post_image">
							<a href="/instashutter/Image/<%=p.getSUUID()%>" ><img src="/instashutter/Image/<%=p.getSUUID()%>"></a>
						</div>
					<div class = "post_timestamp">
						<%  //Convert date to iso 8601 format for use with jquery.timeago.js using code from http://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format
						TimeZone timeZone = TimeZone.getTimeZone("UTC");
		    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			    		df.setTimeZone(timeZone);
			    		String timeSince = df.format(p.getPicAdded()); %>
						<div class="timeConvertsince" title="<%= timeSince %>"><%= timeSince %></div>
					</div>
					<style>	
					.profile_background {
		        		position:absolute;
		        		opacity:0.2;
		       			width:100%;
		        		height:100%;
		        		
				      	background: url("/instashutter/Image/<%=p.getSUUID()%>") no-repeat center center fixed; 
							background-size: cover;
							z-index:-1;
						display:block;
		        		}	
								
			        	.post {
			        		z-index:0;
			        	}
			        </style>
				<div class = "post_author">
					
					<a href="/instashutter/profile/<%= username %>"><img src="<%=request.getContextPath()%>/picture/<%= username %>" alt="Profile image" />@<%= username %></a>
				</div>
				<div class = "post_caption">
					<div class="post_desc"><%= p.getCaption() %></div>
				</div>
				<div class = "post_share">
					<form name="delete" action="/instashutter/post/<%= p.getSUUID() %>/delete" method="post" accept-charset="utf-8">
				        <input type="submit" value="delete"></li>  
					</form> 	
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
		</div>
		</div>

</body>
<script src="<c:url value="/assets/js/jquery.timeago.js"/>"></script>
<script src="<c:url value="/assets/js/app.js"/>"></script>
</html>