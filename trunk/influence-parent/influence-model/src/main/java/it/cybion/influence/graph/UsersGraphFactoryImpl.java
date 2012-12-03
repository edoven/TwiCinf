package it.cybion.influence.graph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import it.cybion.influence.model.User;
import org.apache.log4j.Logger;

import java.util.List;


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

public class UsersGraphFactoryImpl implements UsersGraphFactory {
	
	private static final Logger logger = Logger.getLogger(UsersGraphFactoryImpl.class);
	
	private Graph graph = null;
	private List<User> users = null;
	
	public UsersGraphFactoryImpl(String filePath, List<User> users) {
		this.users = users;
		graph = new TinkerGraph(filePath);
	}
	
	
	@Override
    public Graph createGraph() throws GraphCreationException {
		for (User user : users) {	
			if (!containsUser(user))
				addUser(user);
			if (user.getFollowers() != null)
				addFollowers(user);
			if (user.getFriends() != null)
				addFriends(user);		
		}
		
		return graph;
	}
	
	private void addUser(User user) {
		Vertex userVertex = graph.addVertex(null);
		userVertex.setProperty("userId", Long.toString(user.getId()));	
		logger.info(user.getId()+" added.");
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
		/*
		if (containsRelationship(follower, followed, "follows"))
			throw new GraphCreationException("Trying to insert relation \"follows\" from "+follower.getId()+" to "+followed.getId()+" but relation already exists.");
		*/
		if (!containsUser(follower))
			addUser(follower);
		if (!containsUser(followed))
			addUser(followed);
		
		Vertex followerVertex = getUserVertex(follower);
		Vertex followedVertex = getUserVertex(followed);
		if (followerVertex==null || followedVertex==null)
			throw new GraphCreationException("addFollowsRelationship - can't find followerVertex or followedVertex");
			
		graph.addEdge(null, followerVertex, followedVertex, "follows");	
		logger.info("Added follows relationship from "+follower.getId()+" to "+followed.getId());
	}
		
	//TODO: indexes...
	//TODO: public is for test..this has to be private
	@Override
    public boolean containsUser(User user) {
		
		Iterable<Vertex> vertices = graph.getVertices();	
		for (Vertex vertex : vertices) {
			//logger.info("containsUser - user ="+user.getId()+" vertex.userId="+vertex.getProperty("userId"));
			if (vertex.getProperty("userId").equals(Long.toString(user.getId()))) //TODO: what if not all vertexes are users?? Add vertex class type.
				return true;
		}
		return false;
	}

	private Vertex getUserVertex(User user) {
		
		Iterable<Vertex> vertices = graph.getVertices();	
		for (Vertex vertex : vertices)
			if (vertex.getProperty("userId").equals(Long.toString(user.getId())))
				return vertex;
		logger.info("getUserVertex - can't find user "+user.getId());
		return null;
	}
	

	/* TODO
	private boolean containsRelationship(User from, User to, String relationship) {
		return false;
	}
	*/
	
}
