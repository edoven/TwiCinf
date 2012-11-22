package it.cybion.influence.util;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MysqlConnector {

    //TODO why not local variables?
	private static String mySqlHost = "localhost";
	private static int mySqlPort = 3306;
	private static String mySqlUser = "root";
	private static String mySqlPassword = "qwerty";
	private static String mySqlDatabase = "twitter-monitor";

    //TODO there should be a constructor that gets connection parameters,
    //and then it instantiates an instance variable tweetDao with the configuration.
    //in this way, getAllTwitterJsons calls just selectTweetsByQuery and returns jsons

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
