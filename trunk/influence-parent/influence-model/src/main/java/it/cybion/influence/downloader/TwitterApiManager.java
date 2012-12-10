package it.cybion.influence.downloader;

import org.apache.log4j.Logger;

import twitter4j.IDs;

import twitter4j.TwitterException;

import it.cybion.influence.model.User;

import java.util.ArrayList;
import java.util.List;

/*
 * 
 * TwitterApiManager manages a pool of RequestHandlers, each one 
 * is associated with a different user token.
 * 
 */


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
    
    
    public String getRawJsonUser(String userScreenName) throws  TwitterApiException {
		RequestHandler requestHandler = null;
		String userJson = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			userJson = requestHandler.getRawJsonUser(userScreenName);
		} catch (TwitterException e) {
			 throw new TwitterApiException(e.getMessage());
		}
		return userJson;
	}
    
    
    public String getRawJsonUser(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		String userJson = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			userJson = requestHandler.getRawJsonUser(userId);
		} catch (TwitterException e) {
			 throw new TwitterApiException(e.getMessage());
		}
		return userJson;
	}

   
	public TwitterApiManager(Token consumerToken, List<Token> userTokens) {
		this.consumerToken = consumerToken;
		this.usableUserTokens = userTokens;
		currentUserToken = usableUserTokens.get(0);
		currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);
		logger.info(this.usableUserTokens.size()+" user tokens available.");
	}


	public List<String> getUpTo5000FriendsIds(String userScreenName) throws TwitterApiException {
		RequestHandler requestHandler = null;
		List<String> friendsIds = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			friendsIds = requestHandler.getUpTo5000FriendsIds(userScreenName);
		} catch (TwitterException e) {
			 throw new TwitterApiException(e.getMessage());
		}
		return friendsIds;
	}
	
	
	public List<String> getUpTo5000FollowersIds(String userScreenName) throws TwitterApiException {
		RequestHandler requestHandler = null;
		List<String> friendsIds = null;
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			friendsIds = requestHandler.getUpTo5000FollowersIds(userScreenName);
		} catch (TwitterException e) {
			 throw new TwitterApiException(e.getMessage());
		}
		return friendsIds;
	}
	
	
	//this handles multiple request for users with more than 5000 followers
	public List<String> getAllFollowersIds(String userScreenName) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<String> ids = new ArrayList<String>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			idsContainer = requestHandler.getFollowersWithPagination(userScreenName, cursor);
		} catch (TwitterException e) {
			 throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(Long.toString(id));
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("EXIT! No requests left.");
					System.exit(0);
				} 
				try {
					idsContainer = requestHandler.getFollowersWithPagination(userScreenName, cursor);
				} catch (TwitterException e) {
		            throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(Long.toString(id));
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}
	
	
	//this handles multiple request for users with more than 5000 followers
	public List<String> getAllFollowersIds(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<String> ids = new ArrayList<String>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			idsContainer = requestHandler.getFollowersWithPagination(userId, cursor);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(Long.toString(id));
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("EXIT! No requests left.");
					System.exit(0);
				} 
				try {
					idsContainer = requestHandler.getFollowersWithPagination(userId, cursor);
				} catch (TwitterException e) {
		            throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(Long.toString(id));
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}
	
	//this handles multiple request for users with more than 5000 friends
	public List<String> getAllFriendsIds(String userScreenName) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<String> ids = new ArrayList<String>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			idsContainer = requestHandler.getFriendsWithPagination(userScreenName, cursor);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(Long.toString(id));
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("EXIT! No requests left.");
					System.exit(0);
				} 
				try {
					idsContainer = requestHandler.getFriendsWithPagination(userScreenName, cursor);
				} catch (TwitterException e) {
		            throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(Long.toString(id));
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}
	
	
	//this handles multiple request for users with more than 5000 friends
	public List<String> getAllFriendsIds(long userId) throws TwitterApiException {
		RequestHandler requestHandler = null;
		IDs idsContainer = null;
		long cursor = -1;
		List<String> ids = new ArrayList<String>();
		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			idsContainer = requestHandler.getFriendsWithPagination(userId, cursor);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
		
		for (Long id : idsContainer.getIDs())
			ids.add(Long.toString(id));
		cursor = idsContainer.getNextCursor();
		
		if (cursor!=0) { //if cursor==0 this user has less than 5001 followers
			while (cursor!=0) {
				try {
					requestHandler = getUsableHandler();
				} catch (FinishedUsableHandlersException e) {
					logger.info("EXIT! No requests left.");
					System.exit(0);
				}
				try {
					idsContainer = requestHandler.getFriendsWithPagination(userId, cursor);
				} catch (TwitterException e) {
					throw new TwitterApiException(e.getMessage());
				}
				
				for (Long id : idsContainer.getIDs())
					ids.add(Long.toString(id));
				cursor = idsContainer.getNextCursor();
			}
		}
		return ids;
	}


	private RequestHandler getUsableHandler() throws FinishedUsableHandlersException, TwitterApiException {
		try {
			int limit = currentRequestHandler.getLimit();
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
					currentRequestHandler = new RequestHandlerImpl(consumerToken, currentUserToken);					
					return currentRequestHandler;
				}
			}
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}			
	}

	
	public User getEnrichedUser(String screenName) throws TwitterApiException {

		User user = null;
		RequestHandler requestHandler = null;

		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		
		try {
			user = requestHandler.getUser(screenName);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
	

		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		}

		List<String> friendsIds = getAllFriendsIds(screenName);
        List<User> friends = new ArrayList<User>();
        for (String friendId : friendsIds) {
        	User friend = new User(Long.parseLong(friendId));
        	friends.add(friend);
        }
        user.setFriends(friends);
        
        List<String> followersIds = getAllFollowersIds(screenName);
        List<User> followers = new ArrayList<User>();
        for (String followerId : followersIds) {
        	User follower = new User(Long.parseLong(followerId));
        	followers.add(follower);
        }
        user.setFollowers(followers);
		
        return user;
	}
	
	
	
	public User getEnrichedUser(long userId) throws TwitterApiException {
		User user = null;
		RequestHandler requestHandler = null;
		try {
			try {
				requestHandler = getUsableHandler();
			} catch (TwitterApiException e) {
				throw new TwitterApiException(e.getMessage());
			}
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			user = requestHandler.getUser(userId);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
	

		try {
			requestHandler = getUsableHandler();
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 

		List<String> friendsIds = getAllFriendsIds(userId);
        List<User> friends = new ArrayList<User>();
        for (String friendId : friendsIds) {
        	User friend = new User(Long.parseLong(friendId));
        	friends.add(friend);
        }
        user.setFriends(friends);
        
        List<String> followersIds = getAllFollowersIds(userId);
        List<User> followers = new ArrayList<User>();
        for (String followerId : followersIds) {
        	User follower = new User(Long.parseLong(followerId));
        	followers.add(follower);
        }
        user.setFollowers(followers);

        return user;
	}
	
	
	public User getUser(long userId) throws TwitterApiException {
		User user = null;
		RequestHandler requestHandler = null;
		try {
			try {
				requestHandler = getUsableHandler();
			} catch (TwitterApiException e) {
				throw new TwitterApiException(e.getMessage());
			}
		} catch (FinishedUsableHandlersException e) {
			logger.info("EXIT! No requests left.");
			System.exit(0);
		} 
		try {
			user = requestHandler.getUser(userId);
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
        return user;
	}

}
