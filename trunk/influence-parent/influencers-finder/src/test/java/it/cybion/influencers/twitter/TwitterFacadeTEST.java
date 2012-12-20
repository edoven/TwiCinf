package it.cybion.influencers.twitter;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.mongodb.MongodbPersistanceManager;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.twitter.web.twitter4j.Token;
import it.cybion.influencers.twitter.web.twitter4j.Twitter4jFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

public class TwitterFacadeTEST {
	
	private static final Logger logger = Logger.getLogger(TwitterFacadeTEST.class);
	
	private TwitterFacade twitterFacade;
	
	@BeforeClass
	public void init() throws UnknownHostException {
		Token applicationToken = new Token("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("/home/godzy/tokens/token1.txt"); 
		userTokens.add(userToken1);
		Token userToken2 = new Token("/home/godzy/tokens/token2.txt");
		userTokens.add(userToken2);
		Token userToken3 = new Token("/home/godzy/tokens/token3.txt");
		userTokens.add(userToken3);
		Token userToken4 = new Token("/home/godzy/tokens/token4.txt");
		userTokens.add(userToken4);
		Token userToken5 = new Token("/home/godzy/tokens/token5.txt");
		userTokens.add(userToken5);
		Token userToken6 = new Token("/home/godzy/tokens/token6.txt");
		userTokens.add(userToken6);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jFacade(applicationToken, userTokens, 60);
		PersistanceFacade persistanceFacade = new MongodbPersistanceManager("localhost", "testdb", "testcollection");
		twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
	}

	@Test
	public void getUserTest() throws TwitterApiException {
		String user = twitterFacade.getDescription(14230524l);
		logger.info("BEGIN_"+user+"_END");
		user = twitterFacade.getDescription(14230524l);
		logger.info("BEGIN_"+user+"_END");
	}
	
	@Test
	public void getFriends() throws TwitterApiException, YourCodeReallySucksException {
		List<Long> friendIds = twitterFacade.getFriends(887469007l);
		logger.info(friendIds.size());
	}

}
