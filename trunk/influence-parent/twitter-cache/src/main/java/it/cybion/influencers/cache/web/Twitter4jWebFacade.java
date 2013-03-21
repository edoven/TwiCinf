package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.model.Tweet;
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
		boolean isFinished;
		long oldestId;
		List<String> goodTweets;
		List<String> badTweets;
		public ResultContainer(boolean isFinished, long oldestId,
				List<String> goodTweets, List<String> badTweets)
		{
			super();
			this.isFinished = isFinished;
			this.oldestId = oldestId;
			this.goodTweets = goodTweets;
			this.badTweets = badTweets;
		}
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
	
	private ResultContainer filterTweetsByDate(List<Tweet> tweets,Date startDate, Date endDate)	
	{
		Collections.sort(tweets);
		Tweet minIdTweet = tweets.get(0);
		long oldestId = minIdTweet.id;
		Tweet maxIdTweet = tweets.get(tweets.size()-1);	
		Date maxDate200 = maxIdTweet.created_at;
		List<String> goodTweets = new ArrayList<String>();
		List<String> badTweets = new ArrayList<String>();		
		for (Tweet tweet: tweets)
		{
			if (isGreater(tweet.created_at,startDate) && isGreater(endDate, tweet.created_at))
				goodTweets.add(tweet.originalJson);	
			else
				badTweets.add(tweet.originalJson);
		}
		boolean isFinished = false;
		if (maxDate200.compareTo(startDate)<0)
			isFinished = true;
		if (tweets.size()<2)
			isFinished = true;
		return new ResultContainer(isFinished,oldestId,goodTweets,badTweets);		
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
											long userId, 
											int fromYear, int fromMonth, int fromDay, 
											int toYear,	  int toMonth,   int toDay) throws TwitterException, ProtectedUserException
	{
		
		Date fromDate = new Date(fromYear-1900, fromMonth-1, fromDay);
		Date toDate = new Date(toYear-1900, toMonth-1, toDay);
		if (toDate.compareTo(fromDate)<0)
		{
			logger.info("ERROR! endDate can't be smaller than startDate.");
			System.exit(0);
		}
		List<String> tweetsJsons = getTweetsWithMaxId(userId, -1);
		List<Tweet> tweets = getTweetsFromJsons(tweetsJsons);				
		List<String> goodTweets = new ArrayList<String>();
		List<String> badTweets = new ArrayList<String>();
		ResultContainer resultContainer = filterTweetsByDate(tweets, fromDate, toDate);	
		goodTweets.addAll(resultContainer.goodTweets);
		badTweets.addAll(resultContainer.badTweets);
		if (resultContainer.isFinished==false)
			while (!resultContainer.isFinished)
			{
				long oldestId = resultContainer.oldestId;
				tweetsJsons = getTweetsWithMaxId(userId,oldestId);
				tweets = getTweetsFromJsons(tweetsJsons);
				resultContainer = filterTweetsByDate(tweets, fromDate, toDate);	
				goodTweets.addAll(resultContainer.goodTweets);
				badTweets.addAll(resultContainer.badTweets);
			}		
		return new SearchedByDateTweetsResultContainer(goodTweets, badTweets);
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
		logger.info("Downloading 200 tweets for user with id:" + userId + " with maxid="+maxId);
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
			List<String> chunkResult = getUpTo100Users(chunk);
			usersJsons.addAll(chunkResult);
		}
		return usersJsons;
	}

	private boolean isGreater(Date a, Date b)
	{
		if (a.compareTo(b)>0)
			return true;
		else
			return false;
	}

}
