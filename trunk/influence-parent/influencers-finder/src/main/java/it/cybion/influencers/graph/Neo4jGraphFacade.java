package it.cybion.influencers.graph;

import java.util.Iterator;
import java.util.List;

import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;

import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class Neo4jGraphFacade implements GraphFacade {
	
	private Neo4jGraph graph;
	private Index<Vertex> vertexIndex;
	
	public Neo4jGraphFacade(String dirPath) {
		graph = new Neo4jGraph(dirPath);
		vertexIndex =  graph.createIndex("vertexIndex", Vertex.class, new Parameter<String, String>("analyzer", LowerCaseKeywordAnalyzer.class.getName()));
	}

	@Override
	public void addUsers(List<Long> usersIds) {
		for (Long userId : usersIds) {
			if (getUserVertex(userId)==null) {
				Vertex vertex = graph.addVertex(null);
				vertex.setProperty("userId", userId);
				vertex.setProperty("type", "user");
			}
		}
	}
	
	
	private Vertex getUserVertex(long userId) {
		Iterable<Vertex> results = vertexIndex.get("userId",userId);
		Iterator<Vertex> iterator = results.iterator();
		if (iterator.hasNext()==false)
			return null;
		else
			return iterator.next();
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
				followerVertex.setProperty("userId", userId);
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
				friendVertex.setProperty("userId", userId);
				friendVertex.setProperty("type", "user");
			}
			graph.addEdge(null, userVertex, friendVertex, "follows");	
		}
	}

	@Override
	public void calculateDirectedFollowsDegree(List<Long> fromUsers, List<Long> toUsers) {
		// TODO Auto-generated method stub
	}

	@Override
	public void calculateTotalFollowsDegree(List<Long> totUsers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInDegree(Long userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutDegree(Long userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalDegree(Long userId) {
		// TODO Auto-generated method stub
		return 0;
	}


}
