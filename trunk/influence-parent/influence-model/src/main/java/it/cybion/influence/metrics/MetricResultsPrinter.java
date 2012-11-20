package it.cybion.influence.metrics;

import java.util.List;
import java.util.Map;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;

public class MetricResultsPrinter {
	
	/* 
	 * 
	 * To edit the dataset of tweets use util.MysqlConnector
	 * 
	 */
	
	public static void main(String[] args)
	{
		System.out.print("Getting jsons from the database and creating Tweet objects from json strings...");
		List<Tweet> tweets = JsonDeserializer.jsons2tweets(MysqlConnector.getAllTwitterJsons());
		System.out.println("ok!");
		
		MetricsCalculator calculator = new MetricsCalculator(tweets);
		MetricsReport report = calculator.getReport();

		System.out.println("Dataset tweets count: "+report.getTweetsCount());
		System.out.println("Dataset users count: "+report.getUsersCount());
		
		System.out.println("Follower Count AVG = "+report.getFollowersCountAVG());
		System.out.println("Friends Count AVG = "+report.getFriendsCountAVG());
		System.out.println("Followers/Friends Ratio AVG: "+report.getFollowerFriendsRatioAVG());
		//System.out.println("Tweets per user AVG among the tweets of the dataset: "+report.getTweetsPerUserAmongDatasetAVG());
		//System.out.println("Tweets per user AVG: "+report.getTweetsPerUserAVG());
		
		Map<String, Integer> hashtags2count = report.getHashtags2count();
		for (String hashtag: hashtags2count.keySet() )
			System.out.println(hashtag+" - "+hashtags2count.get(hashtag));
		
		
		/*
		int count = 0;
		for (User user: calculator.getUsers())
		{
			if (user.getStatusesCount()>25000) {
				System.out.println(user.getScreenName()+" - "+user.getStatusesCount());
				count++;
			}
			
		}
		System.out.println("Users with more than 25000 tweets written = "+count);
		*/
	}
	
}
