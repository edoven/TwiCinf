package it.cybion.influencers.twitter.web;

import static org.testng.AssertJUnit.assertTrue;
import it.cybion.influencers.twitter.web.twitter4j.Token;
import it.cybion.influencers.twitter.web.twitter4j.Twitter4jFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Twitter4jFacadeTEST {
	
	private static final Logger logger = Logger.getLogger(Twitter4jFacadeTEST.class);
	
	private Twitter4jFacade twitter4jFacade;


	@BeforeClass
	public void init() {
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
		
		twitter4jFacade = new Twitter4jFacade(applicationToken, userTokens, 60);
	}
	
	@Test
	public void getLessThan5000FollowersIdsTEST() throws TwitterApiException {
		List<Long> followerIds = twitter4jFacade.getFollowersIds("gifo77");
		logger.info(followerIds);
		logger.info(followerIds.size());
		assertTrue(followerIds.size()>5);
	}
	
	@Test
	public void getLessThan5000FriendsIdsTEST() throws TwitterApiException {
		List<Long> friendIds = twitter4jFacade.getFriendsIds("gifo77");
		logger.info(friendIds);
		logger.info(friendIds.size());
		assertTrue(friendIds.size()>40);
	}
	
	@Test
	public void getMoreThan5000FollowersIdsTEST() throws TwitterApiException {
		List<Long> followerIds = twitter4jFacade.getFollowersIds("ChiaraMaci");
		logger.info(followerIds);
		logger.info(followerIds.size());
		assertTrue(followerIds.size()>5000);
	}
	
	@Test
	public void getMoreThan5000FriendssIdsTEST() throws TwitterApiException {
		List<Long> followerIds = twitter4jFacade.getFriendsIds("mattuk");
		logger.info(followerIds);
		logger.info(followerIds.size());
		assertTrue(followerIds.size()>5000);
	}
	
	
	

}
