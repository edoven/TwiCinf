package it.cybion.influencers.crawler.filtering;


import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.cache.TwitterFacade;

import java.util.List;



public interface FilterManager
{
	public void setTwitterFacade(TwitterFacade twitterManager);
	public void setGraphFacade(GraphFacade graphFacade);
	public void setSeedUsers(List<Long> users);
	
	public List<Long> filter();
}
