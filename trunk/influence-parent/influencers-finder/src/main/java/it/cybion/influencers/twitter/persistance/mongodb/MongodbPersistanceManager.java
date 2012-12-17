package it.cybion.influencers.twitter.persistance.mongodb;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongodbPersistanceManager implements PersistanceFacade {

	private DBCollection collection;

	public MongodbPersistanceManager(String host, String database, String collection) throws UnknownHostException {
		super();
		MongoClient mongoClient = new MongoClient( host );
		DB db = mongoClient.getDB( database );
		this.collection = db.getCollection(collection);
		/*
		 * Indexes
		 */
		//this.collection.createIndex(new BasicDBObject("userId", 1));
		//this.collection.ensureIndex("userId");
	}
	

	@Override
	public String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnriched {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext()) {	
			DBObject json = cursor.next();
			if (json.containsField("description"))
				return (String) json.get("description");
			else
				throw new UserNotProfileEnriched("User with id "+userId+" is not profile-eniched.");
		}
		else
			throw new UserNotPresentException("User with id "+userId+" is not in the collection.");
	}
	
	
	@Override
	public void removeUser(Long userId) {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext()) {	
			DBObject json = cursor.next();
			collection.remove(json);
		}
	}

	@Override
	public List<Long> getFollowers(Long userId) throws  UserNotPresentException, UserNotFollowersEnrichedException {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext()) {	
			DBObject json = cursor.next();
			if (json.containsField("followers")) {
				long[] followers = (long[]) json.get("followers");
				List<Long> followersList = new ArrayList<Long>();
				for (long id : followers)
					followersList.add(id);
				return followersList;
			}
			else
				throw new UserNotFollowersEnrichedException("User with id "+userId+" is not followers/friends-eniched.");
		}
		else
			throw new UserNotPresentException("User with id "+userId+" is not in the collection.");
	}

	@Override
	public List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext()) {	
			DBObject json = cursor.next();
			if (json.containsField("friends")) {
				long[] friends = (long[]) json.get("friends");
				List<Long> friendsList = new ArrayList<Long>();
				for (long id : friends)
					friendsList.add(id);
				return friendsList;
			}
			else
				throw new UserNotFriendsEnrichedException("User with id "+userId+" is not followers/friends-eniched.");
		}
		else
			throw new UserNotPresentException("User with id "+userId+" is not in the collection.");
	}

	@Override
	public void putUser(String userJson) {
		/*
		 * 
		 * TODO: what if the user is already in?!
		 * I can remove the old one searching it by id...
		 * but what if the "old" user is full enriched and 
		 * the new one is not?
		 * 
		 * 
		 */
		collection.insert((DBObject) JSON.parse(userJson));
	}

	@Override
	public void putFriends(Long userId, List<Long> friendsIds){
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("friends", friendsIds);
		putUser(user.toString());
		for (Long friendId : friendsIds) {
			DBObject friend = new DBObject();
			friend.put("id", friendId);
			putUser(friend.toString());
		}	
	}

	


	@Override
	public void putFollowers(Long userId, List<Long> followersIds) {
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("followers", followersIds);
		for (Long followerId : followersIds) {
			DBObject follower = new DBObject();
			follower.put("id", followerId);
			putUser(follower.toString());
		}	
	}

	public String getUser(Long userId) throws UserNotPresentException {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext())
			return cursor.next().toString();
		else
			throw new UserNotPresentException("User with id "+userId+" is not in the collection.");
	}
}
