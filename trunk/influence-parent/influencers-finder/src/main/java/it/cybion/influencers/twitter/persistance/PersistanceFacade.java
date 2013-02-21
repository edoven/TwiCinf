package it.cybion.influencers.twitter.persistance;


import it.cybion.influencers.twitter.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserWithNoTweetsException;

import java.util.List;



public interface PersistanceFacade
{

	String getUser(Long userId) throws UserNotPresentException;

	String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;

	String getScreenName(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;

	List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException;

	List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException;

	void putUser(String userJson);

	void putFriends(Long userId, List<Long> friendsJsons) throws UserNotPresentException;

	void putFollowers(Long userId, List<Long> followersJsons) throws UserNotPresentException;

	void removeUser(Long userId);

	int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;

	int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;

	String getDescriptionAndStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;

	List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException;

	void putTweets(List<String> tweets);

	Long getUserId(String screenName) throws UserNotPresentException;

	String getUser(String screenName) throws UserNotPresentException;

}
