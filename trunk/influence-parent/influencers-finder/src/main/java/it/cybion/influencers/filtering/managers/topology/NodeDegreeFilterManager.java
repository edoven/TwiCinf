package it.cybion.influencers.filtering.managers.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.cybion.influencers.filtering.filters.topology.NodeDegreeFilter;
import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.InDegreeNotSetException;
import it.cybion.influencers.graph.OutDegreeNotSetException;
import it.cybion.influencers.graph.UserVertexNotPresent;
import it.cybion.influencers.twitter.TwitterFacade;
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
		absoluteThreshold = (int) Math.round((percentageThreshold * seedUsers.size()));
		calculateUsersToBeFiltered();
		createGraph();
		try {
			calculateNode2degree();
		} catch (UserVertexNotPresent e) {
			e.printStackTrace();
			System.exit(0);
		} catch (InDegreeNotSetException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (OutDegreeNotSetException e) {
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	
	private void calculateUsersToBeFiltered() {
		switch (expansionDirection) {
			case SEEDS:
				usersToBeFiltered = seedUsers;			
			case FOLLOWERS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
				}
				usersToBeFiltered.removeAll(seedUsers);
			case FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.addAll(twitterFacade.getFriends(userId));
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
				}
				usersToBeFiltered.removeAll(seedUsers);
			case FOLLOWERS_AND_FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.addAll(twitterFacade.getFriends(userId));
						usersToBeFiltered.addAll(twitterFacade.getFollowers(userId));						
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
					
				}	
				usersToBeFiltered.removeAll(seedUsers);				
			case SEEDS_AND_FOLLOWERS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.add(userId);
						usersToBeFiltered.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
				}
			case SEEDS_AND_FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.add(userId);
						usersToBeFiltered.addAll(twitterFacade.getFriends(userId));
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
				}
			case SEEDS_AND_FOLLOWERS_AND_FRIENDS:
				usersToBeFiltered = new ArrayList<Long>();
				for (Long userId : seedUsers) {
					try {
						usersToBeFiltered.add(userId);
						usersToBeFiltered.addAll(twitterFacade.getFollowers(userId));
						usersToBeFiltered.addAll(twitterFacade.getFriends(userId));
					} catch (TwitterApiException e) {
						logger.info("Problems with user with id "+userId+". User is skipped.");
					}
				}
			
		}

		//remove duplicates
		usersToBeFiltered = new ArrayList<Long>(new HashSet<Long>(usersToBeFiltered));		
	}
	
	public void createGraph() {
		graphFacade.addUsers(seedUsers);
		for (Long userId : seedUsers) {			
			try {
				List<Long> followersIds = twitterFacade.getFollowers(userId);
				graphFacade.addFollowers(userId , followersIds);
				List<Long> friendsIds = twitterFacade.getFriends(userId);
				graphFacade.addFriends(userId , friendsIds);
			} catch (TwitterApiException e) {
				logger.info("Problem with user with id "+userId+". User skipped.");
			} catch (UserVertexNotPresent e) {
				logger.info("Problem with user with id "+userId+". " +
							"User should have been added to the graph but the user is not present.");
				System.exit(0);
			}
			
		}
	}
	
	private void calculateNode2degree() throws UserVertexNotPresent, InDegreeNotSetException, OutDegreeNotSetException {
		node2degree = new HashMap<Long, Integer>();
		
		switch (degreeDirection) {
			case IN:
											 // source   		  destination
				graphFacade.calculateInDegree(usersToBeFiltered, seedUsers);
				for (Long userId : usersToBeFiltered)
					node2degree.put(userId, graphFacade.getInDegree(userId));
			case OUT:
				 							  // fromThisGroup   toThisGroup
				graphFacade.calculateOutDegree(seedUsers,        usersToBeFiltered);
				for (Long userId : usersToBeFiltered)
					node2degree.put(userId, graphFacade.getOutDegree(userId));
			case TOTAL:
				/*
				 * TODO: implement!
				 */
//				List<Long> totUsers = new ArrayList<Long>();
//				totUsers.addAll(seedUsers);
//				totUsers.addAll(usersToBeFiltered);
//				graphFacade.calculateTotalDegree(totUsers);
//				for (Long userId : usersToBeFiltered)
//					node2degree.put(userId, graphFacade.getTotalDegree(userId));
				return;				

		}
	}
	
}
