package it.cybion.influencers.crawler.filtering.aggregation;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.cache.TwitterCache;


			 
public class OrFilterManager implements FilterManager
{

	private List<FilterManager> filterMangers;
	private GraphFacade graphFacade;
	private List<Long> users;
	private TwitterCache twitterFacade;

	public OrFilterManager(List<FilterManager> filterMangers)
	{
		this.filterMangers = filterMangers;
	}

	@Override
	public List<Long> filter()
	{
		Set<Long> filtered = new HashSet<Long>();
		for (FilterManager filterManager : filterMangers)
		{
			filterManager.setGraphFacade(graphFacade);		
			filterManager.setTwitterFacade(twitterFacade);		
			filterManager.setSeedUsers(users);
			List<Long> resultFromFilter = filterManager.filter();
			filtered.addAll(resultFromFilter);
		}
		return new ArrayList<Long>(filtered);
	}

	@Override
	public void setTwitterFacade(TwitterCache twitterFacade)
	{
		this.twitterFacade = twitterFacade;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade)
	{
		this.graphFacade = graphFacade;
	}

	@Override
	public void setSeedUsers(List<Long> users)
	{
		this.users = users;
	}

	@Override
	public String toString()
	{
		String description = "OrFilter = (  ";
		for (FilterManager filterManager : filterMangers)
			description = description + " ### " + filterManager.toString();
		description = description + "  )";
		return description;
	}

}
