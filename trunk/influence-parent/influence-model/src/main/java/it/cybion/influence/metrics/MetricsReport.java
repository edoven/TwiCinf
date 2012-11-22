package it.cybion.influence.metrics;


import java.util.Map;

public class MetricsReport {

	/*
	 * Single value metrics
	 */
	private int tweetsCount;
	private int usersCount;
	private double followersCountAVG;
	private double friendsCountAVG;
	private double followersFriendsRatioAVG;
	private int retweetsCount; //this is the number of tweets that are retweets
	private double users2tweetsCountAVG;

	/*
	 * Maps
	 */
	private Map<String, Integer> users2tweetsCountAmongDataset;
	private Map<String, Integer> users2tweetsCount;
	private Map<String, Integer> hashtags2count;
	private Map<String, Integer> userMentioned2count;

	/*
	 * ==================
	 * Getters and Setters
	 * ==================
	 * 
	 */

	public int getTweetsCount() {
		return tweetsCount;
	}


	public void setTweetsCount(int tweetsCount) {
		this.tweetsCount = tweetsCount;
	}


	public int getUsersCount() {
		return usersCount;
	}


	public void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}


	public double getFollowersCountAVG() {
		return followersCountAVG;
	}


	public void setFollowersCountAVG(double followersCountAVG) {
		this.followersCountAVG = followersCountAVG;
	}


	public double getFriendsCountAVG() {
		return friendsCountAVG;
	}


	public void setFriendsCountAVG(double friendsCountAVG) {
		this.friendsCountAVG = friendsCountAVG;
	}


	public double getFollowersFriendsRatioAVG() {
		return followersFriendsRatioAVG;
	}


	public void setFollowerFriendsRatioAVG(double followerFriendsRatioAVG) {
		this.followersFriendsRatioAVG = followerFriendsRatioAVG;
	}


	public Map<String, Integer> getUsers2tweetsCountAmongDataset() {
		return users2tweetsCountAmongDataset;
	}


	public void setUsers2tweetsCountAmongDataset(
			Map<String, Integer> users2tweetsCountAmongDataset) {
		this.users2tweetsCountAmongDataset = users2tweetsCountAmongDataset;
	}


	public Map<String, Integer> getUsers2tweetsCount() {
		return users2tweetsCount;
	}


	public void setUsers2tweetsCount(Map<String, Integer> users2tweetsCount) {
		this.users2tweetsCount = users2tweetsCount;
	}


	public Map<String, Integer> getHashtags2count() {
		return hashtags2count;
	}


	public void setHashtags2count(Map<String, Integer> hashtags2count) {
		this.hashtags2count = hashtags2count;
	}


	public int getRetweetsCount() {
		return retweetsCount;
	}


	public void setRetweetsCount(int retweetsCount) {
		this.retweetsCount = retweetsCount;
	}


	public double getUsers2tweetsCountAVG() {
		return users2tweetsCountAVG;
	}


	public void setUsers2tweetsCountAVG(double users2tweetsCountAVG) {
		this.users2tweetsCountAVG = users2tweetsCountAVG;
	}


	public Map<String, Integer> getUserMentioned2count() {
		return userMentioned2count;
	}


	public void setUserMentioned2count(Map<String, Integer> userMentioned2count) {
		this.userMentioned2count = userMentioned2count;
	}

}
