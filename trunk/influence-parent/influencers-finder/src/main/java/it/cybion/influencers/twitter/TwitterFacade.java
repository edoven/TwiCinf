package it.cybion.influencers.twitter;

import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.UserNotPresentException;
import it.cybion.influencers.twitter.persistance.UserNotProfileEnriched;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

import java.util.List;

public class TwitterFacade {
	
	TwitterWebFacade twitterWebFacade;
	PersistanceFacade persistanceFacade;

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

	public List<Long> getFollowers(Long userId) throws TwitterApiException {
		List<Long> followers = persistanceFacade.getFollowers(userId);
		if (followers == null) {
			List<Long> followersJsons = twitterWebFacade.getFollowersIds(userId);
			persistanceFacade.putFollowers(userId, followersJsons);
			followers = persistanceFacade.getFollowers(userId);
		}
		return followers;
	}

	public List<Long> getFriends(Long userId) throws TwitterApiException {
		List<Long> friends = persistanceFacade.getFriends(userId);
		if (friends == null) {
			List<Long> friendsJsons = twitterWebFacade.getFriendsIds(userId);
			persistanceFacade.putFriends(userId, friendsJsons);
			friends = persistanceFacade.getFriends(userId);
		}
		return friends;
	}

	
}
