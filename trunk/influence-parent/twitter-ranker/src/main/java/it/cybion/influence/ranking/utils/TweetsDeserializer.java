package it.cybion.influence.ranking.utils;

import it.cybion.influence.ranking.model.Tweet;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class TweetsDeserializer
{
	public static List<Tweet> getTweetsObjectsFromJsons(List<String> tweetsJsons)
	{
		Gson gson = new Gson();
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet;
		for (String tweetJson : tweetsJsons)
		{
			tweet = gson.fromJson(tweetJson, Tweet.class);
			tweets.add(tweet);
		}
		return tweets;
	}
}
