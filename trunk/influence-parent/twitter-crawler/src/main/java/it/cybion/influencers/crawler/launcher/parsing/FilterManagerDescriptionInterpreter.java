package it.cybion.influencers.crawler.launcher.parsing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.filtering.FilterManagerDescription;
import it.cybion.influencers.crawler.filtering.topologybased.InAndOutDegreeFilterManager;


public class FilterManagerDescriptionInterpreter
{
	private static final Logger logger = Logger.getLogger(FilterManagerDescriptionInterpreter.class);
	
	
	public static FilterManager getFilterManagerFromDescription(FilterManagerDescription description)
	{
		String filterManagerName = description.getFilterManagerName();
		Map<String, Object> parameterName2ParameterValue = description.getParameterName2ParameterValue();
		FilterManager filterManger = null;
		Constructor constructor ;
				
		try
		{
			if(filterManagerName.equals("InAndOutDegreeFilterManager"))
			{
				Class<InAndOutDegreeFilterManager> InAndOutDegreeFilterManagerClass = (Class<InAndOutDegreeFilterManager>) Class.forName("it.cybion.influencers.crawler.filtering.topologybased."+filterManagerName);
				constructor = InAndOutDegreeFilterManagerClass.getConstructor(Float.class, Float.class);
				Float inDegreeThreshold = (Float)parameterName2ParameterValue.get("inDegreePercentageThreshold");
				Float outDegreeThreshold = (Float)parameterName2ParameterValue.get("outDegreePercentageThreshold");
				filterManger = (FilterManager) constructor.newInstance(inDegreeThreshold,outDegreeThreshold	);	
			}				
			else if(filterManagerName.equals("DescriptionAndStatusDictionaryFilterManager"))
			{
				constructor = Class.forName("it.cybion.influencers.crawler.filtering.contentbased."+filterManagerName).getConstructor(List.class);
				filterManger = (FilterManager) constructor.newInstance((List<String>)parameterName2ParameterValue.get("dictionary"));	
			}
			else if(filterManagerName.equals("OutDegreeFilterManager"))
			{
				constructor = Class.forName(filterManagerName).getConstructor(Float.class);
				filterManger = (FilterManager) constructor.newInstance((Float)parameterName2ParameterValue.get("outDegreePercentageThreshold"));
			}
			else if(filterManagerName.equals("InDegreeFilterManager"))
			{
				constructor = Class.forName("it.cybion.influencers.crawler.filtering.topologybased."+filterManagerName).getConstructor(Float.class);
				filterManger = (FilterManager) constructor.newInstance((Float)parameterName2ParameterValue.get("inDegreePercentageThreshold"));
			}
			else if(filterManagerName.equals("OrFilterManager"))
			{
				constructor = Class.forName("it.cybion.influencers.crawler.filtering.aggregation."+filterManagerName).getConstructor(List.class);
				List<FilterManagerDescription> filterManagersDescriptions = 
							(List<FilterManagerDescription>)parameterName2ParameterValue.get("filterManagersDescriptions");
				List<FilterManager> orFilterManagers = new ArrayList<FilterManager>();
				for (FilterManagerDescription filterManagerDescription : filterManagersDescriptions)
					orFilterManagers.add(getFilterManagerFromDescription(filterManagerDescription));
				filterManger = (FilterManager) constructor.newInstance(orFilterManagers);
			}
			else if(filterManagerName.equals("LanguageDetectionFilterManager"))
			{
				constructor = Class.forName("it.cybion.influencers.crawler.filtering.language."+filterManagerName).getConstructor(String.class, String.class);					
				filterManger = (FilterManager) constructor.newInstance(
											(Float)parameterName2ParameterValue.get("languageProfilesDir"),
											(Float)parameterName2ParameterValue.get("language"));
			}
			else
			{
				logger.info("Sorry, I don't know any filter with this name: "+filterManagerName);
				System.exit(0);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return filterManger;
	}
			
}
