#Instashutter
A simple webapp Instagram clone using Cassandra and written in Java.
---------------------

This webapp written in Java is a simple take on cloning Instashutter as part of an assignment [details to follow after assignment deadline]. 
The task of the assignment was by using provided barebone structure find and fix errors and extend the application.

##Features

- Posts images
	- Any image format including GIF's
	- Post image as public or private
		- private images will not appear on the public explore
	- Set a caption
	- Apply filters
- Send messages
	- For people who want to send a message to another user this is made possible by going into /message and selecting the follower you wish to send a message to!
- View other peoples profiles
	- View peoples profiles which includes
		- user information
			- Following, followers
			- user bio, etc.
		- user posts
		- Custom profile background (set url of image in accounts)
- View posts (clicking on a post image will come up with post view)
	- View enchanced larger image
	- delete image (for posted users only)
- Explore: A timeline of all public posts made by all users
- Dashboard/index: A timeline of all posts made by you and the people you follow
- Search: search for posts which captions contain the keyword you define
- Accounts: Edit all of your user information (excluding username)
- And many more.

##Application notes
- Making use of feature of Java 8
- Built using maven
- Using Java 1.8.0_20
- Written using Eclipse

##URI information

- /
	- GET
    - Homepage
      - When user is not logged in this is a homepage displaying login and public features
      - When logged in this is your dashboard
  _ POST
    - Posts comments
- /profile/{username}
  - Displays profiles
- /explore
  - Displays public posts from all users
- /message  | /message/{username}
  - Lets you message users you follow
- /upload
  - Displays page to let you post images
- /account
  - Displays account settings and info
- /search/{Keyword}
  - Searchs public posts with defined keyword
- /post/{UUID} 
  - Displays enchanced image with full comments
- /image/{UUID}
  - Displays original image only
- /picture/{username}
  - Displays profile image

##Cassandra information
- Keyspace: instashutter
- Tables:
  - userprofiles : user profile data
  - pics : picture data
  - userpiclist : user picture data
  - messaging : messaging data
  - comments : comments data
  - friends : following/follower data
