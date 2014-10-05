package me.stuartdouglas.stores;

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
    
    public void setUserSession() {
    	loggedin = true;
    }
    
    public boolean getUserSession() {
    	return loggedin;
    }
}
