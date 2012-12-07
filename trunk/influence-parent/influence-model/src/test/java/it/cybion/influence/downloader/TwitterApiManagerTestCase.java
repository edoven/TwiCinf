package it.cybion.influence.downloader;

import static org.testng.AssertJUnit.assertTrue;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.TwitterException;

import it.cybion.influence.util.TokenBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TwitterApiManagerTestCase {
	
	private static final Logger logger = Logger.getLogger(TwitterApiManagerTestCase.class);

	TwitterApiManager twitterApiManager = null;
	
	@BeforeClass
	public void setup() {
		List<Token> userTokens = new ArrayList<Token>();
		userTokens.add(TokenBuilder.getTokenFromFile("/home/godzy/tokens/token1.txt"));
		userTokens.add(TokenBuilder.getTokenFromFile("/home/godzy/tokens/token1.txt"));
		userTokens.add(TokenBuilder.getTokenFromFile("/home/godzy/tokens/token1.txt"));
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");        
		twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
	}
	
	@AfterClass
	public void shutdown() {
		twitterApiManager = null;
	}
	
	@Test
	public void printResultForOneUser() {
		try {
			List<String> friendsIds = twitterApiManager.getUpTo5000FriendsIds("edoventurini");
			for (String friendId : friendsIds)
				logger.info(friendId);
			logger.info("friends number: "+friendsIds.size());
			assertTrue(friendsIds.size()>0);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
        //TODO add asserts: maybe test that it includes an id of a user you wont unfollow in the near future
	}
	
	
	@Test
	public void testGetFriendsForUserWIthMoreThan5000followers() {
		try {
			List<String> ids = twitterApiManager.getAllFollowersIds("toccodizenzero");
			assertTrue(ids.size()>5000);
			assertTrue(ids.size() == (new ArrayList<String>(new HashSet<String>(ids)).size()) );
			logger.info("followers = "+ids.size());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetFriendsForUserWIthMoreThan5000friends() {
		try {
			List<String> ids = twitterApiManager.getAllFriendsIds("SfigataMente");
			assertTrue(ids.size()>5000);
			assertTrue(ids.size() == (new ArrayList<String>(new HashSet<String>(ids)).size()) );
			logger.info("friends = "+ids.size());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    //TODO test the failure: build another TAM with an empty list of user tokens.
    //in this way, the first call will fail and throw an exception. assert the exception is thrown.
    //you can add a method or add another test class
}
