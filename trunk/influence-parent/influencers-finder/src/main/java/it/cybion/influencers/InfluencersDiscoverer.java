package it.cybion.influencers;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class InfluencersDiscoverer {
	
	private static final Logger logger = Logger.getLogger(InfluencersDiscoverer.class);
	
	private int iterations;
	private List<Long> users;
	private GraphFacade graphFacade;
	private TwitterFacade twitterFacade;
	private List<FilterManager> filterManagers;
	private Set<Long> resultsFromIterations = new HashSet<Long>();
		
	public InfluencersDiscoverer(int iterations, 
								  List<Long> users,
								  GraphFacade graphFacade, 
								  TwitterFacade twitterFacade,
								  List<FilterManager> filterManagers) {
		this.iterations = iterations;
		this.users = users;
		this.graphFacade = graphFacade;
		this.twitterFacade = twitterFacade;
		this.filterManagers = filterManagers;
	}

	public List<Long> getInfluencers() throws IOException  {
		
		printInfo();

		for (int iterationIndex=0; iterationIndex<iterations; iterationIndex++) {
			logger.info("#### ITERATION "+(iterationIndex+1)+" #####");
			for (int filterIndex=0; filterIndex<filterManagers.size(); filterIndex++) {
				FilterManager filterManager = filterManagers.get(filterIndex);
				logger.info("");	
				logger.info("");
				logger.info("#### filter "+(filterIndex+1)+"/"+filterManagers.size()+" ####");	
				logger.info("");
				if (filterIndex>0) {
					logger.info("deleting old graph and recreating a new one");
					graphFacade.eraseGraphAndRecreate();
				}				
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				filterManager.setSeedUsers(users);
				logger.info(filterManager.toString());
				users = filterManager.filter();
				logger.info("results from filtering = "+users);
				logger.info("number of results from filtering = "+users.size());
			}
			resultsFromIterations.addAll(users);
			
		}
		return new ArrayList<Long>(resultsFromIterations);
	}

	private void printInfo() {
		logger.info("############################################");
		logger.info("Iterations =  "+iterations);
		for (int filterIndex=0; filterIndex<filterManagers.size(); filterIndex++) {
			FilterManager filterManager = filterManagers.get(filterIndex);
			logger.info(filterIndex+") "+filterManager.toString());
		}
		logger.info("############################################");
		logger.info("");
		logger.info("");
	}
}
