
<%@page import="me.stuartdouglas.stores.*"%>



<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
</head>


<style>
     <%@ include file="assets/css/styles.css"%>
</style>


<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/instashutter/">Instashutter</a>
    </div>
	<%
		UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
		if (currentSession != null) {
		  String userName = currentSession.getUsername();
		  String background = currentSession.getBackground();
		  if (currentSession.getUserSession()) {
    %>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
      <li class=""><a href="/instashutter/dashboard">Timeline</a></li>
      <li class=""><a href="/instashutter/explore">Explore</a></li>
        <li class=""><a href="/instashutter/upload">New Post</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/instashutter/profile/<%= userName%>">View profile</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%= userName%> <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/instashutter/account">Account settings</a></li>
            <li class="divider"></li>
            <li><a href="/instashutter/logout">Log out</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
    <%}} else {%>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right">
      <li><a href="/instashutter/explore">Explore</a></li>
        <li><a href="/instashutter/login">Login</a></li>
        <li><a href="/instashutter/register">Register</a></li>
        
      </ul>
    </div><!-- /.navbar-collapse -->
    
    
    
    <%}%>
  </div><!-- /.container-fluid -->
</nav>