package it.cybion.influencers.testframework;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import it.cybion.influencers.InfluencersDiscoverer;
import it.cybion.influencers.twitter.TwitterFacade;



public class TestFramework
{

	private static final Logger logger = Logger.getLogger(TestFramework.class);

	private InfluencersDiscoverer influencersDiscoverer;
	private List<Long> toBeInUsers;
	private TwitterFacade twitterFacade;

	public TestFramework(InfluencersDiscoverer influencersDiscoverer, List<Long> toBeInUsers, TwitterFacade twitterFacade)
	{
		this.influencersDiscoverer = influencersDiscoverer;
		this.toBeInUsers = toBeInUsers;
		this.twitterFacade = twitterFacade;
	}

	public void run()
	{
		List<Long> obtainedUsers = influencersDiscoverer.getInfluencers();
		List<Long> notFoundUsers = new ArrayList<Long>(toBeInUsers);
		notFoundUsers.removeAll(obtainedUsers);
		List<Long> foundUsers = new ArrayList<Long>(toBeInUsers);
		foundUsers.removeAll(notFoundUsers);
		logger.info("");
		logger.info("");
		logger.info("###############################");
		logger.info("#### TestFramework results ####");
		logger.info("###############################");
		logger.info("");
		logger.info("toBeInUsers.size()=" + toBeInUsers.size());
		logger.info("obtainedUsers.size()=" + obtainedUsers.size());
		logger.info("notFoundUsers.size()=" + notFoundUsers.size());
		logger.info("foundUsers.size()=" + foundUsers.size());
		logger.info("");
		logger.info("####### notFoundUsers #########");
		logger.info("");
		for (Long userId : notFoundUsers)
			try
			{
				String description = twitterFacade.getDescription(userId).replace('\n', ' ').replace('\r', ' ');
				logger.info(twitterFacade.getScreenName(userId) + " - " + twitterFacade.getFollowersCount(userId) + " - " + description);
			} catch (TwitterException e)
			{
				logger.info("Problem with user with id=" + userId);
			}
		logger.info("");
		logger.info("######### foundUsers ##########");
		logger.info("");
		for (Long userId : foundUsers)
			try
			{
				String description = twitterFacade.getDescription(userId).replace('\n', ' ').replace('\r', ' ');
				logger.info(twitterFacade.getScreenName(userId) + " - " + twitterFacade.getFollowersCount(userId) + " - " + description);
			} catch (TwitterException e)
			{
				logger.info("Problem with user with id=" + userId);
			}
	}

}
