package it.cybion.influence.ranking;

import it.cybion.influence.ranking.model.Tweet;
import it.cybion.influence.ranking.topic.lucene.TweetToTopicSimilarityCalculator;
import it.cybion.influence.ranking.topic.lucene.TweetsIndexCreator;
import it.cybion.influence.ranking.topic.lucene.enriching.UrlsExapandedTweetsTextExtractor;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;

import twitter4j.TwitterException;

import com.google.gson.Gson;


public class RankingCalculator
{
	private static final Logger logger = Logger.getLogger(RankingCalculator.class);
	
	private Gson gson = new Gson();
	
	
	public List<RankedUser> printRanks(List<Long> seedUsers, 
						   List<Long> usersToRank, 
						   TwitterCache twitterFacade,
						   String luceneTempDirPath,
						   int fromYear, int fromMonth, int fromDay,
						   int toYear, int toMonth, int toDay)
	{
		List<RankedUser> rankedUsers = new ArrayList<RankedUser>();
		logger.info("Building lucene indexes - START");
		List<Directory> indexes = TweetsIndexCreator.createSingleDocumentIndexesForUsers(twitterFacade, luceneTempDirPath, seedUsers);
		logger.info("Building lucene indexes - FINISHED");
		TweetToTopicSimilarityCalculator topicCalculator = new TweetToTopicSimilarityCalculator(indexes);
		int userCounts = 0;
		for (Long userId : usersToRank)
		{
			logger.info("Calculating rank for user "+(userCounts++)+"/"+usersToRank.size());
			double accumulator = 0;
			double topicTweetsCount = 0;
			int originalTweetsCount = 0;
			double rank = -1;
			List<String> tweetsJsons;
			try
			{
				tweetsJsons = twitterFacade.getTweetsByDate(userId, fromYear, fromMonth, fromDay, toYear, toMonth, toDay);
			}
			catch (TwitterException e)
			{
				e.printStackTrace();
				logger.info("Error with user with id "+userId+". Skipped!");
				continue;
			}
			catch (ProtectedUserException e1)
			{
				logger.info("User with id "+userId+" is protected. Skipped!");
				continue;
			}
			
			if (tweetsJsons.size()>0)
			{
				List<Tweet> tweets = getTweetsObjectsFromJsons(tweetsJsons);
				tweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(tweets);
				String userScreenName = tweets.get(0).user.screen_name;
				int followersCount = tweets.get(0).user.followers_count;
				for (Tweet tweet : tweets)
				{
					if (tweet.retweeted_status==null && tweet.in_reply_to_status_id_str==null)
					{
						originalTweetsCount++;
						String text = tweet.urlsExpandedText;
						double topicProximity = topicCalculator.getTweetRank(text);
						int retweetCount = tweet.retweet_count;
						accumulator = accumulator + (topicProximity*retweetCount);
						topicTweetsCount = topicTweetsCount + topicProximity;
					}					
				}
				double meanRetweetCount = accumulator / originalTweetsCount;
				double topicTweetsRatio = topicTweetsCount / originalTweetsCount;
				rank = rankingFunction(topicTweetsCount, meanRetweetCount, followersCount, topicTweetsRatio);
				rankedUsers.add(new RankedUser(userScreenName, followersCount, originalTweetsCount, meanRetweetCount, topicTweetsRatio, rank));
				logger.info("user:"+userScreenName+
									" - followers:"+followersCount+
									" - originalTweets:"+originalTweetsCount+
									" - topicTweetsCount:"+topicTweetsCount+
									" - topicTweetsRatio:"+topicTweetsRatio+
									" - meanRetweetCount:"+meanRetweetCount+
									" - rank:"+rank);		
			}	
//			logger.info("Tot tweets = "+tweetsJsons.size());
//			logger.info("Original tweets = "+originalTweetsCount);
		}
		Collections.sort(rankedUsers);
		return rankedUsers;
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
