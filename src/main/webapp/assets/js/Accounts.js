/*
 * 	Title: Toggle_hidden
 * 	Author: Stuart Douglas
 * 
 * 
 * 	A simple js file for the menu in /accounts
 * 
 * 	Hides and unhides elements of settings to keep the ui smaller and to show what it needed.
 * 
 *  Advantages: 1 file instead of multiple jsp's.
 * 
 */
function toggle_hidden(id) {
   var e = document.getElementById(id);
   if (id == "details") {
	   var id = ["password", "profileimage", "bio", "profileBackground", "account_message", "profileEmail"];
	   for (var i = 0; i < id.length; i++){
		   var tmp = document.getElementById(id[i]);
		   tmp.style.display = 'none';
	   } 
   } else if (id == "password") {
	   var id = ["details", "profileimage", "bio","profileBackground", "account_message", "profileEmail"];
	   for (var i = 0; i < id.length; i++){
		   var tmp = document.getElementById(id[i]);
		   tmp.style.display = 'none';
	   }
   } else if (id == "profileimage") {
	   var id = ["details", "password", "bio","profileBackground", "account_message", "profileEmail"];
	   for (var i = 0; i < id.length; i++){
		   var tmp = document.getElementById(id[i]);
		   tmp.style.display = 'none';
	   }
   }	else if (id == "bio")	{
	   var id = ["details", "profileimage","profileBackground", "profileimage", "account_message", "profileEmail"];
	   for (var i = 0; i < id.length; i++){
		   var tmp = document.getElementById(id[i]);
		   tmp.style.display = 'none';
	   }
   }	else if (id == "profileBackground")	{
	   var id = ["details", "profileimage","password", "profileimage", "bio", "profileEmail"];
	   for (var i = 0; i < id.length; i++){
		   var tmp = document.getElementById(id[i]);
		   tmp.style.display = 'none';
	   }
   }	else if (id == "profileEmail") {
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
