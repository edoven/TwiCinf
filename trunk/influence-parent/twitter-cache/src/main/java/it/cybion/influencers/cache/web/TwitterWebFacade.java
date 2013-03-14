package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;

import java.util.List;

import twitter4j.TwitterException;



public interface TwitterWebFacade
{

	String getUserJson(long userId) throws TwitterException;

	List<Long> getFollowersIds(long userId) throws TwitterException;

	List<Long> getFriendsIds(long userId) throws TwitterException;

	List<String> getUsersJsons(List<Long> usersIds);

	List<String> getLast200Tweets(long userId) throws TwitterException, UserWithNoTweetsException;

	String getUserJson(String screenName) throws TwitterException;

	// String getUserJson(String screenName) throws TwitterApiException;
	// List<Long> getFollowersIds(String screenName) throws TwitterApiException;
	// List<Long> getFriendsIds(String screenName) throws TwitterApiException;
}
