package it.cybion.influence.downloader;

import java.util.List;

import twitter4j.TwitterException;

public interface RequestHandler {

	public List<String> getFriendsIds(String userScreenName) throws TwitterException;
	
	public List<String> getFollowersIds(String userScreenName) throws TwitterException;
	
	public int getLimit() throws TwitterException ; 
	
}