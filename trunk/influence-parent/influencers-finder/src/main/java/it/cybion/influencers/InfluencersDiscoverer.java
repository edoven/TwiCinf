package it.cybion.influencers;


import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;



public class InfluencersDiscoverer
{

	private static final Logger logger = Logger.getLogger(InfluencersDiscoverer.class);

	private int iterations;
	private List<Long> users;
	private GraphFacade graphFacade;
	private TwitterFacade twitterFacade;
	private List<FilterManager> toIterateFilters;
	private List<FilterManager> finalizationFilters;
	private Set<Long> resultsFromIterations = new HashSet<Long>();


	/*
	 * The costructor is empty and parameters are passed by builder pattern:
	 * 
	 * new InfluencersDiscoverer().setItarations(iterations)
	 * 							  .setGraphFacade(graphFacade)
								  .setToIterateFilters(iteratingFilters)
								  .setTwitterFacade(twitterFacade)
								  .setUsersScreenNames(screenNames);
	 */
	
	public InfluencersDiscoverer() 
	{
		this.toIterateFilters = null;
		this.twitterFacade = null;
	}
	
	public InfluencersDiscoverer setItarations(int iterations)
	{
		this.iterations = iterations;
		return this;
	}
	public InfluencersDiscoverer setGraphFacade(GraphFacade graphFacade)
	{
		this.graphFacade = graphFacade;
		return this;
	}	
	public InfluencersDiscoverer setTwitterFacade(TwitterFacade twitterFacade)
	{
		this.twitterFacade = twitterFacade;
		return this;
	}
	public InfluencersDiscoverer setToIterateFilters(List<FilterManager> toIterateFilters)
	{
		this.toIterateFilters = toIterateFilters;
		return this;
	}
	public InfluencersDiscoverer setUsersIds(List<Long> usersIds)
	{
		this.users = usersIds;
		return this;
	}
	public InfluencersDiscoverer setUsersScreenNames(List<String> screenNames)
	{
		if (twitterFacade==null)
		{
			logger.info("Error! You can't call setUsersScreenName if twitterFacade is not set!");
			System.exit(0);
		}
		this.users = twitterFacade.getUserIds(screenNames);
		return this;
	}
	
	public InfluencersDiscoverer setFinalizationFilters(List<FilterManager> finalizationFilters)
	{
		this.finalizationFilters = finalizationFilters;
		return this;
	}
	
	

	
	public List<Long> getInfluencers()
	{

		printInfo();

		for (int iterationIndex = 0; iterationIndex < iterations; iterationIndex++)
		{
			logger.info("");
			logger.info("");
			logger.info("#### ITERATION " + (iterationIndex + 1) + " #####");
			logger.info("");
			for (int filterIndex = 0; filterIndex < toIterateFilters.size(); filterIndex++)
			{
				FilterManager filterManager = toIterateFilters.get(filterIndex);
				logger.info("");
				logger.info("");
				logger.info("#### filter " + (filterIndex + 1) + "/" + toIterateFilters.size() + " ####");
				logger.info("");
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				filterManager.setSeedUsers(users);
				logger.info(filterManager.toString());
				users = filterManager.filter();
				logger.info("results from filtering = " + users);
				logger.info("number of results from filtering = " + users.size());
			}
			resultsFromIterations.addAll(users);
		}
		
		logger.info("");
		logger.info("");
		logger.info("results of iteration filters = " + resultsFromIterations);
		logger.info("results of iteration filters size = " + resultsFromIterations.size());
		logger.info("");
		logger.info("");
			

		if (finalizationFilters != null)
		{			
			logger.info("#### FINALIZING FILTERS #####");
			logger.info("");
			for (int filterIndex = 0; filterIndex < finalizationFilters.size(); filterIndex++)
			{
				FilterManager filterManager = finalizationFilters.get(filterIndex);
				logger.info("");
				logger.info("");
				logger.info("#### filter " + (filterIndex + 1) + "/" + toIterateFilters.size() + " ####");
				logger.info("");
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				if (filterIndex == 0)
					filterManager.setSeedUsers(new ArrayList<Long>(resultsFromIterations));
				else
					filterManager.setSeedUsers(users);
				logger.info(filterManager.toString());
				users = filterManager.filter();
				logger.info("results from filtering = " + users);
				logger.info("number of results from filtering = " + users.size());
			}		
		}		
		return users;
	}

	private void printInfo()
	{
		logger.info("############################################");
		logger.info("Iterations =  " + iterations);
		logger.info("--Iteration Filters--");
		for (int filterIndex = 0; filterIndex < toIterateFilters.size(); filterIndex++)
		{
			FilterManager filterManager = toIterateFilters.get(filterIndex);
			logger.info(filterIndex + ") " + filterManager.toString());
		}

		if (finalizationFilters != null)
		{
			logger.info("--Finalization Filters--");
			for (int filterIndex = 0; filterIndex < finalizationFilters.size(); filterIndex++)
			{
				FilterManager filterManager = finalizationFilters.get(filterIndex);
				logger.info(filterIndex + ") " + filterManager.toString());
			}
		}
		logger.info("############################################");
		logger.info("");
		logger.info("");
	}
}
