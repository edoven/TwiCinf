package runnable.userscreator.test;

import twitter4j.TwitterException;

public class UserEnricherTEST {
	
	static long TEST_ID = 302634823;

	public static void main(String[] args) throws TwitterException
	{
		id2followersTEST();
		id2friendsTEST();
	}
	
	public static void id2friendsTEST() throws TwitterException
	{
		long ids[] = runnable.userscreator.UserEnrichener.id2friends(TEST_ID);
		for (int i=0; i<ids.length; i++)
			System.out.println(i+"-"+ids[i]);
	}

	public static void id2followersTEST() throws TwitterException
	{
		long ids[] = runnable.userscreator.UserEnrichener.id2followers(TEST_ID);
		for (int i=0; i<ids.length; i++)
			System.out.println(i+"-"+ids[i]);
	}
	
}
