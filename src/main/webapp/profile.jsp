<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="me.stuartdouglas.stores.*" %>
    <%@page import="java.util.*"%>
    <%
    String user = request.getAttribute("viewingUser").toString();
    String background = null;
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= user %>'s Profile</title>
</head>
<body>


<jsp:include page="header.jsp" />
<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {%>
			<div class="profile_header">
				<div class="profile_background">
					<div class="profile_content">
					<a href="<%=request.getContextPath()%>/picture/<%= user %>"><img src="<%=request.getContextPath()%>/picture/<%= user %>" alt="Profile image" /></a>
						

	<%
       				LinkedList<UserSession> userInfo = (LinkedList<UserSession>) request.getAttribute("UserInfo");
       				if (userInfo == null) {
       			%>
       				<h4>No access: you are not logged in.</h4>
   				<%
       				}	else	{
       					Iterator<UserSession> iterator;
       					iterator = userInfo.iterator();
       					while (iterator.hasNext()) {
       						UserSession p = (UserSession) iterator.next();
       						background = p.getBackground();
       			%>
					First name: <%= p.getfname() %><br>			
					Last name: <%= p.getlname() %><br>
					Location: <%= p.getLocation() %>
       			
       			<%       			
       					}
       				}
   				%>






						<a href="/instashutter/follow/<%= user %>">Follow</a>
						<div class="profile_follow">
							<a href="/instashutter/account/editdetails">Edit your profile</a>
						</div>
					</div>		
				</div>
			</div>
			
			
			
			
			
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
        <div class = "post">
			<div class = "post_image">
				<a href="/instashutter/Image/<%=p.getSUUID()%>" ><img src="/instashutter/Thumb/<%=p.getSUUID()%>"></a>
			</div>
			<div class = "post_timestamp">
				<%= p.getPicAdded() %>
			</div>
			<div class = "post_author">
				<a href="/instashutter/profile/<%= request.getAttribute("viewingUser") %>">@<%= request.getAttribute("viewingUser") %></a>
			</div>
			<div class = "post_caption">
				<div class="post_desc"><%= p.getCaption() %></div>
			</div>
			
			
			<div class = "post_share">
				<p>Sharing coming soon</p>
			</div>
			
			<div class = "post_comments">
				<p>Comments coming soon</p>
			</div>
		</div>

<%

            }
            }
        %>
			
			
			
			
			
		<%}} else {%>
		<p>You shouldn't be here</p>
		<%}%>
		

<style>
	body {
	background: url("<%= background %>") no-repeat center center fixed; 
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
	}

</style>


</body>
</html>