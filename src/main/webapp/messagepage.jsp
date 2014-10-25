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
    <div class="post">
    <%LinkedList<MessageStore> lsPics = (LinkedList<MessageStore>) request.getAttribute("MessageList");
    String otherUsername = request.getAttribute("otherUsername").toString();
	        	if (lsPics == null) {%>
	        		<p>No results</p>
	        		<form name="search" action="/instashutter/search" method="POST">
						<input type="text" class="form-control" name="keyword" placeholder="Search" autofocus>
						<input type="submit" value="comment">
					</form>
	        		 <table style="width:100%">
	       		<%} else {
	            Iterator<MessageStore> iterator;
	            iterator = lsPics.iterator();
	            while (iterator.hasNext()) {
	            	MessageStore p = iterator.next();
					String username = p.getSender();
					String caption = p.getMessage();
					//Convert date to iso 8601 format for use with jquery.timeago.js using code from http://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format
					TimeZone timeZone = TimeZone.getTimeZone("UTC");
	    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		    		df.setTimeZone(timeZone);
					String timeAdded = df.format(p.getDate_sent());
					%>

					<td>
						<tr><div class="timeConvertsince" title="<%= timeAdded %>"><%= timeAdded %></div> </tr>
						<tr><a href="/instashutter/message/<%= username %>">@<%= username %></a></tr>
						<tr><%= caption %></tr>
					</td>

	            <%}} %>
	            </table>
	            <div class="comment_form">
	            	<form name="message_input" action="<%= otherUsername %>/send" method="POST">
						<input type="text" name="message">
						<input type="submit" value="comment">
					</form>
	            </div>
            </div>
  </body>
  <script src="<c:url value="/assets/js/jquery.timeago.js"/>"></script>
	<script src="<c:url value="/assets/js/app.js"/>"></script>
</html>