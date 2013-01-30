package trashion7;

import it.cybion.influencers.InfluencersDiscoverer;
import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.contentbased.DescriptionAndStatusDictionaryFilterManager;
import it.cybion.influencers.filtering.topologybased.InAndOutDegreeFilterManager;
import it.cybion.influencers.filtering.topologybased.OutDegreeFilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.graph.index.IndexType;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

public class Trashion7 {
	
	private static final Logger logger = Logger.getLogger(Trashion7.class);

	
	public static void main(String[] args) throws IOException, TwitterException {

		int iterations = 3;
		GraphFacade graphFacade = getGraphFacade();
		TwitterFacade twitterFacade = getTwitterFacade();
		List<Long> usersIds = getUsersIds();
		List<FilterManager> filterManagers = getFilterManagers();
		
		InfluencersDiscoverer influencersDiscoverer = new InfluencersDiscoverer(iterations, 
																				usersIds, 
																				graphFacade, 
																				twitterFacade, 
																				filterManagers);
		List<Long> influencers = influencersDiscoverer.getInfluencers();
		logger.info("Possible influencers = "+influencers);
		
		for (Long userId : influencers) {
			String description = twitterFacade.getDescription(userId).replace('\n', ' ').replace('\r',' ');
			int followersCount = twitterFacade.getFollowersCount(userId);
			int friendsCount = twitterFacade.getFriendsCount(userId);
			logger.info(twitterFacade.getScreenName(userId)+" - "+
						followersCount+" - "+
						friendsCount+" - "+
						description);
		}
		System.exit(0);
	}

	private static GraphFacade getGraphFacade() throws IOException {
		String graphDirPath = "graphs/trashion7";
		FilesDeleter.delete(new File(graphDirPath));	
		GraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath, IndexType.TREEMAP);
		return graphFacade;
	}
	
	private static TwitterFacade getTwitterFacade() throws UnknownHostException {
		Token applicationToken = new Token("tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("tokens/token1.txt"); 
		userTokens.add(userToken1);
		Token userToken2 = new Token("tokens/token2.txt");
		userTokens.add(userToken2);
		Token userToken3 = new Token("tokens/token3.txt");
		userTokens.add(userToken3);
		Token userToken4 = new Token("tokens/token4.txt");
		userTokens.add(userToken4);
		Token userToken5 = new Token("tokens/token5.txt");
		userTokens.add(userToken5);
		Token userToken6 = new Token("tokens/token6.txt");
		userTokens.add(userToken6);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "twitter", "users");
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
	
	private static List<Long> getUsersIds() {
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(92403540L); //lapinella
		usersIds.add(24499591L); //filippala
		usersIds.add(40090727L); //ChiaraFerragni
		usersIds.add(37491839L); //VeronicaFerraro
		usersIds.add(132888646L); //elenabarolo		
		usersIds.add(236857407L); //chiarabiasi		
		usersIds.add(46164460L); //Eleonoracarisi


		return usersIds;
	}
	
	private static List<FilterManager> getFilterManagers() {
		List<FilterManager> filters = new ArrayList<FilterManager>();
		InAndOutDegreeFilterManager inAndOutDegree = new InAndOutDegreeFilterManager(0.05, 0.1);
		OutDegreeFilterManager outDegree = new OutDegreeFilterManager(0.025);
		List<String> dictionary = new ArrayList<String>();	
		dictionary.add("moda");
		dictionary.add("fashion");
		dictionary.add("outfit");
		dictionary.add("street style");
		dictionary.add("cool hunter");		
		dictionary.add("scarpe");
		dictionary.add("accessori");
		dictionary.add("abito");
		dictionary.add("dress");	
		dictionary.add("eleganza");	
		dictionary.add("lifestyle");	
		
		DescriptionAndStatusDictionaryFilterManager descriptionFilter = new DescriptionAndStatusDictionaryFilterManager(dictionary);
		filters.add(0, inAndOutDegree);
		filters.add(1, descriptionFilter);
		filters.add(2, outDegree);
		filters.add(3, descriptionFilter);
		return filters;
	}
	
}