package it.cybion.influence.metrics;

import it.cybion.influence.IO.MysqlPersistenceFacade;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JsonDeserializer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;


public class SpecialUsersPrinter {
	
	private static final Logger logger = Logger.getLogger(SpecialUsersPrinter.class);
	
	@Test
	public void printsInfoAboutUsersWithMoreThan5000FriendsOrFollowers(){
		List<String> jsons = new MysqlPersistenceFacade("localhost", 3306, "root", "qwerty", "twitter").getAllJsonTweets();
    	JsonDeserializer jd = new JsonDeserializer();
		List<Tweet> tweets = jd.deserializeJsonStringsToTweets(jsons);
		Set<String> authors = new HashSet<String>();
		int count=1;
		for (Tweet tweet : tweets) {
			User user = tweet.getUser();
			if (!authors.contains(user.getScreenName()) && (user.getFollowersCount()>=5000 || user.getFriendsCount()>=5000)) {
				logger.info((count++) + " - " + user.getScreenName() + "\t\t\t - " + user.getFollowersCount() + "\t\t - " + user.getFriendsCount());
				authors.add(user.getScreenName());
			}
		}
		
	}
}
