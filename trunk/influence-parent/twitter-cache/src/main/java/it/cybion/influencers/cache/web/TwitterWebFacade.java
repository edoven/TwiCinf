package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.List;

import twitter4j.TwitterException;



public interface TwitterWebFacade
{

	String getUserJson(long userId) throws TwitterException;

	List<Long> getFollowersIds(long userId) throws TwitterException;

	List<Long> getFriendsIds(long userId) throws TwitterException;

	List<String> getUsersJsons(List<Long> usersIds) throws TwitterException;

//	List<String> getLast200Tweets(long userId) throws TwitterException;

	String getUserJson(String screenName) throws TwitterException;

	List<String> getTweetsWithMaxId(long userId, long maxId) throws TwitterException, ProtectedUserException;

	SearchedByDateTweetsResultContainer getTweetsByDate(long userId,
			int fromYear, int fromMonth, int fromDay, int toYear, int toMonth,
			int toDay) throws TwitterException, ProtectedUserException;

	// String getUserJson(String screenName) throws TwitterApiException;
	// List<Long> getFollowersIds(String screenName) throws TwitterApiException;
	// List<Long> getFriendsIds(String screenName) throws TwitterApiException;
}
