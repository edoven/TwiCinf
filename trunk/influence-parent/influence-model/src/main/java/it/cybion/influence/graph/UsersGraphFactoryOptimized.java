package it.cybion.influence.graph;

import it.cybion.influence.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


/*
 * This class take as input a follower/friends-enriched users list 
 * and return the graph containing:
 * -users (vertexes)
 * -"follows" relationships (edges)
 * 
 * BEWARE: there is no difference between friendship-relation and
 * followship-relation, both are represented with a "follows" relation:
 * -userA is in userB's friends-list -> userB follows userA
 * -userA is in userB's followers-list of -> userA follows userB
 */

public class UsersGraphFactoryOptimized {
	
	private static final Logger logger = Logger.getLogger(UsersGraphFactoryOptimized.class);
	
	private Neo4jGraph graph = null;
	private Index<Vertex> index = null;
	//private Map<String, Vertex> inseredVertices = new HashMap<String, Vertex>();
	
	public UsersGraphFactoryOptimized(String filePath) {
		graph = new Neo4jGraph(filePath);
		index =  ((Neo4jGraph) graph).createIndex("vertexIndex", Vertex.class, new Parameter("analyzer", LowerCaseKeywordAnalyzer.class.getName()));
	}
		
	public void addUsersToGraph(List<User> users) throws GraphCreationException {
		
		for (int i=0; i<users.size(); i++) {				
			User user = users.get(i);
			logger.info("Adding user "+(i+1)+"/"+users.size()+" (followers="+user.getFollowers().size()+"\t - friends="+user.getFriends().size()+")");
			Vertex userVertex = getUserVertex(user);
			if (userVertex == null)
				userVertex = addUser(user);			
			if (user.getFollowers() != null)
				addFollowers(user, userVertex);
			if (user.getFriends() != null)
				addFriends(user, userVertex);	
			logger.info("ok");
			graph.stopTransaction(Conclusion.SUCCESS); //this writes all in secondary memory to avoid main memory problems 
		}
	}
	
	
	public Graph getGraph(){
		return graph;
	}
	
	private Vertex addUser(User user) {
		Vertex userVertex = graph.addVertex(null);
		userVertex.setProperty("userId", Long.toString(user.getId()));	
		index.put("userId", Long.toString(user.getId()), userVertex);
		//inseredVertices.put(Long.toString(user.getId()), userVertex);
		return userVertex;
	}
	
	private void addFollowers(User user, Vertex userVertex) throws GraphCreationException {
		for (User follower : user.getFollowers()) {
			Vertex followerVertex = getUserVertex(follower);
			if (followerVertex==null)
				followerVertex = addUser(follower);
			addFollowsRelationship(followerVertex, userVertex);
		}
			
	}
	
	private void addFriends(User user, Vertex userVertex) throws GraphCreationException {
		for (User friend : user.getFriends()) {
			Vertex friendVertex = getUserVertex(friend);
			if (friendVertex==null)
				friendVertex = addUser(friend);
			addFollowsRelationship(userVertex, friendVertex);
		}
	}
	
	private void addFollowsRelationship(Vertex follower, Vertex followed) throws GraphCreationException {	
		if (follower==null || followed==null)
			throw new GraphCreationException("addFollowsRelationship - can't find followerVertex or followedVertex");			
		graph.addEdge(null, follower, followed, "follows");	
	}
		
	//this.
	private Vertex getUserVertex(User user) {	
		
		Iterable<Vertex> results = index.get("userId", Long.toString(user.getId()));
		Iterator<Vertex> iterator = results.iterator();
		if (iterator.hasNext()==false)
			return null;
		else
			return iterator.next();
			
		//return inseredVertices.get(Long.toString(user.getId()));
	}
	
}
