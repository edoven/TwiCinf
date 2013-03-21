package it.cybion.influencers.cache.persistance;



import it.cybion.influencers.cache.persistance.exceptions.OldestTweetsNeedToBeDownloadedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;

import java.util.List;



public interface PersistanceFacade
{
	String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;
	String getDescriptionAndStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;
	List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException;
	int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;
	List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException;
	int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;
	String getScreenName(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException;
	List<String> getTweetsByDate(long userId, 
			int fromYear, int fromMonth , int fromDay,
			int toYear, int toMonth, int toDay) throws UserWithNoTweetsException, OldestTweetsNeedToBeDownloadedException;

	List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException;
	String getUser(Long userId) throws UserNotPresentException;
	String getUser(String screenName) throws UserNotPresentException;
	Long getUserId(String screenName) throws UserNotPresentException;
	void putFollowers(Long userId, List<Long> followersJsons) throws UserNotPresentException;
	void putFriends(Long userId, List<Long> friendsJsons) throws UserNotPresentException;
	void putTweets(List<String> tweets);
	void putUser(String userJson);
	void removeTweet(Long tweetId);
	void removeUser(Long userId);
}
