package it.cybion.influencers.twitter;


import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



public class TwitterFacadeFactory
{

	private static String mongoDbHost = "localhost";
	private static String mongoDbTwitterDb = "twitter";
	private static String mongoDbTestDb = "testDb";

	private static String applicationTokenPath = "tokens/consumerToken.txt";
	private static String[] tokens =
	{ "tokens/token1.txt", "tokens/token2.txt", "tokens/token3.txt", "tokens/token4.txt", "tokens/token5.txt", "tokens/token6.txt" };

	public static TwitterFacade getTwitterFacade() throws UnknownHostException
	{
		Token applicationToken = new Token(applicationTokenPath);
		List<Token> userTokens = new ArrayList<Token>();
		for (int i = 0; i < tokens.length; i++)
			userTokens.add(new Token(tokens[i]));
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade(mongoDbHost, mongoDbTwitterDb);
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}

	public static TwitterFacade getTwitterFacadeForTests() throws UnknownHostException
	{
		Token applicationToken = new Token(applicationTokenPath);
		List<Token> userTokens = new ArrayList<Token>();
		for (int i = 0; i < tokens.length; i++)
			userTokens.add(new Token(tokens[i]));
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade(mongoDbHost, mongoDbTestDb);
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
}
