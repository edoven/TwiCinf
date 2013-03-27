package it.cybion.influence.ranking;

import it.cybion.influence.ranking.model.Tweet;
import it.cybion.influence.ranking.topic.TweetToTopicDistanceCalculator;
import it.cybion.influence.ranking.urlsexpansion.UrlsExapandedTweetsTextExtractor;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import com.google.gson.Gson;


public class RankingCalculator
{
	private static final Logger logger = Logger.getLogger(RankingCalculator.class);
	
	private Gson gson = new Gson();
	private TwitterCache twitterCache;
	private List<RankedUser> rankedUsers = new ArrayList<RankedUser>();
	private TweetToTopicDistanceCalculator topicDistanceCalculator;
	
	
	public RankingCalculator(TwitterCache twitterCache,
							 TweetToTopicDistanceCalculator topicDistanceCalculator)
	{
		this.twitterCache = twitterCache;
		this.topicDistanceCalculator = topicDistanceCalculator;
	}
			
	public List<RankedUser> getRankedUsersWithUrlsResolution(List<Long> usersToRank, Date fromDate, Date toDate)
	{
		int usersCount = 1;
		for (Long userId : usersToRank)
		{		
			logger.info("");
			logger.info("Calculating rank for user "+(usersCount++)+"/"+usersToRank.size()+" with id "+userId);
			List<String> tweetsJsons = getTweets(userId, fromDate, toDate);
			if (tweetsJsons.isEmpty())
				continue;
			
			RankedUser rankedUser = calculateRankWithUrlsResolution(tweetsJsons);	;
			rankedUsers.add(rankedUser);
					
			logger.info("user:"+rankedUser.getScreenName()+
								" - followers:"+rankedUser.getFollowersCount()+
								" - originalTweets:"+rankedUser.getOriginalTweets()+
								" - topicTweetsCount:"+rankedUser.getTopicTweetsCount()+
								" - topicTweetsRatio:"+rankedUser.getTopicTweetsRatio()+
								" - meanRetweetCount:"+rankedUser.getMeanRetweetsCount()+
								" - rank:"+rankedUser.getRank());			
		}
		Collections.sort(rankedUsers);
		return rankedUsers;
	}
	
	public List<RankedUser> getRankedUsersWithoutUrlsResolution(List<Long> usersToRank, Date fromDate, Date toDate)
	{
		int usersCount = 1;
		for (Long userId : usersToRank)
		{		
			logger.info("");
			logger.info("Calculating rank for user "+(usersCount++)+"/"+usersToRank.size()+" with id "+userId);
			List<String> tweetsJsons = getTweets(userId, fromDate, toDate);
			if (tweetsJsons.isEmpty())
				continue;
			
			RankedUser rankedUser = calculateRankWithoutUrlsResolution(tweetsJsons);	;
			rankedUsers.add(rankedUser);
					
			logger.info("user:"+rankedUser.getScreenName()+
								" - followers:"+rankedUser.getFollowersCount()+
								" - originalTweets:"+rankedUser.getOriginalTweets()+
								" - topicTweetsCount:"+rankedUser.getTopicTweetsCount()+
								" - topicTweetsRatio:"+rankedUser.getTopicTweetsRatio()+
								" - meanRetweetCount:"+rankedUser.getMeanRetweetsCount()+
								" - rank:"+rankedUser.getRank());			
		}
		Collections.sort(rankedUsers);
		return rankedUsers;
	}
		
