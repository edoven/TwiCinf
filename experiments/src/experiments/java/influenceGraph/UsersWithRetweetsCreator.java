package influenceGraph;

import it.cybion.influencers.twitter.TwitterFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.TwitterException;

import com.google.gson.Gson;

public class UsersWithRetweetsCreator {
	
	private class Tweet {
		User user;
		Tweet retweeted_status;	
		
		private class User {
			long id;
		}
	}
	
	

	public static List<User> getRetweetsEnrichenedUsers(TwitterFacade twitterFacade , List<Long> usersIds) {
		List<User> enrichedUsers = new ArrayList<User>();
		int count = 0;
		for (Long userId : usersIds) {
			System.out.println("getRetweetsStatusForUser for user "+(count++)+"/"+usersIds.size());
			try {
				Map<Long, Integer> users2Retweets = getRetweetsStatusForUser(twitterFacade, userId);
				User user = new User(userId, users2Retweets);
				enrichedUsers.add(user);
				
			} catch (TwitterException e) {
				continue;
			}
		}
		return enrichedUsers;
	}
	
	public static Map<Long, Integer> getRetweetsStatusForUser(TwitterFacade twitterFacade, Long userId) throws TwitterException {
		Map<Long, Integer> users2Retweets = new HashMap<Long,Integer>();
		List<String> tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
		List<Tweet> tweets = new ArrayList<Tweet>();
		Gson gson = new Gson();
		for (String tweetJson : tweetsJsons) {
			tweets.add(gson.fromJson(tweetJson, Tweet.class));
		}
		for (Tweet tweet : tweets) {
			if (tweet.retweeted_status!=null) {
				long userIdn = tweet.retweeted_status.user.id;
				if (users2Retweets.containsKey(userIdn))
					users2Retweets.put(userIdn, users2Retweets.get(userIdn));
				else
					users2Retweets.put(userIdn, 1);
			}	
		}
		return users2Retweets;
	}
	
}
