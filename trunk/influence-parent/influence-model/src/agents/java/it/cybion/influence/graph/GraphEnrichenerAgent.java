package it.cybion.influence.graph;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import it.cybion.influence.graph.GraphEnrichener;

public class GraphEnrichenerAgent {
	
	public static void main(String[] args) {
		enrichGraph("src/test/resources/graphs/food-46");
	}
	
	public static void enrichGraph(String graphPath) {
		Neo4jGraph graph = new Neo4jGraph(graphPath);
		GraphEnrichener graphEnrichener = new GraphEnrichener(graph);
		graphEnrichener.addNodesDegreesCounts();
		graph.shutdown();
	}
	
}
