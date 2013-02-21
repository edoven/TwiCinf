package trashion7;


import it.cybion.influencers.InfluencersDiscoverer;
import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.aggregation.OrFilterManager;
import it.cybion.influencers.filtering.contentbased.DescriptionAndStatusDictionaryFilterManager;
import it.cybion.influencers.filtering.language.LanguageDetectionFilterManager;
import it.cybion.influencers.filtering.topologybased.InAndOutDegreeFilterManager;
import it.cybion.influencers.filtering.topologybased.InDegreeFilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.graph.indexes.GraphIndexType;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;
import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;


public class Trashion7
{

	private static final Logger logger = Logger.getLogger(Trashion7.class);

	public static void main(String[] args) throws IOException, TwitterException
	{

		int iterations = 1;
		if (args.length == 2 && args[0].equals("-i"))
		{
			iterations = Integer.parseInt(args[1]);
		} else
		{
			System.out
					.println("Error. Usage: filename.jar -i iteration_number. Launching with 1 iteration.");
		}

		GraphFacade graphFacade = getGraphFacade();
		TwitterFacade twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> usersIds = getUsersIds();
		List<FilterManager> iteratingFilters = getIteratingFilters();
//		List<FilterManager> finalizingFilters = getFinalizingFilters();

//		InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscoverer(
//				iterations, usersIds, graphFacade, twitterFacade,
//				iteratingFilters, finalizingFilters);
		InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscoverer(
				iterations, usersIds, graphFacade, twitterFacade,
				iteratingFilters);
		List<Long> influencers = influencersDiscoverer.getInfluencers();
		logger.info("Possible influencers = " + influencers);
		logger.info("Possible influencers count = " + influencers.size());

		class User implements Comparable<User>
		{
			public int followersCount;
			public int friendsCount;
			public String description;
			public String screenName;

			public User(String screenName, int followersCount,
					int friendsCount, String description)
			{
				this.screenName = screenName;
				this.followersCount = followersCount;
				this.friendsCount = friendsCount;
				this.description = description;
			}

			public int compareTo(User userToCompare)
			{
				return userToCompare.followersCount - this.followersCount;
			}
		}
		;

		List<User> users = new ArrayList<User>();
		for (Long userId : influencers)
		{
			String screenName = twitterFacade.getScreenName(userId);
			int followersCount = twitterFacade.getFollowersCount(userId);
			int friendsCount = twitterFacade.getFriendsCount(userId);
			String description = twitterFacade.getDescription(userId)
					.replace('\n', ' ').replace('\r', ' ');
			users.add(new User(screenName, followersCount, friendsCount,
					description));
		}
		Collections.sort(users);

		int count = 0;
		for (User user : users)
		{
			logger.info(count + ") " + user.screenName + " - "
					+ user.followersCount + " - " + user.friendsCount + " - "
					+ user.description);
			count++;
		}
		System.exit(0);
	}

	private static GraphFacade getGraphFacade() throws IOException
	{
		String graphDirPath = "graphs/trashion7";
		FilesDeleter.delete(new File(graphDirPath));
		GraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath,
				GraphIndexType.TREEMAP);
		return graphFacade;
	}

	private static List<Long> getUsersIds()
	{
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(92403540L); // lapinella
		usersIds.add(24499591L); // filippala
		usersIds.add(40090727L); // ChiaraFerragni
		usersIds.add(37491839L); // VeronicaFerraro
		usersIds.add(132888646L); // elenabarolo
		usersIds.add(236857407L); // chiarabiasi
		usersIds.add(46164460L); // Eleonoracarisi

		return usersIds;
	}

	private static List<FilterManager> getIteratingFilters()
	{
		List<FilterManager> filters = new ArrayList<FilterManager>();
		InAndOutDegreeFilterManager inAndOutDegree = new InAndOutDegreeFilterManager(0.05F, 0.1F);
		InDegreeFilterManager inDegree = new InDegreeFilterManager(0.15F);
		List<FilterManager> orFilters = new ArrayList<FilterManager>();
		orFilters.add(inDegree);
		orFilters.add(inAndOutDegree);
		OrFilterManager orDegree = new OrFilterManager(orFilters);
		List<String> dictionary = new ArrayList<String>();
		dictionary.add("moda");
		dictionary.add("fashion");
		dictionary.add("outfit");
		dictionary.add("street style");
		dictionary.add("cool hunter");
		dictionary.add("scarpe");
		dictionary.add("shoes");
		dictionary.add("accessori");
		dictionary.add("abito");
		dictionary.add("dress");
		dictionary.add("eleganza");
		dictionary.add("elegance");
		dictionary.add("lifestyle");
		dictionary.add("chic");
		dictionary.add("glamour");
		dictionary.add("lingerie");

		DescriptionAndStatusDictionaryFilterManager descriptionFilter = 
				new DescriptionAndStatusDictionaryFilterManager(dictionary);
		filters.add(0, orDegree);
		filters.add(1, descriptionFilter);
		return filters;
	}

	private static List<FilterManager> getFinalizingFilters()
	{
		List<FilterManager> filters = new ArrayList<FilterManager>();
		LanguageDetectionFilterManager languageFilter = new LanguageDetectionFilterManager(
				"/home/godzy/Dropbox/universita/tesi/lib/langdetect-09-13-2011/profiles.sm");
		filters.add(0, languageFilter);
		return filters;
	}

}
