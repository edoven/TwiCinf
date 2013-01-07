package it.cybion.influencers.filtering.managers.topology;

import it.cybion.influencers.filtering.managers.ExpansionDirection;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.persistance.mongodb.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.web.twitter4j.Token;
import it.cybion.influencers.twitter.web.twitter4j.Twitter4jFacade;
import it.cybion.influencers.utils.TokenBuilder;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class NodeDegreeFilterManagerTEST {

	@Test
	public void test1() throws UnknownHostException {
		
		GraphFacade graphFacade = new Neo4jGraphFacade("src/test/resources/graphs/test1");
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
		TwitterFacade twitterFacade  = new TwitterFacade(webFacade, persistanceFacade);
		
		List<Long> seedUsers = new ArrayList<Long>();
		seedUsers.add(426724668l);
		seedUsers.add(887469007l);
		
		NodeDegreeFilterManager filterManager = new NodeDegreeFilterManager(
				ExpansionDirection.FOLLOWERS_AND_FRIENDS,
				DegreeDirection.IN,
				ComparisonOption.GREATER, 
				1.0);
		filterManager.setGraphFacade(graphFacade);
		filterManager.setTwitterFacade(twitterFacade);
		filterManager.setSeedUsers(seedUsers);
		
		List<Long> result = filterManager.filter();
		System.out.println(result);
	}

}
