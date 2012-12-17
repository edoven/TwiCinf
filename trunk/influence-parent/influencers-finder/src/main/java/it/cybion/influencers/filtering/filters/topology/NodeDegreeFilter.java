package it.cybion.influencers.filtering.filters.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.cybion.influencers.filtering.filters.Filter;
import it.cybion.influencers.filtering.managers.topology.ComparisonOption;

public class NodeDegreeFilter implements Filter {

	
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
		for (Entry<Long,Integer> mapEntry : node2degree.entrySet()) {
			switch (comparisonOption) {
				case EQUAL:
					if (mapEntry.getValue() == threshold)
						goodNodes.add(mapEntry.getKey());
				case GREATER:
					if (mapEntry.getValue() > threshold)
						goodNodes.add(mapEntry.getKey());
				case GREATER_OR_EQUAL:
					if (mapEntry.getValue() >= threshold)
						goodNodes.add(mapEntry.getKey());
				case SMALLER:
					if (mapEntry.getValue() < threshold)
						goodNodes.add(mapEntry.getKey());
				case SMALLER_OR_EQUAL:
					if (mapEntry.getValue() <= threshold)
						goodNodes.add(mapEntry.getKey());			
			}
		}
		return goodNodes;
	}
	
}
