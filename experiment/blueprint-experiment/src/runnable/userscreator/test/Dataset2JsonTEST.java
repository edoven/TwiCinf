package runnable.userscreator.test;

import java.util.HashSet;
import java.util.List;

import runnable.model.User;

public class Dataset2JsonTEST {

	public static void main(String[] args) {
		getAuthorsTEST2();		
	}
	
	
	/*
	 * invoca getAuthors() e stampa gli autori sotto forma di "id - screenName"
	 */
	public static void getAuthorsTEST1(){
		List<User> users = runnable.userscreator.Dataset2Json.getFirstNAuthors(3);
		for (int i=0; i<users.size(); i++)
		{
			User u = users.get(i);
			System.out.println(u.getId()+" - "+u.getScreenName());
		}
	}
	
	/*
	 * controlla se ci sono utenti ripetuti
	 */
	public static void getAuthorsTEST2(){
		List<User> users = runnable.userscreator.Dataset2Json.getFirstNAuthors(3);
		System.out.println("originale: "+users.size());
		HashSet<User> usersSet = new HashSet<User>(users);
		System.out.println("senza doppioni: "+usersSet.size());
	}

}
