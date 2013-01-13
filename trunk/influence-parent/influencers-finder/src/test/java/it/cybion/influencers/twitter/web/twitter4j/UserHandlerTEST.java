package it.cybion.influencers.twitter.web.twitter4j;

import org.testng.annotations.Test;

import twitter4j.TwitterException;
import it.cybion.influencers.utils.TokenBuilder;

public class UserHandlerTEST {

	@Test (enabled=true)
	public void getUsersJsonsTEST2() throws TwitterException, LimitReachedForCurrentRequestException, MethodInputNotCorrectException, InterruptedException {
		Token applicationToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		Token userToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt");
		UserHandler userHandler = new UserHandler(applicationToken, userToken);

		
		long[] ids = {11233432L, 3545344L, 1L};		
		userHandler.getUsersJsons(ids);				
	}
}
