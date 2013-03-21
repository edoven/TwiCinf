package it.cybion.influencers.crawler.filtering.language;


import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.cache.TwitterFacade;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import com.google.gson.Gson;



public class LanguageDetectionFilterManager implements FilterManager
{
	
	private static final Logger logger = Logger.getLogger(LanguageDetectionFilterManager.class);

	private TwitterFacade twitterFacade;
	private List<Long> seedUsers;
	private Map<Long, List<String>> user2tweets = new HashMap<Long, List<String>>();
	private String languageProfilesDir;
	private String language;

	public LanguageDetectionFilterManager(String languageProfilesDir, String language)
	{
		this.languageProfilesDir = languageProfilesDir;
		this.language = language;
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade)
	{
		this.twitterFacade = twitterFacade;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade)
	{
		// useless
	}

	@Override
	public void setSeedUsers(List<Long> seedUsers)
	{
		this.seedUsers = seedUsers;
	}

	@Override
	public List<Long> filter()
	{
		solveDependencies();
		return new LanguageDetectionFilter(user2tweets, languageProfilesDir, language).filter();
		// return null;
	}

	private class Tweet
	{
		public String text;
		public Entities entities;

		public class Hashtag
		{
			String text;
		}

		public class UserMention
		{
			String screen_name;
		}

		public class Entities
		{
			List<Hashtag> hashtags;
			List<UserMention> user_mentions;
		}
	}

	private void solveDependencies()
	{
		Gson gson = new Gson();
		for (Long userId : seedUsers)
		{
			List<String> tweetsText = new ArrayList<String>();
			try
			{
				List<String> tweetsJsons;
				try
				{
					tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
				}
				catch (ProtectedUserException e)
				{					
					e.printStackTrace(); 
					logger.info("User with id "+userId+" is protected. Skipped!");
					continue;
				}
				for (String tweetJson : tweetsJsons)
				{
					Tweet tweet = gson.fromJson(tweetJson, Tweet.class);
					String tweetText = tweet.text;

					tweetText = tweetText.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
					tweetText = tweetText.replaceAll("http", "");

					for (Tweet.Hashtag hashtag : tweet.entities.hashtags)
						tweetText = tweetText.replace(hashtag.text, "");
					for (Tweet.UserMention userMention : tweet.entities.user_mentions)
						tweetText = tweetText.replace(userMention.screen_name, "");
					tweetText = tweetText.replace('#', ' ');
					tweetText = tweetText.replace('@', ' ');
					tweetText = tweetText.replace('\n', ' ');
					tweetText = tweetText.replace("RT", " ");

					tweetsText.add(tweetText);
				}
				user2tweets.put(userId, tweetsText);
			} catch (TwitterException e)
			{
				continue;
			}
		}
	}

}
