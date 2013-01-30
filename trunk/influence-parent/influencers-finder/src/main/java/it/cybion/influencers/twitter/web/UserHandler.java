package it.cybion.influencers.twitter.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class UserHandler {

	private static final Logger logger = Logger.getLogger(UserHandler.class);
	
	private Twitter twitter;
	private Map<String, Integer> requestType2limit;
	private int setRequestType2LimitTries = 0;
	
	public Twitter getTwitter() {
		return twitter;
	}


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
		requestType2limit = new HashMap<String, Integer>();
		setRequestType2limit();
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		Runnable periodicTask = new Runnable() {
		    public void run() {
		    	setRequestType2limit();	
		    }
		};			
		
		executor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS);
		logger.debug(requestType2limit);

	}
	
	private void setRequestType2limit() {
		logger.debug("setRequestType2limit");
		Map<String, RateLimitStatus> requestType2limitStatus;
		try {
			requestType2limitStatus = twitter.getRateLimitStatus();
			for (String requestType : requestType2limitStatus.keySet()) {
				int limit = requestType2limitStatus.get(requestType).getRemaining();
				requestType2limit.put(requestType , limit);
			}			
		} catch (TwitterException e) {
			logger.info("Problem with setRequestType2limit.");
			if (setRequestType2LimitTries<2) {
				logger.info("Let's wait for 10 sec and retry.");
				try {
					Thread.sleep(10*1000);
				} catch (InterruptedException e1) {
					logger.info("Problem in Thread.sleep(). Skipped.");
					return;
				}
				setRequestType2LimitTries++;
				setRequestType2limit();
			}
			else
				logger.info("Skipped.");
		}
		setRequestType2LimitTries = 0;		
	}
	
	public String getUserJson(long userId) throws LimitReachedForCurrentRequestException, TwitterException {
		String requestName = "/users/show/:id";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getUserJson="+limit);
		if (limit<=0)
			throw new LimitReachedForCurrentRequestException(requestType2limit);		
		String result =  DataObjectFactory.getRawJSON(twitter.showUser(userId));
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}	
		
	public List<String> getUsersJsons(long usersIds[]) throws LimitReachedForCurrentRequestException, TwitterException, MethodInputNotCorrectException {
		if (usersIds.length > 100 || usersIds.length < 1)
			throw new MethodInputNotCorrectException("Input for method getUsersJsons is not correct. Its lenght is >100 or <1.");		
		String requestName = "/users/lookup";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getUsersJsons="+limit);
		if (limit<=0)
			throw new LimitReachedForCurrentRequestException(requestType2limit);		
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
		String requestName = "/followers/ids";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getFollowersWithPagination="+limit);
		if (limit<=0)
			throw new LimitReachedForCurrentRequestException(requestType2limit);			
		IDs result = twitter.getFollowersIDs(userId, cursor);
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}	
	
	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException, LimitReachedForCurrentRequestException {
		String requestName = "/friends/ids";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getFriendsWithPagination="+limit);
		if (limit<=0)
			throw new LimitReachedForCurrentRequestException(requestType2limit);		
		IDs result = twitter.getFriendsIDs(userId, cursor);
		requestType2limit.put(requestName, (limit-1) );
		return result;
	}
	
	public List<String> getLast200TweetsPostedByUser(long userId) throws LimitReachedForCurrentRequestException, TwitterException {
		String requestName = "/statuses/user_timeline";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getLast200TweetsPostedByUser="+limit);
		if (limit<=0)
			throw new LimitReachedForCurrentRequestException(requestType2limit);
		List<Status> statuses = twitter.getUserTimeline(userId, new Paging(1, 200));
		List<String> result = new ArrayList<String>();
		for (Status status : statuses)
			result.add( DataObjectFactory.getRawJSON(status) );
		return result;
	}
}
