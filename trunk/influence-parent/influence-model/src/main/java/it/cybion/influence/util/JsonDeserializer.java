package it.cybion.influence.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.cybion.influence.model.Tweet;

public class JsonDeserializer {

	/*
	public static void main(String[] args) {
		List<String> jsons = MysqlConnector.getAllTwitterJsons();
		List<Tweet> tweets = jsons2tweets(jsons);
		for (Tweet tweet: tweets)
			System.out.println(tweet.getUser().getScreenName());

	}
	*/
	
	public static List<Tweet> jsons2tweets(List<String> jsons)
	{
		Gson gson = new GsonBuilder().create();
		
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet;
		//int counter = 0;
		for (String json: jsons) {
			//System.out.println("Deserializing json "+counter+" of "+jsons.size());
			tweet = gson.fromJson(json, Tweet.class);
			tweets.add(tweet);
			//counter++;
		}
		return tweets;
	}
	
}