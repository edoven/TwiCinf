package it.cybion.influence.downloader;

import it.cybion.influence.util.TokenBuilder;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApiManager {
	
	
	private List<Token> userTokens = new ArrayList<Token>();
	private Token consumerToken;
	
	/*
	 * test method
	 */
	/*
	public static void main(String[] args) {
		TwitterApiManager manager = new TwitterApiManager("/home/godzy/tokens/consumerToken.txt");
				
		manager.addUserToken("/home/godzy/tokens/token1.txt");
		manager.addUserToken("/home/godzy/tokens/token2.txt");
		manager.addUserToken("/home/godzy/tokens/token3.txt");	

		System.out.println(manager.getTotalLimit());
	}
	*/
	
	public TwitterApiManager(String consumerTokenFilePath) {
		Token consumerToken = TokenBuilder.getTokenFromFile(consumerTokenFilePath);
		this.consumerToken = consumerToken;
		
	}
	
	public void addUserToken(String filePath) {
		Token userToken = TokenBuilder.getTokenFromFile(filePath);
		userTokens.add(userToken);
	}
		
	public int getTotalLimit() {
		int limit = 0;
		for (Token userToken : userTokens)
			limit = limit + getLimitForUser(userToken);
		return limit;		
	}
	
	private int getLimitForUser(Token userToken) {
		
		int limit = -1; //TODO: this is not good
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerToken.getTokenString())
		  .setOAuthConsumerSecret(consumerToken.getSecretString())
		  .setOAuthAccessToken(userToken.getTokenString())
		  .setOAuthAccessTokenSecret(userToken.getSecretString());
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		try {
			limit = twitter.getRateLimitStatus().getRemainingHits();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return limit;
	}

}
