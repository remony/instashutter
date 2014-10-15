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
	        <table>
		        <tr>
		        	<td>
			        	<div class="post_image">
			        		<a href="/instashutter/Image/<%=p.getSUUID()%>" ><img src="/instashutter/Thumb/<%=p.getSUUID()%>"></a>
			        	</div>
		        	</td>
		        </tr>
		        <tr>
	        		<td>
		        		<div class="post_header">
		        			<a href="/instashutter/profile/<%= p.getPostedUsername() %>">@<%= p.getPostedUsername() %></a>
		        		</div>
	        		</td>
	        	</tr>
		        <tr>
		        	<td>	
		        		<div class="post_desc"><%= p.getCaption() %></div>
		        		<%= p.getPicAdded() %>
	        		</td>
	       		</tr>
	        </table>
        </div> 
        <%

            }
            }
       
        %>
        
        
        
        <% %>
</body>
</html>