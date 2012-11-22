package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class JsonDeserializer {

	/* TODO add a constructor that builds an instance variable gson,
	* used to deserialize tweets when calling the non-static-method json2tweets.
	 * building a gson it's costly, why would we do everytime the method is called? */

    //TODO rename it: deserializeJsonStringsToTweets
	public static List<Tweet> jsons2tweets(List<String> jsons)
	{
		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy hh:mm:ss a").create();
		
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