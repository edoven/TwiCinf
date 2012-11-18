package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/*
 * 
 * This test is about the dataset of trenitalia/etc 
 * in my own MySQL server
 * 
 */

//TODO change name to MysqlConnectorTestCase
public class MysqlConnectorTester {
	
	private int TWEET_COUNT = 6214;
	private String firstJsonId = "263328879631036416";
	private String lastJsonId = "266921929523462145";
	List<String> tweets;
	
	Gson gson;
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
		tweets = MysqlConnector.getAllTwitterJsons();
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
