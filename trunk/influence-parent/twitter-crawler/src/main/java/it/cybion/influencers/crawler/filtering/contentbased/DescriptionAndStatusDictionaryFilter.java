package it.cybion.influencers.crawler.filtering.contentbased;

import it.cybion.influencers.crawler.filtering.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class DescriptionAndStatusDictionaryFilter implements Filter
{

	List<String> dictionary;
	Map<Long, String> user2description;

	public DescriptionAndStatusDictionaryFilter(List<String> dictionary, Map<Long, String> user2description)
	{
		super();
		this.dictionary = dictionary;
		this.user2description = user2description;
	}

	@Override
	public List<Long> filter()
	{
		List<Long> goodUsers = new ArrayList<Long>();
		for (Entry<Long, String> mapEntry : user2description.entrySet())
		{
			String description = mapEntry.getValue().toLowerCase();
			Long userId = mapEntry.getKey();
			for (String keyWord : dictionary)
				if (description.contains(keyWord))
				{
					goodUsers.add(userId);
					break;
				}
		}
		return goodUsers;
	}

}
