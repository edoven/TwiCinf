package it.cybion.influence.graph;

import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.Graph;

public class DatasetGraphCreator {
	
	private static final Logger logger = Logger.getLogger(DatasetGraphCreator.class);
	
	
	@Test
	public void datasetGraphCreationTest() {
		logger.info("=== START partialDatasetGraphCreationTest ===");
		MysqlPersistenceFacade persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");	
		UserGraphFactory graphFactory = new UsersGraphFactoryImpl("src/test/resources/graphs/TwitterGraph3000");
		Graph graph = null;
		try {
			List<User> users = getDatasetAuthorsBOUNDED(persistenceFacade);
			
			int userCount = 0;
			
			int PARTITION_SIZE = 30;
			int partitionsCount = (users.size()-(users.size()%PARTITION_SIZE) ) / PARTITION_SIZE;
			List<User> partition;
			for (int i=0; i<partitionsCount+1; i++) {				
				if (users.size()>=PARTITION_SIZE)
					partition = new ArrayList<User>(users.subList(0, PARTITION_SIZE));
				else
					partition = new ArrayList<User>(users.subList(0, users.size()));				
				users.removeAll(partition);
				logger.info("partition.size()="+partition.size());
				logger.info(i+" - users.size()="+users.size());
				
				List<User> enrichedUsers = persistenceFacade.enrichUsersWithFriendsAndFollowers(partition);
				graphFactory.addUsersToGraph(enrichedUsers);				
			}
			graph = graphFactory.getGraph();
			logger.info("serializing...");
			graph.shutdown();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (graph==null)
			logger.info("Graph not created!");	
		
		logger.info("=== END partialDatasetGraphCreationTest ===");
	}
		
	
	
	private  List<User> getDatasetAuthorsBOUNDED(MysqlPersistenceFacade persistenceFacade) {
    	//List<String> jsons = persistenceFacade.getAllJsonTweets();
    	List<String> jsons = persistenceFacade.getFirstNJsonTweets(3000);
    	List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	List<User> users = new ArrayList<User>();
    	
    	for (Tweet tweet : tweets) {
    		User user = tweet.getUser();
    		if (user.getFriendsCount()<5001 && user.getFollowersCount()<5000)
    			users.add(user);
    		//logger.info(user.getScreenName());
    	}
    	users = new ArrayList<User>(new HashSet<User>(users)); //this removes duplicates
    	return users;
	}

}
