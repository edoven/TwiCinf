package it.cybion.influencers.filtering.filters.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import it.cybion.influencers.filtering.filters.Filter;
import it.cybion.influencers.filtering.managers.topology.ComparisonOption;


public class NodeDegreeFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger(NodeDegreeFilter.class);

	
	private Map<Long, Integer> node2degree;
	private int threshold;
	private ComparisonOption comparisonOption;


	public NodeDegreeFilter(Map<Long, Integer> node2degree, int threshold,
			ComparisonOption comparisonOption) {
		super();
		this.node2degree = node2degree;
		this.threshold = threshold;
		this.comparisonOption = comparisonOption;
	}


	@Override
	public List<Long> filter() {
		List<Long> goodNodes = new ArrayList<Long>();	
		switch (comparisonOption) {
			case EQUAL: {
				for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
					if (mapEntry.getValue() == threshold)
						goodNodes.add(mapEntry.getKey());
				}
				break;
			}
			case GREATER: {
				for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
					//logger.info(mapEntry.getKey()+"-"+mapEntry.getValue());
					if (mapEntry.getValue() > threshold)
						goodNodes.add(mapEntry.getKey());
				}					
				break;
			}
			case GREATER_OR_EQUAL: {
				for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
					//logger.info(mapEntry.getKey()+"-"+mapEntry.getValue());
					if (mapEntry.getValue() >= threshold)
						goodNodes.add(mapEntry.getKey());
				}
				break;
			}
			case SMALLER: {
				for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
					//logger.info(mapEntry.getKey()+"-"+mapEntry.getValue());
					if (mapEntry.getValue() < threshold)
						goodNodes.add(mapEntry.getKey());
				}
				break;
			}
			case SMALLER_OR_EQUAL: {
				for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
					//logger.info(mapEntry.getKey()+"-"+mapEntry.getValue());
					if (mapEntry.getValue() <= threshold)
						goodNodes.add(mapEntry.getKey());	
				}
				break;
			}
		}
		return goodNodes;
	}
	
}
