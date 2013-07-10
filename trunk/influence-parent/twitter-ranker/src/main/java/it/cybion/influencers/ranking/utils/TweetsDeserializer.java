package it.cybion.influencers.ranking.utils;

import com.google.gson.Gson;
import it.cybion.influencers.ranking.model.Tweet;

import java.util.ArrayList;
import java.util.List;

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
