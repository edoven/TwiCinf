package properties;


import it.cybion.influencers.filtering.FilterManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;



public class PropertiesReader
{

	private enum filtersManagers
	{
		InAndOutDegreeFilterManager
	}

	public static void main(String[] args) throws IOException
	{
		Properties properties = new Properties();
		String propertiesFile = "/home/godzy/Desktop/food46.properties";
		FileInputStream fileInputStream = new FileInputStream(propertiesFile);
		properties.load(fileInputStream);
		
		List<Long> usersIds = getUsersIds(properties);
		System.out.println(usersIds);
		
		List<FilterManager> iteratingFilters = getIteratingFilters(properties);
		System.out.println(iteratingFilters);

	}

	private static List<Long> getUsersIds(Properties properties)
	{
		String usersIdsString = (String) properties.get("seed_users");
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
		int iteratingFiltersCount = Integer.parseInt( (String) properties.get("iterating_filters_count") );
		for (int i = 1; i <= iteratingFiltersCount; i++)
			filterManagers.add(getIteratingFilter(properties, i));
		return filterManagers;
	}

	private static FilterManager getIteratingFilter(Properties properties, int filterIndex)
	{
		String filterManagerNameKeyString = "itrating_filter_" + filterIndex + "_name";
		String filterManagerName = (String) properties.get(filterManagerNameKeyString);

		
		switch (filtersManagers.valueOf(filterManagerName))
		{
		case InAndOutDegreeFilterManager: {
			String inDegreePercentageThresholdKeyString = "itrating_filter_" +filterIndex + "_inDegreePercentageThreshold";
			float inDegreePercentageThreshold = Float.parseFloat( (String)properties.get(inDegreePercentageThresholdKeyString) );
			String outDegreePercentageThresholdKeyString = "itrating_filter_" +filterIndex + "_outDegreePercentageThreshold";
			float outDegreePercentageThreshold = Float.parseFloat( (String)properties.get(outDegreePercentageThresholdKeyString) );
			FilterManager inAndOutDegreeFilterManager = null;
			try
			{
				Class.forName("it.cybion.influencers.filtering.topologybased.InAndOutDegreeFilterManager").cast(inAndOutDegreeFilterManager);
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				System.exit(0);
			}		
			return inAndOutDegreeFilterManager;
		}

		default:
			return null;
		}

	}
}
