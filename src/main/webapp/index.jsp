<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="java.util.*"%>
	<%@ page import="me.stuartdouglas.stores.PicStore" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>


<%
	       		 
	LinkedList<PicStore> lsPics = (LinkedList<PicStore>) request.getAttribute("background");
	String suuid = null;
	String username = null;
	if (lsPics != null)	{
		Iterator<PicStore> iterator;
	    iterator = lsPics.iterator();
	    while (iterator.hasNext()) {
	        PicStore p = iterator.next();
			username = p.getPostedUsername();
			suuid = p.getSUUID().toString();
	    }
	}

%>



<body>

	<jsp:include page="header.jsp" />

	<div class="wrapper">
		<div class="index_message">
		<h1>Welcome to InstaShutter, where you can share images</h1>
		</div>
		<div class="index_login">

			<form name="login" action="login" method="POST" accept-charset="utf-8">  
			    <ul>  
			        <li><label for="username">Username</label>  
			        <input type="text" name="username" placeholder="username" required></li>  
			        <li><label for="password">Password</label>  
			        <input type="password" name="password" placeholder="password" required></li>  
			        <li>  
			        <input type="submit" value="Login"></li>  
			    </ul>  
			</form>  
			<div class="background_owner">
			<h3>Background posted by <%= username %></h3>
		</div>
		</div>
		

	</div>
	
	


</body>
<style>
	html {
		background: url('/instashutter/Image/<%= suuid %>') no-repeat center center fixed; 
		 -webkit-background-size: cover;
		 -moz-background-size: cover;
		 -o-background-size: cover;
		 background-size: cover;
	}

    ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

	ul li {
        background: none none no-repeat 0 5px;
        padding-left: 14px;
	}
	
	input {
    	border: none; 
	}
	
	.wrapper {
		width:80%;
		padding:30px;
		background-color:rgba(50,50,50,0.4);
		margin:0 auto;
		text-align:center;
	}
	.wrapper h1, p, label, h3	{
		color:#fff;
	}
	label {
		font-size:1.3em;
		padding-right:10px;
	}
	

</style>
</html>

