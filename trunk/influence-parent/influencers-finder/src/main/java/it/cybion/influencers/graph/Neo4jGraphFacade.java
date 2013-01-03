package it.cybion.influencers.graph;


import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class Neo4jGraphFacade implements GraphFacade {
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);
	
	private Neo4jGraph graph;
	private Index<Vertex> vertexIndex;
	
	public Neo4jGraphFacade(String dirPath) {
		graph = new Neo4jGraph(dirPath);
		vertexIndex =  graph.createIndex("vertexIndex", 
										 Vertex.class, 
										 new Parameter<String, String>(	
												 "type", 
												 "exact"));
	}

	@Override
	public void addUsers(List<Long> usersIds) {
		for (Long userId : usersIds)
			addUser(userId);
	}
	
	
	public void addUser(Long userId) {
		if (getUserVertex(userId) == null) {
			Vertex vertex = graph.addVertex(null);
			vertex.setProperty("userId", userId);
			vertex.setProperty("type", "user");
			vertexIndex.put("userId", userId, vertex); //don't forget this ;)
		}		
	}
	
	
	public Vertex getUserVertex(Long userId) {
		Iterable<Vertex> results = vertexIndex.get("userId",userId);
		Iterator<Vertex> iterator = results.iterator();
		if (iterator.hasNext())
			return iterator.next();			
		else
			return null;
	}
	
	public int getVerticesCount() {
		int count = 0;
		Iterable<Vertex> results = graph.getVertices();		
		Iterator<Vertex> iterator = results.iterator();
		while (iterator.hasNext()) {
			count++;
			iterator.next();
		}
		return count;
	}

	@Override
	public void addFollowers(Long userId, List<Long> followersIds) throws UserVertexNotPresent {
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		for (Long followerId : followersIds) {
			Vertex followerVertex = getUserVertex(followerId);
			if (followerVertex == null) {
				followerVertex = graph.addVertex(null);
				followerVertex.setProperty("userId", followerId);
				followerVertex.setProperty("type", "user");
			}
			graph.addEdge(null, followerVertex, userVertex, "follows");	
		}
	}

	@Override
	public void addFriends(Long userId, List<Long> friendsIds) throws UserVertexNotPresent {
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		for (Long friendId : friendsIds) {
			Vertex friendVertex = getUserVertex(friendId);
			if (friendVertex == null) {
				friendVertex = graph.addVertex(null);
				friendVertex.setProperty("userId", friendVertex);
				friendVertex.setProperty("type", "user");
			}
			graph.addEdge(null, userVertex, friendVertex, "follows");	
		}
	}


	@Override
	public int getInDegree(Long userId) throws UserVertexNotPresent {
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		Object inDegree = userVertex.getProperty("inDegree");
		if (inDegree==null) {
			//TODO: do something
		}
		return (Integer) inDegree;
	}

	@Override
	public int getOutDegree(Long userId) {
		Vertex userVertex = getUserVertex(userId);
		Object outDegree = userVertex.getProperty("outDegree");
		if (outDegree==null) {
			//TODO: do something
		}
		return (Integer) outDegree;
	}

	@Override
	public int getTotalDegree(Long userId) {
		Vertex userVertex = getUserVertex(userId);
		Object totalDegree = userVertex.getProperty("totalDegree");
		if (totalDegree==null) {
			//TODO: do something
		}
		return (Integer) totalDegree;
	}

	@Override
	public void calculateInDegree(List<Long> usersToBeCalculated, List<Long> sourceUsers) throws UserVertexNotPresent {
		for (Long userId : usersToBeCalculated) {
			int inDegree = 0;
			Vertex userVertex = getUserVertex(userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex followerVertex = iterator.next();
				Long followerId = (Long)followerVertex.getProperty("userId");
				logger.info("userId="+userId+" - followerId="+followerId);
				if (sourceUsers.contains( followerId ))
					inDegree++;
			}
			userVertex.setProperty("inDegree", inDegree);
			logger.info("Set indegree="+inDegree+" for user with id="+userId);
		}
	}

	@Override
	public void calculateOutDegree(List<Long> usersToBeCalculated, List<Long> destinationUsers) throws UserVertexNotPresent {
		for (Long userId : usersToBeCalculated) {
			int outDegree = 0;
			Vertex userVertex = getUserVertex(userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex friendVertex = iterator.next();
				Long friendId = new Long((Integer)friendVertex.getProperty("userId"));
				logger.info("userId="+userId+" - friendId="+friendId);
				if (destinationUsers.contains( friendId ))
					outDegree++;
			}
			userVertex.setProperty("outDegree", outDegree);
			logger.info("Set outDegree="+outDegree+" for user with id="+userId);
		}
	}

	@Override
	public void calculateTotalDegree(List<Long> usersToBeCalculated) throws UserVertexNotPresent {
		for (Long userId : usersToBeCalculated) {
			int outDegree = 0;
			int inDegree = 0;
			Vertex userVertex = getUserVertex(userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				iterator.next();
				outDegree++;
			}
			iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				iterator.next();
				inDegree++;
			}
			userVertex.setProperty("totalDegree", inDegree+outDegree);
		}
	}

	@Override
	public void calculateTotalDegree(List<Long> usersToBeCalculated,List<Long> wrtUsers) throws UserVertexNotPresent {
		for (Long userId : usersToBeCalculated) {
			int outDegree = 0;
			int inDegree = 0;
			Vertex userVertex = getUserVertex(userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex followerVertex = iterator.next();
				Long followerId = new Long((Integer)followerVertex.getProperty("userId"));
				if (wrtUsers.contains( followerId ))
					outDegree++;
			}
			iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex friendVertex = iterator.next();
				Long friendId = new Long((Integer)friendVertex.getProperty("userId"));
				if (wrtUsers.contains( friendId ))
					inDegree++;
			}
			userVertex.setProperty("totalDegree", inDegree+outDegree);
		}
	}


}
