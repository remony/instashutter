package me.stuartdouglas.stores;

import com.datastax.driver.core.utils.Bytes;

import java.nio.ByteBuffer;

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
    private String title = "";
    
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

}

