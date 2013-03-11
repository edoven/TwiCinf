package it.cybion.influencers;


import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.FilterManagerDescription;
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
	private List<FilterManagerDescription> iteratingFiltersDescriptions;
	private List<FilterManagerDescription> finalizationFiltersDescriptions;
	private Set<Long> resultsFromIterations = new HashSet<Long>();


	public InfluencersDiscoverer() {}
	
	public void setItarations(int iterations){this.iterations = iterations;	}
	public void setGraphFacade(GraphFacade graphFacade){this.graphFacade = graphFacade;}	
	public void setTwitterFacade(TwitterFacade twitterFacade){this.twitterFacade = twitterFacade;}
	public void setIteratingFiltersDescriptions(List<FilterManagerDescription> iteratingFiltersDescriptions){this.iteratingFiltersDescriptions = iteratingFiltersDescriptions;}
	public void setUsersIds(List<Long> usersIds){this.users = usersIds;}
	public void setUsersScreenNames(List<String> screenNames){this.users = twitterFacade.getUserIds(screenNames);}
	public void setFinalizationFiltersDescriptions(List<FilterManagerDescription> finalizationFiltersDescriptions){this.finalizationFiltersDescriptions = finalizationFiltersDescriptions;}
	
	
	public List<Long> getInfluencers()
	{
		printInitInfo();
		for (int iterationIndex = 0; iterationIndex < iterations; iterationIndex++)
		{
			printInfoOnIteration(iterationIndex);
			for (int filterIndex = 0; filterIndex < iteratingFiltersDescriptions.size(); filterIndex++)
			{
				FilterManagerDescription filterManagerDescription = iteratingFiltersDescriptions.get(filterIndex);
				FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
				printInfoOnFilter(filterIndex, iteratingFiltersDescriptions.size());		
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				filterManager.setSeedUsers(users);
				logger.info(filterManager.toString());
				users = filterManager.filter();
				printInfoOnResultFromFilter(users);				
			}
			resultsFromIterations.addAll(users);
			
			logger.info("### ITERATION "+(iterationIndex+1)+" is finished ###");
			logger.info("users.size()="+users.size());
			logger.info("resultsFromIterations.size()="+resultsFromIterations.size());
		}
		printInfoAfterIterationsAreFinished(resultsFromIterations);
		
		
		if (finalizationFiltersDescriptions == null)
			return new ArrayList<Long>(resultsFromIterations);
		else
		{				
			logger.info("#### FINALIZING FILTERS #####");
			logger.info("");
			for (int filterIndex = 0; filterIndex < finalizationFiltersDescriptions.size(); filterIndex++)
			{
				FilterManagerDescription filterManagerDescription = finalizationFiltersDescriptions.get(filterIndex);
				FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
				printInfoOnFilter(filterIndex, finalizationFiltersDescriptions.size());
				filterManager.setGraphFacade(graphFacade);
				filterManager.setTwitterFacade(twitterFacade);
				if (filterIndex == 0)
					filterManager.setSeedUsers(new ArrayList<Long>(resultsFromIterations));
				else
					filterManager.setSeedUsers(users);
				logger.info(filterManager.toString());
				users = filterManager.filter();
				printInfoOnResultFromFilter(users);
			}		
			return users;		
		}
	}

	
	
	
	private void printInitInfo()
	{
		logger.info("############################################");
		logger.info("Iterations =  " + iterations);
		logger.info("--Iteration Filters--");
		for (int filterIndex = 0; filterIndex < iteratingFiltersDescriptions.size(); filterIndex++)
		{
			FilterManagerDescription filterManagerDescription = iteratingFiltersDescriptions.get(filterIndex);
			FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
			logger.info(filterIndex + ") " + filterManager.toString());
		}

		if (finalizationFiltersDescriptions != null)
		{
			logger.info("--Finalization Filters--");
			for (int filterIndex = 0; filterIndex < finalizationFiltersDescriptions.size(); filterIndex++)
			{
				FilterManagerDescription filterManagerDescription = finalizationFiltersDescriptions.get(filterIndex);
				FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
				logger.info(filterIndex + ") " + filterManager.toString());
			}
		}
		logger.info("############################################");
		logger.info("");
		logger.info("");
	}
	private void printInfoOnIteration(int iterationIndex)
	{
		logger.info("");
		logger.info("");
		logger.info("");
		logger.info("#### ITERATION " + (iterationIndex + 1) + " #####");
		logger.info("");	
	}
	private void printInfoOnFilter(int filterIndex, int size)
	{
		logger.info("");
		logger.info("");
		logger.info("#### filter " + (filterIndex + 1) + "/" + size + " ####");
		logger.info("");
	}
	private void printInfoAfterIterationsAreFinished(Set<Long> resultsFromIterations)
	{

		logger.info("");
		logger.info("");
		logger.info("results of iteration filters = " + resultsFromIterations);
		logger.info("results of iteration filters size = " + resultsFromIterations.size());
		logger.info("");
		logger.info("");	
	}
	private void printInfoOnResultFromFilter(List<Long> users)
	{
		logger.info("results from filtering = " + users);
		logger.info("number of results from filtering = " + users.size());
	}
	
}
