package runnable.userscreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import runnable.model.Tweet;
import runnable.model.User;

/*
 * 
 * Partendo dal dataset di tweet salvato su MySQL, 
 * crea la lista di tweet sotto forma di singole righe JSON.
 * Dalla lista vienono costruiti tramite gson gli oggetti Tweet 
 * con il relativo User (=autore del tweet).
 * Il metodo "List<User> getAuthors()" restituisce quindi la lista
 * degli autori di tutti i tweet del dataset.
 * 
 */

public class Dataset2Json {

	
	/*
	 * NUMERO DI TWEET PRESI DAL DATASET MYSQL (x tweet = x utenti)
	 */
	public static int TWEET_RETRIEVED = 5;

	
	
	
	/*
	 * Ritorna la lista degli autori(=utenti) dei primi n twitter del dataset
	 * 
	 * Se n=-1 li ritorna tutti
	 */
	public static List<User> getFirstNAuthors(int n) {
	    Gson gson = new GsonBuilder().create();
	    List<String> jsonTweets = getFirstNJsonTweets(n);
	    List<User> users = new ArrayList<User>();
	    for (int i=0; i<jsonTweets.size(); i++)
	    {
	    	Tweet t = gson.fromJson(jsonTweets.get(i), Tweet.class);
	    	User u = t.getUser();
	    	users.add(u);
	    }
	    return users;
	}
		
	
	/*
	 * Ritorna i primi n tweet (sotto forma di stringa JSON) da MySQL
	 * 
	 * Se n=-1 ritorna tutti i tweet
	 */
	public static List<String> getFirstNJsonTweets(int n) 
	{

		List<String> tweetList = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost/twitter";
		    Connection conn = DriverManager.getConnection(url, "root", "qwerty");
		    
		    Statement statement = conn.createStatement();
		    String query;
		    if (n==-1)
		    	query = "select * from tweet";
		    else
		    	query = "select * from tweet LIMIT "+Integer.toString(n);
		    ResultSet resultSet = statement.executeQuery(query);
		    while (resultSet.next())
		    	tweetList.add(resultSet.getString("tweet_json"));
		    
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tweetList;
	}
	
	
	
	/*
	 * Ritorna n tweet (sotto forma di stringa JSON) da MySQL
	 */
	/*
	public static List<String> getFirstNJsonTweets(int n) {

		List<String> tweetList = new LinkedList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost/twitter";
		    Connection conn = DriverManager.getConnection(url, "root", "qwerty");
		    
		    Statement statement = conn.createStatement();
		    ResultSet resultSet = statement.executeQuery("select * from tweet LIMIT "+n);
		    while (resultSet.next())
		    	tweetList.add(resultSet.getString("tweet_json"));
		    
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tweetList;
	}
	*/
	
}
