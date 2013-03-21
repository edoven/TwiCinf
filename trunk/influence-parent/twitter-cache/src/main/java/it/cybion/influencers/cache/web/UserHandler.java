package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

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


public class UserHandler
{

	private static final Logger logger = Logger.getLogger(UserHandler.class);

	private Twitter twitter;
	private Map<String, Integer> requestType2limit;
	private int setRequestType2LimitTries = 0;

	public UserHandler(Token applicationToken, Token userToken) throws TwitterException
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false).setOAuthConsumerKey(applicationToken.getTokenString()).setOAuthConsumerSecret(applicationToken.getSecretString()).setOAuthAccessToken(userToken.getTokenString())
				.setOAuthAccessTokenSecret(userToken.getSecretString()).setJSONStoreEnabled(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		requestType2limit = new HashMap<String, Integer>();
		setRequestType2limit();

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		Runnable periodicTask = new Runnable()
		{
			public void run()
			{
				setRequestType2limit();
			}
		};

		executor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS);
		logger.debug(requestType2limit);
	}
	
	public boolean requestLimitReached(String requestType)
	{
		int requestsLeft = requestType2limit.get(requestType);
		return requestsLeft<1;
	}

	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException
	{
		String requestName = "/followers/ids";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getFollowersWithPagination=" + limit);
		IDs result = twitter.getFollowersIDs(userId, cursor);
		requestType2limit.put(requestName, (limit - 1));
		return result;
	}


	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException
	{
		String requestName = "/friends/ids";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getFriendsWithPagination=" + limit);
		IDs result = twitter.getFriendsIDs(userId, cursor);
		requestType2limit.put(requestName, (limit - 1));
		return result;
	}

	public List<String> getTweetsWithMaxId(long userId, long maxId) throws TwitterException, ProtectedUserException
	{
		String requestName = "/statuses/user_timeline";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getLast200TweetsPostedByUser=" + limit);
		Paging paging = new Paging();
		paging.setCount(200);
//		paging.setPage(1);
		if (maxId!=-1)
			paging.setMaxId(maxId);
		List<Status> statuses;
		try
		{
			statuses = twitter.getUserTimeline(userId, paging);
		}
		catch (TwitterException e)
		{
			if (e.getStatusCode() == 401)
			{
				throw new ProtectedUserException();
			}
			else
				throw e;
		}
		List<String> result = new ArrayList<String>();
		for (Status status : statuses)
			result.add(DataObjectFactory.getRawJSON(status));
		requestType2limit.put(requestName, (limit - 1));
		return result;
	}

	public String getUserJson(long userId) throws TwitterException
	{
		String requestName = "/users/show/:id";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getUserJson=" + limit);
		String result = DataObjectFactory.getRawJSON(twitter.showUser(userId));
		requestType2limit.put(requestName, (limit - 1));
		return result;
	}

	public String getUserJson(String screenName) throws TwitterException
	{
		String requestName = "/users/show/:id";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getUserJson=" + limit);
		String result = DataObjectFactory.getRawJSON(twitter.showUser(screenName));
		requestType2limit.put(requestName, (limit - 1));
		return result;
	}


	public List<String> getUsersJsons(long usersIds[]) throws TwitterException
	{
		String requestName = "/users/lookup";
		int limit = requestType2limit.get(requestName);
		logger.debug("limit for getUsersJsons=" + limit);
		ResponseList<User> responseList = twitter.lookupUsers(usersIds);
		requestType2limit.put(requestName, (limit - 1));
		List<String> result = new ArrayList<String>();
		Iterator<User> resultIterator = responseList.iterator();
		while (resultIterator.hasNext())
		{
			User user = resultIterator.next();
			String userJson = DataObjectFactory.getRawJSON(user);
			result.add(userJson);
		}
		return result;
	}
	
	public void setRequestType2limit()
	{
		logger.debug("setRequestType2limit");
		Map<String, RateLimitStatus> requestType2limitStatus;
		try
		{
			requestType2limitStatus = twitter.getRateLimitStatus();
			for (String requestType : requestType2limitStatus.keySet())
			{
				int limit = requestType2limitStatus.get(requestType).getRemaining();
				requestType2limit.put(requestType, limit);
			}
		} 
		catch (TwitterException e)
		{
			if (setRequestType2LimitTries < 2)
			{
				logger.info("Problem with setRequestType2limit. Let's wait for 3 sec and retry.");
				try
				{
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e1)
				{
					logger.info("Problem in Thread.sleep(). Skipped.");
					return;
				}
				setRequestType2LimitTries++;
				setRequestType2limit();
			} else
				logger.info("Skipped.");
		}
		setRequestType2LimitTries = 0;
	}
}
