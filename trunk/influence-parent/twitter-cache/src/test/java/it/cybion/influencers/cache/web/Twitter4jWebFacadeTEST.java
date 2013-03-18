package it.cybion.influencers.cache.web;


import static org.testng.AssertJUnit.assertTrue;
import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.Twitter4jWebFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import twitter4j.TwitterException;



/*
 * 
 * TODO:
 * -test the case when all tokens have reached the limit
 * 
 */

public class Twitter4jWebFacadeTEST
{

	private static final Logger logger = Logger.getLogger(Twitter4jWebFacadeTEST.class);

	private Twitter4jWebFacade twitter4jFacade;

	@BeforeClass
	public void init()
	{
		Token applicationToken = new Token("/home/godzy/tokens/consumerToken.properties");
		List<Token> userTokens = new ArrayList<Token>();

		Token userToken0 = new Token("/home/godzy/tokens/token0.properties");
		userTokens.add(userToken0);
		// Token userToken1 = new Token("/home/godzy/tokens/token1.txt");
		// userTokens.add(userToken1);
		// Token userToken2 = new Token("/home/godzy/tokens/token2.txt");
		// userTokens.add(userToken2);
		// Token userToken3 = new Token("/home/godzy/tokens/token3.txt");
		// userTokens.add(userToken3);
		// Token userToken4 = new Token("/home/godzy/tokens/token4.txt");
		// userTokens.add(userToken4);
		Token userToken5 = new Token("/home/godzy/tokens/token5.properties");
		userTokens.add(userToken5);
		

		twitter4jFacade = new Twitter4jWebFacade(applicationToken, userTokens);
	}

	@Test(enabled = false)
	public void getUserJsonTEST() throws TwitterException
	{
		String user = twitter4jFacade.getUserJson(813286l); // BarackObama
		assertTrue(user.contains("Barack Obama"));
		assertTrue(user.contains("followers"));
		assertTrue(user.contains("friends"));
		assertTrue(user.contains("This account is run by"));
	}

