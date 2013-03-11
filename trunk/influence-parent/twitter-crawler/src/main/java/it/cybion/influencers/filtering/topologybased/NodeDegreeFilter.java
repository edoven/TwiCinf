package it.cybion.influencers.filtering.topologybased;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import org.apache.log4j.Logger;

import it.cybion.influencers.filtering.Filter;



public class NodeDegreeFilter implements Filter
{

	// private static final Logger logger =
	// Logger.getLogger(NodeDegreeFilter.class);

	private Map<Long, Integer> node2degree;
	private int threshold;

	public NodeDegreeFilter(Map<Long, Integer> node2degree, int threshold)
	{
		this.node2degree = node2degree;
		this.threshold = threshold;
	}

	@Override
	public List<Long> filter()
	{
		List<Long> goodNodes = new ArrayList<Long>();
		for (Entry<Long, Integer> mapEntry : node2degree.entrySet())
		{
			if (mapEntry.getValue() >= threshold)
				goodNodes.add(mapEntry.getKey());
		}
		return goodNodes;
	}

}
