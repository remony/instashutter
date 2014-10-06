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
    
    public UserSession() {
    	
    }
    
    public void setUsername(String name) {
    	this.Username = name;
    }
    
    public String getUsername() {
    	return Username;
    }
    
    public void setUserSessionOnline() {
    	loggedin = true;
    }
    
    public void setUserSessionOffline() {
    	loggedin = false;
    }
    
    public boolean getUserSession() {
    	return loggedin;
    }
}
