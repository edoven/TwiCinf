package it.cybion.influencers.twitter.web;


import java.util.List;

import org.testng.annotations.Test;

import twitter4j.TwitterException;
import it.cybion.influencers.twitter.web.LimitReachedForCurrentRequestException;
import it.cybion.influencers.twitter.web.MethodInputNotCorrectException;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.UserHandler;
import it.cybion.influencers.utils.TokenBuilder;



public class UserHandlerTEST
{

	@Test(enabled = true)
	public void getUsersJsonsTEST2() throws TwitterException, LimitReachedForCurrentRequestException, MethodInputNotCorrectException, InterruptedException
	{
		Token applicationToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		Token userToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt");
		UserHandler userHandler = new UserHandler(applicationToken, userToken);
		long[] ids =
		{ 11233432L, 3545344L, 1L };
		userHandler.getUsersJsons(ids);
	}

	@Test(enabled = true)
	public void getLast200Tweets() throws TwitterException, LimitReachedForCurrentRequestException, MethodInputNotCorrectException, InterruptedException
	{
		Token applicationToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		Token userToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt");
		UserHandler userHandler = new UserHandler(applicationToken, userToken);
		long userId = 92403540;
		List<String> tweets = userHandler.getLast200TweetsPostedByUser(userId);
		for (String tweet : tweets)
		{
			System.out.println(tweet);
		}
	}
}
