package it.cybion.influencers.cache.web;


import it.cybion.influencers.cache.web.exceptions.LimitReachedForCurrentRequestException;
import it.cybion.influencers.cache.web.exceptions.MethodInputNotCorrectException;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.IDs;
import twitter4j.TwitterException;


/*
 * This class manages a pool of UserHandler. Each RequestHandler is
 * associated with one user (a user is represented by a token saved in a
 * file). Every RequestHandler is associated with the same application (an
 * application is represented by a consumer token saved in a file).
 */



public class UserHandlersManager
{
	private static final Logger logger = Logger.getLogger(UserHandlersManager.class);
	
	// time (in minutes) to wait if all user tokens have reached the request limit
	private final int WAIT_TIME = 1; 
	private List<UserHandler> userHandlers = new ArrayList<UserHandler>();
	
	public UserHandlersManager(Token consumerToken, List<Token> userTokens)
	{
		for (Token userToken : userTokens)
		{
			logger.info("Creating UserHandler");
			for (int i = 0; i < 3; i++)
			{ // 3 tries
				try
				{
					UserHandler userHandler = new UserHandler(consumerToken, userToken);
					userHandlers.add(userHandler);
					break;
				} catch (TwitterException e)
				{
					logger.info("Can't create UserHandler for token = " + userToken + ". Let's wait 1 min and then retry.");
					try
					{
						Thread.sleep(60 * 1000);
					} catch (InterruptedException e1)
					{
						logger.info("Problem in Thread.sleep");
					}
				}			
				logger.info("Can't create UserHandler for token = " + userToken + ". Skipped.");
			}
		}
		logger.info("UserHandlers created");
	}
	
	
	// Factorized method to execute a single request
	public Object executeRequest(RequestName requestName, List<Object> requestParameters) throws TwitterException, ProtectedUserException
	{
		for (int i = 0; i < userHandlers.size(); i++)
		{
			UserHandler userHandler = userHandlers.get(i);
			try
			{
				switch (requestName)
				{
				case GET_UP_TO_100_USERS:
				{
					long[] usersIds = (long[]) requestParameters.get(0);
					List<String> usersJsons = userHandler.getUsersJsons(usersIds);
					return usersJsons;
				}
				case GET_FRIENDS_IDS_WITH_PAGINATION:
				{
					long userId = (Long) requestParameters.get(0);
					long cursor = (Long) requestParameters.get(1);
					IDs result = userHandler.getFriendsWithPagination(userId, cursor);
					return result;
				}
				case GET_FOLLOWERS_IDS_WITH_PAGINATION:
				{
					long userId = (Long) requestParameters.get(0);
					long cursor = (Long) requestParameters.get(1);
					IDs result = userHandler.getFollowersWithPagination(userId, cursor);
					return result;
				}
				case GET_USER_JSON_FROM_ID:
				{
					long userId = (Long) requestParameters.get(0);
					String result = userHandler.getUserJson(userId);
					return result;
				}
				case GET_USER_JSON_FROM_SCREENNAME:
				{
					String screenName = (String) requestParameters.get(0);
					String result = userHandler.getUserJson(screenName);
					return result;
				}
				case GET_USER_TWEETS_WITH_MAX_ID:
				{
					long userId = (Long) requestParameters.get(0);
					long maxId = (Long) requestParameters.get(1);
					List<String> result = userHandler.getTweetsWithMaxId(userId,maxId);
					return result;
				}
				}
			} catch (LimitReachedForCurrentRequestException e)
			{
				logger.debug("Token " + i + " has reached request limit for " + requestName);
				logger.debug("Limits = " + e.getLimits());
			} catch (MethodInputNotCorrectException e)
			{
				logger.info("ERROR: " + e.getStackTrace());
				System.exit(0);
			}
		}
		// this point is reached if all tokens have reached the limit for this request
		try
		{
			logger.info("All handlers have reached the limit, let's wait for " + WAIT_TIME + " min");
			Thread.sleep(WAIT_TIME * 60 * 1000);
			return executeRequest(requestName, requestParameters);
		} catch (InterruptedException e1)
		{
			logger.info("Problem in Thread.sleep().");
			System.exit(0);
			return null;
		}
	}
}
