package it.cybion.influence.util;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//TODO: Create a non-static version

public class MysqlConnector {
	
	private static final Logger logger = Logger.getLogger(MysqlConnector.class);
	
	
	public static void main(String[] args)  {
        //TODO move to a test (rolling back the write transaction in the end, or
        // deleting data at the end of the test:
        // every integration test run should leave the db in the initial state)
		List<String> friends = new ArrayList<String>();
		friends.add("friend1");
		friends.add("friend2");
		friends.add("friend3");
		friends.add("friend4");
		writeFriends("user", friends);
	}
	
	
	

    //TODO why not local variables?
	private static String mySqlHost = "localhost";
	private static int mySqlPort = 3306;
	private static String mySqlUser = "root";
	private static String mySqlPassword = "qwerty";
	private static String mySqlDatabase = "twitter-monitor";

    //TODO there should be a constructor that gets connection parameters,
    //and then it instantiates an instance variable tweetDao with the configuration.
    //in this way, getAllTwitterJsons calls just selectTweetsByQuery and returns jsons
    //TODO you added logic to connect to another database: pass the connection parameters
    //in the constructor. consider wrapping each parameter set in properties objects:
    //Properties twitterMonitorParams
    //Properties twitterFriendsParams

	public static List<String> getAllTwitterJsons() {
		List<String> jsons = new ArrayList<String>();

		TwitterMonitoringPersistenceConfiguration persistenceConfiguration =
                new TwitterMonitoringPersistenceConfiguration(
                		mySqlHost,
                		mySqlPort,
                        mySqlDatabase,
                        mySqlUser,
                        mySqlPassword);
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(1351616021000l) , new DateTime(1352474121000l), true);
		for (Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}
	
	
	public static List<String> getFriendsEnrichedUsers() {
		List<String> users = new ArrayList<String>();

		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        //TODO these parameters should be taken in the constructor,
        //together with the ones used for the twitter-monitor db connection
        String url = "jdbc:mysql://localhost:3306/twitter-users";
        String user = "root";
        String password = "qwerty";
        String query = "SELECT distinct(user_screenname) FROM `twitter-users`.friends;";

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
            	users.add(rs.getString("user_screenname"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                //TODO throw a checked exception
            	ex.printStackTrace();
            }
        }
        return users;   
	}
	
		
	public static void writeFriends(String user, List<String> friends) {
		if (friends.size()==0)
			return;
		Connection con = null;
		PreparedStatement pst = null;

        String url = "jdbc:mysql://localhost:3306/twitter-users";
        String mysqlUser = "root";
        String password = "qwerty";
        String query = createInsertQuery(user, friends);
    		         
        try {
            con = DriverManager.getConnection(url, mysqlUser, password);
            pst = con.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
            	ex.printStackTrace();
            }
        }		
	}

	
	private static String createInsertQuery(String user, List<String> friends ) {
		if (friends.size() == 1) {
			String friend = friends.get(0);
			return  "INSERT INTO `twitter-users`.`friends` (`user_screenname`, `friend_screenname`) VALUES ('"+user+"', '"+friend+"')";		
		}
		String query = "INSERT INTO `twitter-users`.`friends` (`user_screenname`, `friend_screenname`) VALUES ";
		for (String friend : friends) {
			query = query + "('"+user+"', '"+friend+"') ,";
		}
		query = query.substring(0, query.length()-2); //this remove last comma
		return query;
	}

}
