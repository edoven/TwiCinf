package it.cybion.influencers.filtering.topologybased;


import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.exceptions.UserVertexNotPresentException;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

/*
 * 
 * BEWARE: in setSeedUsers(List<Long> seedUsers) if
 * 		   seedUsers are more than 200, only 200 users are taken.
 *         This is done to speed-up the process and for the "sampling theory".
 * 
 */

public abstract class DegreeFilterManager implements FilterManager
{

	private static final Logger logger = Logger.getLogger(DegreeFilterManager.class);

	protected List<Long> seedUsers;
	private TwitterFacade twitterFacade;
	protected GraphFacade graphFacade;
	protected List<User> enrichedSeedUsers;
	protected List<Long> followersAndFriends;

	class User implements Comparable<User>
	{
		private long id;
		private List<Long> followers;
		private List<Long> friends;

		public User(long id, List<Long> followers, List<Long> friends)
		{
			this.id = id;
			this.followers = followers;
			this.friends = friends;
		}

		public long getId()
		{
			return id;
		}

		public List<Long> getFollowers()
		{
			return followers;
		}

		public List<Long> getFriends()
		{
			return friends;
		}

		@Override
		public int compareTo(User userToCompare)
		{
			return (userToCompare.getFollowers().size() + userToCompare.getFriends().size()) - (this.getFollowers().size() + this.getFriends().size());
		}

	};

	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade)
	{
		this.twitterFacade = twitterFacade;

	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade)
	{
		this.graphFacade = graphFacade;
	}

	@Override
	public void setSeedUsers(List<Long> seedUsers)
	{
		this.seedUsers = seedUsers;	
	}

	protected void solveDependencies()
	{
		
		if (seedUsers.size() > 200)
		{
			logger.info("seeds are "+seedUsers.size()+" let's cut them to 200");
			this.seedUsers = get200SeedUsers(seedUsers);
//			Collections.shuffle(seedUsers);
//			this.seedUsers = seedUsers.subList(0, 200);
		}
		// once seedUsers are set, absolute thresholds can be calculated
		setAbsoluteThresholds();
		logger.info("### enriching seed users ###");
		getAndSetFollowersAndFriendsEnrichedUsers();
		logger.info("### creating graph ###");
		createGraph();
		logger.info("### populating followers and friends big list###");
		populateFollowersAndFriendsList();
		logger.info("### calculating node degrees ###");
		calculateNodeDegrees();
	}
	
	private List<Long> get200SeedUsers(List<Long> seedUsers)
	{
		List<Long> notEnrichedSeedUsers = twitterFacade.getNotFollowersAndFriendsEnriched(seedUsers);
		
		List<Long> enrichedSeedUsers = new ArrayList<Long>();
		enrichedSeedUsers.addAll(seedUsers);
		enrichedSeedUsers.removeAll(notEnrichedSeedUsers);
			
		logger.info("notEnrichedSeedUsers.size()="+notEnrichedSeedUsers.size());
		logger.info("enrichedSeedUsers.size()="+enrichedSeedUsers.size());
		
		if (enrichedSeedUsers.size()>=200)
		{
			logger.info("there alreary are more than 200 enriched users, let's take 200 of them.");
			Collections.shuffle(enrichedSeedUsers);
			return enrichedSeedUsers.subList(0, 200);
		}
		
		logger.info("there already are less than 200 enriched users, "+(200-enrichedSeedUsers.size())+"have to be enriched.");
		
		int seedUsersIndex = 0;
		while (enrichedSeedUsers.size()<200)
		{
			Long userId = seedUsers.get(seedUsersIndex);
			if (!enrichedSeedUsers.contains(userId))
				enrichedSeedUsers.add(userId);
			seedUsersIndex++;
		}
		return enrichedSeedUsers;
	}

	private void getAndSetFollowersAndFriendsEnrichedUsers()
	{
		logger.info("Not enriched = " + twitterFacade.getNotFollowersAndFriendsEnriched(seedUsers).size());
		enrichedSeedUsers = new ArrayList<User>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float) seedUsers.size() / 10);
		if (tenPercent == 0)
			tenPercent = 1;

		twitterFacade.donwloadUsersProfiles(seedUsers);

		for (int i = 0; i < seedUsers.size(); i++)
		{
			long userId = seedUsers.get(i);		
			List<Long> followersIds;
			List<Long> friendsIds;
			try
			{
				if (twitterFacade.getFollowersCount(userId) < 800000)
					followersIds = twitterFacade.getFollowers(userId);
				else
				{
					followersIds = new ArrayList<Long>();
					logger.info("User with more than 800000 followers. Followers kipped.");
				}
					
				if (twitterFacade.getFriendsCount(userId) < 800000)
					friendsIds = twitterFacade.getFriends(userId);
				else 
				{
					friendsIds = new ArrayList<Long>();
					logger.info("User with more than 800000 friends. Friends kipped.");
				}
				User user = new User(userId, followersIds, friendsIds);
				enrichedSeedUsers.add(user);
					
			} catch (TwitterException e)
			{
				logger.info("Problem with user with id " + userId + ". User skipped.");
			}	
							
			
			if (i % tenPercent == 0)
			{
				logger.info("getFollowersAndFriendsEnrichedUsers completed for " + percentCompleted + "%");
				percentCompleted = percentCompleted + 10;
			}
		}
		logger.info("getFollowersAndFriendsEnrichedUsers completed for 100%");
		Collections.sort(enrichedSeedUsers);
	}

	private void createGraph()
	{
//		graphFacade.eraseGraphAndRecreate();
		graphFacade.addUsers(seedUsers);
		for (int i = 0; i < enrichedSeedUsers.size(); i++)
		{
			User user = enrichedSeedUsers.get(i);
			logger.info("createGraph user " + (i+1) + "/" + seedUsers.size() +
						" flwrs=" + user.getFollowers().size() +
						" frnds=" + user.getFriends().size() +
						" (freeMem= " + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB - " +
						"vertices="	+ graphFacade.getVerticesCount() + ")");
			try
			{
				graphFacade.addFollowers(user.getId(), user.getFollowers());
				graphFacade.addFriends(user.getId(), user.getFriends());
			} catch (UserVertexNotPresentException e)
			{
				logger.info("Error! User should be in the graph but vertex is not present.");
				System.exit(0);
			}
		}
	}

	// enrichedSeedUsers are used to create the graph
	private void populateFollowersAndFriendsList()
	{
		followersAndFriends = new ArrayList<Long>();
		int percentCompleted = 0;
		int tenPercent = Math.round((float) enrichedSeedUsers.size() / 10);
		if (tenPercent == 0)
			tenPercent = 1;
		for (int i = 0; i < enrichedSeedUsers.size(); i++)
		{
			User user = enrichedSeedUsers.get(i);

			for (int j = 0; j < 1; j++)
			{ // 1 try
				List<Long> followers = user.getFollowers();
				followersAndFriends.addAll(followers);
				List<Long> friends = user.getFriends();
				followersAndFriends.addAll(friends);
			}
			if (i % tenPercent == 0)
			{
				logger.info("populateFollowersAndFriendsList completed for " + percentCompleted + "%");
				percentCompleted = percentCompleted + 10;
			}
		}
		// let's remove duplicates
		followersAndFriends = new ArrayList<Long>(new HashSet<Long>(followersAndFriends));
	}

	protected abstract void calculateNodeDegrees();

	protected abstract void setAbsoluteThresholds();

	@Override
	public abstract List<Long> filter();
}
