package it.cybion.influencers.crawler;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.filtering.FilterManagerDescription;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.crawler.launcher.parsing.FilterManagerDescriptionInterpreter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class Crawler
{	
	private static final Logger LOGGER = Logger.getLogger(Crawler.class);
	

	private int iterations;
	private List<Long> users;
	private GraphFacade graphFacade;
	private TwitterCache twitterFacade;
	private List<FilterManagerDescription> iteratingFiltersDescriptions;
	private List<FilterManagerDescription> finalizationFiltersDescriptions;
	private Set<Long> resultsFromIterations = new HashSet<Long>();


	public Crawler() 
	{
//		setDefaultLogger();
	}
	
	
	public void setIterations(int iterations){this.iterations = iterations;	}
	public void setGraphFacade(GraphFacade graphFacade){this.graphFacade = graphFacade;}	
	public void setTwitterFacade(TwitterCache twitterFacade){this.twitterFacade = twitterFacade;}
	public void setIteratingFiltersDescriptions(List<FilterManagerDescription> iteratingFiltersDescriptions){this.iteratingFiltersDescriptions = iteratingFiltersDescriptions;}
	public void setUsersIds(List<Long> usersIds){this.users = usersIds;}
	public void setUsersScreenNames(List<String> screenNames){this.users = twitterFacade.getUserIds(screenNames);}
	public void setFinalizationFiltersDescriptions(List<FilterManagerDescription> finalizationFiltersDescriptions){this.finalizationFiltersDescriptions = finalizationFiltersDescriptions;}
	
//	public void setFileLogger(String filename) throws IOException
//	{
//		logger = Logger.getLogger(this.getClass());
//		Layout layout = new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
//		Appender appender = new FileAppender(layout, filename);
//		appender.setLayout(layout);
//		logger.addAppender(appender);
//	}
	
//	private void setDefaultLogger()
//	{
//		logger = Logger.getLogger(this.getClass());
//		Appender appender = new ConsoleAppender();
//		Layout layout = new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
//		appender.setLayout(layout);
//		logger.addAppender(appender);	
//	}
	
	
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
				LOGGER.info(filterManager.toString());
				users = filterManager.filter();
				printInfoOnResultFromFilter(users);				
			}
			resultsFromIterations.addAll(users);
			
			LOGGER.info("### ITERATION " + (iterationIndex + 1) + " is finished ###");
			LOGGER.info("users.size()=" + users.size());
			LOGGER.info("resultsFromIterations.size()=" + resultsFromIterations.size());
		}
		printInfoAfterIterationsAreFinished(resultsFromIterations);
		
		
		if (finalizationFiltersDescriptions == null)
			return new ArrayList<Long>(resultsFromIterations);
		else
		{				
			LOGGER.info("#### FINALIZING FILTERS #####");
			LOGGER.info("");
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
				LOGGER.info(filterManager.toString());
				users = filterManager.filter();
				printInfoOnResultFromFilter(users);
			}
			
			
			LOGGER.info("############################################");
			LOGGER.info("");
			LOGGER.info("################# FINISHED #################");
			LOGGER.info("");
			LOGGER.info("############################################");
			return users;		
		}
	}
	
	
	private void printInitInfo()
	{
		LOGGER.info("############################################");
		LOGGER.info("Iterations =  " + iterations);
		LOGGER.info("--Iteration Filters--");
		for (int filterIndex = 0; filterIndex < iteratingFiltersDescriptions.size(); filterIndex++)
		{
			FilterManagerDescription filterManagerDescription = iteratingFiltersDescriptions.get(filterIndex);
			FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
			LOGGER.info(filterIndex + ") " + filterManager.toString());
		}

		if (finalizationFiltersDescriptions != null)
		{
			LOGGER.info("--Finalization Filters--");
			for (int filterIndex = 0; filterIndex < finalizationFiltersDescriptions.size(); filterIndex++)
			{
				FilterManagerDescription filterManagerDescription = finalizationFiltersDescriptions.get(filterIndex);
				FilterManager filterManager = FilterManagerDescriptionInterpreter.getFilterManagerFromDescription(filterManagerDescription);
				LOGGER.info(filterIndex + ") " + filterManager.toString());
			}
		}
		LOGGER.info("############################################");
		LOGGER.info("");
		LOGGER.info("");
	}
	
	private void printInfoOnIteration(int iterationIndex)
	{
		LOGGER.info("");
		LOGGER.info("");
		LOGGER.info("");
		LOGGER.info("#### ITERATION " + (iterationIndex + 1) + " #####");
		LOGGER.info("");
	}
	
	private void printInfoOnFilter(int filterIndex, int size)
	{
		LOGGER.info("");
		LOGGER.info("");
		LOGGER.info("#### filter " + (filterIndex + 1) + "/" + size + " ####");
		LOGGER.info("");
	}
	
	private void printInfoAfterIterationsAreFinished(Set<Long> resultsFromIterations)
	{
		LOGGER.info("");
		LOGGER.info("");
		LOGGER.info("results of iteration filters = " + resultsFromIterations);
		LOGGER.info("results of iteration filters size = " + resultsFromIterations.size());
		LOGGER.info("");
		LOGGER.info("");
	}
	
	private void printInfoOnResultFromFilter(List<Long> users)
	{
		LOGGER.info("results from filtering = " + users);
		LOGGER.info("number of results from filtering = " + users.size());
	}
	
}
