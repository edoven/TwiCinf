package utils;

import it.cybion.influence.ranking.model.Tweet;
import it.cybion.influence.ranking.utils.TweetsDeserializer;
import it.cybion.influence.ranking.utils.urlsexpansion.UrlsExapandedTweetsTextExtractor;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.List;

import twitter4j.TwitterException;

public class TweetsGetter
{
	public static List<String> getUrlsExpandedTweetsTexts(long userId, TwitterCache twitterCache) throws TwitterException, ProtectedUserException
	{
		List<String> jsonTweets = twitterCache.getLast200Tweets(userId);
		List<Tweet> tweets = TweetsDeserializer.getTweetsObjectsFromJsons(jsonTweets);
		tweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(tweets);
		List<String> urlsExpandedTexts = new ArrayList<String>();
		for (Tweet tweet : tweets)
		{
			urlsExpandedTexts.add(tweet.urlsExpandedText);
		}
		return urlsExpandedTexts;
	}
}
