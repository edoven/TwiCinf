package it.cybion.influencers.filtering.expansion;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.topologybased.OutDegreeFilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

public class FollowersAndFriendsExpansionFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(FollowersAndFriendsExpansionFilterManager.class);

	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	private List<Long> seedUsers;


	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade) {
		this.twitterFacade = twitterFacade;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		/*
		 * Graph facade is useless
		 */
	}

	@Override
	public void setSeedUsers(List<Long> seedUsers) {
		this.seedUsers = seedUsers;
	}
	
	
	@Override
	public List<Long> filter() {
		List<Long> followerAndFriends = new ArrayList<Long>();
		for (Long userId : seedUsers) {
			try {
				followerAndFriends.addAll( twitterFacade.getFollowers(userId) );
			} catch (TwitterException e) {
				logger.info("Problem retrieving followers of user with id="+userId+". User skipped.");
			}
			try {
				followerAndFriends.addAll( twitterFacade.getFriends(userId) );
			} catch (TwitterException e) {
				logger.info("Problem retrieving friends of user with id="+userId+". User skipped.");
			}		
		}
		return followerAndFriends;
	}


}
