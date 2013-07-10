package it.cybion.influencers.crawler.filtering.topologybased;

import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;
import org.apache.log4j.Logger;

import java.util.*;



public class InAndOutDegreeFilterManager extends DegreeFilterManager
{

	private static final Logger logger = Logger.getLogger(InAndOutDegreeFilterManager.class);
	private float inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private float outDegreePercentageThreshold;
	private int outDegreeAbsoluteThreshold;
	private Map<Long, Integer> node2inDegree;
	private Map<Long, Integer> node2outDegree;

	public InAndOutDegreeFilterManager(Float inDegreePercentageThreshold, Float outDegreePercentageThreshold)
	{
		super();
		this.inDegreePercentageThreshold = inDegreePercentageThreshold;
		this.outDegreePercentageThreshold = outDegreePercentageThreshold;
	}

	protected void setAbsoluteThresholds()
	{
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * seedUsers.size()));
		if (inDegreeAbsoluteThreshold < 1)
			inDegreeAbsoluteThreshold = 1;
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * seedUsers.size()));
		if (outDegreeAbsoluteThreshold < 2)
			outDegreeAbsoluteThreshold = 2;
	}

	public List<Long> filter()
	{
		super.solveDependencies();
		try
		{
			node2inDegree = graphFacade.getInDegrees(followersAndFriends, seedUsers);
		}
		catch (UserVertexNotPresentException e)
		{
			//unexpected error
			e.printStackTrace();
			System.exit(0);
		}
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(node2inDegree, inDegreeAbsoluteThreshold);
		node2inDegree = null;
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		inDegreeFilter = null;
		logger.info("inDegreeFiltered.size()=" + inDegreeFiltered.size());
		
		try
		{
			node2outDegree = graphFacade.getOutDegrees(followersAndFriends, seedUsers);
		}
		catch (UserVertexNotPresentException e)
		{
			//unexpected error
			e.printStackTrace();
			System.exit(0);
		}
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(node2outDegree, outDegreeAbsoluteThreshold);
		node2outDegree = null;
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		outDegreeFilter = null;	
		logger.info("outDegreeFiltered.size()=" + outDegreeFiltered.size());
		
		List<Long> inAndOutDegreeFiltered = putListsInAnd(inDegreeFiltered, outDegreeFiltered);
		logger.info("inAndOutDegreeFiltered.size()=" + inAndOutDegreeFiltered.size());
		return inAndOutDegreeFiltered;
	}


	private List<Long> putListsInAnd(List<Long> listA, List<Long> listB)
	{
		Set<Long> andSet = new HashSet<Long>();
				
		for (Long elementA : listA)
			if (listB.contains(elementA))
				andSet.add(elementA);
		for (Long elementB : listB)
			if (listA.contains(elementB))
				andSet.add(elementB);
		
		return new ArrayList<Long>(andSet);
	}

	@Override
	public String toString()
	{
		String inputSize = "NotSet";
		String inDegreeAbsThreshold = "CannotBeCalculated";
		String outDegreeAbsThreshold = "CannotBeCalculated";
		if (seedUsers != null)
		{
			inputSize = Integer.toString(seedUsers.size());
			inDegreeAbsThreshold = Integer.toString(inDegreeAbsoluteThreshold);
			outDegreeAbsThreshold = Integer.toString(outDegreeAbsoluteThreshold);
		}
		return "InAndOutDegreeFilterManager" + " (inDegreePercentageThreshold=" + inDegreePercentageThreshold * 100 + "%" + " - outDegreePercentageThreshold=" + outDegreePercentageThreshold * 100 + "%" + " - inDegreeAbsoluteThreshold="
				+ inDegreeAbsThreshold + " - outDegreeAbsoluteThreshold=" + outDegreeAbsThreshold + " - inputSize=" + inputSize + ")";
	}

	@Override
	protected void calculateNodeDegrees()
	{
		//due to optimization problems nodeDegrees are calculate inside filter() method
	}

}
