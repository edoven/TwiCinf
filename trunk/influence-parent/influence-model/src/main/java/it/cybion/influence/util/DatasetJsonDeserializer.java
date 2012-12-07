package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;

import java.util.ArrayList;
import java.util.List;


/*
 * BEWARE BEWARE BEWARE !!
 * 
 * Dataset tweet json is not the original raw json obtained
 * from TwitterAPI!
 * The original json variables names format is "xxx_yyy".
 * Dataset json variables names format is "xxxYyy".
 * 
 * The json used as input for this class methods are dataset tweets
 */

public class DatasetJsonDeserializer {

	private Gson gson;
	
	public DatasetJsonDeserializer() {
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
	
	public User deserializeJsonStringToUser(String json){	
		return gson.fromJson(json, User.class);
	}
	
}