	private RankedUser calculateRankWithoutUrlsResolution(List<String> tweetsJsons)
	{
		long globalBegin = System.currentTimeMillis();
		double retweetsAccumulator = 0;
		double topicTweetsCount = 0;
		double rank;
		List<Tweet> tweets = getTweetsObjectsFromJsons(tweetsJsons);
		String userScreenName = tweets.get(0).user.screen_name;
		int followersCount = tweets.get(0).user.followers_count;
		
		List<Tweet> originalTweets = getOriginalTweets(tweets);
		if (originalTweets.size()<1)
			return new RankedUser(userScreenName, followersCount, 0, 0, 0, 0, 0);
//		long urlsResolutionBegin = System.currentTimeMillis();
//		originalTweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(originalTweets);
//		long urlsResolutionEnd = System.currentTimeMillis();
//		logger.info("Urls resolution time ="+(urlsResolutionEnd-urlsResolutionBegin)/1000.0);
		
		for (Tweet tweet : originalTweets)
		{
//			String text = tweet.urlsExpandedText;
			String text = tweet.text;
			double topicProximity = topicDistanceCalculator.getTweetToTopicDistance(text);
			int retweetCount = tweet.retweet_count;
			retweetsAccumulator = retweetsAccumulator + (topicProximity*retweetCount);
			topicTweetsCount = topicTweetsCount + topicProximity;		
		}
		double meanRetweetCount = retweetsAccumulator / originalTweets.size();
		double topicTweetsRatio = topicTweetsCount / originalTweets.size();
		rank = rankingFunction(topicTweetsCount, meanRetweetCount, followersCount, topicTweetsRatio);
		long globalEnd = System.currentTimeMillis();
		logger.info("Score calcultion time = "+(globalEnd-globalBegin)/1000.0 +" (for "+tweetsJsons.size()+" tweets)");
		return new RankedUser(userScreenName, followersCount, originalTweets.size(), topicTweetsCount, topicTweetsRatio, meanRetweetCount, rank);
	}

	
	private RankedUser calculateRankWithUrlsResolution(List<String> tweetsJsons)
	{
		long globalBegin = System.currentTimeMillis();
		double retweetsAccumulator = 0;
		double topicTweetsCount = 0;
		double rank;
		List<Tweet> tweets = getTweetsObjectsFromJsons(tweetsJsons);
		String userScreenName = tweets.get(0).user.screen_name;
		int followersCount = tweets.get(0).user.followers_count;
		
		List<Tweet> originalTweets = getOriginalTweets(tweets);
		if (originalTweets.size()<1)
			return new RankedUser(userScreenName, followersCount, 0, 0, 0, 0, 0);
		long urlsResolutionBegin = System.currentTimeMillis();
		originalTweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(originalTweets);
		long urlsResolutionEnd = System.currentTimeMillis();
		logger.info("Urls resolution time ="+(urlsResolutionEnd-urlsResolutionBegin)/1000.0);
		
		for (Tweet tweet : originalTweets)
		{
			String text = tweet.urlsExpandedText;
			double topicProximity = topicDistanceCalculator.getTweetToTopicDistance(text);
			int retweetCount = tweet.retweet_count;
			retweetsAccumulator = retweetsAccumulator + (topicProximity*retweetCount);
			topicTweetsCount = topicTweetsCount + topicProximity;		
		}
		double meanRetweetCount = retweetsAccumulator / originalTweets.size();
		double topicTweetsRatio = topicTweetsCount / originalTweets.size();
		rank = rankingFunction(topicTweetsCount, meanRetweetCount, followersCount, topicTweetsRatio);
		long globalEnd = System.currentTimeMillis();
		logger.info("Score calcultion time = "+(globalEnd-globalBegin)/1000.0 +" (for "+tweetsJsons.size()+" tweets)");
		return new RankedUser(userScreenName, followersCount, originalTweets.size(), topicTweetsCount, topicTweetsRatio, meanRetweetCount, rank);
	}

	private List<Tweet> getOriginalTweets(List<Tweet> tweets)
	{
		List<Tweet> originalTweets = new ArrayList<Tweet>();
		for (Tweet tweet : tweets)
			if (tweet.retweeted_status==null && tweet.in_reply_to_status_id_str==null)
				originalTweets.add(tweet);
		return originalTweets;
	}

	private List<String> getTweets(long userId, Date fromDate, Date toDate)
	{
		long begin = System.currentTimeMillis();
		List<String> tweetsJsons;
		try
		{
			tweetsJsons = twitterCache.getTweetsByDate(userId, fromDate, toDate);
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
			logger.info("Error with user with id "+userId+". Skipped!");
			tweetsJsons = Collections.emptyList();
		}
		catch (ProtectedUserException e)
		{
			logger.info("User with id "+userId+" is protected. Skipped!");
			tweetsJsons = Collections.emptyList();
		}
		if (tweetsJsons.size()<1)
			logger.info("User has 0 tweets, can't calculate rank!");
		long end = System.currentTimeMillis();
		logger.info("Tweets retrieval time = "+(end-begin)/1000.0);
		return tweetsJsons;
	}
	
	
	private List<Tweet> getTweetsObjectsFromJsons(List<String> tweetsJsons)
	{
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet;
		for (String tweetJson : tweetsJsons)
		{
			tweet = gson.fromJson(tweetJson, Tweet.class);
			tweets.add(tweet);
		}
		return tweets;
	}
	
	
	private double rankingFunction(double topicTweetsCount, double meanRetweetCount, int followersCount, double topicTweetsRatio)
	{
		double rank = Math.log10(followersCount) * Math.sqrt(topicTweetsCount) * meanRetweetCount * (topicTweetsRatio*100)*(topicTweetsRatio*100);
		return rank;
	}
	
	
}
