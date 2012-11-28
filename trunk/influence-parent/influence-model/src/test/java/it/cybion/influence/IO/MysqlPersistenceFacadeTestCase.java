package it.cybion.influence.IO;


import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.util.JsonDeserializer;

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
	
	//private static final Logger logger = Logger.getLogger(MysqlPersistenceFacadeTestCase.class);
	
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
    	assertEquals(persistenceFacade.getFollowers("03Stefania55").size() ,173);
    }
    

}
