package it.cybion.influencers.twitter.web.twitter4j;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class RequestHandler {

	private Twitter twitter;

	public RequestHandler(Token applicationToken, Token userToken) {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(applicationToken.getTokenString())
		  .setOAuthConsumerSecret(applicationToken.getSecretString())
		  .setOAuthAccessToken(userToken.getTokenString())
		  .setOAuthAccessTokenSecret(userToken.getSecretString())
		  .setJSONStoreEnabled(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	public int getLimit() throws TwitterApiException {
		try {
			return twitter.getRateLimitStatus().getRemainingHits();
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
	}
		
	public String getUserJson(long userId) throws TwitterApiException {
		try {
			return DataObjectFactory.getRawJSON(twitter.showUser(userId));
		} catch (TwitterException e) {
			throw new TwitterApiException(e.getMessage());
		}
	}
	
	public IDs getFollowersWithPagination(long userId, long cursor) throws TwitterException {
		return twitter.getFollowersIDs(userId, cursor);
	}
	
	
	public IDs getFriendsWithPagination(long userId, long cursor) throws TwitterException {
		return twitter.getFriendsIDs(userId, cursor);
	}
	
}
