package it.cybion.influence.graph.metrics;

import it.cybion.influence.util.MapSorter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class GraphMetricsPrinter {
	
	private static final Logger logger = Logger.getLogger(GraphMetricsPrinter.class);
	
	@Test(enabled=false)
	public void printInDegreePartition() {
		Graph graph = new Neo4jGraph("src/test/resources/graphs/TwitterGraphComplete");
		GraphMetricsCalculator metricsCalculator = new GraphMetricsCalculator(graph);
		Map<Integer,Integer> inDegree2edgesCount = metricsCalculator.calculateInDegreePartitions();
		//map serialization		
		try {
			FileOutputStream fos = new FileOutputStream("src/test/resources/graphs/TwitterGraphCompleteMap.ser");
	        ObjectOutputStream objOut = new ObjectOutputStream(fos);
			objOut.writeObject(inDegree2edgesCount);
	        objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		logger.info(inDegree2edgesCount);
		int verticesCount = inDegree2edgesCount.get(0);
		for (Integer key : inDegree2edgesCount.keySet() ) {
			int value = inDegree2edgesCount.get(key);
			double valuePercent = (double)value / verticesCount;
			System.out.printf(key + " - " +value+" - "+ "%f\n", valuePercent);
		}
	}
	
	@Test(enabled=false)
	public void printMap() {
		try {
			FileInputStream fis = new FileInputStream("src/test/resources/graphs/TwitterGraphCompleteMap.ser");
			ObjectInputStream objIn = new ObjectInputStream(fis);
			Map<Integer,Integer> inDegree2edgesCount = (Map<Integer, Integer>) objIn.readObject();
			objIn.close();
			int verticesCount = inDegree2edgesCount.get(0);
			List<Integer> keys = new ArrayList<Integer>(inDegree2edgesCount.keySet());
			Collections.sort(keys);
			for (Integer key : keys ) {
				int value = inDegree2edgesCount.get(key);
				double valuePercent = (double)value / verticesCount;
				System.out.printf(key + " - " +value+" - "+ "%f\n", valuePercent);
					//System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled=true)
	public void getAuthorsToAuthorFollowersCountPRINTER(){
		Graph graph = new Neo4jGraph("src/test/resources/graphs/TwitterGraph");
		GraphMetricsCalculator metricsCalculator = new GraphMetricsCalculator(graph);
		Map<Vertex, Integer> authorsToAuthorFollowersCount = metricsCalculator.getAuthorsToAuthorFollowersCount();
		authorsToAuthorFollowersCount = MapSorter.sortMapByValuesDescending(authorsToAuthorFollowersCount);
	
		Map<Vertex, Double> vertex2rate = new HashMap<Vertex, Double>();
		for (Vertex vertex : authorsToAuthorFollowersCount.keySet())
			vertex2rate.put(vertex, (double)authorsToAuthorFollowersCount.get(vertex)/(Integer)vertex.getProperty("followersCount"));
		
		vertex2rate = MapSorter.sortMapByValuesDescending(vertex2rate);		
		for (Vertex vertex : vertex2rate.keySet())
		logger.info(vertex.getProperty("screenName") + " - " +
						authorsToAuthorFollowersCount.get(vertex) + " - "+
						vertex.getProperty("followersCount") + " - "+
						vertex.getProperty("friendsCount") + " - "+
						vertex2rate.get(vertex));
	}
	
	
}
