package it.cybion.influencers.twitter.persistance;


import it.cybion.influencers.twitter.persistance.exceptions.TweetNotPresentException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.twitter.persistance.exceptions.UserWithNoTweetsException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.util.Map;
import java.util.HashMap;



public class MongodbPersistanceFacade implements PersistanceFacade
{

	private static final Logger logger = Logger.getLogger(MongodbPersistanceFacade.class);

	private DBCollection userCollection;
	private DBCollection tweetsCollection;
	
	public MongodbPersistanceFacade(String host, String database) throws UnknownHostException
	{
		MongoClient mongoClient = new MongoClient(host);
		DB db = mongoClient.getDB(database);
		this.userCollection = db.getCollection("users");
		//if the index already exists this does nothing
		this.userCollection.createIndex(new BasicDBObject("id", 1));
		this.tweetsCollection = db.getCollection("tweets");
		this.tweetsCollection.createIndex(new BasicDBObject("id", 1));
	}

	@Override
	public String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("description"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		String description = (String) user.get("description");
		if (description == null)
			description = "";
		return description;
	}

	@Override
	public String getDescriptionAndStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("description"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		String description = (String) user.get("description");
		DBObject statusObject = (DBObject) user.get("status");
		String status = "";
		if (statusObject != null)
			status = (String) statusObject.get("text");
		return description + " " + status;
	}

	@Override
	public List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException
	{
		DBObject userJson = getUserDbObject(userId);
		if (userJson.containsField("followers"))
		{
			List<Integer> intList = (List<Integer>) userJson.get("followers");
			List<Long> longList = new ArrayList<Long>();
			for (int intElement : intList)
				longList.add((long) intElement);
			return longList;
		} else
			throw new UserNotFollowersEnrichedException("User with id " + userId + " is not followers/friends-eniched.");
	}

	// public List<String> getTweets(long userId) {
	// logger.info("--begin--");
	// DBCursor cursor = tweetsCollection.find(new BasicDBObject("user.id",
	// userId));
	// List<String> tweets = new ArrayList<String>();
	// while (cursor.hasNext()) {
	// DBObject tweet = cursor.next();
	// tweets.add(tweet.toString());
	// logger.info(tweet.toString());
	// }
	// logger.info("--end--");
	// return tweets;
	// }

	@Override
	public int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("followers_count"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		int followersCount = (Integer) user.get("followers_count");
		return followersCount;
	}

	@Override
	public List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException
	{
		DBObject userJson = getUserDbObject(userId);
		if (userJson.containsField("friends"))
		{
			List<Integer> intList = (List<Integer>) userJson.get("friends");
			List<Long> longList = new ArrayList<Long>();
			for (int intElement : intList)
				longList.add((long) intElement);
			return longList;
		} else
			throw new UserNotFriendsEnrichedException("User with id " + userId + " is not friends-eniched.");
	}

	@Override
	public int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("friends_count"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		int followersCount = (Integer) user.get("friends_count");
		return followersCount;
	}

	@Override
	public String getScreenName(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("screen_name"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		String screenName = (String) user.get("screen_name");
		if (screenName == null)
			screenName = "";
		return screenName;
	}

	public String getStatus(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("description"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		DBObject status = (DBObject) user.get("status");
		String text = (String) status.get("text");
		if (text == null)
			text = "";
		return text;
	}

	private String getTweet(long tweetId) throws TweetNotPresentException
	{
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", tweetId);
		DBObject tweet = tweetsCollection.findOne(keys);
		if (tweet == null)
			throw new TweetNotPresentException("Tweet with id " + tweetId + " is not in the collection.");
		return tweet.toString();
	}
	
	@Override
	public List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException
	{
		DBCursor cursor = tweetsCollection.find(new BasicDBObject("user.id", userId));
		List<String> tweets = new ArrayList<String>();
		while (cursor.hasNext() && tweets.size() < 200)
			tweets.add(cursor.next().toString());
		if (tweets.size() == 0)
			throw new UserWithNoTweetsException();
		return tweets;
	}

	@Override
	public String getUser(Long userId) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("id", userId));
		if (user == null)
			throw new UserNotPresentException("User with id " + userId + " is not in the collection.");
		return user.toString();
	}

