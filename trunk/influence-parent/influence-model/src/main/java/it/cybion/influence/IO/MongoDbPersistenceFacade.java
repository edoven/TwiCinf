package it.cybion.influence.IO;

import it.cybion.influence.model.User;
import it.cybion.influence.util.OriginalJsonDeserializer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoDbPersistenceFacade {
	private DBCollection collection;
	
	public MongoDbPersistenceFacade(String host, String database, String collection) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient( "localhost" );
		DB db = mongoClient.getDB( database );
		this.collection = db.getCollection(collection);
	}
	
	
	public void putDoc(String json) {
		collection.save( (DBObject) JSON.parse(json) );
	}
	
	public void putDoc(DBObject json) {
		collection.save( json );
	}
	
	public void deleteDoc(String json) {
		collection.remove((DBObject) JSON.parse(json));
	}
	
	public void deleteDoc(DBObject json) {
		collection.remove(json);
	}
	
	public boolean containsUser(User user) {
		//List<String> jsons = new ArrayList<String>();
		OriginalJsonDeserializer jsonDeserializer = new OriginalJsonDeserializer();
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {	
			User currentUser = jsonDeserializer.deserializeJsonStringsToUser(cursor.next().toString());
			if (currentUser.getId() == user.getId() )
				return true;
		}
		return false;
	}
	
	
//	public void updateUser(User user) {
//		OriginalJsonDeserializer jsonDeserializer = new OriginalJsonDeserializer();
//		DBCursor cursor = collection.find();
//		while (cursor.hasNext()) {	
//			DBObject currentObject = cursor.next();
//			User currentUser = jsonDeserializer.deserializeJsonStringsToUser(currentObject.toString());
//			if (currentUser.getId() == user.getId() ) {
//				collection.remove(currentObject);
//				collection.save();
//				return;
//			}
//		}
//	}

	
	/*
	 * This could not be the best way of doing that.
	 * This class has to be as simple as possible, the responsability
	 * of adding attributes to a document should be addressed elsewhere.
	 * TODO: think about it.
	 */
//	public void putDocWithAttributes(String json, Map<String, String> attributes2value) {
//		DBObject object = (DBObject) JSON.parse(json);
//		object.putAll(attributes2value);
//		collection.save( object );
//	}
	
	
		
	public List<String> getAllDocs(){
		List<String> jsons = new ArrayList<String>();
		DBCursor cursor = collection.find();
		while (cursor.hasNext())
			jsons.add(cursor.next().toString());
		return jsons;
	}
}
