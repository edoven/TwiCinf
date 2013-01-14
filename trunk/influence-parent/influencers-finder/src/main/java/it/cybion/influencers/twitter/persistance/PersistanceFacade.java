package it.cybion.influencers.twitter.persistance;

import java.util.List;

public interface PersistanceFacade {

	String getUser(Long userId) throws UserNotPresentException;
	String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnriched;
	String getScreenName(Long userId) throws UserNotPresentException,			UserNotProfileEnriched;
	List<Long> getFollowers(Long userId) throws  UserNotPresentException, UserNotFollowersEnrichedException;
	List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException;
	
	void putUser(String userJson);
	void putFriends(Long userId, List<Long> friendsJsons) throws UserNotPresentException;
	void putFollowers(Long userId, List<Long> followersJsons) throws UserNotPresentException;
	
	void removeUser(Long userId);
	int getFollowersCount(Long userId) throws UserNotPresentException, UserNotProfileEnriched;
	int getFriendsCount(Long userId) throws UserNotPresentException, UserNotProfileEnriched;
	
		
}
