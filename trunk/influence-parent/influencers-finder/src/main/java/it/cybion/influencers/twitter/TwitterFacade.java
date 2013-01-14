package it.cybion.influencers.twitter;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import twitter4j.TwitterException;

/*
 * I'm using recursive methods!
 * They are less "performance-oriented"...but so elegant. :)  
 */

public class TwitterFacade {
	
	private static final Logger logger = Logger.getLogger(TwitterFacade.class);
	
	TwitterWebFacade twitterWebFacade;
	PersistanceFacade persistanceFacade;
	
	
	public TwitterFacade(TwitterWebFacade twitterWebFacade,	PersistanceFacade persistanceFacade) {
		this.twitterWebFacade = twitterWebFacade;
		this.persistanceFacade = persistanceFacade;
	}
			
	public String getUser(Long userId) throws TwitterException  {
		try {
			String user = persistanceFacade.getUser(userId);
			logger.debug("User with id "+userId+" is in the cache. Let's fetch it!");
			return user;				
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getUser(userId);
		}
	}
		
	public String getDescription(Long userId) throws TwitterException  {
		try {
			String description = persistanceFacade.getDescription(userId);
			logger.debug("User with id "+userId+" is in the cache and has profile informations. Let's fetch it!");
			return description;				
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
		catch (UserNotProfileEnriched e) {
			logger.debug("User with id "+userId+" has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}	
	}
	
	public Map<Long,String> getDescriptions(List<Long> userIds) {
		Map<Long,String> userToDescription = new HashMap<Long,String>();
		List<Long> usersToDownload = new ArrayList<Long>();		
		for (Long userId : userIds) {			
			try {
				String description = persistanceFacade.getDescription(userId);
				userToDescription.put(userId, description);
			} catch (UserNotPresentException e) {
				usersToDownload.add(userId);
			} catch (UserNotProfileEnriched e) {
				usersToDownload.add(userId);
			}			
		}		
		List<String> downloadedUsersJsons = twitterWebFacade.getUsersJsons(usersToDownload);
		for (String userJson : downloadedUsersJsons)
			persistanceFacade.putUser(userJson);
		for (String donwloadedUserJson : downloadedUsersJsons) {
			/*
			 * It's not good to use mongDb object (DBObject) to extract
			 * the id from the user json string!
			 * TODO: use another way of doing this!
			 */
			DBObject downloadedUserObject = (DBObject) JSON.parse(donwloadedUserJson);
			long userId = new Long ((Integer) downloadedUserObject.get("id"));
			try {				
				String description = persistanceFacade.getDescription(userId);
				userToDescription.put(userId, description);
			} catch (UserNotPresentException e) {
				logger.info("ERROR! User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			} catch (UserNotProfileEnriched e) {
				logger.info("ERROR! User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			}
		}
		return userToDescription;
	}
	
	public String getScreenName(Long userId) throws TwitterException  {
		try {
			String description = persistanceFacade.getScreenName(userId);
			logger.debug("User with id "+userId+" is in the cache and has profile informations. Let's fetch it!");
			return description;				
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
		catch (UserNotProfileEnriched e) {
			logger.debug("User with id "+userId+" has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}	
	}
	
	public int getFollowersCount(Long userId) throws TwitterException  {
		try {
			int followersCount = persistanceFacade.getFollowersCount(userId);
			logger.debug("User with id "+userId+" is in the cache and has profile informations. Let's fetch it!");
			return followersCount;				
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFollowersCount(userId);
		}
		catch (UserNotProfileEnriched e) {
			logger.debug("User with id "+userId+" has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFollowersCount(userId);
		}	
	}
	
	public int getFriendsCount(Long userId) throws TwitterException  {
		try {
			int friendsCount = persistanceFacade.getFriendsCount(userId);
			logger.debug("User with id "+userId+" is in the cache and has profile informations. Let's fetch it!");
			return friendsCount;				
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFriendsCount(userId);
		}
		catch (UserNotProfileEnriched e) {
			logger.debug("User with id "+userId+" has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFriendsCount(userId);
		}	
	}

	
	public List<Long> getFollowers(Long userId) throws TwitterException {
		List<Long> followers;
		try {
			followers = persistanceFacade.getFollowers(userId);
			logger.debug("User with id "+userId+" is already followers enriched. Let's fetch it from the cache.");
			return followers;
		} catch (UserNotPresentException e) {
			logger.debug("User with id "+userId+" is not in the cache. It needs to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFollowers(userId);
		} catch (UserNotFollowersEnrichedException e) {
			logger.debug("User with id="+userId+" is in the cache but not followers-enriched. Followers have to be downloaded.");
			followers = twitterWebFacade.getFollowersIds(userId);
			try {
				persistanceFacade.putFollowers(userId, followers);
			} catch (UserNotPresentException e1) {
				logger.info("ERROR! User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			}
			return getFollowers(userId);
		}
	}

	public List<Long> getFriends(Long userId) throws TwitterException {
		List<Long> friends;
		try {
			friends = persistanceFacade.getFriends(userId);
			logger.debug("User with id "+userId+" is already friends enriched. Let's fetch it from the cache.");
			return friends;
		} catch (UserNotPresentException e) {
			logger.debug("User with id="+userId+" is not in the cache. It has to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFriends(userId); 
		} catch (UserNotFriendsEnrichedException e) {
			logger.debug("User with id="+userId+" is in the cache but not friends-enriched. Friends have to be downloaded.");
			friends = twitterWebFacade.getFriendsIds(userId);
			try {
				persistanceFacade.putFriends(userId, friends);
			} catch (UserNotPresentException e1) {
				logger.debug("ERROR! User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			}		
			return getFriends(userId);
		}
	}
	
}
