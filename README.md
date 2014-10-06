#Instagram clone
A simple instagram clone.

Made for AC32007

##Uri

- ``/`` - ROOT
- ''/upload'' - Upload
- ``/profile/*`` - Profile
- ``/accounts`` - Accounts
  - ``/accounts/edit`` - Edit Accounts
  - ``/accounts/password/change`` - Change password
- ``/login`` - Login
- ``/logout`` - Logout
- ``/register`` - Register


##Page information
###ROOT / Index
####When not logged in
- Display welcome screen and ask to login.
####When logged in
- Display all users posts.
- Allow logout

###Upload
####When not logged in
- redirect to login
####WHen logged in
- Display upload form

###Profile
This is the page where you can see all the posts you have made.
####When not logged in
- Display login
####When logged in
- Display own posts

###Accounts
####When not logged in
- Display login
####When logged in
- Display account options
  - Edit details
  - Change password

###Accounts/edit
- Let the user change details about there user
  - first name
  - last name
  - email
  - Profile picture
###Accounts/password/change
- Let the user change there password
  - requires password to authenticate new password

###Login
- Display the login
- WHen already logged in redirected to homepage

###Register
- Display register form
- If logged in already redirect to homepage

###Logout
- Logs user out of session
- Display message informing user of log out.
