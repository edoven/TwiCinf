package it.cybion.influence.util;

import it.cybion.influence.model.Tweet;
import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.testng.Assert.assertEquals;

/*
 * 
 * This test is about the dataset of trenitalia/etc 
 * in my own MySQL server
 * 
 */

public class MysqlConnectorTestCase {
	
	/*
	 * Tested Dataset Data
	 */
	private int TWEET_COUNT = 6214;
	private String firstJsonId = "263328879631036416";
	private String lastJsonId = "266921929523462145";
	List<String> tweets;
	
	
	/*
	 * Connection data
	 */
	private static String mySqlHost = "localhost";
	private static int mySqlPort = 3306;
	private static String mySqlUser = "root";
	private static String mySqlPassword = "qwerty";
	private static String mySqlDatabase = "twitter-monitor";
	
	
	Gson gson;
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
		tweets = getTweets();
	}
	
	
	/*
	 * Method extracted from MysqlConnector
	 */
	public List<String> getTweets() {
		List<String> jsons = new ArrayList<String>();
		

		TwitterMonitoringPersistenceConfiguration persistenceConfiguration =
                new TwitterMonitoringPersistenceConfiguration(
                		mySqlHost,
                		mySqlPort,
                        mySqlDatabase,
                        mySqlUser,
                        mySqlPassword);
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		List<it.cybion.monitor.model.Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(1351616021000l) , new DateTime(1352474121000l), true);
		for (it.cybion.monitor.model.Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}

    @AfterClass
	public void shutdown()
	{
        gson = null;
        tweets = null;
	}
	
	
    @Test
    public void testIfTheDatasetSizeIsCorrect() {
    	assertEquals(tweets.size(), TWEET_COUNT);
    }
    
    @Test
    public void testIfTheFirstAndLastJsonsAreCorrect() {
    	Tweet firstTweet = gson.fromJson(tweets.get(0), Tweet.class);
    	Tweet lastTweet = gson.fromJson(tweets.get(tweets.size()-1), Tweet.class);
    	assertEquals(firstTweet.getId(), firstJsonId);
    	assertEquals(lastTweet.getId(), lastJsonId);
    }

}
