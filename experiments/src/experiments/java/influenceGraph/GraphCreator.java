package influenceGraph;

import java.util.List;

import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;



public class GraphCreator {

	public static void createGraph(String graphPath, List<User> users) {
		Neo4jGraph graph = new Neo4jGraph(graphPath);
		Index<Vertex> vertexIndex = graph.createIndex("vertexIndex", Vertex.class);		
	}
	
	
}
