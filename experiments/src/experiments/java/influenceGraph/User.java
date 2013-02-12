package influenceGraph;

import java.util.Map;

public class User {
	long userId;
	Map<Long,Integer> users2Retweets;
	
	public User(long userId, Map<Long, Integer> users2Retweets) {
		this.userId = userId;
		this.users2Retweets = users2Retweets;
	}
	
	
}
