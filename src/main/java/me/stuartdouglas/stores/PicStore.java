package me.stuartdouglas.stores;

import com.datastax.driver.core.utils.Bytes;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

//import me.stuartdouglas.models.User;

/**
 *
 * @author Administrator
 */
public class PicStore {

    private ByteBuffer bImage = null;
    private int length;
    private String type;
    private java.util.UUID UUID=null;
    private String user;
    private String caption = "";
    java.util.Date timeAdded = new java.util.Date();
    private Date picAdded = new Date();
    private String postComment;
    private final LinkedList<CommentStore> commentlist = new LinkedList<>();
    private boolean isPublic = false;
    
    
    public PicStore() {

    }
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    
    public UUID getUUID ()	{
    	return UUID;
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
	public LinkedList<CommentStore> getCommentlist() {
		return commentlist;
	}

	public String getPostComment() {
		return postComment;
	}
	public void setPostComment(String postComment) {
		this.postComment = postComment;
	}
	public boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public void setCommentlist(UUID uuid, String username, Date comment_added, String comment_text) {
		CommentStore commentS = new CommentStore();
		commentS.setUuid(uuid);
		commentS.setUsername(username);
		commentS.setPosted_time(comment_added);
		commentS.setCommentMessage(comment_text);
		commentlist.add(commentS);
	}

}
