package runnable.userscreator.test;

import java.util.List;

import runnable.model.User;
import runnable.userscreator.*;

public class UserCreatorTEST {

	public static void main(String[] args)
	{
		List<User> users = UsersCreator.getFirstNUsers(1);
		List<User> followers;
		List<User> friends;
		for (User user: users)
		{
			followers = user.getFollowers();
			friends = user.getFriends();
			System.out.println("UTENTE id:"+user.getId()+" - screenName:"+user.getScreenName()+
							   " - followers="+followers.size() + 
							   " - friends="+friends.size() );			
			for (User follower: followers)
				System.out.println("\tFOLLOWER id:"+follower.getId()+" - screenName:"+follower.getScreenName());
			
			for (User friend: friends)
				System.out.println("\tFRIEND id:"+friend.getId()+" - screenName:"+friend.getScreenName());
			
		}
	}
	
}
