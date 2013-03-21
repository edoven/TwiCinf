package it.cybion.influencers.cache.persistance.mongodb;

import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.persistance.exceptions.OldestTweetsNeedToBeDownloadedException;
import it.cybion.influencers.cache.persistance.exceptions.TweetNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class TweetsMongodbPersistanceFacade
{
	private final Logger logger = Logger.getLogger(TweetsMongodbPersistanceFacade.class);
	
	private DBCollection tweetsCollection;
	
	public TweetsMongodbPersistanceFacade(DBCollection tweetsCollection)
	{
		this.tweetsCollection = tweetsCollection;
	}
	
	
	private String getTweet(long tweetId) throws TweetNotPresentException
	{
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", tweetId);
		DBObject tweet = tweetsCollection.findOne(keys);
		if (tweet == null)
			throw new TweetNotPresentException("Tweet with id " + tweetId + " is not in the collection.");
		return tweet.toString();
	}
	
	public List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException
	{
		DBCursor cursor = tweetsCollection.find(new BasicDBObject("user.id", userId));
		List<String> tweets = new ArrayList<String>();
		while (cursor.hasNext() && tweets.size() < 200)
			tweets.add(cursor.next().toString());
		if (tweets.size() == 0)
			throw new UserWithNoTweetsException();
		return tweets;
	}
	
	public List<String> getTweets(long userId) throws UserWithNoTweetsException
	{
		DBCursor cursor = tweetsCollection.find(new BasicDBObject("user.id", userId));
		List<String> tweets = new ArrayList<String>();
		while (cursor.hasNext())
			tweets.add(cursor.next().toString());
		if (tweets.size() == 0)
			throw new UserWithNoTweetsException();
		return tweets;
	}
	
	public void putTweet(String tweetToInsertJson)
	{
		DBObject tweetToInsert = (DBObject) JSON.parse(tweetToInsertJson);
		long tweetId = -1;
		try
		{
			Object id = tweetToInsert.get("id");		
			if (id instanceof Long)
				tweetId = (Long) id;
			else
				if (id instanceof Integer)
					tweetId = new Long((Integer) id);
				else
				{
					logger.info("problem with twitter id "+id);
					System.exit(0);
				}
		} catch (ClassCastException e)
		{
			logger.info("ERROR: problem extracting id from " + tweetToInsertJson);
			return;
		}
		try
		{
			getTweet(tweetId);
		} catch (TweetNotPresentException e)
		{
			tweetsCollection.insert(tweetToInsert);
		}
	}

	
	public void putTweets(List<String> tweets)
	{
		for (String tweet : tweets)
			putTweet(tweet);
	}
	
	
	public void removeTweet(Long tweetId)
	{
		BasicDBObject keys = new BasicDBObject();
		keys.put("id", tweetId);
		DBCursor cursor = tweetsCollection.find(keys);
		if (cursor.hasNext())
		{
			DBObject json = cursor.next();
			tweetsCollection.remove(json);
		}
	}

	
	
	public List<String> getTweetsByDate(long userId, 
										int fromYear, int fromMonth , int fromDay,
										int toYear, int toMonth, int toDay) throws UserWithNoTweetsException, OldestTweetsNeedToBeDownloadedException
	{
		Date fromDate = new Date(fromYear-1900, fromMonth-1, fromDay);
		Date toDate = new Date(toYear-1900, toMonth-1, toDay);
		List<String> userTweets = getTweets(userId);
		List<String> goodTweets = new ArrayList<String>();
		Tweet tweet;
		Date oldestDate = new Date(2200-1900,0,1); // 2200/01/01
		Date tweetDate;
		for (String tweetJson : userTweets)
		{
			tweet = Tweet.buildTweetFromJson(tweetJson);
			tweetDate = tweet.created_at;
			if (fromDate.compareTo(tweetDate)<0 && toDate.compareTo(tweetDate)>0)
				goodTweets.add(tweetJson);
			if (tweetDate.compareTo(oldestDate)<0)
				oldestDate = new Date(tweetDate.getYear(), tweetDate.getMonth(), tweetDate.getDate());
		}
		if (oldestDate.compareTo(fromDate)>0)
			throw new OldestTweetsNeedToBeDownloadedException();
		return goodTweets;
	}
}
