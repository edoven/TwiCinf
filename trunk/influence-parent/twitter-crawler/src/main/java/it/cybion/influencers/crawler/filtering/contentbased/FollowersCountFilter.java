package it.cybion.influencers.crawler.filtering.contentbased;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.cybion.influencers.crawler.filtering.Filter;



public class FollowersCountFilter implements Filter
{

	private int threshold;
	private Map<Long, Integer> user2followersCount;

	public FollowersCountFilter(Map<Long, Integer> user2followersCount, int threshold)
	{
		this.user2followersCount = user2followersCount;
		this.threshold = threshold;
	}

	@Override
	public List<Long> filter()
	{
		List<Long> filtered = new ArrayList<Long>();
		for (Long userId : user2followersCount.keySet())
		{
			int followersCount = user2followersCount.get(userId);
			if (followersCount >= threshold)
				filtered.add(userId);
		}
		return filtered;
	}

}
