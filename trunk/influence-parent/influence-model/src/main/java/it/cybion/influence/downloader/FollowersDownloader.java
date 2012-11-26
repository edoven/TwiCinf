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
 * Application to run to download users followers
 * 
 */


public class FollowersDownloader {
	
	private static final Logger logger = Logger.getLogger(FollowersDownloader.class);	
	private TwitterApiManager twitterApiManager;

	
	public static void main(String[] args) {			
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
		FollowersDownloader followersDownloader = new FollowersDownloader(twitterApiManager);
		
		followersDownloader.run();
	}
		
	public FollowersDownloader(TwitterApiManager twitterApiManager) {
		this.twitterApiManager = twitterApiManager;
	}
		
	public void run() {	
		logger.info("Getting followers to enrich.");
		List<String> usersToEnrich = getUsersToEnrichWithFollowers();
		int count = 0;
		
		logger.info(usersToEnrich.size()+" to be enriched.");
		for (int i=0; i<usersToEnrich.size(); i++) {
			String user = usersToEnrich.get(i);
			List<String> followers;
			try {
				followers = twitterApiManager.getFriends(user);
				if (followers.size()>0)
					MysqlPersistenceFacade.writeFollowers(user, followers);	
				logger.info("Successifully extracted and saved "+followers.size()+" followers for user: "+user);
				count++;
				logger.info("Extracted followers for "+count+" users.");
			} catch (TwitterException e) {
				logger.info("Problem with user:" + user);
				logger.info(e.toString());
			}			
		}
	}
	
	private static List<String> getUsersToEnrichWithFollowers() {
		List<String> allUsers = getAllUsers();
		List<String> alreadyEnriched = getAlreadyEnrichedUsers();
		List<String> toEnrich = new ArrayList<String>(allUsers);
		toEnrich.removeAll(alreadyEnriched);
		return toEnrich;
		
	}
	
	private static List<String> getAllUsers() {
		List<String> jsonTweets = MysqlPersistenceFacade.getAllJsonTweets();
		List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		HashSet<String> users = new HashSet<String>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser().getScreenName()); 
		return new ArrayList<String>(users);
	}

	
	private static List<String> getAlreadyEnrichedUsers() {
		List<String> followersEnrichedUsers = MysqlPersistenceFacade.getFollowersEnrichedUsers();
		return followersEnrichedUsers;
	}
	
}