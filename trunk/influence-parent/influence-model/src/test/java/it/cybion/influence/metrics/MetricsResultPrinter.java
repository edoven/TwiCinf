package it.cybion.influence.metrics;


import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;


public class MetricsResultPrinter {
	
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

		System.out.println("Tweets count: "+report.getTweetsCount());
		System.out.println("Users count: "+report.getUsersCount());
		
		System.out.println("Follower Count AVG = "+report.getFollowersCountAVG());
		System.out.println("Friends Count AVG = "+report.getFriendsCountAVG());
		System.out.println("Followers/Friends Ratio AVG: "+report.getFollowersFriendsRatioAVG());
		//System.out.println("Tweets per user AVG among the tweets of the dataset: "+report.getTweetsPerUserAmongDatasetAVG());
		//System.out.println("Tweets per user AVG: "+report.getTweetsPerUserAVG());
		
		System.out.println("RetweetsCount: "+report.getRetweetsCount());
		System.out.println("RetweetsToTweetsRatio: "+ ((double)report.getRetweetsCount())/report.getTweetsCount());
		System.out.println("users2tweetsCountAVG: "+ report.getUsers2tweetsCountAVG());
		
		
		
		
		
		System.out.println("");
		
		int mostActiveUsersAmongDataset = 10;
		System.out.println("Top "+mostActiveUsersAmongDataset+" most active Users among the tweets of the dataset(screenName - tweet count):");
		Map<String, Integer> users2tweetsAmongDatasetCount = report.getUsers2tweetsCountAmongDataset();
		for (String screenName: users2tweetsAmongDatasetCount.keySet() )
		{
			if ((mostActiveUsersAmongDataset--) > 0)
				System.out.println(screenName+" - "+users2tweetsAmongDatasetCount.get(screenName));			
		}
		
		System.out.println("");
		
		int mostActiveUsersInGeneral = 10;
		System.out.println("Top "+mostActiveUsersInGeneral+" most active Users in general(screenName - count):");
		Map<String, Integer> users2tweetsCount = report.getUsers2tweetsCount();
		for (String screenName: users2tweetsCount.keySet() )
		{
			if ((mostActiveUsersInGeneral--) > 0)
				System.out.println(screenName+" - "+users2tweetsCount.get(screenName));			
		}
	
		
		System.out.println("");
		
		int mostMentionedUser = 10;
		System.out.println("Top "+mostMentionedUser+" most mentioned Users among the tweets of the dataset(screenName - count):");
		Map<String, Integer> usersMentioned2count = report.getUserMentioned2count();
		for (String screenName: usersMentioned2count.keySet() )
		{
			if ((mostMentionedUser--) > 0)
				System.out.println(screenName+" - "+usersMentioned2count.get(screenName));			
		}
		
		
		System.out.println("");
		
		int mostUsedHashtag = 10;
		System.out.println("Top "+mostUsedHashtag+" most used Hashtags (hashtag - count):");
		Map<String, Integer> hashtags2count = report.getHashtags2count();
		for (String hashtag: hashtags2count.keySet() )
		{
			if ((mostUsedHashtag--) > 0)
				System.out.println(hashtag+" - "+hashtags2count.get(hashtag));			
		}
		
		System.out.print("======================================================");
    	
    }
}
