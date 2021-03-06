<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload post</title>
<link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<script  src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/camanjs/4.0.0/caman.full.min.js"></script>



</head>
<body>
<jsp:include page="header.jsp" /> 

<div class = "post">
	        
	<div class="post_image"><!-- assets/images/upload.png -->
		<img id="imagepreview" src="assets/images/upload.png" alt="Preview of image being uploaded" />
	</div>
	<form method="POST" id="uploadform" enctype="multipart/form-data" action="Image" >
	<div class="post_timestamp">
		<input type="file" id="file" name="file" value="/image"  onchange="readfile(this);" required><br/>
	</div>

	<div class="post_caption">
	<p><h3>Caption</h3>enter the text you want with your image</p>
	<textarea rows="4" cols="50" name="description" form="uploadform"></textarea><br>
	<h4>Do you want this photo to be public?</h4>
	<input type="radio" name="public" value="yes" checked>Yes<br>
	<input type="radio" name="public" value="no">No<br>
	
	
	<h2>Filters</h2>
	<p>Filters shown in preview are limited to the capabilities of css.</p>
	<input type="radio" name="filterChoice" value="colour" onclick = 'filter("colour")'checked>Colour [default]
	<input type="radio" name="filterChoice" value="bw" onclick = 'filter("bw")'>Black and White<br>
	<input type="radio" name="filterChoice" value="invert" onclick = 'filter("invert")'>Invert<br>
	<input type="radio" name="filterChoice" value="exposure" onclick = 'filter("exposure")'>Exposure<br>
	<input type="radio" name="filterChoice" value="pointillize" onclick = 'filter("pointillize")'>Pointillize<br>
	<input type="radio" name="filterChoice" value="crystallize" onclick = 'filter("crystalliz")'>Crystallize<br>
	<input type="radio" name="filterChoice" value="pointillize" onclick = 'filter("pointillize")'>PointillizeFilter<br>
	
	

	
	</div>
	<div class="post_comments">
	<button class="btn btn-default" type="submit">Post<span class="glyphicon glyphicon-send"></button>
	</div>
   		
			
		</form>
</div>

<script>
var url = null;

function readfile(input)
{
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function (e) {
			url = e.target.result;
			$('#imagepreview')
			.attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
		
	}
	
}
/*
 *	A simple js function which applies an effect to the image preview  
 *	to give the user a inside of what kind of image they will be getting.
 */
function filter(type) {
	var filter = type.toString();
	if (filter == "bw"){
		imagepreview.style.webkitFilter = "grayscale(1)";
		imagepreview.style.Filter = "grayscale(1)";
		
	} else if (filter == "colour")	{
		imagepreview.style.webkitFilter = "grayscale(0)";
		imagepreview.style.Filter = "grayscale(0)";
	} else if (filter == "invert")	{
		imagepreview.style.webkitFilter = "invert(.8)";
		imagepreview.style.Filter = "invert(.8)";
	} else if (filter == "sepia")	{
		imagepreview.style.webkitFilter = "sepia(1)";
		imagepreview.style.Filter = "sepia(1)";
	} else if (filter == "saturate")	{
		imagepreview.style.webkitFilter = "saturate(8)";
		imagepreview.style.Filter = "saturate(8)";
	} else {
		imagepreview.style.webkitFilter = "grayscale(0)";
		imagepreview.style.Filter = "grayscale(0)";
	}
}
</script>



</body>
</html>