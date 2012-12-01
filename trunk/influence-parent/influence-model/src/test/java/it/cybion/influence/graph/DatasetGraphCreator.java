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
		UsersGraphFactoryOptimized graphFactory = new UsersGraphFactoryOptimized("src/test/resources/graphs/DatasetGraphCreator");
		Graph graph = null;
		try {
			List<User> users = getDatasetAuthors();
			
			int userCount = 0;
			
			int PARTITION_SIZE = 100;
			int partitionsCount = (users.size()-(users.size()%PARTITION_SIZE) ) / PARTITION_SIZE;
			for (int i=0; i<partitionsCount+1; i++) {
				List<User> partition;
				if (users.size()>=PARTITION_SIZE) {
					partition = users.subList(0, PARTITION_SIZE);
					users.subList(0, PARTITION_SIZE).clear();
				}
				else {
					partition = users.subList(0, users.size());
					users.subList(0, users.size()).clear();
				}
				
				List<User> enrichedUser = persistenceFacade.enrichUsersWithFriendsAndFollowers(partition);
				//graphFactory.addUsersToGraph(enrichedUser);
				logger.info(i+" - users.size()="+users.size());
			}
			graph = graphFactory.getGraph();
			logger.info("serializing...");
			graph.shutdown();	
			logger.info("ok!");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (graph==null)
			logger.info("Graph not created!");	
		
		logger.info("=== END partialDatasetGraphCreationTest ===");
	}
		
	
	
	private  List<User> getDatasetAuthors() {
		MysqlPersistenceFacade persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
    	List<String> jsons = persistenceFacade.getAllJsonTweets();
    	//List<String> jsons = persistenceFacade.getFirstNJsonTweets(600);
    	List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	List<User> users = new ArrayList<User>();
    	
    	for (Tweet tweet : tweets)
    		users.add(tweet.getUser());
    	users = new ArrayList<User>(new HashSet<User>(users)); //this removes duplicates
    	return users;
	}
	
	
//	@Test
//	public void partialDatasetGraphCreationTest() {
//		logger.info("=== START partialDatasetGraphCreationTest ===");
//						
//		Graph graph = null;
//		try {
//			List<User> users = getEnrichedDatasetAuthors();
//			int userCount = 0;
////			for (User user : users)
////				logger.info("Enriched user "+(++userCount)+"/"+users.size()+" - "+user.getScreenName()+" - \t\tfollowers:"+user.getFollowers().size()+" - \tfriends:"+user.getFriends().size());  
//			graph = new UsersGraphFactoryOptimized("src/test/resources/graphs/partialDatasetGraphCreationTest", users).createGraph();
//			logger.info("serializing...");
//			graph.shutdown();	
//			logger.info("ok!");	
//		} catch (GraphCreationException e) {
//			e.printStackTrace();
//		}
//		if (graph==null)
//			logger.info("Graph not created!");	
//		
//		logger.info("=== END partialDatasetGraphCreationTest ===");
//	}
	

	
	
    /*
	private  List<User> getEnrichedDatasetAuthors() {
		MysqlPersistenceFacade persistenceFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
    	List<String> jsons = persistenceFacade.getAllJsonTweets();
    	//List<String> jsons = persistenceFacade.getFirstNJsonTweets(600);
    	List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsons);
    	List<User> users = new ArrayList<User>();
    	List<User> enrichedUsers = new ArrayList<User>();
    	
    	for (Tweet tweet : tweets)
    		users.add(tweet.getUser());
    	users = new ArrayList<User>(new HashSet<User>(users)); //this removes duplicates
    	int userCount = 0;
    	for (User user : users) {
    		List<String> followersIds = persistenceFacade.getFollowers(user.getScreenName());
    		List<User> followers = new ArrayList<User>();
    		for (String followerId : followersIds) 
    			followers.add(new User(Long.parseLong(followerId)));
    		user.setFollowers(followers);
    		
    		List<String> friendsIds = persistenceFacade.getFriends(user.getScreenName());
    		List<User> friends = new ArrayList<User>();
    		for (String friendId : friendsIds) 
    			friends.add(new User(Long.parseLong(friendId)));
    		user.setFriends(friends);
    		
    		enrichedUsers.add(user);
    		
    		logger.info("Enriched user "+(++userCount)+"/"+users.size()+" - "+user.getScreenName()+" - \t\tfollowers:"+followers.size()+" - \tfriends:"+friends.size());   		
    	}
    	return enrichedUsers;
	}
	*/
}
