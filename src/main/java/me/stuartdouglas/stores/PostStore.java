package me.stuartdouglas.stores;

import java.nio.ByteBuffer;
import java.util.Date;

import com.datastax.driver.core.utils.Bytes;

public class PostStore {
	private ByteBuffer bImage = null;
    private int length;
    private String type;
    private java.util.UUID UUID=null;
    private String user;
    private String title = "";
    java.util.Date timeAdded = new java.util.Date();
    
    public PostStore() {
    	
    }

    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    
    public String getPostedUsername(){
    	return user;
    }
    public void setPostedUsername(String User) {
    	this.user=User;
    }
    
    public String getSUUID(){
        return UUID.toString();
    }
    public void setPic(ByteBuffer bImage, int length,String type) {
        this.bImage = bImage;
        this.length = length;
        this.type=type;
    }

    public ByteBuffer getBuffer() {
        return bImage;
    }

    public int getLength() {
        return length;
    }
    
    public String getType(){
        return type;
    }

    public byte[] getBytes() {
         
        byte image[] = Bytes.getArray(bImage);
        return image;
    }
    
    public void setTitle(String title) {
    	if (title == null)	{
    		this.title = "opps";
    	} else {
    	this.title = title;
    	}
	}
    
    public String getTitle() {
    	return title;
    }
    
    
    public Date getTimeAdded() {
    	return timeAdded;
    }
}
