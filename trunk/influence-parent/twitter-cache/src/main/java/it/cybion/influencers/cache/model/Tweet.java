package it.cybion.influencers.cache.model;

import java.util.Date;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Tweet implements Comparable<Tweet>
{
	private long id;
	private Date created_at;
	private String originalJson;
	
	public static Tweet buildTweetFromJson(String originalJson)
	{
		Gson gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			// "Wed Oct 17 19:59:40 +0000 2012"
			.setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").create();
		Tweet tweet = gson.fromJson(originalJson, Tweet.class);
		tweet.originalJson = originalJson;
		return tweet;
	}
	
	@Override
	public int compareTo(Tweet toCompare)
	{
		if (this.id>=toCompare.id)
			return 1;
		else
			return -1;
	}

	public String getOriginalJson()
	{
		return originalJson;
	}

	public void setOriginalJson(String originalJson)
	{
		this.originalJson = originalJson;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Date getCreatedAt()
	{
		return created_at;
	}

	public void setCreatedAt(Date created_at)
	{
		this.created_at = created_at;
	}
		
}
