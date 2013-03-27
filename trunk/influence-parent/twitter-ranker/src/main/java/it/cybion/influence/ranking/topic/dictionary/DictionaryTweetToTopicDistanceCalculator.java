package it.cybion.influence.ranking.topic.dictionary;

import it.cybion.influence.ranking.topic.TweetToTopicDistanceCalculator;

import java.util.List;
import java.util.StringTokenizer;

public class DictionaryTweetToTopicDistanceCalculator implements TweetToTopicDistanceCalculator
{
	List<String> dictionary;
	
	public DictionaryTweetToTopicDistanceCalculator(List<String> dictionary)
	{
		this.dictionary = dictionary;
	}

//	@Override
//	public float getTweetToTopicDistance(String tweetText)
//	{
//		tweetText = tweetText.toLowerCase();
//		StringTokenizer tokenizer = new StringTokenizer(tweetText);
//		while (tokenizer.hasMoreElements())
//		{
//			String token = tokenizer.nextToken();
//			if (dictionary.contains(token))
//				return 1;
//		}
//		return 0;
//	}
	
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