	@Test(enabled = false)
	public void getLessThan5000FollowersIdsTEST() throws TwitterException
	{
		List<Long> followerIds = twitter4jFacade.getFollowersIds(58477550); // screenName=gifo77
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size() < 5000);
	}

	@Test(enabled = false)
	public void getMoreThan5000FollowersIdsTEST() throws TwitterException
	{
		List<Long> followerIds = twitter4jFacade.getFollowersIds(444712353); // screenName=ChiaraMaci
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size() > 5000);
	}

	@Test(enabled = false)
	public void getLessThan5000FriendsIdsTEST() throws TwitterException
	{
		List<Long> friendIds = twitter4jFacade.getFriendsIds(58477550); // screenName=gifo77
		logger.info("#############################");
		logger.info(friendIds);
		logger.info(friendIds.size());
		logger.info("#############################");
		assertTrue(friendIds.size() < 5000);
	}

	@Test(enabled = false)
	public void getFriendsIdsRepeatedRequestsTEST() throws TwitterException
	{
		List<Long> friendIds = twitter4jFacade.getFriendsIds(58477550); // screenName=gifo77
		for (int i = 0; i < 15; i++)
			friendIds = twitter4jFacade.getFriendsIds(58477550);
		assertTrue(friendIds.size() < 5000);
	}

	@Test(enabled = false)
	public void getMoreThan5000FriendsIdsTEST() throws TwitterException
	{
		List<Long> followerIds = twitter4jFacade.getFriendsIds(14831419); // screenName=mattuk
		logger.info("#############################");
		logger.info(followerIds);
		logger.info(followerIds.size());
		logger.info("#############################");
		assertTrue(followerIds.size() > 5000);
	}

	@Test(enabled = false)
	public void getUsersJsonsTEST() throws TwitterException
	{
		List<Long> followerIds = new ArrayList<Long>();
		for (long i = 0; i < 101; i++)
			followerIds.add(435668609 + i);
		twitter4jFacade.getUsersJsons(followerIds);
	}
	
	@Test(enabled = false)
	public void deserializeTweetDate()
	{
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                			// "Wed Oct 17 19:59:40 +0000 2012"
                .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
		
		String jsonTweet = "{\"created_at\": \"Wed Jun 06 20:07:10 +0000 2012\"}";
		
		Tweet tweet = gson.fromJson(jsonTweet, Tweet.class);
		Date tweetDate = tweet.created_at;
		Assert.assertEquals(tweetDate.getDate(), 6);
		Assert.assertEquals(tweetDate.getMonth(), 5);
		Assert.assertEquals(tweetDate.getYear(), 2012-1900 );
	}
	
	@Test(enabled = false)
	public void getUserTweetsWithMaxId() throws TwitterException
	{
		List<String> tweets = twitter4jFacade.getTweetsWithMaxId(887469007L, -1);
		Assert.assertTrue(tweets.size()>0);
	}
	
	
	@Test(enabled = false)
	public void getTweetsFromDate1() throws TwitterException, UserWithNoTweetsException
	{
		long userId = 887469007L; //edoventurini
		int fromYear = 2012,
			toYear = 2012;
		int fromMonth = 12,
			toMonth = 12;
		int fromDay = 13,
			toDay = 15;
		SearchedByDateTweetsResultContainer resultContainer = twitter4jFacade.getuserTweetsFromDate(userId,
																	fromYear, fromMonth, fromDay,
																	toYear, 	toMonth,   toDay);
		List<String> tweets = resultContainer.getGoodTweets();
		Assert.assertEquals(tweets.size(), 2);
		for (String tweet : tweets)
		{
			logger.info(tweet);
		}
	}
	
	
	@Test(enabled = true)
	public void getTweetsFromDate2() throws TwitterException, UserWithNoTweetsException
	{
		long userId = 517903407L; //profdalimonte
		int fromYear = 2013,
			toYear = 2013;
		int fromMonth = 2,
			toMonth = 2;
		int fromDay = 4,
			toDay = 5;
		SearchedByDateTweetsResultContainer resultContainer = twitter4jFacade.getuserTweetsFromDate(userId,
																	fromYear, fromMonth, fromDay,
																	toYear, 	toMonth,   toDay);
		List<String> tweets = resultContainer.getGoodTweets();
		Assert.assertEquals(tweets.size(),4);
		for (String tweetJson : tweets)
		{
			Tweet tweet = Tweet.buildTweetFromJson(tweetJson);
			logger.info(tweet.id+" - "+tweet.created_at+" - "+tweet.originalJson);
		}
	}
	
	
	@Test(enabled = false)
	public void getTweetsFromDateCheckIfContainsDuplicates() throws TwitterException, UserWithNoTweetsException
	{
		long userId = 813286L; //BarackObama
		int fromYear = 2012,
			toYear = 2013;
		int fromMonth = 12,
			toMonth = 1;
		int fromDay = 1,
			toDay = 1;
		Date fromDate = new Date(fromYear-1900,fromMonth-1,fromDay);
		Date toDate = new Date(toYear-1900,toMonth-1,toDay);
		SearchedByDateTweetsResultContainer resultContainer = twitter4jFacade.getuserTweetsFromDate(userId,
																	fromYear, fromMonth, fromDay,
																	toYear, 	toMonth,   toDay);
		List<String> tweetJsons = resultContainer.getGoodTweets();
		Set<String> tweetJsonsSet = new HashSet<String>(tweetJsons);
		Assert.assertTrue(tweetJsons.size()>20);
		Assert.assertEquals(tweetJsons.size(), tweetJsonsSet.size());
	}
	
	@Test(enabled = false)
	public void getTweetsFromDateCheckIfDateIsCorrect() throws TwitterException, UserWithNoTweetsException
	{
		long userId = 813286L; //BarackObama
		int fromYear = 2013,
			toYear = 2013;
		int fromMonth = 1,
			toMonth = 3;
		int fromDay = 1,
			toDay = 1;
		Date fromDate = new Date(fromYear-1900,fromMonth-1,fromDay);
		Date toDate = new Date(toYear-1900,toMonth-1,toDay);
		SearchedByDateTweetsResultContainer resultContainer = twitter4jFacade.getuserTweetsFromDate(userId,
																	fromYear, fromMonth, fromDay,
																	toYear, 	toMonth,   toDay);
		List<String> tweetJsons = resultContainer.getGoodTweets();
		for (String tweetJson : tweetJsons)
		{
			Gson gson = new GsonBuilder()
						.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
									// "Wed Oct 17 19:59:40 +0000 2012"
						.setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
			Tweet tweet = gson.fromJson(tweetJson, Tweet.class);
			logger.info(tweet.created_at);
			Assert.assertTrue(tweet.created_at.compareTo(fromDate)>0);
			Assert.assertTrue(tweet.created_at.compareTo(toDate)<0);			
		}
	}
}
