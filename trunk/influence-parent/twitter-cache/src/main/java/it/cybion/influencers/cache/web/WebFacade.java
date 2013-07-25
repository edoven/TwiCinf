package it.cybion.influencers.cache.web;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influencers.cache.exceptions.LimitExceededException;
import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.cache.web.exceptions.UserHandlerException;
import it.cybion.influencers.cache.web.exceptions.WebFacadeException;
import org.apache.log4j.Logger;
import twitter4j.IDs;
import twitter4j.TwitterException;

import java.util.*;

public class WebFacade {

    private static final Logger LOGGER = Logger.getLogger(WebFacade.class);
    public static final int ONE_MINUTE = 60 * 1000;

    private static WebFacade singletonInstance = null;

    private final int ONE = 1;
    private List<UserHandler> userHandlers;

    private class ResultContainer {

        private List<String> goodTweets;
        private List<String> badTweets;

        public ResultContainer(List<String> goodTweets, List<String> badTweets) {

            this.goodTweets = goodTweets;
            this.badTweets = badTweets;
        }

        public List<String> getTweetsInRange() {

            return goodTweets;
        }

        public List<String> getTweetsOutsideRange() {

            return badTweets;
        }
    }

    public static WebFacade getInstance(Token consumerToken, List<Token> userTokens) {

        if (singletonInstance == null) {
            singletonInstance = new WebFacade(consumerToken, userTokens);
        }
        return singletonInstance;
    }

    //TODO pass userHandlers in constructor
    private WebFacade(Token consumerToken, List<Token> userTokens) {

        userHandlers = new ArrayList<UserHandler>();
        for (Token userToken : userTokens) {
            LOGGER.info("Creating UserHandler");
            // 3 tries
            int maxRetries = 3;

            for (int i = 0; i < maxRetries; i++) {
                try {
                    UserHandler userHandler = new UserHandler(consumerToken, userToken);
                    userHandlers.add(userHandler);
                    break;
                } catch (TwitterException e) {
                    LOGGER.info("Can't create UserHandler for token = " + userToken +
                                ". Let's wait 1 min and then retry.: " + e.getMessage());
                    try {
                        Thread.sleep(ONE_MINUTE);
                    } catch (InterruptedException e1) {
                        LOGGER.warn("Problem in Thread.sleep: " + e1.getMessage());
                    }
                }
                LOGGER.warn("Can't create UserHandler for token = " + userToken + ". Skipped.");
            }
        }
        LOGGER.info("UserHandlers created");
    }

    //returns null if all handlers are not available
    public UserHandler getUserHandlerForRequest(final String requestName) {

        UserHandler availableForRequest = null;

        Iterator<UserHandler> userHandlerIterator = this.userHandlers.iterator();

        while (availableForRequest == null && userHandlerIterator.hasNext()) {

            final UserHandler currentHandler = userHandlerIterator.next();

            //update user handler limit status
            long now = System.currentTimeMillis();
            long secondsPassedFromLastRequest =
                    (now - currentHandler.getLastGetRateLimitStatusTime()) / 1000;
            LOGGER.debug("secondsPassedFromLastRequest=" + secondsPassedFromLastRequest);

            if (secondsPassedFromLastRequest > 5) //5 = (15*60)/180
            {
                currentHandler.updateLimitStatuses();
            }

            boolean handlerCanMakeRequest = currentHandler.canMakeRequest(requestName);

            if (handlerCanMakeRequest) {
                LOGGER.debug("userHandler.requestType2limit.get(" + requestName + ")=" +
                             currentHandler.requestLimits.get(requestName));
                availableForRequest = currentHandler;
            }
        }

        return availableForRequest;
    }

    private ResultContainer filterTweetsByDate(List<Tweet> tweets, Date fromDate, Date toDate) {

        List<String> includedInRange = new ArrayList<String>();
        List<String> outsideRange = new ArrayList<String>();
        for (Tweet tweet : tweets) {
            Date tweetDate = tweet.getCreatedAt();
            if (tweetDate.compareTo(fromDate) >= 0 && tweetDate.compareTo(toDate) <= 0) {
                includedInRange.add(tweet.getOriginalJson());
            } else {
                outsideRange.add(tweet.getOriginalJson());
            }
        }
        return new ResultContainer(includedInRange, outsideRange);
    }

