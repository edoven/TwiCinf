package it.cybion.influencers.ranking.topic;

public interface TopicScorer
{
	float getTweetToTopicDistance(String tweetText);
}
