package it.cybion.influencers;


import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.aggregation.OrFilterManager;
import it.cybion.influencers.filtering.contentbased.DescriptionAndStatusDictionaryFilterManager;
import it.cybion.influencers.filtering.language.LanguageDetectionFilterManager;
import it.cybion.influencers.filtering.topologybased.InAndOutDegreeFilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.graph.indexes.GraphIndexType;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;
import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


/*
 * EXAMPLE FILE:
 * 
 * 
 * iterations=2
 * seed_users_ids=4324,46643,4556234,3554634,435341
 * graph_dir_path=/home/user/temp/graphs
 * 
 * 
 * iterating_filters_count=3
 * 
 * iterating_filter_0_name=InAndOutDegreeFilterManager
 * iterating_filter_0_inDegreePercentageThreshold=0.01
 * iterating_filter_0_outDegreePercentageThreshold=0.05
 * 
 * iterating_filter_1_name=DescriptionAndStatusDictionaryFilterManager
 * iterating_filter_1_dictionary=word1,word2,word3
 * 
 * iterating_filter_2_name=OrFilterManager
 * iterating_filter_2.0_name=InAndOutDegreeFilterManager
 * iterating_filter_2_filters_count=2
 * iterating_filter_2.0_inDegreePercentageThreshold=0.01
 * iterating_filter_2.0_outDegreePercentageThreshold=0.05
 * iterating_filter_2.1_name=DescriptionAndStatusDictionaryFilterManager
 * iterating_filter_2.1_dictionary=word1,word2,word3
 * 
 * 
 * finalizing_filters_count=1
 * 
 * finalizing_filter_0_name=LanguageDetectionFilterManager
 * finalizing_filter_0_language=it
 * finalizing_filter_0_languageProfilesDir=/opt/langDetect/profiles
 * 
 */


public class ConfigurationFileParser
{

	private static final Logger logger = Logger.getLogger(ConfigurationFileParser.class);
	
	private enum filtersManagers
	{
		InAndOutDegreeFilterManager,
		DescriptionAndStatusDictionaryFilterManager,
		OrFilterManager,
		LanguageDetectionFilterManager
	}

	public static InfluencersDiscoverer getInfluencersDiscovererFromConfiguration(String configFilePath) throws IOException
	{
		Properties properties = new Properties();
		properties.load(new FileInputStream(configFilePath));
		
		int iterations = getIterations(properties);		
		TwitterFacade twitterFacade = getTwitterFacade();	
		GraphFacade graphFacade = getGraphFacade(properties);
		List<Long> seedUsersIds = getSeedUsersIds(properties);
		System.out.println(seedUsersIds);
		List<FilterManager> iteratingFilters = getIteratingFilters(properties);
		System.out.println(iteratingFilters);	
		List<FilterManager> finalizingFilters = getFinalizingFilters(properties);
		System.out.println(finalizingFilters);
		InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscovererBuilder()
														.giveMeAnInfluenceDiscoverer()
														.iteratingFor(iterations)
														.startingFromUserIds(seedUsersIds)
														.usingGraphFacade(graphFacade)
														.usingTwitterFacade(twitterFacade)
														.iteratingWith(iteratingFilters)
														.finalizingWith(finalizingFilters)
														.build();
														
		if (finalizingFilters.size()>0)
			influencersDiscoverer.setFinalizationFilters(finalizingFilters);
																				 
																				 
		return influencersDiscoverer;
	}

	private static TwitterFacade getTwitterFacade() throws UnknownHostException
	{
		return TwitterFacadeFactory.getTwitterFacade();
	}
	
