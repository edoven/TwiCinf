package it.cybion.influencers.cache.model;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeserializationTEST
{
	@Test(enabled = true)
	public void deserializeTweetDate()
	{
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                			// "Wed Oct 17 19:59:40 +0000 2012"
                .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
		
		String jsonTweet = "{\"created_at\": \"Wed Jun 06 20:07:10 +0000 2012\"}";
		
		Tweet tweet = gson.fromJson(jsonTweet, Tweet.class);
		Date tweetDate = tweet.getCreatedAt();
		Assert.assertEquals(tweetDate.getDate(), 6);
		Assert.assertEquals(tweetDate.getMonth(), 5);
		Assert.assertEquals(tweetDate.getYear(), 2012-1900 );
	}
}
