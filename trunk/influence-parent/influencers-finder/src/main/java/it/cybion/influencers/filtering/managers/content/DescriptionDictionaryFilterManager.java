package it.cybion.influencers.filtering.managers.content;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import it.cybion.influencers.filtering.filters.content.DescriptionDictionaryFilter;
import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;

public class DescriptionDictionaryFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(DescriptionDictionaryFilterManager.class);

	
	TwitterFacade twitterFacade;
	GraphFacade graphFacade;	
	List<Long> seedUsers;
	List<Long> usersToFilter;
	ExpansionDirection expansionDirection;
	Map<Long, String> users2descriptions;
	List<String> dictionary;
		
	@Override
	public List<Long> filter() {
		solveDependencies();	
		DescriptionDictionaryFilter filter = new DescriptionDictionaryFilter(dictionary,users2descriptions);
		return filter.filter();
	}
	
	/*
	 * This method creates and sets user2description
	 * by asking the description to twitterFacade.
	 * It acts in different ways depending on expansionDirection.
	 */
	private void solveDependencies() {		
		createUserToFilterList(); //this expands the user set if needed
		createUsers2descriptions(); //this gets the descriptions from twitterFacade
	}
	
	private void createUserToFilterList() {
		usersToFilter = new ArrayList<Long>();
		switch (expansionDirection) {
			case SEEDS:
				usersToFilter.addAll(seedUsers);			
			case FOLLOWERS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}
				}
			case FRIENDS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.addAll(twitterFacade.getFriends(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}
				}
			case FOLLOWERS_AND_FRIENDS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.addAll(twitterFacade.getFriends(userId));
						usersToFilter.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}					
				}
			case SEEDS_AND_FOLLOWERS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.add(userId);
						usersToFilter.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}
				}
			case SEEDS_AND_FRIENDS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.add(userId);
						usersToFilter.addAll(twitterFacade.getFriends(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}
				}
			case SEEDS_AND_FOLLOWERS_AND_FRIENDS:
				for (Long userId : seedUsers) {
					try {
						usersToFilter.add(userId);
						usersToFilter.addAll(twitterFacade.getFriends(userId));
						usersToFilter.addAll(twitterFacade.getFollowers(userId));
					} catch (TwitterApiException e) {
						logger.info("Problem with user with id "+userId+". User skipped.");
					}					
				}
		}
		//Remove duplicates
		usersToFilter = new ArrayList<Long>(new HashSet<Long>(usersToFilter));
	}
		
	private void createUsers2descriptions() {
		users2descriptions = new HashMap<Long, String>();
		for (Long userId : usersToFilter)
			try {
				users2descriptions.put(userId, twitterFacade.getDescription(userId));
			} catch (TwitterApiException e) {
				logger.info("Error to get user with id "+userId+" from Twitter. User skipped.");
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
	public void setSeedUsers(List<Long> seedUsers) {
		this.seedUsers = seedUsers;
	}

}