    private long[] getChunk(List<Long> list, int chunkSize, int chunkIndex) {

        int firstElementIndex = chunkIndex * chunkSize;
        int lastElementIndex = chunkIndex * chunkSize + chunkSize;
        if (lastElementIndex > list.size()) {
            lastElementIndex = list.size();
            chunkSize = lastElementIndex - firstElementIndex;
        }
        List<Long> chunkList = list.subList(firstElementIndex, lastElementIndex);
        long chunkArray[] = new long[chunkList.size()];
        for (int i = 0; i < chunkSize; i++)
            chunkArray[i] = chunkList.get(i);
        return chunkArray;
    }

    public List<Long> getFollowersIds(long userId) throws TwitterException {

        long cursor = -1;
        List<Long> ids = new ArrayList<Long>();
        while (cursor != 0) {
            IDs idsContainter = getFollowersIdsWithPagination(userId, cursor);
            for (Long id : idsContainter.getIDs())
                ids.add(id);
            cursor = idsContainter.getNextCursor();
        }
        return ids;
    }

    private IDs getFollowersIdsWithPagination(long userId, long cursor) throws TwitterException {

        UserHandler userHandler = getUserHandlerForRequest("/followers/ids");
        return userHandler.getFollowersWithPagination(userId, cursor);
    }

    public List<Long> getFriendsIds(long userId) throws TwitterException {

        long cursor = -1;
        List<Long> ids = new ArrayList<Long>();
        while (cursor != 0) {
            IDs idsContainter = getFriendsIdsWithPagination(userId, cursor);
            for (Long id : idsContainter.getIDs())
                ids.add(id);
            cursor = idsContainter.getNextCursor();
        }
        return ids;
    }

    private IDs getFriendsIdsWithPagination(long userId, long cursor) throws TwitterException {

        UserHandler userHandler = getUserHandlerForRequest("/friends/ids");
        return (IDs) userHandler.getFriendsWithPagination(userId, cursor);
    }

    public SearchedByDateTweetsResultContainer getTweetsByDate(long userId, Date fromDate,
                                                               Date toDate)
            throws TwitterException, ProtectedUserException {

        List<String> tweetsJsons = getTweetsWithMaxId(userId, -1);

        if (tweetsJsons.size() < 1) {
            return new SearchedByDateTweetsResultContainer(Collections.<String>emptyList(),
                    Collections.<String>emptyList());
        }

        List<Tweet> tweetsInPage = parseToTweets(tweetsJsons);
        List<String> inRangeTweets = new ArrayList<String>();
        List<String> outsideRangeTweets = new ArrayList<String>();
        ResultContainer resultContainer = filterTweetsByDate(tweetsInPage, fromDate, toDate);
        inRangeTweets.addAll(resultContainer.getTweetsInRange());
        outsideRangeTweets.addAll(resultContainer.getTweetsOutsideRange());
        Collections.sort(tweetsInPage);
        Tweet oldestTweet = tweetsInPage.get(0);
        while (fromDate.compareTo(oldestTweet.getCreatedAt()) <= 0) {
            long oldestTweetId = oldestTweet.getId();
            tweetsJsons = getTweetsWithMaxId(userId, oldestTweetId);
            if (tweetsJsons.size() <= 1) {
                return new SearchedByDateTweetsResultContainer(inRangeTweets, outsideRangeTweets);
            }
            tweetsInPage = parseToTweets(tweetsJsons);
            resultContainer = filterTweetsByDate(tweetsInPage, fromDate, toDate);
            inRangeTweets.addAll(resultContainer.getTweetsInRange());
            outsideRangeTweets.addAll(resultContainer.getTweetsOutsideRange());
            Collections.sort(tweetsInPage);
            oldestTweet = tweetsInPage.get(0);
        }
        return new SearchedByDateTweetsResultContainer(inRangeTweets, outsideRangeTweets);
    }

