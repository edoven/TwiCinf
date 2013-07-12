package it.cybion.influencers.cache.persistance;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import it.cybion.influencers.cache.model.Tweet;
import it.cybion.influencers.cache.persistance.exceptions.DataRangeNotCoveredException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TweetsPersistenceFacade
{
	private static final Logger LOGGER = Logger.getLogger(TweetsPersistenceFacade.class);
	
	private DBCollection tweetsCollection;
	
	public TweetsPersistenceFacade(DBCollection tweetsCollection)
	{
		this.tweetsCollection = tweetsCollection;
	}
		
	public String getTweet(long tweetId)
	{
		BasicDBObject key = new BasicDBObject();
		key.put("id", tweetId);
		DBObject tweet = tweetsCollection.findOne(key);
		if (tweet == null)
			return null;
		else
			return tweet.toString();	
	}
	
	public List<String> getUpTo200Tweets(long userId) throws UserWithNoTweetsException
	{
		BasicDBObject key = new BasicDBObject();
		key.put("user.id", userId);
		DBCursor cursor = tweetsCollection.find(key);
		List<String> tweets = new ArrayList<String>();
		while (cursor.hasNext() && tweets.size() < 200)
		{
			DBObject retrivedTweet = cursor.next();
			String tweetJson = retrivedTweet.toString();
			tweets.add(tweetJson);
		}			
		if (tweets.size() == 0) {
			throw new UserWithNoTweetsException("userId " + userId + " has no tweets in mongodb collection");
        }
		return tweets;
	}
	
	public List<String> getTweets(long userId) throws UserWithNoTweetsException
	{
		BasicDBObject key = new BasicDBObject();
		key.put("user.id", userId);
		DBCursor cursor = tweetsCollection.find(key);
		List<String> tweets = new ArrayList<String>();
		while (cursor.hasNext())
		{
			DBObject retrivedTweet = cursor.next();
			String tweetJson = retrivedTweet.toString();
			tweets.add(tweetJson);
		}	
		if (tweets.size() == 0)
			throw new UserWithNoTweetsException("userId " + userId + " has no tweets in mongodb collection");
		return tweets;
	}
	
	public void putTweetIfNotPresent(String tweetToInsertJson)
	{
		DBObject tweetToInsert = (DBObject) JSON.parse(tweetToInsertJson);
		long tweetId = -1;
		try
		{
			Object id = tweetToInsert.get("id");		
			if (id instanceof Long)
				tweetId = (Long) id;
			else
				if (id instanceof Integer) {
					tweetId = new Long((Integer) id);
                }
				else
				{
					LOGGER.error("can't cast " + id + " to integer or long");
//					System.exit(0);
				}
		} catch (ClassCastException e)
		{
			LOGGER.error(
                    "ERROR: problem extracting id from " + tweetToInsertJson + " " + e.getMessage());
			return;
		}
		String tweetJson = getTweet(tweetId);
		if (tweetJson == null)
			tweetsCollection.insert(tweetToInsert);
	}

	
	public void putTweets(List<String> tweets)
	{
		for (String tweet : tweets)
			putTweetIfNotPresent(tweet);
	}
	
	
	public void removeTweet(Long tweetId)
	{
		BasicDBObject key = new BasicDBObject();
		key.put("id", tweetId);
		DBCursor cursor = tweetsCollection.find(key);
		if (cursor.hasNext())
		{
			DBObject json = cursor.next();
			tweetsCollection.remove(json);
		}
	}
	
	public List<String> getTweetsByDate(long userId, Date fromDate, Date toDate ) throws UserWithNoTweetsException, DataRangeNotCoveredException
	{
		List<String> tweetsJsons = getTweets(userId);
		List<Tweet> tweets = getTweetsFromJsons(tweetsJsons);
		Collections.sort(tweets);
		Date oldestTweetDate = tweets.get(0).getCreatedAt();
		Date mostRecentTweetDate = tweets.get(tweets.size()-1).getCreatedAt();
		if (fromDate.compareTo(oldestTweetDate)<=0 || toDate.compareTo(mostRecentTweetDate)>=0) {
			throw new DataRangeNotCoveredException("range specified are external to current cached tweets");
        }
		
		List<String> matchingTweets = new ArrayList<String>();
		Date tweetDate;
		for (Tweet tweet : tweets)
		{
			tweetDate = tweet.getCreatedAt();
			if (fromDate.compareTo(tweetDate)<=0 && toDate.compareTo(tweetDate)>0) {
				matchingTweets.add(tweet.getOriginalJson());
            }
		}
		return matchingTweets;
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
