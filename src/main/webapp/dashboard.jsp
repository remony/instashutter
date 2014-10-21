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
<div class="container">
<div class="row">

	<%
		LinkedList<Pic> lsPics = (LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
			String username = p.getPostedUsername();
        %>
        <div class = "post">
			<div class = "post_image">
				<a href="/instashutter/post/<%=p.getSUUID()%>" ><img src="/instashutter/Thumb/<%=p.getSUUID()%>"></a>
			</div>
			<div class = "post_timestamp">
				<%= p.getPicAdded() %>
			</div>
			<div class = "post_author">
				<a href="/instashutter/profile/<%= username %>">@<%= username %></a>
				<a href="<%=request.getContextPath()%>/picture/<%= username %>"><img src="<%=request.getContextPath()%>/picture/<%= username %>" alt="Profile image" /></a>
			</div>
			<div class = "post_caption">
				<div class="post_desc"><%= p.getCaption() %></div>
			</div>
			
			
			<div class = "post_share">
				
				<!-- Button from  https://about.twitter.com/resources/buttons#tweet  -->
<a href="https://twitter.com/share" class="twitter-share-button" data-url="http://localhost:8080/instashutter/post/<%=p.getSUUID()%>" data-hashtags="instashutter">Tweet</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>			
				
			</div>
			
			<div class = "post_comments">
				<% LinkedList<CommentStore> lsComments = (LinkedList<CommentStore>) p.getCommentlist();
				
				if (lsComments == null) {
				} else { %>
				<%
				Iterator<CommentStore> commentIterator;
				commentIterator = lsComments.iterator();
				while(commentIterator.hasNext()){
					CommentStore c = (CommentStore) commentIterator.next();
					%>
						<!-- Print out comments -->
						<div class="comment_container">
							<div class="comment_username">
								<a href="/instashutter/profile/<%=c.getUsername()%>"><%= c.getUsername() %></a>
							</div>
							<div class="comment_text">
								<%= c.getCommentMessage() %>
							</div>
						</div>
						
					<% } } %>
					<div class="post_comment_form">
						<form name="comment_input" action="/instashutter/dashboard" method="POST">
						<input type="hidden" name="uuid" value="<%=p.getSUUID() %>">
						Comment: <input type="text" name="comment">
						<input type="submit" value="comment">
						</form>
					</div>
			</div>
		</div>
			      

        <%

            }
            }
       
        %>
        
</div>
   
        
        
        <% %>
</body>
</html>