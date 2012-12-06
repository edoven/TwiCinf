package it.cybion.influence.graph;

import java.util.Iterator;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;


public class SubsetCreator {
	public static void main(String args[]) {
		Neo4jGraph original = new Neo4jGraph("src/test/resources/graphs/TwitterGraph");
		Neo4jGraph authorsGraph = new Neo4jGraph("/home/godzy/authorsGraph");
		
		Iterator<Vertex> vertexIterator = original.getVertices().iterator();
		while (vertexIterator.hasNext()) {
			Vertex vertex = vertexIterator.next();
			if ((Boolean) vertex.getProperty("isAuthor") == true)
				authorsGraph.addVertex(vertex);
			System.out.println("added use");
		}
		
		Iterator<Edge> edgeIterator = original.getEdges().iterator();
		while (edgeIterator.hasNext()) {
			Edge edge = edgeIterator.next();
			Vertex inVertex = edge.getVertex(Direction.IN);
			Vertex outVertex = edge.getVertex(Direction.OUT);
			if ((Boolean) inVertex.getProperty("isAuthor") == true && 
				(Boolean) outVertex.getProperty("isAuthor") == true)
				authorsGraph.addEdge(null, outVertex, inVertex, "follows");
		}
		authorsGraph.shutdown();
		original.shutdown();
		
	}
}
