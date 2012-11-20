package it.cybion.influence.metrics;

import static org.testng.AssertJUnit.assertEquals;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.InputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetricsCalculatorTestCase {
	

	/*
	 *  VARIABLES
	 */
	String json01path = "src/test/resources/tweet01.json";
	String json02path = "src/test/resources/tweet02.json";
	String json03path = "src/test/resources/tweet03.json";
	Tweet tweet01, tweet02, tweet03;
	List<Tweet> tweets = new ArrayList<Tweet>();
	Gson gson;
	MetricsReport metricsReport;
	Map<String, Integer> users2tweetsCountAmongDataset = new HashMap<String, Integer>();
	Map<String, Integer> users2tweetsCount = new HashMap<String, Integer>();
	Map<User, Integer> mostActiveUsers2tweetCount = new HashMap<User, Integer>();
	Map<String, Integer> hashtags2counts = new HashMap<String, Integer>();
	Map<String, Integer> usersMentioned2counts = new HashMap<String, Integer>();
	
	
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
		
		tweet01 = gson.fromJson(InputReader.readJsonFile(json01path), Tweet.class);
		tweet02 = gson.fromJson(InputReader.readJsonFile(json02path), Tweet.class);
		tweet03 = gson.fromJson(InputReader.readJsonFile(json03path), Tweet.class);
		

		tweets.add(tweet01);
		tweets.add(tweet02);
		tweets.add(tweet03);

		metricsReport = new MetricsCalculator(tweets).getReport();
		
		users2tweetsCountAmongDataset = metricsReport.getUsers2tweetsCountAmongDataset();
		users2tweetsCount = metricsReport.getUsers2tweetsCount();

		hashtags2counts = metricsReport.getHashtags2count();
		
		usersMentioned2counts = metricsReport.getUserMentioned2count();
		
	}
	

    @AfterClass
	public void shutdown()
	{
        gson = null;
        tweet01 = null;
        tweet02 = null;
        tweet03 = null;
        tweets = null;
        metricsReport = null;      
        users2tweetsCountAmongDataset = null;
        users2tweetsCount = null;
        hashtags2counts = null;
	}
	
	
    @Test
    public void testsIfTheMetricsAreCorrect() {

    	//Basic info
    	assertEquals(metricsReport.getTweetsCount(), 3);
    	assertEquals(metricsReport.getUsersCount(), 3);  	
    	assertEquals(metricsReport.getFollowersCountAVG(), ((double)(123 + 98 + 61)) / 3 );
    	assertEquals(metricsReport.getFriendsCountAVG(), ((double)(93 + 193 + 266)) / 3 );   	
    	assertEquals(metricsReport.getFollowersFriendsRatioAVG(), ((123.0/93) + (98.0/193) + (61.0/266)) / 3.0 );
    	
    	//users2tweetsCountAmongDataset
    	assertEquals(users2tweetsCountAmongDataset.size(), 3);  	
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet01.getUser().getScreenName()), 1);
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet02.getUser().getScreenName()), 1);
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet03.getUser().getScreenName()), 1);
    	
    	//users2tweetsCount
    	assertEquals(users2tweetsCount.size(), 3); 
    	assertEquals((int)users2tweetsCount.get(tweet01.getUser().getScreenName()), 996);
    	assertEquals((int)users2tweetsCount.get(tweet02.getUser().getScreenName()), 1171);
    	assertEquals((int)users2tweetsCount.get(tweet03.getUser().getScreenName()), 518);
    	assertEquals(metricsReport.getUsers2tweetsCountAVG(), ( (996.0+1171.0+518.0) / 3) );
    	
    	
    	//hashtags2counts
    	assertEquals(hashtags2counts.size(), 1); 	
    	assertEquals((int)hashtags2counts.get("basket"), 1);
    	
    	//retweetsCount
    	assertEquals(metricsReport.getRetweetsCount(), 1); 	
    	
    	//usersMentioned2counts
    	assertEquals(usersMentioned2counts.size(), 5); 	
    	
    	
    	
    }
    
    
}
