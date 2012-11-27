package it.cybion.influence.util;

import it.cybion.monitor.configuration.TwitterMonitoringPersistenceConfiguration;
import it.cybion.monitor.dao.TweetDao;
import it.cybion.monitor.model.Tweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class MysqlPersistenceFacade {
	
	private String host;
	private int port;
	private String mysqlUser;
	private String password;
	private String database;
		
	
	
	public MysqlPersistenceFacade(String host, int port, String mysqlUser, String password, String database) {
		super();
		this.host = host;
		this.port = port;
		this.mysqlUser = mysqlUser;
		this.password = password;
		this.database = database;
	}
	
	/*
	 * This is the only static method of this class because it's
	 * the only operation that refers to "twitter-monitor" schema.
	 * TODO: migrate the 3 tables (twitter-monitor.tweets, twitter-users.followers,
	 * twitter-users.friends) into a single schema. This will allow us to 
	 * use this method in a non-static way.
	 */
	public static List<String> getAllJsonTweets() {
		String mySqlHost = "localhost";
		int mySqlPort = 3306;
		String mySqlUser = "root";
		String mySqlPassword = "qwerty";
		String mySqlDatabase = "twitter-monitor";
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
	
	public List<String> getFriendsEnrichedUsers() {
		List<String> users = new ArrayList<String>();

		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        //TODO these parameters should be taken in the constructor,
        //together with the ones used for the twitter-monitor db connection
        String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        String query = "SELECT distinct(user_screenname) FROM `"+database+"`.friends;";

        try {
            con = DriverManager.getConnection(url, mysqlUser, password);
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
	
	public void writeFriends(String user, List<String> friends) {
		if (friends.size()==0)
			return;
		Connection con = null;
		PreparedStatement pst = null;

		String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        String query = createFriendsInsertQuery(user, friends);
    		         
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
	
	private String createFriendsInsertQuery(String user, List<String> friends ) {
		String query = "INSERT INTO `"+database+"`.`friends` (`user_screenname`, `friend_screenname`) VALUES ";
		for (String friend : friends) {
			query = query + "('"+user+"', '"+friend+"') ,";
		}
		query = query.substring(0, query.length()-2); //this removes last comma
		return query;
	}
	
	
	
	public List<String> getFollowersEnrichedUsers() {
		List<String> users = new ArrayList<String>();

		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        String query = "SELECT distinct(user_screenname) FROM `"+database+"`.followers;";

        try {
            con = DriverManager.getConnection(url, mysqlUser, password);
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
	
	public void writeFollowers(String user, List<String> friends) {
		if (friends.size()==0)
			return;
		Connection con = null;
		PreparedStatement pst = null;

		String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        String query = createFollowersInsertQuery(user, friends);
    		         
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
	
	/*
	 * This creates a single query string to insert
	 * in a shot all followers ids
	 */
	private String createFollowersInsertQuery(String user, List<String> friends ) {
		String query = "INSERT INTO `"+database+"`.`followers` (`user_screenname`, `follower_screenname`) VALUES ";
		for (String friend : friends) {
			query = query + "('"+user+"', '"+friend+"') ,";
		}
		query = query.substring(0, query.length()-2); //this removes last comma
		return query;
	}
	
	
}