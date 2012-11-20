package it.cybion.influence.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;

public class MysqlConnector {
	
	/*
	 * 
	 * CONNECTION DATA
	 * 
	 */
	
	private static String mySqlHost = "localhost";
	private static int mySqlPort = 3306;
	private static String mySqlUser = "root";
	private static String mySqlPassword = "qwerty";
	private static String mySqlDatabase = "twitter-monitor";

	/*
	public static void main(String[] args) {
		List<String> jsons = getAllTwitterJsons();
		for (String json: jsons)
			System.out.println(json);
		System.out.println(jsons.size());
		
	}
	*/
	
	
	
	public static List<String> getAllTwitterJsons() {
		List<String> jsons = new ArrayList<String>();
		

		TwitterMonitoringPersistenceConfiguration persistenceConfiguration =
                new TwitterMonitoringPersistenceConfiguration(
                		mySqlHost,
                		mySqlPort,
                        mySqlDatabase,
                        mySqlUser,
                        mySqlPassword);
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(1351616021000l) , new DateTime(1352474121000l), true);
		for (Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}
	
}
