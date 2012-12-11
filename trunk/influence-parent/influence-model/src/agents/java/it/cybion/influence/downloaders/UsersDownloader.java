package it.cybion.influence.downloaders;

import it.cybion.influence.IO.MongoDbPersistenceFacade;
import it.cybion.influence.downloader.Token;
import it.cybion.influence.downloader.TwitterApiException;
import it.cybion.influence.downloader.TwitterApiManager;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JodaDateTimeTypeDeserializer;
import it.cybion.influence.util.TokenBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/*
 * This agent is used to download and add to mongoDb the users
 * taken from the followers and friends graph built from the food-46 users set.
 * The users are selected with these features:
 * -"non author" = the user is not one of the 46
 * -indegree>=4 (this means that 4 or more users from the 46 follow this user)
 * -outdegree>=2 (this mean that this user follows 2 or more users from the 46)
 * 
 */

public class UsersDownloader {
	
	private final static String IDS_FILE = "/home/godzy/git/TwiCinf/trunk/influence-parent/influence-model/src/test/resources/food46/id_inGE4-oGE2.txt";
	

	public static void main(String[] args) throws FileNotFoundException, TwitterApiException, UnknownHostException {
		/*
		 * TwitterApiManager
		 */
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token5.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token_edoventu.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);	
		/*
		 * Gson
		 */
		Gson gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
		.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
		.create(); 
		/*
		 * MongodbPersistenceFacade
		 */
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost", "users", "twitterUsers");
				
		List<Long> ids = readIds();
		//System.out.println(ids.size());
		for (int i=330; i<ids.size(); i++) {
			long id = ids.get(i);
			User user = twitterApiManager.getUser(id);
			System.out.println((i+1)+"/"+ids.size()+" - "+user.getScreenName()+" - "+user.getFollowersCount()+" - "+user.getFriendsCount());
			String json = gson.toJson(user);
			//System.out.println(json);
			
			DBObject dbObject = (DBObject) JSON.parse(json);
			dbObject.put("comment", "User created from the 46 food specialists. This is a user with outDegree>=2 and inDegree>=4 among the users of the graph (about 110k nodes).");
			//System.out.println(dbObject.toString());
			persistenceFacade.putDoc(dbObject);
		}
	}
	
	
	public static List<Long> readIds() throws FileNotFoundException {
		List<Long> ids = new ArrayList<Long>();
	    Scanner scanner = new Scanner(
	    			new FileInputStream(IDS_FILE));
	    try {
	      while (scanner.hasNextLine()){
	    	  ids.add(Long.parseLong(scanner.nextLine()));
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    return ids;
	}
}
