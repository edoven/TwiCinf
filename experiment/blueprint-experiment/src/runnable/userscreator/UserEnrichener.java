package runnable.userscreator;

import java.util.ArrayList;
import java.util.List;

import runnable.model.User;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;


/*
 * 
 * Classe che serve ad "arricchire" gli utenti creati con 
 * "List<User> getAuthors()" della classe Dataset2Json.
 * Ogni utente viene arricchito con la lista dei follower 
 * e friends rappresentati come utenti (classe Utente) che hanno 
 * l'id settato in maniera corretta e lo screenanme settato ad "UNKNOWN".
 * 
 */


public class UserEnrichener {

	
	public static void enrichUsers(List<User> users)
	{
		int userSize = users.size();
		for (int i=0; i<userSize; i++) {
			System.out.println("Arricchisco l'utente " +(i+1)+" di "+userSize);
			addFollowersAndFriends(users.get(i));
		}
			
	}
	
	
	public static void addFollowersAndFriends(User u)
	{
		addFollowers(u);
		addFriends(u);
	}
	
	
	public static void addFollowers(User u)
	{
		List<User> followers = new ArrayList<User>();
		long followersIds[] = id2followers(u.getId()); //chiamata alle API di Twitter
		for (int i=0; i<followersIds.length; i++)
		{
			User follower = new User(followersIds[i]);
			followers.add(follower);
		}
		u.setFollowers(followers);
	}
	
	public static void addFriends(User u)
	{
		List<User> friends = new ArrayList<User>();
		long friendsIds[] = id2friends(u.getId()); //chiamata alle API di Twitter
		for (int i=0; i<friendsIds.length; i++)
		{
			User friend = new User(friendsIds[i]);
			friends.add(friend);
		}
		u.setFriends(friends);
	}
	
	/*
	 * sfruttando le api di twitter dall'id di un utente 
	 * viene creata la lista degli id dei follower
	 * 
	 * VENGONO PRESI AL MASSIMO I PRIMI 5000 FOLLOWER
	 */	
	public static long[] id2followers(long id)
	{
		Twitter twitter = new TwitterFactory().getInstance();
		IDs idsContainer;
		long[] ids = {};
		try{
			idsContainer = twitter.getFollowersIDs(id,-1);
			ids = idsContainer.getIDs();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

	
	/*
	 * sfruttando le api di twitter dall'id di un utente 
	 * viene creata la lista degli id dei friend
	 * 
	 * VENGONO PRESI AL MASSIMO I PRIMI 5000 FRIEND
	 */	
	public static long[] id2friends(long id)
	{
		Twitter twitter = new TwitterFactory().getInstance();
		IDs idsContainer;
		long[] ids = {};
		try{
			idsContainer = twitter.getFriendsIDs(id,-1);
			ids = idsContainer.getIDs();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

}
