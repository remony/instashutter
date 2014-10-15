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
<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {%>
			<h3><%= request.getAttribute("viewingUser") %></h3>
			<a href="/instashutter/follow/<%= request.getAttribute("viewingUser") %>">Follow</a>
			
			
			
			
			
			<%
        java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
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
		




</body>
</html>