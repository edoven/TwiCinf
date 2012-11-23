package it.cybion.influence.downloader;

import it.cybion.influence.util.TokenBuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class TwitterApiManager {
	
	private static final Logger logger = Logger.getLogger(TwitterApiManager.class);
	
	private List<Token> userTokens = new ArrayList<Token>();
	private Token consumerToken;
	
	private RequestHandler currentRequestHandler;
	private Token currentUserToken;

	
	public TwitterApiManager(String consumerTokenFilePath, List<String> userTokenFilePaths) {
		this.consumerToken = TokenBuilder.getTokenFromFile(consumerTokenFilePath);
		for (String userTokenFilePath : userTokenFilePaths)
			addUserTokenToPool(userTokenFilePath);
		currentUserToken = userTokens.get(0);
		currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);		
	}

	public List<String> getFriends(String userScreenName) {
		//List<String> friendsIds = new ArrayList<String>();
		RequestHandler requestHandler = getUsableHandler();
		if (requestHandler==null) {
			logger.info("EXIT! No requests left!");
			System.exit(0);
		}
		return requestHandler.getFriendsIds(userScreenName);
	}
	
	//TODO: this can be written in a more elegant way
	private RequestHandler getUsableHandler() {
		if (currentRequestHandler.getLimit()>0)
			return currentRequestHandler;
		else {
			userTokens.remove(currentUserToken);
			if (userTokens.size()==0)
				return null;
			else {
				currentUserToken = userTokens.get(0);
				currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);
				return currentRequestHandler;
			}
		}
			
	}
	
	private void addUserTokenToPool(String filePath) {
		Token userToken = TokenBuilder.getTokenFromFile(filePath);
		userTokens.add(userToken);
	}
	

}
