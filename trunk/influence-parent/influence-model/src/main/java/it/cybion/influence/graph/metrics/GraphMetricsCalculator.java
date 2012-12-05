package it.cybion.influence.graph.metrics;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class GraphMetricsCalculator {
	
	private static final Logger logger = Logger.getLogger(GraphMetricsCalculator.class);
	
	private Graph graph;
	
	public GraphMetricsCalculator(Graph graph) {
		this.graph = graph;
	}

	
	//<0,10> -> 10 vertices have more than 0 in-edges
	//<1, 7>  ->  7 vertices have more than 1 in-edges
	//<2, 5>  ->  5 vertices have more than 2 in-edges
	//<3, 3>  ->  4 vertices have more than 3 in-edges
	//<4, 1>  ->  1 vertices have more than 4 in-edges
	//<5, 0>  ->  0 vertices have more than 5 in-edges
	public Map<Integer, Integer> calculateInDegreePartitions() {
		Iterator<Vertex> iterator = graph.getVertices().iterator();
		Map<Integer, Integer> inDegree2edgesCount = new HashMap<Integer, Integer>();
		initInDegree2edgesCount(inDegree2edgesCount);
		
		int node = 1;
		while (iterator.hasNext()) {
			Vertex vertex = iterator.next();
			int inDegree = (Integer)(vertex.getProperty("inDegree"));
			logger.info("Adding node " + (node++) + " (inDegree=" + inDegree + ")" );
			addInDegreeValue(inDegree2edgesCount, inDegree);			
		}
		return inDegree2edgesCount;
	}
	
	
	
	private void addInDegreeValue(Map<Integer, Integer> inDegree2edgesCount, int inDegreeValue) {
		for (int i=0; i<inDegreeValue+1; i++)
			inDegree2edgesCount.put( i, (inDegree2edgesCount.get(i) + 1) );

	}
	
	private void initInDegree2edgesCount(Map<Integer, Integer> inDegree2edgesCount) {
		for (int i=0; i<=10000; i++)
			inDegree2edgesCount.put(i, 0);
	}
	
	
	public Map<Vertex, Integer> getAuthorsToAuthorFollowersCount() {
		Map<Vertex, Integer> author2authorFollowersCount = new HashMap<Vertex, Integer>();
		Iterator<Vertex> usersIterator = graph.getVertices().iterator();
		while(usersIterator.hasNext()) {
			Vertex vertex = usersIterator.next();
			if ( (Boolean)(vertex.getProperty("isAuthor")) == true ) {
				Iterator<Vertex> followersIterator = vertex.getVertices(Direction.IN, "follows").iterator();
				int counter = 0;
				while (followersIterator.hasNext()) {
					Vertex follower = followersIterator.next();
					if ( (Boolean)(follower.getProperty("isAuthor")) == true ) {
						counter++;
					}
				}
				author2authorFollowersCount.put(vertex, counter);
			}
		}
		return author2authorFollowersCount;
	}
	
}
