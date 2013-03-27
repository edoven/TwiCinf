package it.cybion.influencers.cache.web.implementations.twitter4j;


import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.web.TwitterWebFacade;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.IDs;
import twitter4j.TwitterException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Twitter4jWebFacade implements TwitterWebFacade
{
	private class ResultContainer
	{		
		private List<String> goodTweets;
		private List<String> badTweets;
		
		public ResultContainer(
				List<String> goodTweets, List<String> badTweets)
		{
			this.goodTweets = goodTweets;
			this.badTweets = badTweets;
		}
		
		public List<String> getGoodTweets() {return goodTweets;}
		public List<String> getBadTweets() {return badTweets;}
	}

	private final Logger logger = Logger.getLogger(Twitter4jWebFacade.class);
	
	private final int WAIT_TIME = 1;
	private List<UserHandler> userHandlers; 	
	
	
	public Twitter4jWebFacade(Token consumerToken, List<Token> userTokens)
	{
		userHandlers = new ArrayList<UserHandler>();
		for (Token userToken : userTokens)
		{
			logger.info("Creating UserHandler");
			for (int i = 0; i < 3; i++)
			{ // 3 tries
				try
				{
					UserHandler userHandler = new UserHandler(consumerToken, userToken);
					userHandlers.add(userHandler);
					break;
				} catch (TwitterException e)
				{
					logger.info("Can't create UserHandler for token = " + userToken + ". Let's wait 1 min and then retry.");
					try
					{
						Thread.sleep(60 * 1000);
					} catch (InterruptedException e1)
					{
						logger.info("Problem in Thread.sleep");
					}
				}			
				logger.info("Can't create UserHandler for token = " + userToken + ". Skipped.");
			}
		}
		logger.info("UserHandlers created");
	}
	
	private ResultContainer filterTweetsByDate(List<Tweet> tweets,Date fromDate, Date toDate)	
	{
		List<String> goodTweets = new ArrayList<String>();
		List<String> badTweets = new ArrayList<String>();		
		for (Tweet tweet: tweets)
		{
			Date tweetDate = tweet.getCreatedAt();
			if (tweetDate.compareTo(fromDate)>=0 && tweetDate.compareTo(toDate)<0)
				goodTweets.add(tweet.getOriginalJson());				
			else
				badTweets.add(tweet.getOriginalJson());
		}
		return new ResultContainer(goodTweets,badTweets);		
	}
	
	private long[] getChunk(List<Long> list, int chunkSize, int chunkIndex)
	{
		int firstElementIndex = chunkIndex * chunkSize;
		int lastElementIndex = chunkIndex * chunkSize + chunkSize;
		if (lastElementIndex > list.size())
		{
			lastElementIndex = list.size();
			chunkSize = lastElementIndex - firstElementIndex;
		}
		List<Long> chunkList = list.subList(firstElementIndex, lastElementIndex);
		long chunkArray[] = new long[chunkList.size()];
		for (int i = 0; i < chunkSize; i++)
			chunkArray[i] = chunkList.get(i);
		return chunkArray;
	}

	@Override
	public List<Long> getFollowersIds(long userId) throws TwitterException
	{
		
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		while (cursor != 0)
		{
			IDs idsContainter = getFollowersIdsWithPagination(userId, cursor);
			for (Long id : idsContainter.getIDs())
				ids.add(id);
			cursor = idsContainter.getNextCursor();
		}
		return ids;
	}

	private IDs getFollowersIdsWithPagination(long userId, long cursor) throws TwitterException
	{
		UserHandler userHandler = getUserHandler("/followers/ids");
		return userHandler.getFollowersWithPagination(userId, cursor);
	}

	@Override
	public List<Long> getFriendsIds(long userId) throws TwitterException
	{
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		while (cursor != 0)
		{
			IDs idsContainter = getFriendsIdsWithPagination(userId, cursor);
			for (Long id : idsContainter.getIDs())
				ids.add(id);
			cursor = idsContainter.getNextCursor();
		}
		return ids;
	}

	private IDs getFriendsIdsWithPagination(long userId, long cursor) throws TwitterException
	{
		UserHandler userHandler = getUserHandler("/friends/ids");
		return (IDs) userHandler.getFriendsWithPagination(userId, cursor);
	}

	@Override
	public SearchedByDateTweetsResultContainer getTweetsByDate(
											long userId, Date fromDate, Date toDate
											) throws TwitterException, ProtectedUserException
	{
		List<String> tweetsJsons = getTweetsWithMaxId(userId, -1);
		if (tweetsJsons.size()<1)
			return new SearchedByDateTweetsResultContainer(Collections.<String>emptyList(), Collections.<String>emptyList());
		List<Tweet> tweets = getTweetsFromJsons(tweetsJsons);				
		List<String> goodTweets = new ArrayList<String>();
		List<String> badTweets = new ArrayList<String>();
		ResultContainer resultContainer = filterTweetsByDate(tweets, fromDate, toDate);	
		goodTweets.addAll(resultContainer.getGoodTweets());
		badTweets.addAll(resultContainer.getBadTweets());
		Tweet oldestTweet = getOldestTweet(tweets);
		while (fromDate.compareTo( oldestTweet.getCreatedAt() ) <= 0 )
		{
			long oldestTweetId = oldestTweet.getId();
			tweetsJsons = getTweetsWithMaxId(userId,oldestTweetId);
			if (tweetsJsons.size()<1)
				return new SearchedByDateTweetsResultContainer(goodTweets, badTweets);
			tweets = getTweetsFromJsons(tweetsJsons);
			resultContainer = filterTweetsByDate(tweets, fromDate, toDate);	
			goodTweets.addAll(resultContainer.goodTweets);
			badTweets.addAll(resultContainer.badTweets);
			oldestTweet = getOldestTweet(tweets);
		}		
		return new SearchedByDateTweetsResultContainer(goodTweets, badTweets);
	}

	private Tweet getOldestTweet(List<Tweet> tweets)
	{
		Collections.sort(tweets);
		return tweets.get(0);
	}

	private List<Tweet> getTweetsFromJsons(List<String> tweetsJsons)
	{
		List<Tweet> tweets = new ArrayList<Tweet>();
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
					.setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
		Tweet tweet;
		for (String tweetjson : tweetsJsons)
		{
			tweet = gson.fromJson(tweetjson, Tweet.class);
			tweet.setOriginalJson(tweetjson);
			tweets.add(tweet);
		}
		return tweets;
	}
	
	@Override
	public List<String> getTweetsWithMaxId(long userId, long maxId) throws TwitterException, ProtectedUserException 
	{
		UserHandler userHandler = getUserHandler("/statuses/user_timeline");
//		logger.info("Downloading 200 tweets for user with id:" + userId + " with maxid="+maxId);
		List<String> tweets = userHandler.getTweetsWithMaxId(userId, maxId);
		return tweets;
	}

	private List<String> getUpTo100Users(long[] usersIds) throws TwitterException
	{
		UserHandler userHandler = getUserHandler("/users/lookup");
		return (List<String>) userHandler.getUsersJsons(usersIds);
	}
	
	public UserHandler getUserHandler(String requestName)
	{
		for (UserHandler userHandler : userHandlers)
		{
			if (!userHandler.requestLimitReached(requestName))
				return userHandler;
		}
		try
		{
			logger.info("All handlers have reached the limit, let's wait for " + WAIT_TIME + " min");
			Thread.sleep(WAIT_TIME * 60 * 1000);
			return getUserHandler(requestName);
		} catch (InterruptedException e1)
		{
			logger.info("Problem in Thread.sleep().");
			System.exit(0);
			return null;
		}
	}
	
	@Override
	public String getUserJson(long userId) throws TwitterException
	{
		UserHandler userHandler = getUserHandler("/users/show/:id");
		return userHandler.getUserJson(userId);
	}
		
	@Override
	public String getUserJson(String screenName) throws TwitterException
	{
		UserHandler userHandler = getUserHandler("/users/show/:id");
		return userHandler.getUserJson(screenName);
	}
	
	@Override
	public List<String> getUsersJsons(List<Long> usersIds) throws TwitterException
	{
		logger.info("downloading " + usersIds.size() + " users profiles");
		List<String> usersJsons = new ArrayList<String>();
		int listSize = usersIds.size();
		// logger.info("listSize="+listSize);
		int chunkSize = 100;
		int remainder = (listSize % chunkSize);
		int chunksCount = listSize / chunkSize;
		if (remainder > 0)
			chunksCount++;
		for (int i = 0; i < chunksCount; i++)
		{
			long[] chunk = getChunk(usersIds, 100, i);
			logger.info("downloading chunk " + i + "/" + chunksCount);
			List<String> chunkResult;
			try
			{
				chunkResult = getUpTo100Users(chunk);			
				usersJsons.addAll(chunkResult);
			}
			catch (TwitterException e)
			{
				logger.info("ERROR: problem with chuck, skipped!");
			}
			
		}
		return usersJsons;
	}

}
