package it.cybion.influence.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import it.cybion.influence.model.HashtagEntity;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.MapSorter;

/*
 * To add a new report field:
 * 
 * 1-add the new variable in MetricsReport and generate its getter and setter
 * 2-add the new variable in createReport() (section "variables")
 * 3-add the operation to calculate the new variable in createReport() (section "data calulation")
 * 4-set the report field in createReport() (section "report creation")
 * 
 * DONE!
 * 
 * 5-test it in MetricsCalculatorTestCase class
 */





public class MetricsCalculator {
	
	private MetricsReport report = null;	 
	private List<Tweet> tweets;


	
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
	 * 
	 * Public methods
	 * 
	 */
	
	public MetricsReport getReport() 
	{
		if (this.report==null)
			createReport();
		return this.report;		
	}
	
	/*
	 * 
	 * Private methods
	 * 
	 */
	
	

	private void createReport()  
	{
		/*
		 * Variables
		 */
		int tweetsCount = 0;
		int usersCount = 0;
		double followersCountAVG = 0;
		double friendsCountAVG = 0;
		double followerFriendsRatioAVG = 0;
		
		List<User> users = new ArrayList<User>();
		Map<String, Integer> users2tweetsCountAmongDataset = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> users2tweetsCount = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> hashtags2count = new HashMap<String, Integer>();
		
		int retweetsCount = 0;
		
		
		
		
		/*
		 * ===============
		 * Data calulation (batch process...beware the order of the operations)
		 * ===============
		 */
		
		/*
		 * this cycle builds:
		 * 1-the users list
		 * 2-the users2tweetsCountAmongDataset map<String, Integer> (String = user.getScreenName())
		 * 3-the hashtags2count map<String, Integer> (String = hashtag.getText())
		 */
		for (Tweet tweet: tweets)
		{
			User user = tweet.getUser();	
			
			users = updateUsers(users, user);			
			users2tweetsCountAmongDataset = updateUsers2tweetsCountAmongDataset(users2tweetsCountAmongDataset, user); 
			hashtags2count = updateHashtags2count(hashtags2count, tweet.getHashtagEntities());	
			retweetsCount = updateRetweetsCount(retweetsCount, tweet);
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
		 * ===============
		 * Report creation
		 * ===============
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
		report.setRetweetsCount(retweetsCount);
		
		
	}

	
	
	
	private List<User> updateUsers(List<User> users, User user)
	{
		if (!users.contains(user))
		{
			
			List<User> updatedUsers = users;
			updatedUsers.add(user);
			return updatedUsers;
		}
		else
			return users;
	}
	
	
	private Map<String, Integer> updateUsers2tweetsCountAmongDataset(Map<String, Integer> users2tweetsCountAmongDataset, User user)
	{
		Map<String, Integer> updatedMap = users2tweetsCountAmongDataset;
		if (!updatedMap.containsKey(user.getScreenName()))
			updatedMap.put(user.getScreenName(), 1);
		else {
			int tweetCount = updatedMap.get(user.getScreenName());
			updatedMap.put(user.getScreenName(), (tweetCount+1) );
		}
		return updatedMap;
	}
	
	
	private Map<String, Integer> updateHashtags2count(Map<String, Integer> hashtags2count, List<HashtagEntity> hashtagEntities)
	{
		Map<String, Integer> updatedMap = hashtags2count;
		for (HashtagEntity hashtagEntity: hashtagEntities)
		{
			String hashtag = hashtagEntity.getText();
			if (updatedMap.containsKey(hashtag))
				updatedMap.put(hashtag, (updatedMap.get(hashtag)+1) );
			else
				updatedMap.put(hashtag, 1 );
		}
		return updatedMap;
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
		
	
	/*
	 * BEWARE: this skips the users with friendsCount==0 
	 * to avoid division by 0
	 */
	private double calculateFollowerFriendsRatioAVG(List<User> users) {
		double accumulator = 0.0;
		int countedUsers = 0;
		for (User user: users) {
			if (user.getFriendsCount()>0) {
				accumulator = accumulator + ( (double)user.getFollowersCount() / user.getFriendsCount() );
				countedUsers++;
			}
		}
		
		if (countedUsers==0)
			return 0;
		else
			return accumulator/countedUsers;
	}
	
	
	private int updateRetweetsCount(int retweetsCount, Tweet tweet) {
		if (tweet.getRetweetedStatus()!=null)
			return retweetsCount+1;
		else
			return retweetsCount;
			
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
