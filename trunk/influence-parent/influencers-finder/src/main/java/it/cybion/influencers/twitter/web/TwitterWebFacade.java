package it.cybion.influencers.twitter.web;

import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

import java.util.List;

public interface TwitterWebFacade {

	String getUserJson(long userId) throws TwitterApiException;
	List<Long> getFollowersIds(long userId) throws TwitterApiException;
	public List<Long> getFriendsIds(long userId) throws TwitterApiException;

}
