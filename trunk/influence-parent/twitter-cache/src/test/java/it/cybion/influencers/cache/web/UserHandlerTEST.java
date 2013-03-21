package it.cybion.influencers.cache.web;

import org.testng.annotations.Test;

import twitter4j.TwitterException;

public class UserHandlerTEST
{
	@Test
	public void setRequestType2limitTEST() throws TwitterException
	{
		Token appplicationToken = new Token("/home/godzy/tokens/consumerToken.properties");
		Token userToken = new Token("/home/godzy/tokens/token0.properties");
		UserHandler userHandler = new UserHandler(appplicationToken, userToken);
		for (int i=0; i<10; i++)
		{
			try
			{
				Thread.sleep(1000*5);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("setRequestType2limit for time "+i);
		}
	}
}
