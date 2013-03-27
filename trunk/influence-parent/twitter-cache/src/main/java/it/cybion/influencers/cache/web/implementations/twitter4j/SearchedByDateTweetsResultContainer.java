package it.cybion.influencers.cache.web.implementations.twitter4j;

import java.util.List;

public class SearchedByDateTweetsResultContainer
{
	private List<String> goodTweets;
	private List<String> badTweets;
	
	public SearchedByDateTweetsResultContainer(List<String> goodTweets,
			List<String> badTweets)
	{
		this.goodTweets = goodTweets;
		this.badTweets = badTweets;
	}
	
	public List<String> getGoodTweets()
	{
		return goodTweets;
	}
	public void setGoodTweets(List<String> goodTweets)
	{
		this.goodTweets = goodTweets;
	}
	public List<String> getBadTweets()
	{
		return badTweets;
	}
	public void setBadTweets(List<String> badTweets)
	{
		this.badTweets = badTweets;
	}	
}
