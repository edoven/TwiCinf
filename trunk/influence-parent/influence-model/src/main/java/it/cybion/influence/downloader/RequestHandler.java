package it.cybion.influence.downloader;


import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/*
 * Request handler for a single pair <application,user>
 */
public class RequestHandler {
	
	private Twitter twitter;

	
	
	public RequestHandler(Token applicationToken, Token userToken) {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(applicationToken.getTokenString())
		  .setOAuthConsumerSecret(applicationToken.getSecretString())
		  .setOAuthAccessToken(userToken.getTokenString())
		  .setOAuthAccessTokenSecret(userToken.getSecretString());
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	/*
	 * BEWARE: this ingore pagination. It only get the first 5000 friends (up to 5000).
	 */
	public FriendsRequestReply getFriendsIds(String userScreenName) {
		FriendsRequestReply reply = null;
		int limit = getLimit();
		if (limit<1)
			return null; //this could throw an exception. TODO:think about it!
		else
			reply = new FriendsRequestReply( (limit-1) , downloadFriendsIds(userScreenName));
		return reply;
	
	}
	
	private long[] downloadFriendsIds(String userScreenName) {
		IDs ids = null;
		try {
			ids = twitter.getFriendsIDs(userScreenName, -1);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		if (ids==null)
			return null;
		return ids.getIDs();
	}
	
	
	private int getLimit() {
		int limit = -1;
		try {
			return twitter.getRateLimitStatus().getRemainingHits();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return limit;
	}
}
