package it.cybion.influence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.influence.model.Tweet;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/*
 * 
 * This test is about the dataset of trenitalia/etc 
 * in my own MySQL server
 * 
 */

public class MysqlPersistenceFacadeTestCase {	
	
	@BeforeClass
	public void setup() throws IOException {
	}
	
    @AfterClass
	public void shutdown() {
	}
	  
    @Test
    public void testIfTheExctractedDatasetIsCorrect() {
    	Gson gson = new GsonBuilder().create();
    	List<String> tweets = MysqlPersistenceFacade.getAllJsonTweets();    	
    	Tweet firstTweet = gson.fromJson(tweets.get(0), Tweet.class);
    	Tweet lastTweet = gson.fromJson(tweets.get(tweets.size()-1), Tweet.class);
    	
    	assertEquals(tweets.size(), 6214);
    	assertEquals(firstTweet.getId(), "263328879631036416");
    	assertEquals(lastTweet.getId(), "266921929523462145");
    }
    
    /*
    @Test
    public void writeFriendsTEST() {
    	List<String> friends = new ArrayList<String>();
		friends.add("friend1");
		friends.add("friend2");
		friends.add("friend3");
		friends.add("friend4");
		writeFriends("user", friends);    
    }
    */

}
