package it.cybion.influencers.rank.laperla;

import java.net.UnknownHostException;

import it.cybion.influencers.cache.TwitterCache;

public class TwitterFacadeFactoryTEST
{
	public static void main(String[] args) throws UnknownHostException, InterruptedException
	{
		TwitterCache twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		Thread.sleep(1000*20);
		System.exit(0);
	}
}
