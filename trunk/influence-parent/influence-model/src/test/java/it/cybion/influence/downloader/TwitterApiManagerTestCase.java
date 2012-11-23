package it.cybion.influence.downloader;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TwitterApiManagerTestCase {
	
	private static final Logger logger = Logger.getLogger(TwitterApiManagerTestCase.class);

	TwitterApiManager twitterApiManager = null;
	
	@BeforeClass
	public void setup() {
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		String consumerTokenFilePath = ("/home/godzy/tokens/consumerToken.txt");
        //TODO build programmatically a list of 2 tokens, then
        //TODO use a constructor: TwitterApiManager(List<Token> tokens)
        //in this way we decouple the TwitterApiManager under test from the correct functioning of the TokenBuilder
		twitterApiManager = new TwitterApiManager(consumerTokenFilePath, userTokenFilePaths);
	}
	
	@AfterClass
	public void shutdown() {
		twitterApiManager = null;
	}
	
	@Test
	public void printResultForOneUser() {
		List<String> friendsIds = twitterApiManager.getFriends("edoventurini");
		for (String friendId : friendsIds)
			logger.info(friendId);
		logger.info("friends number: "+friendsIds.size());
        //TODO add asserts: assertTrue(friendsIds.size() > 0), maybe test that it includes
        //an id of a user you wont unfollow in the near future
	}

    //TODO test the failure: build another TAM with an empty list of user tokens.
    //in this way, the first call will fail and throw an exception. assert the exception is thrown.
    //you can add a method or add another test class
}
