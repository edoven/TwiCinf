package it.cybion.influence.downloader;

import it.cybion.influence.model.User;

import java.util.List;

import twitter4j.IDs;
import twitter4j.TwitterException;

public interface RequestHandler {

	public List<String> getUpTo5000FriendsIds(String userScreenName) throws TwitterException;
	
	public List<String> getUpTo5000FollowersIds(String userScreenName) throws TwitterException;
	
	public int getLimit() throws TwitterException ;

	IDs getFriendsWithPagination(String userScreenName, long cursor) throws TwitterException;

	IDs getFollowersWithPagination(String userScreenName, long cursor) throws TwitterException;

	public String getRawJsonUser(String userScreenName) throws TwitterException; 

	public String getRawJsonUser(long userId) throws TwitterException;
	
	public User getUser(String screenName) throws TwitterException;

	public User getUser(long userId) throws TwitterException;

	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException;
	
	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException;

	public String getUserRawJson(long userId) throws TwitterException;
	
}