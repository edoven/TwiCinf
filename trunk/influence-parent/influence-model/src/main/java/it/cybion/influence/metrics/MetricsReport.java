package it.cybion.influence.metrics;

import java.util.List;
import java.util.Map;

import it.cybion.influence.model.User;


public class MetricsReport {

	private int tweetsCount;
	private int usersCount;
	private double followersCountAVG;
	private double friendsCountAVG;
	private double followersFriendsRatioAVG;
	private int retweetsCount; //this is the number of tweets that are retweets
	private double users2tweetsCountAVG;	
	private List<User> users;
	private Map<String, Integer> usersTotweetsCountAmongDataset;
	private Map<String, Integer> usersTotweetsCount;
	private Map<String, Integer> hashtagsTocount;
	private Map<String, Integer> userMentionedTocount;

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
	public void setFollowersFriendsRatioAVG(double followersFriendsRatioAVG) {
		this.followersFriendsRatioAVG = followersFriendsRatioAVG;
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Map<String, Integer> getUsersTotweetsCountAmongDataset() {
		return usersTotweetsCountAmongDataset;
	}
	public void setUsersTotweetsCountAmongDataset(
			Map<String, Integer> usersTotweetsCountAmongDataset) {
		this.usersTotweetsCountAmongDataset = usersTotweetsCountAmongDataset;
	}
	public Map<String, Integer> getHashtagsTocount() {
		return hashtagsTocount;
	}
	public void setHashtagsTocount(Map<String, Integer> hashtagsTocount) {
		this.hashtagsTocount = hashtagsTocount;
	}
	public Map<String, Integer> getUsersTotweetsCount() {
		return usersTotweetsCount;
	}
	public void setUsersTotweetsCount(Map<String, Integer> usersTotweetsCount) {
		this.usersTotweetsCount = usersTotweetsCount;
	}
	public Map<String, Integer> getUserMentionedTocount() {
		return userMentionedTocount;
	}
	public void setUserMentionedTocount(Map<String, Integer> userMentionedTocount) {
		this.userMentionedTocount = userMentionedTocount;
	}		
}
