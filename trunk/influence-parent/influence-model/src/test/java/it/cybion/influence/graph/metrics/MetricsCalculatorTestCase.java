package it.cybion.influence.graph.metrics;

import static org.testng.Assert.assertEquals;
import java.util.Map;

import org.testng.annotations.Test;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class MetricsCalculatorTestCase {

	
	@Test
	public void calculateInDegreePartitionsTEST() {
		Graph graph = new TinkerGraph();
		Vertex v1 = graph.addVertex(null);
		Vertex v2 = graph.addVertex(null);
		Vertex v3 = graph.addVertex(null);
		Vertex v4 = graph.addVertex(null);
		Vertex v5 = graph.addVertex(null);
		
		v1.setProperty("inDegree", 1);
		v2.setProperty("inDegree", 1);
		v3.setProperty("inDegree", 2);
		v4.setProperty("inDegree", 2);
		v5.setProperty("inDegree", 5);
		
		GraphMetricsCalculator metricsCalculator = new GraphMetricsCalculator(graph);
		Map<Integer, Integer> inDegree2edgesCount = metricsCalculator.calculateInDegreePartitions();
		//System.out.println(inDegree2edgesCount);
		assertEquals( (int)inDegree2edgesCount.get(0) , 5);
		assertEquals( (int)inDegree2edgesCount.get(1) , 5);
		assertEquals( (int)inDegree2edgesCount.get(2) , 3);
		assertEquals( (int)inDegree2edgesCount.get(3) , 1);
		assertEquals( (int)inDegree2edgesCount.get(4) , 1);
		assertEquals( (int)inDegree2edgesCount.get(5) , 1);
		assertEquals( (int)inDegree2edgesCount.get(6) , 0);
	}
}
