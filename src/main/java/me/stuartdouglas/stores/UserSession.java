package me.stuartdouglas.stores;

import java.nio.ByteBuffer;

import com.datastax.driver.core.utils.Bytes;

/*
 * 	Author: Stuart Douglas
 *  
 * 	Description: Class get/setter or user session data 
 * 
 */


public final class UserSession {
	boolean loggedin = false;
    String Username = null;
    String fname = "null";
    String lname = "null";
    String userBackground = null;
    String location = null;
    String bio = null;
    private ByteBuffer profileImage = null;
    
    public UserSession() {
    	
    }
    
    public void setAll(String username, String fname, String lname){
    	this.Username = username;
    	this.fname = fname;
    	this.lname = lname;
    }
    
    public void setUsername(String name) {
    	this.Username = name;
    }
    
    public String getUsername() {
    	return Username;
    }
    
    public void setUserSession(Boolean flag) {
    	loggedin = flag;
    }

    
    public boolean getUserSession() {
    	return loggedin;
    }
    
    public void setfname(String name) {
    	this.fname = name;
    }
    
    public String getfname() {
    	return fname;
    }
    
    public void setlname(String name) {
    	this.lname = name;
    }
    
    public String getlname() {
    	
    	return lname;
    }
    
    public void setPic(ByteBuffer bImage) {
        this.profileImage = bImage;
    }
    
    public ByteBuffer getProfilePic() {
    	return profileImage;
    }
    
    public byte[] getBytes() {
        
        byte image[] = Bytes.getArray(profileImage);
        return image;
    }
    
    public void setBackground(String url) {
    	this.userBackground = url;
    }
    
    public String getBackground() {
    	return userBackground;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
    
    public String getLocation()	{
    	return location;
    }
    
    public void setBio(String bio) {
    	this.bio = bio;
    }
    
    public String getBio()	{
    	return bio;
    }
    

}
