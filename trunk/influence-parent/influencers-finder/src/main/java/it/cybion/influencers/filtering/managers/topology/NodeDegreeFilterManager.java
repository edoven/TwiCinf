package it.cybion.influencers.filtering.managers.topology;

import java.util.List;

import it.cybion.influencers.filtering.managers.FilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;


public class NodeDegreeFilterManager implements FilterManager{
	
	private DegreeDirection degreeDirection;
	private ComparisonOption filterComparison;
	private double threshold;
	private TwitterFacade twitterFacade;
	private GraphFacade graphFacade;
	
	public NodeDegreeFilterManager(DegreeDirection degreeDirection,
						 ComparisonOption filterComparison, 
						 double threshold) {
		super();
		this.degreeDirection = degreeDirection;
		this.filterComparison = filterComparison;
		this.threshold = threshold;
	}

	@Override
	public List<Long> filter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTwitterFacade(TwitterFacade twitterFacade) {
		this.twitterFacade = twitterFacade;	
	}

	@Override
	public void setGraphFacade(GraphFacade graphFacade) {
		this.graphFacade = graphFacade;
	}

	@Override
	public void setUsers(List<Long> users) {
		// TODO Auto-generated method stub
		
	}
	
}
