package it.cybion.influencers.twitter.persistance;

import java.util.List;

public interface PersistanceFacade {

	String getDescription(Long userId) throws UserNotPresentException, UserNotProfileEnriched;
	List<Long> getFollowers(Long userId) throws  UserNotPresentException, UserNotFollowersEnrichedException;
	List<Long> getFriends(Long userId) throws UserNotFriendsEnrichedException, UserNotPresentException;
	
	void putUser(String userJson);
	void putFriends(Long userId, List<Long> friendsJsons);
	void putFollowers(Long userId, List<Long> followersJsons);
	
	void removeUser(Long userId);	
}
