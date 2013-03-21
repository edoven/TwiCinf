package it.cybion.influencers.crawler.utils.linkextractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;
import it.cybion.influencers.cache.TwitterFacade;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import com.google.gson.Gson;

public class LinkExtractor
{
	private static final Logger logger = Logger.getLogger(LinkExtractor.class);
	
	public TwitterFacade twitterFacade;	
	
	
	
	public LinkExtractor(TwitterFacade twitterFacade)
	{
		super();
		this.twitterFacade = twitterFacade;
	}


	private class Tweet
	{
		public Entities entities;
		public class Entities{ List<Url> urls; }
		public class Url{ String expanded_url; }
	}
	
	public List<String> getLinks(List<Long> usersIds)  
	{
		List<String> urls = new ArrayList<String>();
		for (Long userId : usersIds)
			try
			{
				try
				{
					urls.addAll(getLinks(userId));
				}
				catch (ProtectedUserException e)
				{
					logger.info("User with id "+userId+" is protected. Skipped!");
					continue;
				}
			}
			catch (TwitterException e)
			{
				e.printStackTrace();
				logger.info("Problem with user with id "+userId+". User skipped.");
			}
		return new ArrayList<String>(new HashSet<String>(urls));
	}

	
	public List<String> getLinks(long userId) throws TwitterException, ProtectedUserException
	{
		List<String> jsonTweets = twitterFacade.getUpTo200Tweets(userId);
		Set<String> urls = new HashSet<String>();
		for (String jsonTweet : jsonTweets)
			urls.addAll(getLinks(jsonTweet));
		List<String> result = new ArrayList<String>();
		result.addAll(urls);
		return result;
	}
	
	
	public List<String> getLinks(String jsonTweet)
	{
		Gson gson = new Gson();
		Tweet tweet = gson.fromJson(jsonTweet, Tweet.class);
		List<Tweet.Url> urls = tweet.entities.urls;
		List<String> stringUlrs = new ArrayList<String>();
		for (Tweet.Url url : urls)
			stringUlrs.add(url.expanded_url);
		return stringUlrs;
	}
}
