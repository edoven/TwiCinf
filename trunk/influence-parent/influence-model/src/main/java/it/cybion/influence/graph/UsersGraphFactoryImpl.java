package it.cybion.influence.graph;

import it.cybion.influence.model.User;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
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

public class UsersGraphFactoryImpl implements UsersGraphFactory {
	
	private static final Logger logger = Logger.getLogger(UsersGraphFactoryImpl.class);
	
	private Neo4jGraph graph = null;
	private Index<Vertex> index = null;
	private int usersCount = 0;
	private int authorsCount = 0;
	
	public UsersGraphFactoryImpl(String dirPath) {
		graph = new Neo4jGraph(dirPath);
		index =  graph.createIndex("vertexIndex", Vertex.class, new Parameter<String, String>("analyzer", LowerCaseKeywordAnalyzer.class.getName()));
	}
	

	@Override
	public void addUsersToGraph(List<User> users) throws GraphCreationException {	
		
		
		
		
		for (int i=0; i<users.size(); i++) {				
			User user = users.get(i);
			logger.info("Adding user "+user.getScreenName()+" - "+(i+1)+"/"+users.size()+" (followers="+user.getFollowers().size()+"\t - friends="+user.getFriends().size()+")");
			Vertex userVertex = getUserVertex(user);
			if (userVertex == null)
				userVertex = addUser(user);		
			else			
				if (userVertex.getProperty("screenName")==null) { 
					//this is the case when an author is already been added to the graph
					//as another author's friend or follower. The author was than added as an
					//"empty" user (only userId is set and screenName is null [not set]) 
					//and so I need to add other labels.
					userVertex.setProperty("isAuthor", true);
					userVertex.setProperty("screenName", user.getScreenName());
					userVertex.setProperty("friendsCount", user.getFriendsCount());
					userVertex.setProperty("followersCount", user.getFollowersCount());
					authorsCount++;
					usersCount--;
				}
			if (user.getFollowers() != null)
				addFollowers(user, userVertex);
			if (user.getFriends() != null)
				addFriends(user, userVertex);	
		}		
		graph.stopTransaction(Conclusion.SUCCESS); //this flushes all to avoid main memory problems 
		logger.info("UsersCount = "+usersCount+" - authorsCount = "+authorsCount);
	}
	
	@Override
	public void addNodesDegreesCounts(){
		Iterable<Vertex> vertices = graph.getVertices();
		int currentVertex = 1;
		for (Vertex vertex : vertices) {
			int inEdges = 0;
			int outEdges = 0;
			//Let's calculate inEdges count
			Iterator<Edge> iterator = vertex.getEdges(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				inEdges++;
				iterator.next();
			}	
			//Let's calculate outEdges count
			iterator = vertex.getEdges(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				outEdges++;
				iterator.next();
			}			
			vertex.setProperty("inDegree", inEdges);
			vertex.setProperty("outDegree", outEdges);
			vertex.setProperty("degree", (inEdges+outEdges) );
			
			currentVertex++;
			
			//Every 1000 nodes we do a flush (stopTransaction on graph)
			if ((currentVertex % 1000) == 0) {
				graph.stopTransaction(Conclusion.SUCCESS); //this flushes all to avoid main memory problems 
				logger.info(currentVertex+" vertices enriched");
			}
		}
	}
	
	@Override
	public Graph getGraph(){
		return graph;
	}
	
	private Vertex addUser(User user) {
		Vertex userVertex = graph.addVertex(null);
		userVertex.setProperty("userId", Long.toString(user.getId()));	
		
		String screenName = user.getScreenName();
		//Only a dataset's tweet author has a full profile.
		if (screenName==null)
			userVertex.setProperty("isAuthor", false);
		else {
			userVertex.setProperty("isAuthor", true);
			userVertex.setProperty("screenName", screenName);
			userVertex.setProperty("friendsCount", user.getFriendsCount());
			userVertex.setProperty("followersCount", user.getFollowersCount());
			authorsCount++;
		}				
		userVertex.setProperty("userId", Long.toString(user.getId()));	
		index.put("userId", Long.toString(user.getId()), userVertex);
		usersCount++;
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
		
	private Vertex getUserVertex(User user) {			
		Iterable<Vertex> results = index.get("userId", Long.toString(user.getId()));
		Iterator<Vertex> iterator = results.iterator();
		if (iterator.hasNext()==false)
			return null;
		else
			return iterator.next();
	}

	@Override
	public int getUsersCount() {
		return usersCount;
	}
	
}
