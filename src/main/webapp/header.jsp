<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
      <a class="navbar-brand" href="<c:url value="/"/>">Instashutter</a>
    </div>
	<%
		UserStore currentSession = (UserStore) session.getAttribute("LoggedIn");
		if (currentSession != null) {
		  String userName = currentSession.getUsername();
		  //String background = currentSession.getBackground();
		  if (currentSession.getUserSession()) {
    %>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    
      <ul class="nav navbar-nav">
      <li class=""><a href="<c:url value="/"/>"><span class="glyphicon glyphicon-home"></span> Dashboard</a></li>
      <li class=""><a href="<c:url value="/explore"/>"><span class="glyphicon glyphicon-globe"></span> Explore</a></li>
      <li class=""><a href="<c:url value="/message"/>"><span class="glyphicon glyphicon-globe"></span> Messages</a></li>
        <li class=""><a href="<c:url value="/upload"/>"><span class="glyphicon glyphicon-upload"></span> New Post</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
      
        
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-search"></span><span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <form class="navbar-form navbar-left"  action="/instashutter/search" role="search" method="POST">
	        <div class="form-group">
	        	<input type="text" class="form-control" name="keyword" placeholder="Search posts">
	        </div>
	      </form>
          </ul>
        </li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-th-list"></span><%= userName%> <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
          	<li><a href="/instashutter/profile/<%= userName%>">View profile</a></li>
            <li><a href="<c:url value="/account"/>"><span class="glyphicon glyphicon-th-list"></span>Account settings</a></li>
            <li class="divider"></li>
            <li><a href="<c:url value="/logout"/>">Log out</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
    <%}} else {%>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right">
      <form class="navbar-form navbar-left"  action="/instashutter/search" role="search" method="POST">
        <div class="form-group">
          <input type="text" class="form-control" name="keyword" placeholder="Search posts">
        </div>
      </form>
      <li><a href="<c:url value="/explore"/>">Explore</a></li>
        <li><a href="<c:url value="/login"/>">Login</a></li>
        <li><a href="<c:url value="/register"/>">Register</a></li>
        
      </ul>
    </div><!-- /.navbar-collapse -->
    
    
    
    <%}%>
  </div><!-- /.container-fluid -->
</nav>