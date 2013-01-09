package it.cybion.influencers.filtering.managers.topology;

import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.neo4j.Neo4jGraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.mongodb.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.web.twitter4j.Token;
import it.cybion.influencers.twitter.web.twitter4j.Twitter4jFacade;
import it.cybion.influencers.twitter.web.twitter4j.TwitterApiException;
import it.cybion.influencers.utils.FilesDeleter;
import it.cybion.influencers.utils.TokenBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NodeDegreeFilterManagerTEST {
	
	
	private static final Logger logger = Logger.getLogger(NodeDegreeFilterManagerTEST.class);

	
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	
	@BeforeClass
	public void init() throws IOException {
		String graphPath = "src/test/resources/graphs/test1";
		FilesDeleter.delete(new File(graphPath));
		
		graphFacade = new Neo4jGraphFacade(graphPath);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "tests", "test1");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token1.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token3.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token4.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token5.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token6.txt") );
		int waitTime = 60;
		Twitter4jFacade webFacade = new Twitter4jFacade(consumerToken, userTokens, waitTime);
		twitterFacade  = new TwitterFacade(webFacade, persistanceFacade);
	}
	
	@Test(enabled=true)
	public void testTheFilteringProcedure() throws IOException, TwitterApiException {	
		List<Long> seedUsers = new ArrayList<Long>();
		long id1 = 887469007;
		long id2 = 426724668;
		seedUsers.add(id1);
		seedUsers.add(id2);
		
		NodeDegreeFilterManager filterManager = new NodeDegreeFilterManager(
				ExpansionDirection.FOLLOWERS_AND_FRIENDS,
				DegreeDirection.IN,
				ComparisonOption.GREATER_OR_EQUAL, 
				0.5);
		filterManager.setGraphFacade(graphFacade);
		filterManager.setTwitterFacade(twitterFacade);
		filterManager.setSeedUsers(seedUsers);
		
		List<Long> result = filterManager.filter();
	}

}
