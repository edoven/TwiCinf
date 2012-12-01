package it.cybion.influence.IO;


import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.util.JsonDeserializer;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
    	logger.info("testIfTheExctractedDatasetIsCorrect START");
    	List<String> tweets = persistenceFacade.getAllJsonTweets();    	
    	  	
    	assertEquals(tweets.size(), 6214);
    	assertEquals(new JsonDeserializer().deserializeJsonStringToTweet(tweets.get(0)).getId(),
    			"263328879631036416");
    	assertEquals(new JsonDeserializer().deserializeJsonStringToTweet(tweets.get(999)).getId(),
    			"263667501894864896");
    	logger.info("testIfTheExctractedDatasetIsCorrect END");
    }
    
    
    @Test
    public void getFriendsAndFollowersTEST() {
    	logger.info("getFriendsAndFollowersTEST START");
    	assertEquals(persistenceFacade.getFriends("citiamo").size() ,204);
    	assertEquals(persistenceFacade.getFollowers("citiamo").size() ,407);
    	assertEquals(persistenceFacade.getFriends("03Stefania55").size() ,1075);
    	assertEquals(persistenceFacade.getFollowers("03Stefania55").size() ,173);
    	logger.info("getFriendsAndFollowersTEST END");
    }
    
    
    @Test
    public void enrichUsersWithFriendsAndFollowersTEST() {
    	logger.info("enrichUsersWithFriendsAndFollowersTEST START");
    	logger.info("getting all jsons");
    	List<String> jsons = persistenceFacade.getAllJsonTweets();
    	logger.info("deserializing jsons");
    	List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	logger.info("extracting authors from tweets");
    	List<User> authors = new ArrayList<User>();
    	for (Tweet tweet : tweets)
    		authors.add(tweet.getUser());
    	authors = new ArrayList<User>( new HashSet<User>(authors));
    	logger.info("enriching authors");
    	authors = persistenceFacade.enrichUsersWithFriendsAndFollowers(authors);
    	logger.info("enrichUsersWithFriendsAndFollowersTEST END");
    }
    
    

}
