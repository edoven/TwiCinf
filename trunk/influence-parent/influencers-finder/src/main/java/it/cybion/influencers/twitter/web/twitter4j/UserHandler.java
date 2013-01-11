package it.cybion.influencers.twitter.web.twitter4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class UserHandler {

	private static final Logger logger = Logger.getLogger(Twitter4jFacade.class);
	
	private Twitter twitter;
	
	public Twitter getTwitter() {
		return twitter;
	}

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
		String result =  DataObjectFactory.getRawJSON(twitter.showUser(userId));
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}	
		
	public List<String> getUsersJsons(long usersIds[]) throws LimitReachedForCurrentRequestException, TwitterException, MethodInputNotCorrectException {
		if (usersIds.length > 100 || usersIds.length < 1)
			throw new MethodInputNotCorrectException("Input for method getUsersJsons is not correct. Its lenght is >100 or <1.");		
		String requestName = "/users/lookup";
		int limit = requestType2limit.get(requestName);
		logger.info("limit for getUsersJsons="+limit);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();		
		ResponseList<User> responseList = twitter.lookupUsers(usersIds);
		requestType2limit.put(requestName, (limit-1) );
		List<String> result = new ArrayList<String>();
		Iterator<User> resultIterator = responseList.iterator();
		while (resultIterator.hasNext()) {
			User user = resultIterator.next();
			String userJson = DataObjectFactory.getRawJSON(user);
			result.add(userJson);
		}
		return result;
	}	
	
	
	
	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException, LimitReachedForCurrentRequestException {
		String requestName = "/friends/ids";
		int limit = requestType2limit.get(requestName);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();			
		IDs result = twitter.getFollowersIDs(userId, cursor);
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}	
	
	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException, LimitReachedForCurrentRequestException {
		String requestName = "/followers/ids";
		int limit = requestType2limit.get(requestName);
		if (limit==0)
			throw new LimitReachedForCurrentRequestException();		
		IDs result = twitter.getFriendsIDs(userId, cursor);
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}
}
