package it.cybion.influencers.filtering.managers.content;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.cybion.influencers.filtering.filters.content.DescriptionDictionaryFilter;
import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;


public class DescriptionDictionaryFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(DescriptionDictionaryFilterManager.class);

	
	TwitterFacade twitterFacade;
	GraphFacade graphFacade;	
	List<Long> seedUsers;
	ExpansionDirection expansionDirection;
	Map<Long, String> users2descriptions;
	List<String> dictionary;
	
	public DescriptionDictionaryFilterManager(List<String> dictionary) {
		this.dictionary = dictionary;
	}
		
	@Override
	public List<Long> filter() {
		solveDependencies();	
		DescriptionDictionaryFilter filter = new DescriptionDictionaryFilter(dictionary,users2descriptions);
		logger.info("users2descriptions.size()="+users2descriptions.size());
		return filter.filter();
	}
	
	/*
	 * This method creates and sets user2description
	 * by asking the description to twitterFacade.
	 * It acts in different ways depending on expansionDirection.
	 */
	private void solveDependencies() {		
		createUsers2descriptions(); //this gets the descriptions from twitterFacade
	}
	
		
	private void createUsers2descriptions() {
		users2descriptions = new HashMap<Long, String>();
		for (Long userId : seedUsers)
			try {
				users2descriptions.put(userId, twitterFacade.getDescription(userId));
			} catch (TwitterException e) {
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
