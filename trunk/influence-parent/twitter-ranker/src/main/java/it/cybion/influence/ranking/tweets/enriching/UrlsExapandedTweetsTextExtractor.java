package it.cybion.influence.ranking.tweets.enriching;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;



public class UrlsExapandedTweetsTextExtractor
{

	private class Tweet
	{
		public class Entities {	List<Url> urls;	}
		public class Url { String expanded_url;	}
		
		public String text;
		public Entities entities;	
	}

	public static List<String> getUrlsExpandedText(List<String> tweetsJsons)
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
			for (it.cybion.influence.ranking.tweets.enriching.UrlsExapandedTweetsTextExtractor.Tweet.Url url : tweet.entities.urls)
				urls.add(url.expanded_url);
		}

		// page titles are extracted for urls
		Map<String, String> urls2Titles = MultithreadUrlsTitleExtractor.getTitles(urls);

		// urls are removed and replaced with titles
		List<String> tweetsTexts = new ArrayList<String>();
		for (Tweet tweet : tweets)
		{
			String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
			for (it.cybion.influence.ranking.tweets.enriching.UrlsExapandedTweetsTextExtractor.Tweet.Url url : tweet.entities.urls)
				tweetText = tweetText + " " + urls2Titles.get(url);
			tweetsTexts.add(tweetText);
		}
		return tweetsTexts;
	}
}