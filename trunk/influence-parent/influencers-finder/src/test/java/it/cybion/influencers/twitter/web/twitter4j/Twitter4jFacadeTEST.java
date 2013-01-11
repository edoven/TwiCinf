package it.cybion.influencers.twitter.web.twitter4j;

import static org.testng.AssertJUnit.assertTrue;
import it.cybion.influencers.twitter.web.twitter4j.Token;
import it.cybion.influencers.twitter.web.twitter4j.Twitter4jFacade;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.TwitterException;



/*
 * 
 * TODO:
 * -test the case when all tokens have reached the limit
 * 
 */


public class Twitter4jFacadeTEST {
	
	private static final Logger logger = Logger.getLogger(Twitter4jFacadeTEST.class);
	
	private Twitter4jFacade twitter4jFacade;


	@BeforeClass
	public void init() {
		Token applicationToken = new Token("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		
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
		Token userToken1 = new Token("/home/godzy/tokens/token1.txt"); 
		userTokens.add(userToken1);
		
		twitter4jFacade = new Twitter4jFacade(applicationToken, userTokens);
	}
		
	@Test(enabled=false)
	public void getUserJsonTEST() throws TwitterException {
		String user = ""; //BarackObama
//		for (int i=0; i<181; i++)
			user = twitter4jFacade.getUserJson(813286l); //BarackObama
		logger.info(user);
		assertTrue(user.contains("Barack Obama"));
		assertTrue(user.contains("followers"));
		assertTrue(user.contains("friends"));
		assertTrue(user.contains("This account is run by"));
	}
	
	@Test(enabled=false)
	public void getLessThan5000FollowersIdsTEST() throws TwitterException {
		List<Long> followerIds = twitter4jFacade.getFollowersIds(58477550);  //screenName=gifo77
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size()<5000);
	}
	
	@Test(enabled=false)
	public void getMoreThan5000FollowersIdsTEST() throws TwitterException {
		List<Long> followerIds = twitter4jFacade.getFollowersIds(444712353); //screenName=ChiaraMaci
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size()>5000);
	}
	
	@Test(enabled=false)
	public void getLessThan5000FriendsIdsTEST() throws TwitterException {
		List<Long> friendIds = twitter4jFacade.getFriendsIds(58477550); //screenName=gifo77
		logger.info("#############################");
		logger.info(friendIds);
		logger.info(friendIds.size());
		logger.info("#############################");
		assertTrue(friendIds.size()<5000);
	}
		
	@Test(enabled=false)
	public void getMoreThan5000FriendsIdsTEST() throws TwitterException {
		List<Long> followerIds = twitter4jFacade.getFriendsIds(14831419); //screenName=mattuk
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size()>5000);
	}
	
	@Test(enabled=true)
	public void getUsersJsonsTEST() throws TwitterException {
		List<Long> followerIds = new ArrayList<Long>();
		for (long i=0; i<2023; i++)
			followerIds.add(435668609+i);
		twitter4jFacade.getUsersJsons(followerIds);
	}
	
}
