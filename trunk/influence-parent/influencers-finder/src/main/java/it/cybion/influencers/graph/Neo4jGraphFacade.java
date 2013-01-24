package it.cybion.influencers.graph;


import it.cybion.influencers.graph.index.GraphIndex;
import it.cybion.influencers.graph.index.IndexType;
import it.cybion.influencers.graph.index.LuceneIndex;
import it.cybion.influencers.graph.index.TreeMapIndex;
import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import scala.reflect.generic.Trees.CaseDef;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class Neo4jGraphFacade implements GraphFacade {
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);
	
	private Neo4jGraph graph;
	private String dirPath;
	private int vericesCount = 0;
	private GraphIndex vertexIndex;
	private IndexType indexType;
	
	private final int OPERATIONS_PER_TRANSACTION = 20000;
		
	public Neo4jGraphFacade(String dirPath, IndexType indexType) {
		this.dirPath = dirPath;
		graph = new Neo4jGraph(dirPath);
		this.indexType = indexType;
		switch (indexType) {
			case LUCENE_INDEX: 
				vertexIndex = new LuceneIndex(graph.createIndex("vertexIndex", Vertex.class));
				break;
			case TREEMAP:
				vertexIndex = new TreeMapIndex();
				break;
		}		
	}
	
	@Override
	public void eraseGraphAndRecreate() throws IOException {
		graph.dropIndex("vertexIndex");
		graph.shutdown();
		FilesDeleter.delete(new File(dirPath));
		graph = new Neo4jGraph(dirPath);				
		switch (indexType) {
			case LUCENE_INDEX: 
				vertexIndex = new LuceneIndex(graph.createIndex("vertexIndex", Vertex.class));
				break;
			case TREEMAP:
				vertexIndex = new TreeMapIndex();
				break;
		}	
		vericesCount = 0;
	}
	
	public Vertex addUser(Long userId) {				
		try {
			return getUserVertex(userId);
		} catch (UserVertexNotPresent e) {
			final Vertex vertex = graph.addVertex(null);
			vertex.setProperty("userId", userId);
			vertexIndex.put(userId, vertex);
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
//		logger.info("adding "+followersIds.size()+" followers for user "+userId);
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
//		logger.info("adding "+friendsIds.size()+" friends for user "+userId);
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
		return vertexIndex.getVertex(graph, userId);		
	}
		
	@Override
	public int getVerticesCount() {
		return vericesCount;
	}
	
//	@Override
//	public int getInDegree(Long userId) throws UserVertexNotPresent, InDegreeNotSetException {
//		Vertex userVertex = getUserVertex(userId);
//		if (userVertex == null)
//			throw new UserVertexNotPresent("Trying to add followers for user with id "+userId+" but user vertex is not in the graph.");
//		Object inDegree = userVertex.getProperty("inDegree");
//		if (inDegree==null) {
//			throw new InDegreeNotSetException("Trying to get inDegree for node of user with id="+userId+" but the inDegree has not been set");
//		}
//		return (Integer) inDegree;
//	}
//
//	@Override
//	public int getOutDegree(Long userId) throws OutDegreeNotSetException, UserVertexNotPresent {
//		Vertex userVertex = getUserVertex(userId);
//		Object outDegree = userVertex.getProperty("outDegree");
//		if (outDegree==null) {
//			throw new OutDegreeNotSetException("Trying to get outDegree for node of user with id="+userId+" but the outDegree has not been set");
//		}
//		return (Integer) outDegree;
//	}

	@Override
	public Map<Long, Integer> getInDegrees(List<Long> usersToBeCalculated, List<Long> sourceUsers) throws UserVertexNotPresent {
		logger.info("### calculateInDegree ###");
		Map<Long, Integer> user2inDegree = new HashMap<Long, Integer>();
		int percentCalculated = 0;
		final int tenPercent = Math.round((float)usersToBeCalculated.size()/10);
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
				if (sourceUsers.contains( (Long) (iterator.next().getProperty("userId")) ))
					inDegree++;
			}
			//userVertex.setProperty("inDegree", inDegree);
			user2inDegree.put(userId, inDegree);
		}
		return user2inDegree;
	}

	@Override
	public Map<Long, Integer> getOutDegrees(List<Long> usersToBeCalculated, List<Long> destinationUsers) throws UserVertexNotPresent {
		logger.info("### calculateOutDegree ###");
		Map<Long, Integer> user2outDegree = new HashMap<Long, Integer>();
		int percentCalculated = 0;
		final int tenPercent = Math.round((float)usersToBeCalculated.size()/10);
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
				//logger.info("userId="+userId+" - friendId="+friendId);
				if (destinationUsers.contains( (Long)friendVertex.getProperty("userId") ))
					outDegree++;
			}
			//userVertex.setProperty("outDegree", outDegree);
			user2outDegree.put(userId, outDegree);
		}
		return user2outDegree;
	}


}
