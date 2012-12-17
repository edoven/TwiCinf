package it.cybion.influencers.twitter.web.twitter4j;

import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.IDs;
import twitter4j.TwitterException;

public class Twitter4jFacade implements TwitterWebFacade{
	
	private Token consumerToken;
    private Token currentUserToken;
	private RequestHandler currentRequestHandler;
    private List<Token> usableUserTokens = new ArrayList<Token>();
    private static final Logger logger = Logger.getLogger(Twitter4jFacade.class);
    //this is the time (in minutes) to wait if all user tokens have reached the request limit
    private int waitTime; 
    

	public Twitter4jFacade(Token consumerToken, List<Token> userTokens, int waitTime) {
		this.consumerToken = consumerToken;
		this.usableUserTokens = userTokens;
		this.waitTime = waitTime;
		currentUserToken = usableUserTokens.get(0);
		currentRequestHandler = new RequestHandler(consumerToken, currentUserToken);
		logger.info(this.usableUserTokens.size()+" user tokens available.");
	}
	
	private RequestHandler getUsableHandler() throws FinishedUsableHandlersException {
		int limit;
		try {
			limit = currentRequestHandler.getLimit();
		} catch (TwitterApiException e) {
			logger.info("Problem while asking the request limit for current user token, changing user.");
			usableUserTokens.remove(currentUserToken);
			if (usableUserTokens.size()==0) {
		        throw new FinishedUsableHandlersException("No more user tokens with available requests.");
		    }
			else {
				currentUserToken = usableUserTokens.get(0);
				currentRequestHandler = new RequestHandler(consumerToken, currentUserToken);					
				return currentRequestHandler;
			}
		}
		logger.info("Current token limit = "+limit);
		if (limit>0)
			return currentRequestHandler;
		else {
			logger.info("Requests limit reached for current user.");
			usableUserTokens.remove(currentUserToken);
			if (usableUserTokens.size()==0) {
		        throw new FinishedUsableHandlersException("No more user tokens with available requests.");
		    }
			else {
				currentUserToken = usableUserTokens.get(0);
				currentRequestHandler = new RequestHandler(consumerToken, currentUserToken);					
				return currentRequestHandler;
			}
		}
	}
	
	@Override
	public String getUserJson(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("Limit reached for all user tokend. Waiting for "+waitTime+" minutes before doing the request.");
			try {
				Thread.sleep(1000*60*waitTime);
			} catch (InterruptedException e1) {
				logger.info("Problem in Thread.sleep().");
				System.exit(0);
			}
			return getUserJson(userId);
		} 
		try {
			return requestHandler.getUserJson(userId);
		} catch (TwitterApiException e) {
			throw e;
		}
	}

	
	public List<Long> getFollowersIds(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("Limit reached for all user tokend. Waiting for "+waitTime+" minutes before doing the request.");
			try {
				Thread.sleep(1000*60*waitTime);
			} catch (InterruptedException e1) {
				logger.info("Problem in Thread.sleep().");
				System.exit(0);
			}
			return getFollowersIds(userId);
		} 
		try {
			idsContainer = requestHandler.getFollowersWithPagination(userId, cursor);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(id);
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("Limit reached for all user tokend. Waiting for "+waitTime+" minutes before doing the request.");
					try {
						Thread.sleep(1000*60*waitTime);
					} catch (InterruptedException e1) {
						logger.info("Problem in Thread.sleep().");
						System.exit(0);
					}
					return getFollowersIds(userId);
				} 
				try {
					idsContainer = requestHandler.getFollowersWithPagination(userId, cursor);
				} catch (TwitterException e) {
		            throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(id);
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}
	
	

	public List<Long> getFriendsIds(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("Limit reached for all user tokend. Waiting for "+waitTime+" minutes before doing the request.");
			try {
				Thread.sleep(1000*60*waitTime);
			} catch (InterruptedException e1) {
				logger.info("Problem in Thread.sleep().");
				System.exit(0);
			}
			return getFriendsIds(userId);
		} 
		try {
			idsContainer = requestHandler.getFriendsWithPagination(userId, cursor);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(id);
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("Limit reached for all user tokend. Waiting for "+waitTime+" minutes before doing the request.");
					try {
						Thread.sleep(1000*60*waitTime);
					} catch (InterruptedException e1) {
						logger.info("Problem in Thread.sleep().");
						System.exit(0);
					}
					return getFriendsIds(userId);
				} 
				try {
					idsContainer = requestHandler.getFriendsWithPagination(userId, cursor);
				} catch (TwitterException e) {
		            throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(id);
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}

}
