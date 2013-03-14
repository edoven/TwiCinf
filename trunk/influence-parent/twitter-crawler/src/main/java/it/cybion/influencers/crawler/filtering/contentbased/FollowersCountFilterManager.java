package it.cybion.influencers.crawler.filtering.contentbased;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.cache.TwitterFacade;



public class FollowersCountFilterManager implements FilterManager
{

	private static final Logger logger = Logger.getLogger(FollowersCountFilterManager.class);

	private TwitterFacade twitterManager;
	private List<Long> users;
	private int threshold;
	private Map<Long, Integer> user2followersCount;

	public FollowersCountFilterManager(int threshold)
	{
		this.threshold = threshold;
	}

	@Override
	public List<Long> filter()
	{
		solveDependencies();
		FollowersCountFilter filter = new FollowersCountFilter(user2followersCount, threshold);
		return filter.filter();
	}

	private void solveDependencies()
	{
		user2followersCount = new HashMap<Long, Integer>();
		for (Long userId : users)
		{
			for (int i = 0; i < 3; i++)
			{ // 3 tries
				try
				{
					int followerCount = twitterManager.getFollowers(userId).size();
					user2followersCount.put(userId, followerCount);
					break;
				} catch (TwitterException e)
				{
					logger.info("Problem with user with id " + userId);
					try
					{
						Thread.sleep(2 * 1000);
					} catch (InterruptedException e1)
					{
						logger.info("Problem in Thread.sleep");
						e1.printStackTrace();
					}
				}
			}

		}
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterManager)
	{
		this.twitterManager = twitterManager;
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade)
	{
		/*
		 * GraphFacade is not needed
		 */
	}

	@Override
	public void setSeedUsers(List<Long> users)
	{
		this.users = users;
	}

}
