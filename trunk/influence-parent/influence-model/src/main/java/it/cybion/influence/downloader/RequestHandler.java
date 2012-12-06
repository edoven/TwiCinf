package it.cybion.influence.downloader;

import java.util.List;

import twitter4j.TwitterException;

public interface RequestHandler {

	public List<String> getUpTo5000FriendsIds(String userScreenName) throws TwitterException;
	
	public List<String> getUpTo5000FollowersIds(String userScreenName) throws TwitterException;
	
	public int getLimit() throws TwitterException ; 
	
}