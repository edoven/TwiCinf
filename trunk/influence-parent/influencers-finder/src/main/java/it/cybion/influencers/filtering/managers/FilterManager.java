package it.cybion.influencers.filtering.managers;

import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.List;

public interface FilterManager {
	
	public List<Long> filter();
	public void setTwitterFacade(TwitterFacade twitterManager);
	public void setGraphFacade(GraphFacade graphFacade);
	public void setSeedUsers(List<Long> users);
}
