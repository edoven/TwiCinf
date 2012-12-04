package it.cybion.influence.graph;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class GraphEnrichener {
	
	private static final Logger logger = Logger.getLogger(GraphEnrichener.class);
	
	private Neo4jGraph graph = null;
	
	public GraphEnrichener(Neo4jGraph graph) {
		this.graph = graph;
	}
	
	public void addNodesDegreesCounts() {
		Iterable<Vertex> vertices = graph.getVertices();
		int currentVertex = 1;
		for (Vertex vertex : vertices) {
			int inEdges = 0;
			int outEdges = 0;
			Iterator<Edge> iterator = vertex.getEdges(Direction.IN, "follows").iterator();
			while (iterator.hasNext()) {
				inEdges++;
				iterator.next();
			}
			
			iterator = vertex.getEdges(Direction.OUT, "follows").iterator();
			while (iterator.hasNext()) {
				outEdges++;
				iterator.next();
			}
			
			vertex.setProperty("inDegree", inEdges);
			vertex.setProperty("outDegree", outEdges);
			vertex.setProperty("degree", (inEdges+outEdges) );
			
			currentVertex++;
			
			if ((currentVertex % 1000) == 0) {
				graph.stopTransaction(Conclusion.SUCCESS); //this flushes all to avoid main memory problems 
				logger.info(currentVertex+" vertices enriched");
			}
		}		
	}
	
	public Neo4jGraph getGraph() {
		return graph;
	}
}
