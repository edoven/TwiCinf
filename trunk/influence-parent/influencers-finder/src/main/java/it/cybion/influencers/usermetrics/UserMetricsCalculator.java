package it.cybion.influencers.usermetrics;


import java.net.UnknownHostException;
import java.util.List;

import it.cybion.influencers.lucene.TweetToTopicSimilarityCalculator;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;



public class UserMetricsCalculator
{

	private TwitterFacade twitterFacade;
	private TweetToTopicSimilarityCalculator topicSimilarityCalculator;

	public UserMetricsCalculator(TwitterFacade twitterFacade, TweetToTopicSimilarityCalculator topicSimilarityCalculator)
	{
		this.twitterFacade = twitterFacade;
		this.topicSimilarityCalculator = topicSimilarityCalculator;
	}

	public UserMetricsReport caluclateReport(Long userId)
	{
		// List<String> tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
		//
		// int followersCount = twitterFacade.getFollowersCount(userId);
		// String userScreenName = twitterFacade.getScreenName(userId);
		// float topicSimilarityTweetByTweet =
		// getTopicSimilarityTweetByTweet(tweets);
		// float retweetsPerTweetAVG = getRetweetsPerTweetAVG(tweets);
		return null;
	}

}
