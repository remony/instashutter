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

<div class="profile_background"></div>
<jsp:include page="header.jsp" />

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

        %>
        <style>
        	.profile_background {
        		position:absolute;
        		width:110%;
        		height:110%;
		      	background: url(/instashutter/Image/<%=p.getSUUID()%>) no-repeat center center fixed; 
				-webkit-background-size: cover;
				-moz-background-size: cover;
				-o-background-size: cover;
				background-size: cover;
				z-index:-1;
				display:block;
				-webkit-filter: blur(25px);
    -moz-filter: blur(15px);
    -o-filter: blur(15px);
    -ms-filter: blur(15px);
    filter: blur(15px);
    opacity: 0.7;
        	
        	}
        	.post {
        		z-index:0;
        	}
        </style>
        <div class = "post">
			<div class = "post_image">
				<a href="/instashutter/Image/<%=p.getSUUID()%>" ><img src="/instashutter/Thumb/<%=p.getSUUID()%>"></a>
			</div>
			<div class = "post_timestamp">
				<%= p.getPicAdded() %>
			</div>
			<div class = "post_author">
				<a href="/instashutter/profile/<%= p.getPostedUsername() %>">@<%= p.getPostedUsername() %></a>
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
				<p>Comments coming soon</p>
			</div>
		</div>
			      

        <%

            }
            }
       
        %>
</body>
</html>