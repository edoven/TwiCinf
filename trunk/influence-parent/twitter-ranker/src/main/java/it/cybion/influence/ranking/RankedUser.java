package it.cybion.influence.ranking;

public class RankedUser implements Comparable<RankedUser>
{
	private String screenName;
	private int followersCount;
	private int originalTweets;
	private double meanRetweetsCount;
	private double rank;
	
	
	public RankedUser(String screenName, int followersCount,
			int originalTweets, double meanRetweetsCount, double rank)
	{
		this.screenName = screenName;
		this.followersCount = followersCount;
		this.originalTweets = originalTweets;
		this.meanRetweetsCount = meanRetweetsCount;
		this.rank = rank;
	}
	
	@Override
	public int compareTo(RankedUser toCompare)
	{
		if (isNaN(this.rank))
			return -1;
		if (isNaN(toCompare.rank))
			return 1;
		if (this.rank>=toCompare.rank)
			return 1;
		else
			return -1;
	}
	
	private boolean isNaN(double x)
	{
		return x != x;
	}


	public String getScreenName()
	{
		return screenName;
	}


	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}


	public int getFollowersCount()
	{
		return followersCount;
	}


	public void setFollowersCount(int followersCount)
	{
		this.followersCount = followersCount;
	}


	public int getOriginalTweets()
	{
		return originalTweets;
	}


	public void setOriginalTweets(int originalTweets)
	{
		this.originalTweets = originalTweets;
	}


	public double getMeanRetweetsCount()
	{
		return meanRetweetsCount;
	}


	public void setMeanRetweetsCount(double meanRetweetsCount)
	{
		this.meanRetweetsCount = meanRetweetsCount;
	}


	public double getRank()
	{
		return rank;
	}


	public void setRank(double rank)
	{
		this.rank = rank;
	}




	
	

}
