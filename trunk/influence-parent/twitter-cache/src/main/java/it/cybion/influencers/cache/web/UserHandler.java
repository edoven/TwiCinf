package it.cybion.influencers.cache.web;

import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.*;


public class UserHandler
{

    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);
    public static final int THREE_SECONDS = 3 * 1000;
    public static final String STATUSES_USER_TIMELINE = "/statuses/user_timeline";

    private Twitter twitter;
	public Map<String, Integer> requestLimits = new HashMap<String, Integer>();
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
		updateLimitStatuses();
	}
	
	private void updateLimitStatuses()
	{
		Map<String, RateLimitStatus> limitStatusesFromTwitter;
        try
		{
            LOGGER.debug("getting limit statuses from twitter");
            limitStatusesFromTwitter = twitter.getRateLimitStatus();
            for (String requestType : limitStatusesFromTwitter.keySet())
			{
				int limit = limitStatusesFromTwitter.get(requestType).getRemaining();
				requestLimits.put(requestType, limit);
			}
		} catch (TwitterException e) {

            LOGGER.warn("twitter exception while updating ratelimits: " + e.getMessage());

            if (failedSetRequestType2LimitTries < 2) {
                LOGGER.warn("Problem with setRequestType2limit. Let's wait for 3 sec and retry.");
                try {
                    Thread.sleep(THREE_SECONDS);
                } catch (InterruptedException e1) {
                    LOGGER.warn("Problem in Thread.sleep(). Skipped. " + e.getMessage());
                }
                failedSetRequestType2LimitTries++;
                updateLimitStatuses();
            } else {
                LOGGER.info("Not updating limit statuses");
            }
        }
        failedSetRequestType2LimitTries = 0;
		lastGetRateLimitStatusTime = System.currentTimeMillis();
	}
	
	private int getRequestLimit(String requestName)
	{
		return requestLimits.get(requestName);
	}
	
	private void updateRequestLimit(String requestName, int limit)
	{
		requestLimits.put(requestName, limit);
	}
	
	
	public boolean canMakeRequest(String requestType)
	{
		int requestsLeft = getRequestLimit(requestType);
		if (requestsLeft > 0) {
			return true;
        }
		else
		{
			long now = System.currentTimeMillis();
			long secondsPassedFromLastRequest = (now-lastGetRateLimitStatusTime)/1000;
			LOGGER.debug("secondsPassedFromLastRequest=" + secondsPassedFromLastRequest);
			if (secondsPassedFromLastRequest  > 5 ) //5 = (15*60)/180
			{
				updateLimitStatuses();
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
		String requestName = STATUSES_USER_TIMELINE;
		int limit = getRequestLimit(requestName);
		LOGGER.debug("limit for getLast200TweetsPostedByUser=" + limit);
		Paging paging = new Paging();
		paging.setCount(200);
//		paging.setPage(1);
		if (maxId != -1) {
			paging.setMaxId(maxId);
        }

		List<Status> statuses;
        try {
            statuses = twitter.getUserTimeline(userId, paging);
        } catch (TwitterException e) {

            updateRequestLimit(requestName, (limit - 1));

            if (e.getStatusCode() == 401) {
                throw new ProtectedUserException();
            } else {
                throw e;
            }
        }
        updateRequestLimit(requestName, (limit - 1));

        //get raw json
        List<String> result = new ArrayList<String>();
        for (Status status : statuses) {
            result.add(DataObjectFactory.getRawJSON(status));
        }
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
