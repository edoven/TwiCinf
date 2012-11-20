package it.cybion.influence.metrics;


import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;


public class MetricsResultPrinter {
	
	private static final Logger logger = Logger.getLogger(MetricsResultPrinter.class);
	
	/*
	 * This is not a real test
	 */
    @Test
    public void printsAllTheResultsFromTheReport() {
    	logger.info("======================================================");
    	logger.info("Getting jsons from the database and creating Tweet objects from json strings...");
		List<Tweet> tweets = JsonDeserializer.jsons2tweets(MysqlConnector.getAllTwitterJsons());
		logger.info("ok!");
		
		MetricsCalculator calculator = new MetricsCalculator(tweets);
		MetricsReport report = calculator.getReport();

		logger.info("Tweets count: "+report.getTweetsCount());
		logger.info("Users count: "+report.getUsersCount());
		
		logger.info("Follower Count AVG = "+report.getFollowersCountAVG());
		logger.info("Friends Count AVG = "+report.getFriendsCountAVG());
		logger.info("Followers/Friends Ratio AVG: "+report.getFollowersFriendsRatioAVG());
		//logger.info("Tweets per user AVG among the tweets of the dataset: "+report.getTweetsPerUserAmongDatasetAVG());
		//logger.info("Tweets per user AVG: "+report.getTweetsPerUserAVG());
		
		logger.info("RetweetsCount: "+report.getRetweetsCount());
		logger.info("RetweetsToTweetsRatio: "+ ((double)report.getRetweetsCount())/report.getTweetsCount());
		logger.info("users2tweetsCountAVG: "+ report.getUsers2tweetsCountAVG());
		
		
		
		
		
		logger.info("");
		
		int mostActiveUsersAmongDataset = 10;
		logger.info("Top "+mostActiveUsersAmongDataset+" most active Users among the tweets of the dataset(screenName - tweet count):");
		Map<String, Integer> users2tweetsAmongDatasetCount = report.getUsers2tweetsCountAmongDataset();
		for (String screenName: users2tweetsAmongDatasetCount.keySet() )
		{
			if ((mostActiveUsersAmongDataset--) > 0)
				logger.info(screenName+" - "+users2tweetsAmongDatasetCount.get(screenName));			
		}
		
		logger.info("");
		
		int mostActiveUsersInGeneral = 10;
		logger.info("Top "+mostActiveUsersInGeneral+" most active Users in general(screenName - count):");
		Map<String, Integer> users2tweetsCount = report.getUsers2tweetsCount();
		for (String screenName: users2tweetsCount.keySet() )
		{
			if ((mostActiveUsersInGeneral--) > 0)
				logger.info(screenName+" - "+users2tweetsCount.get(screenName));			
		}
	
		
		logger.info("");
		
		int mostMentionedUser = 10;
		logger.info("Top "+mostMentionedUser+" most mentioned Users among the tweets of the dataset(screenName - count):");
		Map<String, Integer> usersMentioned2count = report.getUserMentioned2count();
		for (String screenName: usersMentioned2count.keySet() )
		{
			if ((mostMentionedUser--) > 0)
				logger.info(screenName+" - "+usersMentioned2count.get(screenName));			
		}
		
		
		logger.info("");
		
		int mostUsedHashtag = 10;
		logger.info("Top "+mostUsedHashtag+" most used Hashtags (hashtag - count):");
		Map<String, Integer> hashtags2count = report.getHashtags2count();
		for (String hashtag: hashtags2count.keySet() )
		{
			if ((mostUsedHashtag--) > 0)
				logger.info(hashtag+" - "+hashtags2count.get(hashtag));			
		}
		
		System.out.print("======================================================");
    	
    }
}
