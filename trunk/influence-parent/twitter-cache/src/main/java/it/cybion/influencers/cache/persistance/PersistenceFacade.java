package it.cybion.influencers.cache.persistance;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import it.cybion.influencers.cache.persistance.exceptions.*;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;



public class PersistenceFacade
{
	private static PersistenceFacade singletonInstance = null;
	
	private UsersPersistenceFacade usersMongodbPersistanceFacade;
	private TweetsPersistenceFacade tweetsMongodbPersistenceFacade;
	
	public static PersistenceFacade getInstance(String host, String database) throws UnknownHostException
	{
		if (singletonInstance == null)
			singletonInstance = new PersistenceFacade(host, database);
		return singletonInstance;
	}
	
	private PersistenceFacade(String host, String database) throws UnknownHostException
	{
		MongoClient mongoClient = new MongoClient(host);
		DB db = mongoClient.getDB(database);
		DBCollection userCollection = db.getCollection("users");
		userCollection.createIndex(new BasicDBObject("id", 1));		
		DBCollection tweetsCollection = db.getCollection("tweets");
		tweetsCollection.createIndex(new BasicDBObject("id", 1));
		tweetsCollection.createIndex(new BasicDBObject("user.id", 1));
		
		usersMongodbPersistanceFacade = new UsersPersistenceFacade(userCollection);
		tweetsMongodbPersistenceFacade = new TweetsPersistenceFacade(tweetsCollection);
	}


	public String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getDescription(userId);
	}


	public String getDescriptionAndStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getDescriptionAndStatus(userId);
	}


	public List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowers(userId);
	}



	public int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowersCount(userId);
	}


	public List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getFriends(userId);
	}


	public int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getFollowersCount(userId);
	}


	public String getScreenName(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getScreenName(userId);
	}

	public String getStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		return usersMongodbPersistanceFacade.getStatus(userId);
	}

	
	public List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException
	{
		return tweetsMongodbPersistenceFacade.getUpTo200Tweets(userId);
	}


	public String getUser(Long userId) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUser(userId);
	}


	public String getUser(String screenName) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUser(screenName);
	}
	

	public Long getUserId(String screenName) throws UserNotPresentException
	{
		return usersMongodbPersistanceFacade.getUserId(screenName);
	}


	public void putFollowers(Long userId, List<Long> followersIds) throws UserNotPresentException
	{
		usersMongodbPersistanceFacade.putFollowers(userId, followersIds);
	}


	public void putFriends(Long userId, List<Long> friendsIds) throws UserNotPresentException
	{
		usersMongodbPersistanceFacade.putFriends(userId, friendsIds);
	}

	public void putTweet(String tweetToInsertJson)
	{
		tweetsMongodbPersistenceFacade.putTweet(tweetToInsertJson);
	}


	public void putTweets(List<String> tweets)
	{
		tweetsMongodbPersistenceFacade.putTweets(tweets);
	}

	/*
	 * If a user with the same id (beware: id!=_id) is already present, the new
	 * fields (if exist) are added.
	 */
	public void putUser(String userToInsertJson)
	{
		usersMongodbPersistanceFacade.putOrUpdateUser(userToInsertJson);
	}


	public void removeUser(Long userId)
	{
		usersMongodbPersistanceFacade.removeUser(userId);
	}
	

	public void removeTweet(Long tweetId)
	{
		tweetsMongodbPersistenceFacade.removeTweet(tweetId);
	}


	public List<String> getTweetsByDate(long userId,Date fromDate, Date toDate) throws UserWithNoTweetsException, DataRangeNotCoveredException
	{
		return tweetsMongodbPersistenceFacade.getTweetsByDate(userId, fromDate, toDate );
	}
}
