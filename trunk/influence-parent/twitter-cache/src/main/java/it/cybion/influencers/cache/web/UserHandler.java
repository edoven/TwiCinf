package it.cybion.influencers.cache.web;

import it.cybion.influencers.cache.exceptions.LimitExceededException;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.cache.web.exceptions.UserHandlerException;
import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.*;

public class UserHandler {

    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);
    public static final int THREE_SECONDS = 3 * 1000;
    public static final String STATUSES_USER_TIMELINE = "/statuses/user_timeline";

    private Twitter twitter;
    public Map<String, Integer> requestLimits = new HashMap<String, Integer>();
    private int failedSetRequestType2LimitTries = 0;
    private long lastGetRateLimitStatusTime;

    public UserHandler(Token applicationToken, Token userToken) throws TwitterException {

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

    public void updateLimitStatuses() {

        Map<String, RateLimitStatus> limitStatusesFromTwitter;
        try {
            LOGGER.debug("getting limit statuses from twitter");
            limitStatusesFromTwitter = twitter.getRateLimitStatus();
            for (String requestType : limitStatusesFromTwitter.keySet()) {
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

    private int getRequestLimit(String requestName) {

        return requestLimits.get(requestName);
    }

    private void updateRequestLimit(String requestName, int limit) {

        requestLimits.put(requestName, limit);
    }

    public boolean canMakeRequest(String requestType) {

        int requestsLeft = getRequestLimit(requestType);
        return requestsLeft > 0;
    }

    public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException {

        String requestName = "/followers/ids";
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getFollowersWithPagination=" + limit);
        IDs result = twitter.getFollowersIDs(userId, cursor);
        updateRequestLimit(requestName, (limit - 1));
        return result;
    }

    public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException {

        String requestName = "/friends/ids";
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getFriendsWithPagination=" + limit);
        IDs result = twitter.getFriendsIDs(userId, cursor);
        updateRequestLimit(requestName, (limit - 1));
        return result;
    }

    public List<String> getTweetsWithMaxId(long userId, long maxId) throws UserHandlerException {

        String requestName = STATUSES_USER_TIMELINE;
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getLast200TweetsPostedByUser=" + limit);

        if (limit <= 0) {
            throw new LimitExceededException("limit exceeded: " + limit);
        }

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

            if (e.getStatusCode() == 429) {
                final String message =
                        "got limited by twitter: " + e.getMessage() + " updating requests to 0";
                LOGGER.error(message);
                updateRequestLimit(requestName, 0);
                throw new LimitExceededException(message);
            }

            if (e.getStatusCode() == 401) {
                throw new ProtectedUserException("user protected: 401 from twitter");
            }

            throw new UserHandlerException("generic twitter exception: " + e.getMessage(), e);
        }

        updateRequestLimit(requestName, (limit - 1));

        //get raw json
        List<String> result = new ArrayList<String>();
        for (Status status : statuses) {
            result.add(DataObjectFactory.getRawJSON(status));
        }
        return result;
    }

    public String getUserJson(long userId) throws TwitterException {

        String requestName = "/users/show/:id";
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getUserJson=" + limit);
        String result = DataObjectFactory.getRawJSON(twitter.showUser(userId));
        updateRequestLimit(requestName, (limit - 1));
        return result;
    }

    public String getUserJson(String screenName) throws TwitterException {

        String requestName = "/users/show/:id";
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getUserJson=" + limit);
        String result = DataObjectFactory.getRawJSON(twitter.showUser(screenName));
        updateRequestLimit(requestName, (limit - 1));
        return result;
    }

    public List<String> getUsersJsons(long usersIds[]) throws TwitterException {

        String requestName = "/users/lookup";
        int limit = getRequestLimit(requestName);
        LOGGER.debug("limit for getUsersJsons=" + limit);
        ResponseList<User> responseList = twitter.lookupUsers(usersIds);
        updateRequestLimit(requestName, (limit - 1));
        List<String> result = new ArrayList<String>();
        Iterator<User> resultIterator = responseList.iterator();
        while (resultIterator.hasNext()) {
            User user = resultIterator.next();
            String userJson = DataObjectFactory.getRawJSON(user);
            result.add(userJson);
        }
        return result;
    }

    public long getLastGetRateLimitStatusTime() {

        return lastGetRateLimitStatusTime;
    }
}
