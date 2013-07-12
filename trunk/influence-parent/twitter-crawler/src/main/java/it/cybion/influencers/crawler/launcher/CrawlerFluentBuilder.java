package it.cybion.influencers.crawler.launcher;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.crawler.Crawler;
import it.cybion.influencers.crawler.filtering.FilterManagerDescription;
import it.cybion.influencers.crawler.graph.GraphFacade;
import org.apache.log4j.Logger;

import java.util.List;

/*
 * This class is used to build InfluenceDiscoverer object with fluent-builder patter.
 * 
 * 
 * Usage:
 * 
 * InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscovererBuilder()
 * 														.withIterations(int)
 * 														.usingGraphFacade(GraphFacade)
 * 														.usingTwitterFacade(TwitterFacade)
 * 														.startingFromScreenNames(List<String>) // or .startingFromUserIds(List<Long>)
 * 														.iteratingWith(List<FilterManagerDescription>)
 * 														.iteratingWith(List<FilterManagerDescription>)
 * 														.build();
 * 
 */
public class CrawlerFluentBuilder 
{
	private static final Logger LOGGER = Logger.getLogger(CrawlerFluentBuilder.class);
	
	private Crawler influencersDiscoverer;
	private List<String> screenNames;
	private boolean influencersDiscovererCreated = false,
					iterationsSet = false,
					graphFacadeSet = false,
					twitterFacadeSet = false,
					usersScreenNamesSet = false,
					usersIdsSet = false,
					iteratingFiltersSet = false,
					finalizingFiltersSet = false;
	
	public CrawlerFluentBuilder()
	{
	}
	
	public CrawlerFluentBuilder buildAnInfluenceDiscoverer()
	{
		influencersDiscoverer = new Crawler();
		influencersDiscovererCreated = true;
		return this;
	}
	
	public CrawlerFluentBuilder iteratingFor(int iterations)
	{
		influencersDiscoverer.setIterations(iterations);
		iterationsSet = true;
		return this;
	}
	
	public CrawlerFluentBuilder usingGraphFacade(GraphFacade graphFacade)
	{
		influencersDiscoverer.setGraphFacade(graphFacade);
		graphFacadeSet = true;
		return this;
	}
	
	public CrawlerFluentBuilder usingTwitterFacade(TwitterCache twitterFacade)
	{
		influencersDiscoverer.setTwitterFacade(twitterFacade);
		twitterFacadeSet = true;
		return this;
	}
	
	//screen-names have to be resolved into user id using TwitterFacade
	//so influencersDiscoverer.setUsersScreenNames(screenNames) is called in 
	//validate() method after verifying TwitterFacade has been set
	public CrawlerFluentBuilder startingFromScreenNames(List<String> screenNames)
	{
		this.screenNames = screenNames;
		usersScreenNamesSet = true;
		return this;
	}
	
	public CrawlerFluentBuilder startingFromUserIds(List<Long> userIds)
	{
		influencersDiscoverer.setUsersIds(userIds);
		usersIdsSet = true;
		return this;
	}
	
	public CrawlerFluentBuilder iteratingWith(List<FilterManagerDescription> iteratingFiltersDescriptions)
	{
		influencersDiscoverer.setIteratingFiltersDescriptions(iteratingFiltersDescriptions);
		iteratingFiltersSet = true;
		return this;
	}
	
	public CrawlerFluentBuilder finalizingWith(List<FilterManagerDescription> finalizingFiltersDescriptions)
	{
		influencersDiscoverer.setFinalizationFiltersDescriptions(finalizingFiltersDescriptions);
		finalizingFiltersSet = true;
		return this;
	}
	
	public Crawler build()
	{
		validate();
		return this.influencersDiscoverer;
	}

	private void validate()
	{
		if (influencersDiscovererCreated==false)
		{
			LOGGER.error(
                    "Error, you must ask for an InfluenceDiscoverer with giveMeAnInfluenceDiscoverer().");
			throw new IllegalArgumentException();
		}
		if (usersScreenNamesSet==true && usersIdsSet==true)
		{
			LOGGER.error("Error, you can't set both seeds as screen-names and ids.");
            throw new IllegalArgumentException();
		}
		if (usersScreenNamesSet==true && graphFacadeSet==true )
			influencersDiscoverer.setUsersScreenNames(screenNames);
				
			
		if ((usersScreenNamesSet==false && usersIdsSet==false) ||
			 iterationsSet == false ||
			 graphFacadeSet == false ||
			 twitterFacadeSet == false ||
			 iteratingFiltersSet == false)
		{
			LOGGER.error("Error, some parameters are missing. " +
                         "You must set iterations, GraphFacade, TwitterFacade, iteratingFilters" +
                         " ano one betweet seeds screen-names and ids.");
            throw new IllegalArgumentException();
		}
		if (finalizingFiltersSet==false)
			influencersDiscoverer.setFinalizationFiltersDescriptions(null);
			 
	}
}