	@Override
	public String getUser(String screenName) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("screen_name", screenName));
		if (user == null)
			throw new UserNotPresentException("User with screenName " + screenName + " is not in the collection.");
		return user.toString();
	}

	private DBObject getUserDbObject(Long userId) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("id", userId));
		if (user == null)
			throw new UserNotPresentException("User with id " + userId + " is not in the collection.");
		return user;
	}

	@Override
	public Long getUserId(String screenName) throws UserNotPresentException
	{
		String userJson = getUser(screenName);
		DBObject user = (DBObject) JSON.parse(userJson);
		return new Long((Integer)user.get("id"));
	}

	@Override
	public void putFollowers(Long userId, List<Long> followersIds) throws UserNotPresentException
	{
		logger.info("writing " + followersIds.size() + " followers for user with id=" + userId);
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("followers", followersIds);
		putUser(user.toString());
		for (Long followerId : followersIds)
		{
			DBObject follower = new BasicDBObject();
			follower.put("id", followerId);
			putUser(follower.toString());
		}
	}

	@Override
	public void putFriends(Long userId, List<Long> friendsIds) throws UserNotPresentException
	{
		logger.info("writing " + friendsIds.size() + " friends for user with id=" + userId);
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("friends", friendsIds);
		putUser(user.toString());
		for (Long friendId : friendsIds)
		{
			DBObject friend = new BasicDBObject();
			friend.put("id", friendId);
			putUser(friend.toString());
		}
	}

	public void putTweet(String tweetToInsertJson)
	{
		DBObject tweetToInsert = (DBObject) JSON.parse(tweetToInsertJson);
		long tweetId = -1;
		try
		{
			Object id = tweetToInsert.get("id");		
			if (id instanceof Long)
				tweetId = (Long) id;
			else
				if (id instanceof Integer)
					tweetId = new Long((Integer) id);
				else
				{
					logger.info("problem with twitter id "+id);
					System.exit(0);
				}
		} catch (ClassCastException e)
		{
			logger.info("ERROR: problem extracting id from " + tweetToInsertJson);
			return;
		}
		try
		{
			getTweet(tweetId);
		} catch (TweetNotPresentException e)
		{
			tweetsCollection.insert(tweetToInsert);
		}
	}

	@Override
	public void putTweets(List<String> tweets)
	{
		for (String tweet : tweets)
			putTweet(tweet);
	}

	/*
	 * If a user with the same id (beware: id!=_id) is already present, the new
	 * fields (if exist) are added.
	 */
	@Override
	public void putUser(String userToInsertJson)
	{
		DBObject userToInsert = (DBObject) JSON.parse(userToInsertJson);
		long userId = new Long((Integer) userToInsert.get("id"));
		String userInDbJson;
		try
		{
			userInDbJson = getUser(userId);
		} catch (UserNotPresentException e)
		{
			userCollection.insert(userToInsert);
			return;
		}
		DBObject userInDb = (DBObject) JSON.parse(userInDbJson);
		Map<String, Object> field2value = new HashMap<String, Object>();
		for (String field : userInDb.keySet())
			field2value.put(field, userInDb.get(field));
		for (String field : userToInsert.keySet())
			field2value.put(field, userToInsert.get(field));
		DBObject updatedUser = new BasicDBObject();
		for (String field : field2value.keySet())
			updatedUser.put(field, field2value.get(field));
		DBObject query = new BasicDBObject();
		query.put("id", userId);
		userCollection.update(query, updatedUser);
	}

	@Override
	public void removeUser(Long userId)
	{
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = userCollection.find(keys);
		if (cursor.hasNext())
		{
			DBObject json = cursor.next();
			userCollection.remove(json);
		}
	}

}
