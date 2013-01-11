package it.cybion.influencers.twitter.web.twitter4j;

import org.testng.annotations.Test;

import twitter4j.TwitterException;
import it.cybion.influencers.utils.TokenBuilder;

public class UserHandlerTEST {

	@Test
	public void getUsersJsonsTEST() throws TwitterException, LimitReachedForCurrentRequestException, MethodInputNotCorrectException {
		Token applicationToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		Token userToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt");
		UserHandler userHandler = new UserHandler(applicationToken, userToken);
		 
		//userHandler.getUserJson(11233432L);
		userHandler.getTwitter().showUser(11233432L);
		
		long[] ids = {11233432L, 3545344L, 1L};		
		userHandler.getTwitter().lookupUsers(ids);
		//userHandler.getUsersJsons(ids);		
	}

}
