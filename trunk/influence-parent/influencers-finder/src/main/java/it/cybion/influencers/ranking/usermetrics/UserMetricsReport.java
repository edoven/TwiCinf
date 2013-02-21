package it.cybion.influencers.ranking.usermetrics;


public class UserMetricsReport
{

	long userId;
	String userScreenName;
	int followersCount;
	float topicSimilarityTweetByTweet;
	float topicSimilaritySingleDocument;
	// int singleRetweetterersCount;
	float retweetsPerTweetAVG;

	public UserMetricsReport(long userId, String userScreenName, int followersCount, float topicSimilarityTweetByTweet, float topicSimilaritySingleDocument,
	// int singleRetweetterersCount,
			float retweetsPerTweetAVG)
	{
		this.userId = userId;
		this.userScreenName = userScreenName;
		this.followersCount = followersCount;
		this.topicSimilarityTweetByTweet = topicSimilarityTweetByTweet;
		this.topicSimilaritySingleDocument = topicSimilaritySingleDocument;
		// this.singleRetweetterersCount = singleRetweetterersCount;
		this.retweetsPerTweetAVG = retweetsPerTweetAVG;
	}

}
