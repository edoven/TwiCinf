package it.cybion.influence.ranking.model;


public class Tweet
{
	public String text;
	public String urlsExpandedText;
	public Entities entities;	
	public long id;
	public int retweet_count;
	public Tweet retweeted_status;	
	public User user;
}