    private List<Tweet> parseToTweets(List<String> tweetsJsons) {

        List<Tweet> tweets = new ArrayList<Tweet>();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                // "Wed Oct 17 19:59:40 +0000 2012"
                .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
        Tweet tweet;
        for (String tweetjson : tweetsJsons) {
            tweet = gson.fromJson(tweetjson, Tweet.class);
            tweet.setOriginalJson(tweetjson);
            tweets.add(tweet);
        }
        return tweets;
    }

    //returns null list of strings if user is protected, zero sized list if the page is empty
    public List<String> getTweetsWithMaxId(long userId, long maxId)
            throws TwitterException, ProtectedUserException {

        final String statusesUserTimeline = UserHandler.STATUSES_USER_TIMELINE;

        List<String> tweets = null;

        boolean userProtected = false;

        while (tweets == null && !userProtected) {
            final UserHandler userHandler = acquireUserHandler(statusesUserTimeline);
            LOGGER.info(
                    "Downloading 200 tweets for user with id:" + userId + " with maxid=" + maxId);
            try {
                tweets = userHandler.getTweetsWithMaxId(userId, maxId);
            } catch (UserHandlerException e) {
                LOGGER.warn(
                        "couldn't get tweets for: " + userId + " maxId " + maxId + e.getMessage());
                if (e instanceof ProtectedUserException) {
                    userProtected = true;
                }

                if (e instanceof LimitExceededException) {
                    LOGGER.warn("limit exceeded: " + e.getMessage());
                }
            }
        }

        return tweets;
    }

    private UserHandler acquireUserHandler(String requestName) {

        UserHandler userHandler = null;

        long requestTimestamp = System.currentTimeMillis();

        while (userHandler == null) {
            userHandler = getUserHandlerForRequest(requestName);
            if (userHandler == null) {
                LOGGER.info("All handlers have reached the limit, let's wait for " +
                            ONE + " min");
                LOGGER.info("still trying request arrived at " + new Date(
                        requestTimestamp).toString());

                try {
                    Thread.sleep(ONE_MINUTE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } //while

        return userHandler;
    }

    private List<String> getUpTo100Users(long[] usersIds) throws TwitterException {

        UserHandler userHandler = getUserHandlerForRequest("/users/lookup");
        return (List<String>) userHandler.getUsersJsons(usersIds);
    }

    public String getUserJson(long userId) throws TwitterException {

        UserHandler userHandler = getUserHandlerForRequest("/users/show/:id");
        return userHandler.getUserJson(userId);
    }

    public String getUserJson(String screenName) throws TwitterException {

        UserHandler userHandler = getUserHandlerForRequest("/users/show/:id");
        return userHandler.getUserJson(screenName);
    }

    public List<String> getUsersJsons(List<Long> usersIds) throws WebFacadeException {

        LOGGER.info("downloading " + usersIds.size() + " users profiles");
        List<String> usersJsons = new ArrayList<String>();
        int listSize = usersIds.size();
        // logger.info("listSize="+listSize);
        int chunkSize = 100;
        int remainder = (listSize % chunkSize);
        int chunksCount = listSize / chunkSize;
        if (remainder > 0) {
            chunksCount++;
        }
        for (int i = 0; i < chunksCount; i++) {
            long[] chunk = getChunk(usersIds, 100, i);
            LOGGER.info("downloading chunk " + i + "/" + chunksCount);
            LOGGER.info("chunk contents: '" + chunk + "'");
            List<String> chunkResult;
            try {
                chunkResult = getUpTo100Users(chunk);
                usersJsons.addAll(chunkResult);
            } catch (TwitterException e) {
                String message =
                        "problem with chunk while calling twitter, skipped!" + e.getMessage();
                LOGGER.warn(message);
                throw new WebFacadeException(message);
            }
        }
        return usersJsons;
    }

}
