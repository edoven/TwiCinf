package it.cybion.influencers.ranking;

public class RankedUser implements Comparable<RankedUser>
{
	String screenName;
	int followersCount;
	int originalTweets;
	double topicTweetsCount; 
	double topicTweetsRatio; 
	double meanRetweetsCount; 
	double rank;

	
	public RankedUser(String screenName, 
					  int followersCount,
					  int originalTweets, 
					  double topicTweetsCount,
					  double topicTweetsRatio, 
					  double meanRetweetsCount, 
					  double rank)
	{
		super();
		this.screenName = screenName;
		this.followersCount = followersCount;
		this.originalTweets = originalTweets;
		this.topicTweetsCount = topicTweetsCount;
		this.topicTweetsRatio = topicTweetsRatio;
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


	public String toString()
	{
		return "user:"+this.screenName+
				" - followers:"+this.followersCount+
				" - originalTweets:"+this.originalTweets+
				" - topicTweetsCount:"+this.topicTweetsCount+
				" - topicTweetsRatio:"+this.topicTweetsRatio+
				" - meanRetweetsCount:"+this.meanRetweetsCount+
				" - rank:"+this.rank;
	}
	
	public String toCSV()
	{
		return this.screenName+","+
			   this.followersCount+","+
			   this.originalTweets+","+
			   this.topicTweetsCount+","+
			   this.topicTweetsRatio+","+
			   this.meanRetweetsCount;
	}

	public double getTopicTweetsRatio()
	{
		return topicTweetsRatio;
	}

	public void setTopicTweetsRatio(double topicTweetsRatio)
	{
		this.topicTweetsRatio = topicTweetsRatio;
	}

	public double getTopicTweetsCount()
	{
		return topicTweetsCount;
	}

	public void setTopicTweetsCount(double topicTweetsCount)
	{
		this.topicTweetsCount = topicTweetsCount;
	}
	

}
