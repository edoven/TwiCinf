package it.cybion.influencers.crawler.graph.indexes;

import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;

import java.util.Iterator;



public class LuceneIndex implements GraphIndex
{

	Index<Vertex> index;

	public LuceneIndex(Index<Vertex> index)
	{
		this.index = index;
	}

	@Override
	public void put(Long userId, Vertex vertex)
	{
		index.put("userId", userId, vertex);
	}

	@Override
	public Vertex getVertex(Neo4jGraph graph, Long userId) throws UserVertexNotPresentException
	{
		Iterator<Vertex> iterator = index.get("userId", userId).iterator();
		if (iterator.hasNext())
			return iterator.next();
		else
			throw new UserVertexNotPresentException("Trying to get node for user with id=" + userId + " but node is not in the graph");
	}
}
