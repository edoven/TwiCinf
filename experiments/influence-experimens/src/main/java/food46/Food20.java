package food46;


import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.TwitterFacadeFactory;
import it.cybion.influencers.crawler.Crawler;
import it.cybion.influencers.crawler.filtering.FilterManager;
import it.cybion.influencers.crawler.filtering.contentbased.DescriptionAndStatusDictionaryFilterManager;
import it.cybion.influencers.crawler.filtering.topologybased.InOrOutDegreeFilterManager;
import it.cybion.influencers.crawler.filtering.topologybased.OutDegreeFilterManager;
import it.cybion.influencers.crawler.graph.GraphFacade;
import it.cybion.influencers.crawler.graph.Neo4jGraphFacade;
import it.cybion.influencers.crawler.graph.indexes.GraphIndexType;
import it.cybion.influencers.crawler.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;



public class Food20
{

	private static final Logger logger = Logger.getLogger(Food20.class);

	public static void main(String[] args) throws IOException, TwitterException
	{

		int iterations = 1;
		GraphFacade graphFacade = getGraphFacade();
		TwitterCache twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> usersIds = getUsersIds();
		List<FilterManager> filterManagers = getFilterManagers();

		Crawler influencersDiscoverer = new Crawler(
				iterations, usersIds, graphFacade, twitterFacade,
				filterManagers);
		List<Long> influencers = influencersDiscoverer.getInfluencers();

		// logger.info("Possible influencers = "+influencers);
		logger.info("");
		logger.info("");
		logger.info("####### Influencers #########");
		logger.info("");

		for (Long userId : influencers)
		{
			String description = twitterFacade.getDescription(userId)
					.replace('\n', ' ').replace('\r', ' ');
			int followersCount = twitterFacade.getFollowersCount(userId);
			int friendsCount = twitterFacade.getFriendsCount(userId);
			logger.info(twitterFacade.getScreenName(userId) + " - "
					+ followersCount + " - " + friendsCount + " - "
					+ description);
		}
		System.exit(0);
	}

	private static GraphFacade getGraphFacade() throws IOException
	{
		String graphDirPath = "graphs/food20";
		FilesDeleter.delete(new File(graphDirPath));
		GraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath,GraphIndexType.TREEMAP);
		return graphFacade;
	}

	private static List<Long> getUsersIds()
	{
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(200557647L); // cuocopersonale
		usersIds.add(490182956L); // DarioBressanini
		usersIds.add(6832662L); // burde
		usersIds.add(45168679L); // Ricette20
		usersIds.add(76901354L); // spylong
		usersIds.add(138387593L); // giuliagraglia
		usersIds.add(136681167L); // ele_cozzella
		usersIds.add(416427021L); // FilLaMantia
		usersIds.add(444712353L); // ChiaraMaci
		usersIds.add(472363994L); // LucaVissani
		usersIds.add(7077572L); // maghetta
		usersIds.add(16694823L); // paperogiallo
		usersIds.add(991704536L); // craccocarlo
		usersIds.add(46118391L); // Fiordifrolla
		usersIds.add(272022405L); // Davide_Oltolini
		usersIds.add(9762312L); // toccodizenzero
		usersIds.add(96738439L); // oloapmarchi
		usersIds.add(57163636L); // fooders
		usersIds.add(57283474L); // GialloZafferano
		usersIds.add(191365206L); // giornaledelcibo
		// usersIds.add(75086891L); //carlo_spinelli
		// usersIds.add(416478534L); //CarloOttaviano
		// usersIds.add(70918724L); //slow_food_italy
		// usersIds.add(323154299L); //massimobottura
		// usersIds.add(342813082L); //barbierichef
		// usersIds.add(31935994L); //puntarellarossa
		// usersIds.add(28414979L); //morenocedroni
		// usersIds.add(96384661L); //soniaperonaci
		// usersIds.add(86961660L); //scattidigusto
		// usersIds.add(167406951L); //WineNewsIt
		// usersIds.add(368991338L); //TheBreakfastRev
		// usersIds.add(17007757L); //ci_polla
		// usersIds.add(135436730L); //ilgastronauta
		// usersIds.add(23306444L); //dissapore
		// usersIds.add(128564404L); //gianlucamorino
		// usersIds.add(20696734L); //cookaround
		// usersIds.add(7171022L); //cavoletto
		// usersIds.add(22147020L); //Cucina_Italiana
		// usersIds.add(426206087L); //DavideScabin0
		// usersIds.add(54157380L); //italiasquisita
		// usersIds.add(222491618L); //LaCuochina
		// usersIds.add(339541519L); //SingerFood
		// usersIds.add(130209798L); //GigiPadovani
		// usersIds.add(41074932L); //FeudiDSGregorio
		// usersIds.add(342677624L); //MartaTovaglieri
		// usersIds.add(81079701L); //elisiamenduni
		return usersIds;
	}

	private static List<FilterManager> getFilterManagers()
	{
		List<FilterManager> filters = new ArrayList<FilterManager>();
		InOrOutDegreeFilterManager inOrOutDegree = new InOrOutDegreeFilterManager(0.1F, 0.2F);
		OutDegreeFilterManager outDegree = new OutDegreeFilterManager(0.01F);
		List<String> dictionary = new ArrayList<String>();
		dictionary.add("food");
		dictionary.add("cibo");
		dictionary.add("vino");
		dictionary.add("wine");
		dictionary.add("ristorante");
		dictionary.add("chef");
		dictionary.add("restourant");
		dictionary.add("sommelier");
		dictionary.add("degustazioni");
		dictionary.add("gastro");
		dictionary.add("cuoc");
		dictionary.add("cucin");
		dictionary.add("cook");
		dictionary.add("gust");
		dictionary.add("vigna");
		dictionary.add("formagg");
		dictionary.add("gourmet");
		dictionary.add("bere");
		dictionary.add("vini");
		dictionary.add("cucina");
		dictionary.add("ricett");
		dictionary.add("distill");
		dictionary.add("birr");
		dictionary.add("restaurant");
		dictionary.add("sapor");
		dictionary.add("agricol");
		dictionary.add("mangi");
		DescriptionAndStatusDictionaryFilterManager descriptionFilter = new DescriptionAndStatusDictionaryFilterManager(
				dictionary);
		filters.add(0, inOrOutDegree);
		filters.add(1, descriptionFilter);
		filters.add(2, outDegree);
		filters.add(3, descriptionFilter);
		return filters;
	}

}
