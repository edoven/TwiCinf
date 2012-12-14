package it.cybion.influencers.filter.managers.topology;


public class NodeDegreeFilterManager {
	
	private DegreeDirection degreeDirection;
	private ComparisonOption filterComparison;
	private double threshold;
	
	public NodeDegreeFilterManager(DegreeDirection degreeDirection,
						 ComparisonOption filterComparison, 
						 double threshold) {
		super();
		this.degreeDirection = degreeDirection;
		this.filterComparison = filterComparison;
		this.threshold = threshold;
	}
	
}
