
function toggle_hidden(id) {
				       var e = document.getElementById(id);
				       if (id == "details") {
				    	   var id = ["password", "profileimage", "bio", "profileBackground", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   } 
				       } else if (id == "password") {
				    	   var id = ["details", "profileimage", "bio","profileBackground", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       } else if (id == "profileimage") {
				    	   var id = ["details", "password", "bio","profileBackground", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       }	else if (id == "bio")	{
				    	   var id = ["details", "profileimage","profileBackground", "profileimage", "account_message"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       }	else if (id == "profileBackground")	{
				    	   var id = ["details", "profileimage","profileBackground", "profileimage", "bio"];
				    	   for (var i = 0; i < id.length; i++){
				    		   var tmp = document.getElementById(id[i]);
				    		   tmp.style.display = 'none';
				    	   }
				       }	else	{
				    		console.log("Error unknown ID");   
				       }
				          e.style.display = 'block';
				    }
