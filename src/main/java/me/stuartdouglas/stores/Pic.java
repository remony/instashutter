package me.stuartdouglas.stores;

import com.datastax.driver.core.utils.Bytes;

import java.nio.ByteBuffer;
import java.util.Date;

//import me.stuartdouglas.models.User;

/**
 *
 * @author Administrator
 */
public class Pic {

    private ByteBuffer bImage = null;
    private int length;
    private String type;
    private java.util.UUID UUID=null;
    private String user;
    private String caption = "";
    java.util.Date timeAdded = new java.util.Date();
    private Date picAdded = new Date();
    
    
    public Pic() {

    }
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    
    public String getPostedUsername(){
    	return user.toString();
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
    
    public void setCaption(String caption) {
    	if (caption == null)	{
    		this.caption = "opps";
    	} else {
    	this.caption = caption;
    	}
	}
    
    public String getCaption() {
    	return caption;
    }
    
    
    
    public void setPicAdded(Date time) {
    	this.picAdded = time;
    }
    
    public Date getPicAdded() {
    	return picAdded;
    }

}

