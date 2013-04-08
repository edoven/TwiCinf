package it.cybion.influencers.cache.persistance.implementations.mongodb;


import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.persistance.exceptions.DataRangeNotCoveredException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;



public class MongodbPersistanceFacade implements PersistanceFacade
{
	private static final Logger logger = Logger.getLogger(MongodbPersistanceFacade.class);
	
	
	private MongodbUsersPersistanceFacade usersMongodbPersistanceFacade;
	private MongodbTweetsPersistanceFacade tweetsMongodbPersistanceFacade;
	
	
	public MongodbPersistanceFacade(String host, String database) throws UnknownHostException
	{
		MongoClient mongoClient = new MongoClient(host);
		DB db = mongoClient.getDB(database);
		DBCollection userCollection = db.getCollection("users");
		userCollection.createIndex(new BasicDBObject("id", 1));		
		DBCollection tweetsCollection = db.getCollection("tweets");
		tweetsCollection.createIndex(new BasicDBObject("id", 1));
		tweetsCollection.createIndex(new BasicDBObject("user.id", 1));
		
		usersMongodbPersistanceFacade = new MongodbUsersPersistanceFacade(userCollection);
		tweetsMongodbPersistanceFacade = new MongodbTweetsPersistanceFacade(tweetsCollection);
	}

	@Override
	public String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getDescription(userId);
	}

	@Override
	public String getDescriptionAndStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getDescriptionAndStatus(userId);
	}

	@Override
	public List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowers(userId);
	}


	@Override
	public int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowersCount(userId);
	}

	@Override
	public List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getFriends(userId);
	}

	@Override
	public int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowersCount(userId);
	}

	@Override
	public String getScreenName(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getScreenName(userId);
	}

	public String getStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getStatus(userId);
	}

	
	@Override
	public List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException
	{
		return tweetsMongodbPersistanceFacade.getUpTo200Tweets(userId);
	}

	@Override
	public String getUser(Long userId) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUser(userId);
	}

	@Override
	public String getUser(String screenName) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUser(screenName);
	}
	
	@Override
	public Long getUserId(String screenName) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUserId(screenName);
	}

	@Override
	public void putFollowers(Long userId, List<Long> followersIds) throws UserNotPresentException
	{
		usersMongodbPersistanceFacade.putFollowers(userId, followersIds);
	}

	@Override
	public void putFriends(Long userId, List<Long> friendsIds) throws UserNotPresentException
	{
		usersMongodbPersistanceFacade.putFriends(userId, friendsIds);
	}

	public void putTweet(String tweetToInsertJson)
	{
		tweetsMongodbPersistanceFacade.putTweet(tweetToInsertJson);
	}

	@Override
	public void putTweets(List<String> tweets)
	{
		tweetsMongodbPersistanceFacade.putTweets(tweets);
	}

	/*
	 * If a user with the same id (beware: id!=_id) is already present, the new
	 * fields (if exist) are added.
	 */
	@Override
	public void putUser(String userToInsertJson)
	{
		usersMongodbPersistanceFacade.putUser(userToInsertJson);
	}

	@Override
	public void removeUser(Long userId)
	{
		usersMongodbPersistanceFacade.removeUser(userId);
	}
	
	@Override
	public void removeTweet(Long tweetId)
	{
		tweetsMongodbPersistanceFacade.removeTweet(tweetId);
	}

	@Override
	public List<String> getTweetsByDate(long userId,Date fromDate, Date toDate) throws UserWithNoTweetsException, DataRangeNotCoveredException
	{
		return tweetsMongodbPersistanceFacade.getTweetsByDate(userId, fromDate, toDate );
	}
}
