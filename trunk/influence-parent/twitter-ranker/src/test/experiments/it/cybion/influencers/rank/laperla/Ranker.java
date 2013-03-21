package it.cybion.influencers.rank.laperla;


import it.cybion.influencers.cache.TwitterCache;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import twitter4j.TwitterException;


public class Ranker
{
	private static class Tweet
	{
		int retweet_count;
		Tweet retweeted_status;		
	}
	
	private static class User implements Comparable<User>
	{
		String screenName;
		int followersCount;
		int originalTweets;
		double meanRetweetsCount;
		int reach;
		double ranking;
		
		@Override
		public int compareTo(User user)
		{
			if (this.ranking >= user.ranking)
				return -1;
			else
				return 1;
		}
	
	}
	
	public static void main(String[] args) throws UnknownHostException
	{
		TwitterCache twitterFacade;
		twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> laPerla2800UserIds = ListFileReader.readLongListFile("/home/godzy/Desktop/laPerla2800UsersIds.txt");
		Collections.sort(laPerla2800UserIds);
		laPerla2800UserIds = laPerla2800UserIds.subList(0, 30);
		Gson gson = new Gson();
		List<User> users = new ArrayList<User>();
		
		int fromYear = 2013,
			fromMonth = 2,
			fromDay = 1;
		int toYear = 2013,
			toMonth = 3,
			toDay = 1;
		
		int usersCount = 0;
		List<String> jsonTweets;
		for (Long userId : laPerla2800UserIds)
		{
			try {
				jsonTweets = twitterFacade.getUpTo200Tweets(userId);
				jsonTweets = twitterFacade.getTweetsByDate(userId, fromYear, fromMonth, fromDay, toYear, toMonth, toDay);
				User user = new User();
				double accumulator = 0;
				int originalTweets = 0;
				for (String jsonTweet : jsonTweets)
				{
					Tweet tweet = gson.fromJson(jsonTweet, Tweet.class);
					if (tweet.retweeted_status==null)
					{
						originalTweets++;
						accumulator = accumulator + tweet.retweet_count;
					}
				}
				int followers = twitterFacade.getFollowersCount(userId);
				double meanRetweetsCount = accumulator/originalTweets;
				int reach = (int)Math.round( followers*meanRetweetsCount );
				double ranking = (meanRetweetsCount+meanRetweetsCount)/Math.log(followers);
				user.screenName = twitterFacade.getScreenName(userId);
				user.followersCount = followers;
				user.originalTweets = originalTweets;
				user.meanRetweetsCount = meanRetweetsCount;
				user.reach = reach;
				user.ranking = ranking;
				
				users.add(user);
				System.out.println((usersCount++)+")"+twitterFacade.getScreenName(userId)+" - "+meanRetweetsCount+" - "+originalTweets+" - "+followers+" - "+reach);		
			}
			catch (TwitterException e)
			{
				if(e.getStatusCode()==401)
					System.out.println("User with id "+userId+" is protected. Skipped.");
			}			
			
		}
		
		
		System.out.println("=====");
		Collections.sort(users);
		for (User user : users)
		{
			System.out.printf("%15s - %5.2f - %4d - %7d - %9d - %4.5f \n", 
								user.screenName, 
								user.meanRetweetsCount,
								user.originalTweets,
								user.followersCount,
								user.reach, 
								user.ranking);		
			
		}
		System.exit(0);
	}
}
