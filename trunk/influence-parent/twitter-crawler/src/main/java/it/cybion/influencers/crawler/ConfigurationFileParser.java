package it.cybion.influencers.crawler;



import it.cybion.influencers.cache.TwitterFacade;
import it.cybion.influencers.cache.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.Twitter4jWebFacade;
import it.cybion.influencers.cache.web.TwitterWebFacade;

import it.cybion.influencers.crawler.filtering.FilterManagerDescription;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.crawler.graph.Neo4jGraphFacade;
import it.cybion.influencers.crawler.graph.indexes.GraphIndexType;
import it.cybion.influencers.crawler.utils.FilesDeleter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


/*
 * This class parses a configuration file and returns an InfluencersDiscoverer
 * 
 * 
 * CONFIGURATION EXAMPLE FILE:
 * 
 * 

#
#
#	MONGODB CONFIG
#
#
mongodb_host=localhost
mongodb_db=twitter


#
#
#	TWITTER WEB FACADE CONFIG
#
#
application_token_path=/home/godzy/tokens/consumerToken.properties
user_token_0_path=/home/godzy/tokens/token0.properties
user_token_1_path=/home/godzy/tokens/token1.properties
user_token_2_path=/home/godzy/tokens/token2.properties
user_token_3_path=/home/godzy/tokens/token3.properties
user_token_4_path=/home/godzy/tokens/token4.properties
user_token_5_path=/home/godzy/tokens/token5.properties

#
#
#	GRAPHDB CONFIG
#
#
graph_dir_path=/home/godzy/Desktop/laPerlaTempGraphs


#
#
#	APPLICATION CONFIG
#
#
iterations=2
seed_users_screenNames=Fashionista_com,voguemagazine,ELLEmagazine,marieclaire,RachelZoe,TwitterFashion



#
#	ITERATING FILTERS CONFIG
#
iterating_filters_count=2

iterating_filter_0_name=OrFilterManager
iterating_filter_0_filters_count=2
iterating_filter_0.0_name=InAndOutDegreeFilterManager
iterating_filter_0.0_inDegreePercentageThreshold=0.05
iterating_filter_0.0_outDegreePercentageThreshold=0.1
iterating_filter_0.1_name=InDegreeFilterManager
iterating_filter_0.1_inDegreePercentageThreshold=0.15
iterating_filter_1_name=DescriptionAndStatusDictionaryFilterManager
iterating_filter_1_dictionary=moda,fashion,outfit,street style,cool hunter,scarpe,shoes,accessori,abito,dress,eleganza,elegance,lifestyle,chic,glamour,lingerie

#
#	FINALIZING FILTERS CONFIG
#

#finalizing_filters_count=1

#finalizing_filter_0_name=LanguageDetectionFilterManager
#finalizing_filter_0_language=it
#finalizing_filter_0_languageProfilesDir=/opt/langDetect/profiles



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
		LanguageDetectionFilterManager,
		InDegreeFilterManager,
		OutDegreeFilterManager
	}

	public static InfluencersDiscoverer getInfluencersDiscovererFromConfiguration(String configFilePath) throws IOException
	{
		Properties properties = new Properties();
		properties.load(new FileInputStream(configFilePath));
		
		int iterations = getIterations(properties);		
		TwitterFacade twitterFacade = getTwitterFacade(properties);	
		GraphFacade graphFacade = getGraphFacade(properties);
		List<Long> seedUsersIds = getSeedUsersIds(properties);
		List<FilterManagerDescription> iteratingFiltersDescriptions = getIteratingFiltersDescriptions(properties);
		List<FilterManagerDescription> finalizingFiltersDescriptions = getFinalizingFiltersDescriptions(properties);
		List<String> seedUsersScreenNames = getSeedUsersScreenNames(properties);
		InfluencersDiscoverer influencersDiscoverer = null;
		
		if (seedUsersIds==null && seedUsersScreenNames==null)
		{
			logger.info("Error. You can't set both user ids and screen-names. Chose one.");
			System.exit(0);
		}
		else
		{
			if (seedUsersIds!=null)
				influencersDiscoverer = new InfluencersDiscovererBuilder()
					.buildAnInfluenceDiscoverer()
						.startingFromUserIds(seedUsersIds)
						.iteratingFor(iterations)
						.usingGraphFacade(graphFacade)
						.usingTwitterFacade(twitterFacade)
						.iteratingWith(iteratingFiltersDescriptions)
					.build();
			else
			{
				if (seedUsersScreenNames!=null)
					influencersDiscoverer = new InfluencersDiscovererBuilder()
						.buildAnInfluenceDiscoverer()
							.startingFromScreenNames(seedUsersScreenNames)
							.iteratingFor(iterations)
							.usingGraphFacade(graphFacade)
							.usingTwitterFacade(twitterFacade)
							.iteratingWith(iteratingFiltersDescriptions)
						.build();
				else
				{
					logger.info("Error. You must set user ids or screen-names.");
					System.exit(0);
				}
					
			}
		}
		
														
		if (finalizingFiltersDescriptions.size()>0)
			influencersDiscoverer.setFinalizationFiltersDescriptions(finalizingFiltersDescriptions);
		else
			influencersDiscoverer.setFinalizationFiltersDescriptions(null);
																				 
																				 
		return influencersDiscoverer;
	}

	private static TwitterFacade getTwitterFacade(Properties properties) throws UnknownHostException
	{
		
		String mongodbHost = properties.getProperty("mongodb_host");
		String mongodbTwitterDb = properties.getProperty("mongodb_db");
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade(mongodbHost, mongodbTwitterDb);

		String applicationTokenPath = properties.getProperty("application_token_path");
		Token applicationToken = new Token(applicationTokenPath);	
		List<Token> userTokens = new ArrayList<Token>();	
		int i=0;
		String userTokenPath;
		while ((userTokenPath =  properties.getProperty("user_token_"+i+"_path")) != null)
		{
			userTokens.add(new Token(userTokenPath));
			i++;
		}
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
			
		return new TwitterFacade(twitterWebFacade, persistanceFacade);
		
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
		if (usersIdsString==null)
			return null;
		List<String> usersIdsStringList = Arrays.asList(usersIdsString.split(","));
		List<Long> usersIds = new ArrayList<Long>();
		for (String userIdString : usersIdsStringList)
		{
			usersIds.add(Long.parseLong(userIdString));
		}
		return usersIds;
	}
	
	private static List<String> getSeedUsersScreenNames(Properties properties)
	{
		String usersIdsString = (String) properties.get("seed_users_screenNames");
		if (usersIdsString==null)
			return null;
		List<String> usersScreenNames = Arrays.asList(usersIdsString.split(","));
		return usersScreenNames;
	}

	private static List<FilterManagerDescription> getIteratingFiltersDescriptions(Properties properties)
	{
		List<FilterManagerDescription> filterManagersDescriptions = new ArrayList<FilterManagerDescription>();
		int iteratingFiltersCount = Integer.parseInt((String) properties.get("iterating_filters_count"));
		for (int i = 0; i < iteratingFiltersCount; i++)
			filterManagersDescriptions.add(i, getFilterDescription("iterating", properties, i, -1));
		return filterManagersDescriptions;
	}

	
	private static List<FilterManagerDescription> getFinalizingFiltersDescriptions(Properties properties)
	{
		List<FilterManagerDescription> filterManagersDescriptions = new ArrayList<FilterManagerDescription>();
		if (!properties.containsKey("finalizing_filters_count"))
			return filterManagersDescriptions;
		int iteratingFiltersCount = Integer.parseInt((String) properties.get("finalizing_filters_count"));
		for (int i = 0; i < iteratingFiltersCount; i++)
			filterManagersDescriptions.add(i, getFilterDescription("finalizing", properties, i, -1));
		return filterManagersDescriptions;
	}
	
	private static FilterManagerDescription getFilterDescription(String filterType, Properties properties, int filterIndex, int aggregatorFilterIndex)
	{
		//the basic first part filter string is iterating_filter_<filterIndex>
		String filterStringFirstPart = filterType+"_filter_" + filterIndex + "_";
		//the first part filter of an aggregated filter is iterating_filter_<aggregatorFilterIndex>.<filterIndex>
		if (aggregatorFilterIndex!=-1)
			filterStringFirstPart = filterType+"_filter_" + aggregatorFilterIndex + "." + filterIndex+ "_";
		
		String filterManagerNameKeyString = filterStringFirstPart + "name";
		String filterManagerName = (String) properties.get(filterManagerNameKeyString);
		Map<String,Object> parameterName2ParameterValue = new HashMap<String,Object>();

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
				parameterName2ParameterValue.put("inDegreePercentageThreshold", inDegreePercentageThreshold);
				parameterName2ParameterValue.put("outDegreePercentageThreshold", outDegreePercentageThreshold);	
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
			}
			case InDegreeFilterManager:
			{
				String inDegreePercentageThresholdKeyString = filterStringFirstPart + "inDegreePercentageThreshold";
				String inDegreePercentageThresholdString = (String) properties.get(inDegreePercentageThresholdKeyString);
				float inDegreePercentageThreshold = Float.parseFloat(inDegreePercentageThresholdString);					
				parameterName2ParameterValue.put("inDegreePercentageThreshold", inDegreePercentageThreshold);
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
			}
			case OutDegreeFilterManager:
			{
				String outDegreePercentageThresholdKeyString = filterStringFirstPart + "outDegreePercentageThreshold";
				String outDegreePercentageThresholdString = (String) properties.get(outDegreePercentageThresholdKeyString);
				float outDegreePercentageThreshold = Float.parseFloat(outDegreePercentageThresholdString);			
				parameterName2ParameterValue.put("outDegreePercentageThreshold", outDegreePercentageThreshold);	
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
			}
			case DescriptionAndStatusDictionaryFilterManager:
			{
				String dictionaryKeyString = filterStringFirstPart + "dictionary";
				String dictionaryString = (String) properties.get(dictionaryKeyString);
				List<String> dictionary = Arrays.asList(dictionaryString.split(","));
				parameterName2ParameterValue.put("dictionary", dictionary);
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
			}
			case OrFilterManager:
			{
				String aggregationFiltersCount = filterStringFirstPart + "filters_count";
				int filtersCount = Integer.parseInt( (String)properties.getProperty(aggregationFiltersCount));
				List<FilterManagerDescription> filterManagersDescriptions = new ArrayList<FilterManagerDescription>();
				for (int i=0; i<filtersCount; i++)
					filterManagersDescriptions.add(getFilterDescription(filterType, properties, i, filterIndex));
				parameterName2ParameterValue.put("filterManagersDescriptions", filterManagersDescriptions);
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
			}
			case LanguageDetectionFilterManager:
			{
				String languageProfilesDir = filterStringFirstPart + "languageProfilesDir";
				String language = filterStringFirstPart + "language";			
				parameterName2ParameterValue.put("languageProfilesDir", languageProfilesDir);
				parameterName2ParameterValue.put("language", language);
				return new FilterManagerDescription(filterManagerName, parameterName2ParameterValue);
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
