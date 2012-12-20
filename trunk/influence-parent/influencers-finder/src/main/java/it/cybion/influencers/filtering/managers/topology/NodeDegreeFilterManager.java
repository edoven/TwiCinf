package it.cybion.influencers.filtering.managers.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import it.cybion.influencers.filtering.filters.topology.NodeDegreeFilter;
import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.YourCodeReallySucksException;
import it.cybion.influencers.twitter.web.Twitter4jFacadeTEST;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;


public class NodeDegreeFilterManager implements FilterManager{
	
	private static final Logger logger = Logger.getLogger(NodeDegreeFilterManager.class);
	
	private List<Long> seedUsers;
	private List<Long> usersToBeFiltered;
	private Map<Long, Integer> node2degree;
	private ExpansionDirection expansionDirection;
	private DegreeDirection degreeDirection;
	private ComparisonOption comparisonOption;
	private double percentageThreshold;
	private int absoluteThreshold;
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	
	
	public NodeDegreeFilterManager(
							ExpansionDirection expansionDirection,
							DegreeDirection degreeDirection,
							ComparisonOption comparisonOption, 
							double percentageThreshold) {
		super();
		this.expansionDirection = expansionDirection;
		this.degreeDirection = degreeDirection;
		this.comparisonOption = comparisonOption;
		this.percentageThreshold = percentageThreshold;
	}

	@Override
	public List<Long> filter() {
		solveDependencies();
		NodeDegreeFilter filter = new NodeDegreeFilter(node2degree, 
													   absoluteThreshold, 
													   comparisonOption);
		return filter.filter();
		
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade) {
		this.twitterFacade = twitterFacade;	
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		this.graphFacade = graphFacade;
	}

	@Override
	public void setSeedUsers(List<Long> seedUsers) {
		this.seedUsers = seedUsers;	
	}
	
	private void solveDependencies() {		
		absoluteThreshold = (int) (percentageThreshold * seedUsers.size());
		calculateUsersToBeFiltered();
		createGraph();
		calculateNode2degree();	
	}
	
	
	private void calculateUsersToBeFiltered() {
		switch (expansionDirection) {
			case NONE:
				usersToBeFiltered = seedUsers;			
			case FOLLOWERS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					List<Long> followersIds;
					try {
						followersIds = twitterFacade.getFollowers(userId);
						for (Long followerId : followersIds)
							usersToBeFiltered.add(followerId);
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					} catch (YourCodeReallySucksException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			case FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					List<Long> friendsIds;
					try {
						friendsIds = twitterFacade.getFriends(userId);
						for (Long friendId : friendsIds)
							usersToBeFiltered.add(friendId);
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					} catch (YourCodeReallySucksException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			case FOLLOWERS_AND_FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					List<Long> friendsIds;
					List<Long> followersIds;
					try {
						friendsIds = twitterFacade.getFriends(userId);
						for (Long friendId : friendsIds)
							usersToBeFiltered.add(friendId);
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					} catch (YourCodeReallySucksException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						followersIds = twitterFacade.getFollowers(userId);
						for (Long followerId : followersIds)
							usersToBeFiltered.add(followerId);
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					} catch (YourCodeReallySucksException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}	
				/*
				 * TODO:Remember this!!
				 * This is used to remove seed users that are
				 * follower or friend of some seed user.
				 */
				usersToBeFiltered.removeAll(seedUsers);
		}
	}
	
	public void createGraph() {
		graphFacade.addUsers(usersToBeFiltered);
		for (Long userId : usersToBeFiltered) {
			List followersIds = twitterFacade.getFollowers(userId);
			graphFacade.addFollowers(userId , followersIds);
			List friendsIds = twitterFacade.getFriends(userId);
			graphFacade.addFriends(userId , friendsIds);
		}
	}
	
	private void calculateNode2degree() {
		node2degree = new HashMap<Long, Integer>();
		
		switch (degreeDirection) {
			case IN:
											 			// fromThisGroup   toThisGroup
				graphFacade.calculateDirectedFollowsDegree(usersToBeFiltered, seedUsers);
				for (Long userId : usersToBeFiltered)
					node2degree.put(userId, graphFacade.getInDegree(userId));
			case OUT:
				 										// fromThisGroup   toThisGroup
				graphFacade.calculateDirectedFollowsDegree(seedUsers,        usersToBeFiltered);
				for (Long userId : usersToBeFiltered)
					node2degree.put(userId, graphFacade.getOutDegree(userId));
			case TOTAL:
				List<Long> totUsers = new ArrayList<Long>();
				totUsers.addAll(seedUsers);
				totUsers.addAll(usersToBeFiltered);
				graphFacade.calculateTotalFollowsDegree(totUsers);
				for (Long userId : usersToBeFiltered)
					node2degree.put(userId, graphFacade.getTotalDegree(userId));
		}
	}
	
}
