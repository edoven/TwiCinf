package it.cybion.influencers.crawler.filtering.topologybased;

import it.cybion.influencers.crawler.graph.exceptions.UserVertexNotPresentException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;



public class InOrOutDegreeFilterManager extends DegreeFilterManager
{

	private static final Logger logger = Logger.getLogger(InOrOutDegreeFilterManager.class);

	// private List<Long> seedUsers;
	// private TwitterFacade twitterFacade;
	// private GraphFacade graphFacade;
	private float inDegreePercentageThreshold;
	private int inDegreeAbsoluteThreshold;
	private float outDegreePercentageThreshold;
	private int outDegreeAbsoluteThreshold;
	// private List<Long> followersAndFriends;
	// private List<User> enrichedSeedUsers;
	private Map<Long, Integer> node2inDegree;
	private Map<Long, Integer> node2outDegree;

	class User implements Comparable<User>
	{
		private long id;
		private List<Long> followers;
		private List<Long> friends;

		public User(long id, List<Long> followers, List<Long> friends)
		{
			this.id = id;
			this.followers = followers;
			this.friends = friends;
		}

		public long getId()
		{
			return id;
		}

		public List<Long> getFollowers()
		{
			return followers;
		}

		public List<Long> getFriends()
		{
			return friends;
		}

		@Override
		public int compareTo(User userToCompare)
		{
			return userToCompare.getFollowers().size() - this.getFollowers().size();
		}

	};

	public InOrOutDegreeFilterManager(float inDegreePercentageThreshold, float outDegreePercentageThreshold)
	{
		this.inDegreePercentageThreshold = inDegreePercentageThreshold;
		this.outDegreePercentageThreshold = outDegreePercentageThreshold;
	}

	@Override
	public List<Long> filter()
	{
		solveDependencies();
		NodeDegreeFilter inDegreeFilter = new NodeDegreeFilter(node2inDegree, inDegreeAbsoluteThreshold);
		List<Long> inDegreeFiltered = inDegreeFilter.filter();
		logger.info("inDegreeFiltered.size()=" + inDegreeFiltered.size());
		NodeDegreeFilter outDegreeFilter = new NodeDegreeFilter(node2outDegree, outDegreeAbsoluteThreshold);
		List<Long> outDegreeFiltered = outDegreeFilter.filter();
		logger.info("outDegreeFiltered.size()=" + outDegreeFiltered.size());
		List<Long> inOrOutDegreeFiltered = putListsInOr(inDegreeFiltered, outDegreeFiltered);
		logger.info("inAndOutDegreeFiltered.size()=" + inOrOutDegreeFiltered.size());
		return inOrOutDegreeFiltered;
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

	protected void calculateNodeDegrees()
	{
		try
		{
			// this sets an inDegree label in the graph for each node of
			// followersAndFriends set
			node2inDegree = graphFacade.getInDegrees(followersAndFriends, seedUsers);
			// this sets an outDegree label in the graph for each node of
			// followersAndFriends set
			node2outDegree = graphFacade.getOutDegrees(followersAndFriends, seedUsers);
		} catch (UserVertexNotPresentException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	private List<Long> putListsInOr(List<Long> listA, List<Long> listB)
	{
		List<Long> orList = listA;
		orList.addAll(listB);
		orList = new ArrayList<Long>(new HashSet<Long>(orList));
		return orList;
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
		return "InOrOutDegreeFilterManager" + " (inDegreePercentageThreshold=" + inDegreePercentageThreshold * 100 + "%" + " - outDegreePercentageThreshold=" + outDegreePercentageThreshold * 100 + "%" + " - inDegreeAbsoluteThreshold="
				+ inDegreeAbsThreshold + " - outDegreeAbsoluteThreshold=" + outDegreeAbsThreshold + " - inputSize=" + inputSize + ")";
	}
}
