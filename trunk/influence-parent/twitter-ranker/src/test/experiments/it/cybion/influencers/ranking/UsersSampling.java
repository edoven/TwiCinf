package it.cybion.influencers.ranking;

import it.cybion.influencers.cache.TwitterCache;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

public class UsersSampling
{
	public static List<Long> getSamplingByFollowers(List<Long> usersIds, TwitterCache twitterCache)
	{
		List<Long> samples = new ArrayList<Long>();
//		twitterCache.getDescriptions(usersIds);
		int count0to500 = 0,
			count500to2000 = 0,
			count2000to10000 = 0,
			count10000to100000 = 0,
			count100000to500000 = 0,
			count500000toMax = 0;
		int samplingFactor = 100;
		
		
		//0-500
		int usersCount = 0;
		while (usersCount<usersIds.size() && count0to500<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers<500)
				{
					samples.add(userId);
					count0to500++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count0to500+" users with followers 0-500");
		
		
		//500-2000
		usersCount = 0;
		while (usersCount<usersIds.size() && count500to2000<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers>=500 && followers<2000)
				{
					samples.add(userId);
					count500to2000++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count500to2000+" users with followers 500-2000");
		
		
		//2000-10000
		usersCount = 0;
		while (usersCount<usersIds.size() && count2000to10000<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers>=2000 && followers<10000)
				{
					samples.add(userId);
					count2000to10000++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count2000to10000+" users with followers 2000-10000");
		
		//10000-100000
		usersCount = 0;
		while (usersCount<usersIds.size() && count10000to100000<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers>=10000 && followers<100000)
				{
					samples.add(userId);
					count10000to100000++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count10000to100000+" users with followers 10000-100000");
		
		
		//100000-500000
		usersCount = 0;
		while (usersCount<usersIds.size() && count100000to500000<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers>=100000 && followers<500000)
				{
					samples.add(userId);
					count100000to500000++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count100000to500000+" users with followers 100000-500000");
		
		
		//500000-max
		usersCount = 0;
		while (usersCount<usersIds.size() && count500000toMax<samplingFactor)
		{
			long userId = usersIds.get(usersCount++);
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
				if (followers>=500000)
				{
					samples.add(userId);
					count500000toMax++;
				}
			}
			catch (TwitterException e)
			{
				System.out.println("problem with user with id:" + userId);
			}	
		}
		System.out.println("Added "+count500000toMax+" users with followers 500000-Max");
		
		
		for (Long userId : samples)
		{
			try
			{
				System.out.println(twitterCache.getFollowersCount(userId));
			}
			catch (TwitterException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return samples;
	}
}
