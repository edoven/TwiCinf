package it.cybion.influence.util;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlConnector {
	
	public static void main(String[] args)  {
		//getFriendsEnrichedUsers();
		writeFriend("user1", "friend2");
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
                //System.out.println(rs.getString("user_screenname"));
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
            	ex.printStackTrace();
            }
        }
        return users;
    
	}
	
	
	
	public static void writeFriends(String user, List<String> friends) {
		for (String friend : friends)
			writeFriend(user, friend);
	}
	
	
	
	
	
	
	public static void writeFriend(String user, String friend) {

		Connection con = null;
		PreparedStatement pst = null;

        String url = "jdbc:mysql://localhost:3306/twitter-users";
        String mysqlUser = "root";
        String password = "qwerty";
        String query = "INSERT INTO `twitter-users`.`friends` (`user_screenname`, `friend_screenname`) VALUES ('"+user+"', '"+friend+"')";
    	
	         
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

}
