package it.cybion.influence.model;

import java.util.List;

public class User {
	
	private long id;
	private String screenName;
	private List<User> followers;
	private List<User> friends;
	
	public User(long id) {
		this.id = id;
		this.screenName = "UNKNOWN";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
}
