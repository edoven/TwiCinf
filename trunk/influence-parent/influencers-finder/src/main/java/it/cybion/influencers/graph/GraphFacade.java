package it.cybion.influencers.graph;

import java.util.List;


public interface GraphFacade {

	void addUsers(List<Long> usersToBeFiltered);
	void addFollowers(Long userId, List<Long> followersIds) throws UserVertexNotPresent;
	void addFriends(Long userId, List<Long> friendsIds) throws UserVertexNotPresent;

	void calculateDirectedFollowsDegree(List<Long> fromUsers, List<Long> toUsers);	
	void calculateTotalFollowsDegree(List<Long> totUsers);

	int getInDegree(Long userId);
	int getOutDegree(Long userId);	
	int getTotalDegree(Long userId);

}
