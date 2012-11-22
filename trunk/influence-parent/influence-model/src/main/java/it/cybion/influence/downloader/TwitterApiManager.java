package it.cybion.influence.downloader;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApiManager {
	
	private String consumerKey = "Bam9RwjprVxWJd8TdhwQOg";
	private String consumerSecret = "q1wzu5hn9HjYhEvYvPPBSIHWcfHIYJZoGnRlnD14D0";
	
	
	
	
	/*
	 * test method
	 */
	public static void main(String[] args) {
		TwitterApiManager manager = new TwitterApiManager();
		
		UserToken massimoMarini = new UserToken("962689441-yrFTbTzI3nAQ9sIMLnxLexyLWGAfZzhXCosTwuWp",
												"elPwBu9NeAoGXunIl1wyPJDsSYgLWlFQXbXR8C2KQc");
		
		UserToken giulioFerragami = new UserToken("962712157-EHMXCzn4Qu9fGj6u3hWt7h0z1ultPYo4Fyk7LQFk",
				"pGDQfoRjSAjQA4Pck8bK2aELngY3cCEwRpFtl0n57E");

		UserToken gigiPaliermo = new UserToken("962721674-JYJO42tN1WTK2nYUq23aP1rqmwMG3ooqd5Py8Yao",
				"Ys2GAfLSyKdbglzvp93AHmWnAkx0wI1YTjfpMCkN4");
		
		
		List<UserToken> userTokens = new ArrayList<UserToken>();
		userTokens.add(massimoMarini);
		userTokens.add(giulioFerragami);
		userTokens.add(gigiPaliermo);
		
		
		
		System.out.println(manager.getTotalLimit(userTokens));
	}
	
	
	
	
	
	public int getTotalLimit(List<UserToken> userTokens) {
		int limit = 0;
		for (UserToken userToken : userTokens)
			limit = limit + getLimitForUser(userToken);
		return limit;
		
	}
	

	public int getLimitForUser(UserToken userToken) {
		
		int limit = -1; //TODO: this is not good
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret)
		  .setOAuthAccessToken(userToken.getToken())
		  .setOAuthAccessTokenSecret(userToken.getSecret());
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
