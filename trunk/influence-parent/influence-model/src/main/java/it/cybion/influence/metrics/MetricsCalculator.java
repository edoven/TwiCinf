package it.cybion.influence.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import it.cybion.influence.model.HashtagEntity;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.MapSorter;



public class MetricsCalculator {
	
	private MetricsReport report = null;
	 
	private List<Tweet> tweets; // = new ArrayList<Tweet>();
	
	
	//private boolean users2tweetsCountAmongDatasetIsSorted = false;	
	//private boolean users2tweetsCountIsSorted = false;

	
	/*
	 * 
	 * CONSTRUCTOR
	 * 
	 */

	public MetricsCalculator(List<Tweet> tweets)
	{
		this.tweets = tweets;
	}
	
	
	
	/*
	 * Public methods
	 */
	
	public MetricsReport getReport() 
	{
		if (this.report==null)
			createReport();
		return this.report;		
	}
	
	
	
	
	/*
	 * 
	 * CALCULATE INFOS
	 * 
	 */

	
	private void createReport()  
	{
		int tweetsCount;
		int usersCount;
		double followersCountAVG;
		double friendsCountAVG;
		double followerFriendsRatioAVG;
		
		List<User> users = new ArrayList<User>();
		Map<String, Integer> users2tweetsCountAmongDataset = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> users2tweetsCount = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> hashtags2count = new HashMap<String, Integer>();
		
		
		
		
		/*
		 * ===============
		 * Data calulation (batch process...beware the order of the operations)
		 * ===============
		 */
		

		for (Tweet tweet: tweets)
		{
			User user = tweet.getUser();	
			
			updateUsers(users, user);			
			updateUsers2tweetsCountAmongDataset(users2tweetsCountAmongDataset, user); 
			updateHashtags2count(hashtags2count, tweet.getHashtagEntities());				
		}
		
		calculateUsers2tweetsCount(users, users2tweetsCount);	
		
		
		tweetsCount = tweets.size();
		usersCount = users.size();
		followersCountAVG = calculateFollowersCountAVG(users);
		friendsCountAVG = calculateFriendsCountAVG(users);
		followerFriendsRatioAVG = calculateFollowerFriendsRatioAVG(users);

		
		/*
		 * maps sorting
		 */
		users2tweetsCountAmongDataset = MapSorter.sortMapByValuesDescending(users2tweetsCountAmongDataset);
		users2tweetsCount = MapSorter.sortMapByValuesDescending(users2tweetsCount);
		hashtags2count = MapSorter.sortMapByValuesDescending(hashtags2count);
		
		
		
		
		/*
		 * Report creation
		 */
		this.report = new MetricsReport();
		report.setTweetsCount(tweetsCount);
		report.setUsersCount(usersCount);
		report.setFollowersCountAVG(followersCountAVG);
		report.setFriendsCountAVG(friendsCountAVG);
		report.setFollowerFriendsRatioAVG(followerFriendsRatioAVG);
		
		
		
		report.setUsers2tweetsCountAmongDataset(users2tweetsCountAmongDataset);		
		report.setUsers2tweetsCount(users2tweetsCount);
		report.setHashtags2count(hashtags2count);
		
		
	}

	
	
	
	private void updateUsers(List<User> users, User user)
	{
		if (!users.contains(user))
			users.add(user);
	}
	
	
	private void updateUsers2tweetsCountAmongDataset(Map<String, Integer> users2tweetsCountAmongDataset, User user)
	{
		if (!users2tweetsCountAmongDataset.containsKey(user.getScreenName()))
			users2tweetsCountAmongDataset.put(user.getScreenName(), 1);
		else {
			int tweetCount = users2tweetsCountAmongDataset.get(user.getScreenName());
			users2tweetsCountAmongDataset.put(user.getScreenName(), (tweetCount+1) );
		}
	}
	
	
	private void updateHashtags2count(Map<String, Integer> hashtags2count, List<HashtagEntity> hashtagEntities)
	{
		for (HashtagEntity hashtagEntity: hashtagEntities)
		{
			String hashtag = hashtagEntity.getText();
			if (hashtags2count.containsKey(hashtag))
				hashtags2count.put(hashtag, (hashtags2count.get(hashtag)+1) );
			else
				hashtags2count.put(hashtag, 1 );
		}
	}
	
	
	private void calculateUsers2tweetsCount(List<User> users, Map<String, Integer> users2tweetsCount)
	{
		for (User user: users)
			users2tweetsCount.put(user.getScreenName(), user.getStatusesCount() );
	}
	
	
	private double calculateFollowersCountAVG(List<User> users) {
		double accumulator = 0;
		for (User user: users)
			accumulator = accumulator + user.getFollowersCount();
		return accumulator/users.size();
	}
	
	
	private double calculateFriendsCountAVG(List<User> users) {
		double accumulator = 0;
		for (User user: users)
			accumulator = accumulator + user.getFriendsCount();
		return accumulator/users.size();
	}
		
	
	private double calculateFollowerFriendsRatioAVG(List<User> users) {
		double accumulator = 0;
		int countedUsers = 0;
		for (User user: users) {
			if (user.getFriendsCount()>0)
			accumulator = accumulator + ( user.getFollowersCount() / user.getFriendsCount() );
			countedUsers++;
		}
		return accumulator/countedUsers;
	}
	
	
	
	
	
	/*
	private void enrichUsersWithHashtags()
	{
		
		List<HashtagEntity> tweetHashtagEntities;
		for (Tweet tweet: this.tweets) 
		{
			tweetHashtagEntities = tweet.getHashtagEntities();
			if (tweetHashtagEntities.size() > 0) //it does the hashtag extraction/addition only if the tweet contains some hashtags
			{
				User twitterAuthor = tweet.getUser();
				this.users.remove(twitterAuthor);
				for (HashtagEntity hashtagEntity: tweetHashtagEntities) {
					twitterAuthor.addHashtag(hashtagEntity.getText());
				}
				
				this.users.add(twitterAuthor);
			}
		}
	}
	*/
}
