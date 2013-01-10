package it.cybion.influencers.twitter.web.twitter4j;

import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.IDs;
import twitter4j.TwitterException;

public class Twitter4jFacade implements TwitterWebFacade{
	
	/*
	 * This class manages a pool of UserHandler.
	 * Each RequestHandler is associated with one user (a user is represented
	 * by a token saved in a file).
	 * Every RequestHandler is associated with the same application (an application
	 * is represented by a consumer token saved in a file).
	 */	
    
	private static final Logger logger = Logger.getLogger(Twitter4jFacade.class);   
	
    private int waitTime = 5; //time (in minutes) to wait if all user tokens have reached the request limit
    private List<UserHandler> userHandlers = new ArrayList<UserHandler>();
    

	public Twitter4jFacade(Token consumerToken, List<Token> userTokens) {
		for (Token userToken : userTokens) {
			logger.info("Creating UserHandler");
			for (int i=0; i<3; i++) { //3 tries
				try {
					UserHandler userHandler = new UserHandler( consumerToken, userToken );
					userHandlers.add(userHandler);
					break;
				} catch (TwitterException e) {
					logger.info("Can't create UserHandler for token = "+userToken+". Let's retry.");
				}
				logger.info("Can't create UserHandler for token = "+userToken+". Skipped.");
			}
		}
		logger.info("UserHandlers created");
	}
	
	@Override
	public String getUserJson(long userId) throws TwitterException {
		for (int i=0; i<userHandlers.size(); i++) {
			logger.info("Trying handler "+i+" for getUserJson");
			UserHandler requestHandler = userHandlers.get(i);
			try {
				return requestHandler.getUserJson(userId);
			} catch (LimitReachedForCurrentRequestException e) {
				logger.info("Token "+i+" has reached request limit for getUserJson");
			} 
		}
		//this point is reached if all tokens have reached the limit for this request
		try {
			logger.info("All handlers have reached the limit, let's wait for "+waitTime+" min");
			Thread.sleep(1000*60*waitTime);
			return getUserJson(userId);
		} catch (InterruptedException e1) {
			logger.info("Problem in Thread.sleep().");
			System.exit(0);
			return null;
		}	
	}
	
	@Override
	public List<Long> getFollowersIds(long userId) throws TwitterException {
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		while (cursor!=0) {
			IDs idsContainter = getFollowersIdsWithPagination(userId, cursor);			
			for (Long id : idsContainter.getIDs())
				ids.add(id);
			cursor = idsContainter.getNextCursor();
		}
		return ids;
	}
	
	private IDs getFollowersIdsWithPagination(long userId, long cursor) throws TwitterException  {
		for (int i=0; i<userHandlers.size(); i++) {
			UserHandler requestHandler = userHandlers.get(i);
			try {
				return requestHandler.getFollowersWithPagination(userId, cursor);
			} catch (LimitReachedForCurrentRequestException e) {
				logger.info("Token "+i+" has reached request limit for getFollowersIdsWithPagination");
			} 
		}
		//this point is reached if all tokens have reached the limit for this request
		try {
			logger.info("All handlers have reached the limit, let's wait for "+waitTime+" min");
			Thread.sleep(1000*60*waitTime);
			return getFollowersIdsWithPagination(userId, cursor);
		} catch (InterruptedException e1) {
			logger.info("Problem in Thread.sleep().");
			System.exit(0);
			return null;
		}	
	}
	
	@Override
	public List<Long> getFriendsIds(long userId) throws TwitterException {
		long cursor = -1;
		List<Long> ids = new ArrayList<Long>();
		while (cursor!=0) {
			IDs idsContainter = getFriendsIdsWithPagination(userId, cursor);			
			for (Long id : idsContainter.getIDs())
				ids.add(id);
			cursor = idsContainter.getNextCursor();
		}
		return ids;
	}
	
	private IDs getFriendsIdsWithPagination(long userId, long cursor) throws TwitterException  {
		for (int i=0; i<userHandlers.size(); i++) {
			UserHandler requestHandler = userHandlers.get(i);
			try {
				return requestHandler.getFriendsWithPagination(userId, cursor);
			} catch (LimitReachedForCurrentRequestException e) {
				logger.info("Token "+i+" has reached request limit for getFriendsIdsWithPagination");
			} 
		}
		//this point is reached if all tokens have reached the limit for this request
		try {
			logger.info("All handlers have reached the limit, let's wait for "+waitTime+" min");
			Thread.sleep(1000*60*waitTime);
			return getFriendsIdsWithPagination(userId, cursor);
		} catch (InterruptedException e1) {
			logger.info("Problem in Thread.sleep().");
			System.exit(0);
			return null;
		}	
	}


}
