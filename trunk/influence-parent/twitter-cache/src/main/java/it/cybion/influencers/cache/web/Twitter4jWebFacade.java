package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.web.exceptions.MethodInputNotCorrectException;
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
	private final Logger logger = Logger.getLogger(Twitter4jWebFacade.class);

	private UserHandlersManager userHandlersManager;
	
	
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
	

	public Twitter4jWebFacade(Token consumerToken, List<Token> userTokens)
	{
		this.userHandlersManager = new UserHandlersManager(consumerToken, userTokens);
	}

	private ResultContainer filterTweetsByDate(List<Tweet> tweets,Date startDate, Date endDate)	
	{
		Collections.sort(tweets);
		Tweet minIdTweet = tweets.get(0);
		Date minDate200 = minIdTweet.created_at;
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
		long oldestId = minIdTweet.id;
		
		/*
		 * 
		 * caso 1 = i tweet del periodo sono contenuti tutti tra i 200 tweet - si salvano i tweet tra startDate e endDate e stop
		 * caso 2 = i 200 tweet sono più recenti del periodo - bisogna scaricare tweet piu vecchi
		 * caso 3 = i 200 tweet sono a cavallo della endDate - si salvano i tweet più vecchi ma bisogna scaricare tweet piu vecchi
		 * caso 4 = i 200 tweet sono contenuti tutti all'interno del periodo - si salvano tutti i tweet ma bisogna scaricare tweet piu vecchi
		 * caso 5 = i 200 tweet sono a cavallo con la startDate - si salvano i tweet più recenti e stop
		 * caso 6 = i 200 tweet sono tutti più vecchi della startDate - non prendo nessun tweet e stop
		 * 	
		 */
		if (isGreater(startDate,minDate200) && isGreater(maxDate200,endDate))
		{
			logger.debug("caso 1");
			isFinished = true;
		}
		if (isGreater(minDate200,endDate))
		{
			logger.debug("caso 2");
			isFinished = false;
		}			
		if (isGreater(minDate200,startDate) && isGreater(endDate,minDate200) && isGreater(maxDate200,endDate))
		{
			logger.debug("caso 3");
			isFinished = false;
		}		
		if (isGreater(minDate200,startDate) && isGreater(endDate,maxDate200))
		{
			logger.debug("caso 4");
			isFinished = false;
		}		
		if (isGreater(startDate,minDate200) && isGreater(maxDate200, startDate) && isGreater(endDate,maxDate200))
		{
			logger.debug("caso 5");
			isFinished = true;
		}		
		if (isGreater(startDate, maxDate200))
		{
			logger.debug("caso 6");
			isFinished = true;
		}
		
		/*
		 * 
		 * BEWARE!
		 * TODO: handle this in a better way!
		 * 
		 */
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
		RequestName requestName = RequestName.GET_FOLLOWERS_IDS_WITH_PAGINATION;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);
		requestParameters.add(1, cursor);
		try
		{
			return (IDs) userHandlersManager.executeRequest(requestName, requestParameters);
		}
		catch (ProtectedUserException e)
		{
			//this call can't throw this exception
			System.exit(0);
			return null;
		}
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

	

	@Override
	public List<String> getTweetsWithMaxId(long userId, long maxId) throws TwitterException, ProtectedUserException 
	{
		logger.info("Downloading 200 tweets for user with id:" + userId + " with maxid="+maxId);
		RequestName requestname = RequestName.GET_USER_TWEETS_WITH_MAX_ID;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);
		requestParameters.add(1, maxId);
		List<String> tweets = (List<String>) userHandlersManager.executeRequest(requestname, requestParameters);
		return tweets;
	}

	private List<String> getUpTo100Users(long[] usersIds) throws TwitterException, MethodInputNotCorrectException
	{
		RequestName requestname = RequestName.GET_UP_TO_100_USERS;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, usersIds);
		try
		{
			return (List<String>) userHandlersManager.executeRequest(requestname, requestParameters);
		}
		catch (ProtectedUserException e)
		{
			//this call can't throw this exception
			System.exit(0);
			return null;
		}
	}
	
	@Override
	public String getUserJson(long userId) throws TwitterException
	{
		RequestName requestName = RequestName.GET_USER_JSON_FROM_ID;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);
		try
		{
			return (String) userHandlersManager.executeRequest(requestName, requestParameters);
		}
		catch (ProtectedUserException e)
		{
			//this call can't throw this exception
			System.exit(0);
			return null;
		}
	}

	
	
	@Override
	public String getUserJson(String screenName) throws TwitterException
	{
		RequestName requestName = RequestName.GET_USER_JSON_FROM_SCREENNAME;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, screenName);
		try
		{
			return (String) userHandlersManager.executeRequest(requestName, requestParameters);
		}
		catch (ProtectedUserException e)
		{
			//this call can't throw this exception
			System.exit(0);
			return null;
		}
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
			try
			{
				List<String> chunkResult = getUpTo100Users(chunk);
				usersJsons.addAll(chunkResult);
			} catch (MethodInputNotCorrectException e)
			{
				e.printStackTrace();
				System.exit(0);
			} 
		}
		return usersJsons;
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
			while (resultContainer.isFinished==false)
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
	
	
	private boolean isGreater(Date a, Date b)
	{
		if (a.compareTo(b)>0)
			return true;
		else
			return false;
	}
	
	private IDs getFriendsIdsWithPagination(long userId, long cursor) throws TwitterException
	{
		RequestName requestName = RequestName.GET_FRIENDS_IDS_WITH_PAGINATION;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);
		requestParameters.add(1, cursor);
		try
		{
			return (IDs) userHandlersManager.executeRequest(requestName, requestParameters);
		}
		catch (ProtectedUserException e)
		{
			//this call can't throw this exception
			System.exit(0);
			return null;
		}
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

}
