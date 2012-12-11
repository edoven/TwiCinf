package it.cybion.influence.downloaders;

import it.cybion.influence.IO.MongoDbPersistenceFacade;
import it.cybion.influence.downloader.Token;
import it.cybion.influence.downloader.TwitterApiException;
import it.cybion.influence.downloader.TwitterApiManager;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JodaDateTimeTypeDeserializer;
import it.cybion.influence.util.OriginalJsonDeserializer;
import it.cybion.influence.util.TokenBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import twitter4j.TwitterException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Food46top800downloader {

	public static void main(String[] args) throws FileNotFoundException, UnknownHostException, TwitterApiException, TwitterException {
		
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true).setJSONStoreEnabled(true);
//        TwitterFactory twitterFactory = new TwitterFactory(cb.build());       
//        Twitter twitter = twitterFactory.getInstance();
        
        
        
        List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token5.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token6.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
	
		Gson gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
		.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
		.create(); 
		
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost", "users", "twitterUsers");
		List<Long> userIds = getIds("src/agents/resources/food46/800-interesting.txt");

		
		//screenNames = new ArrayList<String>( new HashSet<String>(screenNames)); //this removes duplicates
		for (int i=0; i<userIds.size(); i++) {
			long userId = userIds.get(i);
			System.out.println((i+1)+"/"+userIds+"-"+userId);
			User user = getEnrichedUser(twitterApiManager, userId);
			String json = gson.toJson(user);
			System.out.println(json);
			persistenceFacade.putDoc(json);
		}

	}
	
	
	public static List<Long> getIds(String filePath) throws FileNotFoundException {
		List<Long> ids = new ArrayList<Long>();
		 Scanner scanner = new Scanner(new FileInputStream(filePath));
		 while (scanner.hasNextLine()){
			 String line = scanner.nextLine();
			 if (!line.startsWith("#"))
				 ids.add(Long.parseLong(line));		       
		 }
		 scanner.close();
		 return ids;
	}
	
	
	
	public static User getEnrichedUser(TwitterApiManager twitterApiManager, long userId) throws TwitterApiException, TwitterException {
		String userJsonString = twitterApiManager.getUserRawJson(userId);
        User user = new OriginalJsonDeserializer().deserializeJsonStringsToUser(userJsonString);
        
        List<String> friendsIds = twitterApiManager.getAllFriendsIds(userId);
        List<User> friends = new ArrayList<User>();
        for (String friendId : friendsIds) {
        	User friend = new User(Long.parseLong(friendId));
        	friends.add(friend);
        }
        user.setFriends(friends);
        
        List<String> followersIds = twitterApiManager.getAllFollowersIds(userId);
        List<User> followers = new ArrayList<User>();
        for (String followerId : followersIds) {
        	User follower = new User(Long.parseLong(followerId));
        	followers.add(follower);
        }
        user.setFollowers(followers);
        
        return user;
	}
}
