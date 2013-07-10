package it.cybion.influence.ranking.keywords;

import com.google.gson.Gson;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.ranking.model.Tweet;
import it.cybion.influencers.ranking.utils.ListFileReader;
import it.cybion.influencers.ranking.utils.urlsexpansion.UrlsExapandedTweetsTextExtractor;
import org.apache.log4j.Logger;
import twitter4j.TwitterException;
import utils.TwitterFacadeFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaPerlaTweetsPrinter
{
	
	private static final Logger logger = Logger.getLogger(LaPerlaTweetsPrinter.class);
	
	public static void main(String[] args) throws UnknownHostException 
	{
		List<Long> laPerla2800UserIds = ListFileReader.readLongListFile("/home/godzy/Dropbox/universita/tesi/laPerla/laPerla2800UsersIds.txt");
		TwitterCache twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		Collections.shuffle(laPerla2800UserIds);
		laPerla2800UserIds = laPerla2800UserIds.subList(0, 200);
		List<String> enrichedTexts = new ArrayList<String>();
		int userCount = 0;
		for (Long userId : laPerla2800UserIds)
		{
			logger.info("User "+(userCount++)+"/"+laPerla2800UserIds.size());
			List<String> tweetsJsons = null;
			try
			{
				tweetsJsons = twitterFacade.getLast200Tweets(userId);
			}
			catch (TwitterException e)
			{
				e.printStackTrace();
				logger.info("user skipeed");
				continue;
			}
			catch (ProtectedUserException e)
			{
				e.printStackTrace();
				logger.info("user skipeed");
				continue;
			}
			List<Tweet> tweets = getTweetsObjectsFromJsons(tweetsJsons);
			tweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(tweets);
			for (Tweet tweet : tweets)
				enrichedTexts.add(tweet.urlsExpandedText);
		}
		for (String text : enrichedTexts)
		{
			System.out.println(text);
		}
	}
	
	private static List<Tweet> getTweetsObjectsFromJsons(List<String> tweetsJsons)
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
