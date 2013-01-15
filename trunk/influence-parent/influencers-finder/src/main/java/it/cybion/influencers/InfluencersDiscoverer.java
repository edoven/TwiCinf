package it.cybion.influencers;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.List;

import org.apache.log4j.Logger;

public class InfluencersDiscoverer {
	
	private static final Logger logger = Logger.getLogger(Food46.class);
	
	private int iterations;
	private List<Long> users;
	private GraphFacade graphFacade;
	private TwitterFacade twitterFacade;
	private List<FilterManager> filterManagers;
		
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

	public List<Long> getInfluencers()  {
		for (int iterationIndex=0; iterationIndex<iterations; iterationIndex++) {
			logger.info("#### ITERATION "+iterationIndex+" #####");
			for (int filterIndex=0; filterIndex<filterManagers.size(); filterIndex++) {
				FilterManager filterManager = filterManagers.get(filterIndex);
				logger.info("#### filter "+(filterIndex+1)+"/"+filterManagers.size()+" "+filterManager.getClass().getName()+" ####");			
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				filterManager.setSeedUsers(users);
				users = filterManager.filter();
				logger.info("results from filtering = "+users);
				logger.info("number of results from filtering = "+users.size());
			}
		}
		return users;
	}
}
