package me.stuartdouglas.stores;

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
}
