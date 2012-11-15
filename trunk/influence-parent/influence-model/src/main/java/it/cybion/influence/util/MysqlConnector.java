package it.cybion.influence.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;

public class MysqlConnector {
	
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
                        "localhost",
                        3306,
                        "twitter",
                        "root",
                        "qwerty");
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(1351616021000l) , new DateTime(1352474121000l), true);
		for (Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}
	
}
