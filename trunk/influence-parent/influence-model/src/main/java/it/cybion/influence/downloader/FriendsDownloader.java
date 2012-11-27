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

public class FriendsDownloader {
	
	private static final Logger logger = Logger.getLogger(FriendsDownloader.class);	
	private TwitterApiManager twitterApiManager;
	private MysqlPersistenceFacade mysqlPersistenceFacade;

	
	public static void main(String[] args) {	
		logger.info("Getting friends to enrich.");
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
		//MysqlPersistenceFacade(String host, int port, String mysqlUser, String password, String database)
		MysqlPersistenceFacade mysqlPersistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter-users");
		FriendsDownloader friendsDownloader = new FriendsDownloader(twitterApiManager, mysqlPersistenceFacade);
		
		friendsDownloader.run();

	}
	
	
	
	public FriendsDownloader(TwitterApiManager twitterApiManager, MysqlPersistenceFacade mysqlPersistenceFacade) {
		this.twitterApiManager = twitterApiManager;
		this.mysqlPersistenceFacade = mysqlPersistenceFacade;
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
					mysqlPersistenceFacade.writeFriends(user, friends);	
				
				count++;
				logger.info("Successifully extracted and saved "+friends.size()+" friends for user: "+user+". Extracted friends for "+count+" users.");
			} catch (TwitterException e) {
				logger.info("Problem with user:" + user +". "+e.toString());
			}			
		}
	}
		
	/*
	private List<Token> getTokensFromFilePaths(List<String> filePaths) {
		List<Token> tokens = new ArrayList<Token>();
		for (String filePath : filePaths)
			tokens.add(TokenBuilder.getTokenFromFile(filePath));
		return tokens;
	}
	*/

	
	private List<String> getUsers() {
		List<String> jsonTweets = MysqlPersistenceFacade.getAllJsonTweets();
		List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		HashSet<String> users = new HashSet<String>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser().getScreenName()); 
		return new ArrayList<String>(users);
	}

	
	private List<String> getAlreadyEnrichedUsers() {
		List<String> friendsEnrichedUsers = mysqlPersistenceFacade.getFriendsEnrichedUsers();
		return friendsEnrichedUsers;
	}
	
	private List<String> getUsersToEnrich() {
		List<String> allUsers = getUsers();
		List<String> alreadyEnriched = getAlreadyEnrichedUsers();
		List<String> toEnrich = new ArrayList<String>(allUsers);
		toEnrich.removeAll(alreadyEnriched);
		return toEnrich;
		
	}
	
}