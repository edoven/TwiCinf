package it.cybion.influencers;

import it.cybion.influencers.filter.managers.FilterManager;
import it.cybion.influencers.graph.GraphManager;
import it.cybion.influencers.twitter.TwitterManager;

import java.util.List;

public class InfluencersDiscoverer {
	
	private int iterations;
	private List<String> users;
	private GraphManager graphManager;
	private TwitterManager twitterManager;
	private List<FilterManager> filterManagers;
	
	
	
	public InfluencersDiscoverer(int iterations, 
								  List<String> users,
								  GraphManager graphManager, 
								  TwitterManager twitterManager,
								  List<FilterManager> filterManagers) {
		super();
		this.iterations = iterations;
		this.users = users;
		this.graphManager = graphManager;
		this.twitterManager = twitterManager;
		this.filterManagers = filterManagers;
	}



	public List<String> getInfluencers()  {
		return null;
	}
}
