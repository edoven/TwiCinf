package it.cybion.influencers.crawler.filtering.topologybased;

import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;



public class InDegreeFilterManager extends DegreeFilterManager
{

	private static final Logger LOGGER = Logger.getLogger(InDegreeFilterManager.class);

	private float inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private Map<Long, Integer> node2inDegree;

	public InDegreeFilterManager(Float inDegreePercentageThreshold)
	{
		this.inDegreePercentageThreshold = inDegreePercentageThreshold;
	}

	protected void setAbsoluteThresholds()
	{
		inDegreeAbsoluteThreshold = (int) Math.round((inDegreePercentageThreshold * this.seedUsers.size()));
		if (inDegreeAbsoluteThreshold < 2)
			inDegreeAbsoluteThreshold = 2;
	}

	@Override
	public List<Long> filter()
	{
		solveDependencies();
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(node2inDegree, inDegreeAbsoluteThreshold);
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		LOGGER.info("inDegreeFiltered.size()=" + inDegreeFiltered.size());
		return inDegreeFiltered;
	}

	protected void calculateNodeDegrees()
	{
		try
		{
			node2inDegree = graphFacade.getInDegrees(followersAndFriends, seedUsers);
		} catch (UserVertexNotPresentException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String inputSize = "NotSet";
		String inDegreeAbsThreshold = "CannotBeCalculated";
		if (seedUsers != null)
		{
			inputSize = Integer.toString(seedUsers.size());
			inDegreeAbsThreshold = Integer.toString(inDegreeAbsoluteThreshold);
		}
		return "InDegreeFilterManager" + " (inDegreePercentageThreshold=" + inDegreePercentageThreshold * 100 + "%" + " - inDegreeAbsoluteThreshold=" + inDegreeAbsThreshold + " - inputSize=" + inputSize + ")";
	}
}
