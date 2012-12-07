package it.cybion.influence.graph;

import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.DatasetJsonDeserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.Graph;

public class DatasetGraphCreator {
	
	private static final Logger logger = Logger.getLogger(DatasetGraphCreator.class);
	
	/*
	 * To avoid memory out-of-bound problem the users (the authors
	 * of the tweets in the dataset) are enriched (with followers and friends) 
	 * and added to the graph not in a single shot but using partitions 
	 * of PARTITION_SIZE dimention
	 */
	private int PARTITION_SIZE = 30;

	
	
	
	
	@Test
	public void datasetGraphCreation() {
		logger.info("=== START partialDatasetGraphCreationTest ===");
		MysqlPersistenceFacade persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");	
		UsersGraphFactory graphFactory = new UsersGraphFactoryImpl("src/test/resources/graphs/TwitterGraph");
		Graph graph = null;
		try {
			List<User> users = getDatasetAuthors(persistenceFacade);	
			
			int partitionsCount = (users.size()-(users.size()%PARTITION_SIZE) ) / PARTITION_SIZE;
			List<User> partition;
			for (int i=0; i<=partitionsCount; i++) {				
				if (users.size()>=PARTITION_SIZE)
					partition = new ArrayList<User>(users.subList(0, PARTITION_SIZE));
				else
					partition = new ArrayList<User>(users.subList(0, users.size()));				
				users.removeAll(partition);
				logger.info("(PARTITION "+(i+1)+"/"+partitionsCount+") partition.size()="+partition.size()+" - users.size()="+users.size());				
				List<User> enrichedUsers = persistenceFacade.enrichUsersWithFriendsAndFollowers(partition);
				graphFactory.addUsersToGraph(enrichedUsers);				
			}
			graphFactory.addNodesDegreesCounts(); //this sets degree/inDegree/outDegree follows-edges labels
			graph = graphFactory.getGraph();
			logger.info(graphFactory.getUsersCount()+" users added");
			logger.info("serializing...");
			graph.shutdown();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (graph==null)
			logger.info("Graph not created!");	
		
		logger.info("=== END partialDatasetGraphCreationTest ===");
	}
		
	
	
	private  List<User> getDatasetAuthors(MysqlPersistenceFacade persistenceFacade) {
    	List<String> jsons = persistenceFacade.getAllJsonTweets();
    	//List<String> jsons = persistenceFacade.getFirstNJsonTweets(3000);
    	List<Tweet> tweets = new DatasetJsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	List<User> users = new ArrayList<User>();
    	
    	for (Tweet tweet : tweets) {
    		User user = tweet.getUser();
    		if (user.getFriendsCount()>5000 | user.getFollowersCount()>5000)
    			logger.info("(getDatasetAuthors) user skipped " + user.getScreenName() + "\t\t\t - " + user.getFollowersCount() + "\t\t - " + user.getFriendsCount());  			
    		else
    			users.add(user);
    	}
    	users = new ArrayList<User>(new HashSet<User>(users)); //this removes duplicates
    	return users;
	}

}
