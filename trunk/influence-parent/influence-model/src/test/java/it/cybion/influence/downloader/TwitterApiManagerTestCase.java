package it.cybion.influence.downloader;

import static org.testng.AssertJUnit.assertTrue;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.TwitterException;

import it.cybion.influence.util.TokenBuilder;

import java.util.ArrayList;
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
        //TODO build programmatically a list of 2 tokens, then
        //TODO use a constructor: TwitterApiManager(List<Token> tokens)
        //in this way we decouple the TwitterApiManager under test from the correct functioning of the TokenBuilder
		twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
	}
	
	@AfterClass
	public void shutdown() {
		twitterApiManager = null;
	}
	
	@Test
	public void printResultForOneUser() {
		try {
			List<String> friendsIds = twitterApiManager.getFriends("edoventurini");
			for (String friendId : friendsIds)
				logger.info(friendId);
			logger.info("friends number: "+friendsIds.size());
			assertTrue(friendsIds.size()>0);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
        //TODO add asserts: maybe test that it includes an id of a user you wont unfollow in the near future
	}

    //TODO test the failure: build another TAM with an empty list of user tokens.
    //in this way, the first call will fail and throw an exception. assert the exception is thrown.
    //you can add a method or add another test class
}
