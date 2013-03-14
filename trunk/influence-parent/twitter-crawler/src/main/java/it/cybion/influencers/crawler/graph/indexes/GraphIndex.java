package it.cybion.influencers.crawler.graph.indexes;


import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;



public interface GraphIndex
{
	public void put(Long userId, Vertex vertex);

	public Vertex getVertex(Neo4jGraph graph, Long userId) throws UserVertexNotPresentException;
}
