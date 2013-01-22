package it.cybion.influencers.filtering.topologybased;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
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
	private List<User> enrichedUsers;
	private Map<Long, Integer> node2inDegree;
	private Map<Long, Integer> node2outDegree;
	
	
	class User {
		private long id;
		private List<Long> followers;
		private List<Long> friends;
		
		public User(long id, List<Long> followers, List<Long> friends) {
			this.id = id;
			this.followers = followers;
			this.friends = friends;
		}

		public long getId() { return id;}
		public List<Long> getFollowers() {return followers;}
		public List<Long> getFriends() {return friends;	}

	};
	

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
		logger.info("### calculating thresholds ###");
		calculateThresholds();		
		logger.info("### creating graph ###");
		createGraph();		
		logger.info("### populating followers and friends big list###");
		populateFollowersAndFriendsList();
		logger.info("### calculating node degrees ###");
		calculateNodeDegrees();
		enrichedUsers = null;
	}
	
	private void calculateThresholds() {
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * seedUsers.size()));
		logger.info("inDegreeAbsoluteThreshold="+inDegreeAbsoluteThreshold);
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * seedUsers.size()));
		logger.info("outDegreeAbsoluteThreshold="+outDegreeAbsoluteThreshold);
	}
		
	private void createGraph() {
		graphFacade.addUsers(seedUsers);
		getFollowersAndFriendsEnrichedUsers();
		
		for (int i=0; i<enrichedUsers.size(); i++) {	
			User user = enrichedUsers.get(i);
			logger.info("createGraph user "+i+" of "+seedUsers.size()+" (free memory= "+Runtime.getRuntime().freeMemory()/(1024*1024)+" MB)");
			try {
				graphFacade.addFollowers(user.getId(), user.getFollowers());
				graphFacade.addFriends(user.getId(), user.getFriends());
			} catch (UserVertexNotPresent e) {
				logger.info("Error! User should be in the graph but vertex is not present.");
				System.exit(0);
			}
		}
	}
	
	
	private void getFollowersAndFriendsEnrichedUsers() {
		enrichedUsers = new ArrayList<User>();
		for (int i=0; i<seedUsers.size(); i++) {	
			logger.info("getFollowersAndFriendsEnrichedUsers user "+i+" of "+seedUsers.size());
			long userId = seedUsers.get(i);
			try {					
				List<Long> followersIds = twitterFacade.getFollowers(userId);				
				List<Long> friendsIds = twitterFacade.getFriends(userId);
				User user = new User(userId, followersIds, friendsIds);
				enrichedUsers.add(user);
			} 
			catch (TwitterException e) {
				logger.info("Problem with user with id "+userId+". User skipped.");
			}
		}
	}
	
	private void populateFollowersAndFriendsList() {
		followersAndFriends = new ArrayList<Long>();
		for (int i=0; i<enrichedUsers.size(); i++) {
			User user = enrichedUsers.get(i);
			logger.info("populateFollowersAndFriendsList for user "+i+"/"+seedUsers.size());
			for (int j=0; j<1; j++) { //1 try
				List<Long> followers = user.getFollowers();
				followersAndFriends.addAll(followers);
				List<Long> friends = user.getFriends();
				followersAndFriends.addAll(friends);			
			}
		}
		//some user in seedUsers list can be follower or firend of another seedUsers user
		//so let's remove them from followersAndFriends
		followersAndFriends.removeAll(seedUsers);
		//let's remove duplicates
		followersAndFriends = new ArrayList<Long>( new HashSet<Long>(followersAndFriends));
	}
	
	private void calculateNodeDegrees() {
		try {
			//this sets an inDegree label in the graph for each node of followersAndFriends set
			node2inDegree = graphFacade.calculateInDegree(followersAndFriends, seedUsers); 
			//this sets an outDegree label in the graph for each node of followersAndFriends set
			node2outDegree = graphFacade.calculateOutDegree(followersAndFriends, seedUsers);	
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
