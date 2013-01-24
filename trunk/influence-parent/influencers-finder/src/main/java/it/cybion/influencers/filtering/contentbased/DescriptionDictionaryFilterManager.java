package it.cybion.influencers.filtering.contentbased;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;


public class DescriptionDictionaryFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(DescriptionDictionaryFilterManager.class);

	
	TwitterFacade twitterFacade;
	GraphFacade graphFacade;	
	List<Long> seedUsers;
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
		users2descriptions = twitterFacade.getDescriptions(seedUsers);
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
	public String toString() {
		String result = "###DescriptionDictionaryFilter###" +
				" (inputSize="+seedUsers.size()+
				" dictionary=[";
		for (String word: dictionary)
			result = result.concat(word+",");
		result = result.concat("]");
		return result;
	}

}
