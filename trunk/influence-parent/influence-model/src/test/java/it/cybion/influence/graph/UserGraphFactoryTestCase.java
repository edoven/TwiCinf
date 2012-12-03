package it.cybion.influence.graph;


<<<<<<< HEAD
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

=======
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;
import it.cybion.influence.IO.MysqlPersistenceFacade;
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

<<<<<<< HEAD
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
=======
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d

public class UserGraphFactoryTestCase extends InMemoryGraphDbServiceProvider {

	private static final Logger LOGGER = Logger.getLogger(UserGraphFactoryTestCase.class);

	
	
	@Test
	public void userRetrivalTest() {
		
		LOGGER.info("=== START userRetrivalTest ===");
		
		List<Long> ids = new ArrayList<Long>();
		User u1 = new User(111);
		ids.add(111l);
		User u2 = new User(222);
		ids.add(222l);
		User u3 = new User(333);
		ids.add(333l);
		List<User> users = new ArrayList<User>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
		
		
		UsersGraphFactory ugFactory = new UsersGraphFactoryImpl("src/test/resources/graphs/userRetrivalTest",users);
		Graph graph = null;
		try {
<<<<<<< HEAD
			graph = factory.createGraph();
			graph.shutdown();
=======
			graph = ugFactory.createGraph();
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			assertEquals(ugFactory.containsUser(u1), true);
			assertEquals(ugFactory.containsUser(u2), true);
			assertEquals(ugFactory.containsUser(u3), true);
		}
			
		
		LOGGER.info("=== END userRetrivalTest ===");
	}
	
	
	@Test
	public void testsBasicGraph() {
		
		LOGGER.info("=== START testsBasicGraph ===");
		
		List<Long> ids = new ArrayList<Long>();
		User u1 = new User(111);
		ids.add(111l);
		User u2 = new User(222);
		ids.add(222l);
		User u3 = new User(333);
		ids.add(333l);
		List<User> users = new ArrayList<User>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
		
		
		
		Graph graph = null;
		try {
<<<<<<< HEAD
			graph = new UsersGraphFactory("src/test/resources/graphs/testsBasicGraph", users).createGraph();
			graph.shutdown();
=======
			graph = new UsersGraphFactoryImpl("src/test/resources/graphs/testsBasicGraph", users).createGraph();
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			Iterable<Vertex> vertices = graph.getVertices();
			for (Vertex vertex : vertices) {
				long vertedUserId = Long.parseLong((String)vertex.getProperty("userId")); 
				LOGGER.info(vertedUserId);
				assertTrue(ids.contains(vertedUserId));
				ids.remove(vertedUserId);
			}
		}
		else
			LOGGER.info("Graph not created!");
		
		LOGGER.info("=== END testsBasicGraph ===");
	}
	
	
	
	@Test
	public void testsBasicGraphWithFollowersAndFriends() {
		
		LOGGER.info("=== START testsBasicGraphWithFollowersAndFriends ===");
		
		User user = new User(111);
		
		User friend = new User(222);
		List<User> friends = new ArrayList<User>();
		friends.add(friend);
		user.setFriends(friends);
		
		User follower = new User(333);
		List<User> followers = new ArrayList<User>();
		followers.add(follower);
		user.setFollowers(followers);
		
		List<User> users = new ArrayList<User>();
		users.add(user);
	
		Graph graph = null;
		try {
<<<<<<< HEAD
			graph = new UsersGraphFactory("src/test/resources/graphs/testsBasicGraphWithFollowersAndFriends" , users).createGraph();
			graph.shutdown();
=======
			graph = new UsersGraphFactoryImpl("src/test/resources/graphs/testsBasicGraphWithFollowersAndFriends" , users).createGraph();
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			Iterable<Vertex> vertices = graph.getVertices();
			for (Vertex vertex : vertices) {
				long vertedUserId = Long.parseLong((String)vertex.getProperty("userId")); 
				LOGGER.info(vertedUserId);
			}
		}
		else
			LOGGER.info("Graph not created!");
		
		LOGGER.info("=== END testsBasicGraphWithFollowersAndFriends ===");
	}
	
	
	
	
	
	
<<<<<<< HEAD

	
=======
	@Test
	public void partialDatasetGraphCreationTest() {
		MysqlPersistenceFacade mysqlFacade = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter");
		List<String> jsonTweets = mysqlFacade.getFirstNJsonTweets(10);
		List<Tweet> tweets = new JsonDeserializer().deserializeJsonStringsToTweets(jsonTweets);
		List<User> users = new ArrayList<User>();
		for (Tweet tweet : tweets)
			users.add(tweet.getUser());
		for (User user : users) {
			List<String> friendsId = mysqlFacade.getFriends(user.getScreenName());
			List<User> friends = new ArrayList<User>();
			for (String friendId : friendsId) {
				User friend = new User();
				friend.setId(Long.parseLong(friendId));
				friends.add(friend);
			}
			List<String> followersId = mysqlFacade.getFollowers(user.getScreenName());
			List<User> followers = new ArrayList<User>();
			for (String followerId : followersId) {
				User follower = new User();
				follower.setId(Long.parseLong(followerId));
				friends.add(follower);
			}
			user.setFriends(friends);
			user.setFollowers(followers);
		}
		
		Graph graph = null;
		try {
			graph = new UsersGraphFactoryImpl("src/test/resources/graphs/partialDatasetGraphCreationTest", users).createGraph();
			
			try {
				OutputStream out = new FileOutputStream("src/test/resources/graphs/partialDatasetGraphCreationTest/graph");
				GMLWriter.outputGraph(graph, out);
				out.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			graph.shutdown();
		
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			
		}
		else
			LOGGER.info("Graph not created!");
		
	}
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
	
	
}

