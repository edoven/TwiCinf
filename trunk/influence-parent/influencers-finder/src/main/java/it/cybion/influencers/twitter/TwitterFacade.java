package it.cybion.influencers.twitter;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

import java.util.List;

import org.apache.log4j.Logger;

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
			
	public String getUser(Long userId) throws TwitterApiException  {
		try {
			String user = persistanceFacade.getUser(userId);
			logger.info("User with id "+userId+" is in the cache. Let's fetch it!");
			return user;				
		} catch (UserNotPresentException e) {
			logger.info("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getUser(userId);
		}
	}
		
	public String getDescription(Long userId) throws TwitterApiException  {
		try {
			String description = persistanceFacade.getDescription(userId);
			logger.info("User with id "+userId+" is in the cache and has profile informations. Let's fetch it!");
			return description;				
		} catch (UserNotPresentException e) {
			logger.info("User with id "+userId+" not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
		catch (UserNotProfileEnriched e) {
			logger.info("User with id "+userId+" has no profile informations. Let's donwload them!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}	
	}

	public List<Long> getFollowers(Long userId) throws TwitterApiException {
		try {
			return persistanceFacade.getFollowers(userId);
		} catch (UserNotPresentException e) {
			logger.info("User with id="+userId+" is not in the cache. It has to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFollowers(userId); 
		} catch (UserNotFollowersEnrichedException e) {
			logger.info("User with id="+userId+" is in the cache but not followers-enriched. Followers have to be downloaded.");
			List<Long> followers = twitterWebFacade.getFollowersIds(userId);
			try {
				persistanceFacade.putFollowers(userId, followers);
			} catch (UserNotPresentException e1) {
				logger.info("User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			}
			return getFollowers(userId);
		}
	}

	public List<Long> getFriends(Long userId) throws TwitterApiException {
		try {
			return persistanceFacade.getFriends(userId);
		} catch (UserNotPresentException e) {
			logger.info("User with id="+userId+" is not in the cache. It has to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFriends(userId); 
		} catch (UserNotFriendsEnrichedException e) {
			logger.info("User with id="+userId+" is in the cache but not friends-enriched. Friends have to be downloaded.");
			List<Long> friends = twitterWebFacade.getFriendsIds(userId);
			try {
				persistanceFacade.putFriends(userId, friends);
			} catch (UserNotPresentException e1) {
				logger.info("User with id "+userId+" can't be added to caching system.");
				System.exit(0);
			}
			return getFriends(userId);
		}
	}
	
}
