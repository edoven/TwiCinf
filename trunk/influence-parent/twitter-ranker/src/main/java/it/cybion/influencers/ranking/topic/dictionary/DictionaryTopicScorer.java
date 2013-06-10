package it.cybion.influencers.ranking.topic.dictionary;

import it.cybion.influencers.ranking.topic.TopicScorer;

import java.util.List;

public class DictionaryTopicScorer implements TopicScorer
{
	List<String> dictionary;
	
	public DictionaryTopicScorer(List<String> dictionary)
	{
		this.dictionary = dictionary;
	}
	
	@Override
	public float getTweetToTopicDistance(String tweetText)
	{
		tweetText = tweetText.toLowerCase();
		for (String keyword : dictionary)
			if (tweetText.contains(keyword))
				return 1;
		return 0;
	}

}
