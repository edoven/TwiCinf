package it.cybion.influence.downloader;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.util.MysqlPersistenceFacade;
import it.cybion.influence.util.TokenBuilder;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/*
 * 
 * Application to run to download users firends
 * 
 */

/* TODO 
*
* MysqlPersistenceFacade should have a constructor with connection parameters, one for twitter-monitor db
* and the other for twitter-users. (anyway, why not storing friends on graph?)
* pf = MysqlPersistenceFacade(monitorParameters, usersParameters)
*
* Then, build an object, always in main with the objects constructed:
* fd = new FriendsDownloader(persistenceFacade, tam);
* then, move the main logic you have before in a run() method
* and to fd.run();
*
* */

public class FriendsDownloader {
	
	private static final Logger logger = Logger.getLogger(FriendsDownloader.class);	
	private TwitterApiManager twitterApiManager;

	
	public static void main(String[] args) {	
		logger.info("Getting friends to enrich.");
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
		FriendsDownloader friendsDownloader = new FriendsDownloader(twitterApiManager);
		
		friendsDownloader.run();

	}
	
	
	
	public FriendsDownloader(TwitterApiManager twitterApiManager) {
		this.twitterApiManager = twitterApiManager;
	}
		
	public void run() {		
		List<String> usersToEnrich = getUsersToEnrich();
		int count = 0;
		
		logger.info(usersToEnrich.size()+" to be enriched.");
		for (int i=0; i<usersToEnrich.size(); i++) {
			String user = usersToEnrich.get(i);
			List<String> friends;
			try {
				friends = twitterApiManager.getFriends(user);
				if (friends.size()>0)
					MysqlPersistenceFacade.writeFriends(user, friends);	
				logger.info("Successifully extracted and saved "+friends.size()+" friends for user: "+user);
				count++;
				logger.info("Extracted friends for "+count+" users.");
			} catch (TwitterException e) {
				logger.info("Problem with user:" + user);
				logger.info(e.toString());
			}			
		}
	}
		
	private static List<Token> getTokensFromFilePaths(List<String> filePaths) {
		List<Token> tokens = new ArrayList<Token>();
		for (String filePath : filePaths)
			tokens.add(TokenBuilder.getTokenFromFile(filePath));
		return tokens;
	}

	
	private static List<String> getUsers() {
		List<String> jsonTweets = MysqlPersistenceFacade.getAllJsonTweets();
		List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		HashSet<String> users = new HashSet<String>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser().getScreenName()); 
		return new ArrayList<String>(users);
	}

	
	private static List<String> getAlreadyEnrichedUsers() {
		List<String> friendsEnrichedUsers = MysqlPersistenceFacade.getFriendsEnrichedUsers();
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