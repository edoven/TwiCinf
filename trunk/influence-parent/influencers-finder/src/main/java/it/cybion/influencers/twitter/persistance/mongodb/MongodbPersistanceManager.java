package it.cybion.influencers.twitter.persistance.mongodb;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

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
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		if (user.containsField("description"))
			return (String) user.get("description");
		else
			throw new UserNotProfileEnriched("User with id "+userId+" is not profile-eniched.");
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
	//@SuppressWarnings("unchecked")
	public List<Long> getFollowers(Long userId) throws  UserNotPresentException, UserNotFollowersEnrichedException {
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", userId);
		DBCursor cursor = collection.find(keys);
		if (cursor.hasNext()) {	
			DBObject json = cursor.next();
			if (json.containsField("followers")) {				
				return (List<Long>) json.get("followers");
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
				return (List<Long>) json.get("friends");
			}
			else
				throw new UserNotFriendsEnrichedException("User with id "+userId+" is not followers/friends-eniched.");
		}
		else
			throw new UserNotPresentException("User with id "+userId+" is not in the collection.");
	}

	
	/*
	 * 
	 * If a user with the same id is already present, the new fields (if existing)
	 * are added.
	 */
	@Override
	public void putUser(String userJson) {
		DBObject userToInsert = (DBObject) JSON.parse(userJson);
		DBObject userInDb;
		Long userId = new Long((Integer) userToInsert.get("id"));
		try {
			String userInDbString = getUser(userId);
			userInDb = (DBObject) JSON.parse(userInDbString);
		} catch (UserNotPresentException e) {
			collection.insert(userToInsert);
			return;
		}

		Set<String> userToInsertFields = userInDb.keySet();		
		for (String field : userToInsertFields)
			if (! userInDb.containsField(field))
				userToInsert.put(field, userInDb.get(field));
		collection.remove(userInDb);
		collection.insert(userToInsert);
	}

	@Override
	public void putFriends(Long userId, List<Long> friendsIds) throws UserNotPresentException{
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("friends", friendsIds);
		putUser(user.toString());
		for (Long friendId : friendsIds) {
			DBObject friend = new BasicDBObject();
			friend.put("id", friendId);
			putUser(friend.toString());
		}	
	}

	@Override
	public void putFollowers(Long userId, List<Long> followersIds) throws UserNotPresentException {
		String userJson = getUser(userId);
		DBObject user = (DBObject) JSON.parse(userJson);
		user.put("followers", followersIds);
		putUser(user.toString());
		for (Long followerId : followersIds) {
			DBObject follower = new BasicDBObject();
			follower.put("id", followerId);
			putUser(follower.toString());
		}	
	}

	@Override
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
