package it.cybion.influence.downloader;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;


public class TwitterApiManager {
	
    private Token consumerToken;

    private Token currentUserToken;

	private RequestHandler currentRequestHandler;

    private List<Token> usableUserTokens = new ArrayList<Token>();

    private static final Logger logger = Logger.getLogger(TwitterApiManager.class);

    /*
     * Befor doing a request, getLimit() is checked, so 
     * one getFriends(String userScreenName) request needs 
     * 2 API-requests: one for limit (this call doesn't decrement
     * the limit - check API 1.1) and the real request.
     */

   
	public TwitterApiManager(Token consumerToken, List<Token> userTokens) {
		this.consumerToken = consumerToken;
		this.usableUserTokens = userTokens;
		currentUserToken = usableUserTokens.get(0);
		currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);
		logger.info(this.usableUserTokens.size()+" user tokens available.");
	}


	public List<String> getFriends(String userScreenName) throws TwitterException {
		RequestHandler requestHandler = null;
		List<String> friendsIds = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} catch (TwitterException e) {
            // TODO do not re-throw the same exception: take some decision.
            // Important thing is that the signature of this module
            // does not include exceptions thrown by Twitter4j
			throw e;
		}
		try {
			friendsIds = requestHandler.getFriendsIds(userScreenName);
		} catch (TwitterException e) {
            //TODO same as before
			throw e;
		}
		return friendsIds;
	}
	
	
	public List<String> getFollowers(String userScreenName) throws TwitterException {
		RequestHandler requestHandler = null;
		List<String> friendsIds = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} catch (TwitterException e) {
            // TODO do not re-throw the same exception: take some decision.
            // Important thing is that the signature of this module
            // does not include exceptions thrown by Twitter4j
			throw e;
		}
		try {
			friendsIds = requestHandler.getFollowersIds(userScreenName);
		} catch (TwitterException e) {
            //TODO same as before
			throw e;
		}
		return friendsIds;
	}
	

	private RequestHandler getUsableHandler() throws FinishedUsableHandlersException, TwitterException {
		try {
			int limit = currentRequestHandler.getLimit();
			logger.info("Current token limit = "+limit);
			if (limit>0)
				return currentRequestHandler;
			else {
				logger.info("Requests limit reached for current user.");
				usableUserTokens.remove(currentUserToken);
				if (usableUserTokens.size()==0) {
			        throw new FinishedUsableHandlersException();
			    }
				else {
					currentUserToken = usableUserTokens.get(0);
					currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);
					
					return currentRequestHandler;
				}
			}
		} catch (TwitterException e) {
            //TODO same as before
			throw e;
		}
			
	}


}
