
<%@page import="me.stuartdouglas.stores.*"%>
 

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">



<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
</head>


<style>  
     <%@ include file="assets/css/styles.css"%>  
</style>  



	<header>
		<div class="site_title">
			<h1>InstaShutter</h1>
		</div>
	
	
	<nav>
	<ul>
		<li><a href="/instashutter/">Home</a></li>
		<li><a href="/instashutter/upload">Upload</a></li>
		<%
	UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
	if (currentSession != null) {
		String userName = currentSession.getUsername();
		if (currentSession.getUserSession()) {
			%>
		<li><a href="/instashutter/images/<%=currentSession.getUsername()%>">Your
				Images</a></li>
		<li><a href="/instashutter/profile/<%=currentSession.getUsername()%>">Your
				Profile</a></li>
			<li>Welcome, <%= userName %> <a href="/instashutter/logout">Logout?</a></li>	
		<%}
	} else {
			%>

		<li><a href="/instashutter/register">Register</a></li>
		<li><a href="/instashutter/login">Login</a></li>
		<%
		}
			%>
	</ul>
	</nav>
	</header>
