package it.cybion.influence.graph;

import org.apache.log4j.Logger;
 
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class GraphEnrichenerAGENT {
	
	private static final Logger logger = Logger.getLogger(GraphEnrichenerAGENT.class);

	public static void main(String args[]) {
		Neo4jGraph graph = new Neo4jGraph("src/test/resources/graphs/TwitterGraphComplete");
		
		//enrichGraphWithNodesDegrees(graph);
		printLabels(graph);
	}
	
	public static void enrichGraphWithNodesDegrees(Neo4jGraph graph) {
		GraphEnrichener enrichener = new GraphEnrichener(graph);
		enrichener.addNodesDegreesCounts();
		graph = enrichener.getGraph();
		graph.shutdown();
	}
	
	public static void printLabels(Neo4jGraph graph){
		Iterable<Vertex> vertices = graph.getVertices();
		int count = 1;
		for (Vertex vertex : vertices)
			logger.info( (count++) + "- inDegree:"+vertex.getProperty("inDegree")+ " - outDegree:"+vertex.getProperty("outDegree"));
	}
	
}
