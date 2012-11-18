package it.cybion.influence.metrics;

import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.MapSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MetricsCalculator {

    //init variables
	private List<User> users = new ArrayList<User>();
	private List<Tweet> tweets = new ArrayList<Tweet>();
	private Map<User, Integer> users2tweetsCountAmongDataset = null;
	private boolean users2tweetsCountAmongDatasetIsSorted = false;
	private Map<User, Integer> users2tweetsCount = null;
	private boolean users2tweetsCountIsSorted = false;

    public MetricsCalculator(List<User> users, List<Tweet> tweets) {
        this.users = users;
        this.tweets = tweets;
    }


    /*
      *
      * METRICS
      *
      */
	
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
			accumulator = accumulator + ( (double)user.getFollowersCount() / user.getFriendsCount() );
		return accumulator/users.size();
	}
		

	/*
	 * This method calculates the number of tweet per user among
	 * the tweets in the dataset.
	 */
    //TODO i don't like that has side effects: it updates an internal variable that at the start is null
    //sometimes it's not ideal since in this way the user who calls it
    //should know that some variables change state after calling this method.
	public Map<User, Integer> getUsers2tweetsCountAmongDataset(List<Tweet> tweets) {
		this.users2tweetsCountAmongDataset = new HashMap<User, Integer>();
		for (Tweet tweet: tweets){
			User user = tweet.getUser();
			if (users2tweetsCountAmongDataset.containsKey(user)) {
				Integer tweetCount = users2tweetsCountAmongDataset.get(user);
				users2tweetsCountAmongDataset.put(user, tweetCount+1 );
			}
			else
				users2tweetsCountAmongDataset.put(user, 1 );
		}
		return users2tweetsCountAmongDataset;					
	}
	
	
	/*
	 * This method use users2tweetsCount map.
	 * If the map is null (not calculated yet) the method
	 * calls getUsers2tweetsCount() to calculate it.
	 */
    //TODO this could take a Map<User, Integer> as input parameter (so it's not dependent to getUsers2tweetsCountAmongDataset)
    //this way it could have less and more focused responsibilities
	public double getTweetsPerUserAmongDatasetAVG(List<Tweet> tweets) {
		double accumulator = 0;
        Map<User, Integer> users2tweetsCountAmongDataset = getUsers2tweetsCountAmongDataset(tweets);
        for (User user: users2tweetsCountAmongDataset.keySet()) {
			accumulator = accumulator + users2tweetsCountAmongDataset.get(user);
        }
		return accumulator / users2tweetsCountAmongDataset.size();
	}

    //always best to pass parameters than relying on internal variables:
    //what if the tweets were null?
	public Map<User, Integer> getUsers2tweetsCount() {
		users2tweetsCount = new HashMap<User, Integer>();
		for (Tweet tweet: tweets){
			User user = tweet.getUser();
			users2tweetsCount.put(user, user.getStatusesCount() );
		}
		return users2tweetsCount;					
	}
		
	public double getTweetsPerUserAVG(List<Tweet> tweets) {
		double accumulator = 0;
		if (this.users2tweetsCount == null)
			getUsers2tweetsCountAmongDataset(tweets);
		for (User user: users2tweetsCount.keySet())
			accumulator = accumulator + users2tweetsCount.get(user);
		return accumulator/users2tweetsCount.size();
	}
	
	
	/*
	 * Return this.users2tweetsCountAmongDataset ordered in descending order 
	 * (the most active is on top).
	 * If this.users2tweetsCount is null this.getUsers2tweetsCount() is called.
	 */
	/*
	 * TODO always best having parameters than relying on internal variables
	 */
	public Map<User, Integer> getMostActiveTwittersAmongDataset() {
		if (users2tweetsCountAmongDatasetIsSorted == true)
			return users2tweetsCountAmongDataset;
		else
			users2tweetsCountAmongDataset = 
				MapSorter.sortMapByValuesDescending(users2tweetsCountAmongDataset);
		users2tweetsCountAmongDatasetIsSorted = true;	
        return users2tweetsCountAmongDataset;
	}

    //you always "cache" results internally changing state
    //if anyone wants an ordered list, build it the first time like that or leave ordering to others
	public Map<User, Integer> getMostActiveTwitters() {
		if (users2tweetsCountIsSorted == true)
			return users2tweetsCount;
		else
			users2tweetsCount = 
				MapSorter.sortMapByValuesDescending(users2tweetsCount);
		users2tweetsCountIsSorted = true;
        return users2tweetsCount;
	}
	
	
	/*
	 * 
	 * GETTERS AND SETTERS
	 * 
	 */
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
