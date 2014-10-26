package me.stuartdouglas.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;



import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.imgscalr.Scalr.*;
import me.stuartdouglas.lib.*;
import me.stuartdouglas.stores.FollowingStore;
import me.stuartdouglas.stores.UserStore;

/**
 *
 * @author Administrator
 */
public class UserModel {
    private static Cluster cluster;
    
    public UserModel(){
        
    }
    
    /*
     *  Registers the user to the databast
     * 
     * 
     */
    
    public boolean RegisterUser(String first_name, String last_name, String username, String password, String email, String location, String bio, ServletContext servletContext){
    	//Encodes the password
        String EncodedPassword;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        
        //adds email to the hashset
        Set<String> Email = new HashSet<>();
		Email.add(email);

        Session session = cluster.connect("instashutter");
        //prepares the query
        PreparedStatement ps = session.prepare("insert into userprofiles (first_name, last_name, login,password, email, location, bio) Values(?,?,?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(first_name, last_name, username,EncodedPassword, Email, location, bio));
        //Uploads a default image
        try {

			BufferedImage BI = null;
			//Loads in the default image
			String path = "/assets/images/defaultimage.png";
			InputStream imageIn = servletContext.getResourceAsStream(path);
			BI = ImageIO.read(imageIn);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BI, "png", baos);
            baos.flush();
            byte[] b = baos.toByteArray();
            baos.close();
            //Using the same method for updating profile images, it uploads it to the database
            setProfilePic(b, username ,"png");
			} catch (Exception e) {
				System.out.println("Error uploading default profile image " + e);
				e.printStackTrace();
			}

        session.close();
        return true;
    }

    
    
