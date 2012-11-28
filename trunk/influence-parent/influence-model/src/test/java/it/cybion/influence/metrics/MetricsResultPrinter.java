package it.cybion.influence.metrics;


import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MetricsResultPrinter {
	
	private static final Logger logger = Logger.getLogger(MetricsResultPrinter.class);
	
	/*
	 * 
	 * This is not a real test, it only prints results.
	 */
    @Test
    public void printsResultsFromTheReportGeneratedWithMetricsCalculator() {
    	logger.info("======================================================");
    	List<String> jsons = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter").getAllJsonTweets();
    	JsonDeserializer jd = new JsonDeserializer();
		List<Tweet> tweets = jd.deserializeJsonStringsToTweets(jsons);
		
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
		Map<String, Integer> users2tweetsAmongDatasetCount = report.getUsersTotweetsCountAmongDataset();
		for (String screenName: users2tweetsAmongDatasetCount.keySet() )
		{
			if ((mostActiveUsersAmongDataset--) > 0)
				logger.info(screenName+" - "+users2tweetsAmongDatasetCount.get(screenName));			
		}
		
		logger.info("");
		
		int mostActiveUsersInGeneral = 10;
		logger.info("Top "+mostActiveUsersInGeneral+" most active Users in general(screenName - count):");
		Map<String, Integer> users2tweetsCount = report.getUsersTotweetsCount();
		for (String screenName: users2tweetsCount.keySet() )
		{
			if ((mostActiveUsersInGeneral--) > 0)
				logger.info(screenName+" - "+users2tweetsCount.get(screenName));			
		}
		
		logger.info("");
		
		int mostMentionedUser = 10;
		logger.info("Top "+mostMentionedUser+" most mentioned Users among the tweets of the dataset(screenName - count):");
		Map<String, Integer> usersMentioned2count = report.getUserMentionedTocount();
		for (String screenName: usersMentioned2count.keySet() )
		{
			if ((mostMentionedUser--) > 0)
				logger.info(screenName+" - "+usersMentioned2count.get(screenName));			
		}

		logger.info("");
		
		int mostUsedHashtag = 10;
		logger.info("Top "+mostUsedHashtag+" most used Hashtags (hashtag - count):");
		Map<String, Integer> hashtags2count = report.getHashtagsTocount();
		for (String hashtag: hashtags2count.keySet() )
		{
			if ((mostUsedHashtag--) > 0)
				logger.info(hashtag+" - "+hashtags2count.get(hashtag));			
		}
				
		/*
		 * Calculating users with too much friends or followers :)
		 */				
		List<String> usersWithTooMuchFriends = new ArrayList<String>();
		String result = "";
		for (User user : report.getUsers())
		{
			if (user.getFriendsCount()>5000)
			{
				usersWithTooMuchFriends.add(user.getScreenName());
				result = result + user.getScreenName() + " ";
			}
		}
		logger.info("Users with more than 5000 friends: "+usersWithTooMuchFriends.size());
		logger.info(result);
				
		logger.info("");
		
		List<String> usersWithTooMuchFollowers = new ArrayList<String>();
		result = "";
		for (User user : report.getUsers())
		{
			if (user.getFollowersCount()>5000)
			{
				usersWithTooMuchFollowers.add(user.getScreenName());
				result = result + user.getScreenName() + " ";
			}
		}
		logger.info("Users with more than 5000 followers: "+usersWithTooMuchFollowers.size());
		logger.info(result);		
		
		logger.info("");
				
		List<String> usersWithTooMuchFollowersAndFriends = new ArrayList<String>();
		result = "";
		for (User user : report.getUsers())
		{
			if (user.getFollowersCount()>5000 && user.getFriendsCount()>5000)
			{
				usersWithTooMuchFollowersAndFriends.add(user.getScreenName());
				result = result + user.getScreenName() + " ";
			}
		}
		logger.info("Users with more than 5000 followers and 5000 friends: "+usersWithTooMuchFollowersAndFriends.size());
		logger.info(result);
				
		logger.info("");
				
		List<String> normalUsers = new ArrayList<String>();
		for (User user : report.getUsers())
			normalUsers.add(user.getScreenName());
		normalUsers.removeAll(usersWithTooMuchFriends);
		normalUsers.removeAll(usersWithTooMuchFollowers);
		normalUsers.removeAll(usersWithTooMuchFollowersAndFriends);
		logger.info("Users with less than 5000 friends and 5000 followers: "+normalUsers.size());
				
		logger.info("======================================================");
    }
    
}
