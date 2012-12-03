package it.cybion.influence.graph;


import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.cybion.influence.model.User;
import it.cybion.influence.graph.UsersGraphFactoryImpl;

import org.apache.log4j.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.Vertex;


public class UserGraphFactoryTestCase {

	private static final Logger logger = Logger.getLogger(UserGraphFactoryTestCase.class);
	
	@Test
	public void userRetrivalTest() {
		
		logger.info("=== START userRetrivalTest ===");

		List<String> ids = new ArrayList<String>();
		User u1 = new User(111); ids.add("111");
		User u2 = new User(222); ids.add("222");
		User u3 = new User(333); ids.add("333");
		List<User> users = new ArrayList<User>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
		
		
		UsersGraphFactory factory = new UsersGraphFactoryImpl("src/test/resources/graphs/userRetrivalTest");
		Graph graph = null;
		try {
			factory.addUsersToGraph(users);
			graph = factory.getGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		Iterator<Vertex> iterator = graph.getVertices().iterator();
		while (iterator.hasNext()) {
			Vertex vertex = iterator.next();
			assertTrue(ids.contains((String)(vertex.getProperty("userId"))));
			
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
			UsersGraphFactory factory = new UsersGraphFactoryImpl("src/test/resources/graphs/testsBasicGraph");
			factory.addUsersToGraph(users);
			graph = factory.getGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		Iterable<Vertex> vertices = graph.getVertices();
		for (Vertex vertex : vertices) {
			long vertedUserId = Long.parseLong((String)(vertex.getProperty("userId"))); 
			logger.info(vertedUserId);
			assertTrue(ids.contains(vertedUserId));
			ids.remove(vertedUserId);
		}
		logger.info("=== END testsBasicGraph ===");
	}
	
	
	
	@Test
	public void testsBasicGraphWithFollowersAndFriends() {
		
		logger.info("=== START testsBasicGraphWithFollowersAndFriends ===");
		
		User user = new User(111);
		List<User> friends = new ArrayList<User>();
		List<User> followers = new ArrayList<User>();
		
		User friend = new User(222);		
		friends.add(friend);
		user.setFriends(friends);
		
		User follower = new User(333);		
		followers.add(follower);
		user.setFollowers(followers);
		
		List<User> users = new ArrayList<User>();
		users.add(user);
	
		IndexableGraph graph = null;
		try {
			UsersGraphFactory factory = new UsersGraphFactoryImpl("src/test/resources/graphs/testsBasicGraphWithFollowersAndFriends");
			factory.addUsersToGraph(users);
			graph = (IndexableGraph) factory.getGraph();
			graph.shutdown();
		} catch (GraphCreationException e) {
			e.printStackTrace();
		}
		Iterable<Vertex> vertices = graph.getVertices();
		for (Vertex vertex : vertices) {
			long vertedUserId = Long.parseLong((String)vertex.getProperty("userId")); 
			logger.info(vertedUserId);
			if (vertex.getProperty("userId")=="111") {
				Iterable<Edge> inEdges = vertex.getEdges(Direction.IN, "follows");
				Iterable<Edge> outEdges = vertex.getEdges(Direction.IN, "follows");
				int inEdgesCount = 0;
				int outEdgesCount = 0;
				Iterator<Edge> iterator = inEdges.iterator();
				while (iterator.hasNext()) {
					inEdges.iterator().next();
					inEdgesCount++;
				}
				iterator = outEdges.iterator();
				while (iterator.hasNext()) {
					outEdges.iterator().next();
					outEdgesCount++;
				}
				assertEquals(inEdgesCount, 1);
				assertEquals(outEdgesCount, 1);
				
				iterator = inEdges.iterator();
				Vertex inVertex = iterator.next().getVertex(Direction.OUT);
				assertEquals(inVertex.getProperty("userId"), Long.toString((follower.getId())));
				
				iterator = outEdges.iterator();
				Vertex outVertex = iterator.next().getVertex(Direction.IN);
				assertEquals(outVertex.getProperty("userId"), Long.toString((friend.getId())));
			}
		}
		
		logger.info("=== END testsBasicGraphWithFollowersAndFriends ===");
	}

}

