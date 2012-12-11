package it.cybion.influence.graph;


public class GraphEnrichenerAgent {
	
	public static void main(String[] args) {
		enrichGraph("src/test/resources/graphs/food-46");
	}
	
	public static void enrichGraph(String graphPath) {
		UsersGraphFactory usersGraphFactory = new UsersGraphFactoryImpl(graphPath);
		usersGraphFactory.addNodesDegreesCounts();
		usersGraphFactory.getGraph().shutdown();
	}
	
}
