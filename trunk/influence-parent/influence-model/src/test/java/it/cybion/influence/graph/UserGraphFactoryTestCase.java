package it.cybion.influence.graph;


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

import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;
import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.graph.UsersGraphFactory;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class UserGraphFactoryTestCase {

	private static final Logger logger = Logger.getLogger(UserGraphFactoryTestCase.class);

	
	
	@Test
	public void userRetrivalTest() {
		
		logger.info("=== START userRetrivalTest ===");
		
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
		
		
		UsersGraphFactory factory = new UsersGraphFactory("src/test/resources/graphs/userRetrivalTest",users);
		Graph graph = null;
		try {
			graph = factory.createGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			assertEquals(factory.containsUser(u1), true);
			assertEquals(factory.containsUser(u2), true);
			assertEquals(factory.containsUser(u3), true);
		}
			
		
		logger.info("=== END userRetrivalTest ===");
	}
	
	
	@Test
	public void testsBasicGraph() {
		
		logger.info("=== START testsBasicGraph ===");
		
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
			graph = new UsersGraphFactory("src/test/resources/graphs/testsBasicGraph", users).createGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			Iterable<Vertex> vertices = graph.getVertices();
			for (Vertex vertex : vertices) {
				long vertedUserId = Long.parseLong((String)vertex.getProperty("userId")); 
				logger.info(vertedUserId);
				assertTrue(ids.contains(vertedUserId));
				ids.remove(vertedUserId);
			}
		}
		else
			logger.info("Graph not created!");
		
		logger.info("=== END testsBasicGraph ===");
	}
	
	
	
	@Test
	public void testsBasicGraphWithFollowersAndFriends() {
		
		logger.info("=== START testsBasicGraphWithFollowersAndFriends ===");
		
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
			graph = new UsersGraphFactory("src/test/resources/graphs/testsBasicGraphWithFollowersAndFriends" , users).createGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		if (graph!=null) {
			Iterable<Vertex> vertices = graph.getVertices();
			for (Vertex vertex : vertices) {
				long vertedUserId = Long.parseLong((String)vertex.getProperty("userId")); 
				logger.info(vertedUserId);
			}
		}
		else
			logger.info("Graph not created!");
		
		logger.info("=== END testsBasicGraphWithFollowersAndFriends ===");
	}
	
	
	
	
	
	

	
	
	
}
