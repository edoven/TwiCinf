package it.cybion.influence.metrics;


import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class MetricsResultPrinter {

	@BeforeClass
	public void setup() throws IOException
	{
		
		
	}
	

    @AfterClass
	public void shutdown()
	{
        
	}
	
	/*
	 * This is not a real test
	 */
    @Test
    public void printsAllTheResultsFromTheReport() {
    	System.out.print("======================================================");
    	System.out.print("Getting jsons from the database and creating Tweet objects from json strings...");
		List<Tweet> tweets = JsonDeserializer.jsons2tweets(MysqlConnector.getAllTwitterJsons());
		System.out.println("ok!");
		
		MetricsCalculator calculator = new MetricsCalculator(tweets);
		MetricsReport report = calculator.getReport();

		System.out.println("Dataset tweets count: "+report.getTweetsCount());
		System.out.println("Dataset users count: "+report.getUsersCount());
		
		System.out.println("Follower Count AVG = "+report.getFollowersCountAVG());
		System.out.println("Friends Count AVG = "+report.getFriendsCountAVG());
		System.out.println("Followers/Friends Ratio AVG: "+report.getFollowersFriendsRatioAVG());
		//System.out.println("Tweets per user AVG among the tweets of the dataset: "+report.getTweetsPerUserAmongDatasetAVG());
		//System.out.println("Tweets per user AVG: "+report.getTweetsPerUserAVG());
		
		Map<String, Integer> hashtags2count = report.getHashtags2count();
		for (String hashtag: hashtags2count.keySet() )
			System.out.println(hashtag+" - "+hashtags2count.get(hashtag));
		
		
		System.out.println("RetweetsCount: "+report.getRetweetsCount());
		System.out.println("RetweetsToTweetsRatio: "+ ((double)report.getRetweetsCount())/report.getTweetsCount());
		System.out.print("======================================================");
    	
    }
}
