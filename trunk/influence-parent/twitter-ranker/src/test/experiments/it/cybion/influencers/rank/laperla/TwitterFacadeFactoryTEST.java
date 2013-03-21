package it.cybion.influencers.rank.laperla;

import java.net.UnknownHostException;

import it.cybion.influencers.cache.TwitterFacade;

public class TwitterFacadeFactoryTEST
{
	public static void main(String[] args) throws UnknownHostException, InterruptedException
	{
		TwitterFacade twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		Thread.sleep(1000*20);
		System.exit(0);
	}
}
