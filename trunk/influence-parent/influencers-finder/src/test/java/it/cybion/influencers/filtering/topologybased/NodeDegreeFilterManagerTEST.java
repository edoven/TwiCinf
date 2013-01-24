package it.cybion.influencers.filtering.topologybased;

import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.graph.index.IndexType;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.utils.FilesDeleter;
import it.cybion.influencers.utils.TokenBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.TwitterException;

public class NodeDegreeFilterManagerTEST {
	
	
	private static final Logger logger = Logger.getLogger(NodeDegreeFilterManagerTEST.class);

	
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	
	@BeforeClass
	public void init() throws IOException {
		String graphPath = "src/test/resources/graphs/test1";
		FilesDeleter.delete(new File(graphPath));
		
		graphFacade = new Neo4jGraphFacade(graphPath,  IndexType.TREEMAP);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "tests", "test1");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token1.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token2.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token3.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token4.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token5.txt") );
		userTokens.add( TokenBuilder.getTokenFromFile("/home/godzy/tokens/token6.txt") );
		Twitter4jWebFacade webFacade = new Twitter4jWebFacade(consumerToken, userTokens);
		twitterFacade  = new TwitterFacade(webFacade, persistanceFacade);
	}
	

}
