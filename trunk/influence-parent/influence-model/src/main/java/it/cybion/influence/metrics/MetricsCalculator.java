package it.cybion.influence.metrics;

import it.cybion.influence.model.HashtagEntity;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.model.UserMentionEntity;
import it.cybion.influence.util.MapSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 6-print it in MetricsResultPrinter class
 */


public class MetricsCalculator {
	
	private MetricsReport report = null;	 
	private List<Tweet> tweets;

	public MetricsCalculator(List<Tweet> tweets)
	{
		this.tweets = tweets;
	}

	public MetricsReport getReport() 
	{
		if (this.report==null)
			createReport();
		return this.report;		
	}

	private void createReport()  
	{

		int tweetsCount = 0;
		int usersCount = 0;
		double followersCountAVG = 0;
		double friendsCountAVG = 0;
		double followerFriendsRatioAVG = 0;
		double users2tweetsCountAVG = 0;
		
		List<User> users = new ArrayList<User>();
		Map<String, Integer> users2tweetsCountAmongDataset = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> users2tweetsCount = new HashMap<String, Integer>(); //Map<ScreenName, tweetsCount>
		Map<String, Integer> hashtags2count = new HashMap<String, Integer>();
		Map<String, Integer> usersMentioned2count = new HashMap<String, Integer>();
		
		int retweetsCount = 0;

		/*
		 * ===============
		 * Data calulation (batch process...beware the order of the operations)
		 * ===============
		 */
        /* TODO thinking about this, it is bad to have a monolithic method to "generate ALL reports":
        * better to have N methods, each one responsible to calculate its own metric.
        * Why should I have to wait for the overall method to finish,
        * even if I was only interested in one specific report?
        * bad side-effect: how do i know that every private method is tested?
        * the only way in which i can run that code is to call the public ones. */

 		for (Tweet tweet: tweets)
		{
			User currentUser = tweet.getUser();

            //TODO if the design is like this, using all private methods,
            //why not updating instance variables using void methods?
            //in the end, private methods are not testable just like void methods
			users = updateUsers(users, currentUser);
			users2tweetsCountAmongDataset = updateUsers2tweetsCountAmongDataset(users2tweetsCountAmongDataset, currentUser);
			hashtags2count = updateHashtags2count(hashtags2count, tweet);	
			retweetsCount = updateRetweetsCount(retweetsCount, tweet);
			usersMentioned2count = updateUsersMentioned2count(usersMentioned2count, tweet);
		}
		
		users2tweetsCount = calculateUsers2tweetsCount(users);	
				
		tweetsCount = tweets.size();
		usersCount = users.size();
		followersCountAVG = calculateFollowersCountAVG(users);
		friendsCountAVG = calculateFriendsCountAVG(users);
		followerFriendsRatioAVG = calculateFollowerFriendsRatioAVG(users);
		users2tweetsCountAVG = calculateUsers2tweetsCountAVG(users2tweetsCount);
		
		/*
		 * maps sorting
		 * TODO find and use (during the calculation) proper data structures that keep items
		 * in order and remove these lines
		 */
		users2tweetsCountAmongDataset = MapSorter.sortMapByValuesDescending(users2tweetsCountAmongDataset);
		users2tweetsCount = MapSorter.sortMapByValuesDescending(users2tweetsCount);
		hashtags2count = MapSorter.sortMapByValuesDescending(hashtags2count);
		usersMentioned2count = MapSorter.sortMapByValuesDescending(usersMentioned2count);
		
		/*
		 * ===============
		 * Report creation
		 * ===============
		 */
        /* TODO with this choice, it's easy to build an object and forget about setting one field.
         * what if an item is null and someone gets it?
         * better to set all values with a simple constructor (usually no more than 3-4 params).
         * in this case an overall refactoring is needed. until then, keep it like this.
         *  */
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
		report.setUsers2tweetsCountAVG(users2tweetsCountAVG);
		report.setUserMentioned2count(usersMentioned2count);
	}

	private List<User> updateUsers(List<User> users, User newUser)
	{
		if (!users.contains(newUser))
		{
			List<User> updatedUsers = users;
			updatedUsers.add(newUser);
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
	
	private Map<String, Integer> updateHashtags2count(Map<String, Integer> hashtags2count, Tweet tweet)
	{
		List<HashtagEntity> hashtagEntities = tweet.getHashtagEntities();
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

	private Map<String, Integer> calculateUsers2tweetsCount(List<User> users)
	{
		Map<String, Integer> users2tweetsCount = new HashMap<String,Integer>();
		for (User user: users)
			users2tweetsCount.put(user.getScreenName(), user.getStatusesCount() );
		return users2tweetsCount;
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
	why "beware"? it's correct!
	 * BEWARE: this skips the users with friendsCount==0 
	 * to avoid division by 0
	 */
	private double calculateFollowerFriendsRatioAVG(List<User> users) {
		double accumulator = 0.0;
		int countedUsers = 0;
		for (User user: users) {
			if (user.getFriendsCount()>0) {
                double currentFollowerFriendRatio = (double)user.getFollowersCount() / user.getFriendsCount();
				accumulator = accumulator + currentFollowerFriendRatio;
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
	
	private double calculateUsers2tweetsCountAVG(Map<String, Integer> users2tweetsCount) {
		double accumulator = 0;
		for (Integer count: users2tweetsCount.values())
			accumulator = accumulator + count;
		return accumulator/users2tweetsCount.size();
	}
	
	private Map<String, Integer> updateUsersMentioned2count(Map<String, Integer> userMentioned2count, Tweet tweet) {
		List<UserMentionEntity> userMentionEntities = tweet.getUserMentionEntities();
		if (userMentionEntities.size()==0)
			return userMentioned2count;
		
		Map<String, Integer> updatedMap = userMentioned2count;
		for (UserMentionEntity userMentionEntity: userMentionEntities) {
			String screenName = userMentionEntity.getScreenName();
			if (userMentioned2count.containsKey(screenName))
				updatedMap.put(screenName, (userMentioned2count.get(screenName)+1) );
			else
				updatedMap.put(screenName, 1 );
		}
		return updatedMap;
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
