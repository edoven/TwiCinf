package it.cybion.influence.downloader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlConnector;


/*
 * 
 * Application to run to download users firends
 * 
 */

public class FriendsDownloader {
	
	private static final Logger logger = Logger.getLogger(FriendsDownloader.class);

	
	public static void main(String[] args) {	
		logger.info("Getting friends to enrich.");
		List<String> usersToEnrich = getUsersToEnrich();
		logger.info(usersToEnrich.size()+" to be enriched.");
		
		int count = 0;
		
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		String consumerTokenFilePath = ("/home/godzy/tokens/consumerToken.txt");
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerTokenFilePath, userTokenFilePaths);
		

		for (int i=0; i<usersToEnrich.size(); i++) {
			String user = usersToEnrich.get(i);
			List<String> friends = twitterApiManager.getFriends(user);
			if (friends==null) {
				logger.info("EXIT! TwitterApiManager.getFriends(user)==null for user ="+user+". "+count+" users have been friends-enriched.");
				System.exit(0);
			}
			MysqlConnector.writeFriends(user, friends);	
			logger.info("Successifully extracted and saved "+friends.size()+" friends for user: "+user);
			count++;
		}
	}

	
	private static List<String> getUsers() {
		List<String> jsonTweets = MysqlConnector.getAllTwitterJsons();
		List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		HashSet<String> users = new HashSet<String>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser().getScreenName()); 
		return new ArrayList<String>(users);
	}

	
	private static List<String> getAlreadyEnrichedUsers() {
		List<String> friendsEnrichedUsers = MysqlConnector.getFriendsEnrichedUsers();
		return friendsEnrichedUsers;
	}
	
	private static List<String> getUsersToEnrich() {
		List<String> allUsers = getUsers();
		List<String> alreadyEnriched = getAlreadyEnrichedUsers();
		List<String> toEnrich = new ArrayList<String>(allUsers);
		toEnrich.removeAll(alreadyEnriched);
		return toEnrich;
		
	}
	
}