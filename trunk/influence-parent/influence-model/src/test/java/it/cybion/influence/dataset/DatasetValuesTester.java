package it.cybion.influence.dataset;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.util.DatasetJsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;

import static org.testng.Assert.assertTrue;


public class DatasetValuesTester {
	
	private static final Logger logger = Logger.getLogger(DatasetValuesTester.class);
	
	MysqlPersistenceFacade persistenceFacade;
	
	@BeforeClass
	public void setup() throws IOException {
		persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
	}
	
    @AfterClass
	public void shutdown() {
    	persistenceFacade = null;
	}
	
	@Test
	public void testsThatFollowersCountAndFriendsCountAreMax5000() {
    	List<String> jsons = persistenceFacade.getAllJsonTweets();
    	List<Tweet> tweets = new DatasetJsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	List<User> users = new ArrayList<User>();
    	for (Tweet tweet : tweets)
    		users.add(tweet.getUser());
    	users = new ArrayList<User>(new HashSet<User>(users)); //this removes duplicates
    	int currentUserIndex = 1;
    	for (User user : users) {
    		List<String> followers = persistenceFacade.getFollowers(user.getScreenName());
    		List<String> friends = persistenceFacade.getFriends(user.getScreenName());
    		logger.info(currentUserIndex+"/"+users.size()+" - "+user.getScreenName()+" - followers:"+followers.size()+" - friends:"+friends.size());
    		assertTrue(followers.size()<=5000);
    		assertTrue(friends.size()<=5000);
    		currentUserIndex++;
    	}
	}
}
