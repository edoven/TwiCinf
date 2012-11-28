package it.cybion.influence.metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.InputReader;
import it.cybion.influence.util.JsonDeserializer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;

public class MetricsCalculatorTestCase {
	
	@BeforeClass
	public void setup() throws IOException {		
	}
	
    @AfterClass
	public void shutdown(){
	}
		
    @Test
    public void testsIfTheMetricsAreCorrect() {
    	
    	String json01path = "src/test/resources/tweet01.json",
    		   json02path = "src/test/resources/tweet02.json",
    		   json03path = "src/test/resources/tweet03.json";
    	Tweet tweet01 = null, 
    		  tweet02 = null, 
    		  tweet03 = null;
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	JsonDeserializer deserializer = new JsonDeserializer();
		try {
			tweet01 = deserializer.deserializeJsonStringToTweet(InputReader.fileContentToSingleLine(json01path));
			tweet01 = deserializer.deserializeJsonStringToTweet(InputReader.fileContentToSingleLine(json02path));
    		tweet01 = deserializer.deserializeJsonStringToTweet(InputReader.fileContentToSingleLine(json03path));
		} catch (IOException e) {
			e.printStackTrace();
		}
        tweets.add(tweet01);
		tweets.add(tweet02);
		tweets.add(tweet03);
		
        MetricsReport metricsReport = new MetricsCalculator(tweets).getReport();

        Map<String, Integer> localUsers2tweetsCountAmongDataset =
                metricsReport.getUsersTotweetsCountAmongDataset();

        //how it is different from previous method?
        Map<String, Integer> users2tweetsCount =
                metricsReport.getUsersTotweetsCount();

        Map<String, Integer> hashtags2counts =
                metricsReport.getHashtagsTocount();

        Map<String, Integer> localUsersMentioned2counts = metricsReport.getUserMentionedTocount();

    	//Basic info
    	assertEquals(metricsReport.getTweetsCount(), 3);
    	assertEquals(metricsReport.getUsersCount(), 3);  	
    	assertEquals(metricsReport.getFollowersCountAVG(), ((double)(123 + 98 + 61)) / 3 );
    	assertEquals(metricsReport.getFriendsCountAVG(), ((double)(93 + 193 + 266)) / 3 );   	
    	assertEquals(metricsReport.getFollowersFriendsRatioAVG(), ((123.0/93) + (98.0/193) + (61.0/266)) / 3.0 );
    	
    	//users2tweetsCountAmongDataset
    	assertEquals(localUsers2tweetsCountAmongDataset.size(), 3);
    	assertEquals((int)localUsers2tweetsCountAmongDataset.get(tweet01.getUser().getScreenName()), 1);
    	assertEquals((int)localUsers2tweetsCountAmongDataset.get(tweet02.getUser().getScreenName()), 1);
    	assertEquals((int)localUsers2tweetsCountAmongDataset.get(tweet03.getUser().getScreenName()), 1);
    	
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
    	assertEquals(localUsersMentioned2counts.size(), 5);
    }
}
