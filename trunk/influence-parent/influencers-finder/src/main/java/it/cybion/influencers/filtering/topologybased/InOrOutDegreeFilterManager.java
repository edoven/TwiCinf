package it.cybion.influencers.filtering.topologybased;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.UserVertexNotPresent;
import it.cybion.influencers.twitter.TwitterFacade;

public class InOrOutDegreeFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(InOrOutDegreeFilterManager.class);

	private List<Long> seedUsers;
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	private double inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private double outDegreePercentageThreshold;	
	private int outDegreeAbsoluteThreshold;
	private List<Long> followersAndFriends;
	private List<User> enrichedSeedUsers;
	private Map<Long, Integer> node2inDegree;
	private Map<Long, Integer> node2outDegree;
	
	
	class User implements Comparable<User>{
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
		
		@Override
		public int compareTo(User userToCompare) {	
			return  userToCompare.getFollowers().size() - this.getFollowers().size();	 
		}

	};
	

	public InOrOutDegreeFilterManager(	double inDegreePercentageThreshold,
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
		//once seedUsers are set, absolute thresholds can be calculated
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * seedUsers.size()));
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * seedUsers.size()));
	}

	@Override
	public List<Long> filter()  {
		solveDependencies();	
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(node2inDegree, inDegreeAbsoluteThreshold);
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		logger.info("inDegreeFiltered.size()="+inDegreeFiltered.size());		
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(node2outDegree,outDegreeAbsoluteThreshold);
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		logger.info("outDegreeFiltered.size()="+outDegreeFiltered.size());		
		List<Long> inOrOutDegreeFiltered = putListsInOr(inDegreeFiltered,outDegreeFiltered);
		logger.info("inAndOutDegreeFiltered.size()="+inOrOutDegreeFiltered.size());
		return inOrOutDegreeFiltered;		
	}


	private void solveDependencies() {	
		logger.info("### enriching seed users ###");
		getAndSetFollowersAndFriendsEnrichedUsers();
		logger.info("### creating graph ###");
		createGraph();		
		logger.info("### populating followers and friends big list###");
		populateFollowersAndFriendsList();
		logger.info("### calculating node degrees ###");
		calculateNodeDegrees();
		enrichedSeedUsers = null;
	}

		
	private void createGraph() {		
		graphFacade.addUsers(seedUsers);				
		for (int i=0; i<enrichedSeedUsers.size(); i++) {	
			User user = enrichedSeedUsers.get(i);
			logger.info("createGraph user "+i+"/"+seedUsers.size()+
						" flwrs="+user.getFollowers().size()+
						" frnds="+user.getFriends().size()+
						" (freeMem= "+Runtime.getRuntime().freeMemory()/(1024*1024)+" MB - "+
						"vertices="+graphFacade.getVerticesCount()+")");
			try {
				graphFacade.addFollowers(user.getId(), user.getFollowers());
				graphFacade.addFriends(user.getId(), user.getFriends());
			} catch (UserVertexNotPresent e) {
				logger.info("Error! User should be in the graph but vertex is not present.");
				System.exit(0);
			}
		}
	}
	
	
	private void getAndSetFollowersAndFriendsEnrichedUsers() {
		enrichedSeedUsers = new ArrayList<User>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float)seedUsers.size()/10);
		for (int i=0; i<seedUsers.size(); i++) {	
			
			long userId = seedUsers.get(i);
			try {					
				List<Long> followersIds = twitterFacade.getFollowers(userId);				
				List<Long> friendsIds = twitterFacade.getFriends(userId);
				User user = new User(userId, followersIds, friendsIds);
				enrichedSeedUsers.add(user);
			} 
			catch (TwitterException e) {
				logger.info("Problem with user with id "+userId+". User skipped.");
			}
			
			if (i%tenPercent == 0) {
				logger.info("getFollowersAndFriendsEnrichedUsers completed for "+percentCompleted+"%");
				percentCompleted = percentCompleted + 10;
			}
		}		
		Collections.sort(enrichedSeedUsers);		
	}
	
	private void populateFollowersAndFriendsList() {
		followersAndFriends = new ArrayList<Long>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float)enrichedSeedUsers.size()/10);
		for (int i=0; i<enrichedSeedUsers.size(); i++) {
			User user = enrichedSeedUsers.get(i);
			
			for (int j=0; j<1; j++) { //1 try
				List<Long> followers = user.getFollowers();
				followersAndFriends.addAll(followers);
				List<Long> friends = user.getFriends();
				followersAndFriends.addAll(friends);			
			}
			if (i%tenPercent == 0) {
				logger.info("populateFollowersAndFriendsList completed for "+percentCompleted+"%");
				percentCompleted = percentCompleted + 10;
			}
		}
		//some user in seedUsers list can be follower or firend of another seedUsers user
		//so let's remove them from followersAndFriends
		//followersAndFriends.removeAll(seedUsers);
		/*
		 * 
		 * BEWARE: removing "followersAndFriends.removeAll(seedUsers)" every seed user
		 * can be in followersAndFriends
		 * 
		 */
		
		//let's remove duplicates
		followersAndFriends = new ArrayList<Long>( new HashSet<Long>(followersAndFriends));
	}
	
	private void calculateNodeDegrees() {
		try {
			//this sets an inDegree label in the graph for each node of followersAndFriends set
			node2inDegree = graphFacade.getInDegrees(followersAndFriends, seedUsers); 
			//this sets an outDegree label in the graph for each node of followersAndFriends set
			node2outDegree = graphFacade.getOutDegrees(followersAndFriends, seedUsers);	
		} catch (UserVertexNotPresent e) {			
			e.printStackTrace();
			System.exit(0);
		}			
	}
	
	private List<Long> putListsInOr(List<Long> listA,List<Long> listB) {		
		List<Long> orList = listA;
		orList.addAll(listB);
		orList = new ArrayList<Long>(new HashSet<Long>(orList));
		return orList;
	}

	@Override
	public String toString() {
		String inputSize = "NotSet";
		String inDegreeAbsThreshold = "CannotBeCalculated";
		String outDegreeAbsThreshold = "CannotBeCalculated";
		if (seedUsers!=null ) {
			inputSize = Integer.toString(seedUsers.size());
			inDegreeAbsThreshold = Integer.toString(inDegreeAbsoluteThreshold);
			outDegreeAbsThreshold = Integer.toString(outDegreeAbsoluteThreshold);
		}
		return "InOrOutDegreeFilterManager" +
				" (inDegreePercentageThreshold="+inDegreePercentageThreshold*100+"%"+
				" - outDegreePercentageThreshold="+outDegreePercentageThreshold*100+"%"+
				" - inDegreeAbsoluteThreshold="+inDegreeAbsThreshold+
				" - outDegreeAbsoluteThreshold="+outDegreeAbsThreshold+
				" - inputSize="+inputSize+")";
	}
}