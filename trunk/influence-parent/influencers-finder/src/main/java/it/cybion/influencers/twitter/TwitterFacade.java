package it.cybion.influencers.twitter;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotFollowersEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotFriendsEnrichedException;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

import java.util.List;

/*
 * I'm using recursive methods!
 * They are less "performance-oriented"...but so elegant. :)  
 */

public class TwitterFacade {
	
	TwitterWebFacade twitterWebFacade;
	PersistanceFacade persistanceFacade;
	
	
	public TwitterFacade(TwitterWebFacade twitterWebFacade,	PersistanceFacade persistanceFacade) {
		this.twitterWebFacade = twitterWebFacade;
		this.persistanceFacade = persistanceFacade;
	}
	
	
	
	public String getUser(Long userId) throws TwitterApiException  {
		try {
			return persistanceFacade.getUser(userId);				
		} catch (UserNotPresentException e) {
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getUser(userId);
		}
	}
	
	
	public String getDescription(Long userId) throws TwitterApiException  {
		try {
			return persistanceFacade.getDescription(userId);				
		} catch (UserNotPresentException e) {
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
		catch (UserNotProfileEnriched e) {
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}	
	}

	public List<Long> getFollowers(Long userId) throws TwitterApiException, YourCodeReallySucksException {
		try {
			return persistanceFacade.getFollowers(userId);
		} catch (UserNotPresentException e) {
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFollowers(userId); 
		} catch (UserNotFollowersEnrichedException e) {
			List<Long> followers = twitterWebFacade.getFollowersIds(userId);
			try {
				persistanceFacade.putFollowers(userId, followers);
			} catch (UserNotPresentException e1) {
				throw new YourCodeReallySucksException("User with id "+userId+" can't be added to the caching system.");
			}
			return getFollowers(userId);
		}
	}

	public List<Long> getFriends(Long userId) throws TwitterApiException, YourCodeReallySucksException {
		try {
			return persistanceFacade.getFriends(userId);
		} catch (UserNotPresentException e) {
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFriends(userId); 
		} catch (UserNotFriendsEnrichedException e) {
			List<Long> friends = twitterWebFacade.getFriendsIds(userId);
			try {
				persistanceFacade.putFollowers(userId, friends);
			} catch (UserNotPresentException e1) {
				throw new YourCodeReallySucksException("User with id "+userId+" can't be added to the caching system.");
			}
			return getFriends(userId);
		}
	}

	
}
