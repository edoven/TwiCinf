package it.cybion.influence.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.cybion.influence.model.HashtagEntity;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.model.UserMentionEntity;
import it.cybion.influence.util.MapSorter;

/*
 * TODO: test it!
 */
public class MetricsCalculator {
	
	
	private static final Logger logger = Logger.getLogger(MetricsCalculator.class);
	
	private List<Tweet> tweets;

	
	public MetricsCalculator(List<Tweet> tweets){
		this.tweets = tweets;
	}

	public MetricsReport getReport() {
		MetricsReport report = new MetricsReport();
		
		logger.info("calculating getUsers");
		report.setUsers(getUsers());
		
		logger.info("calculating getUserCount");
		report.setUsersCount(getUserCount());
		
		logger.info("calculating getFollowersCountAVG");
		report.setFollowersCountAVG(getFollowersCountAVG());
		
		logger.info("calculating getFriendsCountAVG");
		report.setFriendsCountAVG(getFriendsCountAVG());
		
		logger.info("calculating getFollowersFriendsRatioAVG");
		report.setFollowersFriendsRatioAVG(getFollowersFriendsRatioAVG());
		
		logger.info("calculating getUsers2tweetsCountAmongDataset");
		report.setUsersTotweetsCountAmongDataset(getUsersTotweetsCountAmongDataset());	
		
		logger.info("calculating getUsers2tweetsCount");
		report.setUsersTotweetsCount(getUsersTotweetsCount());
		
		logger.info("calculating getHashtags2count");
		report.setHashtagsTocount(getHashtagsTocount());
		
		logger.info("calculating getRetweetsCount");
		report.setRetweetsCount(getRetweetsCount());
		
		logger.info("calculating getUsers2tweetsCountAVG");
		report.setUsers2tweetsCountAVG(getUsersTotweetsCountAVG());
		
		logger.info("calculating getUserMentioned2count");
		report.setUserMentionedTocount(getUserMentionedTocount());

		return report;
	}
	

	public List<User> getUsers() {
		HashSet<User> users = new HashSet<User>();
		for (Tweet tweet: tweets)
			users.add(tweet.getUser());
		return new ArrayList<User>(users);
	}
	
	public int getUserCount() {
		return getUsers().size();
	}
	
	public double getFollowersCountAVG() {
		double accumulator = 0;
		List<User> users = getUsers();
		for (User user: users) {
			accumulator = accumulator + user.getFollowersCount();
		}
		return accumulator/users.size();
	}
	
	public double getFriendsCountAVG() {
		double accumulator = 0;
		List<User> users = getUsers();
		for (User user: users) {
			accumulator = accumulator + user.getFriendsCount();
		}
		return accumulator/users.size();
	}
	
	public double getFollowersFriendsRatioAVG() {
		double accumulator = 0;
		List<User> users = getUsers();
		for (User user: users) {
			double currentFollowersFriendsRatio = (double)user.getFollowersCount()/user.getFriendsCount();
			accumulator = accumulator + currentFollowersFriendsRatio;
		}
		return accumulator/users.size();
	}
	
	/*
	 * The map returned (and all other maps of this class) contains the user.getScreenName() as key.
	 * Another option is to put the entire User.
	 * Which one is better? TODO: think about it!
	 */
	public Map<String,Integer> getUsersTotweetsCountAmongDataset() {
		Map<String,Integer> users2tweetsCountAmongDataset = new HashMap<String,Integer>();
		for (Tweet tweet: tweets) {
			User user = tweet.getUser();
			String screenName = user.getScreenName();
			if (users2tweetsCountAmongDataset.containsKey(screenName)) {
				int value = users2tweetsCountAmongDataset.get(screenName);
				users2tweetsCountAmongDataset.put(screenName, (value+1) );
			}
			else
				users2tweetsCountAmongDataset.put(screenName, 1 );
		}
		return MapSorter.sortMapByValuesDescending(users2tweetsCountAmongDataset);
	}
	

	public Map<String,Integer> getUsersTotweetsCount() {
		Map<String,Integer> users2tweetsCount = new HashMap<String,Integer>();
		List<User> users = getUsers();
		for (User user: users)
			users2tweetsCount.put(user.getScreenName(), user.getStatusesCount() );
		return MapSorter.sortMapByValuesDescending(users2tweetsCount);
	}
	
	
	public Map<String,Integer> getHashtagsTocount() {
		Map<String,Integer> hashtags2count = new HashMap<String,Integer>();
		for (Tweet tweet: tweets) {
			for (HashtagEntity hashtagEntity : tweet.getHashtagEntities()) {
				String hashTag = hashtagEntity.getText();
				if (hashtags2count.containsKey(hashTag)) {
					int value = hashtags2count.get(hashTag);
					hashtags2count.put(hashTag, (value+1) );
				}
				else
					hashtags2count.put(hashTag, 1 );
			}	
		}
		return MapSorter.sortMapByValuesDescending(hashtags2count);
	}
	
	
	public int getRetweetsCount() {
		int count = 0;
		for (Tweet tweet: tweets) {
			if (tweet.getRetweetedStatus()!=null)
				count++;
		}
		return count;
	}
	
	public double getUsersTotweetsCountAVG() {
		double accumulator = 0;
		Map<String,Integer> users2tweetsCount = getUsersTotweetsCount();
		for (String screenName : users2tweetsCount.keySet())
			accumulator = accumulator + users2tweetsCount.get(screenName);
		return accumulator / users2tweetsCount.size();
	}
	

	public Map<String, Integer> getUserMentionedTocount() {
		Map<String, Integer> userMentioned2count = new HashMap<String,Integer>();
		for (Tweet tweet: tweets) {
			for (UserMentionEntity userMentionEntioty : tweet.getUserMentionEntities()) {
				String user = userMentionEntioty.getName();
				if (userMentioned2count.containsKey(user)) {
					int value = userMentioned2count.get(user);
					userMentioned2count.put(user, (value+1) );
				}
				else
					userMentioned2count.put(user, 1);
			}	
		}
		return MapSorter.sortMapByValuesDescending(userMentioned2count);
	}
}
