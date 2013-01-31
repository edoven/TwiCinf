package it.cybion.influencers.filtering.aggregation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

public class OrFilterManager implements FilterManager{
	
	private List<FilterManager> filterMangers;
	private GraphFacade graphFacade;
	private List<Long> users;
	private TwitterFacade twitterManager;
	
	public OrFilterManager(List<FilterManager> filterMangers) {
		this.filterMangers = filterMangers;
	}

	@Override
	public List<Long> filter() {
		Set<Long> filtered = new HashSet<Long>();
		for (FilterManager filterManager : filterMangers) {
			filterManager.setGraphFacade(graphFacade); 
			filterManager.setSeedUsers(users);
			filterManager.setTwitterFacade(twitterManager);
			filtered.addAll(filterManager.filter());
		}
		return new ArrayList<Long>(filtered);
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterManager) {
		this.twitterManager = twitterManager;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		this.graphFacade = graphFacade;
	}

	@Override
	public void setSeedUsers(List<Long> users) {
		this.users = users;			
	}
	
	@Override
	public String toString() {
		String description = "OrFilter = (  ";
		for (FilterManager filterManager : filterMangers)
			description = description + " ### " + filterManager.toString();
		description = description + "  )";
		return description;
	}

}
