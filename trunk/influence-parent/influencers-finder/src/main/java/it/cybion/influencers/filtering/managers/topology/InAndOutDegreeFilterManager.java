package it.cybion.influencers.filtering.managers.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.filtering.filters.topology.NodeDegreeFilter;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.InDegreeNotSetException;
import it.cybion.influencers.graph.OutDegreeNotSetException;
import it.cybion.influencers.graph.UserVertexNotPresent;
import it.cybion.influencers.twitter.TwitterFacade;

public class InAndOutDegreeFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(InAndOutDegreeFilterManager.class);

	private List<Long> seedUsers;
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	private double inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private double outDegreePercentageThreshold;	
	private int outDegreeAbsoluteThreshold;
	private List<Long> followersAndFriends;
	

	public InAndOutDegreeFilterManager(	double inDegreePercentageThreshold,
										double outDegreePercentageThreshold) {
		this.inDegreePercentageThreshold = inDegreePercentageThreshold;
		this.outDegreePercentageThreshold = outDegreePercentageThreshold;
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

	@Override
	public List<Long> filter()  {
		solveDependencies();
		
		Map<Long, Integer> node2inDegree = new HashMap<Long, Integer>();
		for (Long userId : followersAndFriends) {
			try {
				int inDegree = graphFacade.getInDegree(userId);
				node2inDegree.put(userId, inDegree);
			} catch (UserVertexNotPresent e) {
				e.printStackTrace();
				System.exit(0);
			} catch (InDegreeNotSetException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		logger.info("node2inDegree.size()="+node2inDegree.size());
		Map<Long, Integer> node2outDegree = new HashMap<Long, Integer>();
		for (Long userId : followersAndFriends) {
			try {
				int outDegree = graphFacade.getOutDegree(userId);
				node2outDegree.put(userId, outDegree);
			} catch (UserVertexNotPresent e) {
				e.printStackTrace();
				System.exit(0);
			} catch (OutDegreeNotSetException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		logger.info("node2outDegree.size()="+node2outDegree.size());
		
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(
											node2inDegree, 
											inDegreeAbsoluteThreshold, 
											ComparisonOption.GREATER_OR_EQUAL);
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		logger.info("inDegreeFiltered.size()="+inDegreeFiltered.size());
		
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(
											node2outDegree, 
											outDegreeAbsoluteThreshold, 
											ComparisonOption.GREATER_OR_EQUAL);
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		logger.info("outDegreeFiltered.size()="+outDegreeFiltered.size());
		
		List<Long> inAndOutDegreeFiltered = putListsInAnd(inDegreeFiltered,outDegreeFiltered);
		logger.info("inAndOutDegreeFiltered.size()="+inAndOutDegreeFiltered.size());
		return inAndOutDegreeFiltered;
		
	}


	private void solveDependencies() {
		calculateThresholds();
		populateFollowersAndFriends();
		createGraph();		
		calculateNodeDegrees();
	}
	
	private void calculateThresholds() {
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * seedUsers.size()));
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * seedUsers.size()));
	}
	
	private void populateFollowersAndFriends() {
		followersAndFriends = new ArrayList<Long>();
		
		for (Long userId : seedUsers) {		
			boolean problemWithUser = false;
			for (int i=0; i<3; i++) {
				try {
					List<Long> followers = twitterFacade.getFollowers(userId);
					followersAndFriends.addAll(followers);
					List<Long> friends = twitterFacade.getFriends(userId);
					followersAndFriends.addAll(friends);
					break;
				} catch (TwitterException e) {
					logger.info("populateFollowersAndFriends: Problem with user with id "+userId+". Let's retry.");
					problemWithUser = true;
				}
			}
			if (problemWithUser == true)
				logger.info("populateFollowersAndFriends: Problem with user with id "+userId+". User skipped.");
		}
		followersAndFriends.removeAll(seedUsers);
		//remove duplicates
		followersAndFriends = new ArrayList<Long>( new HashSet<Long>(followersAndFriends));
	}
	
	private void createGraph() {
		graphFacade.addUsers(seedUsers);		
		for (Long userId : seedUsers) {		
			try {
				List<Long> followersIds = twitterFacade.getFollowers(userId);
				graphFacade.addFollowers(userId, followersIds);
				List<Long> friendsIds = twitterFacade.getFriends(userId);
				graphFacade.addFriends(userId, friendsIds);
			} catch (UserVertexNotPresent e) {
				logger.info("Problem with user with id "+userId+". " +
						"User should have been added to the graph but the user is not present.");
				System.exit(0);
			} catch (TwitterException e) {
				logger.info("Problem with user with id "+userId+". User skipped.");
			}
			
		}
	}
	
	private void calculateNodeDegrees() {
		try {
			graphFacade.calculateInDegree(followersAndFriends, seedUsers);
			graphFacade.calculateOutDegree(followersAndFriends, seedUsers);	
		} catch (UserVertexNotPresent e) {			
			e.printStackTrace();
			System.exit(0);
		}
			
	}
	
	private List<Long> putListsInAnd(List<Long> listA,
									 List<Long> listB) {
		List<Long> andList = new ArrayList<Long>();
		for (Long elementA : listA)
			if (listB.contains(elementA))
				andList.add(elementA);
		for (Long elementB : listB)
			if (listA.contains(elementB))
				andList.add(elementB);
		andList = new ArrayList<Long>( new HashSet<Long>(andList));
		return andList;
	}
}