    //Checks if the user is valid of not
    public boolean IsValidUser(String username, String Password){
        String EncodedPassword;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instashutter");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(boundStatement.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No password returned");
            return false;
        } else {
            for (Row row : rs) {
            	//if the password is the same as the encoded password return true
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
        session.close();
    //password is wrong so return false, user is not valid or entered wrong details
    return false;  
    }
    

   //Gets all the use information
   public LinkedList<UserStore> getUserInfo(String user) {
   	LinkedList<UserStore> userList = new LinkedList<>();
   	Session session = cluster.connect("instashutter");
   	PreparedStatement ps = session.prepare("SELECT * from userprofiles where login =?");
    ResultSet rs;
    BoundStatement boundStatement = new BoundStatement(ps);
    rs = session.execute(boundStatement.bind(user));
    if (rs.isExhausted()) {
        System.out.println("No info returned");
    } else {
    	UserStore userS = new UserStore();
        for (Row row : rs) {
        	userS.setAll(row.getString("login"), row.getString("first_name"), row.getString("last_name"));
        	userS.setBackground(row.getString("background"));
        	userS.setLocation(row.getString("location"));
        	userS.setBio(row.getString("bio"));
        	userS.setEmail(row.getSet("email", String.class));
        	userS.setFollowerCount(getFollowerCount(user));
        	userS.setFollowingCount(getFollowingCount(user));
        	userS.setPostCount(getPostCount(user));
   			userList.add(userS);
        }
    }
   	//closes the session and returns the list
   	session.close();
	return userList;
   }
   
   //returns the amount of followers a user has
   public int getFollowerCount(String username)	{
	   int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select friend from friends where friend=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
              System.out.println("User has posted no followers");
              
          } else {
       	   for (@SuppressWarnings("unused") Row row : rs)	{
       		   count++;
       	   }
          }
			System.out.println(count);
		}	catch(Exception e)	{
			System.out.println("Error counting users" + e);
		}
		return count;
   }
   
   //returns the amount of people a user is following
   public int getFollowingCount(String username)	{
	   int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select friend from friends where username=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
              System.out.println("User is not following anyone");
              
          } else {
       	   for (@SuppressWarnings("unused") Row row : rs)	{
       		   count++;
       	   }
          }
			System.out.println(count);
		}	catch(Exception e)	{
			System.out.println("Error counting users" + e);
		}
		return count;
   }
   
   //returns the amount of posts a user has made
   public int getPostCount(String username)	{
		int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select user from userpiclist where user=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
               System.out.println("User has no posts");
               
           } else {
        	   for (@SuppressWarnings("unused") Row row : rs)	{
        		   count++;
        	   }
           }
			System.out.println(count);
		}	catch(Exception e)	{
			System.out.println("Error counting posts" + e);
		}
		return count;
       
	}
    
    
   public void setCluster(Cluster cluster) {
        UserModel.cluster = cluster;
    }

   public String CurrentSessionUser(HttpServletRequest request) {
	   String username = request.getSession().getAttribute("user").toString();
	   if (username != null){
		   return username;
	   } else {
		   return null;
	   }
	   
   }
   
   
   //Updates the user details
	public void updateUserDetails(String username, String fname, String lname, String location) {
		Session session = cluster.connect("instashutter");
		PreparedStatement ps = session.prepare("UPDATE userprofiles set first_name = ?, last_name = ?, location = ? where login = ?");
		ResultSet rs;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(fname, lname, location, username));
		session.close();
		if (rs.isExhausted()){
			System.out.println("something went wrong");
		} else {
			System.out.println("success");
		}
	}
	
	//Updates users email
	public void updateUserEmail(String username, Set<String> a)	{
		System.out.println("User " + username + " is updating there email.");
		Session session = cluster.connect("instashutter");
		PreparedStatement ps = session.prepare("update userprofiles set email =? where login=?");
		ResultSet rs;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(a, username));
		session.close();
		if(rs.isExhausted())	{
			System.out.println("User " + username + "Edited email successfully");
		} else {
			System.out.println("User " + username + "Edited email unsuccessfully, No change was made");
			
		}
	}
	
	//updates users password
	public void updateUserPassword(String username, String password) {
		System.out.println("Editing user password");
		String EncodedPassword=null;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
        }
		Session session = cluster.connect("instashutter");
		PreparedStatement ps = session.prepare("UPDATE userprofiles set password = ? where login = ?");
		ResultSet rs;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(EncodedPassword, username));
		session.close();
		if (rs.isExhausted()){
			System.out.println("something went wrong");
		} else {
			System.out.println("success");
		}
	}
	
	//gets a users profile picture
	public UserStore getProfilePic(String user)
    {
		ByteBuffer bImage = null;
		Session session = cluster.connect("instashutter");
        
        try {
            ResultSet rs;
            PreparedStatement ps;
            ps = session.prepare("select profileimage from userprofiles where login =?");
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(user));

            if (rs.isExhausted()) {
                System.out.println("No profile image returned");
                return null;
            } else {
                for (Row row : rs) {         	
                        bImage = row.getBytes("profileimage");                
                }
            }
        }	catch (Exception e)	{
            System.out.println("Unable to get profile image: " + e);
            return null;
        }
        session.close();
        UserStore p = new UserStore();
        p.setPic(bImage);
                       
        return p;

    }
	
	
	//sets a profile picture for the user
	public void setProfilePic(byte[] b, String username, String type) {
		try {
			
			System.out.println("Starting upload of profile image");
            ByteBuffer.wrap(b);

			FileOutputStream output = new FileOutputStream(new File("/var/tmp/instashutter/" + username));
            output.write(b);
            
            byte [] profilepic =b;
            ByteBuffer picBuffer = ByteBuffer.wrap(profilepic);

            Session session = cluster.connect("instashutter");
            
            PreparedStatement psInsertPic = session.prepare("update userprofiles set profileimage=? where login=?");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);

            session.execute(bsInsertPic.bind(picBuffer,username));
            output.close();
            
            //Deleting uploaded image from filesystem
            try {
            	File location = new File("/var/tmp/instashutter/" + username);
                if (location.exists()) {
                	location.delete(); 
                }
            	
            }	catch(Exception e)	{
            	System.out.println("Error deleting file: " + e);
            }
            session.close();
			
			
		}	catch (Exception e) {
			System.out.println("Error Setting profile picture: " + e);
		}
	}
	
	//sets the background url for the users profile
	public void updateBackground(String username, String url) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("update userprofiles set background=? where login=?");
            BoundStatement bs = new BoundStatement(ps);

            session.execute(bs.bind(url, username));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error changing user background: " + e);
		}
	}
	
	
	//Updates the users bio
	public void updateBio(String username, String bio) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("update userprofiles set bio=? where login=?");
            BoundStatement bs = new BoundStatement(ps);

            session.execute(bs.bind(bio, username));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error change user background: " + e);
		}
		
	}
	
	//Returns true if the user is registered or false if not
	public boolean isUserRegistered(String username)	{
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select login from userprofiles where login=?");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
                System.out.println("No user returned");
                return false;
            } else {
                return true;
            }
			
			
		}	catch	(Exception e)	{
			System.out.println("Error checking if user exists: " + e);
		}
		return false;
	}
	
	

	public void destroy()	{
		try {
			if(cluster != null) cluster.close();
			System.out.println("cluster in User closed");
		}	catch(Exception e)	{
			System.out.println("error closing cassandra connection " + e);
			e.printStackTrace();
		}
	}

	//followers a user
	public void followUser(String username, String followingUser) {
		try {
			if (!isFollowing(username, followingUser))	{
				Session session = cluster.connect("instashutter");
				Date dateFollowed = new Date();
				PreparedStatement ps = session.prepare("insert into friends (username, friend, since) Values(?,?,?)");
				BoundStatement bs = new BoundStatement(ps);
				ResultSet rs = session.execute(bs.bind(username, followingUser, dateFollowed));
				session.close();
				if (rs.isExhausted()) {
	                System.out.println("User has not followed");
	            } else {
	            	System.out.println("User has followed");
	            }
			}	else	{
				System.out.println("Not executing following");
			}
			
		}	catch(Exception e)	{
			System.out.println("Error following user" + e);
		}
	}
	
	//Removes a user from the users friend list
	public void unFollowUser(String username, String followingUser) {
		try {
			if (!isFollowing(username, followingUser))	{
				System.out.println("Not executing unfollow");
			}	else	{
				Session session = cluster.connect("instashutter");

				PreparedStatement ps = session.prepare("delete from friends where username = ? and friend = ?");
				BoundStatement bs = new BoundStatement(ps);
				@SuppressWarnings("unused")
				ResultSet rs = session.execute(bs.bind(username, followingUser));
				session.close();
			}
			
		}	catch(Exception e)	{
			System.out.println("Error following user" + e);
		}
	}
	
	//checks if a user if following another user
	public boolean isFollowing(String username, String followingUser)	{
		try {
			Session session = cluster.connect("instashutter");
			//Date dateFollowed = new Date();
			PreparedStatement ps = session.prepare("select friend from friends where username =?");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
                System.out.println("Invalid username");
				return false;
            } else {
	            	for (Row row : rs)	{
	                    if (row.getString("friend").equals(followingUser))	{
	                        System.out.println("user " + username + " is following user " + followingUser);
	                        return true;
	                    }	else {
	                        System.out.println("user " + username + " is not following user " + followingUser);
	                        return false;
	                    }
		    		}
            	}
            	return true;
            }	catch(Exception e)	{
    			System.out.println("Error following user" + e);
    		}
		return false;
	}
	
	
	//returns a list of followers a user is following
	public static LinkedList<FollowingStore> getFollowing(String username) {
    	LinkedList<FollowingStore> followingList = new LinkedList<>();
    	LinkedList<FollowingStore> sortedFollowingList = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from friends where username =?");
    	
    	BoundStatement bs = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(bs.bind(username));
    	if (rs.isExhausted()){
    		System.out.println("No followers returned from model");
    		return followingList;
    	} else {
    		for (Row row : rs) {
    			FollowingStore fs = new FollowingStore();
    			fs.setUsername(row.getString("username"));
    			fs.setFollowing(row.getString("friend"));
    			fs.setFollowedSince(row.getDate("since"));
    			followingList.add(fs);
    		}
    		session.close();
    	}
    	//Sorting posts by most recent followed first
    	followingList.stream()
        .sorted((friend1, friend2) -> friend2.getFollowedSince()
                .compareTo(friend1.getFollowedSince()))
        .forEach(sortedFollowingList::add);
    	
		return sortedFollowingList;
		
    }
	
	//returns the list of followers
	public static LinkedList<FollowingStore> getFollowers(String username) {
    	LinkedList<FollowingStore> followingList = new LinkedList<>();
    	LinkedList<FollowingStore> sortedFollowingList = new LinkedList<>();
    	Session session = cluster.connect("instashutter");
    	
    	PreparedStatement statement = session.prepare("SELECT * from friends where friend =? allow filtering");
    	
    	BoundStatement bs = new BoundStatement(statement);
    	
    	ResultSet rs = session.execute(bs.bind(username));
    	if (rs.isExhausted()){
    		System.out.println("No followers returned from model");
    		return followingList;
    	} else {
    		for (Row row : rs) {
    			FollowingStore fs = new FollowingStore();
    			fs.setFollowing(row.getString("username"));
    			fs.setFollowedSince(row.getDate("since"));
    			followingList.add(fs);
    		}
    		session.close();
    	}
    	//Sorting posts by most recent followed first
    	followingList.stream()
        .sorted((friend1, friend2) -> friend2.getFollowedSince()
                .compareTo(friend1.getFollowedSince()))
        .forEach(sortedFollowingList::add);
    	
		return sortedFollowingList;
		
    }

	

}