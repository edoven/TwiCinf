package it.cybion.influencers.filtering.contentbased;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;


public class DescriptionAndStatusDictionaryFilterManager implements FilterManager {
	
	private static final Logger logger = Logger.getLogger(DescriptionAndStatusDictionaryFilterManager.class);

	
	TwitterFacade twitterFacade;
	GraphFacade graphFacade;	
	List<Long> seedUsers;
	Map<Long, String> users2descriptions;
	List<String> dictionary;
	
	public DescriptionAndStatusDictionaryFilterManager(List<String> dictionary) {
		this.dictionary = dictionary;
	}
		
	@Override
	public List<Long> filter() {
		logger.info("seedUsers.size()="+seedUsers.size());
		
		solveDependencies();	
		DescriptionAndStatusDictionaryFilter filter = new DescriptionAndStatusDictionaryFilter(dictionary,users2descriptions);
		logger.info("users2descriptions.size()="+users2descriptions.size());
		
		

//		List<Long> goodUsers = filter.filter();
//		List<Long> badUsers = seedUsers;
//		badUsers.removeAll(goodUsers);	
//		int lessThan100 = 0;
//		int lessThan250 = 0;
//		int lessThan500 = 0;
//		int lessThan1000 = 0;
//		int lessThan2500 = 0;
//		int lessThan5000 = 0;
//		int greatherThan5000 = 0;	
//		for (Long usersId : badUsers) {
//			try {
//				int followersCounts =  twitterFacade.getFollowersCount(usersId);
//				if (followersCounts<100) {
//					lessThan100++;
//					continue;
//				}
//				if (followersCounts<250){
//					lessThan250++;
//					continue;
//				}
//				if (followersCounts<500) {
//					lessThan500++;
//					continue;
//				}
//				if (followersCounts<1000) {
//					lessThan1000++;
//					continue;
//				}
//				if (followersCounts<2500) {
//					lessThan2500++;
//					continue;
//				}
//				if (followersCounts<5000) {
//					lessThan5000++;
//					continue;
//				}
//				greatherThan5000++;
//			} catch (TwitterException e) {
//				logger.info("user skipped");
//			}
//		}
//		//lessThan100 = lessThan100;
//		lessThan250  = lessThan250+lessThan100 ;
//		lessThan500  = lessThan500+lessThan250;
//		lessThan1000 = lessThan1000+lessThan500;
//		lessThan2500 = lessThan2500+lessThan1000;
//		lessThan5000 = lessThan5000+lessThan2500;
//		
//		logger.info("lessThan100 = "+lessThan100+" ("+Math.round(100*((double)lessThan100/badUsers.size()))+"%)");	
//		logger.info("lessThan250 = "+lessThan250+" ("+Math.round(100*((double)lessThan250/badUsers.size()))+"%)");	
//		logger.info("lessThan500 = "+lessThan500+" ("+Math.round(100*((double)lessThan500/badUsers.size()))+"%)");
//		logger.info("lessThan1000 = "+lessThan1000+" ("+Math.round(100*((double)lessThan1000/badUsers.size()))+"%)");		
//		logger.info("lessThan2500 = "+lessThan2500+" ("+Math.round(100*((double)lessThan2500/badUsers.size()))+"%)");
//		logger.info("lessThan5000 = "+lessThan5000+" ("+Math.round(100*((double)lessThan5000/badUsers.size()))+"%)");
//		logger.info("greatherThan5000 = "+greatherThan5000+" ("+Math.round(100*((double)greatherThan5000/badUsers.size()))+"%)");
		
		
		
		
		
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
		users2descriptions = twitterFacade.getDescriptionsAndStatuses(seedUsers);
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
		String inputSize = "NotSet";
		if (seedUsers!=null ) {
			inputSize = Integer.toString(seedUsers.size());
		}
		String result = "DescriptionDictionaryFilter" +
				" (inputSize="+inputSize+
				" dictionary=[";
		for (String word: dictionary)
			result = result.concat(word+",");
		result = result.concat("])");
		return result;
	}

}
