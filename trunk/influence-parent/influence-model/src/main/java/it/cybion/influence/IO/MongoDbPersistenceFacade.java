package it.cybion.influence.IO;

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
	
	public MongoDbPersistenceFacade(String host, String collection) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient( "localhost" );
		DB db = mongoClient.getDB( "users" );
		this.collection = db.getCollection(collection);
	}
	
	
	public void putDoc(String json) {
		collection.save( (DBObject) JSON.parse(json) );
	}
	
	
	public List<String> getAllUsers(){
		List<String> jsons = new ArrayList<String>();
		DBCursor cursor = collection.find();
		while (cursor.hasNext())
			jsons.add(cursor.next().toString());
		return jsons;
	}
}
