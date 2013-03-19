package it.cybion.influencers.cache;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.TwitterException;

import it.cybion.influencers.cache.TwitterFacade;
import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.persistance.mongodb.MongodbPersistanceFacade;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.Twitter4jWebFacade;
import it.cybion.influencers.cache.web.TwitterWebFacade;



public class TwitterFacadeTEST
{

	private static final Logger logger = Logger.getLogger(TwitterFacadeTEST.class);

	private TwitterFacade twitterFacade;

	@BeforeClass
	public void init() throws UnknownHostException
	{
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

		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "testdb");
		twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
	}

	@Test(enabled = false)
	public void getUserTest() throws TwitterException
	{
		String user = twitterFacade.getDescription(14230524l);
		logger.info("BEGIN_" + user + "_END");
		user = twitterFacade.getDescription(14230524l);
		logger.info("BEGIN_" + user + "_END");
	}

	@Test(enabled = true)
	public void getFriends() throws TwitterException
	{
		List<Long> friendIds = twitterFacade.getFriends(253956088L);
		logger.info(friendIds.size());

		friendIds = twitterFacade.getFriends(253956088L);
		logger.info("Friends number = " + friendIds.size());
	}

	@Test(enabled = false)
	public void getFollowers() throws TwitterException
	{
		List<Long> followersIds = twitterFacade.getFollowers(426724668l);
		logger.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(887469007l);
		logger.info("Followers number = " + followersIds.size());
	}

	@Test(enabled = false)
	public void testIfTheFollowersAreSavedInTheCache() throws TwitterException
	{
		List<Long> followersIds = twitterFacade.getFollowers(426724668l);
		logger.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(426724668l);
		logger.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(426724668l);
		logger.info("Followers number = " + followersIds.size());
	}

	@Test(enabled = false)
	public void testIfTheFriendsAreSavedInTheCache() throws TwitterException
	{
		List<Long> friendIds = twitterFacade.getFriends(426724668l);
		logger.info("Friends number = " + friendIds.size());

		friendIds = twitterFacade.getFriends(426724668l);
		logger.info("Friends number = " + friendIds.size());

		friendIds = twitterFacade.getFriends(426724668l);
		logger.info("Friends number = " + friendIds.size());
	}

	@Test(enabled = false)
	public void getDescriptionsTEST() throws TwitterException
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(426724668l);
		ids.add(887469007l);
		ids.add(14230524l);
		// ids.add(43246534L);
		logger.info(twitterFacade.getDescriptions(ids));
	}

	@Test
	public void getUpTo200TweetsTEST() throws TwitterException
	{
		logger.info("--start--");
		logger.info(twitterFacade.getUpTo200Tweets(887469007l));
		logger.info("--end--");
	}

}
