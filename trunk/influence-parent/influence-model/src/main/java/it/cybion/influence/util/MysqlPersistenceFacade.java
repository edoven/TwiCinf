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

public class MysqlPersistenceFacade {
	
	private static final Logger logger = Logger.getLogger(MysqlPersistenceFacade.class);
	

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
	
	/*
	private String host;
	private int port;
	private String user;
	private String password;
	private String database;
	
	public MysqlPersistenceFacade(String host, int port, String user, String password, String database) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
	}
	*/
	
	/*
	public List<String> getAllJsonTweets(long startDate, long endDate) {
		List<String> jsons = new ArrayList<String>();

		TwitterMonitoringPersistenceConfiguration persistenceConfiguration =
                new TwitterMonitoringPersistenceConfiguration(host, port,database,user,password);
		TweetDao tweetDao = new TweetDao(persistenceConfiguration.getProperties());
		List<Tweet> tweetDAOs = tweetDao.selectTweetsByQuery("", new DateTime(startDate) , new DateTime(endDate), true);
		for (Tweet tweet: tweetDAOs)
			jsons.add(tweet.getTweetJson());
		return jsons;
	}
	*/
	

	
	public static List<String> getAllJsonTweets() {
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

	
	/*
	 * This creates a single query string to insert
	 * in a shot all friends ids
	 */
	private static String createFriendsInsertQuery(String user, List<String> friends ) {
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


	public static List<String> getFollowersEnrichedUsers() {
		List<String> users = new ArrayList<String>();

		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        //TODO these parameters should be taken in the constructor,
        //together with the ones used for the twitter-monitor db connection
        String url = "jdbc:mysql://localhost:3306/twitter-users";
        String user = "root";
        String password = "qwerty";
        String query = "SELECT distinct(user_screenname) FROM `twitter-users`.followers;";

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

	
	public static void writeFollowers(String user, List<String> friends) {
		if (friends.size()==0)
			return;
		Connection con = null;
		PreparedStatement pst = null;

        String url = "jdbc:mysql://localhost:3306/twitter-users";
        String mysqlUser = "root";
        String password = "qwerty";
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
	private static String createFollowersInsertQuery(String user, List<String> friends ) {
		if (friends.size() == 1) { //TODO: remove this
			String friend = friends.get(0);
			return  "INSERT INTO `twitter-users`.`followers` (`user_screenname`, `follower_screenname`) VALUES ('"+user+"', '"+friend+"')";		
		}
		String query = "INSERT INTO `twitter-users`.`followers` (`user_screenname`, `follower_screenname`) VALUES ";
		for (String friend : friends) {
			query = query + "('"+user+"', '"+friend+"') ,";
		}
		query = query.substring(0, query.length()-2); //this remove last comma
		return query;
	}

}
