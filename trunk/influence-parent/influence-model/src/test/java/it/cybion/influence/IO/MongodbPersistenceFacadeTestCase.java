package it.cybion.influence.IO;


import static org.testng.AssertJUnit.assertEquals;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JodaDateTimeTypeDeserializer;

import java.util.List;
import java.net.UnknownHostException;

import org.testng.annotations.Test;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MongodbPersistenceFacadeTestCase {

	class TestObject {
		private int id;
		private String description;
		
		public TestObject(int id, String description) {
			super();
			this.setId(id);
			this.setDescription(description);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}		
	}
	
	@Test
	public void deleteTest() throws UnknownHostException{
		TestObject obj1 = new TestObject(1, "Test Object with id=1");
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost",
																					"experiments",
																					"deleteTest");	
		List<String> docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
		
		Gson gson = new Gson();
		String json = gson.toJson(obj1, TestObject.class);
		persistenceFacade.putDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 1);
		
		persistenceFacade.deleteDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
	}
	
	
	@Test (enabled=false)
	public void updateTest() throws UnknownHostException{
		TestObject obj1 = new TestObject(1, "Test Object with id=1");
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost",
																					"experiments",
																					"updateTest");	
//		List<String> docs = persistenceFacade.getAllDocs();
//		for (String doc : docs)
//			persistenceFacade.deleteDoc(doc);
		
		
		List<String> docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
		//insert the obj
		Gson gson = new Gson();
		String json = gson.toJson(obj1, TestObject.class);
		persistenceFacade.putDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 1);
//			
//		//modify the obj and re-insert it.
//		//this should only do an update of the object already present
//		obj1.setDescription("Test Object with id=1 - MODIFIED");
//		json = gson.toJson(obj1, TestObject.class);
//		persistenceFacade.putDoc(json);
//		docs = persistenceFacade.getAllDocs();
//		assertEquals(docs.size(), 1);
			
		//remove the object
		persistenceFacade.deleteDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
		
	}
	
	
	@Test
	public void containsUserTEST() throws UnknownHostException{
		User user = new User(1111);
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost",
																					"experiments",
																					"containsUserTEST");	

		
//		List<String> docs = persistenceFacade.getAllDocs();
//		for (String doc : docs)
//			persistenceFacade.deleteDoc(doc);

		
		
		List<String> docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
		//insert the obj
		Gson gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
			.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
			.create(); 
		String json = gson.toJson(user, User.class);
		persistenceFacade.putDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 1);

		assertEquals(persistenceFacade.containsUser(user), true);
			
		//remove the object
		persistenceFacade.deleteDoc(json);
		docs = persistenceFacade.getAllDocs();
		assertEquals(docs.size(), 0);
		
	}
	
}


