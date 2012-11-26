package it.cybion.influence.util;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class MySQL {
	
	private String host;
	private int port;
	private String user;
	private String password;
	private String database;
	
	public MySQL(String host, int port, String user, String password, String database) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
	}
	
	
	public List<String> getAllJsonTweets(long startDate, long endDate) {
		List<String> jsons = new ArrayList<String>();

		TwitterMonitoringPersistenceConfiguration persistenceConfiguration =
                new TwitterMonitoringPersistenceConfiguration(host, port,database,user,password);
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		/*
		 * TODO: this is 
		 */
		//List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(1351616021000l) , new DateTime(1352474121000l), true);
		List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(startDate) , new DateTime(endDate), true);
		for (Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}
	
}