package it.cybion.influencers.graph;



import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class Neo4jGraphFacade implements GraphFacade {
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);
	
	private Neo4jGraph graph;
	private String dirPath;
	private Index<Vertex> vertexIndex;
	private int vericesCount = 0;
	
	private final int OPERATIONS_PER_TRANSACTION = 700;
		
	public Neo4jGraphFacade(String dirPath) {
		this.dirPath = dirPath;
		graph = new Neo4jGraph(dirPath);
		vertexIndex = graph.createIndex("vertexIndex", Vertex.class);
	}
	
	@Override
	public void eraseGraphAndRecreate() throws IOException {
		graph.shutdown();
		FilesDeleter.delete(new File(dirPath));
		graph = new Neo4jGraph(dirPath);
		vericesCount = 0;
		graph.dropIndex("vertexIndex");
		vertexIndex = graph.createIndex("vertexIndex", Vertex.class);
	}
	
	public Vertex addUser(Long userId) {				
		try {
			return getUserVertex(userId);
		} catch (UserVertexNotPresent e) {
			final Vertex vertex = graph.addVertex(null);
			vertex.setProperty("userId", userId);
			vertexIndex.put("userId", userId, vertex); //do not forget this ;)
			vericesCount++;
			return vertex;
		}	
	}
	
	@Override
	public void addUsers(List<Long> usersIds) {
		for (Long userId : usersIds)
			addUser(userId);
	}
		
	@Override
	public void addFollowers(Long userId, List<Long> followersIds) throws UserVertexNotPresent {
		int insersionsCount = 0;		
		logger.info("adding "+followersIds.size()+" followers for user "+userId);
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		for (Long followerId : followersIds) {		
			Vertex followerVertex;
			try {
				followerVertex = getUserVertex(followerId);
			} catch (UserVertexNotPresent e) {
				followerVertex = addUser(followerId);
			}
			graph.addEdge(null, followerVertex, userVertex, "follows");	
			
			insersionsCount++;
			if (insersionsCount>0 && (insersionsCount%OPERATIONS_PER_TRANSACTION)==0) {
				graph.stopTransaction(Conclusion.SUCCESS);
			}			
		}
		graph.stopTransaction(Conclusion.SUCCESS);
	}

	@Override
	public void addFriends(Long userId, List<Long> friendsIds) throws UserVertexNotPresent {
		int insersionsCount = 0;		
		logger.info("adding "+friendsIds.size()+" friends for user "+userId);
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		for (Long friendId : friendsIds) {
			Vertex friendVertex;
			try {
				friendVertex = getUserVertex(friendId);
			} catch (UserVertexNotPresent e) {
				friendVertex = addUser(friendId);
			}
			graph.addEdge(null, userVertex, friendVertex, "follows");	
			
			insersionsCount++;
			if (insersionsCount>0 && (insersionsCount%OPERATIONS_PER_TRANSACTION)==0) {
				graph.stopTransaction(Conclusion.SUCCESS);
			}	
		}
		graph.stopTransaction(Conclusion.SUCCESS);
	}
	
	public Vertex getUserVertex(Long userId) throws UserVertexNotPresent {
		Iterable<Vertex> results = vertexIndex.get("userId",userId);
		Iterator<Vertex> iterator = results.iterator();
		if (iterator.hasNext())
			return iterator.next();			
		else
			throw new UserVertexNotPresent("Trying to get node for user with id="+userId+" but node is not in the graph");
	}
		
	@Override
	public int getVerticesCount() {
		return vericesCount;
	}
	
	@Override
	public int getInDegree(Long userId) throws UserVertexNotPresent, InDegreeNotSetException {
		Vertex userVertex = getUserVertex(userId);
		if (userVertex == null)
			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
		Object inDegree = userVertex.getProperty("inDegree");
		if (inDegree==null) {
			throw new InDegreeNotSetException("Trying to get inDegree for node of user with id="+userId+" but the inDegree has not been set");
		}
		return (Integer) inDegree;
	}

	@Override
	public int getOutDegree(Long userId) throws OutDegreeNotSetException, UserVertexNotPresent {
		Vertex userVertex = getUserVertex(userId);
		Object outDegree = userVertex.getProperty("outDegree");
		if (outDegree==null) {
			throw new OutDegreeNotSetException("Trying to get outDegree for node of user with id="+userId+" but the outDegree has not been set");
		}
		return (Integer) outDegree;
	}

	@Override
	public Map<Long, Integer> calculateInDegree(List<Long> usersToBeCalculated, List<Long> sourceUsers) throws UserVertexNotPresent {
		logger.info("### calculateInDegree ###");
		Map<Long, Integer> user2inDegree = new HashMap<Long, Integer>();
		int percentCalculated = 0;
		int tenPercent = Math.round((float)usersToBeCalculated.size()/10);
		for (int i=0; i<usersToBeCalculated.size(); i++) {
			if (i%tenPercent==0) {
				logger.info("calculated inDegree for "+percentCalculated+"% of users");
				percentCalculated = percentCalculated + 10;
			}
			long userId = usersToBeCalculated.get(i);
			int inDegree = 0;
			Vertex userVertex = getUserVertex(userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex followerVertex = iterator.next();
				Long followerId = (Long)followerVertex.getProperty("userId");
				//logger.info("userId="+userId+" - followerId="+followerId);
				if (sourceUsers.contains( followerId ))
					inDegree++;
			}
			userVertex.setProperty("inDegree", inDegree);
			user2inDegree.put(userId, inDegree);
			//logger.info("Set indegree="+inDegree+" for user with id="+userId);
			if (i%OPERATIONS_PER_TRANSACTION==0)
				graph.stopTransaction(Conclusion.SUCCESS);
		}
		return user2inDegree;
	}

	@Override
	public Map<Long, Integer> calculateOutDegree(List<Long> usersToBeCalculated, List<Long> destinationUsers) throws UserVertexNotPresent {
		logger.info("### calculateOutDegree ###");
		Map<Long, Integer> user2outDegree = new HashMap<Long, Integer>();
		int percentCalculated = 0;
		int tenPercent = Math.round((float)usersToBeCalculated.size()/10);
		for (int i=0; i<usersToBeCalculated.size(); i++) {
			if (i%tenPercent==0) {
				logger.info("calculated outDegree for "+percentCalculated+"% of users");
				percentCalculated = percentCalculated + 10;
			}
			long userId = usersToBeCalculated.get(i);
			int outDegree = 0;
			Vertex userVertex = getUserVertex( userId);
			if (userVertex == null) {
				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
			}
			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				Vertex friendVertex = iterator.next();
				Long friendId =(Long)friendVertex.getProperty("userId");
				//logger.info("userId="+userId+" - friendId="+friendId);
				if (destinationUsers.contains( friendId ))
					outDegree++;
			}
			userVertex.setProperty("outDegree", outDegree);
			user2outDegree.put(userId, outDegree);
			//logger.info("Set outDegree="+outDegree+" for user with id="+userId);
			if (i%OPERATIONS_PER_TRANSACTION==0)
				graph.stopTransaction(Conclusion.SUCCESS);
		}
		return user2outDegree;
	}


}
