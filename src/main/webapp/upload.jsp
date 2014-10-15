<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp" /> 

<div class = "post">
	        <table>
		        <tr>
		        	<td>
			        	<div class="post_image">
			        		<img id="imagepreview" src="assets/images/upload.png" alt="Preview of image being uploaded" />
			        	</div>
		        	</td>
		        </tr>
		        <tr>
		        	<td>	
		        		<div class="post_desc">
			        		<form method="POST" id="uploadform" enctype="multipart/form-data" action="Image" >
								<input type="file" name="file" value="/tmp"  onchange="readfile(this);"><br/>
								<textarea rows="4" cols="50" name="description" form="uploadform"></textarea>
								<input type="submit" value="Post">
							</form>
							
							
							
		        		</div>
	        		</td>
	       		</tr>
	        </table>
        </div> 

<script>
function readfile(input)
{
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function (e) {
			$('#imagepreview')
			.attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
		
	}
	
}
/*
	
if (input.files && input.files[0])
        {
 		
              var reader = new FileReader();
             reader.onload = function (e)
                                    {
                                          $('#blah')
                                          .attr('src',e.target.result);
                                    };
             reader.readAsDataURL(input.files[0]);
             }
}*/

</script>



</body>
</html>