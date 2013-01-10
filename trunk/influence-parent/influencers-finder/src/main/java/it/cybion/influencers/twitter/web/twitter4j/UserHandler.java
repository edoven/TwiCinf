package it.cybion.influencers.twitter.web.twitter4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class UserHandler {

	private static final Logger logger = Logger.getLogger(Twitter4jFacade.class);
	
	private Twitter twitter;
	private Map<String, Integer> requestType2limit = new HashMap<String, Integer>();

	public UserHandler(Token applicationToken, Token userToken) throws TwitterException {	
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		  .setOAuthConsumerKey(applicationToken.getTokenString())
		  .setOAuthConsumerSecret(applicationToken.getSecretString())
		  .setOAuthAccessToken(userToken.getTokenString())
		  .setOAuthAccessTokenSecret(userToken.getSecretString())
		  .setJSONStoreEnabled(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		
		Map<String, RateLimitStatus> requestType2limitStatus = twitter.getRateLimitStatus();
		for (String requestType : requestType2limitStatus.keySet()) {
			int limit = requestType2limitStatus.get(requestType).getRemaining();
			requestType2limit.put(requestType , limit);
		}
		logger.info("Using token of user "+twitter.getScreenName());
	}
	
	public String getUserJson(long userId) throws LimitReachedForCurrentRequestException, TwitterException {
		String requestName = "/users/show/:id";
		int limit = requestType2limit.get(requestName);
		logger.info("limit for getUserJson="+limit);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();
		requestType2limit.put(requestName, (limit-1) );
		return DataObjectFactory.getRawJSON(twitter.showUser(userId));

	}	
		
	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException, LimitReachedForCurrentRequestException {
		String requestName = "/friends/ids";
		int limit = requestType2limit.get(requestName);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();	
		requestType2limit.put(requestName, (limit-1) );
		return twitter.getFollowersIDs(userId, cursor);
	}	
	
	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException, LimitReachedForCurrentRequestException {
		String requestName = "/followers/ids";
		int limit = requestType2limit.get(requestName);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();
		requestType2limit.put(requestName, (limit-1) );
		return twitter.getFriendsIDs(userId, cursor);
	}
}
