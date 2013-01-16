package it.cybion.influencers.graph;



import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Transaction;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class Neo4jGraphFacade implements GraphFacade {
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);
	
	private Neo4jGraph graph;
	private Index<Vertex> vertexIndex;
	private Transaction transaction;
	private int insersionsCount = 0;
	private String dirPath;
	
	
	public Neo4jGraphFacade(String dirPath) {
		this.dirPath = dirPath;
		graph = new Neo4jGraph(dirPath);
		vertexIndex =  graph.createIndex("vertexIndex", 
										 Vertex.class, 
										 new Parameter<String, String>(	
												 "type", 
												 "exact"));
		
		transaction = graph.getRawGraph().beginTx();
	}
	
	@Override
	public void eraseGraphAndRecreate() throws IOException {
		graph.shutdown();
		FilesDeleter.delete(new File(dirPath));
		graph = new Neo4jGraph(dirPath);
		vertexIndex =  graph.createIndex("vertexIndex", 
										 Vertex.class, 
										 new Parameter<String, String>(	
												 "type", 
												 "exact"));
		
		transaction = graph.getRawGraph().beginTx();
	}
	

	public Vertex addUser(Long userId) {
				
		Vertex vertex;
		try {
			vertex = getUserVertex(userId);
		} catch (UserVertexNotPresent e) {
			vertex = graph.addVertex(null);
			vertex.setProperty("userId", userId);
			vertex.setProperty("nodeType", "user");
			vertexIndex.put("userId", userId, vertex); //do not forget this ;)
			//logger.info("added node for user with id="+userId);
		}	
		
		insersionsCount++;
		if (insersionsCount>0 && (insersionsCount%1000)==0) {
			transaction.success();
			transaction.finish();
			logger.debug("transaction closed");
			transaction = graph.getRawGraph().beginTx();
		}	
		
		return vertex;
	}
	
	@Override
	public void addUsers(List<Long> usersIds) {
		for (Long userId : usersIds)
			addUser(userId);
	}
		
	@Override
	public void addFollowers(Long userId, List<Long> followersIds) throws UserVertexNotPresent {
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
		}
	}

	@Override
	public void addFriends(Long userId, List<Long> friendsIds) throws UserVertexNotPresent {
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
		}
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
	public int getTotalDegree(Long userId) throws TotalDegreeNotSetException, UserVertexNotPresent {
		Vertex userVertex = getUserVertex(userId);
		Object totalDegree = userVertex.getProperty("totalDegree");
		if (totalDegree==null) {
			throw new TotalDegreeNotSetException("Trying to get totalDegree for node of user with id="+userId+" but the totalDegree has not been set");
		}
		return (Integer) totalDegree;
	}

	@Override
	public void calculateInDegree(List<Long> usersToBeCalculated, List<Long> sourceUsers) throws UserVertexNotPresent {
		logger.info("### calculateInDegree ###");
		for (int i=0; i<usersToBeCalculated.size(); i++) {
			logger.info("calculating iDegree for user "+i+" of "+usersToBeCalculated.size());
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
			//logger.info("Set indegree="+inDegree+" for user with id="+userId);
		}
	}

	@Override
	public void calculateOutDegree(List<Long> usersToBeCalculated, List<Long> destinationUsers) throws UserVertexNotPresent {
		logger.info("### calculateOutDegree ###");
		for (int i=0; i<usersToBeCalculated.size(); i++) {
			logger.info("calculating outDegree for user "+i+" of "+usersToBeCalculated.size());
			long userId = usersToBeCalculated.get(i);
			int outDegree = 0;
			Vertex userVertex = getUserVertex(userId);
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
			//logger.info("Set outDegree="+outDegree+" for user with id="+userId);
		}
	}

	
	
//	@Override
//	public void calculateTotalDegree(List<Long> usersToBeCalculated) throws UserVertexNotPresent {
//		for (Long userId : usersToBeCalculated) {
//			int outDegree = 0;
//			int inDegree = 0;
//			Vertex userVertex = getUserVertex(userId);
//			if (userVertex == null) {
//				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
//			}
//			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
//			while (iterator.hasNext()) {
//				iterator.next();
//				outDegree++;
//			}
//			iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
//			while (iterator.hasNext()) {
//				iterator.next();
//				inDegree++;
//			}
//			userVertex.setProperty("totalDegree", inDegree+outDegree);
//		}
//	}
//
//	@Override
//	public void calculateTotalDegree(List<Long> usersToBeCalculated,List<Long> wrtUsers) throws UserVertexNotPresent {
//		for (Long userId : usersToBeCalculated) {
//			int outDegree = 0;
//			int inDegree = 0;
//			Vertex userVertex = getUserVertex(userId);
//			if (userVertex == null) {
//				throw new UserVertexNotPresent("Trying to get user with id "+userId+" but user vertex is not in the graph.");
//			}
//			Iterator<Vertex> iterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
//			while (iterator.hasNext()) {
//				Vertex followerVertex = iterator.next();
//				Long followerId = (Long)followerVertex.getProperty("userId");
//				if (wrtUsers.contains( followerId ))
//					outDegree++;
//			}
//			iterator = userVertex.getVertices(Direction.IN, "follows").iterator();
//			while (iterator.hasNext()) {
//				Vertex friendVertex = iterator.next();
//				Long friendId = (Long)friendVertex.getProperty("userId");
//				if (wrtUsers.contains( friendId ))
//					inDegree++;
//			}
//			userVertex.setProperty("totalDegree", inDegree+outDegree);
//		}
//	}


}
