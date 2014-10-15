
<%@page import="me.stuartdouglas.stores.*"%>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">


<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

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
      <a class="navbar-brand" href="#">Instashutter</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/instashutter/">Home</a></li>
  <li><a href="/instashutter/upload">New post</a></li>
  <%
  UserSession currentSession = (UserSession) session.getAttribute("LoggedIn");
if (currentSession != null) {
  String userName = currentSession.getUsername();
  if (currentSession.getUserSession()) {
    %>

  <li><a href="/instashutter/profile/<%=currentSession.getUsername()%>">Your
      Profile</a></li>
  <li><a href="/instashutter/account/<%=currentSession.getUsername()%>">Your
      Account</a></li>
  <li><a href="/instashutter/logout">Sign out</a></li>
  <%}
} else {
    %>

  <li><a href="/instashutter/register">Register</a></li>
  <li><a href="/instashutter/login">Login</a></li>
  <%
  }
    %>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
