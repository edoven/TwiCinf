package runnable.userscreator;

import java.util.List;

import runnable.model.User;



public class UsersCreator {
	
	
	public static List<User> getFirstNUsers(int n)
	{
		List<User> users = Dataset2Json.getFirstNAuthors(n);
		UserEnrichener.enrichUsers(users);
		return users;
	}
}
