package it.cybion.influence.ranking.topic.lucene.enriching;



import it.cybion.influence.ranking.model.Tweet;
import it.cybion.influence.ranking.model.Url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;



public class UrlsExapandedTweetsTextExtractor
{

	

//	public static List<String> getUrlsExpandedText(List<String> tweetsJsons)
//	{
//		// tweets are built
//		List<Tweet> tweets = new ArrayList<Tweet>();
//		Gson gson = new Gson();
//		for (String tweetJson : tweetsJsons)
//		{
//			Tweet tweet = gson.fromJson(tweetJson, Tweet.class);
//			tweets.add(tweet);
//		}
//
//		// urls are extracted
//		List<String> urls = new ArrayList<String>();
//		for (Tweet tweet : tweets)
//		{
//			for (Tweet.Url url : tweet.entities.urls)
//				urls.add(url.expanded_url);
//		}
//
//		// page titles are extracted for urls
//		Map<String, String> urls2Titles = MultithreadUrlsTitleExtractor.getTitles(urls);
//
//		// urls are removed and replaced with titles
//		List<String> tweetsTexts = new ArrayList<String>();
//		for (Tweet tweet : tweets)
//		{
//			String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
//			for (it.cybion.influence.ranking.topic.lucene.enriching.UrlsExapandedTweetsTextExtractor.Tweet.Url url : tweet.entities.urls)
//				tweetText = tweetText + " " + urls2Titles.get(url);
//			tweetsTexts.add(tweetText);
//		}
//		return tweetsTexts;
//	}
	
	public static Map<Long,String> getUrlsExpandedTextFromTexts(List<String> tweetsJsons)
	{
		// tweets are built
		List<Tweet> tweets = new ArrayList<Tweet>();
		Gson gson = new Gson();
		for (String tweetJson : tweetsJsons)
		{
			Tweet tweet = gson.fromJson(tweetJson, Tweet.class);
			tweets.add(tweet);
		}

		// urls are extracted
		List<String> urls = new ArrayList<String>();
		for (Tweet tweet : tweets)
		{
			for (Url url : tweet.entities.urls)
				urls.add(url.expanded_url);
		}

		// page titles are extracted for urls
		Map<String, String> urls2Titles = MultithreadUrlsTitleExtractor.getTitles(urls);

		// urls are removed and replaced with titles
		Map<Long,String> tweetsIds2Texts = new HashMap<Long,String>();
		for (Tweet tweet : tweets)
		{
			String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
			for (Url url : tweet.entities.urls)
				tweetText = tweetText + " " + urls2Titles.get(url);
			tweetsIds2Texts.put(tweet.id,tweetText);
		}
		return tweetsIds2Texts;
	}
	
	public static List<Tweet> getUrlsExpandedTextTweets(List<Tweet> originalTweets)
	{
		List<String> urls = new ArrayList<String>();
		for (Tweet tweet : originalTweets)
		{
			for (Url url : tweet.entities.urls)
				urls.add(url.expanded_url);
		}
		Map<String, String> urls2Titles = MultithreadUrlsTitleExtractor.getTitles(urls);

		List<Tweet> expandedTweets = new ArrayList<Tweet>();
		for (Tweet tweet : originalTweets)
		{
			String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
			for (Url url : tweet.entities.urls)
				tweetText = tweetText + " " + urls2Titles.get(url);
			Tweet expandedTweet = tweet;
			expandedTweet.urlsExpandedText = tweetText;
			expandedTweets.add(expandedTweet);
		}
		return expandedTweets;
	}
}
