package me.stuartdouglas.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import me.stuartdouglas.lib.AeSimpleSHA1;
import me.stuartdouglas.stores.UserSession;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
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
    
   public String getFName(String username) {
	   String fname = null;
	   Session session = cluster.connect("instashutter");
	   PreparedStatement ps = session.prepare("select fname from userprofiles where login =?");
	   ResultSet rs = null;
	   BoundStatement boundStatement = new BoundStatement(ps);
	   rs = session.execute(boundStatement.bind(username));
	   if (rs.isExhausted()) {
		   System.out.println("Requested user doesn't exist.");
		   return null;
	   } else {
		   for (Row row : rs) {
			   fname = row.getString("first_name");
			   return fname;
		   }
	   }
	return null;
	   
   }
    
    
   public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
