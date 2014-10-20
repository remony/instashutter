package me.stuartdouglas.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;



import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

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

	public void updateUserDetails(String username,
			String fname, String lname) {
		// TODO Auto-generated method stub
		System.out.println("yay");
		Session session = cluster.connect("instashutter");
		PreparedStatement ps = session.prepare("UPDATE userprofiles set first_name = ?, last_name = ? where login = ?");
		ResultSet rs = null;
		BoundStatement boundStatement = new BoundStatement(ps);
		rs = session.execute(boundStatement.bind(fname, lname, username));
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

}
