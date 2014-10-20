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
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;



import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import me.stuartdouglas.lib.*;
import me.stuartdouglas.stores.UserSession;

/**
 *
 * @author Administrator
 */
public class User {
    private Cluster cluster;
    
    public User(){
        
    }
    
    public boolean RegisterUser(String first_name, String last_name, String username, String Password){
        //AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instashutter");
        PreparedStatement ps = session.prepare("insert into userprofiles (first_name, last_name, login,password) Values(?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        first_name, last_name, username,EncodedPassword));
        //We are assuming this always works.  Also a transaction would be good here !
        
        return true;
    }

    
    
    
    public boolean IsValidUser(String username, String Password){
        String EncodedPassword=null;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instashutter");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
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
   
    
    return false;  
    }
    

   
   public LinkedList<UserSession> getUserInfo(String user) {
   	LinkedList<UserSession> userList = new LinkedList<UserSession>();
   	Session session = cluster.connect("instashutter");
   	System.out.println("sarah needs to speak");
   	PreparedStatement ps = session.prepare("SELECT * from userprofiles where login =?");
    ResultSet rs = null;
    BoundStatement boundStatement = new BoundStatement(ps);
    rs = session.execute( // this is where the query is executed
            boundStatement.bind( // here you are binding the 'boundStatement'
                    user));
    if (rs.isExhausted()) {
        System.out.println("No Images returned");
    } else {
        for (Row row : rs) {
        	UserSession ps1 = new UserSession();
   			ps1.setAll(row.getString("login"), row.getString("first_name"), row.getString("last_name"));
   			ps1.setBackground(row.getString("background"));
   			ps1.setLocation(row.getString("location"));
   			ps1.setBio(row.getString("bio"));
   			userList.add(ps1);
        }
    }
   	
   	session.close();
		return userList;
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
		ResultSet rs = null;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(fname, lname, location, username));
		if (rs.isExhausted()){
			System.out.println("something went wrong");
			//return null;
		} else {
			System.out.println("success");
			//return true;
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
		ResultSet rs = null;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(EncodedPassword, username));
		if (rs.isExhausted()){
			System.out.println("something went wrong");
			//return null;
		} else {
			System.out.println("success");
			//return true;
		}
		
		
	}
	
	public UserSession getProfilePic(String user)
    {
		ByteBuffer bImage = null;
		Session session = cluster.connect("instashutter");
        
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;               
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
        UserSession p = new UserSession();
        p.setPic(bImage);
                       
        return p;

    }
	
	
	
	public void setProfilePic(byte[] b, String filename, String type) {
		try {
			
			System.out.println("Starting upload of profile image");
			new Convertors();
            ByteBuffer.wrap(b);
        
            String types[]=Convertors.SplitFiletype(type);
            (new File("/var/tmp/instagram/")).mkdirs();
            @SuppressWarnings("resource")
			FileOutputStream output = new FileOutputStream(new File("/var/tmp/instashutter/" + filename));
            output.write(b);
            
            byte [] profilepic =profileImage(filename,types[1]);
            ByteBuffer picbuf=ByteBuffer.wrap(profilepic);

            Session session = cluster.connect("instashutter");
            
            PreparedStatement psInsertPic = session.prepare("update userprofiles set profileimage=? where login=?");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);

            session.execute(bsInsertPic.bind(picbuf,filename));

            session.close();
			
			
		}	catch (Exception e) {
			System.out.println("Error Setting profile picture: " + e);
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
	
	public static BufferedImage createImage(BufferedImage img)
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



}
