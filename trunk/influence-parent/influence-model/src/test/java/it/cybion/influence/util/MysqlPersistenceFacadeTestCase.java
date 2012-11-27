package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/*
 * 
 * This test is about the dataset of trenitalia/etc 
 * in my own MySQL server
 * 
 */

public class MysqlPersistenceFacadeTestCase {
	
	private static final Logger logger = Logger.getLogger(MysqlPersistenceFacadeTestCase.class);
	
	private MysqlPersistenceFacade persistenceFacade;
	
	@BeforeClass
	public void setup() throws IOException {
		persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
	}
	
    @AfterClass
	public void shutdown() {
    	persistenceFacade = null;
	}
    
    
    @Test
    public void testIfTheExctractedDatasetIsCorrect() {
    	List<String> tweets = persistenceFacade.getAllJsonTweets();    	
    	
    	
    	assertEquals(tweets.size(), 6214);
    	assertEquals(new JsonDeserializer().deserializeJsonStringToTweet(tweets.get(0)).getId(),
    			"263328879631036416");
    	assertEquals(new JsonDeserializer().deserializeJsonStringToTweet(tweets.get(999)).getId(),
    			"263667501894864896");

    }
    
    
    @Test
    public void getFriendsAndFollowersTEST() {
    	assertEquals(persistenceFacade.getFriends("citiamo").size() ,204);
    	assertEquals(persistenceFacade.getFollowers("citiamo").size() ,407);
    	assertEquals(persistenceFacade.getFriends("03Stefania55").size() ,1075);
    	//assertEquals(persistenceFacade.getFollowers("03Stefania55").size() ,173);
    }
    
    
    
    
    /*
    @Test
    public void writeFriendsTEST() {
    	List<String> friends = new ArrayList<String>();
		friends.add("friend1");
		friends.add("friend2");
		friends.add("friend3");
		friends.add("friend4");
		writeFriends("user", friends);    
    }
    */

}
