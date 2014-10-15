<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="me.stuartdouglas.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
 
        <article>







        <%
        List<Pic> lsPics = (List<Pic>)request.getAttribute("Pics");
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
        <%-- <%= p.getTitle() %>
       	<a href="/instashutter/profile/<%= p.getPostedUsername() %>">@<%= p.getPostedUsername() %></a> --%>
       	Title:<%= p.getCaption() %>
        <a href="/instashutter/Image/<%=p.getSUUID()%>" ><img src="/instashutter/Thumb/<%=p.getSUUID()%>"></a><br/>
        
        
		<form name="input" action="/instashutter/Images/" method="get">
		
		<input type="text" name="user" value="<%=p.getSUUID()%>">
		<input type="submit" value="Submit">
		</form>
		
		
<%

            }
            }
        %>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
