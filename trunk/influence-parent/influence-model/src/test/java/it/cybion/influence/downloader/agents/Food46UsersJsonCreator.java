package it.cybion.influence.downloader.agents;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tinkerpop.blueprints.Graph;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import it.cybion.influence.IO.MongoDbPersistenceFacade;
import it.cybion.influence.downloader.Token;
import it.cybion.influence.downloader.TwitterApiException;
import it.cybion.influence.downloader.TwitterApiManager;
import it.cybion.influence.graph.GraphCreationException;
import it.cybion.influence.graph.UsersGraphFactory;
import it.cybion.influence.graph.UsersGraphFactoryImpl;
import it.cybion.influence.model.User;
import it.cybion.influence.util.JodaDateTimeTypeDeserializer;
import it.cybion.influence.util.OriginalJsonDeserializer;
import it.cybion.influence.util.TokenBuilder;


public class Food46UsersJsonCreator {
	
	public static void main(String[] args) throws TwitterException, GraphCreationException {
//		try {
//			downloadAllUsers();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Gson gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
		.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
		.create(); 
		try {
			List<String> usersJsons = getUsers();
			List<User> users = new ArrayList<User>();
			for (int i=0; i<usersJsons.size(); i++ ) {
				User user = gson.fromJson(usersJsons.get(i), User.class);
				System.out.println(user.getScreenName()+"-"+user.getFollowers().size()+"-"+user.getFriends().size());
				users.add(user);
			}
			UsersGraphFactory graphFactory = new UsersGraphFactoryImpl("src/test/resources/newGraph");
			graphFactory.addUsersToGraph(users);
			Graph graph = graphFactory.getGraph();
			graph.shutdown();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static List<String> getUsers() throws UnknownHostException {
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost", "users", "twitterUsers");
		return persistenceFacade.getAllUsers();
	}
	
	public static void downloadAllUsers() throws UnknownHostException, TwitterException, TwitterApiException {
		Gson gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
		.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
		.create(); 
		
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost", "users", "twitterUsers");
		List<String> screenNames = new ArrayList<String>();
		screenNames.add("elisiamenduni");
		screenNames.add("burde");
		screenNames.add("paperogiallo");
		screenNames.add("cavoletto");
		screenNames.add("giuliagraglia");
		screenNames.add("slow_food_italy");
		screenNames.add("dissapore");
		screenNames.add("DarioBressanini");
		screenNames.add("oloapmarchi");
		screenNames.add("ilgastronauta");
		screenNames.add("Cucina_Italiana");
		screenNames.add("spylong");
		screenNames.add("CarloOttaviano");
		screenNames.add("Davide_Oltolini");
		screenNames.add("GialloZafferano");
		screenNames.add("TheBreakfastRev");
		screenNames.add("ChiaraMaci");
		screenNames.add("puntarellarossa");
		screenNames.add("GigiPadovani");
		screenNames.add("LaCuochina");
		screenNames.add("cuocopersonale");
		screenNames.add("ilgastronauta");
		screenNames.add("carlo_spinelli");
		screenNames.add("italiasquisita");
		screenNames.add("fooders");
		screenNames.add("SingerFood");
		screenNames.add("Ricette20");
		screenNames.add("ele_cozzella");
		screenNames.add("cavoletto");
		screenNames.add("MartaTovaglieri");
		screenNames.add("scattidigusto");
		screenNames.add("FilLaMantia");
		screenNames.add("DavideScabin0");
		screenNames.add("paperogiallo");
		screenNames.add("giornaledelcibo");
		screenNames.add("toccodizenzero");
		screenNames.add("italiasquisita");
		screenNames.add("SingerFood");
		screenNames.add("maghetta");
		screenNames.add("burde");
		screenNames.add("CarloCracco1");
		screenNames.add("ci_polla");
		screenNames.add("cookaround");
		screenNames.add("Fiordifrolla");
		screenNames.add("FilLaMantia");
		screenNames.add("morenocedroni");
		screenNames.add("soniaperonaci");
		screenNames.add("massimobottura");
		screenNames.add("barbierichef");
		screenNames.add("LucaVissani");
		screenNames.add("DavideScabin0");
		screenNames.add("WineNewsIt");
		screenNames.add("FeudiDSGregorio");
		screenNames.add("gianlucamorino");
		screenNames = new ArrayList<String>( new HashSet<String>(screenNames));
		for (String screenName : screenNames) {
			User user = getEnrichedUser(screenName);
			String json = gson.toJson(user);
			persistenceFacade.putDoc(json);
		}
	}
	
	
	
	
	public static User getEnrichedUser(String screenName) throws TwitterApiException, TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setJSONStoreEnabled(true);
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());       
        Twitter twitter = twitterFactory.getInstance();
        
        String userJsonString = DataObjectFactory.getRawJSON(twitter.showUser(screenName));
        User user = new OriginalJsonDeserializer().deserializeJsonStringsToUser(userJsonString);
        
        List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token2.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token3.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token4.txt");
		Token consumerToken = TokenBuilder.getTokenFromFile("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = TokenBuilder.getTokensFromFilePaths(userTokenFilePaths);
		TwitterApiManager twitterApiManager = new TwitterApiManager(consumerToken, userTokens);
		
		
        List<String> friendsIds = twitterApiManager.getAllFriendsIds(screenName);
        List<User> friends = new ArrayList<User>();
        for (String friendId : friendsIds) {
        	User friend = new User(Long.parseLong(friendId));
        	friends.add(friend);
        }
        user.setFriends(friends);
        
        List<String> followersIds = twitterApiManager.getAllFollowersIds(screenName);
        List<User> followers = new ArrayList<User>();
        for (String followerId : followersIds) {
        	User follower = new User(Long.parseLong(followerId));
        	followers.add(follower);
        }
        user.setFollowers(followers);
        
        return user;
	}
	
	
	
}
