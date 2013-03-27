package it.cybion.influencers.cache.persistance.implementations.mongodb;

import it.cybion.influencers.cache.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotProfileEnrichedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongodbUsersPersistanceFacade
{
	private final Logger logger = Logger.getLogger(MongodbUsersPersistanceFacade.class);
	
	
	private DBCollection userCollection;
	
	public MongodbUsersPersistanceFacade(DBCollection userCollection)
	{
		this.userCollection = userCollection;
	}
	
	
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

	public List<Long> getFollowers(Long userId) throws UserNotPresentException, UserNotFollowersEnrichedException
	{
		DBObject userJson = getUserDbObject(userId);
		if (userJson.containsField("followers"))
		{
			@SuppressWarnings("unchecked")
			List<Integer> intList = (List<Integer>) userJson.get("followers");
			List<Long> longList = new ArrayList<Long>();
			for (int intElement : intList)
				longList.add((long) intElement);
			return longList;
		} else
			throw new UserNotFollowersEnrichedException("User with id " + userId + " is not followers/friends-eniched.");
	}

	public int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("followers_count"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		int followersCount = (Integer) user.get("followers_count");
		return followersCount;
	}


	public List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException
	{
		DBObject userJson = getUserDbObject(userId);
		if (userJson.containsField("friends"))
		{
			@SuppressWarnings("unchecked")
			List<Integer> intList = (List<Integer>) userJson.get("friends");
			List<Long> longList = new ArrayList<Long>();
			for (int intElement : intList)
				longList.add((long) intElement);
			return longList;
		} else
			throw new UserNotFriendsEnrichedException("User with id " + userId + " is not friends-eniched.");
	}

	
	public int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (!user.containsField("friends_count"))
			throw new UserNotProfileEnrichedException("User with id " + userId + " is not profile-eniched.");
		int followersCount = (Integer) user.get("friends_count");
		return followersCount;
	}

	
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
	
	
	public String getUser(Long userId) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("id", userId));
		if (user == null)
			throw new UserNotPresentException("User with id " + userId + " is not in the collection.");
		return user.toString();
	}
	
	public Long getUserId(String screenName) throws UserNotPresentException
	{
		String userJson = getUser(screenName);
		DBObject user = (DBObject) JSON.parse(userJson);
		return new Long((Integer)user.get("id"));
	}
	
	public String getUser(String screenName) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("screen_name", screenName));
		if (user == null)
			throw new UserNotPresentException("User with screenName " + screenName + " is not in the collection.");
		return user.toString();
	}
	
	
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
	

	private DBObject getUserDbObject(Long userId) throws UserNotPresentException
	{
		DBObject user = userCollection.findOne(new BasicDBObject("id", userId));
		if (user == null)
			throw new UserNotPresentException("User with id " + userId + " is not in the collection.");
		return user;
	}
	
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
