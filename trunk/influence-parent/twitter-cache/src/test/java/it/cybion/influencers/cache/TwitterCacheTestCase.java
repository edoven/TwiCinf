package it.cybion.influencers.cache;

import it.cybion.influencers.cache.exceptions.TwitterCacheException;
import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.persistance.exceptions.PersistenceFacadeException;
import it.cybion.influencers.cache.utils.CalendarManager;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.WebFacade;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import twitter4j.TwitterException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TwitterCacheTestCase
{

	private static final Logger LOGGER = Logger.getLogger(TwitterCacheTestCase.class);

	private TwitterCache twitterFacade;

	@BeforeClass
	public void setUp() throws PersistenceFacadeException {
        //TODO remove absolute paths
        Token applicationToken = new Token("/home/godzy/tokens/consumerToken.properties");
        List<Token> userTokens = new ArrayList<Token>();
        Token userToken0 = new Token("/home/godzy/tokens/token0.properties");
        userTokens.add(userToken0);
        Token userToken1 = new Token("/home/godzy/tokens/token1.properties");
        userTokens.add(userToken1);
        Token userToken2 = new Token("/home/godzy/tokens/token2.properties");
        userTokens.add(userToken2);
        Token userToken3 = new Token("/home/godzy/tokens/token3.properties");
        userTokens.add(userToken3);
        Token userToken4 = new Token("/home/godzy/tokens/token4.properties");
        userTokens.add(userToken4);
        Token userToken5 = new Token("/home/godzy/tokens/token5.properties");
        userTokens.add(userToken5);

        WebFacade twitterWebFacade = WebFacade.getInstance(applicationToken, userTokens);
        PersistenceFacade persistenceFacade = null;

        persistenceFacade = PersistenceFacade.getInstance("localhost", "testdb");

        twitterFacade = TwitterCache.getInstance(twitterWebFacade, persistenceFacade);
    }

	@Test(enabled = false)
	public void getUserTest() throws TwitterException
	{
		String user = twitterFacade.getDescription(14230524l);
		LOGGER.info("BEGIN_" + user + "_END");
		user = twitterFacade.getDescription(14230524l);
		LOGGER.info("BEGIN_" + user + "_END");
	}

	@Test(enabled = false)
	public void getFriends() throws TwitterException
	{
		List<Long> friendIds = twitterFacade.getFriends(253956088L);
		LOGGER.info(friendIds.size());

		friendIds = twitterFacade.getFriends(253956088L);
		LOGGER.info("Friends number = " + friendIds.size());
	}

	@Test(enabled = false)
	public void getFollowers() throws TwitterException
	{
		List<Long> followersIds = twitterFacade.getFollowers(426724668l);
		LOGGER.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(887469007l);
		LOGGER.info("Followers number = " + followersIds.size());
	}

	@Test(enabled = false)
	public void testIfTheFollowersAreSavedInTheCache() throws TwitterException
	{
		List<Long> followersIds = twitterFacade.getFollowers(426724668l);
		LOGGER.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(426724668l);
		LOGGER.info("Followers number = " + followersIds.size());

		followersIds = twitterFacade.getFollowers(426724668l);
		LOGGER.info("Followers number = " + followersIds.size());
	}

	@Test(enabled = false)
	public void testIfTheFriendsAreSavedInTheCache() throws TwitterException
	{
		List<Long> friendIds = twitterFacade.getFriends(426724668l);
		LOGGER.info("Friends number = " + friendIds.size());

		friendIds = twitterFacade.getFriends(426724668l);
		LOGGER.info("Friends number = " + friendIds.size());

		friendIds = twitterFacade.getFriends(426724668l);
		LOGGER.info("Friends number = " + friendIds.size());
	}

	@Test(enabled = false)
	public void getDescriptionsTEST() throws TwitterCacheException {
		List<Long> ids = new ArrayList<Long>();
		ids.add(426724668l);
		ids.add(887469007l);
		ids.add(14230524l);
		// ids.add(43246534L);
        Map<Long,String> descriptions = twitterFacade.getDescriptions(ids);
        LOGGER.info(descriptions);
	}

	@Test(enabled = false)
	public void getUpTo200TweetsTEST() throws TwitterException, ProtectedUserException
	{
		LOGGER.info("--start--");
		LOGGER.info(twitterFacade.getLast200Tweets(887469007l));
		LOGGER.info("--end--");
	}
	
	
	@Test(enabled = true)
	public void getTweetsByDateTEST() throws TwitterException, ProtectedUserException
	{
		long userId = 813286L; //BarackObama
		Date fromDate = CalendarManager.getDate(2012, 12, 1);
		Date toDate   = CalendarManager.getDate(2013, 1, 1);
		List<String> tweetJsons = twitterFacade.getTweetsByDate(userId,fromDate,toDate);
		Assert.assertTrue(tweetJsons.size()>20);
	}
	
	
	@Test(enabled = true)
	public void getTweetsByDateWithStrangeUserTEST() 
	{
		long userId = 16361000; //user: InStyle
		Date fromDate = CalendarManager.getDate(2013, 2, 1);
		Date toDate   = CalendarManager.getDate(2013, 2, 20);
		List<String> tweetJsons = null;
		try
		{
			tweetJsons = twitterFacade.getTweetsByDate(userId,fromDate,toDate);
		}
		catch (TwitterException e)
		{
			Assert.assertTrue(false);
		}
		catch (ProtectedUserException e)
		{
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);		
		LOGGER.info(tweetJsons.size());
		Assert.assertTrue(tweetJsons.size()>20);
	}
	
}
