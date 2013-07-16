package it.cybion.influencers.crawler.testframework;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.crawler.Crawler;
import org.apache.log4j.Logger;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;



public class TestFramework
{

	private static final Logger LOGGER = Logger.getLogger(TestFramework.class);

	private Crawler influencersDiscoverer;
	private List<Long> toBeInUsers;
	private TwitterCache twitterFacade;

	public TestFramework(Crawler influencersDiscoverer, List<Long> toBeInUsers, TwitterCache twitterFacade)
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
		LOGGER.info("");
		LOGGER.info("");
		LOGGER.info("###############################");
		LOGGER.info("#### TestFramework results ####");
		LOGGER.info("###############################");
		LOGGER.info("");
		LOGGER.info("toBeInUsers.size()=" + toBeInUsers.size());
		LOGGER.info("obtainedUsers.size()=" + obtainedUsers.size());
		LOGGER.info("notFoundUsers.size()=" + notFoundUsers.size());
		LOGGER.info("foundUsers.size()=" + foundUsers.size());
		LOGGER.info("");
		LOGGER.info("####### notFoundUsers #########");
		LOGGER.info("");
		for (Long userId : notFoundUsers)
			try
			{
				String description = twitterFacade.getDescription(userId).replace('\n', ' ').replace('\r', ' ');
				LOGGER.info(twitterFacade.getScreenName(userId) + " - " +
                            twitterFacade.getFollowersCount(userId) + " - " + description);
			} catch (TwitterException e)
			{
				LOGGER.info("Problem with user with id=" + userId);
			}
		LOGGER.info("");
		LOGGER.info("######### foundUsers ##########");
		LOGGER.info("");
		for (Long userId : foundUsers)
			try
			{
				String description = twitterFacade.getDescription(userId).replace('\n', ' ').replace('\r', ' ');
				LOGGER.info(twitterFacade.getScreenName(userId) + " - " +
                            twitterFacade.getFollowersCount(userId) + " - " + description);
			} catch (TwitterException e)
			{
				LOGGER.info("Problem with user with id=" + userId);
			}
	}

}
