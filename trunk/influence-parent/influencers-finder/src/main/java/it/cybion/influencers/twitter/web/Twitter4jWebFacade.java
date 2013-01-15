package it.cybion.influencers.twitter.web;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.IDs;
import twitter4j.TwitterException;

public class Twitter4jWebFacade implements TwitterWebFacade{
	
	/*
	 * This class manages a pool of UserHandler.
	 * Each RequestHandler is associated with one user (a user is represented
	 * by a token saved in a file).
	 * Every RequestHandler is associated with the same application (an application
	 * is represented by a consumer token saved in a file).
	 */	
    
	private static final Logger logger = Logger.getLogger(Twitter4jWebFacade.class);   
	
	private final int WAIT_TIME = 1; //time (in minutes) to wait if all user tokens have reached the request limit
	private int lastUsedHandlerIndex = 0; //this is user for round-robin policy in handlers usage
	
	private enum RequestName {
		GET_UP_TO_100_USERS,
		GET_FRIENDS_IDS_WITH_PAGINATION, 
		GET_FOLLOWERS_IDS_WITH_PAGINATION, 
		GET_USER_JSON
	}
   
	private List<UserHandler> userHandlers = new ArrayList<UserHandler>();    

	public Twitter4jWebFacade(Token consumerToken, List<Token> userTokens) {
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
	

	//Factorized method to execute a single request
	private Object executeRequest(RequestName requestName, List<Object> requestParameters) throws MethodInputNotCorrectException, TwitterException {
		Object requestResult = null;
		int currentUserHandlerIndex = lastUsedHandlerIndex;
		for (int i=0; i<userHandlers.size(); i++) {
			currentUserHandlerIndex = (currentUserHandlerIndex+1) % userHandlers.size();
			logger.info("using handler ="+currentUserHandlerIndex);
			UserHandler userHandler = userHandlers.get(currentUserHandlerIndex);			
			try {
				switch (requestName) {
					case GET_UP_TO_100_USERS: {
						long[] usersIds = (long[])requestParameters.get(0);
						List<String> usersJsons =  userHandler.getUsersJsons(usersIds);
						requestResult =  usersJsons;
						break;
					}
					case GET_FRIENDS_IDS_WITH_PAGINATION: {
						long userId = (Long) requestParameters.get(0);
						long cursor = (Long) requestParameters.get(1);
						IDs result = userHandler.getFriendsWithPagination(userId, cursor);
						requestResult = result;
						break;
					}
					case GET_FOLLOWERS_IDS_WITH_PAGINATION: {
						long userId = (Long) requestParameters.get(0);
						long cursor = (Long) requestParameters.get(1);
						IDs result = userHandler.getFollowersWithPagination(userId, cursor);
						requestResult = result;
						break;
					}
					case GET_USER_JSON: {
						long userId = (Long) requestParameters.get(0);
						String result = userHandler.getUserJson(userId);
						requestResult = result;
						break;
					}
				}
			} catch (LimitReachedForCurrentRequestException e) {
				logger.debug("Token "+i+" has reached request limit for "+requestName);
				logger.info(e.getLimits());
			} 
		}
		if (requestResult == null) {
			//this point is reached if all tokens have reached the limit for this request
			try {
				logger.info("All handlers have reached the limit, let's wait for "+WAIT_TIME+" min");
				Thread.sleep(WAIT_TIME*60*1000);
				return executeRequest(requestName, requestParameters);
			} catch (InterruptedException e1) {
				logger.info("Problem in Thread.sleep().");
				System.exit(0);
				return null;
			}	
		}
		lastUsedHandlerIndex = currentUserHandlerIndex;
		logger.debug("requestResult="+requestResult);
		return requestResult;		
	}
	
	@Override
	public String getUserJson(long userId) throws TwitterException {
		RequestName requestName = RequestName.GET_USER_JSON;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);			
		try {
			return (String) executeRequest(requestName, requestParameters);
		} catch (MethodInputNotCorrectException e) {
			// this exception is never thrown
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
		RequestName requestName = RequestName.GET_FOLLOWERS_IDS_WITH_PAGINATION;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);		
		requestParameters.add(1, cursor);	
		try {
			return (IDs) executeRequest(requestName, requestParameters);
		} catch (MethodInputNotCorrectException e) {
			// this exception is never thrown
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
		RequestName requestName = RequestName.GET_FRIENDS_IDS_WITH_PAGINATION;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, userId);		
		requestParameters.add(1, cursor);	
		try {
			return (IDs) executeRequest(requestName, requestParameters);
		} catch (MethodInputNotCorrectException e) {
			// this exception is never thrown
			System.exit(0);
			return null;
		}
	}
	
	@Override
	public List<String> getUsersJsons(List<Long> usersIds) {
		List<String> usersJsons = new ArrayList<String>();
		int listSize = usersIds.size();
		logger.info("listSize="+listSize);
		int chunkSize = 100;	
		int remainder = (listSize%chunkSize);		
		int chunksCount = listSize / chunkSize;			
		if (remainder>0)
			chunksCount++;
		for (int i=0; i<chunksCount; i++) {
			long[] chunk = getChunk(usersIds, 100, i);
			logger.info("chunk.length="+chunk.length);
			try {
				List<String> chunkResult = getUpTo100Users(chunk);				
				usersJsons.addAll(chunkResult);
			} catch (MethodInputNotCorrectException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (TwitterException e) {
				logger.info("Problem with chunk, skipped. Chunk = "+chunk);
			}
		}			
		return usersJsons;
	}
	
	private long[] getChunk(List<Long> list, int chunkSize, int chunkIndex) {
		int firstElementIndex = chunkIndex*chunkSize;
		logger.info("firstElementIndex="+firstElementIndex);
		int lastElementIndex = chunkIndex*chunkSize + chunkSize;
		logger.info("lastElementIndex="+lastElementIndex);
		if (lastElementIndex > list.size()) {
			lastElementIndex = list.size();
			chunkSize = lastElementIndex-firstElementIndex;
		}
		logger.info("end="+lastElementIndex);
		List<Long> chunkList = list.subList(firstElementIndex, lastElementIndex);
		long chunkArray[] = new long[chunkList.size()];
		for (int i=0; i<chunkSize; i++)
			chunkArray[i] = chunkList.get(i);
		return chunkArray;
	}
	
	private List<String> getUpTo100Users(long[] usersIds) throws TwitterException, MethodInputNotCorrectException  {
		RequestName requestname = RequestName.GET_UP_TO_100_USERS;
		List<Object> requestParameters = new ArrayList<Object>();
		requestParameters.add(0, usersIds);		
		return (List<String>) executeRequest(requestname, requestParameters);
	}

}
