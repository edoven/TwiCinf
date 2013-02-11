package it.cybion.influencers.graph.indexes;

import it.cybion.influencers.graph.UserVertexNotPresent;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public interface GraphIndex {
	public void put(Long userId, Vertex vertex);
	public Vertex getVertex(Neo4jGraph graph, Long userId) throws UserVertexNotPresent;
}
