package it.cybion.influence.ranking;


import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.persistance.implementations.mongodb.MongodbPersistanceFacade;
import it.cybion.influencers.cache.web.TwitterWebFacade;
import it.cybion.influencers.cache.web.implementations.twitter4j.Token;
import it.cybion.influencers.cache.web.implementations.twitter4j.Twitter4jWebFacade;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



public class TwitterFacadeFactory
{

	private static String mongoDbHost = "localhost";
	private static String mongoDbTwitterDb = "twitter";
	private static String mongoDbTestDb = "testDb";

	private static String applicationTokenPath = "/home/godzy/tokens/consumerToken.properties";
	private static String[] tokens =
	{ "/home/godzy/tokens/token0.properties", "/home/godzy/tokens/token1.properties", 
	  "/home/godzy/tokens/token2.properties", "/home/godzy/tokens/token3.properties", 
      "/home/godzy/tokens/token4.properties", "/home/godzy/tokens/token5.properties" };

	public static TwitterCache getTwitterFacade() throws UnknownHostException
	{
		Token applicationToken = new Token(applicationTokenPath);
		List<Token> userTokens = new ArrayList<Token>();
		for (int i = 0; i < tokens.length; i++)
			userTokens.add(new Token(tokens[i]));
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade(mongoDbHost, mongoDbTwitterDb);
		TwitterCache twitterFacade = new TwitterCache(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}

	public static TwitterCache getTwitterFacadeForTests() throws UnknownHostException
	{
		Token applicationToken = new Token(applicationTokenPath);
		List<Token> userTokens = new ArrayList<Token>();
		for (int i = 0; i < tokens.length; i++)
			userTokens.add(new Token(tokens[i]));
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade(mongoDbHost, mongoDbTestDb);
		TwitterCache twitterFacade = new TwitterCache(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
}
