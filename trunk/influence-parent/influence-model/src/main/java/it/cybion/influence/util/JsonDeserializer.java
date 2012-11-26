package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;

import java.util.ArrayList;
import java.util.List;


public class JsonDeserializer {

	private Gson gson;
	
	public JsonDeserializer() {
		gson = new GsonBuilder()
					.setDateFormat("MMM dd, yyyy hh:mm:ss a")
					.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
					.create(); 
	}
	 
	public List<Tweet> deserializeJsonStringsToTweets(List<String> jsons){			
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (String json: jsons) {
			Tweet tweet = deserializeJsonStringToTweet(json);
			tweets.add(tweet);
		}
		return tweets;
	}

	public Tweet deserializeJsonStringToTweet(String json){	
		return gson.fromJson(json, Tweet.class);
	}
	
}