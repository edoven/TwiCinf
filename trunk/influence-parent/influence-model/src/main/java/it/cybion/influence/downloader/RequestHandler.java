package it.cybion.influence.downloader;

import java.util.List;

import twitter4j.IDs;
import twitter4j.TwitterException;

public interface RequestHandler {

	public List<String> getUpTo5000FriendsIds(String userScreenName) throws TwitterException;
	
	public List<String> getUpTo5000FollowersIds(String userScreenName) throws TwitterException;
	
	public int getLimit() throws TwitterException ;

	IDs getFriendsWithPagination(String userScreenName, long cursor) throws TwitterException;

	IDs getFollowersWithPagination(String userScreenName, long cursor) throws TwitterException; 
	
}