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

    public RankedUser() {}
	
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
		if (this.rank>toCompare.rank)
			return 1;
        if (this.rank < toCompare.rank)
            return -1;
		else
			return 0;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RankedUser that = (RankedUser) o;

        if (followersCount != that.followersCount) {
            return false;
        }
        if (Double.compare(that.meanRetweetsCount, meanRetweetsCount) != 0) {
            return false;
        }
        if (originalTweets != that.originalTweets) {
            return false;
        }
        if (Double.compare(that.rank, rank) != 0) {
            return false;
        }
        if (Double.compare(that.topicTweetsCount, topicTweetsCount) != 0) {
            return false;
        }
        if (Double.compare(that.topicTweetsRatio, topicTweetsRatio) != 0) {
            return false;
        }
        if (screenName != null ? !screenName.equals(that.screenName) : that.screenName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result;
        long temp;
        result = screenName != null ? screenName.hashCode() : 0;
        result = 31 * result + followersCount;
        result = 31 * result + originalTweets;
        temp = Double.doubleToLongBits(topicTweetsCount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(topicTweetsRatio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(meanRetweetsCount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rank);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
