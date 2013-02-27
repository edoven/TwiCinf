package it.cybion.influencers;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;

import java.util.List;

import org.apache.log4j.Logger;

/*
 * Usage:
 * 
 * InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscovererBuilder()
 * 														.withIterations(interations)
 * 														.usingGraphFacade(graphFacade)
 * 														.usingTwitterFacade(twitterFacade)
 * 														.startingFromScreenNames(screenNames)
 * 														.iteratingWith(iteratingFilters)
 * 														.build();
 * 
 */
public class InfluencersDiscovererBuilder 
{
	
	private static final Logger logger = Logger.getLogger(InfluencersDiscovererBuilder.class);
	
	private InfluencersDiscoverer influencersDiscoverer;
	private List<String> screenNames;
	private boolean influencersDiscovererCreated = false,
					iterationsSet = false,
					graphFacadeSet = false,
					twitterFacadeSet = false,
					usersScreenNamesSet = false,
					usersIdsSet = false,
					iteratingFiltersSet = false,
					finalizingFiltersSet = false;
	
	public InfluencersDiscovererBuilder()
	{
	}
	
	public InfluencersDiscovererBuilder buildAnInfluenceDiscoverer()
	{
		influencersDiscoverer = new InfluencersDiscoverer();
		influencersDiscovererCreated = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder iteratingFor(int iterations)
	{
		influencersDiscoverer.setItarations(iterations);
		iterationsSet = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder usingGraphFacade(GraphFacade graphFacade)
	{
		influencersDiscoverer.setGraphFacade(graphFacade);
		graphFacadeSet = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder usingTwitterFacade(TwitterFacade twitterFacade)
	{
		influencersDiscoverer.setTwitterFacade(twitterFacade);
		twitterFacadeSet = true;
		return this;
	}
	
	//screen-names have to be resolved into user id using TwitterFacade
	//so influencersDiscoverer.setUsersScreenNames(screenNames) is called in 
	//validate() method after verifying TwitterFacade has been set
	public InfluencersDiscovererBuilder startingFromScreenNames(List<String> screenNames)
	{
		this.screenNames = screenNames;
		usersScreenNamesSet = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder startingFromUserIds(List<Long> userIds)
	{
		influencersDiscoverer.setUsersIds(userIds);
		usersIdsSet = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder iteratingWith(List<FilterManager> iteratingFilters)
	{
		influencersDiscoverer.setToIterateFilters(iteratingFilters);
		iteratingFiltersSet = true;
		return this;
	}
	
	public InfluencersDiscovererBuilder finalizingWith(List<FilterManager> finalizingFilters)
	{
		influencersDiscoverer.setFinalizationFilters(finalizingFilters);
		finalizingFiltersSet = true;
		return this;
	}
	
	public InfluencersDiscoverer build()
	{
		validate();
		return this.influencersDiscoverer;
	}

	private void validate()
	{
		if (influencersDiscovererCreated==false)
		{
			logger.info("Error, you must ask for an InfluenceDiscoverer with giveMeAnInfluenceDiscoverer().");
			System.exit(0);
		}
		if (usersScreenNamesSet==true && usersIdsSet==true)
		{
			logger.info("Error, you can't set both seeds as screen-names and ids.");
			System.exit(0);
		}
		if (usersScreenNamesSet==true && graphFacadeSet==true )
			influencersDiscoverer.setUsersScreenNames(screenNames);
				
			
		if ((usersScreenNamesSet==false && usersIdsSet==false) ||
			 iterationsSet == false ||
			 graphFacadeSet == false ||
			 twitterFacadeSet == false ||
			 iteratingFiltersSet == false)
		{
			logger.info("Error, some parameters are missing. " +
						"You must set iterations, GraphFacade, TwitterFacade, iteratingFilters" +
						" ano one betweet seeds screen-names and ids.");
			System.exit(0);
		}
		if (finalizingFiltersSet==false)
			influencersDiscoverer.setFinalizationFilters(null);
			 
	}
}
