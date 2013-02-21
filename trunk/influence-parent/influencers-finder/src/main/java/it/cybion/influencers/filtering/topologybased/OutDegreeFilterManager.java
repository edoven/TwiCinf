package it.cybion.influencers.filtering.topologybased;


import it.cybion.influencers.graph.exceptions.UserVertexNotPresentException;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



public class OutDegreeFilterManager extends DegreeFilterManager
{

	private static final Logger logger = Logger.getLogger(OutDegreeFilterManager.class);

	private float outDegreePercentageThreshold;
	private int outDegreeAbsoluteThreshold;
	private Map<Long, Integer> node2outDegree;

	public OutDegreeFilterManager(float outDegreePercentageThreshold)
	{
		this.outDegreePercentageThreshold = outDegreePercentageThreshold;
	}

	protected void setAbsoluteThresholds()
	{
		outDegreeAbsoluteThreshold = (int) Math.round((outDegreePercentageThreshold * this.seedUsers.size()));
		if (outDegreeAbsoluteThreshold < 2)
			outDegreeAbsoluteThreshold = 2;
	}

	@Override
	public List<Long> filter()
	{
		solveDependencies();
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(node2outDegree, outDegreeAbsoluteThreshold);
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		logger.info("outDegreeFiltered.size()=" + outDegreeFiltered.size());
		return outDegreeFiltered;
	}

	protected void calculateNodeDegrees()
	{
		try
		{
			// this sets an outDegree label in the graph for each node of
			// followersAndFriends set
			node2outDegree = graphFacade.getOutDegrees(followersAndFriends, seedUsers);
		} catch (UserVertexNotPresentException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public String toString()
	{
		String inputSize = "NotSet";
		String outDegreeAbsThreshold = "CannotBeCalculated";
		if (seedUsers != null)
		{
			inputSize = Integer.toString(seedUsers.size());
			outDegreeAbsThreshold = Integer.toString(outDegreeAbsoluteThreshold);
		}
		return "OutDegreeFilterManager" + " (outDegreePercentageThreshold=" + outDegreePercentageThreshold * 100 + "%" + " - outDegreeAbsoluteThreshold=" + outDegreeAbsThreshold + " - inputSize=" + inputSize + ")";
	}
}
