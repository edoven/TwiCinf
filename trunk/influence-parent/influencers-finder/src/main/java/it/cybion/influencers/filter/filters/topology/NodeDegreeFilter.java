package it.cybion.influencers.filter.filters.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.cybion.influencers.filter.filters.Filter;
import it.cybion.influencers.filter.managers.topology.ComparisonOption;

public class NodeDegreeFilter implements Filter {

	
	private Map<String, Integer> node2degree;
	private int threshold;
	private ComparisonOption comparisonOption;


	public NodeDegreeFilter(Map<String, Integer> node2degree, int threshold,
			ComparisonOption comparisonOption) {
		super();
		this.node2degree = node2degree;
		this.threshold = threshold;
		this.comparisonOption = comparisonOption;
	}


	@Override
	public List<String> filter() {
		List<String> goodNodes = new ArrayList<String>();
		for (Entry<String,Integer> mapEntry : node2degree.entrySet()) {
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
