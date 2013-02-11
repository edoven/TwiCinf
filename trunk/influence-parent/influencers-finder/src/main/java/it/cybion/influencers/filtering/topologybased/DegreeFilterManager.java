package it.cybion.influencers.filtering.topologybased;


import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.UserVertexNotPresent;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;


public abstract class DegreeFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(DegreeFilterManager.class);
	
	protected List<Long> seedUsers;
	private TwitterFacade twitterFacade;
	protected GraphFacade graphFacade;
	protected List<User> enrichedSeedUsers;
	protected List<Long> followersAndFriends;
	
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
			return  (userToCompare.getFollowers().size() + userToCompare.getFriends().size())
					- (this.getFollowers().size() + this.getFriends().size());	 
		}

	};
	
	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade) {
		this.twitterFacade = twitterFacade;
		
	}
	
	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		this.graphFacade = graphFacade;
		this.graphFacade.eraseGraphAndRecreate();
	}
	
	@Override
	public void setSeedUsers(List<Long> seedUsers) {
		this.seedUsers = seedUsers;
		/*
		 * 
		 * 
		 *
		 * 
		 * 
		 *
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		if (seedUsers.size()>200) {
			List<Long> alreadyEnriched = seedUsers;
			alreadyEnriched.removeAll(twitterFacade.getNotFollowersAndFriendsEnriched(seedUsers));
			if (alreadyEnriched.size()>200) {
				Collections.shuffle(alreadyEnriched);
				this.seedUsers = alreadyEnriched.subList(0,  200);
			}
			else {
				List<Long> newSeedUsers = alreadyEnriched;
				Collections.shuffle(seedUsers);
				for (int i=0; i<seedUsers.size() && newSeedUsers.size()<200; i++) {
					long userId = seedUsers.get(i);
					if (!newSeedUsers.contains(userId))
						newSeedUsers.add(userId);
				}					
			}				
		}
		
//		if (seedUsers.size()>100) {
//			Collections.shuffle(seedUsers);
//			if (seedUsers.size()>200)
//				this.seedUsers = seedUsers.subList(0,  200);
//			else
//				this.seedUsers = seedUsers.subList(0,  Math.round(seedUsers.size()/6));
//		}
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		//once seedUsers are set, absolute thresholds can be calculated
		setAbsoluteThresholds();	
	}
		
	protected void solveDependencies() {	
		logger.info("### enriching seed users ###");
		getAndSetFollowersAndFriendsEnrichedUsers();
		logger.info("### creating graph ###");
		createGraph();		
		logger.info("### populating followers and friends big list###");
		populateFollowersAndFriendsList();
		logger.info("### calculating node degrees ###");
		calculateNodeDegrees();
	}
	
	private void getAndSetFollowersAndFriendsEnrichedUsers()  {
		logger.info("Not enriched = "+twitterFacade.getNotFollowersAndFriendsEnriched(seedUsers).size());
		enrichedSeedUsers = new ArrayList<User>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float)seedUsers.size()/10);
		if (tenPercent==0)
			tenPercent=1;
		
		
		twitterFacade.donwloadUsersProfiles(seedUsers);
		
		for (int i=0; i<seedUsers.size(); i++) {			
			long userId = seedUsers.get(i);
			int followersCount;
			int friendsCount;
			try {
				followersCount = twitterFacade.getFollowersCount(userId);
				friendsCount = twitterFacade.getFriendsCount(userId);
			} catch (TwitterException e1) {
				logger.info("Problem with user with id="+userId+". Skipped.");
				continue;
			}			
			if (followersCount<800000 && friendsCount<800000) {
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
			else
				logger.info("User with more than 800000 followers or friends. Skipped.");
		}	
		logger.info("getFollowersAndFriendsEnrichedUsers completed for 100%");
		Collections.sort(enrichedSeedUsers);		
	}
	
	private void createGraph() {	
		graphFacade.eraseGraphAndRecreate();
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
	
	
	//enrichedSeedUsers are used to create the graph
	private void populateFollowersAndFriendsList() {
		followersAndFriends = new ArrayList<Long>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float)enrichedSeedUsers.size()/10);
		if (tenPercent==0)
			tenPercent=1;
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
		//let's remove duplicates
		followersAndFriends = new ArrayList<Long>( new HashSet<Long>(followersAndFriends));
	}
	
	protected abstract void calculateNodeDegrees();
	
	protected abstract void setAbsoluteThresholds();
	
	@Override
	public abstract List<Long> filter();
}
