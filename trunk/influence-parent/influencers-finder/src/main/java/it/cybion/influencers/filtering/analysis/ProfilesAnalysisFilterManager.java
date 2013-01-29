package it.cybion.influencers.filtering.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import food46.Food46;

import twitter4j.TwitterException;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

public class ProfilesAnalysisFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(ProfilesAnalysisFilterManager.class);

	private List<Long> users;
	private GraphFacade graphFacade;
	private TwitterFacade twitterManager;
	Map<Integer,Integer> followersCount2usersCount = new HashMap<Integer,Integer>();
	Map<Integer,Integer> friendsCount2usersCount = new HashMap<Integer,Integer>();

	@Override
	public List<Long> filter() {
		
		for (Long userId : users) {			
			try {
				int followersCount = twitterManager.getFollowersCount(userId);
				if (!followersCount2usersCount.containsKey(followersCount))
					followersCount2usersCount.put(followersCount, 1);
				else {
					int usersCount = followersCount2usersCount.get(followersCount);
					followersCount2usersCount.put(followersCount, usersCount+1 );
				}
			} catch (TwitterException e) {
				logger.info("Problem with getFollowersCount for user with id="+userId+". Skipped.");
			}
			try {
				int friendsCount = twitterManager.getFriendsCount(userId);
				if (!friendsCount2usersCount.containsKey(friendsCount))
					friendsCount2usersCount.put(friendsCount, 1);
				else {
					int usersCount = friendsCount2usersCount.get(friendsCount);
					friendsCount2usersCount.put(friendsCount, usersCount+1 );
				}
			} catch (TwitterException e) {
				logger.info("Problem with getFollowersCount for user with id="+userId+". Skipped.");
			}							
		}
		/*
		 * TODO: sort maps!
		 */
		printStats();
		return users;
	}

	private void printStats() {
		//for (Entry<Long,Integer> mapEntry : followersCount2usersCount)
			
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterManager) {
		this.twitterManager = twitterManager;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		/*
		 * graphFacade is useless
		 */
	}

	@Override
	public void setSeedUsers(List<Long> users) {
		this.users = users;
	}

}
