package it.cybion.influence.ranking.topic;

public interface TopicScorer
{
	float getTweetToTopicDistance(String tweetText);
}
