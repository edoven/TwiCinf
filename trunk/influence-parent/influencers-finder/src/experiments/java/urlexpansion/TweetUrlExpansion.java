package urlexpansion;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import twitter4j.TwitterException;

import com.google.gson.Gson;

public class TweetUrlExpansion {

	private class Tweet {		
		public String text;
		public Entities entities;	
		
		public class Url { String expanded_url;}
		
		public class Entities {
			List<Url> urls;
		}			
	}
	
	
	public static void main(String[] args) throws UnknownHostException, TwitterException {
		TwitterFacade twitterFacade = getTwitterFacade();
		List<String> tweetsJsons = twitterFacade.getUpTo200Tweets(6832662L);
		for (String tweetJson : tweetsJsons) 
			getUrlExpandedTweet(tweetJson);
	}
	
	
	public static String getUrlExpandedTweet(String tweetJson) {
		Gson gson = new Gson();	
		Tweet tweet = gson.fromJson(tweetJson, Tweet.class);		
		String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
		
		String beforeTweetText = tweet.text.replaceAll("\n", "");
		System.out.println("BEFORE - "+beforeTweetText);
		for (Tweet.Url url : tweet.entities.urls) {
			//System.out.println("\t"+url.expanded_url);
			tweetText = tweetText + " " + getTitleFromUrl(url.expanded_url);
		}
		System.out.println("AFTER - "+ tweetText.replace("\n", ""));
		System.out.println("");
		return "";		
	}
	
	
	public static String getTitleFromUrl(String urlString) {		
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByTag("title").text();
		} catch (IOException e) {
			return "";
		}		
	}
	
	
	
	private static TwitterFacade getTwitterFacade() throws UnknownHostException {
		Token applicationToken = new Token("tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("tokens/token1.txt"); 
		userTokens.add(userToken1);
		Token userToken2 = new Token("tokens/token2.txt");
		userTokens.add(userToken2);
		Token userToken3 = new Token("tokens/token3.txt");
		userTokens.add(userToken3);
		Token userToken4 = new Token("tokens/token4.txt");
		userTokens.add(userToken4);
		Token userToken5 = new Token("tokens/token5.txt");
		userTokens.add(userToken5);
		Token userToken6 = new Token("tokens/token6.txt");
		userTokens.add(userToken6);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "twitter");
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
}
