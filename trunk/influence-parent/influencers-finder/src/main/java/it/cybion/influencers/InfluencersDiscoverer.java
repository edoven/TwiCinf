package it.cybion.influencers;

import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.List;

public class InfluencersDiscoverer {
	
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
		super();
		this.iterations = iterations;
		this.users = users;
		this.graphFacade = graphFacade;
		this.twitterFacade = twitterFacade;
		this.filterManagers = filterManagers;
	}



	public List<Long> getInfluencers()  {
		for (int i=0; i<iterations; i++) {
			for (int filterIndex=0; filterIndex<filterManagers.size(); filterIndex++) {
				FilterManager filterManager = filterManagers.get(i);
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				filterManager.setUsers(users);
				users = filterManager.filter();
			}
		}
		return users;
	}
}
