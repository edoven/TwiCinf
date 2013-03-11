package it.cybion.influencers.filtering;


import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.List;



public interface FilterManager
{
	public void setTwitterFacade(TwitterFacade twitterManager);
	public void setGraphFacade(GraphFacade graphFacade);
	public void setSeedUsers(List<Long> users);
	
	public List<Long> filter();
}
