package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

	private Twitter twitter;
	public Map<String, Integer> requestType2limit = new HashMap<String, Integer>();
	private int failedSetRequestType2LimitTries = 0;
	private long lastGetRateLimitStatusTime;

	public UserHandler(Token applicationToken, Token userToken) throws TwitterException
	{
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(false)
							.setOAuthConsumerKey(applicationToken.getTokenString())
							.setOAuthConsumerSecret(applicationToken.getSecretString())
							.setOAuthAccessToken(userToken.getTokenString())
							.setOAuthAccessTokenSecret(userToken.getSecretString())
							.setJSONStoreEnabled(true);
		TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
		twitter = twitterFactory.getInstance();
		setRequestType2limit();
	}
	
	private void setRequestType2limit()
	{
		LOGGER.debug("setRequestType2limit");
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
			if (failedSetRequestType2LimitTries < 2)
			{
				LOGGER.info("Problem with setRequestType2limit. Let's wait for 3 sec and retry.");
				try
				{
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e1)
				{
					LOGGER.info("Problem in Thread.sleep(). Skipped.");
				}
				failedSetRequestType2LimitTries++;
				setRequestType2limit();
			} else
				LOGGER.info("Skipped.");
		}
		failedSetRequestType2LimitTries = 0;
		lastGetRateLimitStatusTime = System.currentTimeMillis();
	}
	
	private int getRequestLimit(String requestName)
	{
		return requestType2limit.get(requestName);
	}
	
	private void updateRequestLimit(String requestName, int limit)
	{
		requestType2limit.put(requestName, limit);
	}
	
	
	public boolean canMakeRequest(String requestType)
	{
		int requestsLeft = getRequestLimit(requestType);
		if (requestsLeft>0)
			return true;
		else
		{
			long now = System.currentTimeMillis();
			long secondsPassedFromLastRequest = (now-lastGetRateLimitStatusTime)/1000;
			LOGGER.debug("secondsPassedFromLastRequest=" + secondsPassedFromLastRequest);
			if (secondsPassedFromLastRequest  > 5 ) //5 = (15*60)/180
			{
				setRequestType2limit();
				return canMakeRequest(requestType);
			}
			else //it's too early to ask the limits again
				return false;
		}
	}
	

	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException
	{
		String requestName = "/followers/ids";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getFollowersWithPagination=" + limit);
		IDs result = twitter.getFollowersIDs(userId, cursor);
		updateRequestLimit(requestName, (limit - 1));
		return result;
	}


	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException
	{
		String requestName = "/friends/ids";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getFriendsWithPagination=" + limit);
		IDs result = twitter.getFriendsIDs(userId, cursor);
		updateRequestLimit(requestName, (limit - 1));
		return result;
	}

	public List<String> getTweetsWithMaxId(long userId, long maxId) throws TwitterException, ProtectedUserException
	{
		String requestName = "/statuses/user_timeline";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getLast200TweetsPostedByUser=" + limit);
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
		updateRequestLimit(requestName, (limit - 1));
		return result;
	}

	public String getUserJson(long userId) throws TwitterException
	{
		String requestName = "/users/show/:id";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getUserJson=" + limit);
		String result = DataObjectFactory.getRawJSON(twitter.showUser(userId));
		updateRequestLimit(requestName, (limit - 1));
		return result;
	}

	public String getUserJson(String screenName) throws TwitterException
	{
		String requestName = "/users/show/:id";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getUserJson=" + limit);
		String result = DataObjectFactory.getRawJSON(twitter.showUser(screenName));
		updateRequestLimit(requestName, (limit - 1));
		return result;
	}


	public List<String> getUsersJsons(long usersIds[]) throws TwitterException
	{
		String requestName = "/users/lookup";
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getUsersJsons=" + limit);
		ResponseList<User> responseList = twitter.lookupUsers(usersIds);
		updateRequestLimit(requestName, (limit - 1));
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
}
