package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;
import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/*
 * 
 * This test is about the dataset of trenitalia/etc 
 * in my own MySQL server
 * 
 */

public class MysqlConnectorTestCase {

    //TODO move values where are used, using a local variable
	private static int TWEET_COUNT = 6214;
	private static String firstJsonId = "263328879631036416";
	private static String lastJsonId = "266921929523462145";
	private List<String> tweets;

	private static String mySqlHost = "localhost";
	private static int mySqlPort = 3306;
	private static String mySqlUser = "root";
	private static String mySqlPassword = "qwerty";
	private static String mySqlDatabase = "twitter-monitor";

	private Gson gson;
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
        /* TODO instantiate a MysqlConnector and save the instance to a local variable:
         it is the class under test, since we are in the MysqlConnector...TestCase!
         */
		tweets = loadTweetsFromPersistence();
	}
	
	/*
	 * TODO this is one of the worst practices: duplicate code from a class just for the
	 * sake of testing its functioning.
	  * duplicated code costs to maintain: what if i want to change it here?
	  * how do i know where i have to replicate my change?
	  * This is another side-effect of having no constructor for MysqlConnector:
	  * you can't instantiate it with the parameters you want, since the static variables inside
	  * cant be overwritten in any case.
	  *
	 * Method extracted from MysqlConnector
	 */
	public List<String> loadTweetsFromPersistence() {
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
        //TODO move gson instance here since its only used here for a specific test
    	Tweet firstTweet = gson.fromJson(tweets.get(0), Tweet.class);
    	Tweet lastTweet = gson.fromJson(tweets.get(tweets.size()-1), Tweet.class);
        //TODO move ids here, it's easier when reading tests
    	assertEquals(firstTweet.getId(), firstJsonId);
    	assertEquals(lastTweet.getId(), lastJsonId);
    }

}
