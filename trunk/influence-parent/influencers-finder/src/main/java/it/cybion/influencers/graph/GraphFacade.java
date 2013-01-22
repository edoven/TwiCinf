package it.cybion.influencers.graph;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface GraphFacade {

	void addUsers(List<Long> users);
	void addFollowers(Long userId, List<Long> followersIds) throws UserVertexNotPresent;
	void addFriends(Long userId, List<Long> friendsIds) throws UserVertexNotPresent;

	int getInDegree(Long userId) throws UserVertexNotPresent, InDegreeNotSetException;
	int getOutDegree(Long userId) throws UserVertexNotPresent,OutDegreeNotSetException;	
	//int getTotalDegree(Long userId) throws UserVertexNotPresent,TotalDegreeNotSetException;
	
	/*
	 * This calculates the inDegree of the nodes usersToBeCalculated. 
	 * The edges counted are the ones starting from sourceUsers to usersToBeCalculated
	 */
	Map<Long, Integer> calculateInDegree(List<Long> usersToBeCalculated, List<Long> sourceUsers) throws UserVertexNotPresent;
	/*
	 * This calculates the outDegree of the nodes usersToBeCalculated. 
	 * The edges counted are the ones starting from usersToBeCalculated to destinationUsers
	 */
	Map<Long, Integer> calculateOutDegree(List<Long> usersToBeCalculated, List<Long> destinationUsers) throws UserVertexNotPresent;
//	/*
//	 * This calculates the totalDegree (outDegree+inDegree) of the nodes usersToBeCalculated. 
//	 */
//	void calculateTotalDegree(List<Long> usersToBeCalculated) throws UserVertexNotPresent;
//	/*
//	 * This calculates the totalDegree (outDegree+inDegree) of the nodes usersToBeCalculated.  
//	 * The edges counted are the ones starting from usersToBeCalculated to wrtUsers and
//	 * the ones starting from wrtUsers to usersToBeCalculated.
//	 */
//	void calculateTotalDegree(List<Long> usersToBeCalculated, List<Long> wrtUsers) throws UserVertexNotPresent;
	int getVerticesCount();
	void eraseGraphAndRecreate() throws IOException;

}
