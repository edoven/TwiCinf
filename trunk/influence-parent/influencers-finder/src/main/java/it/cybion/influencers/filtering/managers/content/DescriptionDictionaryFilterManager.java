package it.cybion.influencers.filtering.managers.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cybion.influencers.filtering.filters.content.DescriptionDictionaryFilter;
import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

public class DescriptionDictionaryFilterManager implements FilterManager {

	DescriptionDictionaryFilter filter;
	TwitterFacade twitterFacade;
	GraphFacade graphFacade;
	List<Long> users;
	ExpansionDirection expansionDirection;
	Map<Long, String> user2description;
	
	
	
	@Override
	public List<Long> filter() {
		solveDependencies();
		filter.setDescriptions(user2description);
		return filter.filter();
	}
	
	/*
	 * This method creates and sets user2description
	 * by asking the description to twitterFacade.
	 * It acts in different ways depending on expansionDirection.
	 */
	private void solveDependencies() {
		user2description = new HashMap<Long,String>();
			
		switch (expansionDirection) {
			case NONE:
				for (Long userId : users) {
					String description = twitterFacade.getDescription(userId);
					user2description.put(userId, description);
				}				
			case FOLLOWERS:
				Set<Long> followers = new HashSet<Long>();
				for (Long userId : users) {
					followers.addAll(twitterFacade.getFollowers(userId));
				}
				for (Long userId : followers) {
					String description = twitterFacade.getDescription(userId);
					user2description.put(userId, description);
				}
			case FRIENDS:
				Set<Long> friends = new HashSet<Long>();
				for (Long userId : users) {
					friends.addAll(twitterFacade.getFriends(userId));
				}
				for (Long userId : friends) {
					String description = twitterFacade.getDescription(userId);
					user2description.put(userId, description);
				}
			case FOLLOWERS_AND_FRIENDS:
				Set<Long> followersAndFriends = new HashSet<Long>();
				for (Long userId : users) {
					followersAndFriends.addAll(twitterFacade.getFriends(userId));
					followersAndFriends.addAll(twitterFacade.getFollowers(userId));
				}
				for (Long userId : followersAndFriends) {
					String description = twitterFacade.getDescription(userId);
					user2description.put(userId, description);
				}			
		}
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
	public void setUsers(List<Long> users) {
		this.users = users;
	}

}
