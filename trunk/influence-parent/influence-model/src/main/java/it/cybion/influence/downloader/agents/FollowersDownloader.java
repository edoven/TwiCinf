package it.cybion.influence.downloader.agents;

import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.downloader.Token;
import it.cybion.influence.downloader.TwitterApiManager;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.util.DatasetJsonDeserializer;
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
	private MysqlPersistenceFacade mysqlPersistenceFacade;

	
	public static void main(String[] args) {			
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
		MysqlPersistenceFacade mysqlPersistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
		FollowersDownloader followersDownloader = new FollowersDownloader(twitterApiManager,mysqlPersistenceFacade);
		
		followersDownloader.run();
	}
		
	public FollowersDownloader(TwitterApiManager twitterApiManager, MysqlPersistenceFacade mysqlPersistenceFacade) {
		this.twitterApiManager = twitterApiManager;
		this.mysqlPersistenceFacade = mysqlPersistenceFacade;
	}
		
	public void run() {	
		logger.info("Getting users to followers-enrich.");
		List<String> usersToEnrich = getUsersToEnrichWithFollowers();
		int count = 0;
		
		logger.info(usersToEnrich.size()+" to be enriched.");
		for (int i=0; i<usersToEnrich.size(); i++) {
			String user = usersToEnrich.get(i);
			List<String> followers;
			try {
				followers = twitterApiManager.getUpTo5000FollowersIds(user);
				if (followers.size()>0)
					mysqlPersistenceFacade.writeFollowers(user, followers);	
				count++;
				logger.info("Successifully extracted and saved "+followers.size()+" followers for user: "+user+". Extracted followers for "+count+" users.");
				
			} catch (TwitterException e) {
				logger.info("Problem with user:" + user +". "+e.toString());
			}			
		}
	}
	
	private List<String> getUsersToEnrichWithFollowers() {
		List<String> allUsers = getAllUsers();
		List<String> alreadyEnriched = getAlreadyEnrichedUsers();
		List<String> toEnrich = new ArrayList<String>(allUsers);
		toEnrich.removeAll(alreadyEnriched);
		return toEnrich;
		
	}
	
	private List<String> getAllUsers() {
		List<String> jsonTweets = mysqlPersistenceFacade.getAllJsonTweets();
		List<Tweet> tweets = new DatasetJsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		HashSet<String> users = new HashSet<String>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser().getScreenName()); 
		return new ArrayList<String>(users);
	}

	
	private List<String> getAlreadyEnrichedUsers() {
		List<String> followersEnrichedUsers = mysqlPersistenceFacade.getFollowersEnrichedUsers();
		return followersEnrichedUsers;
	}
	
}