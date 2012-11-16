package it.cybion.influence.metrics;

import static org.testng.AssertJUnit.assertEquals;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.InputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetricsCalculatorTester {
	
	/*
	 * METRICS
	 */
	double followersCountAVG = (123 + 98 + 61) / 3; // = 94
	double friendsCountAVG = (93 + 193 + 266) / 3; // = 184
	double followerFriendsRatioAVG = ( (123.0/93) + (98.0/193) + (61.0/266) ) / 3;
	double tweetsPerUserAVG = ( 996 + 1171 + 518 ) / 3;
	
	int tweetCountAmongDatasetUser01 = 1;
	int tweetCountAmongDatasetUser02 = 1;
	int tweetCountAmongDatasetUser03 = 1;
	double tweetsPerUserAmongDatasetAVG = 1;
	
	int tweetCountUser01 = 996;
	int tweetCountUser02 = 1171;
	int tweetCountUser03 = 518;
	double tweetCountUserAVG = (996 + 1171 + 518) / 3;
	
	String mostActiveUserScreenName01 = "PaolaGigante62";
	String mostActiveUserScreenName02 = "PerugiaToday";
	String mostActiveUserScreenName03 = "lauramesolella";
	
	/*
	 *  VARIABLES
	 */
	String json01path = "src/test/resources/tweet01.json";
	String json02path = "src/test/resources/tweet02.json";
	String json03path = "src/test/resources/tweet03.json";
	Tweet tweet01, tweet02, tweet03;
	List<User> users;
	List<Tweet> tweets;
	Gson gson;
	MetricsCalculator metricsCalculator;
	Map<User, Integer> users2tweetsCountAmongDataset;
	Map<User, Integer> users2tweetsCount;
	Map<User, Integer> mostActiveUsers2tweetCount;
	List<User> mostActiveUsers;
	Map<User, Integer> mostActiveUsersAmongDataset2tweetCount;
	
	
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
		
		tweet01 = gson.fromJson(InputReader.readJsonFile(json01path), Tweet.class);
		tweet02 = gson.fromJson(InputReader.readJsonFile(json02path), Tweet.class);
		tweet03 = gson.fromJson(InputReader.readJsonFile(json03path), Tweet.class);
		
		tweets = new ArrayList<Tweet>();
		tweets.add(tweet01);
		tweets.add(tweet02);
		tweets.add(tweet03);
		
		users = new ArrayList<User>();
		users.add(tweet01.getUser());
		users.add(tweet02.getUser());
		users.add(tweet03.getUser());
		
		metricsCalculator = new MetricsCalculator();
		metricsCalculator.setTweets(tweets);
		metricsCalculator.setUsers(users);
		
		users2tweetsCountAmongDataset = metricsCalculator.getUsers2tweetsCountAmongDataset();
		users2tweetsCount = metricsCalculator.getUsers2tweetsCount();
		
		mostActiveUsers2tweetCount = metricsCalculator.getMostActiveTwitters();
		mostActiveUsers = new ArrayList<User>(mostActiveUsers2tweetCount.keySet());
	}

    @AfterClass
	public void shutdown()
	{
        gson = null;
        tweet01 = null;
        tweet02 = null;
        tweet03 = null;
        tweets = null;
        users = null;
        metricsCalculator = null;
	}
	
	
    @Test
    public void testsIfTheMetricsAreCorrect() {
    	assertEquals(metricsCalculator.getFollowersCountAVG(), followersCountAVG);
    	assertEquals(metricsCalculator.getFriendsCountAVG(), friendsCountAVG);   	
    	assertEquals(metricsCalculator.getFollowerFriendsRatioAVG(), followerFriendsRatioAVG);
    	  	
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet01.getUser()), tweetCountAmongDatasetUser01);
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet02.getUser()), tweetCountAmongDatasetUser02);
    	assertEquals((int)users2tweetsCountAmongDataset.get(tweet03.getUser()), tweetCountAmongDatasetUser03);
    	assertEquals(metricsCalculator.getTweetsPerUserAmongDatasetAVG(), tweetsPerUserAmongDatasetAVG);
    	
    	assertEquals((int)users2tweetsCount.get(tweet01.getUser()), tweetCountUser01);
    	assertEquals((int)users2tweetsCount.get(tweet02.getUser()), tweetCountUser02);
    	assertEquals((int)users2tweetsCount.get(tweet03.getUser()), tweetCountUser03);
    	assertEquals(metricsCalculator.getTweetsPerUserAVG(), tweetsPerUserAVG);
    	
    	assertEquals(mostActiveUsers.get(0).getScreenName(), mostActiveUserScreenName01);
    	assertEquals(mostActiveUsers.get(1).getScreenName(), mostActiveUserScreenName02);
    	assertEquals(mostActiveUsers.get(2).getScreenName(), mostActiveUserScreenName03);
    	
    	/*
    	 * This doesn't test the sorting order because every user has count=1
    	 */
    	assertEquals(mostActiveUsersAmongDataset2tweetCount.size(), 3);
    	
    	
    }
}
