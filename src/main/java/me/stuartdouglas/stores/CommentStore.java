package me.stuartdouglas.stores;

import java.util.Date;
import java.util.UUID;

public class CommentStore {
	private String username;
	private Date posted_time;
	private String commentMessage;
	private UUID uuid = null;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getPosted_time() {
		return posted_time;
	}
	public void setPosted_time(Date posted_time) {
		this.posted_time = posted_time;
	}
	public String getCommentMessage() {
		return commentMessage;
	}
	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}

