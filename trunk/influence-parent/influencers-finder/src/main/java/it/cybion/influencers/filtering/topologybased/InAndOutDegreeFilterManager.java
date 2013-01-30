package it.cybion.influencers.filtering.topologybased;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.cybion.influencers.graph.UserVertexNotPresent;

public class InAndOutDegreeFilterManager extends DegreeFilterManager {
	
	private static final Logger logger = Logger.getLogger(InAndOutDegreeFilterManager.class);
	private double inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private double outDegreePercentageThreshold;	
	private int outDegreeAbsoluteThreshold;
	private Map<Long, Integer> node2inDegree;
	private Map<Long, Integer> node2outDegree;

	
	public InAndOutDegreeFilterManager(	double inDegreePercentageThreshold,
										double outDegreePercentageThreshold) {
		super();
		this.inDegreePercentageThreshold = inDegreePercentageThreshold;
		this.outDegreePercentageThreshold = outDegreePercentageThreshold;
	}
			

	protected void setAbsoluteThresholds() {
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * seedUsers.size()));
		if (inDegreeAbsoluteThreshold<1)
			inDegreeAbsoluteThreshold = 1;
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * seedUsers.size()));
		if (outDegreeAbsoluteThreshold<2)
			outDegreeAbsoluteThreshold = 2;
	}
	

	public List<Long> filter()  {
		super.solveDependencies();	
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(node2inDegree, inDegreeAbsoluteThreshold);
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		logger.info("inDegreeFiltered.size()="+inDegreeFiltered.size());		
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(node2outDegree,outDegreeAbsoluteThreshold);
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		logger.info("outDegreeFiltered.size()="+outDegreeFiltered.size());		
		List<Long> inAndOutDegreeFiltered = putListsInAnd(inDegreeFiltered,outDegreeFiltered);
		logger.info("inAndOutDegreeFiltered.size()="+inAndOutDegreeFiltered.size());
		return inAndOutDegreeFiltered;		
	}
	
	
	protected void calculateNodeDegrees() {
		try {
			//this sets an inDegree label in the graph for each node of followersAndFriends set
			node2inDegree = graphFacade.getInDegrees(followersAndFriends, seedUsers); 
			//this sets an outDegree label in the graph for each node of followersAndFriends set
			node2outDegree = graphFacade.getOutDegrees(followersAndFriends, seedUsers);	
		} catch (UserVertexNotPresent e) {			
			e.printStackTrace();
			System.exit(0);
		}			
	}
	
	
	private List<Long> putListsInAnd(List<Long> listA, List<Long> listB) {
		List<Long> andList = new ArrayList<Long>();
		for (Long elementA : listA)
			if (listB.contains(elementA))
				andList.add(elementA);
		for (Long elementB : listB)
			if (listA.contains(elementB) && !andList.contains(elementB))
				andList.add(elementB);
		andList = new ArrayList<Long>( new HashSet<Long>(andList));
		return andList;
	}
	

	@Override
	public String toString() {
		String inputSize = "NotSet";
		String inDegreeAbsThreshold = "CannotBeCalculated";
		String outDegreeAbsThreshold = "CannotBeCalculated";
		if (seedUsers!=null ) {
			inputSize = Integer.toString(seedUsers.size());
			inDegreeAbsThreshold = Integer.toString(inDegreeAbsoluteThreshold);
			outDegreeAbsThreshold = Integer.toString(outDegreeAbsoluteThreshold);
		}
		return "InAndOutDegreeFilterManager" +
				" (inDegreePercentageThreshold="+inDegreePercentageThreshold*100+"%"+
				" - outDegreePercentageThreshold="+outDegreePercentageThreshold*100+"%"+
				" - inDegreeAbsoluteThreshold="+inDegreeAbsThreshold+
				" - outDegreeAbsoluteThreshold="+outDegreeAbsThreshold+
				" - inputSize="+inputSize+")";
	}
	
}
