package it.cybion.influencers.ranking.usermetrics;


import it.cybion.influencers.ranking.topic.lucene.TweetToTopicSimilarityCalculator;
import it.cybion.influencers.twitter.TwitterFacade;



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
