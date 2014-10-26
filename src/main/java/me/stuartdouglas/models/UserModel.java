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
import java.io.IOException;
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
    
    public boolean RegisterUser(String first_name, String last_name, String username, String password, String email, String location, String bio, ServletContext servletContext){
        //AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
    	
        String EncodedPassword;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        
        Set<String> Email = new HashSet<>();
		Email.add(email);

        Session session = cluster.connect("instashutter");
        PreparedStatement ps = session.prepare("insert into userprofiles (first_name, last_name, login,password, email, location, bio) Values(?,?,?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(first_name, last_name, username,EncodedPassword, Email, location, bio));

        try {

			BufferedImage BI = null;
			String path = "/assets/images/defaultimage.png";
			InputStream imageIn = servletContext.getResourceAsStream(path);
			BI = ImageIO.read(imageIn);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BI, "png", baos);
            baos.flush();
            byte[] b = baos.toByteArray();
            baos.close();
            setProfilePic(b, username ,"png");

			
				
			} catch (Exception e) {
				System.out.println("Error uploading default profile image " + e);
				e.printStackTrace();
			}
        
        
        session.close();
        return true;
    }

    
    
    
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
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
        session.close();
    
    return false;  
    }
    

   
   public LinkedList<UserStore> getUserInfo(String user) {
   	LinkedList<UserStore> userList = new LinkedList<>();
   	Session session = cluster.connect("instashutter");
   	PreparedStatement ps = session.prepare("SELECT * from userprofiles where login =?");
    ResultSet rs;
    BoundStatement boundStatement = new BoundStatement(ps);
    rs = session.execute(boundStatement.bind(user));
    if (rs.isExhausted()) {
        System.out.println("No Images returned");
    } else {
    	UserStore ps1 = new UserStore();
        for (Row row : rs) {
   			ps1.setAll(row.getString("login"), row.getString("first_name"), row.getString("last_name"));
   			ps1.setBackground(row.getString("background"));
   			ps1.setLocation(row.getString("location"));
   			ps1.setBio(row.getString("bio"));
   			ps1.setEmail(row.getSet("email", String.class));
   			ps1.setFollowerCount(getFollowerCount(user));
   			ps1.setFollowingCount(getFollowingCount(user));
   			ps1.setPostCount(getPostCount(user));
   			userList.add(ps1);
        }
    }
   	
   	session.close();
		return userList;
   }
   
   public int getFollowerCount(String username)	{
	   int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select friend from friends where friend=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
              System.out.println("User has posted no posts");
              
          } else {
       	   for (@SuppressWarnings("unused") Row row : rs)	{
       		   count++;
       	   }
          }
			System.out.println(count);
		}	catch(Exception e)	{
			System.out.println("Error deleting post" + e);
		}
		return count;
   }
   
   public int getFollowingCount(String username)	{
	   int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select friend from friends where username=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
              System.out.println("User has posted no posts");
              
          } else {
       	   for (@SuppressWarnings("Used as counting me") Row row : rs)	{
       		   count++;
       	   }
          }
			System.out.println(count);
		}	catch(Exception e)	{
			System.out.println("Error deleting post" + e);
		}
		return count;
   }
   
   public int getPostCount(String username)	{
		int count = 0;
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select user from userpiclist where user=? allow filtering");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
               System.out.println("User has posted no posts");
               
           } else {
        	   for (Row row : rs)	{
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
        this.cluster = cluster;
    }

   public String CurrentSessionUser(HttpServletRequest request) {
	   String username = request.getSession().getAttribute("user").toString();
	   if (username != null){
		   return username;
	   } else {
		   return null;
	   }
	   
   }

	public void updateUserDetails(String username, String fname, String lname, String location) {
		// TODO Auto-generated method stub
		System.out.println("yay");
		Session session = cluster.connect("instashutter");
		PreparedStatement ps = session.prepare("UPDATE userprofiles set first_name = ?, last_name = ?, location = ? where login = ?");
		ResultSet rs;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(fname, lname, location, username));
		session.close();
		if (rs.isExhausted()){
			System.out.println("something went wrong");
			//return null;
		} else {
			System.out.println("success");
			//return true;
		}
		
		
	}
	
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
	
	public void updateUserPassword(String username, String password) {
		// TODO Auto-generated method stub
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
			//return null;
		} else {
			System.out.println("success");
			//return true;
		}
		
		
	}
	
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
	
	
	
	public void setProfilePic(byte[] b, String filename, String type) {
		try {
			
			System.out.println("Starting upload of profile image");
			new Convertors();
            ByteBuffer.wrap(b);
        
            String types[]=Convertors.SplitFiletype(type);
			FileOutputStream output = new FileOutputStream(new File("/var/tmp/instashutter/" + filename));
            output.write(b);
            
            byte [] profilepic =profileImage(filename,type);
            ByteBuffer picbuf=ByteBuffer.wrap(profilepic);

            Session session = cluster.connect("instashutter");
            
            PreparedStatement psInsertPic = session.prepare("update userprofiles set profileimage=? where login=?");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);

            session.execute(bsInsertPic.bind(picbuf,filename));
            output.close();
            session.close();
			
			
		}	catch (Exception e) {
			System.out.println("Error Setting profile picture: " + e);
			e.printStackTrace();
		}
	}
	

	private byte[] profileImage(String filename, String type) {
		// TODO Auto-generated method stub
		try {
			BufferedImage BI = ImageIO.read(new File("/var/tmp/instashutter/" + filename));
			BufferedImage profileImage = createImage(BI);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(profileImage, type, baos);
			baos.flush();
			
			byte[] imageOutput = baos.toByteArray();
			baos.close();
			return imageOutput;
			
			
		}	catch (Exception e)	{
			System.out.println("Error: " + e);
		}
		return null;
	}
	
	private static BufferedImage createImage(BufferedImage img)
    {
    	img = resize(img, Method.SPEED, 300, OP_ANTIALIAS);
        return pad(img, 1);
    }

	public void updateBackground(String username, String url) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("update userprofiles set background=? where login=?");
            BoundStatement bs = new BoundStatement(ps);

            session.execute(bs.bind(url, username));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error change user background: " + e);
		}
	}

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
	
	public boolean isUserRegistered(String username)	{
		try {
			Session session = cluster.connect("instashutter");
			PreparedStatement ps = session.prepare("select login from userprofiles where login=?");
			BoundStatement bs = new BoundStatement(ps);
			ResultSet rs = session.execute(bs.bind(username));
			session.close();
			if (rs.isExhausted()) {
                System.out.println("No profile image returned");
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

	public void followUser(String username, String followingUser) {
		// TODO Auto-generated method stub
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
	
	public void unFollowUser(String username, String followingUser) {
		// TODO Auto-generated method stub
		try {
			if (!isFollowing(username, followingUser))	{
				System.out.println("Not executing unfollow");
			}	else	{
				Session session = cluster.connect("instashutter");
				Date dateFollowed = new Date();
				PreparedStatement ps = session.prepare("delete from friends where username = ? and friend = ?");
				BoundStatement bs = new BoundStatement(ps);
				ResultSet rs = session.execute(bs.bind(username, followingUser));
				session.close();
			}
			
		}	catch(Exception e)	{
			System.out.println("Error following user" + e);
		}
	}
	
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
        .sorted((e1, e2) -> e2.getFollowedSince()
                .compareTo(e1.getFollowedSince()))
        .forEach(sortedFollowingList::add);
    	
		return sortedFollowingList;
		
    }
	
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
        .sorted((e1, e2) -> e2.getFollowedSince()
                .compareTo(e1.getFollowedSince()))
        .forEach(sortedFollowingList::add);
    	
		return sortedFollowingList;
		
    }

	public void deleteUser(String username) {
		try {
			Session session = cluster.connect("instashutter");
            
            PreparedStatement ps = session.prepare("update userprofiles set bio=? where login=?");
            BoundStatement bs = new BoundStatement(ps);

            session.execute(bs.bind(username));

            session.close();
		}	catch (Exception e)	{
			System.out.println("Error change user background: " + e);
		}
		
	}

}