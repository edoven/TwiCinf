package it.cybion.influence.downloader;

import it.cybion.influence.util.TokenBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class TwitterApiManager {
	
	private static final Logger logger = Logger.getLogger(TwitterApiManager.class);
	
	private List<Token> usableUserTokens = new ArrayList<Token>();
	private Token consumerToken;
	
	private RequestHandler currentRequestHandler;
	private Token currentUserToken;


    //TODO remove this constructor, change with a (Token consumerToken, List<Token> usableUserTokens)
	public TwitterApiManager(String consumerTokenFilePath, List<String> userTokenFilePaths) {
		this.consumerToken = TokenBuilder.getTokenFromFile(consumerTokenFilePath);
		for (String userTokenFilePath : userTokenFilePaths)
			addUserTokenToPool(userTokenFilePath);
		currentUserToken = usableUserTokens.get(0);
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
    //TODO: do not return null, ever: throw an FinishedUsableHandlersException instead
	private RequestHandler getUsableHandler() {
		if (currentRequestHandler.getLimit()>0)
			return currentRequestHandler;
		else {
			usableUserTokens.remove(currentUserToken);
			if (usableUserTokens.size()==0) {
                //TODO here, throw an exception!
				return null;
            }
			else {
				currentUserToken = usableUserTokens.get(0);
				currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);
				return currentRequestHandler;
			}
		}
			
	}
	
	private void addUserTokenToPool(String filePath) {
        //TODO this logic will be done before constructing the TAM
		Token userToken = TokenBuilder.getTokenFromFile(filePath);
		usableUserTokens.add(userToken);
	}
	

}
