package me.stuartdouglas.stores;

import java.util.Date;

public class FollowingStore {
	private String username;
	private String following;
	private Date followedSince;
	
	public Date getFollowedSince() {
		return followedSince;
	}
	
	public void setFollowedSince(Date followedSince) {
		this.followedSince = followedSince;
	}
	
	public String getFollowing() {
		return following;
	}
	
	public void setFollowing(String following) {
		this.following = following;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
