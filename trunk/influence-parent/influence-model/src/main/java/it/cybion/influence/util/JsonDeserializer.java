package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class JsonDeserializer {

	/* TODO add a constructor that builds an instance variable gson,
	* used to deserialize tweets when calling the non-static-method json2tweets.
	 * building a gson it's costly! Why would we do it everytime the method is called?
	 */
	 

	public List<Tweet> deserializeJsonStringsToTweets(List<String> jsons)
	{
			
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (String json: jsons) {
			Tweet tweet = deserializeJsonStringToTweet(json);
			tweets.add(tweet);
		}
		return tweets;
	}
	
	public Tweet deserializeJsonStringToTweet(String json)
	{
		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy hh:mm:ss a").create();		
		return gson.fromJson(json, Tweet.class);
	}
	
}