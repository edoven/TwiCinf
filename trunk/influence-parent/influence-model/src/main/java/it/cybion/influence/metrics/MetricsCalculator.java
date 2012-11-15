package it.cybion.influence.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;



public class MetricsCalculator {
	 
	private List<User> users;
	private List<Tweet> tweets;
	private Map<User, Integer> users2tweetsCount = null;

	
	public double getFollowersCountAVG() {
		double accumulator = 0;
		for (User user: users)
			accumulator = accumulator + user.getFollowersCount();
		return accumulator/users.size();
	}
	
	public double getFriendsCountAVG() {
		double accumulator = 0;
		for (User user: users)
			accumulator = accumulator + user.getFriendsCount();
		return accumulator/users.size();
	}
	
	public double getFollowerFriendsRatioAVG() {
		double accumulator = 0;
		for (User user: users)
			accumulator = accumulator + ( user.getFollowersCount() / user.getFriendsCount() );
		return accumulator/users.size();
	}
	
		
	/*
	 * Return this.users2tweetsCount ordered in descending way.
	 * If this.users2tweetsCount is null this.getUsers2tweetsCount() is called.
	 */
	/*
	 * TODO
	 */
	public Map<User, Integer> getMostActiveTwitters() {
        return null;
	}
		
	
	public double getAVGTweetsPerUser() {
		double accumulator = 0;
		if (this.users2tweetsCount == null)
			getUsers2tweetsCount();
		for (User user: users2tweetsCount.keySet())
			accumulator = accumulator + users2tweetsCount.get(user);
		return accumulator/users2tweetsCount.size();
	}
	
	/*
	 * This function also sets this.users2tweetsCount
	 */
	public Map<User, Integer> getUsers2tweetsCount() {
		Map<User, Integer> users2tweetsCount = new HashMap<User, Integer>();
		for (Tweet tweet: tweets){
			User user = tweet.getUser();
			if (users2tweetsCount.containsKey(user)) {
				Integer tweetCount = users2tweetsCount.get(user);
				users2tweetsCount.put(user, tweetCount+1 );
			}
			else
				users2tweetsCount.put(user, 1 );
			
		}
		this.users2tweetsCount = users2tweetsCount;
		return users2tweetsCount;					
	}
	
	
	
	
	

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	
}
