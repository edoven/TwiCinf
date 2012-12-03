package it.cybion.influence.graph;

<<<<<<< HEAD
import it.cybion.influence.model.User;

import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

=======
import com.tinkerpop.blueprints.Graph;
import it.cybion.influence.model.User;
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface UsersGraphFactory {
    Graph createGraph() throws GraphCreationException;

<<<<<<< HEAD
public class UsersGraphFactory {
	
	private static final Logger logger = Logger.getLogger(UsersGraphFactory.class);
	
	private Graph graph = null;
	private List<User> users = null;
	private Index<Vertex> index = null;
	
	public UsersGraphFactory(String filePath, List<User> users) {
		this.users = users;
		graph = new Neo4jGraph(filePath);
		index =  ((Neo4jGraph) graph).createIndex("vertexIndex", Vertex.class, new Parameter("analyzer", LowerCaseKeywordAnalyzer.class.getName()));
	}
		
	public Graph createGraph() throws GraphCreationException {
		for (int i=0; i<users.size(); i++) {	
			logger.info("Adding user "+(i+1)+"/"+users.size());
			User user = users.get(i);
			if (containsUser(user) == false)
				addUser(user);
			if (user.getFollowers() != null)
				addFollowers(user);
			if (user.getFriends() != null)
				addFriends(user);	
			logger.info("ok");
		}		
		return graph;
	}
	
	private void addUser(User user) {
		Vertex userVertex = graph.addVertex(null);
		userVertex.setProperty("userId", Long.toString(user.getId()));	
		index.put("userId", Long.toString(user.getId()), userVertex);
	}
	
	private void addFollowers(User user) throws GraphCreationException {
		for (User follower : user.getFollowers())
			addFollowsRelationship(follower, user);
	}
	
	private void addFriends(User user) throws GraphCreationException {
		for (User friend : user.getFriends())
			addFollowsRelationship(user , friend);
	}
	
	private void addFollowsRelationship(User follower, User followed) throws GraphCreationException {	
//		if (containsRelationship(follower, followed, "follows"))
//			throw new GraphCreationException("Trying to insert relation \"follows\" from "+follower.getId()+" to "+followed.getId()+" but relation already exists.");		
		if (containsUser(follower) == false)
			addUser(follower);
		if (containsUser(followed) == false)
			addUser(followed);
		
		Vertex followerVertex = getUserVertex(follower);
		Vertex followedVertex = getUserVertex(followed);
		if (followerVertex==null || followedVertex==null)
			throw new GraphCreationException("addFollowsRelationship - can't find followerVertex or followedVertex");
			
		graph.addEdge(null, followerVertex, followedVertex, "follows");	
		//logger.info("Added follows relationship from "+follower.getId()+" to "+followed.getId());
	}
		
//	//TODO: indexes...
//	//TODO: public is for test..this has to be private
//	public boolean containsUser(User user) {
//		Iterable<Vertex> vertices = graph.getVertices();	
//		for (Vertex vertex : vertices) {
//			if (vertex.getProperty("userId").equals(Long.toString(user.getId()))) //TODO: what if not all vertexes are users?? Add vertex class type.
//				return true;
//		}
//		return false;
//	}
	
	public boolean containsUser(User user) {
		Iterable<Vertex> results = index.get("userId", Long.toString(user.getId()));
		//if (results>1)
		//	throw something  - TODO
		return results.iterator().hasNext();
	}

	/*
	private Vertex getUserVertex(User user) {
		
		Iterable<Vertex> vertices = graph.getVertices();	
		for (Vertex vertex : vertices)
			if (vertex.getProperty("userId").equals(Long.toString(user.getId())))
				return vertex;
		logger.info("getUserVertex - can't find user "+user.getId());
		return null;
	}
	*/
	private Vertex getUserVertex(User user) {
		
		Iterable<Vertex> results = index.get("userId", Long.toString(user.getId()));
		return results.iterator().next();
	}
	

	/* TODO
	private boolean containsRelationship(User from, User to, String relationship) {
		return false;
	}
	*/
	
=======
    //TODO: indexes...
    //TODO: public is for test..this has to be private
    boolean containsUser(User user);
>>>>>>> 6d7d6e25606353dbc5c16e4606ae61a30e57de6d
}