	private static GraphFacade getGraphFacade(Properties properties) throws IOException
	{
		String graphDirPath = (String) properties.get("graph_dir_path");
		FilesDeleter.delete(new File(graphDirPath));
		GraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath,GraphIndexType.TREEMAP);
		return graphFacade;
	}
	
	private static int getIterations(Properties properties)
	{
		return Integer.parseInt( (String) properties.get("iterations") );
	}

	private static List<Long> getSeedUsersIds(Properties properties)
	{
		String usersIdsString = (String) properties.get("seed_users_ids");
		List<String> usersIdsStringList = Arrays.asList(usersIdsString.split(","));
		List<Long> usersIds = new ArrayList<Long>();
		for (String userIdString : usersIdsStringList)
		{
			usersIds.add(Long.parseLong(userIdString));
		}
		return usersIds;
	}

	private static List<FilterManager> getIteratingFilters(Properties properties)
	{
		List<FilterManager> filterManagers = new ArrayList<FilterManager>();
		int iteratingFiltersCount = Integer.parseInt((String) properties.get("iterating_filters_count"));
		for (int i = 0; i < iteratingFiltersCount; i++)
			filterManagers.add(i, getFilter("iterating", properties, i, -1));
		return filterManagers;
	}

	
	private static List<FilterManager> getFinalizingFilters(Properties properties)
	{
		List<FilterManager> filterManagers = new ArrayList<FilterManager>();
		if (!properties.containsKey("finalizing_filters_count"))
			return filterManagers;
		int iteratingFiltersCount = Integer.parseInt((String) properties.get("finalizing_filters_count"));
		for (int i = 0; i < iteratingFiltersCount; i++)
			filterManagers.add(i, getFilter("finalizing", properties, i, -1));
		return filterManagers;
	}
	
	private static FilterManager getFilter(String filterType, Properties properties, int filterIndex, int aggregatorFilterIndex)
	{
		//the basic first part filter string is iterating_filter_<filterIndex>
		String filterStringFirstPart = filterType+"_filter_" + filterIndex + "_";
		//the first part filter of an aggregated filter is iterating_filter_<aggregatorFilterIndex>.<filterIndex>
		if (aggregatorFilterIndex!=-1)
			filterStringFirstPart = filterType+"_filter_" + aggregatorFilterIndex + "." + filterIndex+ "_";
		
		String filterManagerNameKeyString = filterStringFirstPart + "name";
		String filterManagerName = (String) properties.get(filterManagerNameKeyString);

		switch (filtersManagers.valueOf(filterManagerName))
		{
			case InAndOutDegreeFilterManager:
			{
				String inDegreePercentageThresholdKeyString = filterStringFirstPart + "inDegreePercentageThreshold";
				String inDegreePercentageThresholdString = (String) properties.get(inDegreePercentageThresholdKeyString);
				float inDegreePercentageThreshold = Float.parseFloat(inDegreePercentageThresholdString);
				String outDegreePercentageThresholdKeyString = filterStringFirstPart + "outDegreePercentageThreshold";
				String outDegreePercentageThresholdString = (String) properties.get(outDegreePercentageThresholdKeyString);
				float outDegreePercentageThreshold = Float.parseFloat(outDegreePercentageThresholdString);
				FilterManager inAndOutDegreeFilterManager = new InAndOutDegreeFilterManager(inDegreePercentageThreshold, outDegreePercentageThreshold);
				return inAndOutDegreeFilterManager;
			}
			case DescriptionAndStatusDictionaryFilterManager:
			{
				String dictionaryKeyString = filterStringFirstPart + "dictionary";
				String dictionaryString = (String) properties.get(dictionaryKeyString);
				List<String> dictionary = Arrays.asList(dictionaryString.split(","));
				FilterManager descriptionAndStatusDictionaryFilterManager = new DescriptionAndStatusDictionaryFilterManager(dictionary);
				return descriptionAndStatusDictionaryFilterManager;
			}
			case OrFilterManager:
			{
				String aggregationFiltersCount = filterStringFirstPart + "filters_count";
				int filtersCount = Integer.parseInt( (String)properties.getProperty(aggregationFiltersCount));
				List<FilterManager> filterManagers = new ArrayList<FilterManager>();
				for (int i=0; i<filtersCount; i++)
					filterManagers.add(getFilter(filterType, properties, i, filterIndex));
				FilterManager orFilterManager = new OrFilterManager(filterManagers);
				return orFilterManager;
			}
			case LanguageDetectionFilterManager:
			{
				String languageProfilesDir = filterStringFirstPart + "languageProfilesDir";
				String language = filterStringFirstPart + "language";			
				FilterManager languageDetectionFilterManager = new LanguageDetectionFilterManager(languageProfilesDir,language);
				return languageDetectionFilterManager;
			}
			default: 
			{
				logger.info("ERROR: Type of filter unknown:" + filterManagerName);
				System.exit(0);
				return null;
			}
		}
	}
}
